package ru.biderman.librarymongo.userinputoutput.exceptions;

import ru.biderman.librarymongo.domain.Author;
import ru.biderman.librarymongo.userinputoutput.UIUtils;

import java.util.List;
import java.util.stream.Collectors;

public class DuplicateAuthorUIException extends UserInputException {
    private final List<Author> duplicateList;

    public DuplicateAuthorUIException(List<Author> duplicateList) {
        this.duplicateList = duplicateList;
    }

    @Override
    public String getLocalizedMessageId() {
        return "shell.error.several-authors";
    }

    @Override
    public Object[] getLocalizedMessageArgs() {
        String resultString = duplicateList.stream()
                .map(UIUtils::getAuthorString)
                .collect(Collectors.joining("\n"));
        return new String[]{resultString};
    }
}
