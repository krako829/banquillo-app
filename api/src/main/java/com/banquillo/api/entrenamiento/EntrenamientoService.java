package com.banquillo.api.entrenamiento;

import com.banquillo.api.error.ConflictoException;
import com.banquillo.api.error.NoEncontradoException;
import com.banquillo.api.jugador.Jugador;
import com.banquillo.api.jugador.JugadorService;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EntrenamientoService {

    private final EntrenamientoRepository repositorio;
    private final JugadorService jugadores;

    public EntrenamientoService(EntrenamientoRepository repositorio, JugadorService jugadores) {
        this.repositorio = repositorio;
        this.jugadores = jugadores;
    }

    @Transactional(readOnly = true)
    public List<EntrenamientoResponse> listar() {
        return repositorio.findAllByOrderByFechaAsc().stream().map(EntrenamientoResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public EntrenamientoResponse buscar(LocalDate fecha) {
        return EntrenamientoResponse.de(entrenamiento(fecha));
    }

    public EntrenamientoResponse crear(EntrenamientoRequest datos) {
        if (repositorio.existsByFecha(datos.fecha())) {
            throw new ConflictoException("Ya hay un entrenamiento el " + datos.fecha());
        }
        return EntrenamientoResponse.de(repositorio.save(new Entrenamiento(datos.fecha(), datos.objetivo())));
    }

    public EntrenamientoResponse pasarLista(LocalDate fecha, List<Integer> dorsales) {
        Entrenamiento entrenamiento = entrenamiento(fecha);
        Set<Jugador> asistentes = new HashSet<>();
        for (int dorsal : dorsales) {
            asistentes.add(jugadores.buscar(dorsal));
        }
        entrenamiento.cambiarAsistentes(asistentes);
        return EntrenamientoResponse.de(entrenamiento);
    }

    public void borrar(LocalDate fecha) {
        repositorio.delete(entrenamiento(fecha));
    }

    @Transactional(readOnly = true)
    public AsistenciaResponse asistencia(int dorsal) {
        return asistencia(jugadores.buscar(dorsal), repositorio.count());
    }

    @Transactional(readOnly = true)
    public List<AsistenciaResponse> asistenciaDeTodos() {
        long total = repositorio.count();
        return jugadores.listar().stream()
                .map(jugador -> asistencia(jugador, total))
                .sorted(Comparator.comparingDouble(AsistenciaResponse::porcentaje).reversed())
                .toList();
    }

    private AsistenciaResponse asistencia(Jugador jugador, long total) {
        long asistidos = repositorio.countByAsistentes_Dorsal(jugador.getDorsal());
        double porcentaje = total == 0 ? 0 : Math.round(1000.0 * asistidos / total) / 10.0;
        return new AsistenciaResponse(jugador.getDorsal(), jugador.getNombre(), asistidos, total, porcentaje);
    }

    private Entrenamiento entrenamiento(LocalDate fecha) {
        return repositorio.findByFecha(fecha)
                .orElseThrow(() -> new NoEncontradoException("No hay ningún entrenamiento el " + fecha));
    }
}
