package ru.biderman.library.userinputoutput;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * Доступ к потокам ввода-вывода.
 */
public interface UserInterfaceStreams {
    InputStream getInputStream();
    PrintStream getPrintStream();
}
