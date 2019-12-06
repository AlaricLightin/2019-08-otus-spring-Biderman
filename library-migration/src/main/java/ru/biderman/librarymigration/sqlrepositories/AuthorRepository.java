package ru.biderman.librarymigration.sqlrepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.biderman.librarymigration.domain.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
