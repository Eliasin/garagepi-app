package com.example.steven.garagepi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String selectedDeviceName = null;

    public void sendGarageToggleRequest(View v) {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View button = findViewById(R.id.open_close_button);
        button.setOnClickListener(this::sendGarageToggleRequest);

        final ListView deviceList = (ListView) findViewById(R.id.device_list);
        final List<String> backingList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            backingList.add(Integer.toString(i));
        }
        ArrayAdapter<String> deviceListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, backingList);
        deviceList.setAdapter(deviceListAdapter);
        deviceList.setClickable(true);
        deviceList.setOnItemClickListener((parent, view, position, id) -> {
            selectedDeviceName = ((AppCompatTextView) view).getText().toString();
        });
    }

}
