package com.impl.homesecurity.service.dto;

import com.impl.homesecurity.domain.enumeration.DayOfWeek;
import lombok.*;
import java.util.List;

/**
 * Created by dima.
 * Creation date 21.12.18.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class AutoModeDTO {

    private Long id;
    private Long userId;
    private Long deviceId;
    private String time;
    private Boolean repeatable;
    private Boolean action;
    private Boolean status;
    private List<Integer> dayOfWeek;
    private int successfullyCompleted = 0;
}
