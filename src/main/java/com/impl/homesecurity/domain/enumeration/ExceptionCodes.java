package com.impl.homesecurity.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCodes {

    DUPLICATE_LOGIN(0, "Пользователя с таким логином существует!"),
    INVALID_LENGTH(1,Constants.INVALID_LENGTH_VALUE),
    INVALID_PHONE(2, "Формат логин 0999999999"),
    USER_DOES_NOT_EXIST(3, "Пользователя c таким логином нет!"),
    ERROR_OLD_PASSWORD(4, "Старый пароль неверный"),
    DEVICE_DOES_NOT_EXIST(5, "Устройство с таким номером нет!"),
    INVALID_ROLE(6, "Роль недействительна"),
    INVALID_ROLE_FOR_CREATED(7, "Роле не разрешенно создавать"),
    ERROR_CREATING_USER(8, "Ошибка создания пользователя"),
    ERROR_CREATING_NEW_USER(9, "Роль пользователя не позволяет создавать новых пользователей"),
    ROLE_DOES_NOT_EXIST(10, "Role does not exist with number"),
    INVALID_LOGIN(11, "Логин не совпадает с текущим");

    private final int id;
    private final String msg;

    public static class Constants {
        public static final String INVALID_LENGTH_VALUE = "Длинна должна быть от {min} до {max} символов";
    }
}
