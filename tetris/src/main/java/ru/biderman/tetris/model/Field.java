package ru.biderman.tetris.model;

import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@EqualsAndHashCode
public class Field {
    private final int columnCount;
    private final int rowCount;
    private final boolean[][] fieldCells;

    Field(int columnCount, int rowCount, boolean[][] fieldCells) {
        this.columnCount = columnCount;
        this.rowCount = rowCount;
        this.fieldCells = fieldCells;
    }

    private boolean[][] cloneCells() {
        return Arrays.stream(fieldCells).map(boolean[]::clone).toArray(boolean[][]::new);
    }

    public static Field createNewField(int columnCount, int rowCount) {
        return new Field(columnCount, rowCount, new boolean[rowCount][columnCount]);
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public boolean isCellOccupied(int column, int row) {
        return fieldCells[row][column];
    }

    public Field addFigure(Figure figure) {
        if (figure == null)
            return this;

        boolean[][] newFieldCells = cloneCells();
        FigurePoint[] points = figure.getPoints();
        for(int i = 0; i < Figure.ELEMENT_COUNT; i++) {
            FigurePoint point = points[i];
            if(point.getX() >= 0 && point.getX() < columnCount && point.getY() >= 0 && point.getY() < rowCount)
                newFieldCells[point.getY()][point.getX()] = true;
        }

        return new Field(getColumnCount(), getRowCount(), newFieldCells);
    }

    public boolean canSet(Figure figure) {
        FigurePoint[] points = figure.getPoints();
        return Arrays.stream(points)
                .allMatch(p ->
                        p.getX() >= 0 && p.getX() < columnCount
                        && p.getY() < rowCount
                        && (p.getY() < 0 || !fieldCells[p.getY()][p.getX()])
                );
    }

    public boolean containsAnyInTopRow() {
        boolean[] topRow = fieldCells[0];
        return IntStream.range(0, topRow.length)
                .anyMatch(idx -> topRow[idx]);
    }

    public List<Integer> getFullRows() {
        return IntStream.range(0, rowCount)
                .filter(row ->
                        IntStream.range(0, columnCount)
                                .allMatch(column -> fieldCells[row][column]))
                .boxed()
                .collect(Collectors.toList());
    }

    public Field deleteRows(List<Integer> rows) {
        if (rows.size() == 0)
            return this;

        boolean[][] newFieldCells = cloneCells();
        // считаем, что ряды в списке всегда упорядочены
        for (int row: rows) {
            //noinspection ManualArrayCopy
            for (int i = row; i > 0; i--) {
                newFieldCells[i] = newFieldCells[i - 1];
            }
            newFieldCells[0] = new boolean[columnCount];
        }
        return new Field(columnCount, rowCount, newFieldCells);
    }
}
