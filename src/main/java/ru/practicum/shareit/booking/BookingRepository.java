package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long>, QuerydslPredicateExecutor<Booking>, BookingRepositoryCustom {

    /**
     * Показывает все бронирования пользователя, отсортированные по дате.
     * @param userId - автор бронирований
     * @return List
     */
    List<Booking> findAllByOwnerIdOrderByFromDate(Long userId);

    /**
     * Показывает все завершенные бронирования пользователя, отсортированные по дате.
     * @param userId - автор бронирований
     * @param date - текущая дата
     * @return List
     */
    List<Booking> findAllByOwnerIdAndTillDateIsBeforeOrderByFromDate(Long userId, LocalDate date);

    /**
     * Показывает все будущие бронирования пользователя, отсортированные по дате.
     * @param userId - автор бронирований
     * @param date - текущая дата
     * @return List
     */
    List<Booking> findAllByOwnerIdAndFromDateIsAfterOrderByFromDate(Long userId, LocalDate date);

    /**
     * Показывает все текущие бронирования пользователя, отсортированные по дате.
     * @param userId - автор бронирований
     * @param date - текущая дата
     * @param toDate - текущая дата
     * @return List
     */
    List<Booking> findAllByOwnerIdAndFromDateIsBeforeAndTillDateIsAfterOrderByFromDate
            (Long userId, LocalDate date, LocalDate toDate);

    /**
     * Показывает все бронирования пользователя с выбранным статусом.
     * @param userId - автор бронирований
     * @param status - статус бронирования
     * @return List
     */
    List<Booking> findAllByOwnerIdAndStatusOrderByFromDate(Long userId, BookingStatus status);
}