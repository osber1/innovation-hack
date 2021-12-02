package com.learning.security.model;

import java.util.Set;

import lombok.Data;

@Data
public class UserDTO {

    private String username;

    private Set<UserRoleEntityDTO> role;

    private UserInformationDTO information;
}
