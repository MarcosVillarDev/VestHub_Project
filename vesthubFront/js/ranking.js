/**
 * Meu ranking.
 *
 *  1. Exige login.
 *  2. Busca as universidades (para o filtro) e o ranking (GET /ranking).
 *  3. Mostra todos os cursos, da maior para a menor chance, com um divisor
 *     entre os que estão acima e abaixo da nota de corte.
 *  4. O filtro de universidade chama GET /ranking?universidadeId=...
 */
(function () {
  "use strict";

  exigirLogin();

  const lista       = document.getElementById("listaRanking");
  const vazio       = document.getElementById("rankingVazio");
  const carregando  = document.getElementById("rankingCarregando");
  const resumo      = document.getElementById("resumo");
  const filtro      = document.getElementById("filtroUniversidade");
  const mensagemApi = document.getElementById("mensagemApi");

  const TURNOS = {
    INTEGRAL: "Integral",
    MATUTINO: "Matutino",
    VESPERTINO: "Vespertino",
    NOTURNO: "Noturno",
  };

  // Saudação e botão "Sair" do menu lateral
  const usuario = usuarioLogado();
  if (usuario) {
    document.getElementById("saudacaoMenu").textContent = "Olá, " + usuario.nome.split(" ")[0] + "!";
  }
  document.getElementById("botaoSair").addEventListener("click", sair);

  /** Formata um número no padrão brasileiro, sem casas decimais desnecessárias (29.00 → "29"). */
  function formatar(numero) {
    return Number(numero).toLocaleString("pt-BR", { maximumFractionDigits: 2 });
  }

  /** Cria um elemento com classe e texto. */
  function elemento(tag, classe, texto) {
    const el = document.createElement(tag);
    if (classe) el.className = classe;
    if (texto !== undefined) el.textContent = texto;
    return el;
  }

  /** Linha de um curso. */
  function criarItem(curso, posicao) {
    const acima = Number(curso.diferenca) >= 0;

    const item = elemento("li", "ranking-item");

    const info = elemento("div", "ranking-curso");
    info.append(
      elemento("span", "ranking-curso-nome", curso.curso),
      elemento("span", "ranking-curso-detalhe",
        curso.universidade + " · " + curso.campus + " · " + (TURNOS[curso.turno] || curso.turno)),
      elemento("span", "ranking-curso-notas",
        "Modalidade " + curso.modalidade + " · corte " + formatar(curso.notaCorte) +
        " · sua nota " + formatar(curso.notaUsuario))
    );

    const selo = elemento("span", "ranking-diferenca " + (acima ? "acima" : "abaixo"));
    selo.append(
      elemento("strong", null, (acima ? "▲ +" : "▼ ") + formatar(curso.diferenca)),
      elemento("small", null, acima ? "acima do corte" : "abaixo do corte")
    );

    item.append(elemento("span", "ranking-posicao", posicao), info, selo);
    return item;
  }

  /** Divisor entre os grupos "acima" e "abaixo" do corte. */
  function criarDivisor(texto) {
    const divisor = elemento("li", "ranking-divisor", texto);
    divisor.setAttribute("aria-hidden", "true");
    return divisor;
  }

  /** Monta a lista com os divisores. O ranking já vem ordenado pela API. */
  function mostrarRanking(ranking) {
    const itens = [];
    const totalAcima = ranking.filter((c) => Number(c.diferenca) >= 0).length;

    ranking.forEach((curso, i) => {
      if (i === 0 && totalAcima > 0) itens.push(criarDivisor("Acima da nota de corte"));
      if (i === totalAcima) itens.push(criarDivisor("Abaixo da nota de corte"));
      itens.push(criarItem(curso, i + 1));
    });

    lista.replaceChildren(...itens);

    // Resumo: "8 de 12 cursos acima da nota de corte"
    const destaque = elemento("strong", null, totalAcima + " de " + ranking.length);
    resumo.replaceChildren(destaque, document.createTextNode(" cursos acima da nota de corte"));

    lista.hidden = false;
    resumo.hidden = false;
    vazio.hidden = true;
  }

  /** Busca o ranking (com ou sem filtro). */
  async function carregarRanking() {
    const caminho = filtro.value ? "/ranking?universidadeId=" + filtro.value : "/ranking";
    try {
      const ranking = await api(caminho);
      if (!ranking) return; // 401: o api.js já mandou para o login

      carregando.hidden = true;

      if (ranking.length === 0) {
        lista.hidden = true;
        resumo.hidden = true;
        // Sem filtro e sem resultado = o usuário ainda não tem notas
        if (!filtro.value) {
          vazio.hidden = false;
        } else {
          resumo.textContent = "Você ainda não cadastrou nota nesta universidade.";
          resumo.hidden = false;
        }
        return;
      }

      mostrarRanking(ranking);
    } catch (erro) {
      carregando.hidden = true;
      mostrarMensagem(mensagemApi, erro.message, "danger");
    }
  }

  /** Preenche o filtro com as universidades. */
  async function carregarFiltro() {
    try {
      const universidades = await api("/universidades");
      (universidades || []).forEach((u) => {
        const opcao = elemento("option", null, u.sigla);
        opcao.value = u.id;
        filtro.append(opcao);
      });
    } catch (erro) {
      // O filtro é opcional: se falhar, o ranking continua funcionando sem ele
    }
  }

  filtro.addEventListener("change", carregarRanking);

  carregarFiltro();
  carregarRanking();
})();
