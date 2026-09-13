package app.Api.Auth.MagicLink;

import app.Api.Auth.AuthRepository;
import app.Api.Auth.MagicLink.Dtos.AuthMagicLinkRequestDto;
import app.Api.Auth.MagicLink.Dtos.AuthMagicLinkVerifyDto;
import app.Api.Auth.UserEntity;
import app.Configs.Security.Jwt.JwtDto;
import app.Configs.Security.Jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MagicLinkService {

    private static final long MAGIC_LINK_EXPIRY_MINUTES = 15;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final AuthRepository userDb;
    private final MagicLinkTokenRepository magicLinkTokenDb;
    private final JwtService jwt;

    public Object requestMagicLink(AuthMagicLinkRequestDto userDto) {
        var user = this.userDb.findOneByEmail(userDto.getEmail());
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
        this.sendUserMagicLinkToken(user.get());
        return Map.of("message", "magic link sent");
    }

    public Object verifyMagicLink(AuthMagicLinkVerifyDto userDto) {
        var magicLinkToken = this.magicLinkTokenDb.findOneByToken(userDto.getToken());
        if (magicLinkToken.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid magic link");
        }
        var tokenEntity = magicLinkToken.get();
        if (Boolean.TRUE.equals(tokenEntity.getUsed()) || tokenEntity.getExpiresAt().isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Magic link expired or already used");
        }
        tokenEntity.setUsed(true);
        this.magicLinkTokenDb.save(tokenEntity);

        var jwtPayload = this.getJwtPayload(tokenEntity.getUser());
        return Map.of("token", this.jwt.SignJwtToken(jwtPayload));
    }

    private JwtDto getJwtPayload(UserEntity userEntity) {
        return JwtDto.builder()
                .userId(userEntity.getId().toString())
                .email(userEntity.getEmail())
                .role(userEntity.getRole())
                .build();
    }

    private void sendUserMagicLinkToken(UserEntity user) {
        byte[] randomBytes = new byte[32];
        RANDOM.nextBytes(randomBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        var magicLinkToken = MagicLinkTokenEntity.builder()
                .user(user)
                .token(token)
                .expiresAt(Instant.now().plus(MAGIC_LINK_EXPIRY_MINUTES, ChronoUnit.MINUTES))
                .used(false)
                .build();
        this.magicLinkTokenDb.save(magicLinkToken);

        System.out.println("[email]" + user.getEmail() + " magic link token: " + token);
    }
}
