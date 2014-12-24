package com.kh;

import com.google.gson.JsonSyntaxException;
import com.kh.event.*;
import com.kh.graph.DependencyGraph;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        final DependencyGraph<Character> dependencyGraph = new DependencyGraph<>();
        dependencyGraph.addDependencies('A');
        dependencyGraph.addDependencies('B', 'A');
        dependencyGraph.addDependencies('C', 'A');
        dependencyGraph.addDependencies('D', 'B', 'C');
        dependencyGraph.addDependencies('E');
        dependencyGraph.addDependencies('F', 'B');

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
