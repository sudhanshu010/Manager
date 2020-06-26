package com.example.manager;

import com.example.manager.DialogBox.BottomSheetDialog;
import com.example.manager.fragments.FormFragment1;
import com.example.manager.fragments.FormFragment2;
import com.example.manager.fragments.FormFragment3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manager.models.Machine;
import com.example.manager.models.Manager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.crypto.Mac;

public class GenerateQRActivity extends AppCompatActivity {

    EditText price, serviceTime, serialNumber, typeOfMachine, machineCompany, modelNumber, machinePrice,department, scrapValue, life;// Serial Number mentioned on Machine
    TextView installationDate;
    ImageView qrcode;
    ImageView qrtext;
    Button GenerateQR;
    String generationCode; // Our code which is used for generating qrCode.
    Button save;
    OutputStream outputStream;
    long generationCodeValue = 0;

    String[] descriptionData = {"Step 1", "Step 2", "Step 3"};
    private int count = 1;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference generationCodeReference, machineReference, managerReference;
    FirebaseUser user;

    String generatorName;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference machineQRCodeRefernce;

    LinearLayout linearLayoutimage, aqwesd, linearLayout;
    TextView enter_details;

    Manager manager;
    HashMap<String, Machine> myMachines;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);

        final FormFragment1 fragment1 = new FormFragment1();
        setOurFragment(fragment1);
        final StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.your_stare_progress_id_bar);
        stateProgressBar.setStateDescriptionData(descriptionData);

//        serialNumber = findViewById(R.id.serialNumber);
//
//        serviceTime = findViewById(R.id.serviceTime);
//        installationDate = findViewById(R.id.installation_date);
//        typeOfMachine = findViewById(R.id.machine_type);
//        machineCompany = findViewById(R.id.company);
//        modelNumber = findViewById(R.id.model_number);
//        machinePrice = findViewById(R.id.price);


//        qrcode = findViewById(R.id.qrcode);
//        save = findViewById(R.id.save);
//        GenerateQR = findViewById(R.id.generateQRButton);
//        linearLayout = findViewById(R.id.linearlayout);
//        linearLayoutimage = findViewById(R.id.linearlayoutimage);
//        aqwesd = findViewById(R.id.aqwesd);
//        enter_details = findViewById(R.id.enter_details_text);

        firebaseDatabase  = FirebaseDatabase.getInstance();
        generationCodeReference = firebaseDatabase.getReference("GenerationCode");
        machineReference = firebaseDatabase.getReference("Machines");
        user = FirebaseAuth.getInstance().getCurrentUser();
        managerReference = firebaseDatabase.getReference("Users").child("Manager").child(user.getUid());
//
        managerReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                manager = dataSnapshot.getValue(Manager.class);
                myMachines = manager != null ? manager.getMyMachines() : null;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //qrtext= findViewById(R.id.imageviewqr);


        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
//
//        save.setVisibility(View.INVISIBLE);
//        linearLayoutimage.setVisibility(View.INVISIBLE);
//
        //Get initial value of Code Generator on app startup
        generationCodeReference.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                generationCodeValue = (long) Objects.requireNonNull(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//
        //Permission
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
//
//        GenerateQR.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
        final ImageView nextarrow = (ImageView) findViewById(R.id.next_arrow);
        final ImageView prevarrow = (ImageView) findViewById(R.id.prev_arrow);


        nextarrow.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 count++;
                 if (count == 2) {

                     FormFragment1 formFragment1 = (FormFragment1) getSupportFragmentManager().findFragmentById(R.id.mainframe);
                     if (formFragment1 != null) {
                         department = formFragment1.getView().findViewById(R.id.department);
                         typeOfMachine = formFragment1.getView().findViewById(R.id.machine_type);
                     }

                     stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                     setOurFragment(new FormFragment2());
                     prevarrow.setVisibility(View.VISIBLE);

                 } else if (count == 3) {
                     FormFragment2 formFragment2 = (FormFragment2) getSupportFragmentManager().findFragmentById(R.id.mainframe);
                     if (formFragment2 != null) {
                         serialNumber = formFragment2.getView().findViewById(R.id.serialNumber);
                         machineCompany = formFragment2.getView().findViewById(R.id.company);
                         modelNumber = formFragment2.getView().findViewById(R.id.model_number);
                         serviceTime = formFragment2.getView().findViewById(R.id.serviceTime);
                     }

                     stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                     setOurFragment(new FormFragment3());

                 } else {

                     FormFragment3 formFragment3 = (FormFragment3) getSupportFragmentManager().findFragmentById(R.id.mainframe);
                     if (formFragment3 != null) {
                         machinePrice = Objects.requireNonNull(formFragment3.getView()).findViewById(R.id.price);
                         installationDate = formFragment3.getView().findViewById(R.id.installation_date);
                         scrapValue = Objects.requireNonNull(formFragment3.getView()).findViewById(R.id.scrap_value);
                         life = Objects.requireNonNull(formFragment3.getView()).findViewById(R.id.life);
                     }

                     if (generationCodeValue != 0) {

                         // update value to database.
                         MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                         try {
                             BitMatrix bitMatrix = multiFormatWriter.encode(String.valueOf(generationCodeValue), BarcodeFormat.QR_CODE, 200, 200);
                             BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                             Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);// bitmap contains QRCode image.
                             final BottomSheetDialog bottomSheet = new BottomSheetDialog(generationCodeValue,bitmap);
                             bottomSheet.show(getSupportFragmentManager(),bottomSheet.getTag());

                             //qrcode.setImageBitmap(bitmap);

                             uploadQR(bitmap); // upload QRcode image to FirebaseStorage
//                             save.setVisibility(View.VISIBLE);
//                             GenerateQR.setVisibility(View.INVISIBLE);
//                             aqwesd.setVisibility(View.INVISIBLE);
//                             linearLayoutimage.setVisibility(View.VISIBLE);
//                             enter_details.setText("QR Code");
//                             qrtext.setImageResource(R.drawable.ic_qr_code);
//                             linearLayout.setVisibility(View.INVISIBLE);

                         } catch (WriterException e) {
                             e.printStackTrace();
                         }
                     } else {
                         Toast.makeText(GenerateQRActivity.this, "failed", Toast.LENGTH_SHORT).show();
                     }


                     installationDate.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {
                             Calendar cal = Calendar.getInstance();
                             int year = cal.get(Calendar.YEAR);
                             int month = cal.get(Calendar.MONTH);
                             int day = cal.get(Calendar.DAY_OF_MONTH);

                             DatePickerDialog dialog = new DatePickerDialog(
                                     GenerateQRActivity.this,
                                     //android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                     mDateSetListener,
                                     year, month, day);
                             // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                             dialog.show();
                         }
                     });

                     mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                         @Override
                         public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                             month = month + 1;
                             String date = day + "/" + month + "/" + year;
                             installationDate.setText(date);
                         }
                     };

//                     save.setOnClickListener(new View.OnClickListener() {
//                         @Override
//                         public void onClick(View v) {
//                             //   saveImage();
//
//                         }
//                     });


                 }
             }
         });



        prevarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                if(count<=0)
                {
                    finish();
                }
                else if (count == 1) {
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                    setOurFragment(fragment1);
                    prevarrow.setVisibility(View.INVISIBLE);

                } else if (count == 2) {

                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                    setOurFragment(new FormFragment2());
                } else {

                    finish();
                }
            }
        });
    }

    private void setOurFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainframe, fragment);
        fragmentTransaction.commit();
    }

    private void uploadQR(Bitmap bitmap){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        machineQRCodeRefernce = storageReference.child(generationCodeValue+".jpg");

        UploadTask uploadTask = machineQRCodeRefernce.putBytes(data); // QRCode image has been uploaded to Storage at this line.

        addMachineToDatabase(uploadTask); // Now retrieve URL of Image and upload Machine data to Database.


    }
//
//
    public void addMachineToDatabase(UploadTask uploadTask)
    {

        final String serialNo, dept, type, model, company;
        final float price, scrap, machineLife;
        final int servicetime;
        final String installationdate;



        // Retrieve Data of Machine to be saved.
        serialNo = serialNumber.getText().toString();
        dept = department.getText().toString();
        servicetime = Integer.parseInt(serviceTime.getText().toString());
        installationdate = installationDate.getText().toString();
        type = typeOfMachine.getText().toString();
        model = modelNumber.getText().toString();
        company = machineCompany.getText().toString();
        price = Float.parseFloat(machinePrice.getText().toString());
        scrap = Float.parseFloat(scrapValue.getText().toString());
        machineLife = Float.parseFloat(life.getText().toString());


        // QRCode image url is fetched and on Completion Machine Data is uploaded to database.
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }

                // Continue with the task to get the download URL
                return machineQRCodeRefernce.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {


                    String machineId = String.valueOf(generationCodeValue);
                    Manager tempManager = null;
                    try {
                        tempManager = (Manager) manager.clone();
                    } catch (Exception ignored) {

                    }
                    if (tempManager != null) {
                        tempManager.setMyMachines(null);
                    }
                    Machine machine = new Machine(serialNo, installationdate, dept, machineId, type, company, model,
                            Objects.requireNonNull(task.getResult()).toString(), servicetime, null, price, true, tempManager, null, scrap, machineLife);

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("/Machines/" + machineId, machine);

                    Machine tempMachine=null;
                    try {
                        tempMachine = (Machine) machine.clone();
                    } catch (Exception ignored) {

                    }
                    if (tempMachine != null) {
                        tempMachine.setManager(null);
                    }
                    hashMap.put("/Users/Manager/"+user.getUid()+"/myMachines/"+machineId,tempMachine);

                    firebaseDatabase.getReference().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(GenerateQRActivity.this, "File Updated", Toast.LENGTH_SHORT).show();
                                generationCodeValue = generationCodeValue+1; // increase Value of generationCode Everytime a new machine is entered.
                                generationCodeReference.setValue(generationCodeValue);
                            }
                        }
                    });

                }
            }
        });

    }
//

}











































// Some extra code to use further
// 1. Dialog box for QR code
//Starting of dialog box
/*
    public void myAlertbox()
    {
        MyDialog = new Dialog(GenerateQRActivity.this);
        MyDialog.setContentView(R.layout.loading_dialog);
        MyDialog.setTitle("QR CODE");
        save = (Button)findViewById(R.id.dialogSave);
        exit = (Button)findViewById(R.id.dialogExit);
        ImageinDialog = (ImageView)findViewById(R.id.ImageInDialog);
        ImageinDialog.setImageDrawable(getResources().getDrawable(R.drawable.bhj));

        save.setEnabled(true);
        exit.setEnabled(true);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GenerateQRActivity.this, "Save your image here", Toast.LENGTH_SHORT).show();
            }
        });

    }
    *///------>Ending



