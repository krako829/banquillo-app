package com.banquillo.core.excepciones;

import java.time.LocalDate;

public class EntrenamientoDuplicadoException extends RuntimeException {
    public EntrenamientoDuplicadoException(LocalDate fecha) {
        super("Ya hay un entrenamiento el " + fecha);
    }
}
