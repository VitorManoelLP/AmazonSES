package com.moneyflow.flow.dto;

import com.moneyflow.flow.configuration.AuthConfig;
import com.moneyflow.flow.domain.Roles;
import com.moneyflow.flow.domain.User;
import com.moneyflow.flow.domain.UserRoles;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.util.Assert;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class UserRequestDTO {

    private String name;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public User toUser(final Roles roles) {

        final User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(AuthConfig.password().encode(password));

        final UserRoles userRoles = new UserRoles();
        userRoles.setRoles(roles);
        userRoles.setUser(user);

        user.setAuthorities(Set.of(userRoles));

        return user;
    }

}
