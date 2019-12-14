package ru.biderman.tetris.model;

import lombok.EqualsAndHashCode;

import java.util.Arrays;

@EqualsAndHashCode
public class Figure {
    public static final int ELEMENT_COUNT = 4;

    private final FigurePoint center;
    private final FigurePoint[] deltas; // смещение всех квадратиков фигуры относительно центра

    Figure(FigurePoint center, FigurePoint[] deltas) {
        this.center = center;
        this.deltas = deltas;
    }

    FigurePoint getCenter() {
        return center;
    }

    FigurePoint[] getDeltas() {
        return deltas;
    }

    FigurePoint[] getPoints() {
        return Arrays.stream(deltas)
                .map(p -> p.addTo(center))
                .toArray(FigurePoint[]::new);
    }

    private Figure move(FigurePoint moveDelta) {
        FigurePoint newCenter = center.addTo(moveDelta);
        return new Figure(newCenter, deltas);
    }

    private static final FigurePoint MOVE_LEFT_POINT = new FigurePoint(-1, 0);
    private static final FigurePoint MOVE_RIGHT_POINT = new FigurePoint(1, 0);
    private static final FigurePoint MOVE_DOWN_POINT = new FigurePoint(0, 1);

    private Figure rotate() {
        return new Figure(center,
                Arrays.stream(deltas)
                .map(p -> new FigurePoint(- p.getY(), p.getX()))
                .toArray(FigurePoint[]::new)
        );
    }

    public Figure doOperation(FigureOperation operation) {
        switch (operation) {
            case LEFT:
                return move(MOVE_LEFT_POINT);

            case RIGHT:
                return move(MOVE_RIGHT_POINT);

            case DOWN:
                return move(MOVE_DOWN_POINT);

            case ROTATE:
                return rotate();

            default:
                return this;
        }
    }
}
