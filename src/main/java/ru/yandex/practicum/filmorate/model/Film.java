package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.jackson.Jacksonized;
import ru.yandex.practicum.filmorate.typeadapter.DurationDeserializer;
import ru.yandex.practicum.filmorate.typeadapter.DurationSerializer;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

/**
 * Film.
 */
@Data
@Jacksonized
@EqualsAndHashCode(of = {"id"})
public class Film {
    long id;

    String name;

    String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate releaseDate;

    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    Duration duration;

    List<Long> likes;

    Mpa mpa;

    List<Genre> genres;
}
