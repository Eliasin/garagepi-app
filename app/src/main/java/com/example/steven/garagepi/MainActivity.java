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

    public List<String> getNearbyCompatibleDevices() {
        return new ArrayList<>();
    }

    public void refreshDeviceList(View v) {
        final ListView deviceList = (ListView) findViewById(R.id.device_list);
        List<String> backingList = getNearbyCompatibleDevices();
        ArrayAdapter<String> deviceListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, backingList);
        deviceList.setAdapter(deviceListAdapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View onOffButton = findViewById(R.id.open_close_button);
        onOffButton.setOnClickListener(this::sendGarageToggleRequest);

        final View refreshButton = findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(this::refreshDeviceList);

        final ListView deviceList = (ListView) findViewById(R.id.device_list);
        deviceList.setClickable(true);
        deviceList.setOnItemClickListener((parent, view, position, id) -> selectedDeviceName = ((AppCompatTextView) view).getText().toString());
        refreshDeviceList(deviceList);
    }

}
