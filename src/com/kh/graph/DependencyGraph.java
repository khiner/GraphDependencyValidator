package com.kh.graph;

/**
 * For efficiency reasons, this is not actually implemented as a true graph.
 * It is specialized for unidirectional edge structure of character nodes,
 * where edges go from 'dependents' to 'dependencies'.
 * (It only tracks 'outgoing' connections from characters to a set of other characters.)
 */
public class DependencyGraph {
    private static final int NUM_CHARACTERS = 26;
    private final boolean[] dependentPresent = new boolean[NUM_CHARACTERS]; // has the dependent ever been added?

    // a matrix between two sets of characters, where a true value indicates a dependency
    // from the row to the column (e.g. dependencyMatrix['A']['B'] == true indicates 'A' has a dependency on 'B')
    private final boolean[][] dependencyMatrix = new boolean[NUM_CHARACTERS][NUM_CHARACTERS];

    /**
     * Create outgoing dependency edges from 'dependent' to all given 'dependencies'
     *
     * 'dependencies' can be empty (for nodes with no dependencies).
     *
     * Returns self for chaining.
     */
    public DependencyGraph addDependencies(final char dependent, final char ... dependencies) throws InvalidDependencyException {
        validateCharacterValue(dependent);

        final int dependentArrayIndex = characterToArrayIndex(dependent);

        dependentPresent[dependentArrayIndex] = true;
        for (char dependency : dependencies) {
            validateCharacterValue(dependency);
            dependencyMatrix[dependentArrayIndex][characterToArrayIndex(dependency)] = true;
        }

        return this;
    }

    /**
     * Returns true if 'parents' contains *all* dependencies of 'dependent'
     */
    public boolean satisfiesDependencies(final char dependent, final char[] parents) throws InvalidDependencyException, DependentNotFoundException {
        validateCharacterValue(dependent);

        final int dependentIndex = characterToArrayIndex(dependent);

        if (!dependentPresent[dependentIndex]) {
            // can't really say dependencies are/aren't satisfied if dependent doesn't even exist
            throw new DependentNotFoundException();
        }

        for (char dependency = 'A'; dependency <= 'Z'; dependency++) {
            if (dependencyMatrix[dependentIndex][characterToArrayIndex(dependency)] && !contains(parents, dependency)) {
                return false;
            }
        }

        return true;
    }

    private static void validateCharacterValue(final char value) throws InvalidDependencyException {
        if (value < 'A' || value > 'Z') {
            throw new InvalidDependencyException(value);
        }
    }

    private static int characterToArrayIndex(final char character) {
        return character - 'A';
    }

    private static boolean contains(final char[] array, final char value) {
        if (null == array) {
            return false;
        }

        for (final char element : array) {
            if (element == value) {
                return true;
            }
        }
        return false;
    }
}
