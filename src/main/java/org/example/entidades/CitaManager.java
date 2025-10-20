package org.example.entidades;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CitaManager {
    private final List<Cita> citas = new ArrayList<>();
    private final Map<Paciente, List<Cita>> citasPorPaciente = new HashMap<>();
    private final Map<Medico, List<Cita>> citasPorMedico = new HashMap<>();
    private final Map<Sala, List<Cita>> citasPorSala = new HashMap<>();

    public Cita programarCita(Paciente paciente, Medico medico, Sala sala, String observaciones, LocalDateTime fechaHora, BigDecimal costo) throws CitaException {

        validarCita(fechaHora, costo);

        if (!esMedicoDisponible(medico, fechaHora)) {
            throw new CitaException("El médico no está disponible en la fecha y hora solicitadas.");
        }

        if (!esSalaDisponible(sala, fechaHora)) {
            throw new CitaException("La sala no está disponible en la fecha y hora solicitadas.");
        }

        if (!medico.getEspecialidadMedica().equals(sala.getDepartamento().getEspecialidadMedica())) {
            throw new CitaException("La especialidad del médico no coincide con el departamento de la sala.");
        }

        Cita cita = new Cita(fechaHora, costo, observaciones, paciente, medico, sala);
        citas.add(cita);

        actualizarIndicePaciente(paciente, cita);
        actualizarIndiceMedico(medico, cita);
        actualizarIndiceSala(sala, cita);

        paciente.addCita(cita);
        medico.addCita(cita);
        sala.addCita(cita);

        return cita;
    }

    private void validarCita(LocalDateTime fechaHora, BigDecimal costo) throws CitaException {
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new CitaException("No se puede programar una cita en el pasado.");
        }

        if (costo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CitaException("El costo debe ser mayor que cero.");
        }
    }

    private boolean esMedicoDisponible(Medico medico, LocalDateTime fechaHora) {
        List<Cita> citasExistentes = citasPorMedico.get(medico);
        if (citasExistentes != null) {
            for (Cita citaExistente : citasExistentes) {
                if (Math.abs(citaExistente.getFechaHora().compareTo(fechaHora)) < 2) { // 2 horas de diferencia
                    return false;
                }
            }
        }
        return true;
    }

    private boolean esSalaDisponible(Sala sala, LocalDateTime fechaHora) {
        List<Cita> citasExistentes = citasPorSala.get(sala);
        if (citasExistentes != null) {
            for (Cita citaExistente : citasExistentes) {
                if (Math.abs(citaExistente.getFechaHora().compareTo(fechaHora)) < 2) { // 2 horas de diferencia
                    return false;
                }
            }
        }
        return true;
    }

    private void actualizarIndicePaciente(Paciente paciente, Cita cita) {
        List<Cita> citasPaciente = citasPorPaciente.get(paciente);
        if (citasPaciente == null) {
            citasPaciente = new ArrayList<>();
            citasPorPaciente.put(paciente, citasPaciente);
        }
        citasPaciente.add(cita);
    }

    private void actualizarIndiceMedico(Medico medico, Cita cita) {
        List<Cita> citasMedico = citasPorMedico.get(medico);
        if (citasMedico == null) {
            citasMedico = new ArrayList<>();
            citasPorMedico.put(medico, citasMedico);
        }
        citasMedico.add(cita);
    }

    private void actualizarIndiceSala(Sala sala, Cita cita) {
        List<Cita> citasSala = citasPorSala.get(sala);
        if (citasSala == null) {
            citasSala = new ArrayList<>();
            citasPorSala.put(sala, citasSala);
        }
        citasSala.add(cita);
    }

    public List<Cita> getCitasPorPaciente(Paciente paciente) {
        List<Cita> citasPaciente = citasPorPaciente.get(paciente);
        if (citasPaciente != null) {
            return Collections.unmodifiableList(citasPaciente);
        } else {
            return Collections.emptyList();
        }
    }

    public List<Cita> getCitasPorMedico(Medico medico) {
        List<Cita> citasMedico = citasPorMedico.get(medico);
        if (citasMedico != null) {
            return Collections.unmodifiableList(citasMedico);
        } else {
            return Collections.emptyList();
        }
    }

    public List<Cita> getCitasPorSala(Sala sala) {
        List<Cita> citasSala = citasPorSala.get(sala);
        if (citasSala != null) {
            return Collections.unmodifiableList(citasSala);
        } else {
            return Collections.emptyList();
        }
    }

    public List<Cita> getAllCitas() {
        return Collections.unmodifiableList(citas);
    }

    public long contarCitasPorEstado(EstadoCita estado) {
        return citas.stream()
                .filter(c -> c.getEstadoCita() == estado)
                .count();
    }

    public void cancelarCita(Cita cita, String motivoCancelacion) throws CitaException {
        if (cita.getEstadoCita() != EstadoCita.PROGRAMADA) {
            throw new CitaException("Solo se pueden cancelar citas en estado PROGRAMADA");
        }
        cita.setEstadoCita(EstadoCita.CANCELADA);
        cita.setObservaciones((cita.getObservaciones() != null ? cita.getObservaciones() + " | " : "") + "Cancelada: " + motivoCancelacion);

        cita.getMedico().getInternalCitas().remove(cita);
        citasPorMedico.get(cita.getMedico()).remove(cita);

        cita.getSala().getInternalCitas().remove(cita);
        citasPorSala.get(cita.getSala()).remove(cita);

        cita.getPaciente().getInternalCitas().remove(cita);
        citasPorPaciente.get(cita.getPaciente()).remove(cita);
    }
}


