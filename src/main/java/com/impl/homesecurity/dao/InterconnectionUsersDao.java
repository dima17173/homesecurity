package com.impl.homesecurity.dao;

import com.impl.homesecurity.domain.InterconnectionUsers;
import com.impl.homesecurity.domain.MainUser;
import java.util.List;

public interface InterconnectionUsersDao {

    List<MainUser> getAllUsersDevicesByMainUserId(long mainUserId);
    InterconnectionUsers getInterconnectionUserByMainUser(long mainUserId);
    int addInterconnectionUsers(InterconnectionUsers interconnectionUsers);
    int deleteUserByMainUser(long mainUserId, long userId);
    int deleteAllUsersByMainUser(long mainUserId);
    int deleteUserById(Long userId);
}
