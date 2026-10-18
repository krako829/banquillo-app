package com.banquillo.core.persistencia;

import com.banquillo.core.modelo.Categoria;
import com.banquillo.core.modelo.Posicion;
import java.time.LocalDate;
import java.util.List;

public record DatosEquipo(String nombre,
                          Categoria categoria,
                          DatosEntrenador entrenador,
                          List<DatosJugador> jugadores,
                          List<DatosEntrenamiento> entrenamientos) {

    public record DatosJugador(String nombre, int dorsal, Posicion posicion, String contactoFamilia) {}

    public record DatosEntrenador(String nombre, String licencia, String contacto) {}

    public record DatosEntrenamiento(LocalDate fecha, String objetivo, List<Integer> asistentes) {}
}
