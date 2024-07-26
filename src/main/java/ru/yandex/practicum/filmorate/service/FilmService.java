package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreRepository;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRepository;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;

    public FilmService(@Qualifier("filmRepository") FilmStorage filmStorage,
                       @Qualifier("userRepository") UserStorage userStorage,
                       MpaRepository mpaRepository,
                       GenreRepository genreRepository) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaRepository = mpaRepository;
        this.genreRepository = genreRepository;
    }

    public Collection<Film> findAllFilms() {
        log.info("Запрос на получение списка  фильмов");
        return filmStorage.findAllFilms();
    }

    public Film createFilm(Film film) {
        log.info("Запрос на добавление фильма {}", film);
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
        log.info("Запрос на обновление фильма {}", film);
        filmStorage.updateFilm(film);
        log.info("Обновлен фильм {}", film);
        return film;
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Некорректно введено название фильма {}", film);
            throw new ValidationException("Название фильма некорректно.");
        }
        if (film.getDescription() == null || film.getDescription().isBlank() ||
                film.getDescription().toCharArray().length > 200) {
            log.error("Некорректно введено описание фильма {}", film);
            throw new ValidationException("Описание фильма слишком длинное.");
        }
        if (film.getReleaseDate() == null ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Некорректно введена дата релиза фильма {}", film);
            throw new ValidationException("Дата релиза фильма некорректна.");
        }
        if (film.getDuration() == null || film.getDuration().isNegative()) {
            log.error("Некорректно введена продолжительность фильма {}", film);
            throw new ValidationException("Продолжительность фильма некорректна.");
        }
        if (isMpaRatingNotNullAndExist(film)) {
            log.error("Некорректно введен рейтинг фильма {}", film);
            throw new ValidationException("Рейтинг фильма некорректен.");
        }
        if (film.getGenres() != null) {
            Set<Long> genresId = genreRepository.findAllGenre().stream().map(Genre::getId).collect(Collectors.toSet());
            Set<Long> filmsGenresId = film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
            if (!genresId.containsAll(filmsGenresId)) {
                log.error("Некорректно введен жанр фильма {}", film);
                throw new ValidationException("Жанр фильма некорректен.");
            }
        }

    }

    private void checkFilmId(long id) {
        if (filmStorage.findFilm(id).isEmpty()) {
            log.error("Фильм с ID {} не найден", id);
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
    }

    private void checkUserId(long id) {
        if (userStorage.findUser(id).isEmpty()) {
            log.error("Ползователь с ID {} не найден", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }


    public void addLike(long id, long userId) {
        checkFilmId(id);
        checkUserId(userId);
        log.info("Запрос на добавление лайка к фильму {} ползователем {}", id, userId);
        filmStorage.addLike(id, userId);
        log.info("Пользователь {} поставил лайк фильму {}", userId, id);
    }

    public void deleteLike(long id, long userId) {
        checkFilmId(id);
        checkUserId(userId);
        log.info("Запрос на удаление лайка у фильму {} ползователем {}", id, userId);
        filmStorage.deleteLike(id, userId);
        log.info("Пользователь {} удалил лайк у фильма {}", userId, id);
    }

    public Collection<Film> findPopularFilms(long count) {
        log.info("Запрос на получение списка популярных фильмов");
        return filmStorage.findPopularFilms(count);
    }

    public Film findFilm(long id) {
        Optional<Film> filmOptional = filmStorage.findFilm(id);
        if (filmOptional.isEmpty()) {
            log.error("Фильм с ID {} не найден", id);
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
        return filmOptional.get();
    }

    private boolean isMpaRatingNotNullAndExist(Film film) {
        Set<Long> mpaId = mpaRepository.findAllMpa().stream().map(Mpa::getId).collect(Collectors.toSet());
        return (film.getMpa() == null || !mpaId.contains(film.getMpa().getId()));
    }
}
