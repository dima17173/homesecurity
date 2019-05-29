package com.impl.homesecurity.dao.mapper;

import com.impl.homesecurity.domain.Mote;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MoteRowMapper implements RowMapper<Mote> {

    @Override
    public Mote mapRow(ResultSet row, int rowNum) throws SQLException {
        Mote mote = new Mote();
        mote.setEui(row.getString("eui"));

        return mote;
    }
}
