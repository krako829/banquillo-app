package com.banquillo.core.modelo;

import java.util.Objects;

public class Jugador extends Persona implements Notificable {
    private final int dorsal;
    private final Posicion posicion;
    private final String contactoFamilia;

    public Jugador(String nombre, int dorsal, Posicion posicion) {
        this(nombre, dorsal, posicion, null);
    }

    public Jugador(String nombre, int dorsal, Posicion posicion, String contactoFamilia) {
        super(nombre);
        if (dorsal < 1 || dorsal > 99) {
            throw new IllegalArgumentException("El dorsal debe estar entre 1 y 99");
        }
        if (posicion == null) {
            throw new IllegalArgumentException("La posición es obligatoria");
        }
        this.dorsal = dorsal;
        this.posicion = posicion;
        this.contactoFamilia = (contactoFamilia == null || contactoFamilia.isBlank()) ? null : contactoFamilia.trim();
    }

    public int getDorsal() {
        return dorsal;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    @Override
    public String getContacto() {
        return contactoFamilia;
    }

    @Override
    public String descripcion() {
        return dorsal + " · " + getNombre() + " (" + posicion + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Jugador otro)) return false;
        return dorsal == otro.dorsal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dorsal);
    }
}
