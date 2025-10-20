package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "salas")
@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @Setter
    @Column(unique = true)
    protected String numero;

    @Setter
    @Column(length = 100)
    protected String tipo;

    @Setter(AccessLevel.PROTECTED)
    @ManyToOne
    @JoinColumn(name = "departamento_id")
    @Builder.Default
    protected Departamento departamento = null;

    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    protected List<Cita> citas = new ArrayList<>();

    public Sala(String numero, String tipo, Departamento departamento) {
        this.numero = validarString(numero, "El número de sala no puede ser nulo ni vacío");
        this.tipo = validarString(tipo, "El tipo de sala no puede ser nulo ni vacío");
        this.departamento = Objects.requireNonNull(departamento, "El departamento no puede ser nulo");
    }

    private String validarString(String valor, String mensajeError) {
        Objects.requireNonNull(valor, mensajeError);
        if (valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensajeError);
        }
        return valor;
    }

    public void addCita(Cita cita){
        this.citas.add(cita);
        cita.setSala(this);
    }

    public List<Cita> getCitas() {
        return Collections.unmodifiableList(citas);
    }

    public List<Cita> getInternalCitas() {
        return citas;
    }
}