package com.example.manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GenerateQrOptions extends AppCompatActivity {

    Button excel, manual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr_options);

        excel = findViewById(R.id.import_button);
        manual = findViewById(R.id.button);

        excel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ImportExcelActivity.class);
                startActivity(i);
            }
        });

        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GenerateQrOptions.class);
                startActivity(intent);
            }
        });
    }
}