package com.banquillo.core.modelo;

public enum Categoria {
    PREBENJAMIN("Prebenjamín"),
    BENJAMIN("Benjamín"),
    ALEVIN("Alevín"),
    INFANTIL("Infantil");

    private final String nombreVisible;

    Categoria(String nombreVisible) {
        this.nombreVisible = nombreVisible;
    }

    public String getNombreVisible() {
        return nombreVisible;
    }
}
