package com.impl.homesecurity.dao;

import com.impl.homesecurity.domain.AppData;
import java.sql.Timestamp;
import java.util.List;

public interface AppDataDaoLoraDB {

    List<AppData> getAllDeviceSignalsLoraDB();

    AppData getLastDeviceSignalLoraDB();

    List<AppData> getIntervalAppDataLoraDBById(long fromId, long toId);

    List<AppData> getIntervalAppDataByDate(Timestamp fromDate, Timestamp toDate);
}
