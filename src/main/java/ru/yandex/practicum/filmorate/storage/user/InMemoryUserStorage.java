package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;


@Component
@Qualifier("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users;

    public InMemoryUserStorage() {
        users = new HashMap<>();
    }

    @Override
    public User addUser(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(long id) {
        users.remove(id);
    }

    @Override
    public Optional<User> findUser(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Collection<User> findAllUser() {
        return users.values();
    }

    @Override
    public void addFriend(long id, long friendId) {
        User user = users.get(id);
        Set<Long> friendSet;
        if (user.getFriends() == null) {
            friendSet = new HashSet<>();
        } else {
            friendSet = new HashSet<>(user.getFriends());
        }
        friendSet.add(friendId);
        user.setFriends(friendSet);
    }

    @Override
    public Collection<User> findAllfriends(long id) {
        if (users.get(id).getFriends() == null) {
            return new ArrayList<>();
        }
        return users.get(id).getFriends().stream()
                .map(users::get).collect(Collectors.toList());
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        User user = users.get(id);
        Set<Long> friendSet;
        if (user.getFriends() == null) {
            return;
        } else {
            friendSet = new HashSet<>(user.getFriends());
        }
        friendSet.remove(friendId);
        user.setFriends(friendSet);
    }

    @Override
    public Collection<User> findCommonfriends(long id, long otherId) {
        Set<Long> friends = users.get(otherId).getFriends();
        return users.get(id).getFriends().stream()
                .filter(friends::contains)
                .map(users::get)
                .collect(Collectors.toList());

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
