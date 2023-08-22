package com.moneyflow.flow.component;

import com.moneyflow.flow.domain.User;
import com.moneyflow.flow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class SecurityUtilComponent {

    private final UserRepository userRepository;

    @Transactional
    public void findLoggedUser(final Consumer<User> userConsumer) {
        final String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userRepository.findByEmail(principal).ifPresent(userConsumer);
    }

}
