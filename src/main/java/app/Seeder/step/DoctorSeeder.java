package app.seeder.step;

import app.seeder.gen.SeedData;
import app.seeder.gen.UniqueValues;
import app.api.department.DepartmentEntity;
import app.api.doctor.DoctorEntity;
import app.api.doctor.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static app.seeder.gen.UniqueValues.cut;

@Component
@Order(40)
@RequiredArgsConstructor
public class DoctorSeeder implements SeedStep {

    private final DoctorRepository doctors;
    private final SeedBatch batch;

    @Override
    public String name() {
        return "doctors";
    }

    @Override
    public int run(SeedContext ctx) {
        var f = ctx.getFaker();
        List<DoctorEntity> list = new ArrayList<>();
        long existing = doctors.count();

        for (int i = 0; i < ctx.getDoctorUsers().size(); i++) {
            DepartmentEntity dept = ctx.getDepartments().isEmpty() ? null : ctx.pick(ctx.getDepartments());
            String baseDept = dept == null ? "General Medicine" : dept.getName().replaceAll(" Unit \\d+$", "");
            List<String> specs = SeedData.SPECIALIZATIONS.getOrDefault(baseDept, List.of("General Physician"));
            String name = "Dr. " + f.name().firstName() + " " + f.name().lastName();
            int years = 1 + ctx.getRandom().nextInt(35);

            list.add(DoctorEntity.builder()
                    .user(ctx.getDoctorUsers().get(i))
                    .department(dept)
                    .name(cut(name, 100))
                    .phone(UniqueValues.phone(existing + i + 1))
                    .licenseNumber(UniqueValues.license(existing + i + 1))
                    .specialization(ctx.pick(specs))
                    .qualification(ctx.pick(SeedData.QUALIFICATIONS))
                    .address(cut(f.address().streetAddress(), 255))
                    .city(cut(f.address().city(), 100))
                    .state(cut(f.address().state(), 100))
                    .country(cut(f.address().country(), 100))
                    .postalCode(cut(f.address().zipCode(), 20))
                    .biography(cut(biography(baseDept, years), 1000))
                    .yearsOfExperience(years)
                    .active(true)
                    .build());
        }
        batch.saveAll(doctors, list);
        ctx.getDoctors().addAll(list);
        return list.size();
    }

    // biography is varchar(1000), so it stays a short paragraph rather than a long note
    private String biography(String dept, int years) {
        return "A dedicated " + dept + " physician with " + years + " years of clinical experience. "
                + "Focused on evidence-based, patient-centred care, clear communication with families, and continuous "
                + "learning. Has contributed to departmental protocols, mentors junior doctors and regularly takes part "
                + "in community health education programmes.";
    }
}
