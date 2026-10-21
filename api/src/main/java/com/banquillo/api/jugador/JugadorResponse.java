package com.banquillo.api.jugador;

public record JugadorResponse(String nombre, int dorsal, Posicion posicion, String contactoFamilia) {

    public static JugadorResponse de(Jugador jugador) {
        return new JugadorResponse(jugador.getNombre(), jugador.getDorsal(),
                jugador.getPosicion(), jugador.getContactoFamilia());
    }
}
