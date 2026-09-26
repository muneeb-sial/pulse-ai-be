package app.Seeder;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** {@code app.seed.*} — every count and text size the seeder uses. */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.seed")
public class SeederProperties {

    private boolean enabled = false;
    private long randomSeed = 42;
    /** Plain-text password shared by every seeded account. */
    private String password = "Password@123";
    private boolean reindexSearch = true;

    private Counts counts = new Counts();
    private Words notes = new Words(1000, 1400);
    private Words detail = new Words(120, 250);

    @Getter
    @Setter
    public static class Counts {
        private int admins = 1;
        private int staff = 3;
        private int departments = 12;
        private int doctors = 50;
        private int patients = 500;
        private int medicines = 300;
        private int appointments = 2000;
        private int maxHistoriesPerPatient = 3;
        /** Share of COMPLETED appointments that get a prescription. */
        private double prescriptionRate = 0.7;
        private int maxItemsPerPrescription = 4;
        private int notificationsPerUser = 3;
    }

    @Getter
    @Setter
    public static class Words {
        private int minWords;
        private int maxWords;

        public Words() {
        }

        public Words(int minWords, int maxWords) {
            this.minWords = minWords;
            this.maxWords = maxWords;
        }
    }
}
