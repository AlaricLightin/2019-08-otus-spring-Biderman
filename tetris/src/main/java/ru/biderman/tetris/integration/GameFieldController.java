package ru.biderman.tetris.integration;

import org.springframework.stereotype.Service;
import ru.biderman.tetris.gui.InterfaceCommands;
import ru.biderman.tetris.gui.InterfaceEvents;
import ru.biderman.tetris.gui.MainFrame;
import ru.biderman.tetris.model.Field;
import ru.biderman.tetris.model.GameField;

@Service
class GameFieldController {
    private final InterfaceEvents events;

    public GameFieldController(MainFrame mainFrame,
                               InterfaceCommands interfaceCommands) {
        mainFrame.setCommands(interfaceCommands);
        events = mainFrame.getEvents();
    }

    public void setNewState(GameField gameField) {
        Field field = gameField.getFieldWithFigure();
        events.onUpdateField(field);
    }

    public void setEndGame() {
        events.onEndGame();
    }
}
