package com.impl.homesecurity.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Created by vyacheslav on 11.12.18.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString(callSuper = true)
public class SecurityNotification {

    private UUID uuid;
    private Long userId;
    private Timestamp createTime;
    private String message;
    private String device;

    public SecurityNotification() {
        this.uuid = UUID.randomUUID();
        this.createTime = new Timestamp(System.currentTimeMillis());
    }

    public SecurityNotification(Long userId, String message, String device) {
        this();
        this.userId = userId;
        this.message = message;
        this.device = device;
    }

    public String toText() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern( "uuuu-MM-dd HH:mm:ss" );
        return  "Msg:" + message + ", " +
                "Dev:" + device + ", " +
                "Time:" + createTime.toLocalDateTime().format(format);
    }
}
