package com.lainexperiment.android01;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EventObserver {
    private static final String TAG = "MainActivity";
    private static final String SAVED_INTEGER_LIST = "savedIntegerList";

    private NotificationCenter notificationCenter;
    private MessageController messageController;

    private Button clearButton;
    private Button refreshButton;
    private Button getButton;
    private LinearLayout dataListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notificationCenter = NotificationCenter.getInstance();
        messageController = MessageController.getInstance(this);
        notificationCenter.register(EventType.DATA_LOADED, this);

        initUI();
    }

    private void initUI() {
        clearButton = findViewById(R.id.clear_btn);
        refreshButton = findViewById(R.id.refresh_btn);
        getButton = findViewById(R.id.get_btn);
        dataListLayout = findViewById(R.id.data_layout);
        showData();
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataListLayout.removeAllViews();
                messageController.clearData();
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: called refresh");
                messageController.fetch(true);
            }
        });

        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: called get");
                messageController.fetch(false);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notificationCenter.unregister(EventType.DATA_LOADED, this);
    }

    @Override
    public void update(Event event) {
        Log.d(TAG, "update: called");
        showData();
    }

    private void showData() {
        dataListLayout.removeAllViews();
        ArrayList<Integer> data = messageController.getData();
        Log.d(TAG, "showData: called with " + data.size());
        for (Integer dataItem: data) {
            dataListLayout.addView(getItemView(dataItem));
        }
    }

    private TextView getItemView(Integer dataItem) {
        TextView itemView = new TextView(this);
        itemView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(8,8,8,0);
        itemView.setLayoutParams(params);
        itemView.setText(dataItem.toString());
        itemView.setTextColor(getResources().getColor(R.color.colorAccent));
        return itemView;
    }
}