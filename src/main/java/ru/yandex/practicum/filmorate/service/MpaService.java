package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {
    private final MpaRepository mpaRepository;

    public Collection<Mpa>  findAllMpa() {
        return mpaRepository.findAllMpa();
    }

    public Mpa findMpaById(long id) {
        if (mpaRepository.findMpa(id).isEmpty()) {
            log.warn("Рейтинг с ID {} не найден", id);
            throw new NotFoundException("Рейтинг с id = " + id + " не найден");
        }
        return mpaRepository.findMpa(id).get();
    }
}
