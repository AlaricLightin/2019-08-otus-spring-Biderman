package ru.biderman.tetris.model.actions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class StartStopAction extends GameAction {
    private final boolean isStart;
}
