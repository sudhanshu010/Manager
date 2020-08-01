package com.example.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.manager.models.Complaint;
import com.example.manager.models.Machine;
import com.example.manager.models.Manager;
import com.example.manager.models.MechRating;
import com.example.manager.models.Mechanic;
import com.example.manager.models.Request;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    ImageView save,cancel;
    CircleImageView profile;
    EditText name, phone, staff, jobTitle, department, address;
    DatabaseReference managerReference, complaintReference, requestReference, mechanicReference, machineReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        name = findViewById(R.id.edit_profile_name);
        phone = findViewById(R.id.edit_profile_phone);
        staff = findViewById(R.id.staff_number);
        jobTitle = findViewById(R.id.job_title);
        department = findViewById(R.id.department);
        address = findViewById(R.id.work_address);
        save = findViewById(R.id.save_button);
        cancel = findViewById(R.id.close_btn);
        profile = findViewById(R.id.profilepic);
        profile.setColorFilter(ContextCompat.getColor(
                this,
                R.color.grey_overlay),
                PorterDuff.Mode.SRC_OVER
        );

        firebaseDatabase = FirebaseDatabase.getInstance();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String newName, newPhone, newStaff, newJobTitle, newDepartment, newAddress;
                newName = name.getText().toString();
                newPhone = phone.getText().toString();
                newStaff = staff.getText().toString();
                newJobTitle = jobTitle.getText().toString();
                newDepartment = department.getText().toString();
                newAddress = address.getText().toString();

                managerReference = firebaseDatabase.getReference("Users").child("Manager").child(user.getUid());

                HashMap<String, Object> hashMap = new HashMap<>();
                if(!newName.isEmpty())
                    hashMap.put("/Users/Manager/" + user.getUid() + "/userName", newName);
                if(!newPhone.isEmpty())
                    hashMap.put("/Users/Manager/" + user.getUid() + "/phoneNumber", newPhone);
                if(!newStaff.isEmpty())
                    hashMap.put("/Users/Manager/" + user.getUid() + "/empId", newStaff);
                if(!newJobTitle.isEmpty())
                    hashMap.put("/Users/Manager/" + user.getUid() + "/designation", newJobTitle);
                if(!newDepartment.isEmpty())
                    hashMap.put("/Users/Manager/" + user.getUid() + "/department", newDepartment);
                if(!newAddress.isEmpty())
                    hashMap.put("/Users/Manager/" + user.getUid() + "/savedAddress", newAddress);

                FirebaseDatabase.getInstance().getReference().updateChildren(hashMap);

                complaintReference = firebaseDatabase.getReference("Complaints");

                complaintReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot complaint : dataSnapshot.getChildren())
                        {
                            Complaint complaint1 = complaint.getValue(Complaint.class);
                            if(complaint1.getManager().getUid().equals(user.getUid()))
                            {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                if(!newName.isEmpty())
                                    hashMap.put("/Complaints/" + complaint1.getComplaintId() + "/manager/userName", newName);
                                if(!newPhone.isEmpty())
                                    hashMap.put("/Complaints/" + complaint1.getComplaintId() + "/manager/phoneNumber", newPhone);
                                if(!newStaff.isEmpty())
                                    hashMap.put("/Complaints/" + complaint1.getComplaintId() + "/manager/empId", newStaff);
                                if(!newJobTitle.isEmpty())
                                    hashMap.put("/Complaints/" + complaint1.getComplaintId() + "/manager/designation", newJobTitle);
                                if(!newDepartment.isEmpty())
                                    hashMap.put("/Complaints/" + complaint1.getComplaintId() + "/manager/department", newDepartment);
                                if(!newAddress.isEmpty())
                                    hashMap.put("/Complaints/" + complaint1.getComplaintId() + "/manager/savedAddress", newAddress);


                                FirebaseDatabase.getInstance().getReference().updateChildren(hashMap);

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                requestReference = firebaseDatabase.getReference("Requests");

                requestReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot request : dataSnapshot.getChildren())
                        {
                            Request request1 = request.getValue(Request.class);
                            if(request1.getComplaint().getManager().getUid().equals(user.getUid()))
                            {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                if(!newName.isEmpty())
                                    hashMap.put("/Requests/" + request1.getRequestId() + "/complaint/manager/userName" , newName);
                                if(!newPhone.isEmpty())
                                    hashMap.put("/Requests/" + request1.getRequestId() + "/complaint/manager/phoneNumber", newPhone);
                                if(!newStaff.isEmpty())
                                    hashMap.put("/Requests/" + request1.getRequestId() + "/complaint/manager/empId", newStaff);
                                if(!newJobTitle.isEmpty())
                                    hashMap.put("/Requests/" + request1.getRequestId() + "/complaint/manager/designation", newJobTitle);
                                if(!newDepartment.isEmpty())
                                    hashMap.put("/Requests/" + request1.getRequestId() + "/complaint/manager/department", newDepartment);
                                if(!newAddress.isEmpty())
                                    hashMap.put("/Requests/" + request1.getRequestId() + "/complaint/manager/savedAddress", newAddress);

                                FirebaseDatabase.getInstance().getReference().updateChildren(hashMap);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                mechanicReference = firebaseDatabase.getReference("Users").child("Mechanic");
                mechanicReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot mechanic : dataSnapshot.getChildren())
                        {
                            final Mechanic mechanic1 = mechanic.getValue(Mechanic.class);
                            DatabaseReference pendingComplaint = firebaseDatabase.getReference("Users").child("Mechanic").child(mechanic1.getUid()).child("pendingComplaints");
                            pendingComplaint.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot complaint : dataSnapshot.getChildren())
                                    {
                                        Complaint complaint1 = complaint.getValue(Complaint.class);
                                        if(complaint1.getManager().getUid().equals(user.getUid()))
                                        {
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            if(!newName.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/pendingComplaints/" + complaint1.getComplaintId() + "/manager/userName", newName);
                                            if(!newPhone.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/pendingComplaints/" + complaint1.getComplaintId() + "/manager/phoneNumber", newPhone);
                                            Log.i("msg", "to check");
                                            if(!newStaff.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/pendingComplaints/" + complaint1.getComplaintId() + "/manager/empId", newStaff);
                                            if(!newJobTitle.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/pendingComplaints/" + complaint1.getComplaintId() + "/manager/designation", newJobTitle);
                                            if(!newDepartment.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/pendingComplaints/" + complaint1.getComplaintId() + "/manager/department", newDepartment);
                                            if(!newAddress.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/pendingComplaints/" + complaint1.getComplaintId() + "/manager/savedAddress", newAddress);

                                            FirebaseDatabase.getInstance().getReference().updateChildren(hashMap);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            Log.i("1","2");

                            DatabaseReference completedComplaint = firebaseDatabase.getReference("Users").child("Mechanic").child(mechanic1.getUid()).child("completedComplaints");

                            completedComplaint.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot complaint : dataSnapshot.getChildren())
                                    {
                                        Complaint complaint1 = complaint.getValue(Complaint.class);
                                        if(complaint1.getManager().getUid().equals(user.getUid()))
                                        {
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            if(!newName.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/completedComplaints/" + complaint1.getComplaintId() + "/manager/userName", newName);
                                            if(!newPhone.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/completedComplaints/" + complaint1.getComplaintId() + "/manager/phoneNumber", newPhone);
                                            if(!newStaff.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/completedComplaints/" + complaint1.getComplaintId() + "/manager/empId", newStaff);
                                            if(!newJobTitle.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/completedComplaints/" + complaint1.getComplaintId() + "/manager/designation", newJobTitle);
                                            if(!newDepartment.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/completedComplaints/" + complaint1.getComplaintId() + "/manager/department", newDepartment);
                                            if(!newAddress.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/completedComplaints/" + complaint1.getComplaintId() + "/manager/savedAddress", newAddress);
                                            FirebaseDatabase.getInstance().getReference().updateChildren(hashMap);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            DatabaseReference pendingRequest = firebaseDatabase.getReference("Users").child("Mechanic").child(mechanic1.getUid()).child("pendingRequests");
                            pendingRequest.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot request : dataSnapshot.getChildren())
                                    {
                                        Request request1 = request.getValue(Request.class);
                                        if(request1.getComplaint().getManager().getUid().equals(user.getUid()))
                                        {
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            if(!newName.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/pendingRequests/" + request1.getRequestId() + "/complaint/manager/userName", newName);
                                            if(!newPhone.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/pendingRequests/" + request1.getRequestId() + "/complaint/manager/phoneNumber", newPhone);
                                            if(!newStaff.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/pendingRequests/" + request1.getRequestId() + "/complaint/manager/empId", newStaff);
                                            if(!newJobTitle.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/pendingRequests/" + request1.getRequestId() + "/complaint/manager/designation", newJobTitle);
                                            if(!newDepartment.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/pendingRequests/" + request1.getRequestId() + "/complaint/manager/department", newDepartment);
                                            if(!newAddress.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/pendingRequests/" + request1.getRequestId() + "/complaint/manager/savedAddress", newAddress);
                                            FirebaseDatabase.getInstance().getReference().updateChildren(hashMap);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            DatabaseReference completedRequest = firebaseDatabase.getReference("Users").child("Mechanic").child(mechanic1.getUid()).child("completedRequests");
                            completedRequest.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot request : dataSnapshot.getChildren())
                                    {
                                        Request request1 = request.getValue(Request.class);
                                        if(request1.getComplaint().getManager().getUid().equals(user.getUid()))
                                        {
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            if(!newName.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/completedRequests/" + request1.getRequestId() + "/complaint/manager/userName", newName);
                                            if(!newPhone.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/completedRequests/" + request1.getRequestId() + "/complaint/manager/phoneNumber", newPhone);
                                            if(!newStaff.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/completedRequests/" + request1.getRequestId() + "/complaint/manager/empId", newStaff);
                                            if(!newJobTitle.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/completedRequests/" + request1.getRequestId() + "/complaint/manager/designation", newJobTitle);
                                            Log.i("3","4");
                                            if(!newDepartment.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/completedRequests/" + request1.getRequestId() + "/complaint/manager/department", newDepartment);
                                            if(!newAddress.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/completedRequests/" + request1.getRequestId() + "/complaint/manager/savedAddress", newAddress);
                                            FirebaseDatabase.getInstance().getReference().updateChildren(hashMap);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            DatabaseReference ratingReference = firebaseDatabase.getReference("Users").child("Mechanic").child(mechanic1.getUid()).child("Ratings");
                            ratingReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot rating : dataSnapshot.getChildren())
                                    {
                                        MechRating rating1 = rating.getValue(MechRating.class);
                                        if(rating1.getManager().getUid().equals(user.getUid()))
                                        {
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            if(!newName.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/Ratings/" + rating1.getRatingId() + "/manager/userName", newName);
                                            if(!newPhone.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/Ratings/" + rating1.getRatingId() + "/manager/phoneNumber", newPhone);
                                            if(!newStaff.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/Ratings/" + rating1.getRatingId() + "/manager/empId", newStaff);
                                            if(!newJobTitle.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/Ratings/" + rating1.getRatingId() + "/manager/designation", newJobTitle);
                                            if(!newDepartment.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/Ratings/" + rating1.getRatingId() + "/manager/department", newDepartment);
                                            if(!newAddress.isEmpty())
                                                hashMap.put("/Users/Mechanic/" + mechanic1.getUid() + "/Ratings/" + rating1.getRatingId() + "/manager/savedAddress", newAddress);
                                            FirebaseDatabase.getInstance().getReference().updateChildren(hashMap);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Log.i("5","6");
                machineReference = firebaseDatabase.getReference("Machines");
                machineReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot machine : dataSnapshot.getChildren())
                        {
                            Machine machine1 = machine.getValue(Machine.class);
                            if(machine1.getManager().getUid().equals(user.getUid()))
                            {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                if(!newName.isEmpty())
                                    hashMap.put("/Machines/" + machine1.getMachineId() + "/manager/userName", newName);
                                if(!newPhone.isEmpty())
                                    hashMap.put("/Machines/" + machine1.getMachineId() + "/manager/phoneNumber", newPhone);
                                if(!newStaff.isEmpty())
                                    hashMap.put("/Machines/" + machine1.getMachineId() + "/manager/empId", newStaff);
                                if(!newJobTitle.isEmpty())
                                    hashMap.put("/Machines/" + machine1.getMachineId() + "/manager/designation", newJobTitle);
                                Log.i("7","8");
                                if(!newDepartment.isEmpty())
                                    hashMap.put("/Machines/" + machine1.getMachineId() + "/manager/department", newDepartment);
                                if(!newAddress.isEmpty())
                                    hashMap.put("/Machines/" + machine1.getMachineId() + "/manager/savedAddress", newAddress);

                                FirebaseDatabase.getInstance().getReference().updateChildren(hashMap);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}