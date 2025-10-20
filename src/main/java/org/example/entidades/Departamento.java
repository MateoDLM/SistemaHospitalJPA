package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "departamentos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@ToString
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Setter
    @Column(length = 150)
    protected String nombre;

    @Setter(AccessLevel.NONE)
    @Enumerated(EnumType.STRING)
    protected EspecialidadMedica especialidadMedica;

    @Setter
    @ManyToOne
    @JoinColumn(name = "hospital_id")
    @ToString.Exclude
    protected Hospital hospital;

    @Builder.Default
    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    protected List<Medico> medicos = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    protected List<Sala> salas = new ArrayList<>();

    public Departamento(String nombre, EspecialidadMedica especialidadMedica, Hospital hospital) {
        this.nombre = validarString(nombre, "Nombre del departamento");
        this.especialidadMedica = Objects.requireNonNull(especialidadMedica, "Especialidad no puede ser null");
        this.hospital = hospital;
        this.medicos = new ArrayList<>();
        this.salas = new ArrayList<>();
    }
    private String validarString(String valor, String nombreCampo) {
        if (valor == null || valor.trim().isEmpty()) throw new IllegalArgumentException(nombreCampo + " no puede estar vacío");
        return valor;
    }

    public void agregarMedico(Medico medico) {
        Objects.requireNonNull(medico, "El médico no puede ser nulo");
        if (!medico.getEspecialidadMedica().equals(this.especialidadMedica)) {
            throw new IllegalArgumentException("Especialidad incompatible entre médico y departamento");
        }
        if (!medicos.contains(medico)) {
            this.medicos.add(medico);
            medico.setDepartamento(this);
        }
    }

    public void agregarSala(Sala sala){
        this.salas.add(sala);
        sala.setDepartamento(this);
    }

    public List<Medico> getMedicos() {
        return Collections.unmodifiableList(medicos);
    }

    public List<Sala> getSalas() {
        return Collections.unmodifiableList(salas);
    }
}
