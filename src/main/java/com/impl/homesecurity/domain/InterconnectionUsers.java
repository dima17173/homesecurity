package com.impl.homesecurity.domain;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class InterconnectionUsers {

    private Long id;
    private User mainUser;
    private User user;

    public InterconnectionUsers(User mainUser, User user) {
        this.mainUser = mainUser;
        this.user = user;
    }
}
