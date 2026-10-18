package com.banquillo.core.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JugadorTest {

    @Test
    void jugadorValido_guardaSusDatos() {
        Jugador ane = new Jugador("  Ane  ", 7, Posicion.DELANTERO);

        assertEquals("Ane", ane.getNombre());
        assertEquals(7, ane.getDorsal());
        assertEquals(Posicion.DELANTERO, ane.getPosicion());
    }

    @Test
    void dorsalCero_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Jugador("Ane", 0, Posicion.DELANTERO));
    }

    @Test
    void dorsalCien_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Jugador("Ane", 100, Posicion.DELANTERO));
    }

    @Test
    void nombreVacio_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Jugador("   ", 7, Posicion.DELANTERO));
    }

    @Test
    void dosJugadoresConElMismoDorsal_sonIguales() {
        Jugador a = new Jugador("Ane", 7, Posicion.DELANTERO);
        Jugador b = new Jugador("Otra", 7, Posicion.DEFENSA);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void jugadorConTresDatos_noTieneContacto() {
        Jugador ane = new Jugador("Ane", 7, Posicion.DELANTERO);

        assertNull(ane.getContacto());
    }

    @Test
    void jugadorConContacto_loGuarda() {
        Jugador ane = new Jugador("Ane", 7, Posicion.DELANTERO, "familia.ane@ejemplo.com");

        assertEquals("familia.ane@ejemplo.com", ane.getContacto());
    }
}
