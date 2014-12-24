package com.kh.event;

import com.kh.graph.DependencyGraph;
import com.kh.graph.DependentNotFoundException;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * Listens to an EventStreamParser for parsed events and validates each new event satisfies two properties:
 *   1) All dependencies of the event's type are satisfied by the type of at least one of the event's parents
 *   2) Each parent id/type of the event has been previously validated by this validator
 */
public class EventDependencyValidator implements EventStreamParserListener {
    private final List<Event> events = new ArrayList<>();
    private final EventDependencyValidatorListener listener;
    private final DependencyGraph typeDependencyGraph;

    public EventDependencyValidator(final EventDependencyValidatorListener listener, final DependencyGraph dependencyGraph) {
        this.listener = listener;
        this.typeDependencyGraph = dependencyGraph;
    }

    @Override
    public void onEventParsed(final Event event) {
        if (eventDependenciesSatisfied(event)) {
            events.add(event);
            listener.onEventDependenciesValidated(event);
        } else {
            listener.onEventDependencyValidationFailure(event);
        }
    }

    private boolean eventDependenciesSatisfied(final Event event) {
        try {
            final Collection<Event> parents = event.getParents();
            // previous valid events must contain all parents (with same type & id)
            // *and* all type-dependencies must be satisfied
            return (null == parents || events.containsAll(parents)) &&
                    typeDependencyGraph.satisfiesDependencies(event.getType(), collectTypes(parents));
        } catch (DependentNotFoundException e) {
            return false;
        }
    }

    // collects all type from given events into a set
    private char[] collectTypes(final Collection<Event> events) {
        if (null == events) {
            return new char[0];
        }

        final char[] types = new char[events.size()];
        final Iterator<Event> eventIterator = events.iterator();
        for (int i = 0; i < events.size(); i++) {
            types[i] = eventIterator.next().getType();
        }

        return types;
    }
}
