package ru.biderman.librarywebclassic.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.biderman.librarywebclassic.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
