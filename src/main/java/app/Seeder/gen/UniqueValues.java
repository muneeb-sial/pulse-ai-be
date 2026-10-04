package app.seeder.gen;

/** Deterministic, collision-free phone / id-card / licence strings derived from a running index. */
public final class UniqueValues {

    // odd and not divisible by 5, so i -> i * MULT is a bijection modulo any power of ten
    private static final long MULT = 2654435761L;

    private UniqueValues() {
    }

    private static long scramble(long i, long mod) {
        return (i * MULT) % mod;
    }

    public static String phone(long i) {
        return "+92" + (3_000_000_000L + scramble(i, 1_000_000_000L));
    }

    public static String idCard(long i) {
        String s = String.format("%013d", scramble(i, 10_000_000_000_000L));
        return s.substring(0, 5) + "-" + s.substring(5, 12) + "-" + s.substring(12);
    }

    public static String license(long i) {
        return String.format("LIC-%08d", scramble(i, 100_000_000L));
    }

    public static String cut(String s, int max) {
        return s == null || s.length() <= max ? s : s.substring(0, max);
    }
}
