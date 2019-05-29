package com.impl.homesecurity.service.dto;

import com.impl.homesecurity.domain.ClientMessage;
import com.impl.homesecurity.web.rest.errors.HttpClientResponseError;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SmsServiceResponseDTO {

    private HttpClientResponseError responseError;
    private List<ClientMessage> clientMessages;
}
