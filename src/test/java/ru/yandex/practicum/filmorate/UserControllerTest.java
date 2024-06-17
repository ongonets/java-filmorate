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
        User user = User.builder()
                .name("Имя")
                .birthday(LocalDate.of(1990, 12, 20))
                .login("Логин")
                .build();
        Assertions.assertThrows(ValidationException.class, () -> userService.createUser(user));
        User user2 = user.toBuilder()
                .email("   ")
                .build();
        Assertions.assertThrows(ValidationException.class, () -> userService.createUser(user2));
        User user3 = user.toBuilder()
                .email("examplemail.com")
                .build();
        Assertions.assertThrows(ValidationException.class, () -> userService.createUser(user3));
    }

    @Test
    public void validation_shouldValidateByLogin() {
        User user = User.builder()
                .name("Имя")
                .email("example@mail.com")
                .birthday(LocalDate.of(1990, 12, 20))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> userService.createUser(user));
        User user2 = user.toBuilder()
                .login("   ")
                .build();
        Assertions.assertThrows(ValidationException.class, () -> userService.createUser(user2));

    }

    @Test
    public void validation_shouldValidateByBirthday() {
        User user = User.builder()
                .name("Имя")
                .email("example@mail.com")
                .birthday(LocalDate.of(2025, 12, 20))
                .login("Login")
                .build();
        Assertions.assertThrows(ValidationException.class, () -> userService.createUser(user));


    }

}
