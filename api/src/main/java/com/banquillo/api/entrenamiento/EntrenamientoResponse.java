package com.banquillo.api.entrenamiento;

import com.banquillo.api.jugador.Jugador;
import java.time.LocalDate;
import java.util.List;

public record EntrenamientoResponse(LocalDate fecha, String objetivo, List<Integer> asistentes) {

    public static EntrenamientoResponse de(Entrenamiento entrenamiento) {
        List<Integer> dorsales = entrenamiento.getAsistentes().stream()
                .map(Jugador::getDorsal)
                .sorted()
                .toList();
        return new EntrenamientoResponse(entrenamiento.getFecha(), entrenamiento.getObjetivo(), dorsales);
    }
}
