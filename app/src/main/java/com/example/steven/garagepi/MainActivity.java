package com.example.steven.garagepi;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.icu.util.Output;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private List<String> deviceList = new ArrayList<>();
    private String deviceKey = "";
    private UUID uuid = UUID.fromString("9d298d8d-06b4-4da5-b913-0440aa7b4c70");

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

    private Optional<BluetoothDevice> getBluetoothDeviceFromName(String name) {
        for (BluetoothDevice bDevice : bluetoothAdapter.getBondedDevices()) {
            if (bDevice.getName().equals(name)) {
                return Optional.of(bDevice);
            }
        }
        return Optional.empty();
    }

    private void sendGarageToggleRequest(BluetoothDevice device) {
        try {
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
            socket.connect();
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            byte[] challenge = new byte[29];
            if (inputStream.read(challenge, 0, 29) != 29) {
                System.err.println("Fragmented challenge");
            }
            socket.close();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
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

    private String readKey() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkForBluetoothAdapter();

        deviceKey = readKey();

        final View refreshButton = findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(this::refreshDeviceList);

        final ListView deviceListView = (ListView) findViewById(R.id.device_list);
        deviceListView.setClickable(true);
        ArrayAdapter<String> deviceListViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceList);
        deviceListView.setAdapter(deviceListViewAdapter);
        deviceListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedDeviceName = ((AppCompatTextView) view).getText().toString();
            Optional<BluetoothDevice> selectedDevice = getBluetoothDeviceFromName(selectedDeviceName);
            selectedDevice.ifPresent(this::sendGarageToggleRequest);
        });

        refreshDeviceList(deviceListView);
    }

}
