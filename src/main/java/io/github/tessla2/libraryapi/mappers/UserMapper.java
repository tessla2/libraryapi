package io.github.tessla2.libraryapi.mappers;

import io.github.tessla2.libraryapi.controller.dto.UserDTO;
import io.github.tessla2.libraryapi.controller.dto.UserRegistrationDTO;
import io.github.tessla2.libraryapi.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDTO dto);

    UserDTO toDTO(User user);

    User toEntity(UserRegistrationDTO dto);
}