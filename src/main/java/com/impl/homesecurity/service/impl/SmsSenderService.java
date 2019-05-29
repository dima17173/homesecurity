package com.impl.homesecurity.service.impl;

import com.impl.homesecurity.dao.UserDao;
import com.impl.homesecurity.domain.SmsSender;
import com.impl.homesecurity.domain.enumeration.ExceptionCodes;
import com.impl.homesecurity.web.rest.errors.BadRequestException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;

/**
 * Сервис отправки SMS пользователям
 */
@Service
public class SmsSenderService {

    private final Logger log = LoggerFactory.getLogger(SmsSenderService.class);
    private UserDao userDao;
    private static final String URL1 = "https://smsc.ua/sys/send.php?login=";
    private static final String URL2 = "&psw=";
    private static final String URL3 = "&phones=";
    private static final String URL4 = "&mes=";
    private static final String URL5 = "&translit=1";
    private static final String URL6 = "&charset=utf-8";

    @Value("${app.httpClientServiceLogin}")
    private String login;
    @Value("${app.httpClientServicePass}")
    private String password;

    public void sentResponse(SmsSender smsSender) throws IOException {

        if (log.isDebugEnabled()) {
            log.debug("Request to sent message {} ", smsSender);
        }
        String userLogin = smsSender.getPhone();
        String userMessage = smsSender.getMessage();

        if (userLogin != null && userMessage != null) {
            String url = URL1 + login + URL2 + password + URL3 + userLogin + URL4 + userMessage.replaceAll(" ", "+") + URL5 + URL6;
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            client.execute(request);
            log.info("SMS send "+userLogin); //todo: убрать
            if (log.isDebugEnabled()) {
                log.debug("SMS send");
            }
        } else {
            if (!userDao.existsByUsername(userLogin)) {
                if (log.isErrorEnabled()) {
                    log.error("Cannot find user with login {} ", login);
                }
                throw new BadRequestException(ExceptionCodes.USER_DOES_NOT_EXIST.getMsg());
            } else if (userLogin == null) {
                if (log.isErrorEnabled()) {
                    log.error("Login is empty");
                }
                throw new BadRequestException("Invalid login");
            } else if (userMessage == null) {
                if (log.isErrorEnabled()) {
                    log.error("Message is empty");
                }
                throw new BadRequestException("Invalid message");
            }
        }
    }
}
