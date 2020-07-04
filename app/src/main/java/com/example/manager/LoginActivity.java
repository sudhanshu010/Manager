package com.example.manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class LoginActivity extends AppCompatActivity {

    Cipher cipher;
    EditText loginEmail, loginPassword;
    Button loginButton;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference serviceManReference;

    CustomDialogBox customDialogBox;
    TextView ScanBC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(LoginActivity.this,BottomNavigationActivity.class));
            finish();
        }
        customDialogBox = new CustomDialogBox(LoginActivity.this);
        customDialogBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ScanBC = findViewById(R.id.scanBC);
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);

        firebaseDatabase = FirebaseDatabase.getInstance();
        serviceManReference = firebaseDatabase.getReference("Users").child("Mechanic");

        ScanBC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, BarCodeLoginActivity.class);
                overridePendingTransition(R.anim.slide_in_left,R.anim.stay);
                startActivityForResult(i, 32);
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();
                login(email,password);

            }
        });
    }

    private void login(String email, String password) {

        customDialogBox.show();
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {

                            SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                            String token = sharedPref.getString("token", "null");
                            FirebaseDatabase.getInstance().getReference("tokens/" +
                                    mAuth.getCurrentUser().getUid()).setValue(token);

                            FirebaseUser user = mAuth.getCurrentUser();

                            user.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                                @Override
                                public void onSuccess(GetTokenResult getTokenResult) {

                                    try {
                                        boolean isManager = (boolean) getTokenResult.getClaims().get("manager");

                                        if(isManager)
                                        {
                                            customDialogBox.dismiss();
                                            Intent i = new Intent(LoginActivity.this,BottomNavigationActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                        else
                                        {
                                            customDialogBox.dismiss();
                                            Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        customDialogBox.dismiss();
                                        Toast.makeText(LoginActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });


                        } else {
                            customDialogBox.dismiss();
                            Toast.makeText(getApplicationContext(), "Some Error Occured", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==32)
        {
            if(resultCode == RESULT_OK)
            {
                String result = data.getStringExtra("Scan_result");
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

                KeyGenerator keyGenerator = null;
                try {
//                    Log.i("Result",result);
//                    keyGenerator = KeyGenerator.getInstance("AES");
//                    keyGenerator.init(128);
//                    SecretKey secretKey = keyGenerator.generateKey();
//                    cipher = Cipher.getInstance("AES");
//
//                    String decryptedText = decrypt(result, secretKey);
//                    Log.i("Decrypted Text After Decryption: " ,decryptedText);
                    String[] emailAndPassword = result.split(" ",2);
                    String email = emailAndPassword[0];
                    String password = emailAndPassword[1];
                    login(email,password);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void onLoginClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String encrypt(String plainText, SecretKey secretKey)
            throws Exception {
        byte[] plainTextByte = plainText.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByte = cipher.doFinal(plainTextByte);
        Base64.Encoder encoder = Base64.getEncoder();
        String encryptedText = encoder.encodeToString(encryptedByte);
        return encryptedText;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String decrypt(String encryptedText, SecretKey secretKey)
            throws Exception {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedText);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        String decryptedText = new String(decryptedByte);
        return decryptedText;
    }


}
