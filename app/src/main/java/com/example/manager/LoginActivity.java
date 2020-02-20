package com.example.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manager.DialogBox.CustomDialogBox;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    Button loginButton;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference serviceManReference;

    CustomDialogBox customDialogBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        customDialogBox = new CustomDialogBox(LoginActivity.this);
        customDialogBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);

        firebaseDatabase = FirebaseDatabase.getInstance();
        serviceManReference = firebaseDatabase.getReference("Users").child("ServiceMan");

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();
                customDialogBox.show();

                login(email,password);

            }
        });
    }

    private void login(String email, String password) {

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {

                            SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                            String token = sharedPref.getString("token", "null");
                            FirebaseDatabase.getInstance().getReference("tokens/" +
                                    mAuth.getCurrentUser().getUid()).setValue(token);

                            serviceManReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists())
                                    {
                                        customDialogBox.dismiss();
                                        Intent i = new Intent(LoginActivity.this,BottomNavigationActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                    else
                                    {
                                        customDialogBox.dismiss();
                                        startActivity(new Intent(getApplicationContext(), BottomNavigationActivity.class));
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        } else {
                            customDialogBox.dismiss();
                            Toast.makeText(getApplicationContext(), "Some Error Occured", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    public void onLoginClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
        finish();

    }

}
