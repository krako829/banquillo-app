package com.banquillo.core.servicio;

import static org.junit.jupiter.api.Assertions.*;

import com.banquillo.core.modelo.Jugador;
import com.banquillo.core.modelo.Posicion;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class RotacionJustaTest {

    private final RotacionJusta rotacion = new RotacionJusta();

    private List<Jugador> convocados(int cuantos) {
        return IntStream.rangeClosed(1, cuantos)
                .mapToObj(i -> new Jugador("Jugador " + i, i, Posicion.CENTROCAMPISTA))
                .toList();
    }

    @Test
    void todosLosConvocadosJuegan() {
        Map<Jugador, Integer> minutos = rotacion.repartirMinutos(convocados(10), 50, 7, 5);
        assertEquals(10, minutos.size());
        minutos.values().forEach(m -> assertTrue(m > 0));
    }

    @Test
    void seRepartenTodosLosMinutosDelPartido() {
        Map<Jugador, Integer> minutos = rotacion.repartirMinutos(convocados(10), 50, 7, 5);
        int total = minutos.values().stream().mapToInt(Integer::intValue).sum();
        assertEquals(50 * 7, total);
    }

    @Test
    void elRepartoEsJusto() {
        Map<Jugador, Integer> minutos = rotacion.repartirMinutos(convocados(10), 50, 7, 5);
        int max = Collections.max(minutos.values());
        int min = Collections.min(minutos.values());
        assertTrue(max - min <= 5, "Diferencia de " + (max - min) + " minutos");
    }

    @Test
    void siCabenTodos_juegaTodoElPartido() {
        Map<Jugador, Integer> minutos = rotacion.repartirMinutos(convocados(7), 50, 7, 5);
        minutos.values().forEach(m -> assertEquals(50, m));
    }

    @Test
    void conMenosConvocadosQueEnCampo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> rotacion.repartirMinutos(convocados(5), 50, 7, 5));
    }

    @Test
    void siLaDuracionNoSeDivideEnBloques_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> rotacion.repartirMinutos(convocados(10), 50, 7, 7));
    }
}
