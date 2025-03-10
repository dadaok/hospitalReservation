package test.test12.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import test.test12.entity.Appointment;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppointmentServiceTest {

    @Autowired
    private AppointmentService appointmentService;

    @Test
    public void 예약정보조회() {
        // given
        LocalDate date = LocalDate.of(2024, 3, 4);
        // when
        appointmentService.getWeeklySchedule("Dr. Smith", date);
        // then

    }

}