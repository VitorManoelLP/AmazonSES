package com.moneyflow.flow.configuration;

import com.moneyflow.flow.repository.UserRepository;
import com.moneyflow.flow.service.UserService;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@Configuration
public class AuthConfig {

    public static final String AUTH_SIGN_UP = "/api/auth/sign-up";
    public static final String AUTH_SIGN_IN = "/api/auth/sign-in";
    private final UserService userDetailsService;
    private final UserRepository userRepository;

    public AuthConfig(@Qualifier("userService") UserService userDetailsService,
                      UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {

        AuthenticationManagerBuilder sharedObject = httpSecurity
                .getSharedObject(AuthenticationManagerBuilder.class);

        sharedObject
                .userDetailsService(userDetailsService)
                .passwordEncoder(password());

        final AuthenticationManager manager = sharedObject.build();

        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher(HttpMethod.POST, AUTH_SIGN_UP),
                                AntPathRequestMatcher.antMatcher(HttpMethod.POST, AUTH_SIGN_IN)
                        )
                        .permitAll()
                        .anyRequest().authenticated())
                .authenticationManager(manager)
                .addFilter(new AuthFilter(manager, userRepository))
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }

    @Bean
    public static BCryptPasswordEncoder password() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(SecurityConstraint.ROLE_ALL_ROLES));
        configuration.setAllowedMethods(List.of(SecurityConstraint.ROLE_ALL_ROLES));
        configuration.setAllowedHeaders(List.of(SecurityConstraint.ROLE_ALL_ROLES));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/".concat(SecurityConstraint.ROLE_ALL_AUTHENTICATED_USERS), configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
