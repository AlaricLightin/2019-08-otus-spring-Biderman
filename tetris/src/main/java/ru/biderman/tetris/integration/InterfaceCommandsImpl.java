package ru.biderman.tetris.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.biderman.tetris.gui.InterfaceCommands;
import ru.biderman.tetris.model.FigureOperation;
import ru.biderman.tetris.model.actions.FigureMoveAction;
import ru.biderman.tetris.model.actions.NewGameAction;
import ru.biderman.tetris.model.actions.StartStopAction;

@Component
@RequiredArgsConstructor
class InterfaceCommandsImpl implements InterfaceCommands {
    private final InputActionEndPoint actionEndPoint;

    private void sendFigureAction(FigureOperation figureOperation) {
        actionEndPoint.sendAction(new FigureMoveAction(figureOperation));
    }

    private void sendStartStopAction(boolean isStart) {
        actionEndPoint.sendAction(new StartStopAction(isStart));
    }

    @Override
    public void left() {
        sendFigureAction(FigureOperation.LEFT);
    }

    @Override
    public void right() {
        sendFigureAction(FigureOperation.RIGHT);
    }

    @Override
    public void rotate() {
        sendFigureAction(FigureOperation.ROTATE);
    }

    @Override
    public void down() {
        sendFigureAction(FigureOperation.DOWN);
    }

    @Override
    public void start() {
        sendStartStopAction(true);
    }

    @Override
    public void pause() {
        sendStartStopAction(false);
    }

    @Override
    public void newGame() {
        actionEndPoint.sendAction(new NewGameAction());
        sendStartStopAction(true);
    }
}
