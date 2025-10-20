package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="historias_clinicas")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@AllArgsConstructor
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false, unique = true, length = 50)
    protected String numeroHistoria;

    @Column(nullable = false)
    protected LocalDateTime fechaCreacion;

    @OneToOne(mappedBy = "historiaClinica")
    @ToString.Exclude
    protected Paciente paciente;

    @ElementCollection
    @Setter(AccessLevel.NONE)
    @CollectionTable(name = "diagnosticos", joinColumns = @JoinColumn(name = "historia_id"))
    @Builder.Default
    @Column(length = 500)
    protected final List<String> diagnosticos = new ArrayList<>();

    @ElementCollection
    @Setter(AccessLevel.NONE)
    @CollectionTable(name = "tratamientos", joinColumns = @JoinColumn(name = "historia_id"))
    @Builder.Default
    @Column(length = 500)
    protected final List<String> tratamientos = new ArrayList<>();

    @ElementCollection
    @Setter(AccessLevel.NONE)
    @CollectionTable(name = "alergias", joinColumns = @JoinColumn(name = "historia_id"))
    @Builder.Default
    @Column(length = 200)
    protected final List<String> alergias = new ArrayList<>();

    public HistoriaClinica(Paciente paciente) {
        this.paciente = Objects.requireNonNull(paciente, "El paciente no puede ser nulo");
        this.fechaCreacion = LocalDateTime.now();
        this.numeroHistoria = generarNumeroHistoria();
        this.diagnosticos = new ArrayList<>();
        this.tratamientos = new ArrayList<>();
        this.alergias = new ArrayList<>();
    }

    private String generarNumeroHistoria() {
        return "HC-" + paciente.getDni() + "-" + System.currentTimeMillis();
    }

    public void agregarDiagnostico(String diagnostico) {
        if (diagnostico == null || diagnostico.trim().isEmpty()) throw new IllegalArgumentException("Diagnóstico no puede estar vacío");
        this.diagnosticos.add(diagnostico);
    }

    public void agregarTratamiento(String tratamiento) {
        if (tratamiento == null || tratamiento.trim().isEmpty()) throw new IllegalArgumentException("Tratamiento no puede estar vacío");
        this.tratamientos.add(tratamiento);
    }

    public void agregarAlergia(String alergia) {
        if (alergia == null || alergia.trim().isEmpty()) throw new IllegalArgumentException("Alergia no puede estar vacío");
        this.alergias.add(alergia);
    }

    public List<String> getDiagnosticos() {
        return Collections.unmodifiableList(diagnosticos);
    }

    public List<String> getTratamientos() {
        return Collections.unmodifiableList(tratamientos);
    }

    public List<String> getAlergias() {
        return Collections.unmodifiableList(alergias);
    }
}
