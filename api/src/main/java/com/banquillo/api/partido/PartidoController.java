package com.banquillo.api.partido;

import com.banquillo.api.jugador.Jugador;
import com.banquillo.api.jugador.JugadorService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PartidoController {

    private final JugadorService jugadores;
    private final RotacionJusta rotacion = new RotacionJusta();

    public PartidoController(JugadorService jugadores) {
        this.jugadores = jugadores;
    }

    @PostMapping("/api/partidos/rotacion")
    public List<MinutosResponse> rotacion(@Valid @RequestBody RotacionRequest datos) {
        List<Jugador> convocados = datos.dorsales().stream()
                .distinct()
                .map(jugadores::buscar)
                .toList();
        Map<Jugador, Integer> minutos = rotacion.repartirMinutos(convocados,
                datos.duracion(), datos.jugadoresEnCampo(), datos.minutosPorBloque());
        return minutos.entrySet().stream()
                .map(e -> new MinutosResponse(e.getKey().getDorsal(), e.getKey().getNombre(), e.getValue()))
                .toList();
    }
}
