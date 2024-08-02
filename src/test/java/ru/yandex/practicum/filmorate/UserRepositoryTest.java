package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserRepository;
import ru.yandex.practicum.filmorate.storage.user.UserRowMapper;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserRepository.class, UserRowMapper.class})
public class UserRepositoryTest {
    @Qualifier("userRepository")
    private final UserStorage userRepository;

    static User getUser() {
        User user = new User();
        user.setId(1);
        user.setEmail("testEmail1");
        user.setLogin("testLogin1");
        user.setName("testName1");
        user.setBirthday(LocalDate.of(2020, 11, 23));
        return user;
    }

    @Test
    public void testFindUserById() {

        Optional<User> userOptional = userRepository.findUser(1);

        assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(getUser());
    }

    @Test
    public void testFindAllUsers() {

        Collection<User> users = userRepository.findAllUser();
        assertThat(users.size() == 3);
        assertThat(users.stream().findFirst())
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(getUser());
    }

    @Test
    public void testAddUser() {
        User user = new User();
        user.setId(4);
        user.setEmail("testEmail4");
        user.setLogin("testLogin4");
        user.setName("testName4");
        user.setBirthday(LocalDate.of(2020, 11, 23));

        userRepository.addUser(user);

        Optional<User> userOptional = userRepository.findUser(4);
        assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(user);
    }

    @Test
    public void testFindAllfriends() {
        Collection<User> users = userRepository.findAllfriends(3);

        assertThat(users.size() == 2);

        assertThat(users.stream().filter(user -> user.getId() == 1).findFirst())
                .isEmpty();

        users = userRepository.findAllfriends(2);

        assertThat(users.size() == 2);

        assertThat(users.stream().filter(user -> user.getId() == 1).findFirst())
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(getUser());
    }

    @Test
    public void testDeleteFriend() {
        Collection<User> users = userRepository.findAllfriends(2);

        assertThat(users.size() == 2);

        assertThat(users.stream().filter(user -> user.getId() == 1).findFirst())
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(getUser());

        userRepository.deleteFriend(2, 1);

        users = userRepository.findAllfriends(2);

        assertThat(users.size() == 1);

        assertThat(users.stream().filter(user -> user.getId() == 1).findFirst())
                .isEmpty();
    }

    @Test
    public void testAddFriend() {
        Collection<User> users = userRepository.findAllfriends(3);

        assertThat(users.size() == 1);

        assertThat(users.stream().filter(user -> user.getId() == 1).findFirst())
                .isEmpty();

        userRepository.addFriend(3, 1, 1);

        users = userRepository.findAllfriends(3);

        assertThat(users.size() == 1);

        assertThat(users.stream().filter(user -> user.getId() == 1).findFirst())
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(getUser());
    }

    @Test
    public void testUpdateUser() {
        User user = getUser();
        user.setId(3);

        Optional<User> userOptional = userRepository.findUser(3);
        assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isNotEqualTo(user);

        userRepository.updateUser(user);

        userOptional = userRepository.findUser(3);
        assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(user);
    }

    @Test
    public void testFindCommonfriends() {
        Collection<User> users = userRepository.findCommonfriends(1, 2);

        assertThat(users.size() == 1);

        assertThat(users.stream().filter(user -> user.getId() == 3).findFirst())
                .isPresent();
    }
}
