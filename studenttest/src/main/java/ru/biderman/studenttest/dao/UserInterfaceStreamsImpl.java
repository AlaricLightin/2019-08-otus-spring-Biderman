package ru.biderman.studenttest.dao;

import java.io.InputStream;
import java.io.PrintStream;

public class UserInterfaceStreamsImpl implements UserInterfaceStreams {
    @Override
    public InputStream getInputStream() {
        return System.in;
    }

    @Override
    public PrintStream getPrintStream() {
        return System.out;
    }
}
