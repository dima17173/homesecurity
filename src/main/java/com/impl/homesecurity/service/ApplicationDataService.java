package com.impl.homesecurity.service;

import com.impl.homesecurity.domain.AppData;

import java.util.List;

/**
 * Created by diana.
 * Creation date 24.12.18.
 */
public interface ApplicationDataService {

    List<AppData> getAllDeviceSignals();

    AppData getLastDeviceSignal();

    int addAppData(AppData appData);
}
