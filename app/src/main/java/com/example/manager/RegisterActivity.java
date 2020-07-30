package com.example.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manager.DialogBox.CustomDialogBox;
import com.example.manager.fragments.ProfileFragment;
import com.example.manager.models.Manager;
import com.github.aakira.expandablelayout.ExpandableWeightLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.schibstedspain.leku.LocationPicker;
import com.schibstedspain.leku.LocationPickerActivity;
import com.schibstedspain.leku.tracker.LocationPickerTracker;
import com.schibstedspain.leku.tracker.TrackEvents;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import kotlin.jvm.internal.Intrinsics;
import xyz.hasnat.sweettoast.SweetToast;

public class RegisterActivity extends AppCompatActivity {

    Button registerButton;
    EditText registerName, registerEmail, registerPassword, registerPhone, registerEmpId, registerDepartment, registerDesignation;
    TextView textAddress, attachFile;

    double latitude;
    double longitude;
    String address;

    FirebaseAuth auth;
    FirebaseUser user;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference userReference;
    FirebaseFunctions firebaseFunctions;
    ImageView image, image1;

    byte[] documentImage = null;
    UploadTask uploadTask;
    String imageLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = findViewById(R.id.registerButton1);
        registerName = findViewById(R.id.editTextName);
        registerPhone = findViewById(R.id.editTextPhone);
        registerEmail = findViewById(R.id.editTextEmail);
        registerPassword = findViewById(R.id.editTextPassword);
        registerEmpId = findViewById(R.id.editTextName1);
        registerDepartment = findViewById(R.id.editTextEmail1);
        registerDesignation = findViewById(R.id.editTextPassword1);
        textAddress = findViewById(R.id.editTextAddress);
        image = findViewById(R.id.image);
        image1 = findViewById(R.id.image1);
        attachFile = findViewById(R.id.attach_file);

        final ExpandableWeightLayout expandableLayout = (ExpandableWeightLayout) findViewById(R.id.expandableLayout);
        final ExpandableWeightLayout expandableLayout1 = (ExpandableWeightLayout) findViewById(R.id.expandableLayout1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!expandableLayout.isExpanded()) {
                    image.animate().rotationBy(180).setDuration(200).start();
                    expandableLayout.toggle();
                    if (expandableLayout1.isExpanded()) {
                        expandableLayout1.collapse();
                        image1.animate().rotationBy(180).start();
                    }
                }
            }
        }, 1000);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.animate().rotationBy(180).setDuration(200).start();
                expandableLayout.toggle();
                if (expandableLayout1.isExpanded()) {
                    expandableLayout1.collapse();
                    image1.animate().rotationBy(180).start();
                }
            }
        });

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image1.animate().rotationBy(180).start();
                expandableLayout1.toggle();
                if (expandableLayout.isExpanded()) {
                    expandableLayout.collapse();
                    image.animate().rotationBy(180).setDuration(200).start();
                }
            }
        });

        // front end done till here----------------------------------------------------------------------------------------------

        // call for picking ID proof from external file
        attachFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 12);
            }
        });

        //Address Picker
        textAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent locationPickerIntent = new LocationPickerActivity.Builder().withLocation(41.4036299D, 2.1743558D)
                        .withGooglePlacesApiKey("AIzaSyDdFsWe5_FaQVqtUFa4JeH_xGEvc7Uwjcc")
                        .withGeolocApiKey("AIzaSyDdFsWe5_FaQVqtUFa4JeH_xGEvc7Uwjcc")
                        .withDefaultLocaleSearchZone()
                        .withGoogleTimeZoneEnabled()
                        .withUnnamedRoadHidden()
                        .withMapStyle(R.raw.map_style_retro)
                        .build(RegisterActivity.this);
                Context var10001 = RegisterActivity.this.getApplicationContext();
                Intrinsics.checkExpressionValueIsNotNull(var10001, "applicationContext");

                locationPickerIntent.putExtra("test", "this is a test");
                RegisterActivity.this.startActivityForResult(locationPickerIntent, 1);


            }
        });
        this.initializeLocationPickerTracker();



        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseFunctions = FirebaseFunctions.getInstance();


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomDialogBox dialogBox = new CustomDialogBox(RegisterActivity.this);
                dialogBox.show();

                final String userName = registerName.getText().toString();
                final String phone = registerPhone.getText().toString();
                final String email = registerEmail.getText().toString();
                final String password = registerPassword.getText().toString();
                final String empId = registerEmpId.getText().toString();
                final String department = registerDepartment.getText().toString();
                final String designation = registerDesignation.getText().toString();

                StorageReference storageReference = FirebaseStorage.getInstance().getReference("/idProof/"+registerEmpId.getText().toString());
                uploadTask = storageReference.putBytes(documentImage);

                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageLink = uri.toString();
                                Manager manager = new Manager(email, userName, null, phone, address, null, null, null, null, null, password, empId, department, designation, String.valueOf(longitude), String.valueOf(latitude),imageLink);

                                userReference = firebaseDatabase.getReference("UnverifiedAccounts");
                //                HashMap<String, Object> hashMap = new HashMap<>();
                //                hashMap.put("/UnverifiedAccounts/Manager/" + email, manager);
                //                FirebaseDatabase.getInstance().getReference().updateChildren(hashMap);

                                userReference.child("Manager").child(empId).setValue(manager);
                                dialogBox.dismiss();
                                SweetToast.success(RegisterActivity.this, "wait for verification");


                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialogBox.dismiss();
                    }
                });


            }
        });
    }

    public void onLoginClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);

        changeStatusBarColor();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    //Map Result ..
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Log.i("RESULT****", "OK");

            StringBuilder stringBuilder = new StringBuilder();
            if (requestCode == 1) {
                latitude = data.getDoubleExtra("latitude", 0.0D);
                Log.i("LATITUDE****", String.valueOf(latitude));
                stringBuilder.append(latitude);
                stringBuilder.append(",");
                longitude = data.getDoubleExtra("longitude", 0.0D);
                stringBuilder.append(longitude);
                stringBuilder.append(",");
                Log.i("LONGITUDE****", String.valueOf(longitude));
                address = data.getStringExtra("location_address");
                if (address != null) {
                    Log.i("ADDRESS****", address);
                }

                stringBuilder.append(address);
//                stringBuilder.append(",");

//                String postalcode = data.getStringExtra("zipcode");
//                Log.i("POSTALCODE****", postalcode.toString());
//                stringBuilder.append(postalcode);
//                stringBuilder.append(",");
//
//                Bundle bundle = data.getBundleExtra("transition_bundle");
//                Log.i("BUNDLE TEXT****", bundle.getString("test"));
//                Address fullAddress = (Address)data.getParcelableExtra("address");
//                if (fullAddress != null) {
//                    Log.e("FULL ADDRESS****", fullAddress.toString());
//                    stringBuilder.append(fullAddress);
//                    stringBuilder.append(",");
//                }
//
//                String timeZoneId = data.getStringExtra("time_zone_id");
//                if (timeZoneId != null) {
//                    Log.i("TIME ZONE ID****", timeZoneId);
//                }
//
//                String timeZoneDisplayName = data.getStringExtra("time_zone_display_name");
//                if (timeZoneDisplayName != null) {
//                    Log.i("TIME ZONE NAME****", timeZoneDisplayName);
//                }

            }
            textAddress.setText(stringBuilder.toString());
        }

        if (requestCode == 12) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Log.i("PRofile", "helo1");

                //dialogBox.show();

                Uri imageUri = data.getData();
                try {
                    ContentResolver cr = getBaseContext().getContentResolver();
                    InputStream inputStream = cr.openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    documentImage = baos.toByteArray();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

            if (resultCode == 0) {
                Log.d("RESULT****", "CANCELLED");
            }
        }

        private final void initializeLocationPickerTracker () {
            LocationPicker.INSTANCE.setTracker((LocationPickerTracker) (new RegisterActivity.MyPickerTracker((Context) this)));
        }


        private static final class MyPickerTracker implements LocationPickerTracker {
            private final Context context;

            public void onEventTracked(@NotNull TrackEvents event) {
                Intrinsics.checkParameterIsNotNull(event, "event");
                Toast.makeText(this.context, (CharSequence) ("Event: " + event.getEventName()), Toast.LENGTH_SHORT).show();
            }

            public MyPickerTracker(@NotNull Context context) {
                super();
                Intrinsics.checkParameterIsNotNull(context, "context");
                this.context = context;
            }
        }
    }
