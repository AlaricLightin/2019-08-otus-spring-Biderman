package ru.biderman.library.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.biderman.library.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom {
}
