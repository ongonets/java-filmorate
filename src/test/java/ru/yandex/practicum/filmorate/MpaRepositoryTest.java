package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRepository;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRowMapper;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({MpaRepository.class, MpaRowMapper.class})
public class MpaRepositoryTest {
    private final MpaRepository mpaRepository;

    @Test
    public void testFindAllMpa() {
        Collection<Mpa> mpas = mpaRepository.findAllMpa();
        assertThat(mpas.size() == 6);
        Mpa mpa = new Mpa(1, "G");
        assertThat(mpas.stream().findFirst())
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(mpa);

    }

    @Test
    public void testFindGenre() {
        Optional<Mpa> mpaOptional = mpaRepository.findMpa(3);
        Mpa mpa = new Mpa(3, "PG-13");
        assertThat(mpaOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(mpa);
    }
}
