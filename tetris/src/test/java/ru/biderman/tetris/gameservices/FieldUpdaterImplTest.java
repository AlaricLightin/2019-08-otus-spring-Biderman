package ru.biderman.tetris.gameservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.biderman.tetris.model.*;
import ru.biderman.tetris.model.actions.*;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.biderman.tetris.model.ModelTestUtils.createSquare;

@ExtendWith(SpringExtension.class)
@DisplayName("Объект, отвечающий за обновление поля")
class FieldUpdaterImplTest {
    @DisplayName("должен получать новое состояние")
    @ParameterizedTest(name = "{0}")
    @MethodSource("dataForGetNew")
    void shouldGetNewField(@SuppressWarnings("unused") String description,
                           GameField startField,
                           GameAction action,
                           GameField result) {
        FieldUpdaterImpl fieldUpdater = new FieldUpdaterImpl(startField);
        GameField newGameField = fieldUpdater.getNew(action);
        assertThat(newGameField).isEqualTo(result);
        assertThat(fieldUpdater)
                .hasFieldOrPropertyWithValue("gameField", result);
    }

    @DisplayName("должен сбрасывать поле на стартовое при начале игры")
    @Test
    void shouldBeginNewGame() {
        GameField startField = new GameField(Field.createNewField(2, 3), null);
        FieldUpdaterImpl fieldUpdater = new FieldUpdaterImpl(startField);
        fieldUpdater.getNew(new NewFigureAction(FigureType.Square));
        GameField newGameField = fieldUpdater.getNew(new NewGameAction());
        assertThat(newGameField).isEqualTo(startField);
    }

    private static Stream<Arguments> dataForGetNew() {
        return Stream.of(
                Arguments.of(
                        "Сдвиг вниз, когда сдвинуться можно",
                        new GameField(
                                Field.createNewField(2, 3),
                                createSquare(0, 1)
                        ),
                        new FigureMoveAction(FigureOperation.DOWN),
                        new GameField(
                                Field.createNewField(2, 3),
                                createSquare(0, 2)
                        )
                ),

                Arguments.of(
                        "Сдвиг влево, когда сдвинуться нельзя",
                        new GameField(
                                Field.createNewField(2, 3),
                                createSquare(0, 1)
                        ),
                        new FigureMoveAction(FigureOperation.LEFT),
                        new GameField(
                                Field.createNewField(2, 3),
                                createSquare(0, 1)
                        )
                ),

                Arguments.of(
                        "Сдвиг вниз, когда фигура должна прилипнуть",
                        new GameField(
                                Field.createNewField(2, 2),
                                createSquare(0, 1)
                        ),
                        new FigureMoveAction(FigureOperation.DOWN),
                        new GameField(
                                ModelTestUtils.createField(2, 2,
                                        new boolean[][]{{true, true}, {true, true}}),
                                null
                        )
                ),

                Arguments.of(
                        "Фигуры нет, почему-то пришла команда на движение",
                        new GameField(
                                Field.createNewField(2, 2),
                                null
                        ),
                        new FigureMoveAction(FigureOperation.DOWN),
                        new GameField(
                                Field.createNewField(2, 2),
                                null
                        )
                ),

                Arguments.of(
                        "Команда на удаление рядов",
                        new GameField(
                                ModelTestUtils.createField(2, 3,
                                        new boolean[][]{{true, true}, {true, false}, {true, true}}),
                                null
                        ),
                        new DeleteRowAction(Arrays.asList(0, 2)),
                        new GameField(
                                ModelTestUtils.createField(2, 3,
                                        new boolean[][]{{false, false}, {false, false}, {true, false}}),
                                null
                        )
                ),

                Arguments.of(
                        "Добавление новой фигуры",
                        new GameField(
                                Field.createNewField(5, 3),
                                null
                        ),
                        new NewFigureAction(FigureType.SLeft),
                        new GameField(
                                Field.createNewField(5, 3),
                                FiguresFactory.createFigure(FigureType.SLeft, 3, 0)
                        )
                )
        );
    }
}