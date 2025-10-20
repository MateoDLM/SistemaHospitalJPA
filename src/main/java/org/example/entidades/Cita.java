package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "citas")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Setter(AccessLevel.NONE)
    @Column(nullable = false, precision = 15, scale = 2)
    protected BigDecimal costo;

    @Setter
    @Enumerated(EnumType.STRING)
    @Builder.Default
    protected EstadoCita estadoCita = EstadoCita.PROGRAMADA;

    @Setter
    @Column(length = 1000)
    @Builder.Default
    protected String observaciones = "";

    @Setter
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "paciente_id")
    @ToString.Exclude
    protected Paciente paciente;

    @Setter
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "medico_id")
    @ToString.Exclude
    protected Medico medico;

    @Setter
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "sala_id")
    @ToString.Exclude
    protected Sala sala;

    public Cita(LocalDateTime fechaHora, BigDecimal costo, String observaciones, Paciente paciente, Medico medico, Sala sala) {
        this.fechaHora = fechaHora;
        this.costo =  Objects.requireNonNull(costo, "Costo");
        this.observaciones = observaciones;
        this.paciente = Objects.requireNonNull(paciente, "Paciente");
        this.medico = Objects.requireNonNull(medico, "Medico");
        this.sala = Objects.requireNonNull(sala, "Sala");
        this.estadoCita = EstadoCita.PROGRAMADA;
    }

    public String toCsvString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                paciente.getDni(),
                medico.getDni(),
                sala.getNumero(),
                fechaHora.toString(),
                costo.toPlainString(),
                estadoCita.name(),
                observaciones.replaceAll(",", ";")
        );}
}
