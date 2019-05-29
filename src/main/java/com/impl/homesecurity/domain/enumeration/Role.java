package com.impl.homesecurity.domain.enumeration;

import com.impl.homesecurity.web.rest.errors.AppException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by dima.
 * Creation date 29.10.18.
 */
@Getter
@AllArgsConstructor
public enum Role {

    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_SUPERADMIN("ROLE_SUPERADMIN"),
    ROLE_USER("ROLE_USER"),
    ROLE_SUPERUSER("ROLE_SUPERUSER");

    private final String roleName;

    public static Role getRoleByNumber(int number) {
        switch (number) {
            case 1 : return Role.ROLE_ADMIN;
            case 2 : return Role.ROLE_SUPERADMIN;
            case 3 : return Role.ROLE_USER;
            case 4 : return Role.ROLE_SUPERUSER;
        }
        throw new AppException(ExceptionCodes.ROLE_DOES_NOT_EXIST);
    }

    public static boolean isValid(String name) {
        Role[] roles = Role.values();
        for (Role role : roles) {
            if (role.getRoleName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public int getCustomOrdinal() {
        if (this.equals(ROLE_ADMIN)) {
            return 1;
        } else if (this.equals(ROLE_SUPERADMIN)) {
            return 2;
        } else if (this.equals(ROLE_USER)) {
            return 3;
        } else if (this.equals(ROLE_SUPERUSER)) {
            return 4;
        }
        return 0;
    }
}
