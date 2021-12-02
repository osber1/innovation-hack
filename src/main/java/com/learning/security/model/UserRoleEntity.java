package com.learning.security.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class UserRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userRoleId;

    private String role;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER,
        cascade = CascadeType.ALL)
    @JoinColumn(name = "Username")
    private UserEntity userEntity;

    public UserRoleEntity(String role, UserEntity userEntity) {
        this.role = role;
        this.userEntity = userEntity;
    }
}
