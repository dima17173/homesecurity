package com.impl.homesecurity.util;

import java.util.regex.Pattern;

/**
 * Created by dima.
 * Creation date 20.11.18.
 */
public class PhoneValidatorUtil {

    private static final String PHONE_NUMBER_REGEX = "^(380)[0-9]{2}[0-9]{3}[0-9]{2}[0-9]{2}$";
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(PHONE_NUMBER_REGEX);

    //валидация на номер телефона украинского оператора
    public static boolean validatePhoneNumber(String login) {
        return login != null && PHONE_NUMBER_PATTERN.matcher(login).matches();
    }
}
