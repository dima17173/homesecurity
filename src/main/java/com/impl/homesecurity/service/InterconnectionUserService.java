package com.impl.homesecurity.service;

import com.impl.homesecurity.domain.MainUser;
import java.util.List;

/**
 * Created by dima.
 * Creation date 28.11.18.
 */
public interface InterconnectionUserService {
    List<MainUser> getAllUsersDevicesByMainUserId(long mainUserId);
}
