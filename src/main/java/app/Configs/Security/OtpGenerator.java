package app.Configs.Security;

import java.security.SecureRandom;

public class OtpGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateOtp() {
        return String.format("%06d", RANDOM.nextInt(1_000_000));
    }
}