package com.kh.event;

import com.kh.graph.DependencyGraph;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import com.google.common.collect.Sets;

import static org.junit.Assert.assertTrue;

public class EventDependencyValidatorTest {

    @Test
    public void validatesEventsCorrectly() {
        final DependencyGraph<Character> dependencyGraph = createDependencyGraph();
        final Set<Event> validEvents = new HashSet<>();
        final Set<Event> invalidEvents = new HashSet<>();

        final EventDependencyValidatorListener listener = new EventDependencyValidatorListener() {
            @Override
            public void onEventDependenciesValidated(final Event event) {
                validEvents.add(event);
            }

            @Override
            public void onEventDependencyValidationFailure(final Event event) {
                invalidEvents.add(event);
            }
        };

        final EventDependencyValidator eventStreamDependencyValidator = new EventDependencyValidator(listener, dependencyGraph);


        eventStreamDependencyValidator.onEventParsed(new Event(1, 'A'));
        assertTrue(validEvents.contains(new Event(1, 'A')));

        eventStreamDependencyValidator.onEventParsed(new Event(1, 'E'));
        assertTrue(validEvents.contains(new Event(1, 'E')));

        // no parent
        eventStreamDependencyValidator.onEventParsed(new Event(1, 'B'));
        assertTrue(invalidEvents.contains(new Event(1, 'B')));

        // parent wrong type
        eventStreamDependencyValidator.onEventParsed(new Event(2, 'B', "test", Sets.newHashSet(new Event(1, 'E'))));
        assertTrue(invalidEvents.contains(new Event(2, 'B')));

        // parent wrong id
        eventStreamDependencyValidator.onEventParsed(new Event(3, 'B', "test", Sets.newHashSet(new Event(2, 'A'))));
        assertTrue(invalidEvents.contains(new Event(3, 'B')));

        // parent juuuuuust right
        eventStreamDependencyValidator.onEventParsed(new Event(4, 'B', "test", Sets.newHashSet(new Event(1, 'A'))));
        assertTrue(validEvents.contains(new Event(4, 'B')));

        // one of two parents
        eventStreamDependencyValidator.onEventParsed(new Event(1, 'D', "test", Sets.newHashSet(new Event(1, 'B'))));
        assertTrue(invalidEvents.contains(new Event(1, 'D')));

        // both parent-types satisfied, but only one parent exists
        eventStreamDependencyValidator.onEventParsed(new Event(2, 'D', "test", Sets.newHashSet(new Event(1, 'B'), new Event(1, 'C'))));
        assertTrue(invalidEvents.contains(new Event(2, 'D')));

        // both parent-types satisfied *and* parents exist. happy day.
        eventStreamDependencyValidator.onEventParsed(new Event(1, 'C', "test", Sets.newHashSet(new Event(1, 'A'))));
        assertTrue(validEvents.contains(new Event(1, 'C')));
        eventStreamDependencyValidator.onEventParsed(new Event(3, 'D', "test", Sets.newHashSet(new Event(4, 'B'), new Event(1, 'C'))));
        assertTrue(validEvents.contains(new Event(3, 'D')));

        // more parents than dependencies are ok
        eventStreamDependencyValidator.onEventParsed(new Event(1, 'F', "test", Sets.newHashSet(new Event(4, 'B'), new Event(1, 'C'), new Event(1, 'A'))));
        assertTrue(validEvents.contains(new Event(1, 'F')));
    }

    private static DependencyGraph<Character> createDependencyGraph() {
        final DependencyGraph<Character> dependencyGraph = new DependencyGraph<>();

        dependencyGraph.addDependencies('A');
        dependencyGraph.addDependencies('B', 'A');
        dependencyGraph.addDependencies('C', 'A');
        dependencyGraph.addDependencies('D', 'B', 'C');
        dependencyGraph.addDependencies('E');
        dependencyGraph.addDependencies('F', 'B');

        return dependencyGraph;
    }
}
