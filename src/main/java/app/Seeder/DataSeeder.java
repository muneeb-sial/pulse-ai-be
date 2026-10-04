package app.seeder;

import app.seeder.step.SeedContext;
import app.seeder.step.SeedStep;
import app.seeder.step.UserSeeder;
import app.api.auth.AuthRepository;
import app.api.search.SearchIndexer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * Seeds every entity on boot when {@code app.seed.enabled=true}. Runs each {@link SeedStep} in its own transaction,
 * in {@code @Order}, then rebuilds the Elasticsearch indexes (JPA saves do not index anything on their own).
 * Skipped when the marker admin account already exists.
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "app.seed", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private final SeederProperties props;
    private final List<SeedStep> steps;
    private final AuthRepository users;
    private final PasswordEncoder passwordEncoder;
    private final PlatformTransactionManager txManager;
    private final SearchIndexer searchIndexer;

    @Override
    public void run(ApplicationArguments args) {
        if (users.findOneByEmail(UserSeeder.ADMIN_EMAIL).isPresent()) {
            log.info("Seeder: {} already exists, skipping", UserSeeder.ADMIN_EMAIL);
            return;
        }

        long begin = System.currentTimeMillis();
        log.info("Seeder: starting (random-seed={})", props.getRandomSeed());
        // BCrypt is slow, so hash once and share it across every seeded account
        SeedContext ctx = new SeedContext(props, passwordEncoder.encode(props.getPassword()));
        TransactionTemplate tx = new TransactionTemplate(txManager);

        for (SeedStep step : steps) {
            long t = System.currentTimeMillis();
            Integer rows = tx.execute(status -> step.run(ctx));
            log.info("Seeder: {} -> {} rows in {} ms", step.name(), rows, System.currentTimeMillis() - t);
        }

        if (props.isReindexSearch()) {
            try {
                log.info("Seeder: search reindex -> {}", searchIndexer.reindexAll());
            } catch (RuntimeException ex) {
                log.warn("Seeder: search reindex skipped ({}). POST /search/reindex once Elasticsearch is up", ex.getMessage());
            }
        }
        log.info("Seeder: done in {} ms. Log in as {} / {}", System.currentTimeMillis() - begin, UserSeeder.ADMIN_EMAIL, props.getPassword());
    }
}
