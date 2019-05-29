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
public class SignUpRequest {

    @NotBlank
    @Size(min = 4, max = 25, message = ExceptionCodes.Constants.INVALID_LENGTH_VALUE)
    private String name;

    @NotBlank
    @Size(min = 4, max = 25, message = ExceptionCodes.Constants.INVALID_LENGTH_VALUE)
    private String login;
    private String deviceNumber;
}
