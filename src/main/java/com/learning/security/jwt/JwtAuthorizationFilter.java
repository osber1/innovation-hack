package com.learning.security.jwt;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.learning.security.security.UserDetailsService;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserDetailsService userDetailsService;

    private final JwtConfig config;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  UserDetailsService userDetailsService,
                                  JwtConfig config) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.config = config;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String authHeader = request.getHeader(config.getAuthorizationHeader());
        if (authHeader == null || authHeader.isEmpty() || !authHeader.startsWith(config.getTokenPrefix())) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(authHeader);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String authHeader) {
        if (authHeader != null) {
            String usernameFromJwt = JWT.require(HMAC512(config.getSecret()))
                .build()
                .verify(authHeader.replace(config.getTokenPrefix(), ""))
                .getSubject();
            if (usernameFromJwt != null) {
                return new UsernamePasswordAuthenticationToken(usernameFromJwt,
                    null,
                    userDetailsService.loadUserByUsername(usernameFromJwt).getAuthorities());
            }
        }
        return null;
    }
}
