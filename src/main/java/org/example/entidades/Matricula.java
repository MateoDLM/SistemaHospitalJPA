package org.example.entidades;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Embeddable
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Matricula {

    protected String numeroMatricula;

    public Matricula(String numeroMatricula) {
        if (!numeroMatricula.matches("MP-\\d{4,6}")) {
            throw new IllegalArgumentException("Formato de matrícula inválido. Debe ser MP-XXXX o MP-XXXXXX");
        }
        this.numeroMatricula = numeroMatricula;
    }
}
