package test.test12.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.test12.entity.Appointment;
import test.test12.entity.Doctor;
import test.test12.entity.Patient;
import test.test12.enums.AppointmentStatus;
import test.test12.repository.AppointmentRepository;
import test.test12.repository.DoctorRepository;
import test.test12.repository.PatientRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    private final String[] dayOfWeekArr = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    @PostConstruct
    void init() {
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

    public void getWeeklySchedule(String name, LocalDate date){
        List<Appointment> appointments = appointmentRepository.findByDoctor_NameAndAppointmentDateBetweenAndStatus(
                name
                , LocalDateTime.of(date, LocalTime.of(0,0))
                , LocalDateTime.of(date.plusWeeks(1), LocalTime.of(23,59))
                , AppointmentStatus.CONFIRMED
        );

        Map<Integer,List<Appointment>> resultList = appointments.stream().collect(Collectors.groupingBy(g -> g.getAppointmentDate().getDayOfWeek().getValue()));

        resultList.entrySet().stream().sorted((o1, o2) -> {
            return Integer.compare(o1.getKey(), o2.getKey());
        }).forEach(e -> {
            StringBuilder builder = new StringBuilder();
            builder.append(dayOfWeekArr[e.getKey()]);
            builder.append(": [");

            for( int i = 0 ; i < e.getValue().size() ; i++ ){
                if(i != 0) builder.append(", ");
                builder.append( e.getValue().get(i).getPatient().getName() );
                builder.append("(");
                builder.append( e.getValue().get(i).getAppointmentDate().getHour() );
                builder.append(":");
                builder.append( e.getValue().get(i).getAppointmentDate().getMinute() );
                builder.append(")");
            }

            builder.append("]");

            log.info(builder.toString());
        });

//        Monday: [Alice (10:00), Bob (14:30)]
//        Wednesday: [Charlie (16:00)]
//        Friday: [Bob (09:00)]
    }
}
