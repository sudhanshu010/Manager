package com.example.manager.DialogBox;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;

import com.example.manager.R;
import com.example.manager.models.Complaint;
import com.example.manager.models.Manager;
import com.example.manager.models.Mechanic;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

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

    private EditText complaintDescription, instruction;
    private TextView cancelButton, submitButton;
    ImageView closeButton;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button generate;

    String serviceType;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference machineReference, complaintIdReference, mechanicListReference, managerReference,complaintReference,serviceManReference, FaultMachineReference;

    FirebaseAuth auth;
    FirebaseUser user;

    FirebaseFunctions firebaseFunctions;

    Manager manager;

    Complaint complaint;
    long complaintId;
    long faultMachine;

    List<Mechanic> serviceManListObjects;

    HashMap<String,Mechanic> mechanicList;

    LottieAnimationView animationView;

    public ComplaintDescriptionDialog(Activity a, Complaint complaint) {
        super(a);
        generate = a.findViewById(R.id.generateComplaint);
        this.complaint = complaint;
        this.setCanceledOnTouchOutside(false);
        // TODO Auto-generated constructor stub

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.generate_complaint_description_dialog);

//        animationView = findViewById(R.id.lottieAnimationView);

        complaintDescription = findViewById(R.id.editText);
        cancelButton = findViewById(R.id.cancel_button);
        submitButton = findViewById(R.id.submit_button);
        closeButton = findViewById(R.id.cancel);
        radioGroup = findViewById(R.id.radioGroup);
        instruction = findViewById(R.id.instruction);



        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        complaintIdReference = firebaseDatabase.getReference("ComplaintId");
        mechanicListReference = firebaseDatabase.getReference("Users").child("Mechanic");
        managerReference = firebaseDatabase.getReference("Users").child("Manager").child(user.getUid());
        serviceManReference = firebaseDatabase.getReference("Users").child("ServiceMan");
        FaultMachineReference = firebaseDatabase.getReference("FaultMachines");

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
        FaultMachineReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                faultMachine = (long) dataSnapshot.getValue();
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

//                generate.setText("generated");
//                generate.setEnabled(false);
//                animationView.setVisibility(View.VISIBLE);
//                animationView.playAnimation();
                dismiss();

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

                        int selectId = radioGroup.getCheckedRadioButtonId();
                        radioButton = findViewById(selectId);
                        serviceType = radioButton.getText().toString();

                        complaint.setServiceType(serviceType);
                        complaint.setInstruction(instruction.getText().toString());

                        Mechanic tempMechanic = null;
                        try {
                            tempMechanic = (Mechanic) mechanic.clone();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        if (tempMechanic != null) {
                            tempMechanic.setPendingComplaints(null);
                            tempMechanic.setCompletedComplaints(null);
                            tempMechanic.setCompletedRequest(null);
                            tempMechanic.setPendingRequest(null);
                            tempMechanic.setRatings(null);
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
                        updateDatabaseValue.put("/FaultMachines", faultMachine+1);
                        updateDatabaseValue.put("/Complaints/"+complaintId,complaint);
                        //updateDatabaseValue.put("/Complaints/"+complaintId+"/mechanic/load",mechanic.getLoad()+1);
                        updateDatabaseValue.put("/Users/Manager/"+user.getUid()+"/pendingComplaints/"+complaintId,tempComplaint);
                        //updateDatabaseValue.put("/Users/Manager/"+user.getUid()+"/pendingComplaints/"+complaintId+"/mechanic/load",mechanic.getLoad()+1);

                        FirebaseDatabase.getInstance().getReference().updateChildren(updateDatabaseValue);

                        HashMap<String,Object> updateDatabaseValue1 = new HashMap<>();

                        updateDatabaseValue1.put("/Complaints/"+complaintId+"/mechanic/load",mechanic.getLoad()+1);
                        updateDatabaseValue1.put("/Machines/" + complaint.getMachine().getMachineId() + "/working", false);
                        updateDatabaseValue1.put("/Users/Manager/"+user.getUid()+"/pendingComplaints/"+complaintId+"/mechanic/load",mechanic.getLoad()+1);

                        FirebaseDatabase.getInstance().getReference().updateChildren(updateDatabaseValue1);

                        // sending notification to mechanic
                        Log.i("ankit token","here");
                        FirebaseDatabase.getInstance().getReference("tokens").child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String token = (String) dataSnapshot.getValue();
                                Log.i("ankit token fetch checking",token);
                                sendNotification(complaint,token);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
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
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void sendNotification(Complaint complaint, String token) {
        Log.i("ankit complaint checking", String.valueOf(complaint));
        final HashMap<String,String> data = new HashMap<>();
        data.put("serviceType",complaint.getServiceType());
        data.put("description",complaint.getDescription());
        data.put("instruction",complaint.getInstruction());
        data.put("token",token);
        Log.i("ankit complaint ", "before function call");

        firebaseFunctions = FirebaseFunctions.getInstance();
        firebaseFunctions.getHttpsCallable("sendMessage")
                .call(data)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        HashMap<String,String> hashMap;
                        hashMap = (HashMap<String, String>) httpsCallableResult.getData();
                        if(hashMap.get("status").equals("successful")){
                            Log.i("ankit successful","successfully sent");

                        }
                        else{
                            Log.i("ankit error occured","error");

                        }
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
