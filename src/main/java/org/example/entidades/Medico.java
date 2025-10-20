package org.example.entidades;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Entity
@Table(name = "medicos")
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Medico extends Persona {

    @Enumerated(EnumType.STRING)
    protected EspecialidadMedica especialidadMedica;

    @Embedded
    protected Matricula matricula;

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    protected List<Cita> citas = new ArrayList<>();

    @Setter
    @ManyToOne
    @JoinColumn(name="departamento_id")
    @Builder.Default
    protected Departamento departamento = null;

    protected Medico(MedicoBuilder<?, ?> builder) {
        super(builder);
        this.matricula= Objects.requireNonNull(builder.matricula,"Matricula no puede ser null");
        this.especialidadMedica=Objects.requireNonNull(builder.especialidadMedica, "Especialidad no puede ser null");
        this.citas = new ArrayList<>();
    }

    public void addCita(Cita cita){
        this.citas.add(cita);
        cita.setMedico(this);
    }

    public List<Cita> getCitas() {
        return Collections.unmodifiableList(citas);
    }

    public List<Cita> getInternalCitas() {
        return citas;
    }
}
