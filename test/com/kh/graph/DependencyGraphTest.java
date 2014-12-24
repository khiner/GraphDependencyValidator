package com.kh.graph;

import com.google.common.collect.Sets;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DependencyGraphTest {

    @Test
    public void dependenciesSatisfied() throws NodeNotFoundException {
        final DependencyGraph<Character> dependencyGraph = createDependencyGraph();


        assertTrue(dependencyGraph.satisfiesDependencies('A', null));
        assertTrue(dependencyGraph.satisfiesDependencies('B', Sets.newHashSet('A')));
        assertTrue(dependencyGraph.satisfiesDependencies('C', Sets.newHashSet('A')));
        assertTrue(dependencyGraph.satisfiesDependencies('D', Sets.newHashSet('B', 'C')));
        assertTrue(dependencyGraph.satisfiesDependencies('E', null));
        assertTrue(dependencyGraph.satisfiesDependencies('F', Sets.newHashSet('B')));
    }

    @Test
    public void dependenciesNotSatisfied() throws NodeNotFoundException {
        final DependencyGraph<Character> dependencyGraph = createDependencyGraph();

        assertFalse(dependencyGraph.satisfiesDependencies('C', Sets.newHashSet('B')));
        assertFalse(dependencyGraph.satisfiesDependencies('D', null));
        assertFalse(dependencyGraph.satisfiesDependencies('D', Sets.newHashSet('B')));
        assertFalse(dependencyGraph.satisfiesDependencies('F', Sets.newHashSet('A')));
    }

    @Test
    // Parents not satisfying all dependencies will fail validation,
    // but parents satisfying too many dependencies is ok
    public void dependenciesOversatisfied() throws NodeNotFoundException {
        final DependencyGraph<Character> dependencyGraph = createDependencyGraph();

        assertTrue(dependencyGraph.satisfiesDependencies('A', Sets.newHashSet('B')));
        assertTrue(dependencyGraph.satisfiesDependencies('B', Sets.newHashSet('A', 'B')));
        assertTrue(dependencyGraph.satisfiesDependencies('C', Sets.newHashSet('A', 'B', 'C')));
        assertTrue(dependencyGraph.satisfiesDependencies('E', Sets.newHashSet('E')));
        assertTrue(dependencyGraph.satisfiesDependencies('F', Sets.newHashSet('B', 'C', 'D')));
    }

    @Test(expected = NodeNotFoundException.class)
    public void throwsNodeNotFoundException() throws NodeNotFoundException {
        final DependencyGraph<Character> dependencyGraph = createDependencyGraph();

        dependencyGraph.satisfiesDependencies('Z', null);
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
