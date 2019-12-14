package ru.biderman.tetris.gameservices;

import ru.biderman.tetris.model.GameFieldState;
import ru.biderman.tetris.model.actions.GameAction;

import java.util.List;

public interface ActionService {
    List<GameAction> getActions(GameFieldState state);
}
