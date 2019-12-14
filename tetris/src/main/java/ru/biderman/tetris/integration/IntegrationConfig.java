package ru.biderman.tetris.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.endpoint.EventDrivenConsumer;
import ru.biderman.tetris.gameservices.ActionService;
import ru.biderman.tetris.gameservices.FieldAnalyzer;
import ru.biderman.tetris.gameservices.FieldUpdater;
import ru.biderman.tetris.model.GameField;
import ru.biderman.tetris.model.actions.EndGameAction;
import ru.biderman.tetris.model.actions.GameAction;
import ru.biderman.tetris.model.actions.StartStopAction;

@Configuration
public class IntegrationConfig {
    @Bean
    public PublishSubscribeChannel gameActionSubscribeChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public PublishSubscribeChannel currentFieldChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public EventDrivenConsumer drawEndPoint(GameFieldController gameFieldController) {
        return new EventDrivenConsumer(currentFieldChannel(),
                message -> {
                    if (message.getPayload() instanceof GameField)
                        gameFieldController.setNewState((GameField) message.getPayload());
                }
        );
    }

    @Bean
    public IntegrationFlow incomingActionFlow(FieldUpdater updater, GameTimer timer) {
        return IntegrationFlows
                .from(gameActionSubscribeChannel())
                .<GameAction, Boolean>route(
                        fieldAction -> fieldAction instanceof StartStopAction,
                        mapping ->
                                mapping
                                        .subFlowMapping(true, sf -> sf
                                                .transform(StartStopAction.class, StartStopAction::isStart)
                                                .publishSubscribeChannel(c ->
                                                        c.subscribe(s ->
                                                                s.handle(m ->
                                                                        timer.setRunning((Boolean) m.getPayload()))))
                                        )
                                        .subFlowMapping(false, sf -> sf
                                                .handle(updater)
                                                .channel(currentFieldChannel()))
                )
                .get();
    }

    @Bean
    public IntegrationFlow analyzeFieldFlow(FieldAnalyzer analyzer,
                                            ActionService actionService,
                                            GameFieldController gameFieldController) {
        return IntegrationFlows
                .from(currentFieldChannel())
                .handle(analyzer)
                .handle(actionService)
                .split()
                .<GameAction, Boolean>route(
                        fieldAction -> fieldAction instanceof EndGameAction,
                        mapping ->
                                mapping
                                        .subFlowMapping(true, sf -> sf.publishSubscribeChannel(c ->
                                                c.subscribe(s ->
                                                        s.handle(m -> gameFieldController.setEndGame()))))
                                        .subFlowMapping(false, sf -> sf.channel(gameActionSubscribeChannel()))
                )
                .get();
    }
}
