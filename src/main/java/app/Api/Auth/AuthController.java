package app.Api.Auth;

import app.Api.Auth.Dtos.*;
import app.Api.Auth.MagicLink.Dtos.AuthMagicLinkRequestDto;
import app.Api.Auth.MagicLink.Dtos.AuthMagicLinkVerifyDto;
import app.Api.Auth.MagicLink.MagicLinkService;
import app.Configs.Security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
        return user;
    }

    @GetMapping("/all")
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
