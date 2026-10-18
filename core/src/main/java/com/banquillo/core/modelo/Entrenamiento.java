package com.banquillo.core.modelo;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Entrenamiento {
    private final LocalDate fecha;
    private final String objetivo;
    private final Set<Jugador> asistentes = new HashSet<>();

    public Entrenamiento(LocalDate fecha, String objetivo) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria");
        }
        if (objetivo == null || objetivo.isBlank()) {
            throw new IllegalArgumentException("El objetivo no puede estar vacío");
        }
        this.fecha = fecha;
        this.objetivo = objetivo.trim();
    }

    public void marcarAsistencia(Jugador jugador) {
        asistentes.add(jugador);
    }

    public boolean haAsistido(Jugador jugador) {
        return asistentes.contains(jugador);
    }

    public Set<Jugador> getAsistentes() {
        return Set.copyOf(asistentes);
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getObjetivo() {
        return objetivo;
    }

    @Override
    public String toString() {
        return fecha + " · " + objetivo + " (" + asistentes.size() + " asistentes)";
    }
}
