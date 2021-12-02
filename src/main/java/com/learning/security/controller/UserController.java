package com.learning.security.controller;

import static com.learning.security.model.UserInformation.Status.ACTIVE;
import static com.learning.security.model.UserInformation.Status.INACTIVE;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.security.mapper.ImageMapper;
import com.learning.security.mapper.UserMapper;
import com.learning.security.model.ImageAction;
import com.learning.security.model.ImageDTO;
import com.learning.security.model.UserDTO;
import com.learning.security.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping
@AllArgsConstructor
public class UserController {

    private final UserService service;

    private final UserMapper userMapper;

    private final ImageMapper imageMapper;

    @PostMapping("api/activate")
    public void activateUser(@RequestHeader("Authorization") String token) {
        service.changeUserStatus(token, ACTIVE);
    }

    @PostMapping("api/deactivate")
    public void deactivateUser(@RequestHeader("Authorization") String token) {
        service.changeUserStatus(token, INACTIVE);
    }

    @GetMapping("api/image")
    public ImageDTO getImage() {
        return imageMapper.imageToDTO(service.getImage());
    }

    @GetMapping("rfid/{rfid}")
    public void rfidExistsAndActive(@PathVariable String rfid) {
        service.rfidExistsAndActive(rfid);
    }

    @PostMapping("api/image/{id}/{action}")
    public void processImage(@PathVariable int id, @PathVariable ImageAction action) {
        service.processImage(id, action);
    }

    @GetMapping("api")
    public UserDTO getUser(@RequestHeader("Authorization") String token) {
        return userMapper.userToDTO(service.getUserEntityByUsername(token));
    }
}
