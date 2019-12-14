package ru.biderman.tetris.gameservices;

import org.springframework.stereotype.Service;
import ru.biderman.tetris.model.FigureType;

import java.util.Random;

@Service
class RandomFigureService implements FigureService {
    private final Random random = new Random();

    @Override
    public FigureType nextFigureType() {
        return FigureType.values()[random.nextInt(FigureType.values().length)];
    }
}
