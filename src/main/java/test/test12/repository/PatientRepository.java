package test.test12.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.test12.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
