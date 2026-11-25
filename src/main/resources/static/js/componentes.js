/**
 * Componentes compartilhados por todas as páginas:
 * cabeçalho, rodapé, ilustração das bebidas e formatadores.
 */
const PAGINAS = [
    { href: "index.html", texto: "Início" },
    { href: "portfolio.html", texto: "Portfólio" },
    { href: "institucional.html", texto: "Missão, visão e valores" },
    { href: "eventos.html", texto: "Eventos" },
];

function montarCabecalho() {
    const atual = location.pathname.split("/").pop() || "index.html";
    const itens = PAGINAS.map(p =>
        `<li><a href="${p.href}" class="${p.href === atual ? "ativo" : ""}">${p.texto}</a></li>`).join("");
    document.body.insertAdjacentHTML("afterbegin", `
        <header class="topo">
            <div class="container">
                <a class="marca" href="index.html" aria-label="Brassaria Internacional - início">
                    <span class="marca-selo">1246</span>
                    <span class="marca-nome">Brassaria<small>Intranet institucional</small></span>
                </a>
                <button class="menu-botao" aria-label="Abrir menu" onclick="document.querySelector('.menu').classList.toggle('aberto')">Menu</button>
                <nav><ul class="menu">${itens}</ul></nav>
            </div>
        </header>`);
}

function montarRodape() {
    document.body.insertAdjacentHTML("beforeend", `
        <footer class="rodape">
            <div class="container">
                <div><strong>Brassaria Internacional</strong><br>Desde 1246 fermentando boas histórias.</div>
                <div>Intranet institucional<br>Dados servidos pela API Spring Boot</div>
            </div>
            <div class="container aviso-legal">Beba com moderação · Se beber, não dirija · Venda proibida para menores de 18 anos</div>
        </footer>`);
}

/** Ilustração SVG de uma garrafa/lata, colorida com a cor da bebida vinda da API. */
function ilustracaoBebida(bebida) {
    const cor = bebida.corHex || "#1E4D3B";
    const faixa = bebida.alcoolica ? "#ffffff" : "#1e4d3b";
    const textoFaixa = bebida.alcoolica ? `${formatarTeor(bebida.teorAlcoolico)}` : "ZERO";
    const corTextoFaixa = bebida.alcoolica ? "#121417" : "#ffffff";
    return `
    <svg viewBox="0 0 80 200" role="img" aria-label="Ilustração de ${escapar(bebida.nome)}">
        <rect x="32" y="4" width="16" height="10" rx="2" fill="#d9482b"/>
        <path d="M33 14 h14 v26 q0 10 10 20 q8 8 8 22 v100 q0 10 -10 10 h-30 q-10 0 -10 -10 v-100 q0 -14 8 -22 q10 -10 10 -20 z" fill="${cor}"/>
        <path d="M33 14 h14 v26 q0 10 10 20 q8 8 8 22 v100 q0 10 -10 10 h-30 q-10 0 -10 -10 v-100 q0 -14 8 -22 q10 -10 10 -20 z" fill="url(#brilho)" opacity=".35"/>
        <rect x="15" y="100" width="50" height="56" rx="4" fill="${faixa}"/>
        <text x="40" y="122" text-anchor="middle" font-family="Arial, sans-serif" font-size="9" font-weight="700" fill="${corTextoFaixa}">BRASSARIA</text>
        <line x1="22" y1="128" x2="58" y2="128" stroke="${corTextoFaixa}" stroke-width=".6" opacity=".6"/>
        <text x="40" y="144" text-anchor="middle" font-family="Arial, sans-serif" font-size="11" font-weight="700" fill="${corTextoFaixa}">${textoFaixa}</text>
        <defs>
            <linearGradient id="brilho" x1="0" x2="1">
                <stop offset="0" stop-color="#fff" stop-opacity="0"/>
                <stop offset=".25" stop-color="#fff" stop-opacity=".9"/>
                <stop offset=".4" stop-color="#fff" stop-opacity="0"/>
            </linearGradient>
        </defs>
    </svg>`;
}

function cardBebida(b) {
    const zero = !b.alcoolica;
    return `
    <article class="card bebida">
        <div class="bebida-ilustracao">${ilustracaoBebida(b)}</div>
        <div class="card-corpo">
            <span class="bebida-categoria">${escapar(b.categoriaNome)}</span>
            <h3>${escapar(b.nome)}</h3>
            <p>${escapar(b.descricao || "")}</p>
            <div class="ficha">
                <div class="${zero ? "selo-zero" : ""}"><strong>${formatarTeor(b.teorAlcoolico)}</strong><span>álcool</span></div>
                <div><strong>${b.calorias100ml ?? "—"}</strong><span>kcal/100ml</span></div>
                <div><strong>${b.volumeMl}</strong><span>ml</span></div>
            </div>
            ${b.harmonizacao ? `<p class="harmonizacao"><strong>Harmoniza com:</strong> ${escapar(b.harmonizacao)}</p>` : ""}
        </div>
    </article>`;
}

const MESES = ["jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez"];

/** Converte "2026-12-12" sem passar por fuso horário (evita mostrar o dia anterior). */
function partesData(iso) {
    const [ano, mes, dia] = iso.split("-").map(Number);
    return { dia: String(dia).padStart(2, "0"), mes: MESES[mes - 1], ano };
}

function formatarDataLonga(iso) {
    const p = partesData(iso);
    return `${p.dia} ${p.mes} ${p.ano}`;
}

function formatarTeor(teor) {
    return `${Number(teor).toLocaleString("pt-BR", { maximumFractionDigits: 1 })}%`;
}

function cardEvento(e, { destacarNovo = false } = {}) {
    const d = partesData(e.dataEvento);
    const bebidas = e.bebidasServidas.map(b =>
        `<span class="etiqueta"><i style="background:${b.corHex || "#1E4D3B"}"></i>${escapar(b.nome)}</span>`).join("");
    return `
    <article class="card">
        <div class="evento-data">
            <div class="dia">${d.dia}</div>
            <div class="mes">${d.mes}<br>${d.ano}</div>
            ${destacarNovo ? `<span class="etiqueta etiqueta-novo" style="margin-left:auto">Novo</span>` : ""}
        </div>
        <div class="card-corpo">
            <h3>${escapar(e.titulo)}</h3>
            <span class="local">${escapar(e.local)}</span>
            <p>${escapar(e.resumo)}</p>
            <button class="link-card" data-evento="${e.id}">Ler notícia completa →</button>
            <div class="etiquetas">${bebidas}</div>
        </div>
    </article>`;
}

/** Modal de detalhe do evento: busca o evento pelo ID na API (GET /api/eventos/{id}). */
function prepararModalEvento(container) {
    if (!document.getElementById("modal-evento")) {
        document.body.insertAdjacentHTML("beforeend", `
            <div class="modal" id="modal-evento" role="dialog" aria-modal="true">
                <div class="modal-caixa"><div id="modal-conteudo"></div></div>
            </div>`);
        const modal = document.getElementById("modal-evento");
        modal.addEventListener("click", ev => {
            if (ev.target === modal || ev.target.closest(".modal-fechar")) modal.classList.remove("aberto");
        });
        document.addEventListener("keydown", ev => { if (ev.key === "Escape") modal.classList.remove("aberto"); });
    }
    container.addEventListener("click", async ev => {
        const botao = ev.target.closest("[data-evento]");
        if (!botao) return;
        const modal = document.getElementById("modal-evento");
        const conteudo = document.getElementById("modal-conteudo");
        conteudo.innerHTML = `<p class="carregando">Carregando…</p>`;
        modal.classList.add("aberto");
        try {
            const e = await Api.evento(botao.dataset.evento);
            const d = partesData(e.dataEvento);
            conteudo.innerHTML = `
                <div class="evento-data">
                    <div class="dia">${d.dia}</div><div class="mes">${d.mes}<br>${d.ano}</div>
                    <button class="modal-fechar" style="margin-left:auto">Fechar</button>
                </div>
                <div class="card-corpo">
                    <h2>${escapar(e.titulo)}</h2>
                    <span class="local">${escapar(e.local)}</span>
                    <p style="color:var(--texto)">${escapar(e.conteudo).replace(/\n/g, "<br>")}</p>
                    ${e.bebidasServidas.length ? `<strong>Bebidas servidas</strong>` : ""}
                    <div class="etiquetas" style="margin-top:0">${e.bebidasServidas.map(b =>
                        `<span class="etiqueta"><i style="background:${b.corHex}"></i>${escapar(b.nome)}</span>`).join("")}</div>
                </div>`;
        } catch (erro) {
            conteudo.innerHTML = `<div class="card-corpo">${avisoErro(erro.message)}</div>`;
        }
    });
}

function avisoErro(msg) {
    return `<div class="aviso aviso-erro" style="grid-column:1/-1">${escapar(msg)}</div>`;
}

/** Evita injeção de HTML com dados vindos da API (proteção contra XSS). */
function escapar(texto) {
    return String(texto ?? "").replace(/[&<>"']/g, c =>
        ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c]));
}

montarCabecalho();
document.addEventListener("DOMContentLoaded", montarRodape);
