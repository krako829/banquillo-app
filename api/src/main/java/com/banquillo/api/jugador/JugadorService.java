package com.banquillo.api.jugador;

import com.banquillo.api.error.ConflictoException;
import com.banquillo.api.error.NoEncontradoException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JugadorService {

    private final JugadorRepository repositorio;

    public JugadorService(JugadorRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional(readOnly = true)
    public List<Jugador> listar() {
        return repositorio.findAllByOrderByDorsalAsc();
    }

    @Transactional(readOnly = true)
    public Jugador buscar(int dorsal) {
        return repositorio.findByDorsal(dorsal)
                .orElseThrow(() -> new NoEncontradoException("No hay ningún jugador con el dorsal " + dorsal));
    }

    public Jugador crear(JugadorRequest datos) {
        if (repositorio.existsByDorsal(datos.dorsal())) {
            throw new ConflictoException("Ya hay un jugador con el dorsal " + datos.dorsal());
        }
        Jugador jugador = new Jugador(datos.nombre(), datos.dorsal(), datos.posicion(), datos.contactoFamilia());
        return repositorio.save(jugador);
    }

    public Jugador actualizar(int dorsal, JugadorRequest datos) {
        Jugador jugador = buscar(dorsal);
        if (datos.dorsal() != dorsal && repositorio.existsByDorsal(datos.dorsal())) {
            throw new ConflictoException("Ya hay un jugador con el dorsal " + datos.dorsal());
        }
        jugador.actualizar(datos.nombre(), datos.dorsal(), datos.posicion(), datos.contactoFamilia());
        return jugador;
    }

    public void borrar(int dorsal) {
        repositorio.delete(buscar(dorsal));
    }
}
