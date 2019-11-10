package ru.biderman.librarywebbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.biderman.librarywebbackend.domain.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
