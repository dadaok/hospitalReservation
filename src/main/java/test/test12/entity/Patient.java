package test.test12.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tb_patient")
@NoArgsConstructor
@Getter
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate birthDate;

    public Patient(String name, LocalDate birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }
}