package com.banquillo.api.entrenamiento;

import com.banquillo.api.jugador.Jugador;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Entrenamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    private String objetivo;

    @ManyToMany
    @JoinTable(name = "asistencia",
            joinColumns = @JoinColumn(name = "entrenamiento_id"),
            inverseJoinColumns = @JoinColumn(name = "jugador_id"))
    private Set<Jugador> asistentes = new HashSet<>();

    protected Entrenamiento() {
        // Lo necesita JPA
    }

    public Entrenamiento(LocalDate fecha, String objetivo) {
        this.fecha = fecha;
        this.objetivo = objetivo.trim();
    }

    public void cambiarAsistentes(Set<Jugador> nuevos) {
        asistentes.clear();
        asistentes.addAll(nuevos);
    }

    public Long getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public Set<Jugador> getAsistentes() {
        return asistentes;
    }
}
