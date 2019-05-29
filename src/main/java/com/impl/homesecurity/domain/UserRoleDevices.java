package com.impl.homesecurity.domain;

import com.impl.homesecurity.domain.enumeration.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

/**
 * Created by vyacheslav on 25.10.18.
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserRoleDevices extends User{

    private List<Role> authorities;
    private List<Device> devices;

    public UserRoleDevices() {
        super();
    }

    public UserRoleDevices(String name, String login) {
        super(name, login);
    }
}
