package ru.biderman.tetris.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
class FigurePoint {
    private final int x;
    private final int y;

    FigurePoint addTo(FigurePoint point) {
        return new FigurePoint(point.getX() + x, point.getY() + y);
    }
}
