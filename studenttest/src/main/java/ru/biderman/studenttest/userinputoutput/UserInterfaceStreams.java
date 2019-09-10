package ru.biderman.studenttest.userinputoutput;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * Доступ к потокам ввода-вывода.
 */
interface UserInterfaceStreams {
    InputStream getInputStream();
    PrintStream getPrintStream();
}
