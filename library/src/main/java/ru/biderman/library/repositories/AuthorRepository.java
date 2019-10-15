package ru.biderman.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.biderman.library.domain.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
