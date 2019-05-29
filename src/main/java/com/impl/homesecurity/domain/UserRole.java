package com.impl.homesecurity.domain;

import com.impl.homesecurity.domain.enumeration.Role;
import lombok.*;

/**
 * Created by vyacheslav on 24.10.18.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class UserRole {

    private Long userId;
    private Role role;
}
