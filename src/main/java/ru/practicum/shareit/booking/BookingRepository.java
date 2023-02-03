package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long>, QuerydslPredicateExecutor<Booking>,
        BookingRepositoryCustom {

    /**
     * Показывает все бронирования пользователя, отсортированные по дате.
     * @param userId - автор бронирований
     * @return List
     */
    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId);

    /**
     * Показывает все бронирования пользователя с выбранным статусом.
     * @param userId - автор бронирований
     * @param status - статус бронирования
     * @return List
     */
    List<Booking> findAllByBookerIdAndStatusOrderByStart(Long userId, BookingStatus status);

    List<Booking>findAllByOwnerIdOrderByStartDesc(Long userId);

    List<Booking> findAllByOwnerIdAndStatusOrderByStart(Long userId, BookingStatus status);

    List<Booking> findAllByItemIdAndStartIsAfterOrderByStart(Long itemId, LocalDateTime now);

    List<Booking> findAllByItemIdAndStartIsBeforeOrderByStart(Long itemId, LocalDateTime now);

    List<Booking> findByItemIdAndBookerId(Long itemId, Long bookerId);
}