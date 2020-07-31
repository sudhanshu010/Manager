package com.example.manager;

import com.badoualy.stepperindicator.StepperIndicator;
import com.example.manager.DialogBox.BottomSheetDialog;
import com.example.manager.DialogBox.GenerateQRDialogBox;
import com.example.manager.fragments.FormFragment1;
import com.example.manager.fragments.FormFragment2;
import com.example.manager.fragments.FormFragment3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
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

import com.example.manager.models.ComparisonMachine;
import com.example.manager.models.Machine;
import com.example.manager.models.Manager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
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

import xyz.hasnat.sweettoast.SweetToast;

public class GenerateQRActivity extends AppCompatActivity {

    EditText price, serviceTime, serialNumber, typeOfMachine, machineCompany, modelNumber, machinePrice, department, scrapValue, life;// Serial Number mentioned on Machine
    TextView installationDate;
    ImageView qrcode;
    ImageView qrtext;
    Button GenerateQR;
    String generationCode; // Our code which is used for generating qrCode.
    Button save;
    OutputStream outputStream;
    long generationCodeValue = 0;
    long totalMachines;
    StepperIndicator indicator;

    String[] descriptionData = {"Basic\nDetails", "Specific\nDetails", "Commercial\nDetails"};
    private int count = 1;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference generationCodeReference, machineReference, managerReference, totalMachineReference;
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


        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextAppearance(this, R.style.TitleTextAppearance);
        final FormFragment1 fragment1 = new FormFragment1();
        setOurFragment(fragment1);

        indicator = findViewById(R.id.stepper_indicator);
        indicator.setCurrentStep(0);

        firebaseDatabase = FirebaseDatabase.getInstance();
        generationCodeReference = firebaseDatabase.getReference("GenerationCode");
        machineReference = firebaseDatabase.getReference("Machines");
        user = FirebaseAuth.getInstance().getCurrentUser();
        managerReference = firebaseDatabase.getReference("Users").child("Manager").child(user.getUid());

        totalMachineReference = firebaseDatabase.getReference("TotalMachines");
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
        totalMachineReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalMachines = (long) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
//
//        GenerateQR.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
        final FloatingActionButton nextarrow = findViewById(R.id.next_arrow);
        final FloatingActionButton prevarrow = findViewById(R.id.prev_arrow);


        nextarrow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                count++;
                if (count == 2) {

                    FormFragment1 formFragment1 = (FormFragment1) getSupportFragmentManager().findFragmentById(R.id.mainframe);
                    if (formFragment1 != null) {
                        department = formFragment1.getView().findViewById(R.id.department);
                        typeOfMachine = formFragment1.getView().findViewById(R.id.machine_type);
                    }
                    final String department1, machine_type;
                    department1 = department.getText().toString().trim();
                    machine_type = typeOfMachine.getText().toString().trim();
                    if (department1.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(v, "Enter Department", Snackbar.LENGTH_LONG);
                        TextView tv = (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/imprima.ttf");
                        tv.setTypeface(font);
                        snackbar.show();
                        department.setError("Field can't be empty");
                        department.requestFocus();
                        count--;
                    } else if (machine_type.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(v, "Enter Machine Type", Snackbar.LENGTH_LONG);
                        TextView tv = (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/imprima.ttf");
                        tv.setTypeface(font);
                        snackbar.show();
                        typeOfMachine.setError("");
                        typeOfMachine.requestFocus();
                        count--;
                    } else {
                        indicator.setCurrentStep(1);
                        indicator.animate();
//                         stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                        setOurFragment(new FormFragment2());
                        prevarrow.setVisibility(View.VISIBLE);
                    }

                } else if (count == 3) {
                    FormFragment2 formFragment2 = (FormFragment2) getSupportFragmentManager().findFragmentById(R.id.mainframe);
                    if (formFragment2 != null) {
                        serialNumber = formFragment2.getView().findViewById(R.id.serialNumber);
                        machineCompany = formFragment2.getView().findViewById(R.id.company);
                        modelNumber = formFragment2.getView().findViewById(R.id.model_number);
                        serviceTime = formFragment2.getView().findViewById(R.id.serviceTime);
                    }
                    final String serialNumber1, machineCompany1, modelNumber1, serviceTime1;
                    serialNumber1 = serialNumber.getText().toString().trim();
                    machineCompany1 = machineCompany.getText().toString().trim();
                    modelNumber1 = modelNumber.getText().toString().trim();
                    serviceTime1 = serviceTime.getText().toString().trim();
                    if (serialNumber1.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(v, "Enter Serial Number", Snackbar.LENGTH_LONG);
                        TextView tv = (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/imprima.ttf");
                        tv.setTypeface(font);
                        snackbar.show();
                        serialNumber.setError("");
                        serialNumber.requestFocus();
                        count--;
                    } else if (machineCompany1.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(v, "Enter Company", Snackbar.LENGTH_LONG);
                        TextView tv = (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/imprima.ttf");
                        tv.setTypeface(font);
                        snackbar.show();
                        machineCompany.setError("");
                        machineCompany.requestFocus();
                        count--;
                    } else if (modelNumber1.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(v, "Enter Model Number", Snackbar.LENGTH_LONG);
                        TextView tv = (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/imprima.ttf");
                        tv.setTypeface(font);
                        snackbar.show();
                        modelNumber.setError("");
                        modelNumber.requestFocus();
                        count--;
                    } else if (serviceTime1.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(v, "Enter Service Time", Snackbar.LENGTH_LONG);
                        TextView tv = (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/imprima.ttf");
                        tv.setTypeface(font);
                        snackbar.show();
                        serviceTime.setError("");
                        serviceTime.requestFocus();
                        count--;
                    } else {
//                         stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                        indicator.setCurrentStep(2);
                        setOurFragment(new FormFragment3());
                    }

                } else {

                    FormFragment3 formFragment3 = (FormFragment3) getSupportFragmentManager().findFragmentById(R.id.mainframe);
                    if (formFragment3 != null) {

                        machinePrice = Objects.requireNonNull(formFragment3.getView()).findViewById(R.id.price);
                        installationDate = formFragment3.getView().findViewById(R.id.installation_date);
                        scrapValue = Objects.requireNonNull(formFragment3.getView()).findViewById(R.id.scrap_value);
                        life = Objects.requireNonNull(formFragment3.getView()).findViewById(R.id.life);

                    }
                    final String machinePrice1, installationDate1, scrapValue1, life1;
                    machinePrice1 = machinePrice.getText().toString().trim();
                    installationDate1 = installationDate.getText().toString().trim();
                    scrapValue1 = scrapValue.getText().toString().trim();
                    life1 = life.getText().toString().trim();
                    if (installationDate1.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(v, "Enter Installation Date", Snackbar.LENGTH_LONG);
                        TextView tv = (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/imprima.ttf");
                        tv.setTypeface(font);
                        snackbar.show();
                        installationDate.setError("");
                        installationDate.requestFocus();
                        count--;
                    } else if (machinePrice1.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(v, "Enter Machine Price", Snackbar.LENGTH_LONG);
                        TextView tv = (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/imprima.ttf");
                        tv.setTypeface(font);
                        snackbar.show();
                        machinePrice.setError("");
                        machinePrice.requestFocus();
                        count--;
                    } else if (life1.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(v, "Enter Expected Life", Snackbar.LENGTH_LONG);
                        TextView tv = (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/imprima.ttf");
                        tv.setTypeface(font);
                        snackbar.show();
                        life.setError("");
                        life.requestFocus();
                        count--;
                    } else if (scrapValue1.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(v, "Enter Scrap Value", Snackbar.LENGTH_LONG);
                        TextView tv = (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/imprima.ttf");
                        tv.setTypeface(font);
                        snackbar.show();
                        scrapValue.setError("");
                        scrapValue.requestFocus();
                        count--;
                    } else {
                        if (generationCodeValue != 0) {

                            // update value to database.
                            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                            try {
                                BitMatrix bitMatrix = multiFormatWriter.encode(String.valueOf(generationCodeValue), BarcodeFormat.QR_CODE, 200, 200);
                                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);// bitmap contains QRCode image.
                                GenerateQRDialogBox generateQRDialogBox = new GenerateQRDialogBox(GenerateQRActivity.this,generationCodeValue,bitmap);
                                generateQRDialogBox.show();
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
                            SweetToast.error(GenerateQRActivity.this, "failed");
                        }




//                     save.setOnClickListener(new View.OnClickListener() {
//                         @Override
//                         public void onClick(View v) {
//                             //   saveImage();
//
//                         }
//                     });

                    }
                }
            }
        });


        prevarrow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                count--;
                if (count <= 0) {
                    finish();
                } else if (count == 1) {
//                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                    indicator.setCurrentStep(0);
                    setOurFragment(fragment1);
                    prevarrow.setVisibility(View.INVISIBLE);

                } else if (count == 2) {

//                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                    indicator.setCurrentStep(1);
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

    private void uploadQR(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        machineQRCodeRefernce = storageReference.child(generationCodeValue + ".jpg");

        UploadTask uploadTask = machineQRCodeRefernce.putBytes(data); // QRCode image has been uploaded to Storage at this line.

        addMachineToDatabase(uploadTask); // Now retrieve URL of Image and upload Machine data to Database.


    }

    //
//
    public void addMachineToDatabase(UploadTask uploadTask) {

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
                        tempManager.setCompletedComplaints(null);
                        tempManager.setPendingApprovalRequest(null);
                        tempManager.setPendingComplaints(null);
                    }
                    Machine machine = new Machine(serialNo, installationdate, dept, machineId, type, company, model,
                            Objects.requireNonNull(task.getResult()).toString(), servicetime, null, price, true, tempManager, null, scrap, machineLife,true);

                    //TODO: Check once

                    // [ ReplacementAlgo   starts -->]

                    String[] arrOfStr = installationdate.split("/", 4);
                    int date = Integer.parseInt(arrOfStr[0]);
                    int month = Integer.parseInt(arrOfStr[1]);
                    int year = Integer.parseInt(arrOfStr[2]);

                    int serviceTime = servicetime;
                    month += serviceTime;
                    month = (month + 11) % 12;
                    year += month / 12;
                    month %= 12;
                    float Tavg = 2 * price;


                    String nextServiceDate = String.valueOf(date) + '/' + month + '/' + year;
                    ComparisonMachine comparisonMachine = new ComparisonMachine(dept, type, 0, nextServiceDate, user.getUid(), Tavg, 0, 0, (int) price, serviceTime, machineId);

                    DatabaseReference reference1 = firebaseDatabase.getReference();
                    reference1.child("comparisonMachine").child(dept).child(type).child(machineId).setValue(comparisonMachine);
                    reference1.child("comparisonMachine").child(dept).child(type).child(machineId).child("OverallCost").child("0").setValue("0");

                    // [ ReplacementAlgo   end -->]


                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("/Machines/" + machineId, machine);

                    Machine tempMachine = null;
                    try {
                        tempMachine = (Machine) machine.clone();
                    } catch (Exception ignored) {

                    }
                    if (tempMachine != null) {
                        tempMachine.setManager(null);
                    }
                    hashMap.put("/Users/Manager/" + user.getUid() + "/myMachines/" + machineId, tempMachine);

                    firebaseDatabase.getReference().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                SweetToast.success(GenerateQRActivity.this, "File Updated");
                                generationCodeValue = generationCodeValue + 1; // increase Value of generationCode Everytime a new machine is entered.
                                generationCodeReference.setValue(generationCodeValue);
                                totalMachines = totalMachines+1;
                                totalMachineReference.setValue(totalMachines);
                            }
                        }
                    });

                }
            }
        });

    }

    //
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
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



