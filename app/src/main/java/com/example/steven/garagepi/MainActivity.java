package com.example.steven.garagepi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public void sendGarageToggleRequest(View V) {
    }
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View button = findViewById(R.id.button);
        button.setOnClickListener(this::sendGarageToggleRequest);


        final ListView deviceList = (ListView) findViewById(R.id.list);
        final List<String> backingList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            backingList.add(Integer.toString(i));
        }
        ArrayAdapter<String> deviceListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, backingList);
        deviceList.setAdapter(deviceListAdapter);
    }

}
