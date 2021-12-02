package com.learning.security.model;

import com.learning.security.model.UserInformation.Status;

import lombok.Data;

@Data
public class UserInformationDTO {

    private int id;

    private String name;

    private String lastName;

    private String email;

    private String rfid;

    private double points;

    private Status status;
}