package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

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
        checkId(user.getId());
        User oldUser = userStorage.findUser(user.getId()).get();
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


    public void addFriend(long id, long friendId) {
        checkId(id);
        checkId(friendId);
        userStorage.addFriend(id, friendId);
        userStorage.addFriend(friendId, id);
        log.info("Пользователь {} добавил в друзья  пользователя {}", id, friendId);
    }

    public Collection<User> findAllFriend(long id) {
        checkId(id);
        return userStorage.findAllfriends(id);
    }

    public void deleteFriend(long id, long friendId) {
        checkId(id);
        checkId(friendId);
        userStorage.deleteFriend(id, friendId);
        userStorage.deleteFriend(friendId, id);
        log.info("Пользователь {} удалил из друзей пользователя {}", id, friendId);
    }

    public Collection<User> findCommonFriend(long id, long otherId) {
        checkId(id);
        checkId(otherId);
        return userStorage.findCommonfriends(id, otherId);
    }

    private void checkId(long id) {
        if (userStorage.findUser(id).isEmpty()) {
            log.warn("Ползователь с ID {} не найден", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }
}
