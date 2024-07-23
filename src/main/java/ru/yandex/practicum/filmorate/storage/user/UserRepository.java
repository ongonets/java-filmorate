package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;


@Repository
@Qualifier("userRepository")
public class UserRepository extends BaseRepository<User> implements UserStorage {

    private static final String INSERT_QUERY = "INSERT INTO users (user_name,email,login,birthday) VALUES (?,?,?,?)";
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO friends (user_id,friend_id,status_id) " +
            "VALUES (?,?,?)";
    private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String FIND_USER_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String FIND_ALL_FRIENDS_QUERY = "SELECT f.friend_id AS user_id, u.email, u.login, " +
            "u.user_name, u.birthday FROM friends AS f JOIN users AS u ON f.user_id = u.user_id " +
            "WHERE f.user_id = ? AND STATUS_ID = 1";
    private static final String FIND_COMMON_FRIENDS_QUERY = "SELECT f.friend_id AS user_id, u.email, u.login, " +
            "u.user_name, u.birthday  FROM friends AS f JOIN users AS u ON f.user_id = u.user_id " +
            "WHERE f.user_id= ? AND f.friend_id IN (SELECT friend_id FROM friends WHERE user_id = ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET user_name = ?, email = ?, login = ?, birthday = ?  " +
            "WHERE user_id = ?";
    private static final String UPDATE_FRIEND_QUERY = "UPDATE friends SET user_id = ?, friend_id = ?, status_id = ?" +
            "WHERE user_id = ?";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";


    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public User addUser(User user) {
        long id = insert(INSERT_QUERY,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                Timestamp.valueOf(user.getBirthday().atStartOfDay()));
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        update(UPDATE_QUERY,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                Timestamp.valueOf(user.getBirthday().atStartOfDay()),
                user.getId());
        return user;
    }

    @Override
    public void deleteUser(long id) {
    }

    @Override
    public Optional<User> findUser(long id) {
        return findOne(FIND_USER_BY_ID_QUERY, id);
    }

    @Override
    public Collection<User> findAllUser() {
        return findMany(FIND_ALL_USERS_QUERY);
    }

    @Override
    public void addFriend(long id, long friendId, long statusId) {
        update(INSERT_FRIEND_QUERY, id, friendId, statusId);
    }

    @Override
    public void updateFriend(long id, long friendId, long statusId) {
        update(UPDATE_FRIEND_QUERY, id, friendId, statusId, id);
    }

    @Override
    public Collection<User> findAllfriends(long id) {
        return findMany(FIND_ALL_FRIENDS_QUERY, id);
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        update(DELETE_FRIEND_QUERY, id, friendId);
    }

    @Override
    public Collection<User> findCommonfriends(long id, long otherId) {
        return findMany(FIND_COMMON_FRIENDS_QUERY, id, otherId);
    }
}
