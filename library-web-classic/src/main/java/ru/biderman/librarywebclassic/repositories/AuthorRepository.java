package ru.biderman.librarywebclassic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.biderman.librarywebclassic.domain.Author;

import java.util.List;

@SuppressWarnings("SqlResolve")
public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query(nativeQuery = true, value = "SELECT DISTINCT author_id FROM book_authors")
    List<Long> getUsedAuthorIdList();
}
