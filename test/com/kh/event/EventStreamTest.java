package com.kh.event;

import com.google.gson.*;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventStreamTest {

    @Test
    // just make sure the stream loads correctly (file exists and has JSON content)
    public void noErrorsThrownAndJsonParseable() throws IOException {
        try (InputStream eventStream = EventStream.generateEvents()) {
            final BufferedReader streamReader = new BufferedReader(new InputStreamReader(eventStream, "UTF-8"));
            final String serializedEventLine = streamReader.readLine();
            assertFalse(serializedEventLine.isEmpty());

            final JsonElement eventElement = new Gson().fromJson(serializedEventLine, JsonElement.class);
            final JsonObject eventObject = eventElement.getAsJsonObject();
            assertTrue(eventObject.isJsonObject());
        } catch(IOException e) {
            throw e;
        }
    }
}
