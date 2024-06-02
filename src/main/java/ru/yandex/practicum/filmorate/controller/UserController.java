package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Получен запрос на добавление  пользователя {}", user);
        validation(user);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User newUser) {
        if (newUser.getId() == 0) {
            throw new ValidationException("Id должен быть указан");
        }
        validation(newUser);
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            oldUser = oldUser.toBuilder()
                    .login(newUser.getLogin())
                    .email(newUser.getEmail())
                    .birthday(newUser.getBirthday())
                    .name(newUser.getName())
                    .build();
            log.info("Обновлен пользователь {}", newUser);
            // если публикация найдена и все условия соблюдены, обновляем её содержимое
            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    private User validation(User user) {
        if (user.getEmail() == null || !(user.getEmail().matches("^(.+)@(\\S+)$"))) {
            log.warn("Некорректно введен имейл у пользователя {}", user);
            throw new ValidationException("Имейл указан некорректно");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || !(user.getLogin().matches("^(.+)$"))) {
            log.warn("Некорректно введен логин у пользователя {}", user);
            throw new ValidationException("Логин указан некоректно");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Некорректно введена дата рождения у пользователя {}", user);
            throw new ValidationException("Дата рождения указана некоректно");
        }
        return user;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
