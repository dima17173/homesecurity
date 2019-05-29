package com.impl.homesecurity.service.impl;

import com.impl.homesecurity.dao.InterconnectionUsersDao;
import com.impl.homesecurity.domain.MainUser;
import com.impl.homesecurity.service.InterconnectionUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by dima.
 * Creation date 28.11.18.
 */
@Service
public class InterconnectionUserServiceImpl implements InterconnectionUserService {

    private final Logger log = LoggerFactory.getLogger(SettingsServiceImpl.class);
    private final InterconnectionUsersDao interconnectionUsersDao;

    @Autowired
    public InterconnectionUserServiceImpl(InterconnectionUsersDao interconnectionUsersDao) {
        this.interconnectionUsersDao = interconnectionUsersDao;
    }

    @Override
    public List<MainUser> getAllUsersDevicesByMainUserId(long mainUserId) {
        if (log.isDebugEnabled()) {
            log.debug("Request to get mainUser with id {} ", mainUserId);
        }
        return interconnectionUsersDao.getAllUsersDevicesByMainUserId(mainUserId);
    }
}
