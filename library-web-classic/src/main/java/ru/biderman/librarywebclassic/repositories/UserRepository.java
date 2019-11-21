package ru.biderman.librarywebclassic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.biderman.librarywebclassic.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
