package com.banquillo.api.partido;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class RotacionJustaTest {

    private final RotacionJusta rotacion = new RotacionJusta();

    private List<Integer> convocados(int cuantos) {
        return IntStream.rangeClosed(1, cuantos).boxed().toList();
    }

    @Test
    void todosLosConvocadosJuegan() {
        Map<Integer, Integer> minutos = rotacion.repartirMinutos(convocados(10), 50, 7, 5);
        assertEquals(10, minutos.size());
        minutos.values().forEach(m -> assertTrue(m > 0));
    }

    @Test
    void seRepartenTodosLosMinutosDelPartido() {
        Map<Integer, Integer> minutos = rotacion.repartirMinutos(convocados(10), 50, 7, 5);
        assertEquals(50 * 7, minutos.values().stream().mapToInt(Integer::intValue).sum());
    }

    @Test
    void elRepartoEsJusto() {
        Map<Integer, Integer> minutos = rotacion.repartirMinutos(convocados(10), 50, 7, 5);
        assertTrue(Collections.max(minutos.values()) - Collections.min(minutos.values()) <= 5);
    }

    @Test
    void siCabenTodos_juegaTodoElPartido() {
        rotacion.repartirMinutos(convocados(7), 50, 7, 5).values().forEach(m -> assertEquals(50, m));
    }

    @Test
    void conMenosConvocadosQueEnCampo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> rotacion.repartirMinutos(convocados(5), 50, 7, 5));
    }

    @Test
    void siLaDuracionNoSeDivideEnBloques_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> rotacion.repartirMinutos(convocados(10), 50, 7, 7));
    }
}
