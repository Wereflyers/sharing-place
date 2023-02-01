package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long>, QuerydslPredicateExecutor<Booking>,
        BookingRepositoryCustom {

    /**
     * Показывает все бронирования пользователя, отсортированные по дате.
     * @param userId - автор бронирований
     * @return List
     */
    List<Booking> findAllByBookerIdOrderByStart(Long userId);

    /**
     * Показывает все завершенные бронирования пользователя, отсортированные по дате.
     * @param userId - автор бронирований
     * @param date - текущая дата
     * @return List
     */
    List<Booking> findAllByBookerIdAndEndIsBeforeOrderByStart(Long userId, LocalDateTime date);

    /**
     * Показывает все будущие бронирования пользователя, отсортированные по дате.
     * @param userId - автор бронирований
     * @param date - текущая дата
     * @return List
     */
    List<Booking> findAllByBookerIdAndStartIsAfterOrderByStart(Long userId, LocalDateTime date);

    /**
     * Показывает все текущие бронирования пользователя, отсортированные по дате.
     * @param userId - автор бронирований
     * @param date - текущая дата
     * @param toDate - текущая дата
     * @return List
     */
    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStart(
            Long userId, LocalDateTime date, LocalDateTime toDate);

    /**
     * Показывает все бронирования пользователя с выбранным статусом.
     * @param userId - автор бронирований
     * @param status - статус бронирования
     * @return List
     */
    List<Booking> findAllByBookerIdAndStatusOrderByStart(Long userId, BookingStatus status);
}