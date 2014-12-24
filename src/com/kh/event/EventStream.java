package com.kh.event;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class EventStream {
    public static InputStream generateEvents() throws IOException {
        return new FileInputStream("assets/serialized_events.txt");
    }
}
