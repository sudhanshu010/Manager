package com.example.manager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager.R;
import com.example.manager.adapters.PendingComplaintAdapter;
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

public class PendingComplaintFragment extends Fragment {

    RecyclerView rm_recyclerView_pending_complaint;
    PendingComplaintAdapter pendingComplaintAdapter;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    FirebaseUser user;

    public PendingComplaintFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView =  inflater.inflate(R.layout.pending_complaint_fragment, container, false);
        rm_recyclerView_pending_complaint = rootView.findViewById(R.id.rm_recyclerView_pending_complaint);
        rm_recyclerView_pending_complaint.setLayoutManager(new LinearLayoutManager(getActivity()));

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase =  FirebaseDatabase.getInstance();

        Query baseQuery = firebaseDatabase.getReference("Users").child("Manager").child(user.getUid()).child("pendingComplaints");

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build();

        DatabasePagingOptions<Complaint> options = new DatabasePagingOptions.Builder<Complaint>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery,config,Complaint.class)
                .build();

        pendingComplaintAdapter = new PendingComplaintAdapter(options, getActivity().getApplicationContext());
        rm_recyclerView_pending_complaint.setAdapter(pendingComplaintAdapter);
        pendingComplaintAdapter.startListening();

        return rootView;
    }
}
