package ru.biderman.librarywebbackend.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.biderman.librarywebbackend.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
