package com.banquillo.api.jugador;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private int dorsal;

    @Enumerated(EnumType.STRING)
    private Posicion posicion;

    @Column(name = "contacto_familia")
    private String contactoFamilia;

    protected Jugador() {
        // Lo necesita JPA
    }

    public Jugador(String nombre, int dorsal, Posicion posicion, String contactoFamilia) {
        actualizar(nombre, dorsal, posicion, contactoFamilia);
    }

    public void actualizar(String nombre, int dorsal, Posicion posicion, String contactoFamilia) {
        this.nombre = nombre.trim();
        this.dorsal = dorsal;
        this.posicion = posicion;
        this.contactoFamilia = (contactoFamilia == null || contactoFamilia.isBlank()) ? null : contactoFamilia.trim();
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDorsal() {
        return dorsal;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public String getContactoFamilia() {
        return contactoFamilia;
    }
}
