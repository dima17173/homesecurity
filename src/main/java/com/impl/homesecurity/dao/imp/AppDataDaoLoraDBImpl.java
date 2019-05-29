package com.impl.homesecurity.dao.imp;

import com.impl.homesecurity.dao.AppDataDaoLoraDB;
import com.impl.homesecurity.dao.mapper.AppDataRowMapper;
import com.impl.homesecurity.domain.AppData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class AppDataDaoLoraDBImpl implements AppDataDaoLoraDB {

    private final Logger log = LoggerFactory.getLogger(AppDataDaoLoraDBImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final AppDataRowMapper dataRowMapper;

    @Autowired
    public AppDataDaoLoraDBImpl(@Qualifier("mysqlJdbcTemplateMain") JdbcTemplate jdbcTemplate, AppDataRowMapper dataRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataRowMapper = dataRowMapper;
    }

    @Override
    public List<AppData> getAllDeviceSignalsLoraDB() {
        if (log.isDebugEnabled()) {
            log.debug("Try get all app data");
        }
        String sql = "SELECT id, mote, `time`, time_usec, accurateTime, seqNo, `port`, `data` FROM appdata";
        return jdbcTemplate.query(sql, dataRowMapper);
    }

    @Override
    public AppData getLastDeviceSignalLoraDB() {
        if (log.isDebugEnabled()) {
            log.debug("Try get last device signal from LoraDB app data");
        }
        String sql = "SELECT id, mote, `time`, time_usec, accurateTime, seqNo, `port`, `data` FROM appdata WHERE id=(SELECT MAX(id) FROM appdata)";

        try {
            return jdbcTemplate.queryForObject(sql, dataRowMapper);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<AppData> getIntervalAppDataLoraDBById(long fromId, long toId) {
        if (log.isDebugEnabled()) {
            log.debug("Try get LoraDB's app data from id " + fromId + " to id " + toId);
        }
        String sql = "SELECT id, mote, `time`, time_usec, accurateTime, seqNo, `port`, `data` FROM appdata WHERE id BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, dataRowMapper, fromId, toId);
    }

    @Override
    public List<AppData> getIntervalAppDataByDate(Timestamp fromDate, Timestamp toDate) {
        if (log.isDebugEnabled()) {
            log.debug("Try get app data from date " + fromDate + " to date " + toDate);
        }
        String sql = "SELECT id, mote, `time`, time_usec, accurateTime, seqNo, `port`, `data` FROM appdata WHERE time BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, dataRowMapper, fromDate, toDate);
    }
}
