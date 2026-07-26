package app.Api.Auth;

import app.Api.Auth.Dtos.AuthLoginDto;
import app.Api.Auth.Dtos.AuthRegisterDto;
import app.Configs.Security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

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
    Object forgetPassword(@RequestBody AuthLoginDto user) {
        return "User Logged in";
    }

    @PostMapping("/reset-password")
    Object resetPassword(@RequestBody AuthLoginDto user) {
        return "User Logged in";
    }

    @PostMapping("/login/google")
    Object googleAuthCodeAuthentication(@RequestBody AuthLoginDto user) {
        return "User Logged in";
    }


}
