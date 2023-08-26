package com.moneyflow.flow.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.moneyflow.flow.component.SecurityUtilComponent;
import com.moneyflow.flow.configuration.AuthConfig;
import com.moneyflow.flow.domain.Roles;
import com.moneyflow.flow.domain.User;
import com.moneyflow.flow.dto.EmailConfirmDTO;
import com.moneyflow.flow.dto.EmailStructureDTO;
import com.moneyflow.flow.dto.PasswordConfirmDTO;
import com.moneyflow.flow.dto.UserRequestDTO;
import com.moneyflow.flow.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EmailSendProducer emailSendProducer;
    private final EntityManager em;
    private final AmazonSimpleEmailService emailService;
    private final SecurityUtilComponent securityUtilComponent;

    @Transactional
    public void save(@Valid final UserRequestDTO userRequest) {

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new IllegalArgumentException("Este e-mail já está cadastrado");
        }

        final User entity = userRequest.toUser(em.getReference(Roles.class, 1L));
        emailSendProducer.sendEvent(EmailStructureDTO.newInstanceBySystem(entity.getEmail()));
        userRepository.save(entity);
    }


    @Transactional
    public UserDetails login(final UserRequestDTO userRequest, AuthenticationManager authenticationManager) {

        final String email = userRequest.getEmail();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, userRequest.getPassword()));

        final User user = (User) loadUserByUsername(email);

        if (!user.getVerified()) {
            final List<String> verifiedMails = emailService.listVerifiedEmailAddresses().getVerifiedEmailAddresses();
            user.setVerified(verifiedMails.stream().anyMatch(it -> it.equals(user.getEmail())));
        }

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.loadUserByUsername(username);
    }

    @Transactional
    public void changePassword(final PasswordConfirmDTO confirmDTO, AuthenticationManager authenticationManager) {
        securityUtilComponent.findLoggedUser((user) -> {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), confirmDTO.getPassword()));
            user.setPassword(AuthConfig.password().encode(confirmDTO.getNewPassword()));
            emailSendProducer.sendEvent(EmailStructureDTO.newInstanceBySystemPassword(user.getEmail()));
        });
    }

    @Transactional
    public void changeEmail(final EmailConfirmDTO confirmDTO, AuthenticationManager authenticationManager) {
        securityUtilComponent.findLoggedUser((user) -> {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            user.setEmail(confirmDTO.getEmail());
            user.setVerified(false);
            emailSendProducer.sendEvent(EmailStructureDTO.newInstanceBySystem(user.getEmail()));
        });
    }
}
