package ru.biderman.tetris.model;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class GameFieldState {
    private boolean needFigure;
    private boolean gameOver;
    private List<Integer> fullRows = Collections.emptyList();
}
