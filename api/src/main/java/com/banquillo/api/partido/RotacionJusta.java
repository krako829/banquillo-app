package com.banquillo.api.partido;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RotacionJusta {

    public <T> Map<T, Integer> repartirMinutos(List<T> convocados,
                                               int duracionPartido,
                                               int jugadoresEnCampo,
                                               int minutosPorBloque) {
        if (convocados.size() < jugadoresEnCampo) {
            throw new IllegalArgumentException("Hay " + convocados.size()
                    + " convocados y hacen falta al menos " + jugadoresEnCampo);
        }
        if (minutosPorBloque <= 0 || duracionPartido % minutosPorBloque != 0) {
            throw new IllegalArgumentException("La duración (" + duracionPartido
                    + " min) no se puede dividir en bloques de " + minutosPorBloque + " min");
        }

        Map<T, Integer> minutos = new LinkedHashMap<>();
        for (T jugador : convocados) {
            minutos.put(jugador, 0);
        }

        int bloques = duracionPartido / minutosPorBloque;
        for (int bloque = 0; bloque < bloques; bloque++) {
            List<T> ordenados = new ArrayList<>(convocados);
            ordenados.sort(Comparator.comparing(minutos::get));
            for (int i = 0; i < jugadoresEnCampo; i++) {
                T jugador = ordenados.get(i);
                minutos.put(jugador, minutos.get(jugador) + minutosPorBloque);
            }
        }
        return minutos;
    }
}
