package com.impl.homesecurity.service.impl;

import com.impl.homesecurity.dao.DeviceDao;
import com.impl.homesecurity.dao.imp.MoteDaoImpl;
import com.impl.homesecurity.domain.Device;
import com.impl.homesecurity.domain.Mote;
import com.impl.homesecurity.service.MoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MoteServiceImpl implements MoteService {

    private final Logger log = LoggerFactory.getLogger(MoteServiceImpl.class);
    private final MoteDaoImpl moteDao;
    private final DeviceDao deviceDao;

    @Autowired
    public MoteServiceImpl(MoteDaoImpl moteDao, DeviceDao deviceDao) {
        this.moteDao = moteDao;
        this.deviceDao = deviceDao;
    }

    /*Сравнивает список датчиков из двух таблиц, и если есть различие, то
    у Админа отображаются свободные датчики
    */
    @Override
    public List<Mote> getAllFreeDevice() {
        if (log.isInfoEnabled()) {
            log.info("Request to get free devices");
        }
        List<Mote> allMotes = moteDao.getAllMotes();
        if (allMotes.isEmpty()){
            if (log.isDebugEnabled()) {
                log.debug("No free motes");
            }
            return null;
        }

        List<Device> allDevices = deviceDao.getAllDevices();
        if(allDevices.isEmpty()){
            if (log.isDebugEnabled()) {
                log.debug("Devices are empty. Request to get all motes");
            }
            return allMotes.stream()
                    .map((d) -> new Mote("00" +
                            Long.toHexString(Long.parseLong(d.getEui())).toUpperCase()))
                    .collect(Collectors.toList());
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Request to add free devices");
            }
            List<Mote> motesDevice = allDevices.stream()
                    .map((d) -> new Mote(d.getDeviceNumber()))
                    .collect(Collectors.toList());

            List<Mote> freeDevice = new ArrayList<>();
            for (Mote mote : allMotes) {
                if (motesDevice.containsAll(allMotes)){
                    return null;
                } else if (!motesDevice.contains(mote)) {
                    String s = "00" + Long.toHexString(Long.parseLong(mote.getEui())).toUpperCase();
                    freeDevice.add(new Mote(s));
                }
            }
            return freeDevice;
        }
    }
}
