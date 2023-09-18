package com.moneyflow.flow.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
public class UserRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_roles_seq")
    @SequenceGenerator(name = "user_roles_seq", sequenceName = "user_roles_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Roles roles;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
