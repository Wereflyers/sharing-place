package ru.practicum.shareit.booking;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingMapper implements RowMapper<Booking> {
    @Override
    public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Booking.builder()
                .userId(rs.getLong("userId"))
                .itemId(rs.getLong("itemId"))
                .fromDate(rs.getDate("fromDate").toLocalDate())
                .tillDate(rs.getDate("tillDate").toLocalDate())
                .approved(rs.getBoolean("approved"))
                .build();
    }
}
