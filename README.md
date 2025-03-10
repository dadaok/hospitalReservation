### **📌 JPA + Java Stream을 활용한 고급 구현 문제**
---

## **🏥 문제: 병원 예약 시스템**
당신은 **병원 예약 관리 시스템**을 개발하는 팀에 합류했습니다.  
데이터베이스에는 환자의 예약 정보가 저장되어 있으며, **특정 의사의 주간 예약 스케줄을 생성하는 기능**을 구현해야 합니다.

---

## **📂 데이터베이스 테이블**
### **1️⃣ `Doctor` (의사)**
| 필드명  | 타입  | 설명  |
|---------|------|------|
| `id`  | `Long` | 의사 ID (Primary Key) |
| `name` | `String` | 의사 이름 |
| `specialty` | `String` | 전문 분야 |

---

### **2️⃣ `Patient` (환자)**
| 필드명  | 타입  | 설명  |
|---------|------|------|
| `id`  | `Long` | 환자 ID (Primary Key) |
| `name` | `String` | 환자 이름 |
| `birthDate` | `LocalDate` | 생년월일 |

---

### **3️⃣ `Appointment` (예약)**
| 필드명  | 타입  | 설명  |
|---------|------|------|
| `id`  | `Long` | 예약 ID (Primary Key) |
| `doctor` | `Doctor` | 예약한 의사 (Many-to-One 관계) |
| `patient` | `Patient` | 예약한 환자 (Many-to-One 관계) |
| `appointmentDate` | `LocalDateTime` | 예약 날짜 및 시간 |
| `status` | `AppointmentStatus` | 예약 상태 (`CONFIRMED`, `CANCELED`, `PENDING`) |

---

## **📌 요구사항**
1. 특정 의사의 **주간 예약 스케줄을 조회하는 기능**을 구현하세요.
2. `JPA`를 사용하여 데이터를 조회하고, `Java Stream`을 활용하여 **요일별 예약 리스트를 정리**하세요.
3. `PENDING` 상태의 예약은 포함하지 않고, `CONFIRMED` 상태의 예약만 반환하세요.
4. 결과는 **요일별로 정리하여 반환**해야 합니다. (`Monday → Sunday` 순서)

---

## **📌 구현 예시**
### ✅ **입력**

```java
getWeeklySchedule(doctorId, LocalDate.of(2024, 3, 4)); // 2024년 3월 4일(월)부터 1주일간 조회
```

---

## **📌 샘플 데이터 및 테스트**
### **✅ 샘플 데이터 삽입**
#### **`CommandLineRunner`로 초기 데이터 추가**
```java
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public DataInitializer(DoctorRepository doctorRepository, PatientRepository patientRepository, AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 의사 데이터 저장
        Doctor doctor1 = doctorRepository.save(new Doctor("Dr. Smith", "Cardiology"));

        // 환자 데이터 저장
        Patient alice = patientRepository.save(new Patient("Alice", LocalDate.of(1990, 5, 20)));
        Patient bob = patientRepository.save(new Patient("Bob", LocalDate.of(1985, 8, 15)));
        Patient charlie = patientRepository.save(new Patient("Charlie", LocalDate.of(1995, 11, 3)));

        // 예약 데이터 저장
        List<Appointment> appointments = List.of(
                new Appointment(doctor1, alice, LocalDateTime.of(2024, 3, 4, 10, 0), AppointmentStatus.CONFIRMED),
                new Appointment(doctor1, bob, LocalDateTime.of(2024, 3, 4, 14, 30), AppointmentStatus.CONFIRMED),
                new Appointment(doctor1, charlie, LocalDateTime.of(2024, 3, 6, 16, 0), AppointmentStatus.CONFIRMED),
                new Appointment(doctor1, bob, LocalDateTime.of(2024, 3, 8, 9, 0), AppointmentStatus.CONFIRMED),
                new Appointment(doctor1, alice, LocalDateTime.of(2024, 3, 5, 11, 0), AppointmentStatus.PENDING) // PENDING 예약 (제외됨)
        );
        appointmentRepository.saveAll(appointments);
    }
}
```

---

### **✅ 실행 결과**
```
Monday: [Alice (10:00), Bob (14:30)]
Wednesday: [Charlie (16:00)]
Friday: [Bob (09:00)]
```