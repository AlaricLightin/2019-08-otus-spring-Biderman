package ru.biderman.tetris.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class GameField {
    private final Field field;
    private final Figure figure;

    public Field getFieldWithFigure() {
        return field.addFigure(figure);
    }
}
