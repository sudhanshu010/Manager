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
import com.example.manager.utilityclass.CaesarCipherUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import javax.crypto.Cipher;

import xyz.hasnat.sweettoast.SweetToast;

public class LoginActivity extends AppCompatActivity {

    String privateKey;

    Cipher cipher;
    EditText loginEmail, loginPassword;
    Button loginButton;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    FirebaseDatabase firebaseDatabase;


    CustomDialogBox customDialogBox;
    TextView ScanBC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);

       // FirebaseDatabase.getInstance().setPersistenceEnabled(true);

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

                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    String newToken = instanceIdResult.getToken();
                                    FirebaseDatabase.getInstance().getReference("tokens/" +
                                            mAuth.getCurrentUser().getUid()).setValue(newToken);

                                }
                            });

                            FirebaseUser user = mAuth.getCurrentUser();

                            user.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                                @Override
                                public void onSuccess(GetTokenResult getTokenResult) {

                                    try {
                                        boolean isManager = (boolean) getTokenResult.getClaims().get("manager");

                                        if(isManager)
                                        {
                                            customDialogBox.dismiss();
                                            SweetToast.info(LoginActivity.this,"Success");
                                            Intent i = new Intent(LoginActivity.this,BottomNavigationActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                        else
                                        {
                                            customDialogBox.dismiss();
                                            SweetToast.error(LoginActivity.this, "error");
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        customDialogBox.dismiss();
                                        SweetToast.error(LoginActivity.this, "Error Occured");

                                    }

                                }
                            });


                        } else {
                            customDialogBox.dismiss();
                            SweetToast.error(getApplicationContext(), "Some Error Occured");

                        }
                    }
                });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==32)
        {
            if(resultCode == RESULT_OK)
            {
                assert data != null;
                final String result = data.getStringExtra("Scan_result");

                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

                assert result != null;
                String decrypted = CaesarCipherUtil.decode(result);

                String[] emailAndPassword = decrypted.split("-",2);

                String email = emailAndPassword[0];
                String password = emailAndPassword[1];
                login(email,password);

            }
        }
    }

    public void onLoginClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
        finish();
    }


}