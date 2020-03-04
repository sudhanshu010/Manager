package com.example.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.manager.adapters.MachineAdapter;
import com.example.manager.holders.MyMachinesHolder;
import com.example.manager.models.Machine;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class MyMachinesActivity extends AppCompatActivity {

    RecyclerView recyclerView_machine;
    MachineAdapter machineAdapter;

    FirebaseAuth auth;
    FirebaseUser user;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference rmMachineReference, machineReference;

    List<Machine> machineList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_machine);

        recyclerView_machine=findViewById(R.id.recyclerView_machine);
        recyclerView_machine.setLayoutManager(new LinearLayoutManager(this));

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        machineList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        rmMachineReference = firebaseDatabase.getReference("Users").child("Manager").child(user.getUid()).child("myMachines");
        machineReference = firebaseDatabase.getReference("Machines");

        Query baseQuery = firebaseDatabase.getReference("Users").child("Manager").child(user.getUid()).child("myMachines");

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build();

        DatabasePagingOptions<Machine> options = new DatabasePagingOptions.Builder<Machine>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery,config,Machine.class)
                .build();

        FirebaseRecyclerPagingAdapter<Machine, MyMachinesHolder> adapter = new FirebaseRecyclerPagingAdapter<Machine, MyMachinesHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyMachinesHolder viewHolder, int position, @NonNull Machine model) {

                viewHolder.bind(model);
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {

            }

            @NonNull
            @Override
            public MyMachinesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_machine_item,null);
                return new MyMachinesHolder(view);
            }
        };

        recyclerView_machine.setAdapter(adapter);
        adapter.startListening();

//
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
