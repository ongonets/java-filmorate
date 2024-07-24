package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmExtractor;
import ru.yandex.practicum.filmorate.storage.film.FilmRepository;
import ru.yandex.practicum.filmorate.storage.film.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmRepository.class, FilmRowMapper.class, FilmExtractor.class})
public class FilmRepositoryTest {
    @Qualifier("filmRepository")
    private final FilmStorage filmRepository;


    static Film getFilm() {
        Film film = new Film();
        film.setId(1);
        film.setName("testFilm1");
        film.setDescription("testDescription1");
        film.setReleaseDate(LocalDate.of(2020, 11, 23));
        film.setDuration(Duration.ofMinutes(150));
        film.setMpa(new Mpa(1, "G"));
        ArrayList<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, "Комедия"));
        film.setGenres(genres);
        return film;
    }

    @Test
    public void TestFindAllFilms() {
        Collection<Film> films = filmRepository.findAllFilms();
        assertThat(films.size() == 2);

        assertThat(films.stream().findFirst())
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(getFilm());
    }

    @Test
    public void TestCreateFilm() {
        Collection<Film> films = filmRepository.findAllFilms();
        assertThat(films.size() == 2);
        assertThat(films.stream().filter(film -> film.getId() == 3).findFirst())
                .isEmpty();

        Film film = getFilm();
        film.setId(3);
        filmRepository.createFilm(film);

        films = filmRepository.findAllFilms();
        assertThat(films.size() == 3);
        assertThat(films.stream().filter(film1 -> film1.getId() == 3).findFirst())
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(film);
    }

    @Test
    public void TestFindFilm() {
        Optional<Film> filmOptional = filmRepository.findFilm(1);
        assertThat(filmOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(getFilm());
    }

    @Test
    public void TestUpdateFilm() {
        Film film = getFilm();
        film.setId(2);
        Optional<Film> filmOptional = filmRepository.findFilm(2);
        assertThat(filmOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isNotEqualTo(film);

        filmRepository.updateFilm(film);

        filmOptional = filmRepository.findFilm(2);
        assertThat(filmOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(film);
    }

    @Test
    public void TestAddLike() {
        Collection<Film> films = filmRepository.findPopularFilms(1);
        assertThat(films.stream().filter(film1 -> film1.getId() == 1).findFirst())
                .isEmpty();

        filmRepository.addLike(1, 2);

        films = filmRepository.findPopularFilms(1);
        assertThat(films.size() == 2);
        assertThat(films.stream().filter(film1 -> film1.getId() == 1).findFirst())
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(getFilm());
    }

    @Test
    public void TestDeleteLike() {
        Collection<Film> films = filmRepository.findPopularFilms(1);
        assertThat(films.stream().filter(film1 -> film1.getId() == 1).findFirst())
                .isEmpty();

        filmRepository.deleteLike(2, 1);

        films = filmRepository.findPopularFilms(1);
        assertThat(films.size() == 2);
        assertThat(films.stream().filter(film1 -> film1.getId() == 1).findFirst())
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(getFilm());
    }

    @Test
    public void TestFindPopularFilms() {
        Collection<Film> films = filmRepository.findPopularFilms(1);
        assertThat(films.stream().filter(film1 -> film1.getId() == 1).findFirst())
                .isEmpty();

        films = filmRepository.findPopularFilms(5);
        assertThat(films.size() == 2);
        assertThat(films.stream().filter(film1 -> film1.getId() == 1).findFirst())
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(getFilm());
    }
}
