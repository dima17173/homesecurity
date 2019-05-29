package com.impl.homesecurity.dao.imp;

import com.impl.homesecurity.dao.ApplicationDataDao;
import com.impl.homesecurity.dao.mapper.AppDataRowMapper;
import com.impl.homesecurity.domain.AppData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by diana.
 * Creation date 13.12.18.
 */
@Repository
public class ApplicationDataDaoImpl implements ApplicationDataDao {

    private final Logger log = LoggerFactory.getLogger(ApplicationDataDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final AppDataRowMapper dataRowMapper;

    @Autowired
    public ApplicationDataDaoImpl(@Qualifier("mysqlJdbcTemplateApp") JdbcTemplate jdbcTemplate, AppDataRowMapper dataRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataRowMapper = dataRowMapper;
    }

    @Override
    public List<AppData> getAllDeviceSignals() {
        if (log.isDebugEnabled()) {
            log.debug("Try get all device signals from our app data");
        }
        String sql = "SELECT id, mote, `time`, time_usec, accurateTime, seqNo, `port`, `data` FROM appdata";
        return jdbcTemplate.query(sql, dataRowMapper);
    }

    @Override
    public AppData getLastDeviceSignal() {
        if (log.isDebugEnabled()) {
            log.debug("Try get last device signal from our app data");
        }
        String sql = "SELECT id, mote, `time`, time_usec, accurateTime, seqNo, `port`, `data` FROM appdata WHERE id=(SELECT MAX(id) FROM appdata)";

        try {
            return jdbcTemplate.queryForObject(sql, dataRowMapper);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public int addAppData(AppData appData) {
        if (log.isDebugEnabled()) {
            log.debug("try adding new appData to DB");
        }
        String sql = "INSERT INTO appdata (id, mote, time, time_usec, accurateTime, seqNo, port, data) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, appData.getId(), appData.getMote(), appData.getTime(), appData.getTime_usec(),
                appData.getAccurateTime(), appData.getSeqNo(), appData.getPort(), appData.getData());
    }
}
