package app.seeder.step;

import app.seeder.gen.SeedData;
import app.seeder.gen.UniqueValues;
import app.shared.enums.GenderEnum;
import app.api.patient.PatientEntity;
import app.api.patient.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static app.seeder.gen.UniqueValues.cut;

@Component
@Order(50)
@RequiredArgsConstructor
public class PatientSeeder implements SeedStep {

    private final PatientRepository patients;
    private final SeedBatch batch;

    @Override
    public String name() {
        return "patients";
    }

    @Override
    public int run(SeedContext ctx) {
        var f = ctx.getFaker();
        var rnd = ctx.getRandom();
        List<PatientEntity> list = new ArrayList<>();
        long existing = patients.count();

        for (int i = 0; i < ctx.getPatientUsers().size(); i++) {
            long n = existing + i + 1;
            list.add(PatientEntity.builder()
                    .user(ctx.getPatientUsers().get(i))
                    .name(cut(f.name().firstName() + " " + f.name().lastName(), 200))
                    .phone(UniqueValues.phone(n + 5_000_000))
                    .idCard(UniqueValues.idCard(n))
                    .dateOfBirth(LocalDate.now().minusDays(365L + rnd.nextInt(365 * 89)))
                    .gender(GenderEnum.values()[rnd.nextInt(GenderEnum.values().length)])
                    .address(cut(f.address().streetAddress(), 255))
                    .city(cut(f.address().city(), 100))
                    .state(cut(f.address().state(), 100))
                    .country(cut(f.address().country(), 100))
                    .postalCode(cut(f.address().zipCode(), 20))
                    .bloodGroup(ctx.pick(SeedData.BLOOD_GROUPS))
                    .emergencyContactName(cut(f.name().firstName() + " " + f.name().lastName(), 150))
                    .emergencyContactPhone(UniqueValues.phone(n + 9_000_000))
                    .allergies(ctx.detail())
                    .notes(ctx.note())
                    .active(true)
                    .build());
        }
        batch.saveAll(patients, list);
        ctx.getPatients().addAll(list);
        return list.size();
    }
}
