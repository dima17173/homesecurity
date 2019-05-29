package com.impl.homesecurity.service.impl;

import com.impl.homesecurity.dao.ApplicationDataDao;
import com.impl.homesecurity.domain.AppData;
import com.impl.homesecurity.service.ApplicationDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by dima.
 * Creation date 24.12.18.
 */
@Service
public class ApplicationDataServiceImpl implements ApplicationDataService {

    private final Logger log = LoggerFactory.getLogger(ApplicationDataServiceImpl.class);
    private final ApplicationDataDao applicationDataDao;

    @Autowired
    public ApplicationDataServiceImpl(ApplicationDataDao applicationDataDao) {
        this.applicationDataDao = applicationDataDao;
    }

    @Override
    public List<AppData> getAllDeviceSignals() {
        if (log.isDebugEnabled()) {
            log.debug("Try to get all application data");
        }
        return applicationDataDao.getAllDeviceSignals();
    }

    @Override
    public AppData getLastDeviceSignal() {
        if (log.isDebugEnabled()) {
            log.debug("Try get last device signal from our app data");
        }
        return applicationDataDao.getLastDeviceSignal();
    }

    @Override
    public int addAppData(AppData appData) {
        if (log.isDebugEnabled()) {
            log.debug("Request to add application data : {}", appData);
        }
        return applicationDataDao.addAppData(appData);
    }
}
