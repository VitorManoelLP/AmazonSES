package com.moneyflow.flow.service;

import com.moneyflow.flow.domain.Roles;
import com.moneyflow.flow.domain.User;
import com.moneyflow.flow.dto.EmailStructureDTO;
import com.moneyflow.flow.dto.UserRequestDTO;
import com.moneyflow.flow.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EmailSendProducer emailSendProducer;
    private final EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.loadUserByUsername(username);
    }

    @Transactional
    public void save(@Valid UserRequestDTO userRequest) {
        final User entity = userRequest.toUser(em.getReference(Roles.class, 1L));
        emailSendProducer.sendEvent(EmailStructureDTO.newInstanceBySystem(entity.getEmail()));
        userRepository.save(entity);
    }

}
