package com.impl.homesecurity.util;

import java.util.Random;

public class TemporaryPasswordUtil {

    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "abcdefghijklmnopqrstuvwxyz0123456789?$&*%";
    private static final int length = 6;

    //создание временного пароля
    public static String createTemporaryPassword() {

        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }

        return String.valueOf(result);
    }
}
