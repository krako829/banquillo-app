package com.banquillo.core.modelo;

public abstract class Persona {
    private final String nombre;

    protected Persona(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public abstract String descripcion();

    @Override
    public String toString() {
        return descripcion();
    }
}
