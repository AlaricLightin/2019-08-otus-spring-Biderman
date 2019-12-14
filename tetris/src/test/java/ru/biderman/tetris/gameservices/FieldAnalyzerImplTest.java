package ru.biderman.tetris.gameservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.biderman.tetris.model.Field;
import ru.biderman.tetris.model.GameField;
import ru.biderman.tetris.model.GameFieldState;
import ru.biderman.tetris.model.ModelTestUtils;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.biderman.tetris.model.ModelTestUtils.createSquare;

@ExtendWith(SpringExtension.class)
@DisplayName("Анализатор поля ")
class FieldAnalyzerImplTest {
    @DisplayName("должен возвращать его состояние")
    @ParameterizedTest
    @MethodSource("data")
    void shouldGetState(GameField gameField,
                        boolean isGameOver,
                        boolean isNeedFigure,
                        Integer[] rowsForDelete) {
        FieldAnalyzerImpl analyzer = new FieldAnalyzerImpl();
        GameFieldState gameFieldState = analyzer.analyze(gameField);
        assertThat(gameFieldState)
                .hasFieldOrPropertyWithValue("gameOver", isGameOver)
                .hasFieldOrPropertyWithValue("needFigure", isNeedFigure)
                .satisfies(state -> assertThat(state.getFullRows()).containsExactly(rowsForDelete));
    }

    private static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of(
                        new GameField(
                                Field.createNewField(2, 2),
                                createSquare(0, 1)
                        ),
                        false, false, new Integer[]{}
                ),

                Arguments.of(
                        new GameField(
                                Field.createNewField(2, 2),
                                null
                        ),

                        false, true, new Integer[]{}
                ),

                Arguments.of(
                        new GameField(
                                ModelTestUtils.createField(2, 3,
                                        new boolean[][]{{false, false}, {true, true}, {true, true}}),
                                null
                        ),

                        false, true, new Integer[]{1, 2}
                ),

                Arguments.of(
                        new GameField(
                                ModelTestUtils.createField(2, 2,
                                        new boolean[][]{{true, true}, {true, true}}),
                                null
                        ),
                        true, false, new Integer[]{}
                )
        );
    }
}