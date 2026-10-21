package com.banquillo.api.jugador;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.banquillo.api.TestDeIntegracion;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class JugadorControllerTest extends TestDeIntegracion {

    private void crear(String json) throws Exception {
        mvc.perform(post("/api/jugadores").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void crearJugador_devuelve201ConSuUbicacion() throws Exception {
        mvc.perform(post("/api/jugadores").contentType(MediaType.APPLICATION_JSON).content("""
                        {"nombre": "Ane", "dorsal": 7, "posicion": "DELANTERO", "contactoFamilia": "familia.ane@ejemplo.com"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/jugadores/7"))
                .andExpect(jsonPath("$.nombre").value("Ane"))
                .andExpect(jsonPath("$.contactoFamilia").value("familia.ane@ejemplo.com"));
    }

    @Test
    void listar_devuelveLosJugadoresOrdenadosPorDorsal() throws Exception {
        crear("""
                {"nombre": "Ane", "dorsal": 7, "posicion": "DELANTERO"}
                """);
        crear("""
                {"nombre": "Unai", "dorsal": 1, "posicion": "PORTERO"}
                """);

        mvc.perform(get("/api/jugadores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre").value("Unai"))
                .andExpect(jsonPath("$[1].nombre").value("Ane"));
    }

    @Test
    void dorsalRepetido_devuelve409() throws Exception {
        crear("""
                {"nombre": "Ane", "dorsal": 7, "posicion": "DELANTERO"}
                """);

        mvc.perform(post("/api/jugadores").contentType(MediaType.APPLICATION_JSON).content("""
                        {"nombre": "Mikel", "dorsal": 7, "posicion": "DEFENSA"}
                        """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Ya hay un jugador con el dorsal 7"));
    }

    @Test
    void datosNoValidos_devuelve400() throws Exception {
        mvc.perform(post("/api/jugadores").contentType(MediaType.APPLICATION_JSON).content("""
                        {"nombre": "", "dorsal": 120, "posicion": "DELANTERO"}
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void posicionQueNoExiste_devuelve400() throws Exception {
        mvc.perform(post("/api/jugadores").contentType(MediaType.APPLICATION_JSON).content("""
                        {"nombre": "Ane", "dorsal": 7, "posicion": "LATERAL"}
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarDorsalQueNoExiste_devuelve404() throws Exception {
        mvc.perform(get("/api/jugadores/5"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("No hay ningún jugador con el dorsal 5"));
    }

    @Test
    void actualizar_cambiaLosDatos() throws Exception {
        crear("""
                {"nombre": "Ane", "dorsal": 7, "posicion": "DELANTERO"}
                """);

        mvc.perform(put("/api/jugadores/7").contentType(MediaType.APPLICATION_JSON).content("""
                        {"nombre": "Ane", "dorsal": 10, "posicion": "CENTROCAMPISTA"}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dorsal").value(10));

        mvc.perform(get("/api/jugadores/10"))
                .andExpect(jsonPath("$.posicion").value("CENTROCAMPISTA"));
        mvc.perform(get("/api/jugadores/7"))
                .andExpect(status().isNotFound());
    }

    @Test
    void borrar_devuelve204YYaNoExiste() throws Exception {
        crear("""
                {"nombre": "Ane", "dorsal": 7, "posicion": "DELANTERO"}
                """);

        mvc.perform(delete("/api/jugadores/7"))
                .andExpect(status().isNoContent());
        mvc.perform(get("/api/jugadores/7"))
                .andExpect(status().isNotFound());
    }
}
