package ru.biderman.tetris.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.biderman.tetris.model.ModelTestUtils.createSLeft;

@ExtendWith(SpringExtension.class)
@DisplayName("Фигура должна ")
class FigureTest {
    @DisplayName("двигаться")
    @ParameterizedTest
    @MethodSource("dataForMoveTest")
    void shouldMove(FigureOperation operation, Integer resultX, Integer resultY, FigurePoint[] result) {
        Figure figure = createSLeft(0, 0).doOperation(operation);
        assertThat(figure)
                .satisfies(f -> assertThat(f.getCenter())
                        .hasFieldOrPropertyWithValue("x", resultX)
                        .hasFieldOrPropertyWithValue("y", resultY)
                )
                .satisfies(f -> assertThat(f.getDeltas())
                        .isEqualTo(result));
    }

    private static Stream<Arguments> dataForMoveTest() {
        return Stream.of(
                Arguments.of(FigureOperation.LEFT,
                        -1, 0,
                        new FigurePoint[]{
                                new FigurePoint(-1, -1), new FigurePoint(0, -1),
                                new FigurePoint(0, 0), new FigurePoint(1, 0)
                        }),
                Arguments.of(FigureOperation.RIGHT,
                        1, 0,
                        new FigurePoint[]{
                                new FigurePoint(-1, -1), new FigurePoint(0, -1),
                                new FigurePoint(0, 0), new FigurePoint(1, 0)
                        }),
                Arguments.of(FigureOperation.DOWN,
                        0, 1,
                        new FigurePoint[]{
                                new FigurePoint(-1, -1), new FigurePoint(0, -1),
                                new FigurePoint(0, 0), new FigurePoint(1, 0)
                        }),
                Arguments.of(FigureOperation.ROTATE,
                        0, 0,
                        new FigurePoint[]{
                                new FigurePoint(1, -1), new FigurePoint(1, 0),
                                new FigurePoint(0, 0), new FigurePoint(0, 1)
                        })
        );
    }

    @DisplayName("возвращать свои точки")
    @Test
    void shouldGetPoints() {
        int centerX = 100;
        int centerY = 100;
        Figure figure = createSLeft(centerX, centerY);
        assertThat(figure.getPoints())
                .isEqualTo(new FigurePoint[]{
                        new FigurePoint(centerX - 1, centerY - 1),
                        new FigurePoint(centerX, centerY - 1),
                        new FigurePoint(centerX, centerY),
                        new FigurePoint(centerX + 1, centerY)
                });
    }
}