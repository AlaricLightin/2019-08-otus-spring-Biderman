package ru.biderman.tetris.model.actions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class DeleteRowAction extends GameAction {
    private final List<Integer> rows;
}
