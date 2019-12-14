package ru.biderman.tetris.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.biderman.tetris.model.ModelTestUtils.createSLeft;

@ExtendWith(SpringExtension.class)
@DisplayName("Объект поле должен")
class FieldTest {

    @DisplayName("добавлять на себя фигуру")
    @ParameterizedTest
    @MethodSource("dataForAddFigure")
    void shouldAddFigure(Figure figure, boolean[][] resultCells) {
        final int columnCount = 3;
        final int rowCount = 2;

        Field field = Field.createNewField(columnCount, rowCount);

        Field newField = field.addFigure(figure);
        assertThat(newField)
                .isEqualTo(new Field(columnCount, rowCount, resultCells));
    }

    private static Stream<Arguments> dataForAddFigure() {
        return Stream.of(
                Arguments.of(new Figure(new FigurePoint(1, 1),
                        new FigurePoint[]{
                                new FigurePoint(0, -1),
                                new FigurePoint(-1, 0),
                                new FigurePoint(0, 0),
                                new FigurePoint(1, 0)}),
                        new boolean[][]{{false, true, false}, {true, true, true}}),

                Arguments.of(createSLeft(1, 1),
                        new boolean[][]{{true, true, false}, {false, true, true}})
        );
    }

    @DisplayName("проверять, можно ли поставить фигуру")
    @ParameterizedTest
    @MethodSource("dataForCanSetTest")
    void shouldCheckCanSet(Figure startFigure, Figure checkedFigure, boolean result) {
        Field field = Field
                .createNewField(3, 4)
                .addFigure(startFigure);

        assertThat(field.canSet(checkedFigure)).isEqualTo(result);
    }

    private static Stream<Arguments> dataForCanSetTest() {
        return Stream.of(
                Arguments.of(null, createSLeft(0, 0), false),
                Arguments.of(null, createSLeft(1, 0), true),
                Arguments.of(null, createSLeft(1, 1), true),
                Arguments.of(null, createSLeft(1, 4), false),
                Arguments.of(null, createSLeft(3, 1), false),
                Arguments.of(createSLeft(1, 3), createSLeft(1, 2), false),
                Arguments.of(createSLeft(1, 3), createSLeft(1, 1), true)
        );
    }

    @DisplayName("проверять, если ли что-то в верхнем ряду")
    @ParameterizedTest
    @MethodSource("dataForContainsInTopRow")
    void shouldCheckContainsInTopRow(Field field, Boolean result) {
        assertThat(field.containsAnyInTopRow()).isEqualTo(result);
    }

    private static Stream<Arguments> dataForContainsInTopRow() {
        return Stream.of(
                Arguments.of(ModelTestUtils.createField(2, 2,
                        new boolean[][]{{false, true}, {true, true}}), true),
                Arguments.of(Field.createNewField(2, 2), false)
        );
    }

    @DisplayName("возвращать список заполненных рядов")
    @ParameterizedTest
    @MethodSource("dataForFullRows")
    void shouldGetFullRowsList(Field field, Integer[] result) {
        assertThat(field.getFullRows()).containsExactly(result);
    }

    private static Stream<Arguments> dataForFullRows() {
        return Stream.of(
                Arguments.of(Field.createNewField(2, 2), new Integer[]{}),
                Arguments.of(ModelTestUtils.createField(2, 2,
                        new boolean[][]{{false, true}, {true, true}}), new Integer[]{1}),
                Arguments.of(ModelTestUtils.createField(2, 3,
                        new boolean[][]{{true, true}, {false, true}, {true, true}}),
                        new Integer[]{0, 2})
        );
    }

    @DisplayName("удалять ряды")
    @ParameterizedTest
    @MethodSource("dataForDeleteRows")
    void shouldDeleteRows(Field startField, List<Integer> rows, Field resultField) {
        Field newField = startField.deleteRows(rows);
        assertThat(newField).isEqualTo(resultField);
    }

    private static Stream<Arguments> dataForDeleteRows() {
        return Stream.of(
                Arguments.of(
                        ModelTestUtils.createField(1, 1, new boolean[][]{{false}}),
                        Collections.emptyList(),
                        ModelTestUtils.createField(1, 1, new boolean[][]{{false}})
                ),

                Arguments.of(
                        ModelTestUtils.createField(2, 3,
                                new boolean[][]{{true, true}, {false, true}, {true, true}}),
                        Arrays.asList(0, 2),
                        ModelTestUtils.createField(2, 3,
                                new boolean[][]{{false, false}, {false, false}, {false, true}})
                )
        );
    }
}