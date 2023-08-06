package com.moneyflow.flow.service;

import com.moneyflow.flow.domain.Roles;
import com.moneyflow.flow.domain.User;
import com.moneyflow.flow.dto.EmailStructureDTO;
import com.moneyflow.flow.dto.UserRequestDTO;
import com.moneyflow.flow.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EmailSendProducer emailSendProducer;
    private final EntityManager em;
    private final EmailService emailService;

    @Transactional
    public void save(@Valid final UserRequestDTO userRequest) {
        final User entity = userRequest.toUser(em.getReference(Roles.class, 1L));
        emailSendProducer.sendEvent(EmailStructureDTO.newInstanceBySystem(entity.getEmail()));
        userRepository.save(entity);
    }


    @Transactional
    public UserDetails login(final UserRequestDTO userRequest, final AuthenticationManager authenticationManager) {

        final String name = userRequest.getName();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(name, userRequest.getPassword()));

        final User user = (User) loadUserByUsername(name);

        if (!user.getVerified()) {
            final List<String> verifiedMails = emailService.getAllVerifiedMails();
            user.setVerified(verifiedMails.stream().anyMatch(it -> it.equals(user.getEmail())));
        }

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.loadUserByUsername(username);
    }

}
