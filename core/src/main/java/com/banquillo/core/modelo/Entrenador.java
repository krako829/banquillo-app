package com.banquillo.core.modelo;

public class Entrenador extends Persona implements Notificable {
    private final String licencia;
    private final String contacto;

    public Entrenador(String nombre, String licencia, String contacto) {
        super(nombre);
        if (licencia == null || licencia.isBlank()) {
            throw new IllegalArgumentException("La licencia no puede estar vacía");
        }
        this.licencia = licencia.trim();
        this.contacto = (contacto == null || contacto.isBlank()) ? null : contacto.trim();
    }

    public String getLicencia() {
        return licencia;
    }

    @Override
    public String getContacto() {
        return contacto;
    }

    @Override
    public String descripcion() {
        return "Entrenador: " + getNombre() + " (" + licencia + ")";
    }
}
