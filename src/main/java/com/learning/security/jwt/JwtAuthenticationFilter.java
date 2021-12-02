package com.learning.security.jwt;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.security.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JwtConfig config;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        User user;
        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()));
        } catch (BadCredentialsException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        User user = (User) authResult.getPrincipal();
        String token = JWT.create()
            .withSubject(user.getUsername())
            .withExpiresAt(Date.valueOf(LocalDate.now().plusDays(config.getTokenExpirationAfterDays())))
            .sign(HMAC512(config.getSecret()));
        response.addHeader(config.getAuthorizationHeader(), config.getTokenPrefix() + token);
    }
}
