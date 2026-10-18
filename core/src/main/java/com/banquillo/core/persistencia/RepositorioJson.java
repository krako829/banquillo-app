package com.banquillo.core.persistencia;

import com.banquillo.core.modelo.Entrenador;
import com.banquillo.core.modelo.Entrenamiento;
import com.banquillo.core.modelo.Equipo;
import com.banquillo.core.modelo.Jugador;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class RepositorioJson {
    private final Path archivo;
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.INDENT_OUTPUT);

    public RepositorioJson(Path archivo) {
        this.archivo = archivo;
    }

    public void guardar(Equipo equipo) throws IOException {
        Entrenador e = equipo.getEntrenador();
        DatosEquipo datos = new DatosEquipo(
                equipo.getNombre(),
                equipo.getCategoria(),
                e == null ? null : new DatosEquipo.DatosEntrenador(e.getNombre(), e.getLicencia(), e.getContacto()),
                equipo.getPlantilla().stream()
                        .map(j -> new DatosEquipo.DatosJugador(j.getNombre(), j.getDorsal(), j.getPosicion(), j.getContacto()))
                        .toList(),
                equipo.getEntrenamientos().stream()
                        .map(en -> new DatosEquipo.DatosEntrenamiento(en.getFecha(), en.getObjetivo(),
                                en.getAsistentes().stream().map(Jugador::getDorsal).sorted().toList()))
                        .toList());
        Path carpeta = archivo.toAbsolutePath().getParent();
        Files.createDirectories(carpeta);
        mapper.writeValue(archivo.toFile(), datos);
    }

    public Optional<Equipo> cargar() throws IOException {
        if (Files.notExists(archivo)) {
            return Optional.empty();
        }
        DatosEquipo datos = mapper.readValue(archivo.toFile(), DatosEquipo.class);

        Equipo equipo = new Equipo(datos.nombre(), datos.categoria());
        if (datos.entrenador() != null) {
            DatosEquipo.DatosEntrenador e = datos.entrenador();
            equipo.setEntrenador(new Entrenador(e.nombre(), e.licencia(), e.contacto()));
        }
        for (DatosEquipo.DatosJugador j : datos.jugadores()) {
            equipo.anadirJugador(new Jugador(j.nombre(), j.dorsal(), j.posicion(), j.contactoFamilia()));
        }
        for (DatosEquipo.DatosEntrenamiento d : datos.entrenamientos()) {
            Entrenamiento entrenamiento = new Entrenamiento(d.fecha(), d.objetivo());
            equipo.anadirEntrenamiento(entrenamiento);
            for (int dorsal : d.asistentes()) {
                Jugador jugador = equipo.buscarPorDorsal(dorsal);
                if (jugador != null) {
                    entrenamiento.marcarAsistencia(jugador);
                }
            }
        }
        return Optional.of(equipo);
    }
}
