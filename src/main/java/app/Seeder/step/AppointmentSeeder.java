package app.seeder.step;

import app.seeder.gen.SeedData;
import app.shared.enums.AppointmentStatus;
import app.shared.enums.AppointmentType;
import app.api.appointment.AppointmentEntity;
import app.api.appointment.AppointmentRepository;
import app.api.doctor.DoctorEntity;
import app.api.patient.PatientEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Appointments on a fixed 30-minute grid (09:00-17:00) over the last 90 / next 30 days. A doctor or a patient is never
 * double-booked, which mirrors AppointmentService.assertNoConflict so the data stays valid for the API.
 */
@Slf4j
@Component
@Order(70)
@RequiredArgsConstructor
public class AppointmentSeeder implements SeedStep {

    private static final int DAYS_BACK = 90;
    private static final int DAYS_AHEAD = 30;
    private static final int SLOTS_PER_DAY = 16;
    private static final int SLOT_MINUTES = 30;
    private static final int MAX_TRIES = 60;

    private final AppointmentRepository appointments;
    private final SeedBatch batch;

    @Override
    public String name() {
        return "appointments";
    }

    @Override
    public int run(SeedContext ctx) {
        int wanted = ctx.getProps().getCounts().getAppointments();
        if (ctx.getDoctors().isEmpty() || ctx.getPatients().isEmpty() || wanted <= 0) {
            return 0;
        }
        Random rnd = ctx.getRandom();
        LocalDate start = LocalDate.now().minusDays(DAYS_BACK);
        int totalDays = DAYS_BACK + DAYS_AHEAD + 1;
        LocalDateTime now = LocalDateTime.now();

        Set<Long> doctorSlots = new HashSet<>();
        Set<Long> patientSlots = new HashSet<>();
        List<AppointmentEntity> list = new ArrayList<>();

        for (int n = 0; n < wanted; n++) {
            AppointmentEntity appt = null;
            for (int t = 0; t < MAX_TRIES && appt == null; t++) {
                DoctorEntity doctor = ctx.pick(ctx.getDoctors());
                PatientEntity patient = ctx.pick(ctx.getPatients());
                int slot = rnd.nextInt(totalDays) * SLOTS_PER_DAY + rnd.nextInt(SLOTS_PER_DAY);
                long doctorKey = doctor.getId() * 100_000L + slot;
                long patientKey = patient.getId() * 100_000L + slot;
                if (doctorSlots.contains(doctorKey) || patientSlots.contains(patientKey)) {
                    continue;
                }
                doctorSlots.add(doctorKey);
                patientSlots.add(patientKey);

                LocalDateTime from = LocalDateTime.of(start.plusDays(slot / SLOTS_PER_DAY),
                        LocalTime.of(9, 0).plusMinutes((long) (slot % SLOTS_PER_DAY) * SLOT_MINUTES));
                AppointmentStatus status = status(rnd, from.isBefore(now));
                appt = AppointmentEntity.builder()
                        .patient(patient)
                        .doctor(doctor)
                        .startTime(from)
                        .endTime(from.plusMinutes(SLOT_MINUTES))
                        .status(status)
                        .type(type(rnd))
                        .reason(ctx.pick(SeedData.APPOINTMENT_REASONS))
                        .notes(ctx.note())
                        .paid(status == AppointmentStatus.COMPLETED || (status == AppointmentStatus.CONFIRMED && rnd.nextBoolean()))
                        .build();
            }
            if (appt == null) {
                log.warn("Appointment grid is nearly full after {} appointments; add doctors or days to seed more", list.size());
                break;
            }
            list.add(appt);
        }
        batch.saveAll(appointments, list);
        ctx.getAppointments().addAll(list);
        return list.size();
    }

    private AppointmentStatus status(Random rnd, boolean past) {
        int r = rnd.nextInt(100);
        if (past) {
            return r < 70 ? AppointmentStatus.COMPLETED : r < 85 ? AppointmentStatus.CANCELLED : AppointmentStatus.NO_SHOW;
        }
        return r < 60 ? AppointmentStatus.SCHEDULED : r < 95 ? AppointmentStatus.CONFIRMED : AppointmentStatus.CANCELLED;
    }

    private AppointmentType type(Random rnd) {
        int r = rnd.nextInt(100);
        return r < 50 ? AppointmentType.CONSULTATION
                : r < 75 ? AppointmentType.FOLLOW_UP
                : r < 85 ? AppointmentType.ONLINE
                : r < 93 ? AppointmentType.EMERGENCY
                : AppointmentType.HOME_VISIT;
    }
}
