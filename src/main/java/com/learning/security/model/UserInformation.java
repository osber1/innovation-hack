package com.learning.security.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class UserInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String lastName;

    private String email;

    private String rfid;

    private double points;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        ACTIVE,
        INACTIVE
    }

    public UserInformation(String name, String lastName, String email, String rfid, double points, Status status) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.rfid = rfid;
        this.points = points;
        this.status = status;
    }
}