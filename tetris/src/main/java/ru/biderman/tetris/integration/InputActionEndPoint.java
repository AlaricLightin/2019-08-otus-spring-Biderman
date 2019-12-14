package ru.biderman.tetris.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.biderman.tetris.model.actions.GameAction;

@MessagingGateway
public interface InputActionEndPoint {
    @Gateway(requestChannel = "gameActionSubscribeChannel")
    void sendAction(GameAction action);
}
