package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Data
//@Builder(toBuilder = true)
@NoArgsConstructor
public class User {
    long id;
    String email;
    String login;
    String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;
    Set<Long> friends;
}
