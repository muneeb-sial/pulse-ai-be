package app.Api.Auth;

import app.Api.Auth.Dtos.*;
import app.Configs.Security.Jwt.JwtDto;
import app.Configs.Security.Jwt.JwtService;
import app.Configs.Security.OtpGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository userDb;
    private final JwtService jwt;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;


    public Object registerUser(AuthRegisterDto userRegisterDto) {
        var user = this.findUserByEmail(userRegisterDto.getEmail());
        if (user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exits");
        }
        UserEntity userDto = this.authMapper.fromRegistrationToEntity(userRegisterDto);
        var hashedPassword = this.passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(hashedPassword);
        var userEntity = this.userDb.save(userDto);
        var jwtPayload = this.getJwtPayload(userEntity);
        return Map.of("token", this.jwt.SignJwtToken(jwtPayload));

    }

    public Object loginUser(AuthLoginDto userDto) {
        var user = this.findUserByEmail(userDto.getEmail());
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
        if (!passwordEncoder.matches(userDto.getPassword(), user.get().getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid credentials"
            );
        }
        var jwtPayload = this.getJwtPayload(user.get());
        return Map.of("token", this.jwt.SignJwtToken(jwtPayload));
    }

    public List<UserEntity> getAllUsers() {
        return this.userDb.findAll(
                (root, query, cb) ->
                        cb.and(
                                cb.equal(root.get("email"), "string"),
                                cb.equal(root.get("password"), "string")
                        )
        );
    }

    public Object forgetPassword(AuthForgetPasswordDto userDto) {
        var user = this.findUserByEmail(userDto.getEmail());
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
        this.sendUserOtpToken(user.get());
        return Map.of("message","otp sent");
    }

    public Object verifyEmail(AuthVerifyEmailDto userDto) {
        var user = this.findUserByEmail(userDto.getEmail());
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
        this.verifyUserOtpToken(user.get(),userDto.getOtpToken());
        var jwtPayload = this.getJwtPayload(user.get());
        return Map.of("token", this.jwt.SignJwtToken(jwtPayload));
    }

    public Object resetPassword(Long userId, AuthResetPasswordDto userDto){
        var user = this.userDb.findById(userId);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
        if (!passwordEncoder.matches(userDto.getOldPassword(), user.get().getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid credentials"
            );
        }
        var hashedPassword = this.passwordEncoder.encode(userDto.getNewPassword());
        user.get().setPassword(hashedPassword);
        this.userDb.save(user.get());
        return Map.of("message","password reset");
    }
//
//    public googleAuthCodeAuthentication(){}
//
//    public getAuthenticatedUser(){}

    private Optional<UserEntity> findUserByEmail(String email){
        return this
                .userDb
                .findOneByEmail(email);
    }

    private JwtDto getJwtPayload(UserEntity userEntity){
        return JwtDto.builder()
                .userId(userEntity.getId().toString())
                .email(userEntity.getEmail())
                .role(userEntity.getRole())
                .build();
    }

    private void sendUserOtpToken(UserEntity user){
        String otp = OtpGenerator.generateOtp();
        user.setOtpToken(otp);
        this.userDb.save(user);
        System.out.println("[email]"+user.getEmail()+" otp: "+ otp);
    }

    private void verifyUserOtpToken(UserEntity user,String otpToken){
        if(user.getOtpToken().equals(otpToken)){
            user.setOtpToken(null);
            user.setIsVerified(true);
            this.userDb.save(user);
        }
        else{
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "In Valid Otp"
            );
        }
    }
}
