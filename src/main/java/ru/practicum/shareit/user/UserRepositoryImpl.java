package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateException;
import java.util.Objects;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final UserRepository userRepository;

    public UserRepositoryImpl(@Lazy UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User update(User user) {
        User newUser = userRepository.findById(user.getId()).get();
        if (user.getEmail() != null) {
            if(userRepository.findAllByEmail(user.getEmail()).size() > 0 &&
                    !Objects.equals(userRepository.findAllByEmail(user.getEmail()).get(0).getId(), user.getId())) {
                throw new DuplicateException("Email already exist.");
            }
            newUser.setEmail(user.getEmail());
        }
        if (user.getName() != null)
            newUser.setName(user.getName());
        return userRepository.save(newUser);
    }
}
