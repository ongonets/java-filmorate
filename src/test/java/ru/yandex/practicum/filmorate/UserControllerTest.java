package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

public class UserControllerTest {
    private final UserService userService = new UserService(new InMemoryUserStorage());

    @Test
    public void validation_shouldValidateByEmail() {
        User user = new User();
        user.setName("Имя");
        user.setBirthday(LocalDate.of(1990, 12, 20));
        user.setLogin("Логин");

        Assertions.assertThrows(ValidationException.class, () -> userService.createUser(user));
        User user2 = new User();
        user.setEmail("   ");
        Assertions.assertThrows(ValidationException.class, () -> userService.createUser(user2));
        User user3 = new User();
        user.setEmail("examplemail.com");
        Assertions.assertThrows(ValidationException.class, () -> userService.createUser(user3));
    }

    @Test
    public void validation_shouldValidateByLogin() {
        User user = new User();
        user.setName("Имя");
        user.setEmail("example@mail.com");
        user.setBirthday(LocalDate.of(1990, 12, 20));
        Assertions.assertThrows(ValidationException.class, () -> userService.createUser(user));
        User user2 = new User();
        user.setLogin("   ");
        Assertions.assertThrows(ValidationException.class, () -> userService.createUser(user2));

    }

    @Test
    public void validation_shouldValidateByBirthday() {
        User user = new User();
        user.setName("Имя");
        user.setEmail("example@mail.com");
        user.setBirthday(LocalDate.of(2025, 12, 20));
        user.setLogin("Login");
        Assertions.assertThrows(ValidationException.class, () -> userService.createUser(user));


    }

}
