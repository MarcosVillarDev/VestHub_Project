/**
 * Cadastro de usuário.
 *
 * Estratégia:
 *  1. Intercepta o evento "submit" com preventDefault().
 *  2. Valida cada campo individualmente com checkValidity().
 *  3. Verifica se as duas senhas coincidem.
 *  4. Aplica classes Bootstrap "is-invalid" / "is-valid".
 *  5. Se tudo válido, envia para a API (POST /auth/registrar).
 */
(function () {
  "use strict";
  
  redirecionarSeLogado();
  const form           = document.getElementById("registerForm");
  const nomeInput      = document.getElementById("nome");
  const emailInput     = document.getElementById("email");
  const senhaInput     = document.getElementById("senha");
  const confirmarInput = document.getElementById("confirmarSenha");
  const botao          = form.querySelector('button[type="submit"]');
  const mensagemApi    = document.getElementById("mensagemApi");

  /**
   * Aplica feedback visual a um input individual.
   * @param {HTMLInputElement} input - O campo a ser avaliado.
   * @param {boolean} [forceInvalid] - Força estado inválido (ex: senhas diferentes).
   * @returns {boolean} true se válido, false se inválido.
   */
  function validarCampo(input, forceInvalid = false) {
    if (!input.checkValidity() || forceInvalid) {
      input.classList.remove("is-valid");
      input.classList.add("is-invalid");
      return false;
    }
    input.classList.remove("is-invalid");
    input.classList.add("is-valid");
    return true;
  }

  /* Validação em tempo real ao sair do campo (blur) */
  nomeInput.addEventListener("blur",  () => validarCampo(nomeInput));
  emailInput.addEventListener("blur", () => validarCampo(emailInput));
  senhaInput.addEventListener("blur", () => validarCampo(senhaInput));

  confirmarInput.addEventListener("blur", () => {
    const senhasIguais = confirmarInput.value === senhaInput.value;
    validarCampo(confirmarInput, !senhasIguais);
  });

  /* Revalida confirmação ao alterar a senha original */
  senhaInput.addEventListener("input", () => {
    if (confirmarInput.classList.contains("is-invalid") ||
        confirmarInput.classList.contains("is-valid")) {
      const senhasIguais = confirmarInput.value === senhaInput.value;
      validarCampo(confirmarInput, !senhasIguais);
    }
  });

  /* Intercepta o envio do formulário e chama a API */
  form.addEventListener("submit", async function (event) {
    event.preventDefault();

    const senhasIguais  = confirmarInput.value === senhaInput.value;

    const nomeValido    = validarCampo(nomeInput);
    const emailValido   = validarCampo(emailInput);
    const senhaValida   = validarCampo(senhaInput);
    const confirmValida = validarCampo(confirmarInput, !senhasIguais);

    if (!(nomeValido && emailValido && senhaValida && confirmValida)) {
      // ❌ Falha: foca o primeiro campo inválido
      const primeiroCampoInvalido = form.querySelector(".is-invalid");
      if (primeiroCampoInvalido) primeiroCampoInvalido.focus();
      return;
    }

    botao.disabled = true; // evita clique duplo enquanto a API responde

    try {
      await api("/auth/registrar", {
        method: "POST",
        body: {
          nome:  nomeInput.value.trim(),
          email: emailInput.value.trim(),
          senha: senhaInput.value,
        },
      });

      // ✅ Sucesso: conta criada, vai para o login
      mostrarMensagem(mensagemApi, "Conta criada! Redirecionando para o login...", "success");
      setTimeout(() => (window.location.href = "login.html"), 1500);
    } catch (erro) {
      // A mensagem vem da API, ex.: "Já existe uma conta com este e-mail"
      mostrarMensagem(mensagemApi, erro.message, "danger");
      botao.disabled = false;
    }
  });
})();