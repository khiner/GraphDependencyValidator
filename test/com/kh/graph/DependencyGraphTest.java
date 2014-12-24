package com.kh.graph;

import com.google.common.collect.Sets;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DependencyGraphTest {

    @Test
    public void dependenciesSatisfied() throws DependentNotFoundException {
        final DependencyGraph dependencyGraph = createTestDependencyGraph();


        assertTrue(dependencyGraph.satisfiesDependencies('A', null));
        assertTrue(dependencyGraph.satisfiesDependencies('B', new char[] {'A'}));
        assertTrue(dependencyGraph.satisfiesDependencies('C', new char[] {'A'}));
        assertTrue(dependencyGraph.satisfiesDependencies('D', new char[] {'B', 'C'}));
        assertTrue(dependencyGraph.satisfiesDependencies('E', null));
        assertTrue(dependencyGraph.satisfiesDependencies('F', new char[] {'B'}));
    }

    @Test
    public void dependenciesNotSatisfied() throws DependentNotFoundException {
        final DependencyGraph dependencyGraph = createTestDependencyGraph();

        assertFalse(dependencyGraph.satisfiesDependencies('C', new char[] {'B'}));
        assertFalse(dependencyGraph.satisfiesDependencies('D', null));
        assertFalse(dependencyGraph.satisfiesDependencies('D', new char[] {'B'}));
        assertFalse(dependencyGraph.satisfiesDependencies('F', new char[] {'A'}));
    }

    @Test
    // Parents not satisfying all dependencies will fail validation,
    // but parents satisfying too many dependencies is ok
    public void dependenciesOversatisfied() throws DependentNotFoundException {
        final DependencyGraph dependencyGraph = createTestDependencyGraph();

        assertTrue(dependencyGraph.satisfiesDependencies('A', new char[] {'B'}));
        assertTrue(dependencyGraph.satisfiesDependencies('B', new char[] {'A', 'B'}));
        assertTrue(dependencyGraph.satisfiesDependencies('C', new char[] {'A', 'B', 'C'}));
        assertTrue(dependencyGraph.satisfiesDependencies('E', new char[] {'E'}));
        assertTrue(dependencyGraph.satisfiesDependencies('F', new char[] {'B', 'C', 'D'}));
    }

    @Test(expected = DependentNotFoundException.class)
    public void throwsNodeNotFoundException() throws DependentNotFoundException {
        final DependencyGraph dependencyGraph = createTestDependencyGraph();

        dependencyGraph.satisfiesDependencies('Z', null);
    }

    private static DependencyGraph createTestDependencyGraph() {
        return new DependencyGraph()
                .addDependencies('A')
                .addDependencies('B', 'A')
                .addDependencies('C', 'A')
                .addDependencies('D', 'B', 'C')
                .addDependencies('E')
                .addDependencies('F', 'B');
    }
}
