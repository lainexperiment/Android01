package com.lainexperiment.android01;

import java.util.ArrayList;
import java.util.Observer;

public class Event {
    private final EventType eventType;
    private boolean changed = false;
    private ArrayList<EventObserver> observers = new ArrayList<>();

    public Event(EventType eventType) {
        this.eventType = eventType;
    }

    public void setChanged() {
        changed = true;
    }

    public void clearChanged() {
        changed = false;
    }

    public boolean hasChanged() {
        return changed;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void notifyObservers() {
        Object[] arrLocal;
        synchronized (this) {
            if (!hasChanged()) {
                return;
            }
            arrLocal = observers.toArray();
            clearChanged();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((EventObserver)arrLocal[i]).update(this);
    }

    public void addObserver(EventObserver observer) {
        observers.add(observer);
    }

    public void deleteObserver(EventObserver observer) {
        observers.remove(observer);
    }
}
