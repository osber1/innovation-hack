package com.learning.security.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.learning.security.jwt.JwtAuthenticationFilter;
import com.learning.security.jwt.JwtAuthorizationFilter;
import com.learning.security.jwt.JwtConfig;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtConfig config;

    private final UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().and().csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/**").hasAnyRole("USER", "ADMIN")
            .anyRequest().authenticated()
            .and().addFilter(new JwtAuthenticationFilter(authenticationManager(), config))
            .addFilter(new JwtAuthorizationFilter(authenticationManager(), userDetailsService, config))
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(WebSecurity web) {
        web
            .ignoring()
            .antMatchers("/h2-console/**", "/rfid/**");
    }
}
