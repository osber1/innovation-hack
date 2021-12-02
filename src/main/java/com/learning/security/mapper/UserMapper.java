package com.learning.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.learning.security.model.UserDTO;
import com.learning.security.model.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "userRoleEntities")
    UserDTO userToDTO(UserEntity user);
}
