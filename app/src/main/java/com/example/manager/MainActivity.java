package com.example.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.example.manager.DialogBox.ComplaintDescriptionDialog;
import com.example.manager.models.Complaint;

public class MainActivity extends AppCompatActivity {
    TextView phoneTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        phoneTV = findViewById(R.id.phone_tv);
//        if (Build.VERSION.SDK_INT > 22) {
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
//
//                return;
//            }
//            Intent callIntent = new Intent(Intent.ACTION_CALL);
//            callIntent.setData(Uri.parse("tel:+" + phoneTV.getText().toString().trim()));
//            startActivity(callIntent);
//        } else {
//
//            Intent callIntent = new Intent(Intent.ACTION_CALL);
//            callIntent.setData(Uri.parse("tel:+" + phoneTV.getText().toString().trim()));
//            startActivity(callIntent);
//        }
    }
}
