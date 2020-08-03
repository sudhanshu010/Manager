package com.example.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import com.example.manager.DialogBox.GenerateQRDialogBox;
import com.example.manager.models.ComparisonMachine;
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

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

import xyz.hasnat.sweettoast.SweetToast;

public class ImportExcelActivity extends AppCompatActivity {

    private TextView textView;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference machineQRCodeRefernce;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference generationCodeReference, machineReference, managerReference, totalMachineReference;
    FirebaseUser user;

    Manager manager;
    HashMap<String, Machine> myMachines;

    long generationCodeValue = 0;
    long totalMachines;

    String department, type, sno, company, model, servicetime, date, price, life, scrap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_excel);

        textView = findViewById(R.id.textview);

        firebaseDatabase = FirebaseDatabase.getInstance();
        generationCodeReference = firebaseDatabase.getReference("GenerationCode");
        machineReference = firebaseDatabase.getReference("Machines");
        user = FirebaseAuth.getInstance().getCurrentUser();
        managerReference = firebaseDatabase.getReference("Users").child("Manager").child(user.getUid());

        totalMachineReference = firebaseDatabase.getReference("TotalMachines");
        firebaseStorage = FirebaseStorage.getInstance();
        Log.i("1","2");
        storageReference = firebaseStorage.getReference();

        managerReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                manager = dataSnapshot.getValue(Manager.class);
                myMachines = manager != null ? manager.getMyMachines() : null;

                totalMachineReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        totalMachines = (long) dataSnapshot.getValue();
                        Log.i("3","4");
                        generationCodeReference.addValueEventListener(new ValueEventListener() {
                            @Override

                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                generationCodeValue = (long) Objects.requireNonNull(dataSnapshot.getValue());
                                Log.i("5","6");
                                readExcelFileFromAssets();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

    }

    private void uploadQR(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        machineQRCodeRefernce = storageReference.child(generationCodeValue + ".jpg");

        UploadTask uploadTask = machineQRCodeRefernce.putBytes(data); // QRCode image has been uploaded to Storage at this line.

        addMachineToDatabase(uploadTask); // Now retrieve URL of Image and upload Machine data to Database.


    }

    public void readExcelFileFromAssets() {

        Log.i("hi", "vikas");

        try {

            Log.i("11", "12");
            InputStream myInput;
            Log.i("13", "14");
            // initialize asset manager
            AssetManager assetManager = getApplicationContext().getResources().getAssets();
            Log.i("15", "16");
            //  open excel sheet
            myInput = assetManager.open("machineDetails.xls");
            Log.i("17", "18");
            // Create a POI File System object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            Log.i("7","8");

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            // We now need something to iterate through the cells.
            Iterator<Row> rowIter = mySheet.rowIterator();
            int rowno =0;
            textView.append("\n");
            while (rowIter.hasNext()) {
                Log.i("row", " row no "+ rowno );
                HSSFRow myRow = (HSSFRow) rowIter.next();
                if(rowno !=0) {
                    Iterator<Cell> cellIter = myRow.cellIterator();
                    int colno =0;

                    while (cellIter.hasNext()) {
                        HSSFCell myCell = (HSSFCell) cellIter.next();
                        if (colno==0){
                            department = myCell.toString();
                            Log.i("9","10");
                        }else if (colno==1){
                            type = myCell.toString();
                        }else if (colno==2){
                            sno = myCell.toString();
                        }
                        else if (colno==3){
                            company = myCell.toString();
                        }
                        else if (colno==4){
                            model = myCell.toString();
                        }
                        else if (colno==5){
                            servicetime = myCell.toString();
                        }
                        else if (colno==6){
                            date = myCell.toString();
                        }
                        else if (colno==7){
                            price = myCell.toString();
                        }
                        else if (colno==8){
                            life = myCell.toString();
                        }
                        else if (colno==9){
                            scrap = myCell.toString();
                        }
                        colno++;

                    }
                    if (generationCodeValue != 0) {

                        // update value to database.
                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                        try {
                            BitMatrix bitMatrix = multiFormatWriter.encode(String.valueOf(generationCodeValue), BarcodeFormat.QR_CODE, 200, 200);
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);// bitmap contains QRCode image.

                            FileOutputStream outStream = null;
                            File sdCard = Environment.getExternalStorageDirectory();
                            File dir = new File(sdCard.getAbsolutePath() + "/QRcode");
                            dir.mkdirs();
                            File outFile = new File(dir, generationCodeValue +".jpg");
                            try {
                                outStream = new FileOutputStream(outFile);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                            //Toast.makeText(this, "Image Saved Successfully", Toast.LENGTH_SHORT).show();
                            try {
                                assert outStream != null;
                                outStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                outStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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
                        SweetToast.error(ImportExcelActivity.this, "failed");
                    }
                    textView.append( sno + " -- "+ date+ "  -- "+ company+"\n");


                }
                rowno++;
            }

            generationCodeReference.setValue(generationCodeValue);
            totalMachineReference.setValue(totalMachines);

        } catch (Exception e) {
            Log.i("error", "error "+ e.toString());
        }
    }

    public void addMachineToDatabase(UploadTask uploadTask) {


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
                    Machine machine = new Machine(sno, date, department, machineId, type, company, model,
                            Objects.requireNonNull(task.getResult()).toString(), (int) Float.parseFloat(servicetime), null, Float.parseFloat(price), true, tempManager, null, Float.parseFloat(scrap), Float.parseFloat(life),true);

                    //TODO: Check once

                    // [ ReplacementAlgo   starts -->]

                    String[] arrOfStr = date.split("/", 4);
                    int date = Integer.parseInt(arrOfStr[0]);
                    int month = Integer.parseInt(arrOfStr[1]);
                    int year = Integer.parseInt(arrOfStr[2]);

                    int serviceTime = Integer.parseInt(servicetime);
                    month += serviceTime;
                    month = (month + 11) % 12;
                    year += month / 12;
                    month %= 12;
                    float Tavg = 2 * Float.parseFloat(price);


                    String nextServiceDate = String.valueOf(date) + '/' + month + '/' + year;
                    ComparisonMachine comparisonMachine = new ComparisonMachine(department, type, 0, nextServiceDate, user.getUid(), Tavg, 0, 0, (int) Float.parseFloat(price), serviceTime, machineId);

                    DatabaseReference reference1 = firebaseDatabase.getReference();
                    reference1.child("comparisonMachine").child(department).child(type).child(machineId).setValue(comparisonMachine);
                    reference1.child("comparisonMachine").child(department).child(type).child(machineId).child("OverallCost").child("0").setValue("0");

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
                            generationCodeValue = generationCodeValue + 1;
                            totalMachines = totalMachines+1;
                        }
                    });


                }
            }
        });

    }

}