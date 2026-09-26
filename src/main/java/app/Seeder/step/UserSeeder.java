package app.Seeder.step;

import app.Api.Auth.AuthRepository;
import app.Api.Auth.UserEntity;
import app.Shared.Enum.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/** Accounts for admins, staff, doctors and patients. The doctor/patient users are handed to later steps. */
@Component
@Order(10)
@RequiredArgsConstructor
public class UserSeeder implements SeedStep {

    public static final String ADMIN_EMAIL = "admin@seed.local";

    private final AuthRepository users;
    private final SeedBatch batch;

    @Override
    public String name() {
        return "users";
    }

    @Override
    public int run(SeedContext ctx) {
        var counts = ctx.getProps().getCounts();
        List<UserEntity> all = new ArrayList<>();

        for (int i = 1; i <= Math.max(1, counts.getAdmins()); i++) {
            all.add(user(ctx, i == 1 ? ADMIN_EMAIL : "admin" + i + "@seed.local", Role.ADMIN));
        }
        for (int i = 1; i <= counts.getStaff(); i++) {
            all.add(user(ctx, "staff" + i + "@seed.local", Role.STAFF));
        }
        for (int i = 1; i <= counts.getDoctors(); i++) {
            UserEntity u = user(ctx, "doctor" + i + "@seed.local", Role.DOCTOR);
            ctx.getDoctorUsers().add(u);
            all.add(u);
        }
        for (int i = 1; i <= counts.getPatients(); i++) {
            UserEntity u = user(ctx, "patient" + i + "@seed.local", Role.PATIENT);
            ctx.getPatientUsers().add(u);
            all.add(u);
        }

        batch.saveAll(users, all);
        ctx.getAllUsers().addAll(all);
        return all.size();
    }

    private UserEntity user(SeedContext ctx, String email, Role role) {
        return UserEntity.builder()
                .email(email)
                .password(ctx.getPasswordHash())
                .isVerified(true)
                .provider("LOCAL")
                .role(role.name())
                .build();
    }
}
