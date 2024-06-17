package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @Override
    public Optional<Film> findFilm(long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Film updateFilm(Film film) {
        return films.put(film.getId(), film);
    }

    @Override
    public void addLike(long id, long userId) {
        Film film = films.get(id);
        List<Long> likeList;
        if (film.getLikes() == null) {
            likeList = new ArrayList<>();
        } else {
            likeList = film.getLikes();
        }
        likeList.add(userId);
        film.setLikes(likeList);
    }

    @Override
    public void deleteLike(long id, long userId) {
        Film film = films.get(id);
        List<Long> likeList;
        if (film.getLikes() == null) {
            return;
        } else {
            likeList = film.getLikes();
        }
        likeList.remove(userId);
        film.setLikes(likeList);
    }

    @Override
    public Collection<Film> findPopularFilms(long count) {
        return films.values().stream()
                .sorted(new Comparator<Film>() {
                    @Override
                    public int compare(Film o1, Film o2) {
                        if (o1.getLikes() == null) {
                            return o2.getLikes() == null ? 0 : 1;
                        } else if (o2.getLikes() == null) {
                            return -1;
                        }
                        return o2.getLikes().size() - o1.getLikes().size();
                    }
                })
                .limit(count)
                .collect(Collectors.toList());
    }
}
