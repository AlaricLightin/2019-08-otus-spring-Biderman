package ru.biderman.tetris.model.actions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.biderman.tetris.model.FigureType;

@RequiredArgsConstructor
@Getter
public class NewFigureAction extends GameAction {
    private final FigureType figureType;
}
