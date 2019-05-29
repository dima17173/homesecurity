package com.impl.homesecurity.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class LoginRequest {

    @NotBlank
    private String login;

    @NotBlank
    private String password;
}
