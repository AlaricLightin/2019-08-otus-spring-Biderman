package ru.biderman.librarywebclassic.info;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.biderman.librarywebclassic.services.DatabaseService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
@AutoConfigureMockMvc
@DisplayName("Компонент LibraryInfoContributor ")
class LibraryInfoContributorTest {
    @MockBean
    DatabaseService databaseService;

    @Autowired
    MockMvc mockMvc;

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
    }

    @DisplayName("должен возвращать статистику книг и авторов")
    @Test
    void shouldGetStatistics() throws Exception{
        final long authorCount = 11;
        final long bookCount = 21;

        when(databaseService.getAuthorCount()).thenReturn(authorCount);
        when(databaseService.getBookCount()).thenReturn(bookCount);

        mockMvc.perform(get("/actuator/info").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.libraryStat").isMap())
                .andExpect(jsonPath("$.libraryStat.bookCount").value(bookCount))
                .andExpect(jsonPath("$.libraryStat.authorCount").value(authorCount));
    }
}