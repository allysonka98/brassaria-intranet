package br.com.brassaria.intranet.config;

import br.com.brassaria.intranet.model.*;
import br.com.brassaria.intranet.repository.BebidaRepository;
import br.com.brassaria.intranet.repository.CategoriaRepository;
import br.com.brassaria.intranet.repository.EmpresaRepository;
import br.com.brassaria.intranet.repository.EventoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Popula o banco na primeira execução (apenas se ele estiver vazio),
 * para que o site já abra com conteúdo de exemplo.
 */
@Component
public class CargaInicialDados implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CargaInicialDados.class);

    private final EmpresaRepository empresaRepository;
    private final CategoriaRepository categoriaRepository;
    private final BebidaRepository bebidaRepository;
    private final EventoRepository eventoRepository;

    private final Map<String, Bebida> bebidas = new HashMap<>();

    public CargaInicialDados(EmpresaRepository empresaRepository,
                             CategoriaRepository categoriaRepository,
                             BebidaRepository bebidaRepository,
                             EventoRepository eventoRepository) {
        this.empresaRepository = empresaRepository;
        this.categoriaRepository = categoriaRepository;
        this.bebidaRepository = bebidaRepository;
        this.eventoRepository = eventoRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (empresaRepository.count() > 0) {
            return;
        }
        log.info("Banco vazio: carregando dados iniciais da Brassaria Internacional...");
        criarEmpresa();
        criarPortfolio();
        criarEventos();
        log.info("Dados iniciais carregados.");
    }

    private void criarEmpresa() {
        Empresa e = new Empresa();
        e.setNome("Brassaria Internacional");
        e.setAnoFundacao(1246);
        e.setSlogan("780 anos fermentando boas histórias.");
        e.setHistoria("Fundada em 1246 por uma família de mestres cervejeiros, a Brassaria Internacional "
                + "atravessou séculos guardando receitas, técnicas de fermentação e o hábito de reunir pessoas "
                + "em volta de uma boa bebida. Hoje, aos 780 anos, une tradição e ciência para oferecer um "
                + "portfólio completo — de lagers clássicas a fermentados sem álcool — com informação clara "
                + "para que cada cliente escolha o que faz bem para si.");
        e.setMissao("Produzir bebidas fermentadas de alta qualidade, alcoólicas ou não, que celebrem bons "
                + "momentos e respeitem a saúde de quem consome, com transparência nutricional, incentivo ao "
                + "consumo moderado e opções para todos os estilos de vida.");
        e.setVisao("Ser, até 2030, a fabricante de bebidas mais lembrada do Brasil pelo equilíbrio entre "
                + "tradição, sabor e bem-estar, com metade do portfólio composta por opções sem álcool ou "
                + "de baixo teor alcoólico.");
        e.adicionarValor(new Valor("Consumo consciente",
                "Incentivamos o beber com moderação, a intercalar com água e a nunca combinar álcool e direção."));
        e.adicionarValor(new Valor("Transparência",
                "Calorias, teor alcoólico e ingredientes de cada bebida estão sempre visíveis no rótulo e no site."));
        e.adicionarValor(new Valor("Bem-estar para todos",
                "Ampliamos continuamente a linha sem álcool e os fermentados naturais, para que ninguém fique de fora do brinde."));
        e.adicionarValor(new Valor("Tradição que evolui",
                "Respeitamos 780 anos de receitas, mas testamos, medimos e melhoramos todos os dias."));
        e.adicionarValor(new Valor("Juventude e engajamento",
                "Ouvimos nossos clientes e colaboradores e construímos com eles a Brassaria dos próximos séculos."));
        empresaRepository.save(e);
    }

    private void criarPortfolio() {
        Categoria lagers = categoriaRepository.save(new Categoria("Lagers",
                "Cervejas de baixa fermentação, leves e refrescantes."));
        Categoria especiais = categoriaRepository.save(new Categoria("Ales e Especiais",
                "Estilos de alta fermentação, com mais corpo, aroma e personalidade."));
        Categoria semAlcool = categoriaRepository.save(new Categoria("Sem Álcool",
                "Todo o sabor da Brassaria com até 0,5% de teor alcoólico."));
        Categoria fermentados = categoriaRepository.save(new Categoria("Fermentados Naturais",
                "Kombuchas, sidras e hidroméis produzidos com frutas, chás e mel."));

        bebida(lagers, "Brassaria Pilsen 1246", "A receita que deu origem à casa: leve, dourada e de amargor suave.",
                true, 4.8, 350, 42, "#E8B84A", "Petiscos fritos, saladas e frutos do mar.");
        bebida(lagers, "Brassaria Puro Malte Dourada", "100% malte, com corpo médio e final limpo.",
                true, 5.0, 600, 44, "#D9A030", "Churrasco e queijos suaves.");
        bebida(lagers, "Brassaria Lager Leve", "Menos álcool e menos calorias, sem perder o frescor.",
                true, 3.5, 350, 29, "#F0CC6A", "Pratos leves e grelhados.");
        bebida(especiais, "IPA Rota das Especiarias", "Lupulada, com notas cítricas e de especiarias.",
                true, 6.2, 473, 55, "#C8811E", "Comida indiana, hambúrgueres e queijos curados.");
        bebida(especiais, "Weiss Imperial", "Cerveja de trigo turva, com aroma de banana e cravo.",
                true, 5.4, 500, 47, "#E3B565", "Saladas, peixes e sobremesas cítricas.");
        bebida(especiais, "Stout dos Mosteiros", "Escura e cremosa, com notas de café e chocolate.",
                true, 7.0, 355, 62, "#3B2416", "Chocolate amargo, carnes assadas e ostras.");
        bebida(semAlcool, "Brassaria Zero Pilsen", "Pilsen 0,0% com o mesmo frescor da original.",
                false, 0.0, 350, 18, "#EFD27A", "Qualquer hora do dia.");
        bebida(semAlcool, "Brassaria Zero Weiss", "Trigo sem álcool, ideal para depois do treino.",
                false, 0.3, 350, 22, "#E9C46A", "Pós-treino, saladas e frutas.");
        bebida(fermentados, "Kombucha Hibisco e Gengibre", "Fermentado de chá com hibisco e gengibre, naturalmente gaseificado.",
                false, 0.4, 300, 14, "#B23A48", "Comida japonesa e pratos picantes.");
        bebida(fermentados, "Sidra Maçã Verde", "Sidra seca e frutada, feita com maçãs selecionadas.",
                true, 4.5, 330, 40, "#B9C46A", "Queijos brancos e carne de porco.");
        bebida(fermentados, "Hidromel Medieval 1246", "A bebida dos fundadores: mel fermentado, doce e aromático.",
                true, 9.0, 375, 75, "#D48F2A", "Sobremesas e queijos azuis.");
    }

    private void bebida(Categoria categoria, String nome, String descricao, boolean alcoolica, double teor,
                        int volume, int calorias, String cor, String harmonizacao) {
        Bebida b = new Bebida();
        b.setCategoria(categoria);
        b.setNome(nome);
        b.setDescricao(descricao);
        b.setAlcoolica(alcoolica);
        b.setTeorAlcoolico(teor);
        b.setVolumeMl(volume);
        b.setCalorias100ml(calorias);
        b.setCorHex(cor);
        b.setHarmonizacao(harmonizacao);
        bebidas.put(nome, bebidaRepository.save(b));
    }

    private void criarEventos() {
        evento("Tour pela Cervejaria Histórica",
                "Visitas guiadas às adegas originais de 1246, com degustação ao final.",
                "Durante todo o ano do aniversário, abrimos as adegas mais antigas da Brassaria para visitas "
                        + "guiadas. O roteiro passa pelas tinas de fermentação restauradas, pelo museu de rótulos e "
                        + "termina com uma degustação comentada — com opções sem álcool para motoristas e menores de idade.",
                "Fábrica Matriz — Guarulhos/SP", LocalDate.of(2026, 9, 12),
                Set.of("Brassaria Pilsen 1246", "Hidromel Medieval 1246", "Brassaria Zero Pilsen"));
        evento("Corrida Brassaria Zero 5K",
                "Corrida de rua com hidratação, frutas e Brassaria Zero na linha de chegada.",
                "A Corrida Brassaria Zero 5K reuniu mais de 2 mil participantes num percurso plano e arborizado. "
                        + "Os postos de hidratação ofereceram água e isotônico, e a arena de chegada contou com "
                        + "frutas, alongamento orientado por educadores físicos e a nossa linha sem álcool.",
                "Parque do Ibirapuera — São Paulo/SP", LocalDate.of(2026, 10, 18),
                Set.of("Brassaria Zero Pilsen", "Brassaria Zero Weiss", "Kombucha Hibisco e Gengibre"));
        evento("Oficina de Harmonização com Chefs",
                "Chefs convidados ensinam a combinar estilos de bebida e pratos leves.",
                "Em parceria com chefs da cena paulistana, a oficina apresentou combinações de pratos leves com "
                        + "cervejas especiais e fermentados naturais. Cada participante recebeu um guia com "
                        + "informações nutricionais e dicas de consumo moderado.",
                "Espaço Gastronômico Brassaria — São Paulo/SP", LocalDate.of(2026, 11, 8),
                Set.of("IPA Rota das Especiarias", "Weiss Imperial", "Sidra Maçã Verde"));
        evento("Festival 780 Anos",
                "Dois dias de música, gastronomia e lançamentos para celebrar os 780 anos da Brassaria.",
                "O Festival 780 Anos é a grande celebração do aniversário da Brassaria Internacional. Serão dois "
                        + "dias com shows de bandas independentes, food trucks, área kids e o lançamento da edição "
                        + "comemorativa Stout dos Mosteiros. Haverá pontos de água gratuita em todo o espaço e "
                        + "parceria com aplicativos de transporte para a volta segura.",
                "Memorial da América Latina — São Paulo/SP", LocalDate.of(2026, 12, 12),
                Set.of("Stout dos Mosteiros", "Brassaria Puro Malte Dourada", "Brassaria Zero Pilsen",
                        "Kombucha Hibisco e Gengibre"));
    }

    private void evento(String titulo, String resumo, String conteudo, String local, LocalDate data,
                        Set<String> nomesBebidas) {
        Evento ev = new Evento();
        ev.setTitulo(titulo);
        ev.setResumo(resumo);
        ev.setConteudo(conteudo);
        ev.setLocal(local);
        ev.setDataEvento(data);
        nomesBebidas.forEach(n -> ev.getBebidasServidas().add(bebidas.get(n)));
        eventoRepository.save(ev);
    }
}
