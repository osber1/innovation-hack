package com.learning.security.model;

import lombok.Data;

@Data
public class ImageDTO {

    private int id;

    private byte[] image;

    private String path;
}
