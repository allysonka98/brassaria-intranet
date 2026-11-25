package br.com.brassaria.intranet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração da API (sobe o contexto completo com banco H2 em memória).
 * Rode com: mvn test
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class ApiIntegracaoTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void deveRetornarDadosInstitucionais() throws Exception {
        mvc.perform(get("/api/empresa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Brassaria Internacional"))
                .andExpect(jsonPath("$.missao", not(emptyString())))
                .andExpect(jsonPath("$.valores", hasSize(greaterThan(0))));
    }

    @Test
    void deveListarPortfolioEFiltrarSemAlcool() throws Exception {
        mvc.perform(get("/api/bebidas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(5))));

        mvc.perform(get("/api/bebidas").param("alcoolica", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].alcoolica", everyItem(is(false))));
    }

    @Test
    void recentesDeveTrazerNoMaximoTresEventosEOUltimoCadastradoPrimeiro() throws Exception {
        String json = """
                {"titulo":"Evento de teste","resumo":"Resumo","conteudo":"Conteúdo",
                 "local":"Guarulhos/SP","dataEvento":"2027-01-10","bebidaIds":[1,2]}
                """;
        mvc.perform(post("/api/eventos").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.bebidasServidas", hasSize(2)));

        mvc.perform(get("/api/eventos/recentes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].titulo").value("Evento de teste"));
    }

    @Test
    void deveValidarCamposObrigatoriosDoEvento() throws Exception {
        mvc.perform(post("/api/eventos").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.titulo").exists());
    }

    @Test
    void deveRecusarBebidaAlcoolicaComTeorZero() throws Exception {
        String json = """
                {"nome":"Teste","categoriaId":1,"alcoolica":true,"teorAlcoolico":0,"volumeMl":350}
                """;
        mvc.perform(post("/api/bebidas").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deveRetornar404ParaEventoInexistente() throws Exception {
        mvc.perform(get("/api/eventos/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem", containsString("9999")));
    }
}
