package com.example.manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class ServiceStopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_stop);
        Toast.makeText(getApplicationContext(), "You are Logged Out!", Toast.LENGTH_SHORT).show();
        //getApplicationContext().stopService(new Intent(ServiceStopActivity.this,BackgroundService.class));

    }
}