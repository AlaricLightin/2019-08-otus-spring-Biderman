package ru.biderman.tetris.gameservices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.biderman.tetris.model.GameFieldState;
import ru.biderman.tetris.model.actions.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
class ActionServiceImpl implements ActionService{
    private final FigureService figureService;

    @Override
    public List<GameAction> getActions(GameFieldState state) {
        ArrayList<GameAction> result = new ArrayList<>();
        if (state.getFullRows().size() > 0)
            result.add(new DeleteRowAction(state.getFullRows()));
        if (state.isNeedFigure())
            result.add(new NewFigureAction(figureService.nextFigureType()));
        if (state.isGameOver()) {
            result.add(new EndGameAction());
            result.add(new StartStopAction(false));
        }
        return result;
    }
}
