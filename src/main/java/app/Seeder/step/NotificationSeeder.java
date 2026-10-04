package app.seeder.step;

import app.seeder.gen.SeedData;
import app.api.auth.UserEntity;
import app.api.notification.NotificationEntity;
import app.api.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/** Short notifications per user. notification.description is varchar(255), so no long text here. */
@Component
@Order(100)
@RequiredArgsConstructor
public class NotificationSeeder implements SeedStep {

    private final NotificationRepository notifications;
    private final SeedBatch batch;

    @Override
    public String name() {
        return "notifications";
    }

    @Override
    public int run(SeedContext ctx) {
        int perUser = ctx.getProps().getCounts().getNotificationsPerUser();
        List<NotificationEntity> list = new ArrayList<>();
        for (UserEntity user : ctx.getAllUsers()) {
            for (int i = 0; i < perUser; i++) {
                list.add(NotificationEntity.builder()
                        .user(user)
                        .title(ctx.pick(SeedData.NOTIFICATION_TITLES))
                        .description(ctx.pick(SeedData.NOTIFICATION_BODIES))
                        .read(ctx.getRandom().nextInt(100) < 40)
                        .build());
            }
        }
        batch.saveAll(notifications, list);
        return list.size();
    }
}
