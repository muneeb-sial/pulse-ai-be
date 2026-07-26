package app.Configs.Security.Jwt;


import app.Configs.Security.PublicRoutesConfig;
import app.Configs.Security.UserPrincipal;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

public class JwtFilter extends OncePerRequestFilter {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final ObjectMapper jsonWriter = new ObjectMapper();

    @Autowired
    private JwtService jwtService;


    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        try {
            var path = req.getRequestURI();

            // Skip JWT check for public routes
            boolean isPublic = PublicRoutesConfig.routes.stream()
                    .anyMatch(route -> pathMatcher.match(route, path));

            if (isPublic) {
                filterChain.doFilter(req, res); //let go if route is public
                return;
            }

            var bearerToken = req.getHeader("Authorization");

            if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.setContentType("application/json");
                res.getWriter().write(jsonWriter.writeValueAsString(Map.of(
                        "message", "Forbidden resource",
                        "status", HttpServletResponse.SC_UNAUTHORIZED
                )));
                return;
            }
            var jwtToken = bearerToken.substring(7);
            var userPayload = jwtService.VerifyJwtToken(jwtToken);
            var userPrincipal = new UserPrincipal(userPayload.getUserId(), userPayload.getEmail(), userPayload.getRole());
            var authUser = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authUser);
            filterChain.doFilter(req, res);
        } catch (JWTVerificationException ex) {

            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().write(jsonWriter.writeValueAsString(Map.of(
                    "message", "Invalid or expired JWT: " + ex.getMessage(),
                    "status", HttpServletResponse.SC_UNAUTHORIZED
            )));
        }
    }
}
