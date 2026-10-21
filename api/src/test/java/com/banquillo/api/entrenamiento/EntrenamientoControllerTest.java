package com.banquillo.api.entrenamiento;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.banquillo.api.TestDeIntegracion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class EntrenamientoControllerTest extends TestDeIntegracion {

    private void enviar(String url, String json) throws Exception {
        mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }

    @BeforeEach
    void plantillaDePrueba() throws Exception {
        enviar("/api/jugadores", """
                {"nombre": "Ane", "dorsal": 7, "posicion": "DELANTERO"}
                """);
        enviar("/api/jugadores", """
                {"nombre": "Unai", "dorsal": 1, "posicion": "PORTERO"}
                """);
        enviar("/api/entrenamientos", """
                {"fecha": "2026-11-05", "objetivo": "Tiro"}
                """);
        enviar("/api/entrenamientos", """
                {"fecha": "2026-11-03", "objetivo": "Pase"}
                """);
    }

    @Test
    void listar_salenOrdenadosPorFecha() throws Exception {
        mvc.perform(get("/api/entrenamientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].fecha").value("2026-11-03"))
                .andExpect(jsonPath("$[1].fecha").value("2026-11-05"));
    }

    @Test
    void mismaFecha_devuelve409() throws Exception {
        mvc.perform(post("/api/entrenamientos").contentType(MediaType.APPLICATION_JSON).content("""
                        {"fecha": "2026-11-03", "objetivo": "Defensa"}
                        """))
                .andExpect(status().isConflict());
    }

    @Test
    void pasarLista_guardaLosAsistentes() throws Exception {
        mvc.perform(put("/api/entrenamientos/2026-11-03/asistentes").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"dorsales": [7, 1]}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.asistentes[0]").value(1))
                .andExpect(jsonPath("$.asistentes[1]").value(7));
    }

    @Test
    void pasarListaConDorsalQueNoExiste_devuelve404() throws Exception {
        mvc.perform(put("/api/entrenamientos/2026-11-03/asistentes").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"dorsales": [99]}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void asistencia_calculaElPorcentaje() throws Exception {
        mvc.perform(put("/api/entrenamientos/2026-11-03/asistentes").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"dorsales": [7]}
                        """));

        mvc.perform(get("/api/jugadores/7/asistencia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.asistidos").value(1))
                .andExpect(jsonPath("$.total").value(2))
                .andExpect(jsonPath("$.porcentaje").value(50.0));

        mvc.perform(get("/api/asistencia"))
                .andExpect(jsonPath("$[0].nombre").value("Ane"))
                .andExpect(jsonPath("$[1].porcentaje").value(0.0));
    }

    @Test
    void borrarJugadorConAsistencias_funciona() throws Exception {
        mvc.perform(put("/api/entrenamientos/2026-11-03/asistentes").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"dorsales": [7]}
                        """));

        mvc.perform(delete("/api/jugadores/7"))
                .andExpect(status().isNoContent());
        mvc.perform(get("/api/entrenamientos/2026-11-03"))
                .andExpect(jsonPath("$.asistentes", hasSize(0)));
    }

    @Test
    void fechaQueNoExiste_devuelve404() throws Exception {
        mvc.perform(get("/api/entrenamientos/2026-12-25"))
                .andExpect(status().isNotFound());
    }
}
