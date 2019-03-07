package com.lainexperiment.android01;

import android.content.Context;

import java.util.ArrayList;

public class MessageController {

    private ConnectionManager connectionManager;
    private StorageManager storageManager;
    private NotificationCenter notificationCenter;

    public ArrayList<Integer> getData() {
        return data;
    }

    private ArrayList<Integer> data = new ArrayList<>();

    private static MessageController single_instance = null;

    private MessageController(Context context) {
        connectionManager = new ConnectionManager();
        storageManager = new StorageManager(context);
        notificationCenter = NotificationCenter.getInstance();
    }

    public static MessageController getInstance(Context context) {
        if (single_instance == null)
            single_instance = new MessageController(context);

        return single_instance;
    }

    public void fetch(Boolean fromCache) {
        int last = data.isEmpty() ? 0 : data.get(data.size() - 1);
        if (fromCache) {
            storageManager.load(last, new DataLoadCallback() {
                @Override
                public void onDataLoaded(int[] result) {
                    if (result != null) {
                        clearData();
                        for (int aLoadedData : result) {
                            data.add(aLoadedData);
                        }
                        notificationCenter.dataLoaded();
                    }
                }
            });
        } else {
            connectionManager.load(last, new DataLoadCallback() {
                @Override
                public void onDataLoaded(int[] result) {
                    if (result != null) {
                        clearData();
                        for (int i = 1; i <= result[9]; i++) {
                            data.add(i);
                        }
                        notificationCenter.dataLoaded();
                        storageManager.save(result[result.length - 1]);
                    }
                }
            });
        }
    }

    private void loadData(int[] loadedData) {

    }

    public void clearData() {
        data.clear();
    }
}
