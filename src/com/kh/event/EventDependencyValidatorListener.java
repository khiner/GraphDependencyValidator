package com.kh.event;

public interface EventDependencyValidatorListener {
    void onEventDependenciesValidated(Event event);
    void onEventDependencyValidationFailure(Event event);
}
