package com.moneyflow.flow.exception.body;

import lombok.*;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class CustomBadCredentialsException {

    private String cause;
    private final String message = "E-email e/ou senha inválidos";

    public CustomBadCredentialsException withCause(String cause) {
        this.cause = cause;
        return this;
    }

}
