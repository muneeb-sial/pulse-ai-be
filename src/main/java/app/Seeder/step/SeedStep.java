package app.seeder.step;

/** One entity group of the seeder. Implementations are ordered with {@code @Order} and run in a transaction each. */
public interface SeedStep {

    String name();

    /** @return number of rows created */
    int run(SeedContext ctx);
}
