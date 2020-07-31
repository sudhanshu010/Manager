package com.example.manager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.manager.models.Manager;
import com.example.manager.models.MechRating;
import com.example.manager.models.Mechanic;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import xyz.hasnat.sweettoast.SweetToast;

public class RatingActivity extends AppCompatActivity {

    RatingBar ratingBar;
    Button submit_rating;
    EditText reviews;

    String serviceManUid;
    DatabaseReference databaseReference, managerReference;

    FirebaseAuth auth;
    FirebaseUser user;

    Mechanic mechanic;
    float rating;
    int number;

    Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        submit_rating = findViewById(R.id.submit_rating);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextAppearance(this,R.style.TitleTextAppearance);
        ratingBar = findViewById(R.id.rating_bar);

        reviews = findViewById(R.id.mech_review);

        serviceManUid = getIntent().getStringExtra("serviceManUid");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Mechanic").child(serviceManUid);
        managerReference = FirebaseDatabase.getInstance().getReference("Users").child("Manager").child(user.getUid());

        managerReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                manager = dataSnapshot.getValue(Manager.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        submit_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        mechanic = dataSnapshot.getValue(Mechanic.class);
                        rating = mechanic.getOverallRating();
                        number = mechanic.getNumberOfRating();

                        float newRating = ((rating*number)+ratingBar.getProgress()/2)/(number+1);
                        databaseReference.removeEventListener(this);

                        MechRating mechRating = new MechRating();
                        mechRating.setStars(ratingBar.getProgress()/2);
                        mechRating.setReviews(reviews.getText().toString());
                        mechRating.setRatingId(number);

                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        month = month + 1;
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        mechRating.setRevDate(day + "/" + month + "/" + year);

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
                        mechRating.setManager(tempManager);

                        Map<String, Object> mechRatingValue = mechRating.toMap();

                        HashMap<String,Object> update = new HashMap<>();

                        update.put("/Users/Mechanic/"+serviceManUid+"/overallRating",newRating);
                        update.put("/Users/Mechanic/"+serviceManUid+"/numberOfRating",number+1);

                        update.put("/Users/Mechanic/" + serviceManUid + "/Ratings/" + number, mechRatingValue);

                        FirebaseDatabase.getInstance().getReference().updateChildren(update);
                        SweetToast.success(RatingActivity.this,"Review Submitted");
                        Intent intent = new Intent(getApplicationContext(), BottomNavigationActivity.class);
                        startActivity(intent);
                        finish();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
