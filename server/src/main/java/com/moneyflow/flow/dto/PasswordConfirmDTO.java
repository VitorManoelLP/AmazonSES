package com.moneyflow.flow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordConfirmDTO implements Serializable {

    private String password;
    private String newPassword;

}
