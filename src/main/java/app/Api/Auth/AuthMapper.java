package app.api.auth;

import org.mapstruct.Mapper;

import app.api.auth.dtos.AuthRegisterDto;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    UserEntity fromRegistrationToEntity(AuthRegisterDto authRegisterDto);
}
