package com.example.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.manager.DialogBox.CustomDialogBox;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class BarCodeLogin extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    boolean isDetected = false;
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private Button scanBtn;
    ImageButton flashlight1 ;
    int flash_ON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_login);

        scannerView = findViewById(R.id.ScannerFrontend);
        flashlight1 = findViewById(R.id.flashlight1);
        flash_ON = 0;


    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(BarCodeLogin.this, CAMERA)== PackageManager.PERMISSION_GRANTED);
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA},REQUEST_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode)
        {
            case REQUEST_CAMERA:
                if(grantResults.length>0) {
                    boolean CameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (CameraAccepted)
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                displayAlertMessage("You need to allow for both permission",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int i) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                                    requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                            }
                                        });
                                return ;
                            }
                        }
                    }
                }
                break;

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            if(checkPermission())
            {
                if(scannerView==null)
                {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(BarCodeLogin.this);
                scannerView.startCamera();
            }
            else
            {
                requestPermission();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }


    public void displayAlertMessage(String message, DialogInterface.OnClickListener Listener)
    {

    }

    @Override
    public void handleResult(Result result) {
        String scanResult = result.getText();


        Intent data = new Intent();
        data.putExtra("Scan_result", scanResult);
        setResult(RESULT_OK, data);
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);;
        finish();
    }


    public void FlashLight(View v)
    {
        if(flash_ON==0)
        {
            flash_ON =1;
            flashlight1.setImageResource(R.drawable.ic_flash_off_black_24dp);
            scannerView.setFlash(true);
            //Make flash on
        }
        else
        {
            flash_ON = 0;
            flashlight1.setImageResource(R.drawable.ic_flash_on_black_24dp);
            scannerView.setFlash(false);
        }
    }

}
