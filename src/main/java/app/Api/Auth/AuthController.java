package app.api.auth;

import java.util.Map;

import org.springframework.web.server.ResponseStatusException;

import org.springframework.http.HttpStatus;

import app.configs.security.UserPrincipal;
import app.api.auth.dtos.*;
import app.api.auth.magiclink.MagicLinkService;
import app.api.auth.magiclink.dtos.AuthMagicLinkRequestDto;
import app.api.auth.magiclink.dtos.AuthMagicLinkVerifyDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MagicLinkService magicLinkService;

    @GetMapping("/me")
    Object getAuthenticatedUser(@AuthenticationPrincipal UserPrincipal user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        // Returned explicitly rather than serializing UserPrincipal: Jackson found no
        // properties on it and the endpoint answered 200 with an empty body.
        return Map.of(
                "userId", user.getUserId(),
                "email", user.getEmail(),
                "role", user.getRole()
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    Object getAllUser() {
        return this.authService.getAllUsers();
    }

    @PostMapping("/register")
    Object registerUser(@Valid @RequestBody AuthRegisterDto user) {
        return this.authService.registerUser(user);
    }

    @PostMapping("/login")
    Object loginUser(@Valid @RequestBody AuthLoginDto user) {
        return this.authService.loginUser(user);
    }

    @PostMapping("/forget-password")
    Object forgetPassword(@RequestBody AuthForgetPasswordDto user) {
        return this.authService.forgetPassword(user);
    }

    @PostMapping("/verify-email")
    Object verifyEmail(@RequestBody AuthVerifyEmailDto user) {
        return this.authService.verifyEmail(user);
    }

    @PostMapping("/reset-password")
    Object resetPassword(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody AuthResetPasswordDto userDto
    ) {
        return this.authService.resetPassword(userPrincipal.getUserId(),userDto);
    }

    @PostMapping("/login/google")
    Object googleAuthCodeAuthentication(@RequestBody AuthLoginDto user) {
        return "User Logged in";
    }

    @PostMapping("/magic-link")
    Object requestMagicLink(@Valid @RequestBody AuthMagicLinkRequestDto user) {
        return this.magicLinkService.requestMagicLink(user);
    }

    @PostMapping("/magic-link/verify")
    Object verifyMagicLink(@Valid @RequestBody AuthMagicLinkVerifyDto user) {
        return this.magicLinkService.verifyMagicLink(user);
    }


}
