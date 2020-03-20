package com.example.manager.DialogBox;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;

import com.example.manager.R;
import com.example.manager.models.Complaint;
import com.example.manager.models.Manager;
import com.example.manager.models.Mechanic;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ComplaintDescriptionDialog extends Dialog implements
        View.OnClickListener {


    public Dialog d;
    String filename;

    LinearLayout descriptionlayout;

    private EditText complaintDescription;
    private TextView cancelButton, submitButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference machineReference, complaintIdReference, mechanicListReference, managerReference,complaintReference,serviceManReference;

    FirebaseAuth auth;
    FirebaseUser user;

    Manager manager;

    Complaint complaint;
    long complaintId;

    List<Mechanic> serviceManListObjects;

    HashMap<String,Mechanic> mechanicList;

    LottieAnimationView animationView;

    public ComplaintDescriptionDialog(Activity a, Complaint complaint) {
        super(a);
        this.complaint = complaint;
        this.setCanceledOnTouchOutside(false);
        // TODO Auto-generated constructor stub

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.generate_complaint_description_dialog);

        animationView = findViewById(R.id.lottieAnimationView);

        complaintDescription = findViewById(R.id.complaintDescription);
        cancelButton = findViewById(R.id.cancelButton);
        submitButton = findViewById(R.id.submitButton);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        complaintIdReference = firebaseDatabase.getReference("ComplaintId");
        mechanicListReference = firebaseDatabase.getReference("Users").child("Mechanic");
        managerReference = firebaseDatabase.getReference("Users").child("Manager").child(user.getUid());
        serviceManReference = firebaseDatabase.getReference("Users").child("ServiceMan");

        serviceManListObjects = new ArrayList<>();

        complaintIdReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                complaintId = (long) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        managerReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                manager = dataSnapshot.getValue(Manager.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        complaintReference = firebaseDatabase.getReference("Complaints");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                animationView.setVisibility(View.VISIBLE);
                animationView.playAnimation();

                mechanicList = new HashMap<>();     // list of mechanic
                mechanicListReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot mechanicReference : dataSnapshot.getChildren())
                        {
                            String key = mechanicReference.getKey();
                            Mechanic mechanic = mechanicReference.getValue(Mechanic.class);
                            mechanicList.put(key,mechanic);
                        }

                        mechanicList = sortByValue(mechanicList);   // sort mechanic list by load

                        final Map.Entry<String,Mechanic> entry = mechanicList.entrySet().iterator().next();
                        Mechanic mechanic = entry.getValue();
                        String uid = entry.getKey();


                        HashMap<String,Object> updateDatabaseValue = new HashMap<>();

                        complaint.setComplaintId(complaintId);
                        complaint.setDescription(complaintDescription.getText().toString());

                        Mechanic tempMechanic = null;
                        try {
                            tempMechanic = (Mechanic) mechanic.clone();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        if (tempMechanic != null) {
                            tempMechanic.setPendingComplaints(null);
                        }
                        complaint.setMechanic(tempMechanic);

                        Complaint tempComplaint = null;
                        try {
                            tempComplaint = (Complaint) complaint.clone();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        if (tempComplaint != null) {
                            tempComplaint.setMechanic(null);
                        }
                        updateDatabaseValue.put("/Users/Mechanic/"+uid+"/pendingComplaints/"+complaintId,tempComplaint);
                        mechanicListReference.removeEventListener(this);

                        try {
                            tempComplaint = (Complaint) complaint.clone();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }

                        if (tempComplaint != null) {
                            tempComplaint.setManager(null);
                        }


                        updateDatabaseValue.put("/Users/Mechanic/"+uid+"/load",mechanic.getLoad()+1);

                        updateDatabaseValue.put("/ComplaintId",complaintId+1);
                        updateDatabaseValue.put("/Complaints/"+complaintId,complaint);
                        //updateDatabaseValue.put("/Complaints/"+complaintId+"/mechanic/load",mechanic.getLoad()+1);
                        updateDatabaseValue.put("/Users/Manager/"+user.getUid()+"/pendingComplaints/"+complaintId,tempComplaint);
                        //updateDatabaseValue.put("/Users/Manager/"+user.getUid()+"/pendingComplaints/"+complaintId+"/mechanic/load",mechanic.getLoad()+1);

                        FirebaseDatabase.getInstance().getReference().updateChildren(updateDatabaseValue);

                        HashMap<String,Object> updateDatabaseValue1 = new HashMap<>();

                        updateDatabaseValue1.put("/Complaints/"+complaintId+"/mechanic/load",mechanic.getLoad()+1);
                        updateDatabaseValue1.put("/Users/Manager/"+user.getUid()+"/pendingComplaints/"+complaintId+"/mechanic/load",mechanic.getLoad()+1);

                        FirebaseDatabase.getInstance().getReference().updateChildren(updateDatabaseValue1);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public static HashMap<String, Mechanic> sortByValue(HashMap<String, Mechanic> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Mechanic> > list =
                new LinkedList<Map.Entry<String, Mechanic> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Mechanic> >() {
            public int compare(Map.Entry<String, Mechanic> o1,
                               Map.Entry<String, Mechanic> o2)
            {
                return Integer.compare(o1.getValue().getLoad(), o2.getValue().getLoad());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Mechanic> temp = new LinkedHashMap<String, Mechanic>();
        for (Map.Entry<String, Mechanic> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    @Override
    public void onClick(View v) {

    }
}
