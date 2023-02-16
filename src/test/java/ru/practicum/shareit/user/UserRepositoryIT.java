package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryIT {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void addUsers() {
        userRepository.save(User.builder()
                .email("john@mail.ru")
                .name("john")
                .build());
        userRepository.save(User.builder()
                .name("jane")
                .email("jane@mail.ru")
                .build());
    }

    @Test
    void findAllByEmail() {
        List<User> users = userRepository.findAllByEmail("john@mail.ru");

        assertFalse(users.isEmpty());
        assertEquals(users.size(), 1);
        assertEquals(users.get(0).getEmail(), "john@mail.ru");
        assertEquals(users.get(0).getName(), "john");
    }
}