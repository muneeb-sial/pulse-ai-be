package app.seeder.step;

import app.seeder.gen.SeedData;
import app.shared.enums.AppointmentStatus;
import app.api.appointment.AppointmentEntity;
import app.api.medicine.MedicineEntity;
import app.api.prescription.PrescriptionEntity;
import app.api.prescription.PrescriptionItemEntity;
import app.api.prescription.PrescriptionItemRepository;
import app.api.prescription.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Prescriptions (with items) for a share of the COMPLETED appointments, dated the day of the visit. */
@Component
@Order(90)
@RequiredArgsConstructor
public class PrescriptionSeeder implements SeedStep {

    private final PrescriptionRepository prescriptions;
    private final PrescriptionItemRepository items;
    private final SeedBatch batch;

    @Override
    public String name() {
        return "prescriptions + items";
    }

    @Override
    public int run(SeedContext ctx) {
        if (ctx.getMedicines().isEmpty()) {
            return 0;
        }
        var counts = ctx.getProps().getCounts();
        int maxItems = Math.max(1, Math.min(counts.getMaxItemsPerPrescription(), ctx.getMedicines().size()));

        List<PrescriptionEntity> rx = new ArrayList<>();
        for (AppointmentEntity a : ctx.getAppointments()) {
            if (a.getStatus() == AppointmentStatus.COMPLETED && ctx.getRandom().nextDouble() < counts.getPrescriptionRate()) {
                rx.add(PrescriptionEntity.builder()
                        .patient(a.getPatient())
                        .doctor(a.getDoctor())
                        .appointment(a)
                        .prescribedDate(a.getStartTime().toLocalDate())
                        .diagnosis(ctx.detail())
                        .notes(ctx.note())
                        .build());
            }
        }
        batch.saveAll(prescriptions, rx);

        List<PrescriptionItemEntity> list = new ArrayList<>();
        for (PrescriptionEntity p : rx) {
            int n = 1 + ctx.getRandom().nextInt(maxItems);
            Set<Long> picked = new HashSet<>();
            for (int i = 0; i < n; i++) {
                MedicineEntity m = ctx.pick(ctx.getMedicines());
                if (!picked.add(m.getId())) {
                    continue; // same medicine twice on one prescription
                }
                list.add(PrescriptionItemEntity.builder()
                        .prescription(p)
                        .medicine(m)
                        .quantity(1 + ctx.getRandom().nextInt(30))
                        .dosage(ctx.pick(SeedData.DOSAGES))
                        .frequency(ctx.pick(SeedData.FREQUENCIES))
                        .duration(ctx.pick(SeedData.DURATIONS))
                        .instructions(ctx.detail())
                        .build());
            }
        }
        batch.saveAll(items, list);
        return rx.size() + list.size();
    }
}
