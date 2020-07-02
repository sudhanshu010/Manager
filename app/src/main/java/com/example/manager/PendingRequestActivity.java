package com.example.manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.manager.adapters.PendingRequestAdapter;
import com.example.manager.models.Complaint;
import com.example.manager.models.Request;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PendingRequestActivity extends AppCompatActivity {

    RecyclerView pendingRequestRecycler;
    PendingRequestAdapter pendingRequestAdapter;
    LinearLayout nothing;
    FirebaseDatabase firebaseDatabase;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_request);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        nothing = findViewById(R.id.EmptyList3);
        pendingRequestRecycler = findViewById(R.id.rmpending_request);
        pendingRequestRecycler.setLayoutManager(new LinearLayoutManager(this));
        firebaseDatabase =  FirebaseDatabase.getInstance();

        Query baseQuery = firebaseDatabase.getReference("Users").child("Manager").child(user.getUid()).child("pendingRequests");

        DatabaseReference reference1 = firebaseDatabase.getReference().child("Users").child("Manager").child(user.getUid()).child("pendingRequests");
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    nothing.setVisibility(View.VISIBLE);
                    pendingRequestRecycler.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build();

        DatabasePagingOptions<Request> options = new DatabasePagingOptions.Builder<Request>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery,config,Request.class)
                .build();

        pendingRequestAdapter = new PendingRequestAdapter(options, PendingRequestActivity.this);
        pendingRequestRecycler.setAdapter(pendingRequestAdapter);
        pendingRequestAdapter.startListening();


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
