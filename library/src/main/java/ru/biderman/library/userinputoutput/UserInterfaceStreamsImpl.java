package ru.biderman.library.userinputoutput;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.PrintStream;

@Service
class UserInterfaceStreamsImpl implements UserInterfaceStreams {
    @Override
    public InputStream getInputStream() {
        return System.in;
    }

    @Override
    public PrintStream getPrintStream() {
        return System.out;
    }
}
