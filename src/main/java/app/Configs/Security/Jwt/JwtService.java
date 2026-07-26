package app.Configs.Security.Jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY = "S";
    private final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
    private final JWTVerifier verifier = JWT.require(algorithm).build();

    public String SignJwtToken(JwtDto userPayload) {
        JWTCreator.Builder jwtBuilder = JWT.create()
                .withClaim("userId", userPayload.getUserId())
                .withClaim("email", userPayload.getEmail())
                .withClaim("role", userPayload.getRole())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600_000 * 24));

        return jwtBuilder.sign(this.algorithm);

    }

    public JwtDto VerifyJwtToken(String jwtToken) throws JWTVerificationException {
        DecodedJWT userClaims = this.verifier.verify(jwtToken);
        return JwtDto.builder()
                .userId(userClaims.getClaim("userId").asString())
                .email(userClaims.getClaim("email").asString())
                .role(userClaims.getClaim("role").asString())
                .build();
    }

    public JwtDto DecodeJwtToken(String jwtToken) {
        DecodedJWT userClaims = JWT.decode(jwtToken);
        return JwtDto.builder()
                .userId(userClaims.getClaim("userId").asString())
                .email(userClaims.getClaim("email").asString())
                .role(userClaims.getClaim("role").asString())
                .build();
    }
}
