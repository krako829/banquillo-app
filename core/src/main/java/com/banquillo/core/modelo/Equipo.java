package com.banquillo.core.modelo;

import com.banquillo.core.excepciones.DorsalRepetidoException;
import com.banquillo.core.excepciones.EntrenamientoDuplicadoException;
import com.banquillo.core.excepciones.JugadorNoEncontradoException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Equipo {
    private final String nombre;
    private final Categoria categoria;
    private final Map<Integer, Jugador> plantilla = new LinkedHashMap<>();
    private final Map<LocalDate, Entrenamiento> entrenamientos = new TreeMap<>();
    private Entrenador entrenador;

    public Equipo(String nombre, Categoria categoria) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del equipo no puede estar vacío");
        }
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría es obligatoria");
        }
        this.nombre = nombre.trim();
        this.categoria = categoria;
    }

    // ---------- Jugadores ----------

    public void anadirJugador(Jugador nuevo) {
        if (plantilla.containsKey(nuevo.getDorsal())) {
            throw new DorsalRepetidoException(nuevo.getDorsal());
        }
        plantilla.put(nuevo.getDorsal(), nuevo);
    }

    public Jugador buscarPorDorsal(int dorsal) {
        return plantilla.get(dorsal);
    }

    public void borrarJugador(int dorsal) {
        if (plantilla.remove(dorsal) == null) {
            throw new JugadorNoEncontradoException(dorsal);
        }
    }

    // ---------- Entrenamientos y asistencia ----------

    public void anadirEntrenamiento(Entrenamiento nuevo) {
        if (entrenamientos.containsKey(nuevo.getFecha())) {
            throw new EntrenamientoDuplicadoException(nuevo.getFecha());
        }
        entrenamientos.put(nuevo.getFecha(), nuevo);
    }

    public Entrenamiento buscarEntrenamiento(LocalDate fecha) {
        return entrenamientos.get(fecha);
    }

    public int entrenamientosAsistidos(Jugador jugador) {
        int asistidos = 0;
        for (Entrenamiento entrenamiento : entrenamientos.values()) {
            if (entrenamiento.haAsistido(jugador)) {
                asistidos++;
            }
        }
        return asistidos;
    }

    public double porcentajeAsistencia(Jugador jugador) {
        if (entrenamientos.isEmpty()) {
            return 0;
        }
        return 100.0 * entrenamientosAsistidos(jugador) / entrenamientos.size();
    }

    public List<Jugador> jugadoresPorAsistencia() {
        List<Jugador> ordenados = new ArrayList<>(plantilla.values());
        ordenados.sort(Comparator.comparing(this::porcentajeAsistencia).reversed());
        return ordenados;
    }

    // ---------- Estadísticas ----------

    public Map<Posicion, Long> jugadoresPorPosicion() {
        return plantilla.values().stream()
                .collect(Collectors.groupingBy(Jugador::getPosicion, TreeMap::new, Collectors.counting()));
    }

    public double asistenciaMedia() {
        return plantilla.values().stream()
                .mapToDouble(this::porcentajeAsistencia)
                .average()
                .orElse(0);
    }

    public List<Jugador> jugadoresConAsistenciaMenorQue(double porcentaje) {
        return plantilla.values().stream()
                .filter(jugador -> porcentajeAsistencia(jugador) < porcentaje)
                .toList();
    }

    // ---------- Avisos y entrenador ----------

    public List<Notificable> destinatariosAvisos() {
        List<Notificable> destinatarios = new ArrayList<>();
        if (entrenador != null) {
            destinatarios.add(entrenador);
        }
        destinatarios.addAll(plantilla.values());
        return destinatarios;
    }

    public void setEntrenador(Entrenador entrenador) {
        this.entrenador = entrenador;
    }

    public Entrenador getEntrenador() {
        return entrenador;
    }

    // ---------- Getters ----------

    public List<Jugador> getPlantilla() {
        return List.copyOf(plantilla.values());
    }

    public List<Entrenamiento> getEntrenamientos() {
        return List.copyOf(entrenamientos.values());
    }

    public String getNombre() {
        return nombre;
    }

    public Categoria getCategoria() {
        return categoria;
    }
}
