package app.Configs.Security;

import java.util.List;

public class PublicRoutesConfig {
    public static List<String> routes =List.of(
            "/auth/**",
            "/home",
            "/swagger-ui/**",
            "/**"
    );
}
