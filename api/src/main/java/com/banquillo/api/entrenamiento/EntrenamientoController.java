package com.banquillo.api.entrenamiento;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntrenamientoController {

    private final EntrenamientoService servicio;

    public EntrenamientoController(EntrenamientoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/api/entrenamientos")
    public List<EntrenamientoResponse> listar() {
        return servicio.listar();
    }

    @GetMapping("/api/entrenamientos/{fecha}")
    public EntrenamientoResponse buscar(@PathVariable LocalDate fecha) {
        return servicio.buscar(fecha);
    }

    @PostMapping("/api/entrenamientos")
    public ResponseEntity<EntrenamientoResponse> crear(@Valid @RequestBody EntrenamientoRequest datos) {
        EntrenamientoResponse creado = servicio.crear(datos);
        return ResponseEntity.created(URI.create("/api/entrenamientos/" + creado.fecha())).body(creado);
    }

    @PutMapping("/api/entrenamientos/{fecha}/asistentes")
    public EntrenamientoResponse pasarLista(@PathVariable LocalDate fecha,
                                            @Valid @RequestBody AsistentesRequest datos) {
        return servicio.pasarLista(fecha, datos.dorsales());
    }

    @DeleteMapping("/api/entrenamientos/{fecha}")
    public ResponseEntity<Void> borrar(@PathVariable LocalDate fecha) {
        servicio.borrar(fecha);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/asistencia")
    public List<AsistenciaResponse> asistenciaDeTodos() {
        return servicio.asistenciaDeTodos();
    }

    @GetMapping("/api/jugadores/{dorsal}/asistencia")
    public AsistenciaResponse asistencia(@PathVariable int dorsal) {
        return servicio.asistencia(dorsal);
    }
}
