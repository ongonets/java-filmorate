package ru.yandex.practicum.filmorate.storage.film;


import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Film createFilm(Film film);

    Collection<Film> findAllFilms();

    Optional<Film> findFilm(long id);

    Film updateFilm(Film oldFilm);

    void addLike(long id, long userId);

    void deleteLike(long id, long userId);

    Collection<Film> findPopularFilms(long count);
}
