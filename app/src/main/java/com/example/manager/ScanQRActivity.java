package com.example.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import static android.Manifest.permission.CAMERA;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.otaliastudios.cameraview.CameraView;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import xyz.hasnat.sweettoast.SweetToast;

public class ScanQRActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    boolean isDetected = false;
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private Button scanBtn;
    ImageButton flashlight1 ;
    int flash_ON;
    DatabaseReference reference1;
    CustomDialogBox customDialogBox;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        scannerView = findViewById(R.id.ScannerFrontend);
        flashlight1 = findViewById(R.id.flashlight1);
        flash_ON = 0;

        customDialogBox = new CustomDialogBox(ScanQRActivity.this);
        customDialogBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        reference1 = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void handleResult(Result result) {
        final String scanResult = result.getText();

        customDialogBox.show();

        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Machines").child(scanResult).exists())
                {
                    scannerView.stopCamera();
                    SweetToast.success(getApplicationContext(),"Success");
                    Intent i = new Intent(ScanQRActivity.this, GetMachineDetailsActivity.class);
                    i.putExtra("generationCode",scanResult);
                    startActivity(i);
                    customDialogBox.dismiss();
                    finish();
                }
                else
                {
                    SweetToast.error(getApplicationContext(),"QR doesn't belong to AAI");
                    onResume();
                    customDialogBox.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }



    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(ScanQRActivity.this, CAMERA)== PackageManager.PERMISSION_GRANTED);
    }

    public void requestPermission()
    {
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
                scannerView.setResultHandler(ScanQRActivity.this);
                scannerView.startCamera();
            }
            else
            {
                requestPermission();
            }
        }
    }




    public void displayAlertMessage(String message, DialogInterface.OnClickListener Listener)
    {

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