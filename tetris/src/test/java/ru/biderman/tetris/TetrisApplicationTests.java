package ru.biderman.tetris;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import ru.biderman.tetris.gui.MainFrame;

@SpringBootTest
@DisplayName("Приложение ")
class TetrisApplicationTests {
    @MockBean
    MainFrame mainFrame;

    @Test
    @DisplayName("должно загружать контекст")
    void contextLoads() {
    }

}
