/**
 * api.js — funções compartilhadas para conversar com a API do VestHub.
 * Todas as páginas carregam este arquivo antes do próprio script.
 */

// Endereço da API. Se ela for publicada, só esta linha muda.
const API_URL = "http://localhost:8080";

/* ---------------------------------------------------------
   Login guardado no navegador (sessionStorage)
   --------------------------------------------------------- */

/**
 * Converte um texto para Base64 aceitando acentos (o btoa puro não aceita).
 * @param {string} texto
 * @returns {string}
 */
function paraBase64(texto) {
  const bytes = new TextEncoder().encode(texto);
  let binario = "";
  bytes.forEach((b) => (binario += String.fromCharCode(b)));
  return btoa(binario);
}

/**
 * Monta o cabeçalho do HTTP Basic: "Basic " + base64("email:senha").
 */
function montarAuth(email, senha) {
  return "Basic " + paraBase64(email + ":" + senha);
}

/** Guarda o login depois que a API confirmou e-mail e senha. */
function salvarLogin(auth, usuario) {
  sessionStorage.setItem("vesthub_auth", auth);
  sessionStorage.setItem("vesthub_usuario", JSON.stringify(usuario));
}

/** Devolve o usuário logado ({ id, nome, email }) ou null. */
function usuarioLogado() {
  const salvo = sessionStorage.getItem("vesthub_usuario");
  return salvo ? JSON.parse(salvo) : null;
}


/** Apaga o login e volta para a tela de entrada. */
function sair() {
  sessionStorage.removeItem("vesthub_auth");
  sessionStorage.removeItem("vesthub_usuario");
  // replace: troca a página atual no histórico em vez de empilhar uma nova
  window.location.replace("login.html");
}

/** Para páginas que exigem login: sem login, volta para a entrada. */
function exigirLogin() {
  if (!sessionStorage.getItem("vesthub_auth")) {
    window.location.replace("login.html");
  }

  // Se a página veio da memória do navegador (botão voltar),
  // o código acima não roda de novo. O evento "pageshow" roda.
  window.addEventListener("pageshow", (evento) => {
    if (evento.persisted && !sessionStorage.getItem("vesthub_auth")) {
      window.location.replace("login.html");
    }
  });
}

/** Para login e cadastro: quem já está logado vai direto para o início. */
function redirecionarSeLogado() {
  if (sessionStorage.getItem("vesthub_auth")) {
    window.location.replace("inicio.html");
  }
}


/* ---------------------------------------------------------
   Chamada à API
   --------------------------------------------------------- */

/**
 * Faz uma requisição à API e devolve o JSON da resposta.
 * Em caso de erro, lança um Error com a mensagem da API ({ "mensagem": "..." }).
 *
 * @param {string} caminho - ex.: "/universidades"
 * @param {object} [opcoes]
 * @param {string} [opcoes.method="GET"] - GET, POST, PUT ou DELETE
 * @param {object} [opcoes.body] - objeto que vira JSON no corpo
 * @param {string} [opcoes.auth] - cabeçalho de login específico (usado no login)
 */
async function api(caminho, opcoes = {}) {
  const headers = {};

  // Login: o informado (tela de login) ou o guardado no navegador
  const auth = opcoes.auth || sessionStorage.getItem("vesthub_auth");
  if (auth) headers["Authorization"] = auth;

  if (opcoes.body !== undefined) headers["Content-Type"] = "application/json";

  let resposta;
  try {
    resposta = await fetch(API_URL + caminho, {
      method: opcoes.method || "GET",
      headers: headers,
      body: opcoes.body !== undefined ? JSON.stringify(opcoes.body) : undefined,
    });
  } catch (e) {
    // A API não respondeu: desligada ou endereço errado
    throw new Error("Não foi possível conectar ao servidor.");
  }

  // 401 numa página logada = login expirou ou foi apagado
  if (resposta.status === 401 && !opcoes.auth) {
    sair();
    return;
  }

  // 204 (No Content): sucesso sem corpo, como no DELETE
  if (resposta.status === 204) return null;

  const dados = await resposta.json().catch(() => null);

  if (!resposta.ok) {
    const erro = new Error(dados && dados.mensagem ? dados.mensagem : "Algo deu errado. Tente novamente.");
    erro.status = resposta.status;
    throw erro;
  }

  return dados;
}

/**
 * Mostra uma mensagem num alerta do Bootstrap.
 * @param {HTMLElement} elemento - a div do alerta
 * @param {string} texto - a mensagem
 * @param {string} tipo - "success" (verde) ou "danger" (vermelho)
 */
function mostrarMensagem(elemento, texto, tipo) {
  elemento.textContent = texto;
  elemento.className = "alert alert-" + tipo;
}