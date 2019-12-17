package ru.biderman.tetris.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("game-field")
@Data
public class GameFieldProperties {
    private int columnCount;
    private int rowCount;
}
