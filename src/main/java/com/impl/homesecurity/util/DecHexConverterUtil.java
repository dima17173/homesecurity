package com.impl.homesecurity.util;

import org.springframework.stereotype.Component;

@Component
public class DecHexConverterUtil {

    //конвертирование десятичной числа в шестнадцатеричную
    public static String convertDecimalToHex(Long decimal) {
        return Long.toHexString(decimal);
    }

    //конвертирование шестнадцатеричного числа в десятичную систему исчисления
    public static Long convertHexToDecimal(String hex) {
        return Long.parseLong(hex, 16);
    }
}
