package ru.biderman.tetris.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.biderman.tetris.model.FigureOperation;
import ru.biderman.tetris.model.actions.FigureMoveAction;

@Component
@RequiredArgsConstructor
class GameTimer {
    private final InputActionEndPoint inputActionEndPoint;
    private boolean isRunning = false;

    @Scheduled(fixedRate = 250)
    public void sendDownMessage() {
        if (isRunning)
            inputActionEndPoint.sendAction(new FigureMoveAction(FigureOperation.DOWN));
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
