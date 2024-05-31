package com.irsan.templateproject.entity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 29/05/2024
 */
public class ObjectMapper implements RowMapper<Map<String, Object>> {

    @Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
        int noOfColumnIndex = rs.getMetaData().getColumnCount();
        Map<String, Object> rowMap = new HashMap<>();
        for (int columnIndex = 1; columnIndex <= noOfColumnIndex; columnIndex++) {
            String columnName = rs.getMetaData().getColumnName(columnIndex);
            Object columnValue = rs.getObject(columnIndex);
            if (columnValue != null) {
                rowMap.put(columnName.toLowerCase(), columnValue);
            } else {
                rowMap.put(columnName.toLowerCase(), "");
            }
        }
        return rowMap;
    }

}
