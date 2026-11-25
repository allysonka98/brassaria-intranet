/**
 * Cliente da API REST da Brassaria.
 * Todas as informações exibidas no site vêm daqui.
 *
 * Se o site for servido pelo próprio Spring Boot (http://localhost:8080), a API
 * é chamada na mesma origem. Se for aberto por outro servidor (ex.: Live Server,
 * porta 5500) ou direto do disco, as chamadas vão para http://localhost:8080.
 */
const API_BASE = (location.port === "8080") ? "/api" : "http://localhost:8080/api";

class ErroApi extends Error {
    constructor(status, corpo) {
        super(corpo?.mensagem || `Erro ${status} ao acessar a API.`);
        this.status = status;
        this.campos = corpo?.campos || {};
    }
}

async function requisitar(caminho, opcoes = {}) {
    let resposta;
    try {
        resposta = await fetch(API_BASE + caminho, {
            headers: { "Content-Type": "application/json" },
            ...opcoes,
        });
    } catch (e) {
        throw new ErroApi(0, { mensagem: "Não foi possível conectar à API. Verifique se o back-end está rodando em http://localhost:8080." });
    }
    if (resposta.status === 204) return null;
    const corpo = await resposta.json().catch(() => null);
    if (!resposta.ok) throw new ErroApi(resposta.status, corpo);
    return corpo;
}

const Api = {
    empresa: () => requisitar("/empresa"),

    categorias: () => requisitar("/categorias"),

    bebidas: (filtros = {}) => {
        const params = new URLSearchParams();
        Object.entries(filtros).forEach(([k, v]) => {
            if (v !== undefined && v !== null && v !== "") params.append(k, v);
        });
        const qs = params.toString();
        return requisitar("/bebidas" + (qs ? "?" + qs : ""));
    },

    eventos: () => requisitar("/eventos"),
    eventosRecentes: () => requisitar("/eventos/recentes"),
    evento: (id) => requisitar(`/eventos/${id}`),
    criarEvento: (dados) => requisitar("/eventos", { method: "POST", body: JSON.stringify(dados) }),
    excluirEvento: (id) => requisitar(`/eventos/${id}`, { method: "DELETE" }),
};
