package ru.biderman.tetris.model;

public class ModelTestUtils {
    public static Field createField(int columnCount, int rowCount, boolean[][] cells) {
        return new Field(columnCount, rowCount, cells);
    }

    public static Figure createSquare(int centerX, int centerY) {
        return FiguresFactory.createFigure(FigureType.Square, centerX, centerY);
    }

    public static Figure createSLeft(int centerX, int centerY) {
        return FiguresFactory.createFigure(FigureType.SLeft, centerX, centerY);
    }
}
