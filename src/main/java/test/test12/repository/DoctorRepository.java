package test.test12.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.test12.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
