package com.banquillo.core.excepciones;

public class JugadorNoEncontradoException extends RuntimeException {
    public JugadorNoEncontradoException(int dorsal) {
        super("No hay ningún jugador con el dorsal " + dorsal);
    }
}
