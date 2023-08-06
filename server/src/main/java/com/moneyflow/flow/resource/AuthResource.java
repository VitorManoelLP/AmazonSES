package com.moneyflow.flow.resource;

import com.moneyflow.flow.configuration.JwtTokenValidator;
import com.moneyflow.flow.dto.UserRequestDTO;
import com.moneyflow.flow.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthResource {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody @Valid final UserRequestDTO userRequest) {
        userService.save(userRequest);
        return ResponseEntity
                .created(URI.create(ServletUriComponentsBuilder.fromCurrentRequest().toUriString()))
                .build();
    }

    @GetMapping(value = "/sign-in", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> signIn(@RequestBody @Valid final UserRequestDTO userRequest) {

        final UserDetails userDetails = userService.login(userRequest, authenticationManager);

        return ResponseEntity.ok(JwtTokenValidator.generateToken(userDetails));
    }

}
