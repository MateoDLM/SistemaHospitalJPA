package org.example.entidades;

public class CitaException extends Exception {
    public CitaException(String mensaje) {
        super(mensaje);
    }

    public CitaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
