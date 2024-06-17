package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.Duration;
import java.time.LocalDate;

public class FilmControllerTest {
    FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());

    @Test
    public void validateFilm_shouldValidateByName() {
        Film film = Film.builder()
                .description("Описание")
                .releaseDate(LocalDate.of(1990, 12, 20))
                .duration(Duration.ofMinutes(100))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> filmService.createFilm(film));
    }

    @Test
    public void validateFilm_shouldValidateByReleaseDate() {
        Film film = Film.builder()
                .name("Название")
                .description("Описание")
                .releaseDate(LocalDate.of(1790, 12, 20))
                .duration(Duration.ofMinutes(100))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> filmService.createFilm(film));
    }

    @Test
    public void validateFilm_shouldValidateByDuration() {
        Film film = Film.builder()
                .name("Название")
                .description("Описание")
                .releaseDate(LocalDate.of(1990, 12, 20))
                .duration(Duration.ofMinutes(-100))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> filmService.createFilm(film));
    }

}
