package com.kh.graph;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * For efficiency reasons, this is not actually implemented as a true graph.
 * It is specialized for unidirectional edge structure, where outgoing nodes go from 'dependents' to 'dependencies'
 * (It only tracks a 'outgoing' connections from objects to a set of other objects.)
 */
public class DependencyGraph<T> {
    private final HashMap<T, Set<T>> nodes = new HashMap<>();

    /**
     * Create outgoing dependency edges from 'dependent' to all given 'dependencies'
     *
     * 'dependent' will be added to the graph if it is not already present.
     * 'dependencies' can be empty (for nodes with no dependencies).
     *
     * Returns self for chaining.
     */
    public DependencyGraph<T> addDependencies(final T dependent, final T... dependencies) {
        if (nodes.containsKey(dependent)) {
            final Set<T> currentDependencies = nodes.get(dependent);
            Collections.addAll(currentDependencies, dependencies);
        } else {
            nodes.put(dependent, Sets.newHashSet(dependencies));
        }
        return this;
    }

    /**
     * Returns true if 'parents' contains *all* dependencies of 'dependent'
     */
    public boolean satisfiesDependencies(final T dependent, final Set<T> parents) throws NodeNotFoundException {
        if (!nodes.containsKey(dependent)) {
            // can't really say dependencies are/aren't satisfied if dependent doesn't even exist
            throw new NodeNotFoundException();
        }

        return (null == parents && nodes.get(dependent).isEmpty()) ||
                (null != parents && parents.containsAll(nodes.get(dependent)));
    }
}
