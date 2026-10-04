package app.seeder.step;

import app.seeder.gen.SeedData;
import app.api.medicine.MedicineEntity;
import app.api.medicine.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Order(30)
@RequiredArgsConstructor
public class MedicineSeeder implements SeedStep {

    private final MedicineRepository medicines;
    private final SeedBatch batch;

    @Override
    public String name() {
        return "medicines";
    }

    @Override
    public int run(SeedContext ctx) {
        int count = ctx.getProps().getCounts().getMedicines();
        Set<String> used = new HashSet<>();
        medicines.findAll().forEach(m -> used.add(m.getName()));

        List<MedicineEntity> list = new ArrayList<>();
        int attempts = 0;
        while (list.size() < count) {
            var t = ctx.pick(SeedData.MEDICINES);
            String strength = ctx.pick(t.strengths());
            String form = ctx.pick(t.forms());
            String maker = ctx.pick(SeedData.MANUFACTURERS);

            String name = t.generic() + " " + strength + " " + form;
            // once plain names start colliding, qualify with the manufacturer, then fall back to a number
            if (attempts++ > count * 2 || used.contains(name)) {
                name = name + " (" + maker + ")";
            }
            if (!used.add(name)) {
                name = name + " #" + (list.size() + 1);
                used.add(name);
            }
            list.add(MedicineEntity.builder()
                    .name(name)
                    .genericName(t.generic())
                    .manufacturer(maker)
                    .strength(strength)
                    .form(form)
                    .build());
        }
        batch.saveAll(medicines, list);
        ctx.getMedicines().addAll(list);
        return list.size();
    }
}
