package com.moneyflow.flow.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class EmailConfirmDTO {

    private String password;
    private String email;

}
