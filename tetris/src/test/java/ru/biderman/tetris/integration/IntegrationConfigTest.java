package ru.biderman.tetris.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.biderman.tetris.gameservices.FieldUpdater;
import ru.biderman.tetris.gui.InterfaceCommands;
import ru.biderman.tetris.gui.MainFrame;
import ru.biderman.tetris.model.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class IntegrationConfigTest {
    @MockBean
    MainFrame mainFrame;

    @MockBean
    GameFieldController gameFieldController;
    @MockBean
    GameTimer gameTimer;
    @MockBean
    FieldUpdater fieldUpdater;

    @Autowired
    InterfaceCommands interfaceCommands;

    @DisplayName("Должна отрабатывать команда start")
    @Test
    void shouldDoStart() {
        interfaceCommands.start();
        verify(gameTimer).setRunning(true);
    }

    @DisplayName("Должно передаваться состояние поля")
    @Test
    void shouldReceiveUpdatedField() {
        GameField gameField = new GameField(Field.createNewField(2, 3),
                FiguresFactory.createFigure(FigureType.Square, 0, 1));
        when(fieldUpdater.getNew(any())).thenReturn(gameField);
        interfaceCommands.down();
        verify(gameFieldController).setNewState(gameField);
    }

    @DisplayName("Должен приходить сигнал конец игры")
    @Test
    void shouldReceiveEndGame() {
        GameField gameField = new GameField(ModelTestUtils.createField(2, 2,
                new boolean[][] {{true, true}, {true, true}}), null);
        when(fieldUpdater.getNew(any())).thenReturn(gameField);
        interfaceCommands.down();
        verify(gameFieldController).setEndGame();
    }
}