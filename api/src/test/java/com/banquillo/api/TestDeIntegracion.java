package com.banquillo.api;

import com.banquillo.api.entrenamiento.EntrenamientoRepository;
import com.banquillo.api.jugador.JugadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
public abstract class TestDeIntegracion {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    private EntrenamientoRepository entrenamientos;

    @Autowired
    private JugadorRepository jugadores;

    @BeforeEach
    void vaciarBaseDeDatos() {
        entrenamientos.deleteAll();
        jugadores.deleteAll();
    }
}
