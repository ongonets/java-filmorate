package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAllUser() {
        return userStorage.findAllUser();
    }

    public User createUser(User user) {
        validation(user);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        userStorage.addUser(user);
        log.info("Добавлен пользователь {}", user);
        return user;

    }

    public User updateUser(User user) {
        if (user.getId() == 0) {
            throw new ValidationException("Id должен быть указан");
        }
        validation(user);
        Optional<User> userOpt = userStorage.findUser(user.getId());
        if (userOpt.isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }
        User oldUser = userOpt.get();
        oldUser = oldUser.toBuilder()
                .login(user.getLogin())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .name(user.getName())
                .build();
        userStorage.updateUser(oldUser);
        log.info("Обновлен пользователь {}", user);
        return oldUser;

    }

    private void validation(User user) {
        if (user.getEmail() == null || !(user.getEmail().matches("^(.+)@(\\S+)$"))) {
            log.warn("Некорректно введен имейл у пользователя {}", user);
            throw new ValidationException("Имейл указан некорректно");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || !(user.getLogin().matches("^(.+)$"))) {
            log.warn("Некорректно введен логин у пользователя {}", user);
            throw new ValidationException("Логин указан некоректно");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Некорректно введена дата рождения у пользователя {}", user);
            throw new ValidationException("Дата рождения указана некоректно");
        }
    }


}
