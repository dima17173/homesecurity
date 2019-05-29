package com.impl.homesecurity.service;

import com.impl.homesecurity.domain.AppData;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by diana.
 * Creation date 22.11.18.
 */
public interface AppDataLoraDBService {

    List<AppData> getAllDeviceSignalsLoraDB();

    AppData getLastDeviceSignalLoraDB();

    List<AppData> getIntervalAppDataLoraDBById(long fromId, long toId);

    List<AppData> getIntervalAppDataByDate(Timestamp fromDate, Timestamp toDate);

    void filterDataByDeviceStatus(AppData appData);
}
