package com.kh.graph;

/**
 * Created by khiner on 12/23/14.
 */
public class InvalidDependencyException extends Exception {
    public InvalidDependencyException(final char dependency) {
        super("Dependent of '" + dependency + "' has an invalid value. Must be a capital letter");
    }
}
