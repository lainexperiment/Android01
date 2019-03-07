package com.lainexperiment.android01;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class NotificationCenter {
    private Map<EventType, Event> observableMap;
    private static final String TAG = "NotificationCenter";

    private static NotificationCenter single_instance = null;

    private NotificationCenter() {
        this.observableMap = new HashMap<>();
        initializeObservables();
    }

    public static NotificationCenter getInstance() {
        if (single_instance == null)
            single_instance = new NotificationCenter();

        return single_instance;
    }

    private void initializeObservables() {
        for (EventType type : EventType.values()) {
            observableMap.put(type, new Event(type));
        }
    }

    public void register(EventType eventType, EventObserver observer) {
        Event eventObservable = observableMap.get(eventType);
        if (eventObservable == null) {
            Log.e(TAG, "register: unable to register to observable with type " + eventType +
                    " observable was null.");
            return;
        }
        eventObservable.addObserver(observer);
    }

    public void unregister(EventType eventType, EventObserver observer) {
        Event eventObservable = observableMap.get(eventType);
        if (eventObservable == null) {
            Log.e(TAG, "unregister: unable to unregister from observable with type " + eventType +
                    " observable was null.");
            return;
        }
        eventObservable.deleteObserver(observer);
    }

    public void dataLoaded() {
        Log.d(TAG, "dataLoaded: called");
        Event dataLoadedObservable = observableMap.get(EventType.DATA_LOADED);
        if (dataLoadedObservable == null) {
            Log.e(TAG, "dataLoaded: No event found for Data_Loaded eventType");
            return;
        }
        dataLoadedObservable.setChanged();
        dataLoadedObservable.notifyObservers();
    }
}
