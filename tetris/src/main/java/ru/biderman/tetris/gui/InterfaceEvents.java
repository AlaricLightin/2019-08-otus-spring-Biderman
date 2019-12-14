package ru.biderman.tetris.gui;

import ru.biderman.tetris.model.Field;

public interface InterfaceEvents {
    void onUpdateField(Field field);
    void onEndGame();
}
