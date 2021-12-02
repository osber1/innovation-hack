package com.learning.security.util;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.learning.security.model.AuthoritiesEntity;
import com.learning.security.model.UserEntity;
import com.learning.security.model.UserInformation;
import com.learning.security.model.UserInformation.Status;
import com.learning.security.model.UserRoleEntity;
import com.learning.security.repository.AuthoritiesRepository;
import com.learning.security.repository.UserInformationRepository;
import com.learning.security.repository.UserRepository;
import com.learning.security.repository.UserRoleRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class InitClass implements CommandLineRunner {

    private final AuthoritiesRepository authoritiesRepository;

    private final UserRoleRepository userRoleRepository;

    private final UserInformationRepository userInformationRepository;

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        AuthoritiesEntity userAuthorities = new AuthoritiesEntity("GET", "ROLE_USER");
        AuthoritiesEntity adminAuthorities1 = new AuthoritiesEntity("MANAGE", "ROLE_ADMIN");
        authoritiesRepository.save(userAuthorities);
        authoritiesRepository.save(adminAuthorities1);

        UserEntity user = new UserEntity("test.user", "$2a$12$svkF3pPM2P37WfEPqZ7bk.cwgAEeigzDwoezu0d92X0Dr./tYcGgi", true);
        UserEntity admin = new UserEntity("admin.user", "$2a$12$9MWQkPSKerrYSO5KSobT2.sCpZG5FB8IcVZ66wFuvZKg9VQR834ne", true);

        UserInformation testInformation = new UserInformation("Test", "User", "test.user@gmail.com", "1094313389154", 0, Status.INACTIVE);
        UserInformation adminInformation = new UserInformation("Admin", "User", "admin.user@gmail.com", null, 0, Status.INACTIVE);
        userInformationRepository.save(testInformation);
        userInformationRepository.save(adminInformation);

        UserRoleEntity userRoleEntity = new UserRoleEntity("ROLE_USER", user);
        Set<UserRoleEntity> userRolesSet = new HashSet<>();
        userRolesSet.add(userRoleEntity);
        user.setUserRoleEntities(userRolesSet);
        user.setInformation(testInformation);
        userRoleRepository.save(userRoleEntity);

        UserRoleEntity adminRoleEntity = new UserRoleEntity("ROLE_ADMIN", admin);
        Set<UserRoleEntity> adminRolesSet = new HashSet<>();
        adminRolesSet.add(adminRoleEntity);
        admin.setUserRoleEntities(adminRolesSet);
        admin.setInformation(adminInformation);
        userRoleRepository.save(adminRoleEntity);

        userRepository.save(admin);
        userRepository.save(user);
    }
}
