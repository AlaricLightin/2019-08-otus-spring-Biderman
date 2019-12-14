package ru.biderman.tetris.model.actions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.biderman.tetris.model.FigureOperation;

@RequiredArgsConstructor
@Getter
public class FigureMoveAction extends GameAction {
    private final FigureOperation operation;
}
