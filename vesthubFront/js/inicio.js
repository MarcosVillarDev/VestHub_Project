/**
 * Tela inicial (após o login).
 *
 *  1. Exige login (sem login, volta para login.html).
 *  2. Busca o ranking do usuário (GET /ranking).
 *  3. Com notas cadastradas: mostra os 3 cursos com maior chance.
 *     Sem notas: mostra um convite para cadastrar.
 */
(function () {
  "use strict";

  exigirLogin();

  const lista       = document.getElementById("listaPrevia");
  const vazia       = document.getElementById("previaVazia");
  const carregando  = document.getElementById("previaCarregando");
  const botao       = document.getElementById("botaoPrevia");
  const mensagemApi = document.getElementById("mensagemApi");

  const QUANTIDADE = 3; // quantos cursos aparecem na prévia

  // Saudação no topo da prévia e no menu lateral
  const usuario = usuarioLogado();
  if (usuario) {
    const primeiroNome = usuario.nome.split(" ")[0];
    document.getElementById("saudacaoPrevia").textContent = "Olá, " + primeiroNome + "!";
    document.getElementById("saudacaoMenu").textContent   = "Olá, " + primeiroNome + "!";
  }
  document.getElementById("botaoSair").addEventListener("click", sair);

  /** Formata um número no padrão brasileiro, sem casas decimais desnecessárias (29.00 → "29"). */
  function formatar(numero) {
    return Number(numero).toLocaleString("pt-BR", { maximumFractionDigits: 2 });
  }

  /** Monta uma linha da prévia. */
  function criarItem(curso, posicao) {
    const acima = Number(curso.diferenca) >= 0;

    const item = document.createElement("li");
    item.className = "previa-item";

    const numero = document.createElement("span");
    numero.className = "previa-posicao";
    numero.textContent = posicao;

    const info = document.createElement("div");
    info.className = "previa-curso";

    const nome = document.createElement("span");
    nome.className = "previa-curso-nome";
    nome.textContent = curso.curso;

    const detalhe = document.createElement("span");
    detalhe.className = "previa-curso-detalhe";
    detalhe.textContent = curso.universidade + " · " + curso.campus + " · corte " + formatar(curso.notaCorte);

    info.append(nome, detalhe);

    const selo = document.createElement("span");
    selo.className = "previa-diferenca " + (acima ? "acima" : "abaixo");

    const valor = document.createElement("strong");
    valor.textContent = (acima ? "▲ +" : "▼ ") + formatar(curso.diferenca);

    const legenda = document.createElement("small");
    legenda.textContent = acima ? "acima do corte" : "abaixo do corte";

    selo.append(valor, legenda);

    item.append(numero, info, selo);
    return item;
  }

  /** Busca o ranking e mostra a prévia (ou o convite). */
  async function carregar() {
    try {
      const ranking = await api("/ranking");
      if (!ranking) return; // 401: o api.js já mandou para o login

      carregando.hidden = true;

      if (ranking.length === 0) {
        // Sem notas: convite para cadastrar
        vazia.hidden = false;
        botao.textContent = "Cadastrar notas";
        botao.href = "notas.html";
      } else {
        // O ranking já vem ordenado da maior para a menor chance
        const primeiros = ranking.slice(0, QUANTIDADE);
        lista.replaceChildren(...primeiros.map((curso, i) => criarItem(curso, i + 1)));
        lista.hidden = false;
      }

      botao.hidden = false;
    } catch (erro) {
      carregando.hidden = true;
      mostrarMensagem(mensagemApi, erro.message, "danger");
    }
  }

  carregar();
})();