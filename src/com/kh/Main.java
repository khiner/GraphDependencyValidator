package com.kh;

import com.google.gson.JsonSyntaxException;
import com.kh.event.Event;
import com.kh.event.EventDependencyValidator;
import com.kh.event.EventDependencyValidatorListener;
import com.kh.event.EventStream;
import com.kh.event.EventStreamParser;
import com.kh.graph.DependencyGraph;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        final DependencyGraph dependencyGraph =
                new DependencyGraph()
                        .addDependencies('A')
                        .addDependencies('B', 'A')
                        .addDependencies('C', 'A')
                        .addDependencies('D', 'B', 'C')
                        .addDependencies('E')
                        .addDependencies('F', 'B');

        final EventDependencyValidatorListener validatorListener = new EventDependencyValidatorListener() {
            @Override
            public void onEventDependenciesValidated(final Event event) {
                System.out.println("Event dependencies satisfied:\n" + event + "\n");
            }

            @Override
            public void onEventDependencyValidationFailure(final Event event) {
                System.out.println("Event dependencies not satisfied!\n" + event + "\n");
            }
        };

        final EventDependencyValidator eventDependencyValidator = new EventDependencyValidator(validatorListener, dependencyGraph);
        final EventStreamParser eventStreamParser = new EventStreamParser(eventDependencyValidator);

        try {
            eventStreamParser.parse(EventStream.generateEvents());
        } catch (IOException | JsonSyntaxException e) {
            System.out.println("Error parsing event stream:");
            e.printStackTrace();
        }
    }
}
