package ru.biderman.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.biderman.library.domain.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
