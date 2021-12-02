package com.learning.security.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class UserEntity {

    @Id
    private String username;

    private String password;

    private boolean enabled;

    @OneToMany(mappedBy = "userEntity",
        fetch = FetchType.EAGER,
        cascade = CascadeType.ALL)
    private Set<UserRoleEntity> userRoleEntities;

    @OneToOne
    private UserInformation information;

    public UserEntity(String username, String password, boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }
}
