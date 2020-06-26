package com.example.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manager.models.Manager;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    Button registerButton;
    EditText registerName, registerEmail, registerPassword;

    FirebaseAuth auth;
    FirebaseUser user;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference userReference;

    FirebaseFunctions firebaseFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton  = findViewById(R.id.registerButton);
        registerName = findViewById(R.id.editTextName);
        registerEmail = findViewById(R.id.editTextEmail);
        registerPassword = findViewById(R.id.editTextPassword);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseFunctions = FirebaseFunctions.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userName = registerName.getText().toString();
                final String email = registerEmail.getText().toString();
                String password = registerPassword.getText().toString();

                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            user = auth.getCurrentUser();
                            userReference = firebaseDatabase.getReference("Users");

                            HashMap<String,String> data = new HashMap<>();

                            data.put("claim","manager");
                            data.put("email",user.getEmail());

                            firebaseFunctions.getHttpsCallable("setCustomClaim")
                                    .call(data)
                                    .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                                        @Override
                                        public void onSuccess(HttpsCallableResult httpsCallableResult) {

                                            user = auth.getCurrentUser();
                                            HashMap<String,String> hashMap = (HashMap<String, String>) httpsCallableResult.getData();
                                            if(hashMap.get("status").equals("Successful"))
                                            {
                                                Manager manager = new Manager();
                                                manager.setEmail(email);
                                                manager.setUserName(userName);
                                                manager.setUid(user.getUid());

                                                userReference.child("Manager").child(user.getUid()).setValue(manager);
                                                startActivity(new Intent(getApplicationContext(), BottomNavigationActivity.class));
                                                finish();
                                            }
                                            else
                                            {
                                                user.delete();
                                                Toast.makeText(RegisterActivity.this, "Some Error Occured \n Please try again", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                        }

                    }
                });





            }
        });
    }
    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);

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
}
