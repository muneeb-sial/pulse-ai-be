package app.configs.security;

import java.util.List;

public class PublicRoutesConfig {
    /**
     * Endpoints reachable without a bearer token.
     *
     * Listed individually rather than as "/auth/**": that wildcard also exempted the
     * endpoints that read the caller via @AuthenticationPrincipal, so JwtFilter skipped
     * them, never populated the SecurityContext, and the principal arrived null -
     * /auth/me answered 200 with an empty body and /auth/reset-password would NPE.
     */
    public static List<String> routes = List.of(
            "/auth/login",
            "/auth/login/google",
            "/auth/register",
            "/auth/forget-password",
            "/auth/verify-email",
            "/auth/magic-link",
            "/auth/magic-link/verify",
            "/health",
            // Scraped by Prometheus, which sends no bearer token. Listed as two
            // exact paths rather than "/actuator/**" so a future endpoint cannot
            // become public by accident.
            "/actuator/prometheus",
            "/actuator/health",
            "/swagger-ui.html",
            "/search/reindex",
            "/search",
            "/search/suggest",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );
}
