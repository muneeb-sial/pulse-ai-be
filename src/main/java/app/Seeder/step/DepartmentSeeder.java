package app.Seeder.step;

import app.Api.Department.DepartmentEntity;
import app.Api.Department.DepartmentRepository;
import app.Seeder.gen.SeedData;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(20)
@RequiredArgsConstructor
public class DepartmentSeeder implements SeedStep {

    private final DepartmentRepository departments;
    private final SeedBatch batch;

    @Override
    public String name() {
        return "departments";
    }

    @Override
    public int run(SeedContext ctx) {
        int count = ctx.getProps().getCounts().getDepartments();
        int bank = SeedData.DEPARTMENTS.size();
        List<DepartmentEntity> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            // past the built-in list, add numbered units so names stay distinct
            String base = SeedData.DEPARTMENTS.get(i % bank);
            String name = i < bank ? base : base + " Unit " + (i / bank + 1);
            list.add(DepartmentEntity.builder().name(name).build());
        }
        batch.saveAll(departments, list);
        ctx.getDepartments().addAll(list);
        return list.size();
    }
}
