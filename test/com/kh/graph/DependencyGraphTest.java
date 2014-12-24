package com.kh.graph;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DependencyGraphTest {

    @Test
    public void dependenciesSatisfied() throws NodeNotFoundException {
        final DependencyGraph<Character> dependencyGraph = createDependencyGraph();

        assertTrue(dependencyGraph.satisfiesDependencies('A', new Character[]{}));
        assertTrue(dependencyGraph.satisfiesDependencies('B', new Character[]{'A'}));
        assertTrue(dependencyGraph.satisfiesDependencies('C', new Character[]{'A'}));
        assertTrue(dependencyGraph.satisfiesDependencies('D', new Character[]{'B', 'C'}));
        assertTrue(dependencyGraph.satisfiesDependencies('E', new Character[]{}));
        assertTrue(dependencyGraph.satisfiesDependencies('F', new Character[]{'B'}));
    }

    @Test
    public void dependenciesNotSatisfied() throws NodeNotFoundException {
        final DependencyGraph<Character> dependencyGraph = createDependencyGraph();

        assertFalse(dependencyGraph.satisfiesDependencies('C', new Character[]{'B'}));
        assertFalse(dependencyGraph.satisfiesDependencies('D', new Character[]{}));
        assertFalse(dependencyGraph.satisfiesDependencies('D', new Character[]{'B'}));
        assertFalse(dependencyGraph.satisfiesDependencies('F', new Character[]{'A'}));
    }

    @Test
    // Parents not satisfying all dependencies will fail validation,
    // but parents satisfying too many dependencies is ok
    public void dependenciesOversatisfied() throws NodeNotFoundException {
        final DependencyGraph<Character> dependencyGraph = createDependencyGraph();

        assertTrue(dependencyGraph.satisfiesDependencies('A', new Character[]{'B'}));
        assertTrue(dependencyGraph.satisfiesDependencies('B', new Character[]{'A', 'B'}));
        assertTrue(dependencyGraph.satisfiesDependencies('C', new Character[]{'A', 'B', 'C'}));
        assertTrue(dependencyGraph.satisfiesDependencies('E', new Character[]{'E'}));
        assertTrue(dependencyGraph.satisfiesDependencies('F', new Character[]{'B', 'C', 'D'}));
    }

    @Test(expected = NodeNotFoundException.class)
    public void throwsNodeNotFoundException() throws NodeNotFoundException {
        final DependencyGraph<Character> dependencyGraph = createDependencyGraph();

        dependencyGraph.satisfiesDependencies('Z', new Character[]{});
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
