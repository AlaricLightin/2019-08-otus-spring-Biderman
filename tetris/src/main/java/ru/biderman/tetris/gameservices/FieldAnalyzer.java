package ru.biderman.tetris.gameservices;

import ru.biderman.tetris.model.GameField;
import ru.biderman.tetris.model.GameFieldState;

public interface FieldAnalyzer {
    GameFieldState analyze(GameField gameField);
}
