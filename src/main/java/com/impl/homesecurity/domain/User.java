package com.impl.homesecurity.domain;

import com.impl.homesecurity.domain.enumeration.Role;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class User {

    private Long id;
    private String name;
    private String login;
    private String password;
    private Boolean firstSignIn;
    private User mainUser;
    private Role role;

    public User (Long id){
        this.id = id;
    }

    public User(String name ,String login) {
        this.name = name;
        this.login = login;
    }
}
