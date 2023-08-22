package com.moneyflow.flow.repository;

import com.moneyflow.flow.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    default User loadUserByUsername(String name) {
        return findByEmail(name).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

}
