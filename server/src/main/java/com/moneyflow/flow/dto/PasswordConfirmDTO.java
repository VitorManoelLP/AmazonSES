package com.moneyflow.flow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordConfirmDTO {

    private final String password;
    private final String newPassword;

}
