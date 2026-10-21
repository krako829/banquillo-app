package com.banquillo.api.jugador;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jugadores")
public class JugadorController {

    private final JugadorService servicio;

    public JugadorController(JugadorService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<JugadorResponse> listar() {
        return servicio.listar().stream().map(JugadorResponse::de).toList();
    }

    @GetMapping("/{dorsal}")
    public JugadorResponse buscar(@PathVariable int dorsal) {
        return JugadorResponse.de(servicio.buscar(dorsal));
    }

    @PostMapping
    public ResponseEntity<JugadorResponse> crear(@Valid @RequestBody JugadorRequest datos) {
        Jugador jugador = servicio.crear(datos);
        return ResponseEntity.created(URI.create("/api/jugadores/" + jugador.getDorsal()))
                .body(JugadorResponse.de(jugador));
    }

    @PutMapping("/{dorsal}")
    public JugadorResponse actualizar(@PathVariable int dorsal, @Valid @RequestBody JugadorRequest datos) {
        return JugadorResponse.de(servicio.actualizar(dorsal, datos));
    }

    @DeleteMapping("/{dorsal}")
    public ResponseEntity<Void> borrar(@PathVariable int dorsal) {
        servicio.borrar(dorsal);
        return ResponseEntity.noContent().build();
    }
}
