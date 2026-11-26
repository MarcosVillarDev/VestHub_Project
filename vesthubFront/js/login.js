(function () {
        "use strict";
        redirecionarSeLogado();
        const form        = document.getElementById("loginForm");
        const emailInput  = document.getElementById("email");
        const senhaInput  = document.getElementById("senha");
        const botao       = form.querySelector('button[type="submit"]');
        const mensagemApi = document.getElementById("mensagemApi");

        /** Aplica feedback visual a um campo (mesma lógica do cadastro). */
        function validarCampo(input) {
          if (!input.checkValidity()) {
            input.classList.remove("is-valid");
            input.classList.add("is-invalid");
            return false;
          }
          input.classList.remove("is-invalid");
          input.classList.add("is-valid");
          return true;
        }

        emailInput.addEventListener("blur", () => validarCampo(emailInput));
        senhaInput.addEventListener("blur", () => validarCampo(senhaInput));

        form.addEventListener("submit", async function (event) {
          event.preventDefault();

          const emailValido = validarCampo(emailInput);
          const senhaValida = validarCampo(senhaInput);
          if (!emailValido || !senhaValida) {
            form.querySelector(".is-invalid").focus();
            return;
          }

          // Monta o cabeçalho do HTTP Basic com o que o usuário digitou
          const auth = montarAuth(emailInput.value.trim().toLowerCase(), senhaInput.value);

          botao.disabled = true;

          try {
            // A API confere e-mail e senha; se der certo, devolve { id, nome, email }
            const usuario = await api("/auth/login", { auth: auth });
            salvarLogin(auth, usuario);
            window.location.replace("inicio.html");
          } catch (erro) {
            const texto = erro.status === 401 ? "E-mail ou senha inválidos." : erro.message;
            mostrarMensagem(mensagemApi, texto, "danger");
            botao.disabled = false;
          }
        });
      })();