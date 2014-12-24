package com.kh.graph;

import java.util.HashSet;
import java.util.Set;

public class GraphNode<T> {
    private final T value;
    private final Set<GraphNode<T>> outgoingNodes = new HashSet<>();

    public GraphNode(final T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void addOutgoingNode(final GraphNode<T> node) {
        outgoingNodes.add(node);
    }

    public Set<GraphNode<T>> getOutgoingNodes() {
        return outgoingNodes;
    }
}
