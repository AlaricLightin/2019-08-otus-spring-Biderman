package ru.biderman.librarywebclassic.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import ru.biderman.librarywebclassic.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Book> findAll();

    @PostAuthorize("hasPermission(returnObject.orElse(null), 'READ')")
    Optional<Book> findById(Long id);
}
