package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Film createFilm(Film film) {
        validateFilm(film);
        film = filmStorage.createFilm(film);
        log.info("Добавлен фильм {}", film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (film.getId() == 0) {
            throw new ValidationException("Id должен быть указан");
        }
        validateFilm(film);
        checkFilmId(film.getId());
        Film oldFilm = filmStorage.findFilm(film.getId()).get();
        oldFilm = oldFilm.toBuilder()
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .build();
        filmStorage.updateFilm(oldFilm);
        log.info("Обновлен фильм {}", film);
        return oldFilm;
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Некорректно введено название фильма {}", film);
            throw new ValidationException("Название фильма некорректно.");
        }
        if (film.getDescription() == null || film.getDescription().isBlank() ||
                film.getDescription().toCharArray().length > 200) {
            log.warn("Некорректно введено описание фильма {}", film);
            throw new ValidationException("Описание фильма слишком длинное.");
        }
        if (film.getReleaseDate() == null ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Некорректно введена дата релиза фильма {}", film);
            throw new ValidationException("Дата релиза фильма некорректна.");
        }
        if (film.getDuration() == null || film.getDuration().isNegative()) {
            log.warn("Некорректно введена продолжительность фильма {}", film);
            throw new ValidationException("Продолжительность фильма некорректна.");
        }
    }

    private void checkFilmId(long id) {
        if (filmStorage.findFilm(id).isEmpty()) {
            log.warn("Фильм с ID {} не найден", id);
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
    }

    private void checkUserId(long id) {
        if (userStorage.findUser(id).isEmpty()) {
            log.warn("Ползователь с ID {} не найден", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }


    public void addLike(long id, long userId) {
        checkFilmId(id);
        checkUserId(userId);
        filmStorage.addLike(id, userId);
        log.info("Пользователь {} поставил лайк фильму {}", userId, id);
    }

    public void deleteLike(long id, long userId) {
        checkFilmId(id);
        checkUserId(userId);
        filmStorage.deleteLike(id, userId);
        log.info("Пользователь {} удалил лайк у фильма {}", userId, id);
    }

    public Collection<Film> findPopularFilms(long count) {
        return filmStorage.findPopularFilms(count);
    }
}
