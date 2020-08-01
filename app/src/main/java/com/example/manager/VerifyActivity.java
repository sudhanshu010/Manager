package com.example.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manager.models.Manager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import xyz.hasnat.sweettoast.SweetToast;

public class VerifyActivity extends AppCompatActivity  {

    FirebaseAuth mAuth;
    FirebaseUser user;
    OtpTextView otpTextView;
    private ProgressDialog progress;
    DatabaseReference databaseReference;
    private String codesent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        mAuth = FirebaseAuth.getInstance();

        otpTextView = findViewById(R.id.otp_view);
        otpTextView.requestFocusOTP();
        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox

            }
            @Override
            public void onOTPComplete(String otp) {
                // fired when user has entered the OTP fully.
                Toast.makeText(VerifyActivity.this, "The OTP is " + otp,  Toast.LENGTH_SHORT).show();
            }
        });

        progress = new ProgressDialog(this);
        progress.setMessage("Verifying Otp..");
        progress.setCancelable(true);
        user = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child("Manager")
                .child(user.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Manager manager = dataSnapshot.getValue(Manager.class);
                String phone = "";
                if (manager != null) {
                    phone = manager.getPhoneNumber();
                }
                if(!phone.isEmpty()) {
                    sendVerificationCode(phone);
                }else{
                    SweetToast.error(VerifyActivity.this,"No Phone Number");
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        findViewById(R.id.VerifyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code =otpTextView.getOTP();
                if (code.isEmpty() || code.length() < 6) {
                    otpTextView.showError();
                    otpTextView.requestFocusOTP();
                    return;
                }
                progress.show();
                verifySignInCode(code);
            }
        });
    }
    private void sendVerificationCode(String phone) {

        progress.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phone,                     // Phone number to verify
                60,                           // Timeout duration
                TimeUnit.SECONDS,                // Unit of timeout
                this,        // Activity (for callback binding)
                mCallbacks);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            progress.dismiss();
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
                otpTextView.setOTP(code);
                verifySignInCode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            progress.dismiss();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codesent = s;

        }

    };

    private void verifySignInCode(String code)
    {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codesent,code);
        signInWithPhoneAuthCredential(credential);
    }
    void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            progress.dismiss();
                            SweetToast.success(VerifyActivity.this,"OTP matched");
                            Intent intent = new Intent(getApplicationContext(),BottomNavigationActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                progress.dismiss();
                                SweetToast.error(VerifyActivity.this,"OTP doesn't match");
                                finish();
                            }
                        }
                    }
                });
    }

}
