package app.seeder.step;

import app.seeder.gen.SeedData;
import app.shared.enums.MedicalHistoryType;
import app.api.medicalhistory.MedicalHistoryEntity;
import app.api.medicalhistory.MedicalHistoryRepository;
import app.api.patient.PatientEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/** 1..N histories per patient, so every medical record later has a valid history of the same patient to point at. */
@Component
@Order(60)
@RequiredArgsConstructor
public class MedicalHistorySeeder implements SeedStep {

    private final MedicalHistoryRepository histories;
    private final SeedBatch batch;

    @Override
    public String name() {
        return "medical histories";
    }

    @Override
    public int run(SeedContext ctx) {
        int max = Math.max(1, ctx.getProps().getCounts().getMaxHistoriesPerPatient());
        List<MedicalHistoryEntity> list = new ArrayList<>();
        MedicalHistoryType[] types = MedicalHistoryType.values();

        for (PatientEntity patient : ctx.getPatients()) {
            int n = 1 + ctx.getRandom().nextInt(max);
            for (int i = 0; i < n; i++) {
                MedicalHistoryType type = types[ctx.getRandom().nextInt(types.length)];
                list.add(MedicalHistoryEntity.builder()
                        .patient(patient)
                        .type(type)
                        .title(ctx.pick(SeedData.HISTORY_TITLES.get(type)))
                        .description(ctx.detail())
                        .active(true)
                        .build());
            }
        }
        batch.saveAll(histories, list);
        for (MedicalHistoryEntity h : list) {
            ctx.getHistoriesByPatientId().computeIfAbsent(h.getPatient().getId(), k -> new ArrayList<>()).add(h);
        }
        return list.size();
    }
}
