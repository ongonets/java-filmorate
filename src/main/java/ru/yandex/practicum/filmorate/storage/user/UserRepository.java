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
    private static final String GET_ALL_USER_QUERY = "SELECT * FROM users";
    private static final String GET_USER_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String UPDATE_QUERY = "UPDATE users SET user_name = ?, email = ?, login = ?, birthday = ?  " +
            "WHERE user_id = ?";
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
                user.getId()
         );
        return user;
    }

    @Override
    public void deleteUser(long id) {

    }

    @Override
    public Optional<User> findUser(long id) {
        return findOne(GET_USER_BY_ID_QUERY,id);
    }

    @Override
    public Collection<User> findAllUser() {
        return findMany(GET_ALL_USER_QUERY);
    }

    @Override
    public void addFriend(long id, long friendId) {

    }

    @Override
    public Collection<User> findAllfriends(long id) {
        return null;
    }

    @Override
    public void deleteFriend(long id, long friendId) {

    }

    @Override
    public Collection<User> findCommonfriends(long id, long otherId) {
        return null;
    }
}
