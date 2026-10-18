package com.banquillo.core.persistencia;

import static org.junit.jupiter.api.Assertions.*;

import com.banquillo.core.modelo.Categoria;
import com.banquillo.core.modelo.Entrenador;
import com.banquillo.core.modelo.Entrenamiento;
import com.banquillo.core.modelo.Equipo;
import com.banquillo.core.modelo.Jugador;
import com.banquillo.core.modelo.Posicion;
import java.nio.file.Path;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class RepositorioJsonTest {

    @Test
    void siNoHayArchivo_devuelveVacio(@TempDir Path carpeta) throws Exception {
        RepositorioJson repositorio = new RepositorioJson(carpeta.resolve("no-existe.json"));

        assertTrue(repositorio.cargar().isEmpty());
    }

    @Test
    void loQueSeGuarda_seCargaIgual(@TempDir Path carpeta) throws Exception {
        RepositorioJson repositorio = new RepositorioJson(carpeta.resolve("equipo.json"));
        Equipo original = new Equipo("Alevín A", Categoria.ALEVIN);
        original.setEntrenador(new Entrenador("Jon", "Nivel 1", "jon@ejemplo.com"));
        Jugador ane = new Jugador("Ane", 7, Posicion.DELANTERO, "familia.ane@ejemplo.com");
        Jugador unai = new Jugador("Unai", 1, Posicion.PORTERO);
        original.anadirJugador(ane);
        original.anadirJugador(unai);
        Entrenamiento martes = new Entrenamiento(LocalDate.of(2026, 11, 3), "Pase");
        original.anadirEntrenamiento(martes);
        original.anadirEntrenamiento(new Entrenamiento(LocalDate.of(2026, 11, 5), "Tiro"));
        martes.marcarAsistencia(ane);

        repositorio.guardar(original);
        Equipo cargado = repositorio.cargar().orElseThrow();

        assertEquals("Alevín A", cargado.getNombre());
        assertEquals(Categoria.ALEVIN, cargado.getCategoria());
        assertEquals("Jon", cargado.getEntrenador().getNombre());
        assertEquals(original.getPlantilla(), cargado.getPlantilla());
        assertEquals("familia.ane@ejemplo.com", cargado.buscarPorDorsal(7).getContacto());
        assertEquals(2, cargado.getEntrenamientos().size());
        assertEquals(50.0, cargado.porcentajeAsistencia(cargado.buscarPorDorsal(7)), 0.01);
        assertEquals(0.0, cargado.porcentajeAsistencia(cargado.buscarPorDorsal(1)), 0.01);
    }
}
