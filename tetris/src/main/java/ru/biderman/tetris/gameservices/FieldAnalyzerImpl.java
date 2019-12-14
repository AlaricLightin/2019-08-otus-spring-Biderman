package ru.biderman.tetris.gameservices;

import org.springframework.stereotype.Service;
import ru.biderman.tetris.model.GameField;
import ru.biderman.tetris.model.GameFieldState;

import java.util.List;

@Service
class FieldAnalyzerImpl implements FieldAnalyzer{
    @Override
    public GameFieldState analyze(GameField gameField) {
        GameFieldState state = new GameFieldState();
        state.setGameOver(checkGameOver(gameField));
        if (!state.isGameOver()) {
            state.setNeedFigure(gameField.getFigure() == null);
            if (state.isNeedFigure())
                state.setFullRows(getFullRows(gameField));
        }
        return state;
    }

    private List<Integer> getFullRows(GameField gameField) {
        return gameField.getField().getFullRows();
    }

    private boolean checkGameOver(GameField gameField) {
        return gameField.getField().containsAnyInTopRow();
    }
}
