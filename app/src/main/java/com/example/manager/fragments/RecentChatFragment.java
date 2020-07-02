package com.example.manager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager.R;
import com.example.manager.adapters.RecentChatAdapter;
import com.example.manager.models.Complaint;
import com.example.manager.models.Machine;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class RecentChatFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    ConstraintLayout emptyView;


    public RecentChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.recent_chat_fragment, container, false);

        recyclerView = view.findViewById(R.id.recent_chat_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseDatabase = FirebaseDatabase.getInstance();
        Query baseQuery = firebaseDatabase.getReference("Complaints");
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build();

        DatabasePagingOptions<Complaint> options = new DatabasePagingOptions.Builder<Complaint>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery,config,Complaint.class)
                .build();
        RecentChatAdapter recentChatAdapter = new RecentChatAdapter(options,getActivity().getApplicationContext());
        recyclerView.setAdapter(recentChatAdapter);
        recentChatAdapter.startListening();

        return view;
    }
}
