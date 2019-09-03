package ru.biderman.studenttest.dao;

import java.io.InputStream;
import java.io.PrintStream;

public interface UserInterfaceStreams {
    InputStream getInputStream();
    PrintStream getPrintStream();
}
