package ru.biderman.librarymigration.sqlrepositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.biderman.librarymigration.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
