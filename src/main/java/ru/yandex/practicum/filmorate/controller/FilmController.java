package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film newFilm) {
        if (newFilm.getId() == 0) {
            throw new ValidationException("Id должен быть указан");
        }
        validateFilm(newFilm);
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            oldFilm = oldFilm.toBuilder()
                    .name(newFilm.getName())
                    .description(newFilm.getDescription())
                    .releaseDate(newFilm.getReleaseDate())
                    .duration(newFilm.getDuration())
                    .build();
            log.info("Обновлен фильм {}", newFilm);

            return oldFilm;
        }
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    private Film validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Некорректно введено название фильма {}", film);
            throw new ValidationException("Название фильма некорректно.");
        }
        if (film.getDescription().toCharArray().length > 200) {
            log.warn("Некорректно введено описание фильма {}", film);
            throw new ValidationException("Описание фильма слишком длинное.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Некорректно введена дата релиза фильма {}", film);
            throw new ValidationException("Дата релиза фильма некорректна.");
        }
        if (film.getDuration().isNegative()) {
            log.warn("Некорректно введена продолжительность фильма {}", film);
            throw new ValidationException("Продолжительность фильма некорректна.");
        }
        return film;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
