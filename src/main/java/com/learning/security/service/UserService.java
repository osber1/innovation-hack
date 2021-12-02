package com.learning.security.service;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.learning.security.model.ImageAction.APPROVE;
import static com.learning.security.model.UserInformation.Status.ACTIVE;
import static java.lang.String.format;
import static java.util.stream.Stream.ofNullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.learning.security.exception.BadRequestException;
import com.learning.security.exception.NotFoundException;
import com.learning.security.jwt.JwtConfig;
import com.learning.security.model.AuthoritiesEntity;
import com.learning.security.model.Image;
import com.learning.security.model.ImageAction;
import com.learning.security.model.User;
import com.learning.security.model.UserEntity;
import com.learning.security.model.UserInformation;
import com.learning.security.model.UserInformation.Status;
import com.learning.security.repository.AuthoritiesRepository;
import com.learning.security.repository.ImageRepository;
import com.learning.security.repository.UserInformationRepository;
import com.learning.security.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    public static final String USER_NOT_FOUND = "User not found with name: %s";

    public static final String NO_IMAGES_FOUND = "No images found.";

    public static final int POINTS_INCREMENT = 1;

    //    public static final String PATH = "/home/osvaldas/innovation/images";
    public static final String PATH = "/innovation/images";

    private final JwtConfig config;

    private final UserRepository userRepository;

    private final UserInformationRepository userInformationRepository;

    private final ImageRepository imageRepository;

    private final AuthoritiesRepository authoritiesRepository;

    public UserEntity getUserEntityByUsername(String token) {
        String username = getUsername(token);
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new NotFoundException(format(USER_NOT_FOUND, username)));
    }

    @Transactional
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .map(this::createUserFromUserEntity)
            .orElseThrow(() -> new UsernameNotFoundException(format(USER_NOT_FOUND, username)));
    }

    @Transactional
    public Image getImage() {
        File folder = new File(PATH);
        ofNullable(folder.listFiles())
            .flatMap(Stream::of)
            .filter(File::isFile)
            .filter(s -> !imageRepository.existsByPath(s.getPath()))
            .forEach(s -> {
                try {
                    Image image = new Image();
                    InputStream in = new FileInputStream(s);
                    image.setPath(s.getPath());
                    image.setImage(IOUtils.toByteArray(in));
                    imageRepository.save(image);
                } catch (Exception e) {
                    throw new NotFoundException(NO_IMAGES_FOUND);
                }
            });
        return imageRepository.findAll().stream().findFirst()
            .orElseThrow(() -> new NotFoundException(NO_IMAGES_FOUND));
    }

    @Transactional
    public void processImage(int id, ImageAction action) {
        Image image = imageRepository.findById(id).orElseThrow(() -> new NotFoundException(NO_IMAGES_FOUND));
        try {
//            String rfid = image.getPath().substring(33, 46);
            String rfid = image.getPath().substring(19, 32);
            UserInformation user = userInformationRepository.findByRfid(rfid).get();
            double currentUserPoints = user.getPoints();
            if (APPROVE == action) {
                user.setPoints(currentUserPoints + POINTS_INCREMENT);
            } else {
                if (user.getPoints() - POINTS_INCREMENT >= 0) {
                    user.setPoints(currentUserPoints - POINTS_INCREMENT);
                } else {
                    user.setPoints(0);
                }
            }
            userInformationRepository.save(user);
            Files.delete(Path.of(image.getPath()));
            imageRepository.deleteById(id);
        } catch (Exception e) {
            throw new BadRequestException("Error while processing image.");
        }
    }

    @Transactional
    public void changeUserStatus(String token, Status status) {
        UserEntity user = getUserEntityByUsername(token);
        user.getInformation().setStatus(status);
        userRepository.save(user);
    }

    public void rfidExistsAndActive(String rfid) {
        if (!userInformationRepository.existsByRfidAndStatusEquals(rfid, ACTIVE))
            throw new NotFoundException("Rfid dont exists.");
    }

    private String getUsername(String token) {
        return JWT.require(HMAC512(config.getSecret()))
            .build()
            .verify(token.replace(config.getTokenPrefix(), ""))
            .getSubject();
    }

    private User createUserFromUserEntity(UserEntity userEntity) {
        User user = new User(userEntity.getUsername(), userEntity.getPassword(), userEntity.isEnabled());
        Set<String> authorities = userEntity.getUserRoleEntities().stream()
            .flatMap(ur -> {
                Set<AuthoritiesEntity> authoritiesEntities = authoritiesRepository.findByRole(ur.getRole());
                return authoritiesEntities.stream().map(AuthoritiesEntity::getAuthority);
            })
            .collect(Collectors.toSet());
        userEntity.getUserRoleEntities().forEach(ur -> authorities.add(ur.getRole()));
        user.setAuthorities(authorities.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet()));
        return user;
    }
}