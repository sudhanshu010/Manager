package com.example.manager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.manager.R;
import com.example.manager.adapters.CompletedComplaintAdapter;
import com.example.manager.models.Complaint;
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

public class CompletedComplaintFragment extends Fragment {

    RecyclerView rm_recyclerView_completed_complaint;
    CompletedComplaintAdapter completedComplaintAdapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference complaintReference, responsibleManReference, pendingComplaintListReference;
    LinearLayout nothing;
    FirebaseAuth auth;
    FirebaseUser user;

    public CompletedComplaintFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView =  inflater.inflate(R.layout.completed_complaint_fragment, container, false);
        nothing = rootView.findViewById(R.id.EmptyList1);
        rm_recyclerView_completed_complaint = rootView.findViewById(R.id.rm_recyclerView_completed_complaint);
        rm_recyclerView_completed_complaint.setLayoutManager(new LinearLayoutManager(getActivity()));

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();




        firebaseDatabase =  FirebaseDatabase.getInstance();
//        responsibleManReference = firebaseDatabase.getReference("Users").child("Manager").child(user.getUid());
//        pendingComplaintListReference = responsibleManReference.child("completedComplaintList");
//        complaintReference = firebaseDatabase.getReference("Complaints");

        Query baseQuery = firebaseDatabase.getReference("Users").child("Manager").child(user.getUid()).child("completedComplaints");
        DatabaseReference reference1 = firebaseDatabase.getReference().child("Users").child("Manager").child(user.getUid()).child("completedComplaints");
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    nothing.setVisibility(View.VISIBLE);
                    rm_recyclerView_completed_complaint.setVisibility(View.GONE);
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

        DatabasePagingOptions<Complaint> options = new DatabasePagingOptions.Builder<Complaint>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery,config,Complaint.class)
                .build();

        completedComplaintAdapter = new CompletedComplaintAdapter(options, getActivity().getApplicationContext());
        rm_recyclerView_completed_complaint.setAdapter(completedComplaintAdapter);
        completedComplaintAdapter.startListening();

        return rootView;
    }
}
