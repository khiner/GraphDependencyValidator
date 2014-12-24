package com.kh.graph;

import java.util.HashMap;

/**
 * Generic directed graph data structure, composed of GraphNodes
 * Specialized to unidirectional edge structure, where outgoing nodes go from 'dependents' to 'dependencies' (one-to-many)
 * Does not handle cyclic-dependency checks!
 */
public class DependencyGraph<T> {
    private final HashMap<T, GraphNode<T>> nodes = new HashMap<>();

    /**
     * Create outgoing dependency edges from 'dependent' to all given 'dependencies'
     * 'dependent' will be added to the graph if it is not already present
     * 'dependencies' can be empty (for nodes with no dependencies)
     */
    public void addDependencies(final T dependent, final T... dependencies) {
        final GraphNode<T> dependentNode = findOrCreateNode(dependent);

        for (final T dependency : dependencies) {
            final GraphNode<T> dependencyNode = findOrCreateNode(dependency);
            // could also add incoming connections for bidirectional edges here,
            // but not strictly necessary for this application
            dependentNode.addOutgoingNode(dependencyNode);
        }
    }

    /**
     * Returns true if 'parents' contains *all* dependencies of 'dependent'
     */
    public boolean satisfiesDependencies(final T dependent, final T[] parents) throws NodeNotFoundException {
        final GraphNode<T> dependentNode = nodes.get(dependent);
        if (null == dependentNode) {
            // can't really say dependencies are/aren't satisfied if dependent doesn't even exist
            throw new NodeNotFoundException();
        }

        for (final GraphNode<T> dependencyNode : dependentNode.getOutgoingNodes()) {
            if (!contains(parents, dependencyNode.getValue())) {
                return false;
            }
        }

        return true;
    }

    private GraphNode<T> findOrCreateNode(final T type) {
        if (nodes.containsKey(type)) {
            return nodes.get(type);
        } else {
            final GraphNode<T> node = new GraphNode<>(type);
            nodes.put(type, node);
            return node;
        }
    }

    // a little utility method to avoid creating a temp List for a `contains(...)` call
    private static <T> boolean contains(final T[] array, final T v) {
        for (final T e : array) {
            if (e == v || v != null && v.equals(e)) {
                return true;
            }
        }
        return false;
    }
}
