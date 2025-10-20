package org.example.entidades;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Objects;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public abstract class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    protected String nombre;

    protected String apellido;

    protected String dni;

    protected LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    protected TipoSangre tipoSangre;

    protected Persona(String nombre, String apellido, LocalDate fechaNacimiento, String dni, TipoSangre tipoSangre) {
        this.nombre = validarString(nombre, "El nombre no puede ser vacío");
        this.apellido = validarString(apellido, "El apellido no puede ser vacío");
        this.fechaNacimiento = Objects.requireNonNull(fechaNacimiento, "La fecha no puede ser nula");
        this.dni = validarDNI(dni);
        this.tipoSangre = tipoSangre;
    }

    public String getNombreCompleto(){
        return apellido + ", " + nombre;
    }

    public int getEdad(){
        return LocalDate.now().getYear() - fechaNacimiento.getYear();
    }

    public String validarString(String valor, String nombreCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(nombreCampo + " no puede estar vacío ni ser nulo");
        }
        return valor;
    }

    public String validarDNI(String dni) {
        if (!dni.matches("\\d{7,8}")){
            throw new IllegalArgumentException("DNI invalido");
        }
        return dni;
    }
}