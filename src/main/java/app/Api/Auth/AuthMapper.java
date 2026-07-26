package app.Api.Auth;

import app.Api.Auth.Dtos.AuthRegisterDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    UserEntity fromRegistrationToEntity(AuthRegisterDto authRegisterDto);
}
