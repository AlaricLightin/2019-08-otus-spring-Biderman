package ru.biderman.librarywebbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.biderman.librarywebbackend.domain.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
