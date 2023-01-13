package ru.practicum.shareit.item;

import org.springframework.jdbc.core.RowMapper;
import ru.practicum.shareit.item.model.Item;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemMapper implements RowMapper<Item> {
    @Override
    public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Item.builder()
                .id(rs.getLong("id"))
                .ownerId(rs.getLong("ownerId"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .available(rs.getBoolean("added"))
                .rentTimes(rs.getLong("rentTimes"))
                .build();
    }
}
