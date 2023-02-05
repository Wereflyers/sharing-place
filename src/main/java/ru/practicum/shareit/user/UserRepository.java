package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User>, UserRepositoryCustom {

    /**
     * Поиск всех пользователей по емейлу.
     * @param email - емейл
     * @return List
     */
    List<User> findAllByEmail(String email);

}