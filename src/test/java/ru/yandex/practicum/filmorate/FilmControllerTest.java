package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreRepository;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRepository;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.Duration;
import java.time.LocalDate;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmControllerTest {
    private final FilmService filmService;

    @Test
    public void validateFilm_shouldValidateByName() {
        Film film = new Film();
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(1990, 12, 20));
        film.setDuration(Duration.ofMinutes(100));
        Assertions.assertThrows(ValidationException.class, () -> filmService.createFilm(film));
    }

    @Test
    public void validateFilm_shouldValidateByReleaseDate() {
        Film film = new Film();
        film.setName("Название");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(1790, 12, 20));
        film.setDuration(Duration.ofMinutes(100));
        Assertions.assertThrows(ValidationException.class, () -> filmService.createFilm(film));
    }

    @Test
    public void validateFilm_shouldValidateByDuration() {
        Film film = new Film();
        film.setName("Название");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(1790, 12, 20));
        film.setDuration(Duration.ofMinutes(-100));
        Assertions.assertThrows(ValidationException.class, () -> filmService.createFilm(film));
    }

}
