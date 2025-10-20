package org.example.entidades;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="pacientes")
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Paciente extends Persona{

    @Setter
    @Column(length = 300)
    protected String direccion;

    @Setter
    @Column(length = 20)
    protected String telefono;

    @OneToOne(cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "historiaClinica_id",unique = true)
    protected HistoriaClinica historiaClinica;

    @Setter
    @ManyToOne
    @JoinColumn(name = "hospital_id")
    @Builder.Default
    protected Hospital hospital = null;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    protected List<Cita> citas = new ArrayList<>();

    protected Paciente(PacienteBuilder<?, ?> builder) {
        super(builder);
        this.telefono = validarString(builder.telefono, "El teléfono no puede estar vacío");
        this.direccion = validarString(builder.direccion, "La dirección no puede estar vacía");
        this.historiaClinica = new HistoriaClinica(this);
        this.citas = new ArrayList<>();
    }

    public void addCita(Cita cita){
        this.citas.add(cita);
        cita.setPaciente(this);
    }
    public List<Cita> getCitas() {
        return Collections.unmodifiableList(citas);
    }

    public List<Cita> getInternalCitas() {
        return citas;
    }
}