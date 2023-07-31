package com.example.ldap.config;

import com.example.ldap.provider.LdapAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class LdapSecurityConfig {

    private final LdapAuthenticationProvider ldapAuthenticationProvider;
    public static final String[] IGNORED_URLS = new String[]{
            "/error", "/favicon.ico/", "/csrf",
            "/actuator/**", "/i18n/**", "/swagger-ui/**", "/content/**",
            "/v3/api-docs", "/v3/api-docs/swagger-config", "/webjars/**", "/swagger-resources/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .anyRequest().permitAll());
        return http.build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(IGNORED_URLS)
                .requestMatchers("/login")
                .requestMatchers("/v1/*/auth/register")
                .requestMatchers("/v1/*/auth/refresh");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .authenticationProvider(ldapAuthenticationProvider)
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=groups")
                .contextSource()
                .url("ldap://localhost:8389/dc=springframework,dc=org")
                .and()
                .passwordCompare()
                .passwordEncoder(new BCryptPasswordEncoder())
                .passwordAttribute("userPassword");

        return authenticationManagerBuilder.build();
    }


}

