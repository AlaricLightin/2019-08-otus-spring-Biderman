package ru.biderman.tetris.gameservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.biderman.tetris.model.FigureType;
import ru.biderman.tetris.model.GameFieldState;
import ru.biderman.tetris.model.actions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DisplayName("Компонент новых action'ов ")
class ActionServiceImplTest {
    @DisplayName("должен обрабатывать состояние")
    @ParameterizedTest
    @MethodSource("dataForGetActionTest")
    void shouldGetAction(boolean isGameOver,
                         boolean isNeedFigure,
                         List<Integer> rowsForDelete,
                         GameAction[] result) {
        FigureService figureService = mock(FigureService.class);
        when(figureService.nextFigureType()).thenReturn(FigureType.Square);

        ActionServiceImpl actionService = new ActionServiceImpl(figureService);
        GameFieldState state = mock(GameFieldState.class);
        when(state.isNeedFigure()).thenReturn(isNeedFigure);
        when(state.isGameOver()).thenReturn(isGameOver);
        when(state.getFullRows()).thenReturn(rowsForDelete);

        assertThat(actionService.getActions(state))
                .usingFieldByFieldElementComparator()
                .containsExactly(result);
    }

    private static Stream<Arguments> dataForGetActionTest() {
        return Stream.of(
                Arguments.of(false, true, Collections.emptyList(),
                        new GameAction[]{new NewFigureAction(FigureType.Square)}),
                Arguments.of(false, false, Collections.singletonList(1),
                        new GameAction[]{new DeleteRowAction(Collections.singletonList(1))}
                        ),
                Arguments.of(false, true, Arrays.asList(1, 2),
                        new GameAction[]{new DeleteRowAction(Arrays.asList(1, 2)),
                                new NewFigureAction(FigureType.Square)}
                        ),
                Arguments.of(true, false, Collections.emptyList(),
                        new GameAction[]{new EndGameAction(), new StartStopAction(false)})
        );
    }
}