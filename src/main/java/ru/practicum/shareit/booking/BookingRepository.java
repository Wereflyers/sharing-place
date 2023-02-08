package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, QuerydslPredicateExecutor<Booking> {

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

    /**
     * Все бронирования хозяина предмета, отсортированные по дате.
     * @param userId - хозяин вещи
     * @return List
     */
    List<Booking> findAllByOwnerIdOrderByStartDesc(Long userId);

    /**
     * Все бронирования хозяина предмета с выбранным статусом, отсортированные по дате.
     * @param userId - хозяин вещи
     * @param status - статус бронирования
     * @return List
     */
    List<Booking> findAllByOwnerIdAndStatusOrderByStart(Long userId, BookingStatus status);

    /**
     * Все бронирования по айди предмета, отсортированные по дате.
     * @param itemId - айди предмета
     * @return List
     */
    List<Booking> findAllByItemIdOrderByStart(Long itemId);

    /**
     * Все бронирования выбранного предмета выбранным пользователем.
     * @param itemId - предмет
     * @param bookerId - пользователь
     * @return List
     */
    List<Booking> findByItemIdAndBookerId(Long itemId, Long bookerId);
}