package com.example.manager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.manager.R;
import com.example.manager.adapters.CompletedComplaintAdapter;
import com.example.manager.models.Complaint;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CompletedComplaintFragment extends Fragment {

    RecyclerView rm_recyclerView_completed_complaint;
    CompletedComplaintAdapter completedComplaintAdapter;

    List<Complaint> completedComplaintObjectList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference complaintReference, responsibleManReference, pendingComplaintListReference;

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

        rm_recyclerView_completed_complaint = rootView.findViewById(R.id.rm_recyclerView_completed_complaint);
        rm_recyclerView_completed_complaint.setLayoutManager(new LinearLayoutManager(getActivity()));

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        completedComplaintObjectList = new ArrayList<>();

        completedComplaintAdapter = new CompletedComplaintAdapter(getActivity().getApplicationContext(), completedComplaintObjectList);
        rm_recyclerView_completed_complaint.setAdapter(completedComplaintAdapter);


        firebaseDatabase =  FirebaseDatabase.getInstance();
        responsibleManReference = firebaseDatabase.getReference("Users").child("ResponsibleMan").child(user.getUid());
        pendingComplaintListReference = responsibleManReference.child("completedComplaintList");
        complaintReference = firebaseDatabase.getReference("Complaints");

        pendingComplaintListReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String key = dataSnapshot.getKey();

                complaintReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Complaint completedComplaint = new Complaint();
                        completedComplaint = dataSnapshot.getValue(Complaint.class);

                        completedComplaintObjectList.add(0,completedComplaint);
                        completedComplaintAdapter.notifyDataSetChanged();

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
        return rootView;
    }
}
