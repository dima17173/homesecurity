package com.impl.homesecurity.dao.mapper;

import com.impl.homesecurity.domain.AppData;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AppDataRowMapper implements RowMapper<AppData> {

    @Override
    public AppData mapRow(ResultSet row, int rowNum) throws SQLException {
        AppData appData = new AppData();

        appData.setId(row.getLong("id"));
        appData.setMote(row.getLong("mote"));
        appData.setTime(row.getTimestamp("time"));
        appData.setTime_usec(row.getInt("time_usec"));
        appData.setAccurateTime(row.getInt("accurateTime"));
        appData.setSeqNo(row.getInt("seqNo"));
        appData.setPort(row.getInt("port"));
        appData.setData(row.getString("data"));

        return appData;
    }
}
