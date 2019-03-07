package com.example.android01;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class StorageManager {

    private Context context;

    private static final String TAG = "StorageManager";
    private static final String CONFIG_FILE = "Config.txt";
    private Handler storageHandler;
    private Handler mainThreadHandler;
    private Thread storage = new Thread(new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            storageHandler = new Handler();
            Looper.loop();
        }
    });

    public StorageManager(Context context) {
        this.context = context;
        this.mainThreadHandler = new Handler(Looper.getMainLooper());
        storage.start();
    }

    public void save(final int lastSeenNumber) {
        storageHandler.post(new Runnable() {
            @Override
            public void run() {
                writeToFile(String.valueOf(lastSeenNumber));
            }
        });
    }

    public void load(final int last, final DataLoadCallback callback) {
        storageHandler.post(new Runnable() {
            @Override
            public void run() {
                int storedNumber = 0;
                int[] result = null;
                String storedData = readFromFile();
                if (!storedData.isEmpty()) {
                    try {
                        storedNumber = Integer.parseInt(storedData);
                        result = new int[10];
                        if (last >= storedNumber) result = null;
                        else {
                            for (int i = 0; i < 10; i++) {
                                result[i] = last + i + 1;
                            }
                        }
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Malformed data found in config file", e);
                    }
                }

                final int[] finalResult = result;
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                    callback.onDataLoaded(finalResult);
                    }
                });
            }
        });
    }

    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(CONFIG_FILE, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "writeToFile: File not found", e);
        }
        catch (IOException e) {
            Log.e(TAG, "writeToFile: Error trying to write data to config file", e);
        }

    }

    private String readFromFile() {
        String readInfo = "";
        try {
            InputStream inputStream = context.openFileInput(CONFIG_FILE);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                readInfo = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "readFromFile: File not found", e);
        }
        catch (IOException e) {
            Log.e(TAG, "readFromFile: Error trying to read data from config file", e);
        }

        return readInfo;
    }
}
