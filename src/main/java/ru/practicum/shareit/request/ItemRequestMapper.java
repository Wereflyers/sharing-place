package ru.practicum.shareit.request;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ItemRequestMapper implements RowMapper<ItemRequest> {
    @Override
    public ItemRequest mapRow (ResultSet rs, int rowNum) throws SQLException {
        return ItemRequest.builder()
                .id(rs.getLong("id"))
                .userId(rs.getLong("userId"))
                .name(rs.getString("name"))
                .added(rs.getBoolean("added"))
                .build();
    }
}
