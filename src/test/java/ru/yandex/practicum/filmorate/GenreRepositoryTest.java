package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreRepository;
import ru.yandex.practicum.filmorate.storage.genre.GenreRowMapper;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({GenreRepository.class, GenreRowMapper.class})
public class GenreRepositoryTest {
    private final GenreRepository genreRepository;

    @Test
    public void testFindAllGenres() {
        Collection<Genre> genres = genreRepository.findAllGenre();
        assertThat(genres.size() == 6);
        Genre genre = new Genre(1, "Комедия");
        assertThat(genres.stream().findFirst())
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(genre);

    }

    @Test
    public void testFindGenre() {
        Optional<Genre> genreOptional = genreRepository.findGenre(3);
        Genre genre = new Genre(3, "Мультфильм");
        assertThat(genreOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(genre);
    }
}
