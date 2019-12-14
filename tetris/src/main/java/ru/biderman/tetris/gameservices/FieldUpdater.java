package ru.biderman.tetris.gameservices;

import ru.biderman.tetris.model.GameField;
import ru.biderman.tetris.model.actions.GameAction;

public interface FieldUpdater {
    GameField getNew(GameAction action);
}
