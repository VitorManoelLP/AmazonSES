package com.moneyflow.flow.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;


@Entity
@Table
@Getter
public class Roles implements GrantedAuthority {

    private static final String DEFAULT_ROLE = "ROLE_USER";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_seq")
    @SequenceGenerator(name = "roles_seq", sequenceName = "roles_seq", allocationSize = 1)
    private Long id;

    @Column(name = "authority")
    private String authority = DEFAULT_ROLE;

    @Override
    public String getAuthority() {
        return authority;
    }

}
