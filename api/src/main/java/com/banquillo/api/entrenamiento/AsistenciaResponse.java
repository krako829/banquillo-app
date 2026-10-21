package com.banquillo.api.entrenamiento;

public record AsistenciaResponse(int dorsal, String nombre, long asistidos, long total, double porcentaje) {
}
