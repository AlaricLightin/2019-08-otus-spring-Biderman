package ru.biderman.librarymongo.userinputoutput;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * Доступ к потокам ввода-вывода.
 */
public interface UserInterfaceStreams {
    InputStream getInputStream();
    PrintStream getPrintStream();
}
