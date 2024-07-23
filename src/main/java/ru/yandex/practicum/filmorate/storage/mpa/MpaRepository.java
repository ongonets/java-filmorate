package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class MpaRepository extends BaseRepository<Mpa> {
    private static final String FIND_ALL_MPA_QUERY = "SELECT mpa_id, mpa_name FROM mpa";
    private static final String FIND_MPA_BY_ID_QUERY = "SELECT mpa_id, mpa_name FROM mpa WHERE mpa_id = ?";

    public MpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Mpa> findAllMpa() {
        return findMany(FIND_ALL_MPA_QUERY);
    }

    public Optional<Mpa> findMpa(long id) {
        return findOne(FIND_MPA_BY_ID_QUERY,id);
    }

}
