package ru.biderman.tetris.gameservices;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.biderman.tetris.config.GameFieldProperties;
import ru.biderman.tetris.model.Field;
import ru.biderman.tetris.model.GameField;

@Configuration
public class GameConfig {
    @Bean
    public FieldUpdater fieldUpdater(GameFieldProperties gameFieldProperties) {
        return new FieldUpdaterImpl(new GameField(
                Field.createNewField(gameFieldProperties.getColumnCount(),
                        gameFieldProperties.getRowCount()), null));
    }
}
