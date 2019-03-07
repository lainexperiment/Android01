package com.example.android01;

import android.os.Handler;
import android.os.Looper;

public class ConnectionManager {
    private static final String TAG = "ConnectionManager";
    private Handler cloudHandler;
    private Handler mainThreadHandler;
    private Thread cloud = new Thread(new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            cloudHandler = new Handler();
            Looper.loop();
        }
    });

    public ConnectionManager() {
        this.mainThreadHandler = new Handler(Looper.getMainLooper());
        cloud.start();
    }

    public void load(final int last, final DataLoadCallback callback) {
        cloudHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] result = new int[10];
                for (int i = 0; i < 10; i++) {
                    result[i] = last + i + 1;
                }
                final int[] finalResult = result;
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDataLoaded(finalResult);
                    }
                });
            }
        }, 5000);
    }
}
