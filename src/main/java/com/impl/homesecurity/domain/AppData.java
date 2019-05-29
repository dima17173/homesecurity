package com.impl.homesecurity.domain;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class AppData {

    private Long id;
    private Long mote;
    private Date time;
    private int time_usec;
    private int accurateTime;
    private int seqNo;
    private int port;
    private String data;
}
