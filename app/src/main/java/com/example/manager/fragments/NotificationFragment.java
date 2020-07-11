package com.example.manager.fragments;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager.R;
import com.example.manager.adapters.MachineAdapter;
import com.example.manager.adapters.NotificationAdapter;
import com.example.manager.models.Machine;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class NotificationFragment extends Fragment {


    RecyclerView recyclerView;
    ScrollView scrollView;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    LinearLayoutManager HorizontalLayout;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.notification_fragment, container, false);
        final Toolbar toolbar=view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(R.drawable.ic_notfication_tb);

//        recyclerView = view.findViewById(R.id.notification_recyclerview);
//        scrollView = view.findViewById(R.id.empty_scrolView);
//
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        reference = FirebaseDatabase.getInstance().getReference().child("Broadcast").child("Manager").child(user.getUid());
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(!dataSnapshot.exists())
//                {
//                    scrollView.setVisibility(View.VISIBLE);
//                    recyclerView.setVisibility(View.GONE);
//                }
//                else
//                {
//                    scrollView.setVisibility(View.GONE);
//                    recyclerView.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        HorizontalLayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(HorizontalLayout);
//
//        Query baseQuery = FirebaseDatabase.getInstance().getReference("Broadcast").child("Manager").child(user.getUid());
//
//        PagedList.Config config = new PagedList.Config.Builder()
//                .setEnablePlaceholders(false)
//                .setPrefetchDistance(10)
//                .setPageSize(20)
//                .build();
//
//        DatabasePagingOptions<Machine> options = new DatabasePagingOptions.Builder<Machine>()
//                .setLifecycleOwner(this)
//                .setQuery(baseQuery,config,Machine.class)
//                .build();
//
//        NotificationAdapter = new NotificationAdapter(options,getActivity().getApplicationContext());
//        recyclerView.setAdapter(NotificationAdapter);
//        NotificationAdapter.startListening();

        return view;
    }
}
