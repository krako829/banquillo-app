package com.banquillo.core.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EntrenadorTest {

    @Test
    void entrenadorValido_guardaSusDatos() {
        Entrenador jon = new Entrenador("Jon", "Nivel 1", "jon@ejemplo.com");

        assertEquals("Jon", jon.getNombre());
        assertEquals("Nivel 1", jon.getLicencia());
        assertEquals("jon@ejemplo.com", jon.getContacto());
    }

    @Test
    void entrenadorSinNombre_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Entrenador(" ", "Nivel 1", null));
    }

    @Test
    void contactoVacio_seGuardaComoNull() {
        Entrenador jon = new Entrenador("Jon", "Nivel 1", "  ");

        assertNull(jon.getContacto());
    }
}
