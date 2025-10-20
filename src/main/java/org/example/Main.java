package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.example.entidades.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando persistencia");
        EntityManagerFactory emf= Persistence.createEntityManagerFactory("hospital-persistence-unit");
        EntityManager em= emf.createEntityManager();
        try {
            Hospital hospital = Hospital.builder()
                    .nombre("Hospital Español")
                    .telefono("2614133300")
                    .direccion("San Martin 965, Godoy Cruz, Mendoza")
                    .build();

            Departamento cardiologia = Departamento.builder()
                    .nombre("Cardiologia")
                    .especialidadMedica(EspecialidadMedica.CARDIOLOGIA)
                    .build();
            Departamento pediatria = Departamento.builder()
                    .nombre("Pediatria")
                    .especialidadMedica(EspecialidadMedica.PEDIATRIA)
                    .build();
            Departamento traumatologia = Departamento.builder()
                    .nombre("Traumatologia")
                    .especialidadMedica(EspecialidadMedica.TRAUMATOLOGIA)
                    .build();
            hospital.agregarDepartamento(cardiologia);
            hospital.agregarDepartamento(pediatria);
            hospital.agregarDepartamento(traumatologia);

            Sala sC1 = Sala.builder()
                    .numero("C101")
                    .tipo("Consultorio")
                    .build();
            Sala sC2 = Sala.builder()
                    .numero("C102")
                    .tipo("Quirofano")
                    .build();
            Sala sP1 = Sala.builder()
                    .numero("P201")
                    .tipo("Consultorio")
                    .build();
            Sala sP2 = Sala.builder()
                    .numero("P202")
                    .tipo("Sala de Observacion")
                    .build();
            Sala sT1 = Sala.builder()
                    .numero("T301")
                    .tipo("Consultorio")
                    .build();
            Sala sT2 = Sala.builder()
                    .numero("T302")
                    .tipo("Emergencias")
                    .build();
            cardiologia.agregarSala(sC1);
            cardiologia.agregarSala(sC2);
            pediatria.agregarSala(sP1);
            pediatria.agregarSala(sP2);
            traumatologia.agregarSala(sT1);
            traumatologia.agregarSala(sT2);

            Medico cardiologo = Medico.builder()
                    .nombre("Marcos")
                    .apellido("Fernandez")
                    .dni("17855455")
                    .fechaNacimiento(LocalDate.of(1968, 1, 18))
                    .tipoSangre(TipoSangre.A_NEGATIVO)
                    .matricula(new Matricula("MP-5588"))
                    .especialidadMedica(EspecialidadMedica.CARDIOLOGIA)
                    .build();
            Medico pediatra = Medico.builder()
                    .nombre("Maria")
                    .apellido("Gomez")
                    .dni("25669847")
                    .fechaNacimiento(LocalDate.of(1974, 5, 6))
                    .tipoSangre(TipoSangre.O_NEGATIVO)
                    .matricula(new Matricula("MP-14558"))
                    .especialidadMedica(EspecialidadMedica.PEDIATRIA)
                    .build();
            Medico traumatologo = Medico.builder()
                    .nombre("Pedro")
                    .apellido("Lopez")
                    .dni("6478120")
                    .fechaNacimiento(LocalDate.of(1941, 9, 8))
                    .tipoSangre(TipoSangre.A_NEGATIVO)
                    .matricula(new Matricula("MP-459684"))
                    .especialidadMedica(EspecialidadMedica.TRAUMATOLOGIA)
                    .build();

            cardiologia.agregarMedico(cardiologo);
            pediatria.agregarMedico(pediatra);
            traumatologia.agregarMedico(traumatologo);

            Paciente paciente1 = Paciente.builder()
                    .nombre("Ramon")
                    .apellido("Dino")
                    .dni("41202056")
                    .fechaNacimiento(LocalDate.of(2001, 2, 3))
                    .tipoSangre(TipoSangre.A_POSITIVO)
                    .direccion("Beltran 500, Godoy Cruz")
                    .telefono("2615444744")
                    .build();
            Paciente paciente2 = Paciente.builder()
                    .nombre("Chris")
                    .apellido("Bumstead")
                    .dni("36444158")
                    .fechaNacimiento(LocalDate.of(1997, 12, 3))
                    .tipoSangre(TipoSangre.AB_NEGATIVO)
                    .direccion("Godoy Cruz 500, Guaymallen")
                    .telefono("2615778989")
                    .build();
            Paciente paciente3 = Paciente.builder()
                    .nombre("Urs")
                    .apellido("Kalecinski")
                    .dni("96412120")
                    .fechaNacimiento(LocalDate.of(2019, 8, 27))
                    .tipoSangre(TipoSangre.B_POSITIVO)
                    .direccion("Viamonte 2043, Lujan de Cuyo")
                    .telefono("2614021597")
                    .build();
            paciente1.getHistoriaClinica().agregarTratamiento("Ibuprofeno cada 8 horas");
            paciente2.getHistoriaClinica().agregarAlergia("Mani");
            paciente3.getHistoriaClinica().agregarDiagnostico("Celiaquia");
            hospital.agregarPaciente(paciente1);
            hospital.agregarPaciente(paciente2);
            hospital.agregarPaciente(paciente3);

            em.getTransaction().begin();
            em.persist(hospital);
            // No es necesario persistir Salas y Médicos manualmente,
            // ya se persisten por cascade desde Hospital -> Departamentos -> Salas/Médicos

            CitaManager citaManager = new CitaManager();
            Cita cita1 = null;
            Cita cita2 = null;
            Cita cita3 = null;
            try {
                cita1 = citaManager.programarCita(
                        paciente1,
                        cardiologo,
                        sC2,
                        "",
                        LocalDateTime.of(2025, 12, 12, 10, 0),
                        new BigDecimal("50000.00")
                );
                cita2 = citaManager.programarCita(
                        paciente2,
                        pediatra,
                        sP1,
                        "Atencion especial",
                        LocalDateTime.of(2025, 11, 12, 15, 0),
                        new BigDecimal("100000.00")
                );
                cita3 = citaManager.programarCita(
                        paciente3,
                        traumatologo,
                        sT1,
                        "Dolor en la rodilla",
                        LocalDateTime.of(2025, 12, 10, 13, 0),
                        new BigDecimal("250000.00")
                );
            } catch (CitaException c) {
                System.err.println("Error al programar cita: " + c.getMessage());
            }
            if (cita1!=null && cita2!=null && cita3!=null) {
                em.persist(cita1);
                em.persist(cita2);
                em.persist(cita3);
            }
            em.getTransaction().commit();
            System.out.println("Datos persistidos");

            System.out.println("--------------------CONSULTAS JPQL--------------------");
            System.out.println("--------------------HOSPITALES--------------------");
            TypedQuery<Hospital> hospitalTypedQuery = em.createQuery("SELECT h FROM Hospital h", Hospital.class);
            List<Hospital> hospitales = hospitalTypedQuery.getResultList();
            for (Hospital h : hospitales) {
                System.out.println(h);
            }

            System.out.println("--------------------MEDICOS--------------------");
            TypedQuery<Medico> queryMedicosCardiologia = em.createQuery("SELECT m FROM Medico m WHERE m.especialidadMedica = :especialidad", Medico.class);
            queryMedicosCardiologia.setParameter("especialidad", EspecialidadMedica.CARDIOLOGIA);
            List<Medico> cardiologos = queryMedicosCardiologia.getResultList();
            for (Medico m : cardiologos) {
                System.out.println(m);
            }

            TypedQuery<Medico> queryMedicosPediatria = em.createQuery("SELECT m FROM Medico m WHERE m.especialidadMedica = :especialidad", Medico.class);
            queryMedicosPediatria.setParameter("especialidad", EspecialidadMedica.PEDIATRIA);
            List<Medico> pediatras = queryMedicosPediatria.getResultList();
            for (Medico m : pediatras) {
                System.out.println(m);
            }

            TypedQuery<Medico> queryMedicosTraumatologia = em.createQuery("SELECT m FROM Medico m WHERE m.especialidadMedica = :especialidad", Medico.class);
            queryMedicosTraumatologia.setParameter("especialidad", EspecialidadMedica.TRAUMATOLOGIA);
            List<Medico> traumatologos = queryMedicosTraumatologia.getResultList();
            for (Medico m : traumatologos) {
                System.out.println(m);
            }

            System.out.println("--------------------PACIENTES--------------------");
            TypedQuery<Paciente> queryPacientes = em.createQuery("SELECT p FROM Paciente p", Paciente.class);
            List<Paciente> pacientes = queryPacientes.getResultList();
            for (Paciente p : pacientes) {
                System.out.println(p);
            }

            System.out.println("--------------------CITAS--------------------");
            TypedQuery<Cita> queryCitasProgramadas = em.createQuery("SELECT c FROM Cita c WHERE c.estadoCita = :estadoCita", Cita.class);
            queryCitasProgramadas.setParameter("estadoCita", EstadoCita.PROGRAMADA);
            List<Cita> citasProgramadas = queryCitasProgramadas.getResultList();
            for (Cita c : citasProgramadas) {
                System.out.println(c);
            }
            if (cita1!=null){
                em.getTransaction().begin();
                cita1.setEstadoCita(EstadoCita.COMPLETADA);
                em.merge(cita1);
                em.getTransaction().commit();
                System.out.println("Cita actualizada");
            }

            System.out.println("--------------------ESTADISTICAS CON COUNT--------------------");
            System.out.println("--------------------MÉDICOS POR ESPECIALIDAD--------------------");
            for (EspecialidadMedica especialidad : EspecialidadMedica.values()) {
                TypedQuery<Long> queryMedicos = em.createQuery("SELECT COUNT(m) FROM Medico m WHERE m.especialidadMedica = :especialidad", Long.class);
                queryMedicos.setParameter("especialidad", especialidad);
                Long count = queryMedicos.getSingleResult();
                if (count > 0) {System.out.println(especialidad.getDescripcion() + ": " + count);}
            }

            System.out.println("--------------------CITAS POR ESTADO--------------------");
            for (EstadoCita estado : EstadoCita.values()) {
                TypedQuery<Long> queryCitas = em.createQuery("SELECT COUNT(c) FROM Cita c WHERE c.estadoCita = :estadoCita", Long.class);
                queryCitas.setParameter("estadoCita", estado);
                Long count = queryCitas.getSingleResult();
                if (count > 0) {System.out.println(estado.name() + ": " + count);}
            }

            System.out.println("--------------------TOTALES--------------------");
            System.out.println("--------------------PACIENTES--------------------");
            TypedQuery<Long> queryTotalPacientes = em.createQuery("SELECT COUNT(p) FROM Paciente p", Long.class);
            Long totalPacientes = queryTotalPacientes.getSingleResult();
            System.out.println("Total de pacientes: " + totalPacientes);

            System.out.println("--------------------SALAS--------------------");
            TypedQuery<Long> queryTotalSalas = em.createQuery("SELECT COUNT(s) FROM Sala s", Long.class);
            Long totalSalas = queryTotalSalas.getSingleResult();
            System.out.println("Total de salas: " + totalSalas);

            System.out.println("--------------------MEDICOS--------------------");
            TypedQuery<Long> queryTotalMedicos = em.createQuery("SELECT COUNT(m) FROM Medico m", Long.class);
            Long totalMedicos = queryTotalMedicos.getSingleResult();
            System.out.println("Total de médicos: " + totalMedicos);

            System.out.println("--------------------CITAS--------------------");
            TypedQuery<Long> queryTotalCitas = em.createQuery("SELECT COUNT(c) FROM Cita c", Long.class);
            Long totalCitas = queryTotalCitas.getSingleResult();
            System.out.println("Total de citas: " + totalCitas);


            System.out.println("SISTEMA EJECUTADO EXITOSAMENTE");
        }
        catch (Exception e){
            if (em.getTransaction().isActive()) {em.getTransaction().rollback();}
            e.printStackTrace();
            System.err.println("Error en la ejecución: " + e.getMessage());
        }
        finally {
            em.close();
            emf.close();
        }
    }
}