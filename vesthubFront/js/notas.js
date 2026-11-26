/**
 * Minhas notas.
 *
 *  1. Exige login.
 *  2. Busca as universidades, as notas já cadastradas e as modalidades de cada universidade.
 *  3. Monta um card por universidade, com a modalidade e a nota preenchidas (se existirem).
 *  4. Salvar   → PUT /desempenhos (cria ou atualiza, nunca duplica).
 *     Remover  → DELETE /desempenhos/{universidadeId}.
 */
(function () {
  "use strict";

  exigirLogin();

  const lista       = document.getElementById("listaNotas");
  const mensagemApi = document.getElementById("mensagemApi");

  // Explica a escala de cada vestibular: a nota precisa estar na mesma escala da nota de corte
  const ESCALAS = {
    USP:     "Nº de acertos na 1ª fase da Fuvest (de 0 a 90).",
    UNICAMP: "Nº de acertos na 1ª fase da Unicamp (de 0 a 72).",
    UNESP:   "Nº de acertos na 1ª fase da Unesp (de 0 a 90).",
  };

  // Saudação e botão "Sair" do menu lateral
  const usuario = usuarioLogado();
  if (usuario) {
    document.getElementById("saudacaoMenu").textContent = "Olá, " + usuario.nome.split(" ")[0] + "!";
  }
  document.getElementById("botaoSair").addEventListener("click", sair);

  /** Cria um elemento com classe e texto (atalho para não repetir código). */
  function elemento(tag, classe, texto) {
    const el = document.createElement(tag);
    if (classe) el.className = classe;
    if (texto !== undefined) el.textContent = texto;
    return el;
  }

  /** Mostra uma mensagem dentro do card. */
  function avisarNoCard(card, texto, tipo) {
    const aviso = card.querySelector(".nota-feedback");
    aviso.textContent = texto;
    aviso.className = "nota-feedback alert alert-" + tipo;
  }

  /** Mostra ou esconde o selo "Nota salva" e o botão "Remover". */
  function marcarSalva(card, salva) {
    card.querySelector(".nota-salva").hidden = !salva;
    card.querySelector(".btn-remover").hidden = !salva;
  }

  /** Monta o card de uma universidade. */
  function criarCard(universidade, modalidades, desempenho) {
    const id = universidade.id;

    const card = elemento("article", "nota-card");

    // Topo: sigla, nome e selo
    const topo = elemento("div", "nota-card-topo");
    const titulos = elemento("div");
    titulos.append(
      elemento("h2", "nota-card-sigla", universidade.sigla),
      elemento("p", "nota-card-nome", universidade.nome)
    );
    const selo = elemento("span", "nota-salva");
    const iconeSelo = elemento("i", "bi bi-check-circle-fill");
    iconeSelo.setAttribute("aria-hidden", "true");
    selo.append(iconeSelo, document.createTextNode(" Nota salva"));
    topo.append(titulos, selo);

    // Formulário
    const form = elemento("form");
    form.noValidate = true;

    const campos = elemento("div", "nota-campos");

    // Campo: modalidade
    const grupoModalidade = elemento("div");
    const labelModalidade = elemento("label", "form-label", "Modalidade");
    labelModalidade.htmlFor = "modalidade-" + id;
    const select = elemento("select", "form-select");
    select.id = "modalidade-" + id;
    select.required = true;
    const opcaoVazia = elemento("option", null, "Escolha a modalidade");
    opcaoVazia.value = "";
    select.append(opcaoVazia);
    modalidades.forEach((m) => {
      const opcao = elemento("option", null, m.sigla + " — " + m.nome);
      opcao.value = m.id;
      select.append(opcao);
    });
    const feedbackModalidade = elemento("div", "invalid-feedback", "Escolha a modalidade.");
    grupoModalidade.append(labelModalidade, select, feedbackModalidade);

    // Campo: nota
    const grupoNota = elemento("div");
    const labelNota = elemento("label", "form-label", "Nota");
    labelNota.htmlFor = "nota-" + id;
    const inputNota = elemento("input", "form-control");
    inputNota.id = "nota-" + id;
    inputNota.type = "number";
    inputNota.inputMode = "decimal";
    inputNota.min = "0";
    inputNota.max = universidade.notaMaxima; // ex.: 90 na USP, 72 na Unicamp
    inputNota.step = "0.01";
    inputNota.required = true;
    inputNota.placeholder = "Ex.: 62";
    const feedbackNota = elemento("div", "invalid-feedback",
    "Informe uma nota de 0 a " + universidade.notaMaxima + ".");
    const dica = elemento("p", "nota-dica", ESCALAS[universidade.sigla] || "Use a mesma escala da nota de corte.");
    grupoNota.append(labelNota, inputNota, feedbackNota, dica);

    campos.append(grupoModalidade, grupoNota);

    // Aviso do card e botões
    const aviso = elemento("div", "nota-feedback d-none");
    aviso.setAttribute("role", "status");

    const acoes = elemento("div", "nota-acoes");
    const botaoSalvar = elemento("button", "btn btn-login", "Salvar");
    botaoSalvar.type = "submit";
    const botaoRemover = elemento("button", "btn-remover", "Remover");
    botaoRemover.type = "button";
    acoes.append(botaoSalvar, botaoRemover);

    form.append(campos, aviso, acoes);
    card.append(topo, form);

    // Preenche com a nota já cadastrada, se existir
    if (desempenho) {
      select.value = desempenho.modalidadeId;
      inputNota.value = desempenho.nota;
    }
    marcarSalva(card, Boolean(desempenho));

    // Comportamento
    form.addEventListener("submit", (evento) => {
      evento.preventDefault();
      salvar(card, universidade, select, inputNota, botaoSalvar);
    });
    botaoRemover.addEventListener("click", () => remover(card, universidade, select, inputNota, botaoRemover));

    return card;
  }

  /** Valida os campos (mesma lógica das telas de login e cadastro). */
  function validarCampo(campo) {
    const valido = campo.checkValidity();
    campo.classList.toggle("is-invalid", !valido);
    campo.classList.toggle("is-valid", valido);
    return valido;
  }

  /** PUT /desempenhos: cria ou atualiza a nota da universidade. */
  async function salvar(card, universidade, select, inputNota, botao) {
    const modalidadeOk = validarCampo(select);
    const notaOk = validarCampo(inputNota);
    if (!modalidadeOk || !notaOk) return;

    botao.disabled = true;
    try {
      await api("/desempenhos", {
        method: "PUT",
        body: {
          universidadeId: universidade.id,
          modalidadeId: Number(select.value),
          nota: inputNota.valueAsNumber,
        },
      });
      marcarSalva(card, true);
      avisarNoCard(card, "Nota salva! Seu ranking já foi atualizado.", "success");
    } catch (erro) {
      avisarNoCard(card, erro.message, "danger");
    } finally {
      botao.disabled = false;
    }
  }

  /** DELETE /desempenhos/{universidadeId}: remove a nota da universidade. */
  async function remover(card, universidade, select, inputNota, botao) {
    botao.disabled = true;
    try {
      await api("/desempenhos/" + universidade.id, { method: "DELETE" });
      select.value = "";
      inputNota.value = "";
      select.classList.remove("is-valid", "is-invalid");
      inputNota.classList.remove("is-valid", "is-invalid");
      marcarSalva(card, false);
      avisarNoCard(card, "Nota removida.", "secondary");
    } catch (erro) {
      avisarNoCard(card, erro.message, "danger");
    } finally {
      botao.disabled = false;
    }
  }

  /** Busca os dados e monta a tela. */
  async function carregar() {
    try {
      const [universidades, desempenhos] = await Promise.all([
        api("/universidades"),
        api("/desempenhos"),
      ]);
      if (!universidades || !desempenhos) return; // 401: o api.js já mandou para o login

      // As modalidades de cada universidade, todas ao mesmo tempo
      const modalidadesPorUniversidade = await Promise.all(
        universidades.map((u) => api("/universidades/" + u.id + "/modalidades"))
      );

      const cards = universidades.map((u, i) => {
        const desempenho = desempenhos.find((d) => d.universidadeId === u.id);
        return criarCard(u, modalidadesPorUniversidade[i], desempenho);
      });

      lista.replaceChildren(...cards);
    } catch (erro) {
      lista.replaceChildren();
      mostrarMensagem(mensagemApi, erro.message, "danger");
    }
  }

  carregar();
})();
