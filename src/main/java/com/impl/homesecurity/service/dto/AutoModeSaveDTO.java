package com.impl.homesecurity.service.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

/**
 * Created by dima.
 * Creation date 14.01.19.
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class AutoModeSaveDTO {

    private String time;
    private Boolean repeatable;
    private Boolean action;
    private Boolean status;
    private List<Integer> dayOfWeek;
    private long lastUpdate;


    @Override
    public String toString() {
        return "{" +
                "\"autoMode\":{" +
                "\"time\":" +  "\"" + time + "\""  +
                ", \"repeatable\":" + repeatable +
                ", \"action\":" + action +
                ", \"status\":" + status +
                ", \"dayOfWeek\":" + "" + dayOfWeek + "" +
                ", \"lastUpdate\":" + "" + lastUpdate + "" +
                '}' +
                "}";
    }
}
