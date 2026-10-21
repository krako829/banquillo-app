package com.banquillo.api;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

class OpenApiTest extends TestDeIntegracion {

    @Test
    void laDocumentacionIncluyeLosEndpoints() throws Exception {
        mvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/api/jugadores")))
                .andExpect(content().string(containsString("/api/partidos/rotacion")));
    }
}
