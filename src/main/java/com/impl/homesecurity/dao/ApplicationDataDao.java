package com.impl.homesecurity.dao;

import com.impl.homesecurity.domain.AppData;
import java.util.List;

/**
 * Created by diana.
 * Creation date 13.12.18.
 */
public interface ApplicationDataDao {

    List<AppData> getAllDeviceSignals();

    AppData getLastDeviceSignal();

    int addAppData(AppData appData);
}
