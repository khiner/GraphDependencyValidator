package com.kh.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Parses an InputStream of JSON-formatted event objects into Event objects and
 * notifies a listener for each parsed event
 */
public class EventStreamParser {
    private final EventStreamParserListener eventListener;
    private final Gson gson = new GsonBuilder().create();

    public EventStreamParser(final EventStreamParserListener eventListener) {
        this.eventListener = eventListener;
    }

    public void parse(final InputStream in) throws IOException, JsonSyntaxException {
        final BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String serializedEvent;
        while ((serializedEvent = streamReader.readLine()) != null) {
            final Event event = gson.fromJson(serializedEvent, Event.class);
            if (eventListener != null) {
                eventListener.onEventParsed(event);
            }
        }
    }
}
