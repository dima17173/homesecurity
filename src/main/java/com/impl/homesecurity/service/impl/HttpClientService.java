package com.impl.homesecurity.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.impl.homesecurity.domain.ClientMessage;
import com.impl.homesecurity.service.dto.SmsServiceResponseDTO;
import com.impl.homesecurity.web.rest.errors.HttpClientResponseError;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class HttpClientService {

    private final Logger log = LoggerFactory.getLogger(HttpClientService.class);
    private static final String URL1 = "https://smsc.ua/sys/get.php?get_answers=1&login=";
    private static final String URL2 = "&psw=";
    private static final String URL3 = "&fmt=3&hour=48";
    private String url;

    public HttpClientService(@Value("${app.httpClientServiceLogin}")String login,
                             @Value("${app.httpClientServicePass}")String password) {
        url = URL1 + login + URL2 + password + URL3;
    }

    //возвращаем список или ошибку
    SmsServiceResponseDTO getRequest() {
        if (log.isDebugEnabled()) {
            log.debug("Request to return client messages");
        }
        if (getResponseString() != null) {
            String responseString = getResponseString();
            SmsServiceResponseDTO result = new SmsServiceResponseDTO();
            Gson gson = new Gson();

            if (responseString.contains("error")) {
                HttpClientResponseError responseError = gson
                        .fromJson(responseString, HttpClientResponseError.class);
                result.setResponseError(responseError);
            } else {
                List<ClientMessage> clientMessageList = gson
                        .fromJson(responseString,
                                new TypeToken<List<ClientMessage>>() {
                                }.getType());
                result.setClientMessages(clientMessageList);
            }
            if (log.isInfoEnabled()) {
                log.info("Result of getting sms from users : {}", result.toString());
            }
            return result;
        }
        return null;
    }

    //получаем список логинов и смс-сообщений, отправленных пользователями
    private String getResponseString() {
        if (log.isDebugEnabled()) {
            log.debug("Request to get sms from users");
        }
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuilder responseString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseString.append(line);
            }

            return String.valueOf(responseString);
        } catch (IOException e) {
            if (log.isErrorEnabled()) {
                log.error("Failed to get sms from users with message : {}", e.getMessage());
            }
        }
        return null;
    }
}
