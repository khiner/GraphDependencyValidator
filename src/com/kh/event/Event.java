package com.kh.event;

import java.util.Iterator;
import java.util.Set;

public class Event {
    private final int id;
    private final char type;
    private String name;
    private Set<Event> parent; // called 'parent' (singular) to match JSON structure
    private char[] parentTypes; // copy types of all parents during instantiation for faster dependency analysis

    // This constructor is here to allow automatic JSON parsing of incomplete 'parent' event elements (with id/type)
    public Event(final int id, final char type) {
        this.id = id;
        this.type = type;
        parentTypes = new char[0];
    }

    public Event(final int id, final char type, final String name, final Set<Event> parent) {
        this(id, type);
        this.name = name;
        this.parent = parent;

        if (null == parent) {
            parentTypes = new char[0];
        } else {
            parentTypes = new char[parent.size()];
            final Iterator<Event> eventIterator = parent.iterator();
            for (int i = 0; i < parent.size(); i++) {
                parentTypes[i] = eventIterator.next().getType();
            }
        }
    }

    public int getId() {
        return id;
    }

    public char getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Set<Event> getParents() {
        return parent;
    }

    // collects all type from given events into a set
    public char[] getParentTypes() {
        return parentTypes;
    }

    @Override
    // uniqueness determined by id & type alone
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return id == event.id && type == event.type;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (int) type;
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", parent=" + parent +
                '}';
    }
}
