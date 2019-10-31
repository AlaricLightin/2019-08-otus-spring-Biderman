package ru.biderman.librarywebclassic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.biderman.librarywebclassic.domain.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
