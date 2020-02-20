package com.example.manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.manager.adapters.MachineAdapter;
import com.example.manager.models.Machine;
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

public class MyMachine extends AppCompatActivity {

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

        Log.i("vikas", "goyal");

        recyclerView_machine=findViewById(R.id.recyclerView_machine);
        recyclerView_machine.setLayoutManager(new LinearLayoutManager(this));

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        machineList = new ArrayList<>();

        machineAdapter = new MachineAdapter(getApplicationContext(), machineList);
        recyclerView_machine .setAdapter(machineAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        rmMachineReference = firebaseDatabase.getReference("Users").child("ResponsibleMan").child(user.getUid()).child("myMachines");
        machineReference = firebaseDatabase.getReference("machines");


        rmMachineReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String key = dataSnapshot.getKey();
                Log.i("machine key", key);

                machineReference.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Machine machine = new Machine();
                        machine = dataSnapshot.getValue(Machine.class);
                        Log.i("department", "vikas");
                        machineList.add(0, machine);
                        machineAdapter.notifyDataSetChanged();
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

    }
}
