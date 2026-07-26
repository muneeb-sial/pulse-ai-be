package app.Api.Auth;

import app.Api.Auth.Dtos.AuthLoginDto;
import app.Api.Auth.Dtos.AuthRegisterDto;
import app.Configs.Security.Jwt.JwtDto;
import app.Configs.Security.Jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private AuthRepository userDb;

    @Autowired
    private JwtService jwt;

    //
    public Object registerUser(AuthRegisterDto userRegisterDto) {
        if (this.userDb.findOneByEmail(userRegisterDto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exits");
        }
        var userInstance = UserEntity.builder()
                .name(userRegisterDto.getName())
                .email(userRegisterDto.getEmail())
                .password(userRegisterDto.getPassword())
                .provider("LOCAL")
                .role("USER")
                .build();

        var userEntity = this.userDb.save(userInstance);

        var jwtPayload = JwtDto.builder()
                .userId(userEntity.getId().toString())
                .email(userInstance.getEmail())
                .role(userInstance.getRole())
                .build();

        return Map.of("token", this.jwt.SignJwtToken(jwtPayload));

    }

    public Object loginUser(AuthLoginDto user) {
        var userData = this.userDb.findOneByEmailAndPassword(user.getEmail(), user.getPassword());
        if (userData.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
        var jwtPayload = JwtDto.builder()
                .userId(userData.get().getId().toString())
                .email(userData.get().getEmail())
                .role(userData.get().getRole())
                .build();
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

//    public Object forgetPassword(){}
//
//    public Object resetPassword(){}
//
//    public googleAuthCodeAuthentication(){}
//
//    public getAuthenticatedUser(){}
}
