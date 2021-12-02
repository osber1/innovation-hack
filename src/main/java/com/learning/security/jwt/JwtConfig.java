package com.learning.security.jwt;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {

    private Integer tokenExpirationAfterDays;

    private String secret;

    private String tokenPrefix;

    public String getAuthorizationHeader() {
        return AUTHORIZATION;
    }
}
