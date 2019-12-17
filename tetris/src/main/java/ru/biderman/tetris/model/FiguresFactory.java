package ru.biderman.tetris.model;

import java.util.HashMap;

public class FiguresFactory {
    private static FigurePoint[] createFigurePointArray(
            int x1, int y1,
            int x2, int y2,
            int x3, int y3,
            int x4, int y4
    ) {
        return new FigurePoint[] {
                new FigurePoint(x1, y1),
                new FigurePoint(x2, y2),
                new FigurePoint(x3, y3),
                new FigurePoint(x4, y4),
        };
    }

    private static final HashMap<FigureType, FigurePoint[]> figurePoints = new HashMap<>();
    static {
        figurePoints.put(FigureType.SLeft,
                createFigurePointArray(-1, -1, 0, -1, 0, 0, 1, 0));
        figurePoints.put(FigureType.SRight,
                createFigurePointArray(-1, 0, 0, 0, 0, -1, 1, -1));
        figurePoints.put(FigureType.Square,
                createFigurePointArray(0, -1, 1, -1, 0, 0, 1, 0));
        figurePoints.put(FigureType.Line,
                createFigurePointArray(-1, 0, 0, 0, 1, 0, 2, 0));
        figurePoints.put(FigureType.LLeft,
                createFigurePointArray(-1, -1, -1, 0, 0, 0, 1, 0));
        figurePoints.put(FigureType.LRight,
                createFigurePointArray(-1, 0, 0, 0, 1, 0, 1, -1));
        figurePoints.put(FigureType.T,
                createFigurePointArray(-1, -1, 0, -1, 1, -1, 0, 0));
    }

    public static Figure createFigure(FigureType figureType, int centerX, int centerY) {
        FigurePoint[] points = figurePoints.get(figureType);
        assert points != null;
        return new Figure(new FigurePoint(centerX, centerY), points);
    }
}
