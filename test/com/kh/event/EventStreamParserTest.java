package com.kh.event;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EventStreamParserTest {

    @Test
    public void parsesEventsWithParents() throws IOException {
        final List<Event> events = new ArrayList<>();

        final EventStreamParser eventStreamParser = new EventStreamParser(new EventStreamParserListener() {
            @Override
            public void onEventParsed(final Event event) {
                assertNotNull(event);
                events.add(event);
            }
        });

        final String[] serializedEvents = {
            "{\"type\":\"A\",\"id\":1,\"name\":\"apple\"}",
            "{\"type\":\"A\",\"id\":2,\"name\":\"banana\"}",
            "{\"type\":\"B\",\"id\":1,\"parent\":[{\"type\":\"A\",\"id\":1}],\"name\":\"volvo\"}",
            "{\"type\":\"B\",\"id\":2,\"parent\":[{\"type\":\"A\",\"id\":2}],\"name\":\"audi\"}",
            "{\"type\":\"C\",\"id\":1,\"parent\":[{\"type\":\"A\",\"id\":1}],\"name\":\"soccer\"}",
            "{\"type\":\"C\",\"id\":2,\"parent\":[{\"type\":\"A\",\"id\":2}],\"name\":\"football\"}",
            "{\"type\":\"D\",\"id\":1,\"parent\":[{\"type\":\"B\",\"id\":2},{\"type\":\"C\",\"id\":1}],\"name\":\"squirrels\"}",
            "{\"type\":\"D\",\"id\":2,\"parent\":[{\"type\":\"B\",\t\"id\":1\t},\t{\"type\":\"C\",\"id\":2}],\"name\":\"chipmunk\"}",
            "{\"type\":\"E\",\"id\":1,\"name\":\"plane\"}",
            "{\"type\":\"E\",\"id\":2,\"name\":\"train\"}",
            "{\"type\":\"F\",\"id\":8,\"parent\":[{\"type\":\"B\",\"id\":1}],\"name\":\"squid\"}",
        };

        final String combinedEvents = StringUtils.join(serializedEvents, '\n');
        eventStreamParser.parse(IOUtils.toInputStream(combinedEvents, "UTF-8"));

        assertEquals(events.size(), serializedEvents.length);

        final Event apple = events.get(0);
        assertEquals(apple.getId(), 1);
        assertEquals(apple.getType(), 'A');
        assertEquals(apple.getName(), "apple");

        final Event banana = events.get(1);
        assertEquals(banana.getId(), 2);
        assertEquals(banana.getType(), 'A');
        assertEquals(banana.getName(), "banana");

        final Event volvo = events.get(2);
        assertEquals(volvo.getId(), 1);
        assertEquals(volvo.getType(), 'B');
        assertEquals(volvo.getName(), "volvo");
        assertEquals(volvo.getParents().size(), 1);
        assertTrue(volvo.getParents().contains(new Event(1, 'A')));

        final Event squirrels = events.get(6);
        assertEquals(squirrels.getId(), 1);
        assertEquals(squirrels.getType(), 'D');
        assertEquals(squirrels.getName(), "squirrels");
        assertEquals(squirrels.getParents().size(), 2);
        assertTrue(squirrels.getParents().contains(new Event(2, 'B')));
        assertTrue(squirrels.getParents().contains(new Event(1, 'C')));

        final Event squid = events.get(10);
        assertEquals(squid.getId(), 8);
        assertEquals(squid.getType(), 'F');
        assertEquals(squid.getName(), "squid");
        assertEquals(squid.getParents().size(), 1);
        assertTrue(squid.getParents().contains(new Event(1, 'B')));
    }
}
