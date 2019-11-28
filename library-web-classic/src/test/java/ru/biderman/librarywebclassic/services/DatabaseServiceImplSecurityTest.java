package ru.biderman.librarywebclassic.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Сервис базы данных при тестировании безопасности")
class DatabaseServiceImplSecurityTest {
    @Autowired
    DatabaseService databaseService;

    @DisplayName("должен возвращать правильное число книг для совершеннолетнего пользователя")
    @WithMockUser(roles = {"USER", "ADULT"})
    @Test
    void shouldGetCorrectBookNumber() {
        int ADULT_BOOK_COUNT = 2;
        assertThat(databaseService.getAllBooks()).hasSize(ADULT_BOOK_COUNT);
    }

    @DisplayName("должен возвращать правильное количество книг для несовершеннолетнего")
    @WithMockUser
    @Test
    void shouldGetCorrectBookNumberForMinor() {
        int MINOR_BOOK_COUNT = 1;
        assertThat(databaseService.getAllBooks()).hasSize(MINOR_BOOK_COUNT);
    }
}