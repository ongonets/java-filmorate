package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {
    private final GenreRepository genreRepository;

    public Collection<Genre> findAllGenres() {
        log.info("Запрос на получение списка жанров");
        return genreRepository.findAllGenre();
    }

    public Genre findGenreById(long id) {
        Optional<Genre> genreOptional = genreRepository.findGenre(id);
        if (genreOptional.isEmpty()) {
            log.error("Жанр с ID {} не найден", id);
            throw new NotFoundException("Жанр с id = " + id + " не найден");
        }
        return genreOptional.get();
    }


}
