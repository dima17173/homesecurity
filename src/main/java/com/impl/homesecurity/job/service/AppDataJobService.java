package com.impl.homesecurity.job.service;

import com.impl.homesecurity.domain.AppData;
import com.impl.homesecurity.service.AppDataLoraDBService;
import com.impl.homesecurity.service.ApplicationDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.SchedulingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

/**
 * Обработчик новых сигналов из базы LoraDB
 */
@Service
public class AppDataJobService {

    private final Logger log = LoggerFactory.getLogger("Scheduled");
    private final AppDataLoraDBService appDataLoraDBService;
    private final ApplicationDataService applicationDataService;

    @Autowired
    public AppDataJobService(AppDataLoraDBService appDataLoraDBService, ApplicationDataService applicationDataService) {
        this.appDataLoraDBService = appDataLoraDBService;
        this.applicationDataService = applicationDataService;
    }

    private List<AppData> getIntervalAppDataLoraDBById(long fromId, long toId) {
        return appDataLoraDBService.getIntervalAppDataLoraDBById(fromId, toId);
    }

    private void filterDataByDeviceStatus(AppData appData) {
        appDataLoraDBService.filterDataByDeviceStatus(appData);
    }

    /**
     * Получаем и обрабатываем только уникальные события открытия/закрытия каждые 10 секунд
     */
    @Scheduled(fixedRate = 10000)
    public void getDataFromAppData() {
        if (log.isDebugEnabled()) {
            log.debug("Try to get new signals from LoraDB");
        }
        try {
            AppData lastDeviceSignal = applicationDataService.getLastDeviceSignal();
            AppData lastDeviceSignalLoraDB = appDataLoraDBService.getLastDeviceSignalLoraDB();

            if(lastDeviceSignal != null && lastDeviceSignal.getId() != null
                    && lastDeviceSignalLoraDB != null && lastDeviceSignalLoraDB.getId() != null){
                if(!Objects.equals(lastDeviceSignal.getId(), lastDeviceSignalLoraDB.getId())){
                    if (log.isInfoEnabled()) {
                        log.info("Getting settings from {} to {}", lastDeviceSignal.getId(), lastDeviceSignalLoraDB.getId());
                    }
                    List<AppData> intervalAppDataLoraDBById = getIntervalAppDataLoraDBById(lastDeviceSignal.getId(), lastDeviceSignalLoraDB.getId());

                    long lastSeqNo = lastDeviceSignal.getSeqNo();
                    for (AppData appDataLoraDB : intervalAppDataLoraDBById) {
                        if (appDataLoraDB.getSeqNo() != lastSeqNo && appDataLoraDB.getData() != null && appDataLoraDB.getData().startsWith("0a0")) {
                            filterDataByDeviceStatus(appDataLoraDB);
                            applicationDataService.addAppData(appDataLoraDB);
                            lastSeqNo = appDataLoraDB.getSeqNo();
                        } else {
                            if (log.isInfoEnabled()) {
                                log.info("Invalid signal from LoraDB");
                            }
                        }
                    }
                }
            } else {
                if (log.isErrorEnabled()) {
                    log.error("Application data must not be empty");
                }
            }
        } catch (SchedulingException e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage());
            }
        }
    }
}