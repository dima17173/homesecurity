package com.impl.homesecurity.domain;

import com.impl.homesecurity.domain.enumeration.DayOfWeek;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by dima.
 * Creation date 29.11.18.
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class AutoMode {

    private Date time;
    private Boolean repeatable;
    private Boolean action;
    private Boolean status;
    private List<Integer> dayOfWeek;
    private int successfullyCompleted = 0;
    private Date lastPerform;
    private long lastUpdate;


    public String toJson() {
        LocalTime autoModeTime = LocalDateTime.ofInstant(time.toInstant(), ZoneId.systemDefault()).toLocalTime();
        return "{" +
                "\"autoMode\":{" +
                "\"time\":" +  "\"" + autoModeTime + "\""  +
                ", \"repeatable\":" + repeatable +
                ", \"action\":" + action +
                ", \"status\":" + status +
                ", \"dayOfWeek\":" + "" + dayOfWeek + "" +
                ", \"successfullyCompleted\":" + successfullyCompleted +
                ", \"lastPerform\":" + "\"" + lastPerform + "\"" +
                ", \"lastUpdate\":" + "\"" + lastUpdate + "\"" +
                '}' +
                "}";
    }

    @Override
    public String toString() {
        LocalTime automodeTime = LocalDateTime.ofInstant(time.toInstant(), ZoneId.systemDefault()).toLocalTime();
        return "AutoMode{" +
                "time=" + automodeTime +
                ", repeatable=" + repeatable +
                ", action=" + action +
                ", status=" + status +
                ", dayOfWeek=" + dayOfWeek +
                ", successfullyCompleted=" + successfullyCompleted +
                ", lastPerform=" + lastPerform +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
