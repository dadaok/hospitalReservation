package test.test12.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import test.test12.entity.Appointment;
import test.test12.enums.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @EntityGraph(attributePaths = {"patient"})
    List<Appointment> findByDoctor_NameAndAppointmentDateBetweenAndStatus(String name, LocalDateTime start, LocalDateTime end, AppointmentStatus status);
}
