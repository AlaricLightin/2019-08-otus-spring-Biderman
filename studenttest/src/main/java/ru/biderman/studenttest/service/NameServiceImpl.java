package ru.biderman.studenttest.service;

import ru.biderman.studenttest.dao.UserInterface;

import java.util.Optional;

public class NameServiceImpl implements NameService{
    private final UserInterface userInterface;

    public NameServiceImpl(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public Optional<String> getName() {
        return userInterface.readValue("Введите имя и фамилию через пробел.", NameInputChecker::new);
    }
}
