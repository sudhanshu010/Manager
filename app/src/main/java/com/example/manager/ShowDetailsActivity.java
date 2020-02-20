package com.example.manager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.manager.adapters.ShowDetailsAdapter;
import com.example.manager.models.PastRecord;
import com.example.manager.models.Request;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private ShowDetailsAdapter showDetailsAdapter;
    FloatingActionButton floatingActionButton;
    String generationCode;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference historyReference,pastRecordsReference;
    SwipeRefreshLayout swipeRefereshLayout;
    ShimmerFrameLayout shimmerFrameLayout;

    List<PastRecord> pastRecords;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);

        generationCode = getIntent().getStringExtra("generationCode");

        firebaseDatabase = FirebaseDatabase.getInstance();
        historyReference = firebaseDatabase.getReference("machines").child(generationCode).child("pastRecords");

        floatingActionButton = findViewById(R.id.btn_float);
        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

       // shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout);
       // shimmerFrameLayout.startShimmerAnimation();

        pastRecords = new ArrayList<>();
        showDetailsAdapter = new ShowDetailsAdapter(getApplicationContext(),pastRecords);
        recyclerView.setAdapter(showDetailsAdapter);

        swipeRefereshLayout = findViewById(R.id.swipeRefreshLayout);

        swipeRefereshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefereshLayout.setColorSchemeColors(Color.BLUE);

                swipeRefereshLayout.setRefreshing(false);

            }
        });


        historyReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String request = dataSnapshot.getKey();

                Log.i("History","something");
                DatabaseReference requestRefernce = FirebaseDatabase.getInstance().getReference("Requests").child(request);
                requestRefernce.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Log.i("History","something1 ");

                        PastRecord pastRecord = new PastRecord();
                        Request request= dataSnapshot.getValue(Request.class);
                        pastRecord.setDescription(request.getDescription());
                        pastRecord.setServiceMan(request.getServicemanName());
                        pastRecord.setComplaintId(request.getComplaintId());
                        //shimmerFrameLayout.stopShimmerAnimation();
                        //shimmerFrameLayout.setVisibility(View.INVISIBLE);
                        pastRecords.add(pastRecord);
                        showDetailsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent i = new Intent(ShowDetailsActivity.this, UpdateActivity.class);
               // startActivity(i);
                finish();

            }
        });
    }



}