package ru.biderman.librarywebclassic.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.acls.domain.EhCacheBasedAclCache;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.biderman.librarywebclassic.domain.Book;
import ru.biderman.librarywebclassic.repositories.BookRepository;

import javax.sql.DataSource;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
@DisplayName("Сервис прав на просмотр книг ")
class AvailabilityForMinorsServiceImplTest {
    @Autowired
    AvailabilityForMinorsServiceImpl availabilityForMinorsService;

    @Autowired
    BookRepository bookRepository;

    @TestConfiguration
    static class TestConfig {
        @Primary
        @Bean
        public EhCacheManagerFactoryBean aclCacheManager() {
            EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
            factoryBean.setShared(false);
            factoryBean.setAcceptExisting(true);
            return factoryBean;
        }

        @Primary
        @Bean
        public JdbcMutableAclService aclService(DataSource dataSource, LookupStrategy lookupStrategy, EhCacheBasedAclCache aclCache) {
            return new JdbcMutableAclService(dataSource, lookupStrategy, aclCache);
        }
    }

    @ParameterizedTest
    @MethodSource("dataForAvailabilityCheck")
    @WithMockUser(roles = {"USER", "ADULT"})
    @DisplayName("должен возвращать доступность для несовершеннолетних")
    void shouldCheckAvailability(long bookId, boolean result) {
        Book book = bookRepository.findById(bookId).orElse(null);
        assertThat(book).isNotNull();

        assertThat(availabilityForMinorsService.isAdultOnly(book)).isEqualTo(result);
    }

    private static Stream<Arguments> dataForAvailabilityCheck() {
        return Stream.of(
                Arguments.of(1, false),
                Arguments.of(2, true)
        );
    }

    @ParameterizedTest
    @MethodSource("dataForChangeRights")
    @WithMockUser(roles = {"ADMIN", "USER", "ADULT"})
    @Transactional
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен менять права")
    void shouldChangeRights(long bookId, boolean newRights) {
        Book book = bookRepository.findById(bookId).orElse(null);
        assertThat(book).isNotNull();
        availabilityForMinorsService.setRights(book, newRights);

        assertThat(availabilityForMinorsService.isAdultOnly(book)).isEqualTo(newRights);
    }

    private static Stream<Arguments> dataForChangeRights() {
        return Stream.of(
                Arguments.of(1, true),
                Arguments.of(1, false),
                Arguments.of(2, false),
                Arguments.of(2, true)
        );
    }

}