package com.impl.homesecurity.dao.imp;

import com.impl.homesecurity.dao.MoteDao;
import com.impl.homesecurity.dao.mapper.MoteRowMapper;
import com.impl.homesecurity.domain.Mote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class MoteDaoImpl implements MoteDao {

    private final Logger log = LoggerFactory.getLogger(DeviceDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final MoteRowMapper moteRowMapper;

    @Autowired
    public MoteDaoImpl(@Qualifier("mysqlJdbcTemplateMain") JdbcTemplate jdbcTemplate, MoteRowMapper moteRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.moteRowMapper = moteRowMapper;
    }

    @Override
    public List<Mote> getAllMotes() {
        if (log.isInfoEnabled()) {
            log.info("Try get all motes");
        }
        String sql = "SELECT eui FROM motes";
        return jdbcTemplate.query(sql, moteRowMapper);
    }
}
