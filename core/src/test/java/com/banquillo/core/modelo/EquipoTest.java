package com.banquillo.core.modelo;

import static org.junit.jupiter.api.Assertions.*;

import com.banquillo.core.excepciones.DorsalRepetidoException;
import com.banquillo.core.excepciones.EntrenamientoDuplicadoException;
import com.banquillo.core.excepciones.JugadorNoEncontradoException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class EquipoTest {

    @Test
    void anadirJugador_loGuardaEnLaPlantilla() {
        Equipo equipo = new Equipo("Alevín A", Categoria.ALEVIN);
        Jugador ane = new Jugador("Ane", 7, Posicion.DELANTERO);

        equipo.anadirJugador(ane);

        assertEquals(1, equipo.getPlantilla().size());
        assertEquals(ane, equipo.buscarPorDorsal(7));
    }

    @Test
    void anadirJugador_conDorsalRepetido_lanzaExcepcion() {
        Equipo equipo = new Equipo("Alevín A", Categoria.ALEVIN);
        equipo.anadirJugador(new Jugador("Ane", 7, Posicion.DELANTERO));

        assertThrows(DorsalRepetidoException.class,
                () -> equipo.anadirJugador(new Jugador("Unai", 7, Posicion.DEFENSA)));
    }

    @Test
    void buscarPorDorsal_siNoExiste_devuelveNull() {
        Equipo equipo = new Equipo("Alevín A", Categoria.ALEVIN);

        assertNull(equipo.buscarPorDorsal(5));
    }

    @Test
    void getPlantilla_noSePuedeModificarDesdeFuera() {
        Equipo equipo = new Equipo("Alevín A", Categoria.ALEVIN);

        assertThrows(UnsupportedOperationException.class,
                () -> equipo.getPlantilla().add(new Jugador("Mikel", 3, Posicion.PORTERO)));
    }

    @Test
    void borrarJugador_loQuitaDeLaPlantilla() {
        Equipo equipo = new Equipo("Alevín A", Categoria.ALEVIN);
        equipo.anadirJugador(new Jugador("Ane", 7, Posicion.DELANTERO));

        equipo.borrarJugador(7);

        assertTrue(equipo.getPlantilla().isEmpty());
    }

    @Test
    void borrarJugador_siNoExiste_lanzaExcepcion() {
        Equipo equipo = new Equipo("Alevín A", Categoria.ALEVIN);

        assertThrows(JugadorNoEncontradoException.class, () -> equipo.borrarJugador(7));
    }

    @Test
    void anadirEntrenamiento_mismaFecha_lanzaExcepcion() {
        Equipo equipo = new Equipo("Alevín A", Categoria.ALEVIN);
        equipo.anadirEntrenamiento(new Entrenamiento(LocalDate.of(2026, 11, 3), "Pase"));

        assertThrows(EntrenamientoDuplicadoException.class,
                () -> equipo.anadirEntrenamiento(new Entrenamiento(LocalDate.of(2026, 11, 3), "Tiro")));
    }

    @Test
    void porcentajeAsistencia_sinEntrenamientos_esCero() {
        Equipo equipo = new Equipo("Alevín A", Categoria.ALEVIN);
        Jugador ane = new Jugador("Ane", 7, Posicion.DELANTERO);
        equipo.anadirJugador(ane);

        assertEquals(0.0, equipo.porcentajeAsistencia(ane), 0.01);
    }

    @Test
    void porcentajeAsistencia_unoDeDos_esCincuenta() {
        Equipo equipo = new Equipo("Alevín A", Categoria.ALEVIN);
        Jugador ane = new Jugador("Ane", 7, Posicion.DELANTERO);
        equipo.anadirJugador(ane);
        Entrenamiento martes = new Entrenamiento(LocalDate.of(2026, 11, 3), "Pase");
        Entrenamiento jueves = new Entrenamiento(LocalDate.of(2026, 11, 5), "Tiro");
        equipo.anadirEntrenamiento(martes);
        equipo.anadirEntrenamiento(jueves);

        martes.marcarAsistencia(ane);

        assertEquals(50.0, equipo.porcentajeAsistencia(ane), 0.01);
    }

    @Test
    void porcentajeAsistencia_unoDeTres_esUnTercio() {
        Equipo equipo = new Equipo("Alevín A", Categoria.ALEVIN);
        Jugador ane = new Jugador("Ane", 7, Posicion.DELANTERO);
        equipo.anadirJugador(ane);
        Entrenamiento primero = new Entrenamiento(LocalDate.of(2026, 11, 3), "Pase");
        equipo.anadirEntrenamiento(primero);
        equipo.anadirEntrenamiento(new Entrenamiento(LocalDate.of(2026, 11, 5), "Tiro"));
        equipo.anadirEntrenamiento(new Entrenamiento(LocalDate.of(2026, 11, 10), "Defensa"));

        primero.marcarAsistencia(ane);

        assertEquals(33.33, equipo.porcentajeAsistencia(ane), 0.01);
    }

    @Test
    void jugadoresPorAsistencia_ordenaDeMasAMenos() {
        Equipo equipo = new Equipo("Alevín A", Categoria.ALEVIN);
        Jugador ane = new Jugador("Ane", 7, Posicion.DELANTERO);
        Jugador unai = new Jugador("Unai", 1, Posicion.PORTERO);
        equipo.anadirJugador(unai);
        equipo.anadirJugador(ane);
        Entrenamiento martes = new Entrenamiento(LocalDate.of(2026, 11, 3), "Pase");
        equipo.anadirEntrenamiento(martes);

        martes.marcarAsistencia(ane);

        assertEquals(ane, equipo.jugadoresPorAsistencia().get(0));
        assertEquals(unai, equipo.jugadoresPorAsistencia().get(1));
    }

    @Test
    void buscarEntrenamiento_porFecha() {
        Equipo equipo = new Equipo("Alevín A", Categoria.ALEVIN);
        Entrenamiento martes = new Entrenamiento(LocalDate.of(2026, 11, 3), "Pase");
        equipo.anadirEntrenamiento(martes);

        assertEquals(martes, equipo.buscarEntrenamiento(LocalDate.of(2026, 11, 3)));
        assertNull(equipo.buscarEntrenamiento(LocalDate.of(2026, 11, 4)));
    }

    @Test
    void getEntrenamientos_salenOrdenadosPorFecha() {
        Equipo equipo = new Equipo("Alevín A", Categoria.ALEVIN);
        equipo.anadirEntrenamiento(new Entrenamiento(LocalDate.of(2026, 11, 5), "Tiro"));
        equipo.anadirEntrenamiento(new Entrenamiento(LocalDate.of(2026, 11, 3), "Pase"));
        equipo.anadirEntrenamiento(new Entrenamiento(LocalDate.of(2026, 11, 10), "Defensa"));

        assertEquals(LocalDate.of(2026, 11, 3), equipo.getEntrenamientos().get(0).getFecha());
        assertEquals(LocalDate.of(2026, 11, 5), equipo.getEntrenamientos().get(1).getFecha());
        assertEquals(LocalDate.of(2026, 11, 10), equipo.getEntrenamientos().get(2).getFecha());
    }

    @Test
    void entrenamientosAsistidos_cuentaSoloDondeVino() {
        Equipo equipo = new Equipo("Alevín A", Categoria.ALEVIN);
        Jugador ane = new Jugador("Ane", 7, Posicion.DELANTERO);
        equipo.anadirJugador(ane);
        Entrenamiento martes = new Entrenamiento(LocalDate.of(2026, 11, 3), "Pase");
        equipo.anadirEntrenamiento(martes);
        equipo.anadirEntrenamiento(new Entrenamiento(LocalDate.of(2026, 11, 5), "Tiro"));

        martes.marcarAsistencia(ane);

        assertEquals(1, equipo.entrenamientosAsistidos(ane));
    }

    @Test
    void destinatariosAvisos_incluyeEntrenadorYJugadores() {
        Equipo equipo = new Equipo("Alevín A", Categoria.ALEVIN);
        Entrenador jon = new Entrenador("Jon", "Nivel 1", "jon@ejemplo.com");
        Jugador ane = new Jugador("Ane", 7, Posicion.DELANTERO);
        equipo.setEntrenador(jon);
        equipo.anadirJugador(ane);

        assertEquals(2, equipo.destinatariosAvisos().size());
        assertTrue(equipo.destinatariosAvisos().contains(jon));
        assertTrue(equipo.destinatariosAvisos().contains(ane));
    }

    @Test
    void jugadoresPorPosicion_cuentaCadaPosicion() {
        Equipo equipo = new Equipo("Alevín A", Categoria.ALEVIN);
        equipo.anadirJugador(new Jugador("Unai", 1, Posicion.PORTERO));
        equipo.anadirJugador(new Jugador("Ane", 7, Posicion.DELANTERO));
        equipo.anadirJugador(new Jugador("Mikel", 9, Posicion.DELANTERO));

        assertEquals(1L, equipo.jugadoresPorPosicion().get(Posicion.PORTERO));
        assertEquals(2L, equipo.jugadoresPorPosicion().get(Posicion.DELANTERO));
    }

    @Test
    void asistenciaMedia_conPlantillaVacia_esCero() {
        Equipo equipo = new Equipo("Alevín A", Categoria.ALEVIN);

        assertEquals(0.0, equipo.asistenciaMedia(), 0.01);
    }

    @Test
    void jugadoresConAsistenciaMenorQue_filtraCorrectamente() {
        Equipo equipo = new Equipo("Alevín A", Categoria.ALEVIN);
        Jugador ane = new Jugador("Ane", 7, Posicion.DELANTERO);
        Jugador unai = new Jugador("Unai", 1, Posicion.PORTERO);
        equipo.anadirJugador(ane);
        equipo.anadirJugador(unai);
        Entrenamiento martes = new Entrenamiento(LocalDate.of(2026, 11, 3), "Pase");
        equipo.anadirEntrenamiento(martes);
        martes.marcarAsistencia(ane);

        assertEquals(List.of(unai), equipo.jugadoresConAsistenciaMenorQue(50));
        assertEquals(50.0, equipo.asistenciaMedia(), 0.01);
    }

    @Test
    void equipoSinCategoria_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Equipo("Alevín A", null));
    }
}
