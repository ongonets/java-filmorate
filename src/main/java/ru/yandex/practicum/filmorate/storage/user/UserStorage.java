package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User addUser(User user);
    User updateUser(User user);
    void deleteUser(long id);
    Optional<User> findUser(long id);
    Collection<User> findAllUser();


}
