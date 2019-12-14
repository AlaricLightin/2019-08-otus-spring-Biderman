package ru.biderman.tetris.gameservices;

import ru.biderman.tetris.model.Figure;
import ru.biderman.tetris.model.FigureOperation;
import ru.biderman.tetris.model.FiguresFactory;
import ru.biderman.tetris.model.GameField;
import ru.biderman.tetris.model.actions.*;

class FieldUpdaterImpl implements FieldUpdater{
    private final GameField startGameField;
    private GameField gameField;

    public FieldUpdaterImpl(GameField gameField) {
        this.startGameField = gameField;
        this.gameField = this.startGameField;
    }

    @Override
    public GameField getNew(GameAction action) {
        if (action instanceof FigureMoveAction) {
            afterFigureMoveAction((FigureMoveAction) action);
        }
        else if (action instanceof NewFigureAction) {
            int centerX = (gameField.getField().getColumnCount() + 1) / 2;
            gameField = new GameField(gameField.getField(), FiguresFactory.createFigure(
                    ((NewFigureAction) action).getFigureType(),
                    centerX, 0));
        }
        else if (action instanceof DeleteRowAction) {
            gameField = new GameField(gameField.getField().deleteRows(((DeleteRowAction) action).getRows()),
                    gameField.getFigure());
        }
        else if (action instanceof NewGameAction) {
            gameField = startGameField;
        }

        return gameField;
    }

    private void afterFigureMoveAction(FigureMoveAction action) {
        if (gameField.getFigure() != null) {
            FigureOperation operation = action.getOperation();
            Figure newFigure = gameField.getFigure().doOperation(operation);
            if (gameField.getField().canSet(newFigure))
                gameField = new GameField(gameField.getField(), newFigure);
            else if (operation == FigureOperation.DOWN) {
                gameField = new GameField(gameField.getFieldWithFigure(), null);
            }
        }
    }
}
