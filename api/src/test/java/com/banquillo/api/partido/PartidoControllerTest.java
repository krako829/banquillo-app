package com.banquillo.api.partido;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.oneOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.banquillo.api.TestDeIntegracion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class PartidoControllerTest extends TestDeIntegracion {

    @BeforeEach
    void ochoJugadores() throws Exception {
        for (int dorsal = 1; dorsal <= 8; dorsal++) {
            mvc.perform(post("/api/jugadores").contentType(MediaType.APPLICATION_JSON)
                            .content("{\"nombre\": \"Jugador " + dorsal + "\", \"dorsal\": " + dorsal
                                    + ", \"posicion\": \"CENTROCAMPISTA\"}"))
                    .andExpect(status().isCreated());
        }
    }

    @Test
    void rotacion_reparteLosMinutosEntreTodos() throws Exception {
        mvc.perform(post("/api/partidos/rotacion").contentType(MediaType.APPLICATION_JSON).content("""
                        {"dorsales": [1, 2, 3, 4, 5, 6, 7, 8], "duracion": 50, "jugadoresEnCampo": 7, "minutosPorBloque": 5}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(8)))
                .andExpect(jsonPath("$[*].minutos", everyItem(oneOf(40, 45))));
    }

    @Test
    void pocosConvocados_devuelve400() throws Exception {
        mvc.perform(post("/api/partidos/rotacion").contentType(MediaType.APPLICATION_JSON).content("""
                        {"dorsales": [1, 2, 3], "duracion": 50, "jugadoresEnCampo": 7, "minutosPorBloque": 5}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Hay 3 convocados y hacen falta al menos 7"));
    }

    @Test
    void dorsalQueNoExiste_devuelve404() throws Exception {
        mvc.perform(post("/api/partidos/rotacion").contentType(MediaType.APPLICATION_JSON).content("""
                        {"dorsales": [1, 2, 3, 4, 5, 6, 7, 50], "duracion": 50, "jugadoresEnCampo": 7, "minutosPorBloque": 5}
                        """))
                .andExpect(status().isNotFound());
    }
}
