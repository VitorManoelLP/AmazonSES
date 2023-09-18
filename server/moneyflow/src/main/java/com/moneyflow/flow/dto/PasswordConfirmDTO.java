package com.moneyflow.flow.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PasswordConfirmDTO implements Serializable {

    private String password;
    private String newPassword;

}
