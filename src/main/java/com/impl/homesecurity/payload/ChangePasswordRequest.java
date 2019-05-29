package com.impl.homesecurity.payload;

import com.impl.homesecurity.domain.enumeration.ExceptionCodes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class ChangePasswordRequest {

    private String login;
    private String oldPassword;

    @NotBlank
    @Size(min = 4, max = 25, message = ExceptionCodes.Constants.INVALID_LENGTH_VALUE)
    private String newPassword;
}
