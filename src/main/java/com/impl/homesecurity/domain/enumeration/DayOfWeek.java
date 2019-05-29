package com.impl.homesecurity.domain.enumeration;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dima.
 * Creation date 29.11.18.
 */
public enum DayOfWeek {

    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY;

    public static int getCurrentDay(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        String day = simpleDateFormat.format(date);
        switch (day) {
            case "Sunday":
                return 0;
            case "Monday":
                return 1;
            case "Tuesday":
                return 2;
            case "Wednesday":
                return 3;
            case "Thursday":
                return 4;
            case "Friday":
                return 5;
            default:
                return 6;
        }
    }
}
