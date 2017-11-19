package com.example.steven.garagepi;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String selectedDeviceName = null;
    private List<String> deviceList = new ArrayList<>();

    private int BLUETOOTH_ENABLE_REQUEST = 1;

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private void goToMainScreen() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    private void checkForBluetoothAdapter() {
        if (bluetoothAdapter == null) {
            System.err.println("No bluetooth adapter detected");
            goToMainScreen();
        }
        else if (!bluetoothAdapter.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, BLUETOOTH_ENABLE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BLUETOOTH_ENABLE_REQUEST) {
            if (resultCode == RESULT_CANCELED) {
                System.err.println("Need bluetooth to function");
                goToMainScreen();
            }
        }
    }

    private void sendGarageToggleRequest(View v) {
    }

    private List<String> getNearbyCompatibleDevices() {
        List<String> names = new ArrayList<>();
        for (BluetoothDevice bDevice : bluetoothAdapter.getBondedDevices()) {
            names.add(bDevice.getName());
        }
        return names;
    }

    public void refreshDeviceList(View v) {
        deviceList.clear();
        deviceList.addAll(getNearbyCompatibleDevices());
        final ListView deviceListView = (ListView) findViewById(R.id.device_list);
        final BaseAdapter deviceListViewAdapter = (BaseAdapter) deviceListView.getAdapter();
        deviceListViewAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkForBluetoothAdapter();

        final View onOffButton = findViewById(R.id.open_close_button);
        onOffButton.setOnClickListener(this::sendGarageToggleRequest);

        final View refreshButton = findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(this::refreshDeviceList);

        final ListView deviceListView = (ListView) findViewById(R.id.device_list);
        deviceListView.setClickable(true);
        ArrayAdapter<String> deviceListViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceList);
        deviceListView.setAdapter(deviceListViewAdapter);
        deviceListView.setOnItemClickListener((parent, view, position, id) -> selectedDeviceName = ((AppCompatTextView) view).getText().toString());

        refreshDeviceList(deviceListView);
    }

}
