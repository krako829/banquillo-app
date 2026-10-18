package com.banquillo.core.excepciones;

public class DorsalRepetidoException extends RuntimeException {
    public DorsalRepetidoException(int dorsal) {
        super("Ya hay un jugador con el dorsal " + dorsal);
    }
}
