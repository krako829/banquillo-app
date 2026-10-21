package com.banquillo.api.error;

public class NoEncontradoException extends RuntimeException {
    public NoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
