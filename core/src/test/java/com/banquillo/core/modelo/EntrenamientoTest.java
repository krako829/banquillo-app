package com.banquillo.core.modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class EntrenamientoTest {

    @Test
    void entrenamientoValido_guardaSusDatos() {
        Entrenamiento e = new Entrenamiento(LocalDate.of(2026, 11, 3), "Pase y control");

        assertEquals(LocalDate.of(2026, 11, 3), e.getFecha());
        assertEquals("Pase y control", e.getObjetivo());
    }

    @Test
    void fechaNula_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Entrenamiento(null, "Pase"));
    }

    @Test
    void objetivoVacio_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Entrenamiento(LocalDate.of(2026, 11, 3), " "));
    }

    @Test
    void marcarAsistencia_dosVeces_cuentaUnaSola() {
        Entrenamiento e = new Entrenamiento(LocalDate.of(2026, 11, 3), "Pase");
        Jugador ane = new Jugador("Ane", 7, Posicion.DELANTERO);

        e.marcarAsistencia(ane);
        e.marcarAsistencia(ane);

        assertTrue(e.haAsistido(ane));
        assertEquals(1, e.getAsistentes().size());
    }
}
