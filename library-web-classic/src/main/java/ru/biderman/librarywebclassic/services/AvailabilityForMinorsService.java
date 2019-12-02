package ru.biderman.librarywebclassic.services;

import ru.biderman.librarywebclassic.domain.Book;

public interface AvailabilityForMinorsService {
    boolean isAdultOnly(Book book);
    void setRights(Book book, boolean adultOnly);
}
