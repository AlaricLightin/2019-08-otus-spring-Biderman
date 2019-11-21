package ru.biderman.librarywebclassic.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.biderman.librarywebclassic.domain.User;
import ru.biderman.librarywebclassic.repositories.UserRepository;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Сервис по работе с пользовательскими данными ")
class UserDetailServiceImplTest {
    private UserRepository userRepository;
    private UserDetailServiceImpl userDetailService;

    @BeforeEach
    void init() {
        userRepository = mock(UserRepository.class);
        userDetailService = new UserDetailServiceImpl(userRepository);
    }

    @DisplayName("должен получать данные пользователей")
    @ParameterizedTest
    @MethodSource("dataForUserDetails")
    void shouldGetUserDetails(boolean isAdmin, String[] roles) {
        String username = "user";
        User user = new User(1, username, "pass", isAdmin);
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        assertThat(userDetailService.loadUserByUsername(username))
                .satisfies(userDetails -> assertThat(userDetails.getUsername()).isEqualTo(username))
                .satisfies(userDetails -> assertThat(userDetails.getPassword()).isEqualTo(user.getPassword()))
                .satisfies(userDetails -> assertThat(userDetails.getAuthorities())
                        .extracting("role")
                        .containsOnly(roles));
    }

    private static Stream<Arguments> dataForUserDetails() {
        return Stream.of(
                Arguments.of(true, new String[]{"ROLE_ADMIN", "ROLE_USER"}),
                Arguments.of(false, new String[]{"ROLE_USER"})
        );
    }

    @DisplayName("должен возвращать исключение, если пользователя нет")
    @Test
    void shouldThrowExceptionIfUserAbsent() {
        String username = "user";
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userDetailService.loadUserByUsername(username));
    }
}