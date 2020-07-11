package com.example.manager.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager.R;
import com.example.manager.adapters.RecentChatAdapter;
import com.example.manager.models.Complaint;
import com.example.manager.models.Machine;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import pl.droidsonroids.gif.GifImageView;


public class RecentChatFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    ConstraintLayout emptyView;
    FirebaseAuth auth;
    FirebaseUser user;

    ShimmerFrameLayout shimmerFrameLayout;
    public RecentChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.recent_chat_fragment, container, false);


        final Toolbar toolbar=view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(R.drawable.ic_chat_tb);

        shimmerFrameLayout = view.findViewById(R.id.shimmer_container);
        shimmerFrameLayout.startShimmer();
        recyclerView = view.findViewById(R.id.recent_chat_rv);
        recyclerView.setVisibility(View.INVISIBLE); // this will be set to visible recent chat adapter after shimmer effect stops.
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Query baseQuery = firebaseDatabase.getReference("Users/Manager/"+user.getUid()+"/pendingComplaints");
        baseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {

                    ConstraintLayout noMessageView = view.findViewById(R.id.empty_view_msg);
                    ShimmerFrameLayout shimmerFrameLayout = view.findViewById(R.id.shimmer_container);
                    shimmerFrameLayout.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmer();
                    noMessageView.setVisibility(View.VISIBLE);
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
        RecentChatAdapter recentChatAdapter = new RecentChatAdapter(options,getActivity().getApplicationContext(),view);
        recyclerView.setAdapter(recentChatAdapter);
        recentChatAdapter.startListening();

        return view;
    }


}
