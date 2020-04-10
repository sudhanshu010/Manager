package com.example.manager.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.manager.R;
import com.example.manager.RatingActivity;
import com.example.manager.models.Complaint;
import com.example.manager.models.PastRecord;
import com.example.manager.models.Request;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class PendingRequestAdapter extends FirebaseRecyclerPagingAdapter<Request, PendingRequestAdapter.MyHolder> {

    Context c;
    /**
     * Construct a new FirestorePagingAdapter from the given {@link DatabasePagingOptions}.
     *
     * @param options
     */


    public PendingRequestAdapter(DatabasePagingOptions<Request> options, Context c) {
        super(options);
        this.c = c;
    }



    @NonNull
    @Override
    public PendingRequestAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_request_item, null);
        return new PendingRequestAdapter.MyHolder(view);
    }

    protected void onBindViewHolder(PendingRequestAdapter.MyHolder viewHolder,int position,  Request model) {

        viewHolder.bind(model);

    }

    protected  void  onLoadingStateChanged(LoadingState state) {

    }



    class MyHolder extends RecyclerView.ViewHolder {


        TextView serviceman1,requestid1,complain_id,description, accept_button,decline_button;

        String load;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference complaintReference,loadValue, mechComplaint, mechRequest;

        FirebaseAuth auth;
        FirebaseUser user;

        Complaint complaint = null;
        Request request = null;

        public MyHolder(@NonNull final View itemView) {
            super(itemView);

            requestid1 = itemView.findViewById(R.id.request_id1);
            serviceman1 = itemView.findViewById(R.id.serviceman1);
            complain_id = itemView.findViewById(R.id.complain_id);
            description = itemView.findViewById(R.id.description);
            accept_button = itemView.findViewById(R.id.accept_button);
            decline_button = itemView.findViewById(R.id.decline_button);

        }

        public void  bind(final Request model)
        {
            serviceman1.setText(model.getComplaint().getMechanic().getUserName());
            requestid1.setText(String.valueOf(model.getRequestId()));
            complain_id.setText(String.valueOf(model.getComplaint().getComplaintId()));
            description.setText(model.getDescription());



            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();


            complaintReference = FirebaseDatabase.getInstance().getReference("Complaints").child(String.valueOf(model.getComplaint().getComplaintId()));
            loadValue = FirebaseDatabase.getInstance().getReference("Users").child("Mechanic").child(model.getComplaint().getMechanic().getUid()).child("load");
            mechComplaint = FirebaseDatabase.getInstance().getReference("Users").child("Mechanic").child(model.getComplaint().getMechanic().getUid()).child("pendingComplaints").child(String.valueOf(model.getComplaint().getComplaintId()));
            mechRequest = FirebaseDatabase.getInstance().getReference("Users").child("Mechanic").child(model.getComplaint().getMechanic().getUid()).child("pendingRequests").child(String.valueOf(model.getRequestId()));
            loadValue.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    load = dataSnapshot.getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            mechComplaint.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    complaint = dataSnapshot.getValue(Complaint.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            mechRequest.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    request = dataSnapshot.getValue(Request.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(model.isStatus())
            {
                final HashMap<String,Object> updateDatabaseValue = new HashMap<>();

                //add data
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                month = month+1;
                int day = cal.get(Calendar.DAY_OF_MONTH);


                updateDatabaseValue.put("/Complaints/"+model.getComplaint().getComplaintId()+"/completedDate",day+"/"+month+"/"+year);
                updateDatabaseValue.put("/Complaints/"+model.getComplaint().getComplaintId()+"/status",5);

                updateDatabaseValue.put("/Users/Manager/"+user.getUid()+"/completedComplaints/"+model.getComplaint().getComplaintId(),model.getComplaint());

                updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/completedComplaints/"+model.getComplaint().getComplaintId(),complaint);

                updateDatabaseValue.put("/Requests/"+model.getRequestId()+"/approvedDate",day+"/"+month+"/"+year);
                updateDatabaseValue.put("/Requests/"+model.getRequestId()+"/complaint/completedDate",day+"/"+month+"/"+year);
                updateDatabaseValue.put("/Requests/"+model.getRequestId()+"/complaint/status",5);

                updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/completedRequests/"+model.getRequestId(),request);

                updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/load",Integer.parseInt(load)-1);
                updateDatabaseValue.put("/Complaints/"+model.getComplaint().getComplaintId()+"/mechanic/load",Integer.parseInt(load)-1);
                updateDatabaseValue.put("/Requests/"+model.getRequestId()+"/complaint/mechanic/load",Integer.parseInt(load)-1);



                // delete data
                updateDatabaseValue.put("/Users/Manager/"+user.getUid()+"/pendingComplaints/"+model.getComplaint().getComplaintId(),null);
                updateDatabaseValue.put("/Users/Manager/"+user.getUid()+"/pendingRequests/"+model.getRequestId(),null);
                updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/pendingComplaints/"+model.getComplaint().getComplaintId(),null);
                updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/pendingRequests/"+model.getRequestId(),null);

                FirebaseDatabase.getInstance().getReference().updateChildren(updateDatabaseValue);

                final HashMap<String,Object> updateDatabaseValue1 = new HashMap<>();

                updateDatabaseValue1.put("/Users/Manager/"+user.getUid()+"/completedComplaints/"+model.getComplaint().getComplaintId()+"/completedDate",day+"/"+month+"/"+year);
                updateDatabaseValue1.put("/Users/Manager/"+user.getUid()+"/completedComplaints/"+model.getComplaint().getComplaintId()+"/status",5);

                updateDatabaseValue1.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/completedComplaints/"+model.getComplaint().getComplaintId()+"/completedDate",day+"/"+month+"/"+year);
                updateDatabaseValue1.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/completedComplaints/"+model.getComplaint().getComplaintId()+"/status",5);

                updateDatabaseValue1.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/completedRequests/"+model.getRequestId()+"/approvedDate",day+"/"+month+"/"+year);
                updateDatabaseValue1.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/completedRequests/"+model.getRequestId()+"/complaint/completedDate",day+"/"+month+"/"+year);
                updateDatabaseValue1.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/completedRequests/"+model.getRequestId()+"/complaint/status",5);

                updateDatabaseValue1.put("/Users/Manager/"+user.getUid()+"/completedComplaints/"+model.getComplaint().getComplaintId()+"/mechanic/load",Integer.parseInt(load)-1);

                FirebaseDatabase.getInstance().getReference().updateChildren(updateDatabaseValue1);

                String serviceDate;
                String serviceMan;
                String description;
                String complaintId;
                float cost;

                serviceDate = day+"/"+month+"/"+year;
                serviceMan = model.getComplaint().getMechanic().getUserName();
                description = model.getDescription();
                complaintId = String.valueOf(model.getComplaint().getComplaintId());
                cost = 2500;

                PastRecord pastRecord = new PastRecord(serviceDate, serviceMan, description, true, complaintId, cost);

                HashMap<String, Object> hashMap = new HashMap<>();

                hashMap.put("/Machines/"+model.getComplaint().getMachine().getMachineId()+"/pastRecords/"+model.getRequestId(),pastRecord);

                hashMap.put("/Users/Manager/"+user.getUid()+"/myMachines/"+model.getComplaint().getMachine().getMachineId()+"/pastRecords/"+model.getRequestId(),pastRecord);

                FirebaseDatabase.getInstance().getReference().updateChildren(hashMap);

                Intent i = new Intent(c, RatingActivity.class);
                i.putExtra("serviceManUid",model.getComplaint().getMechanic().getUid());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);

            }
            else
            {
                final HashMap<String,Object> updateDatabaseValue = new HashMap<>();

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                month = month+1;
                int day = cal.get(Calendar.DAY_OF_MONTH);

                //add data
                updateDatabaseValue.put("/Requests/"+model.getRequestId()+"/approvedDate",day+"/"+month+"/"+year);
                updateDatabaseValue.put("/Requests/"+model.getRequestId()+"/complaint/status",2);

                updateDatabaseValue.put("/Users/Manager/"+user.getUid()+"/pendingComplaints/"+model.getComplaint().getComplaintId()+"/status",2);

                updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/pendingComplaints/"+model.getComplaint().getComplaintId()+"/status",2);

                updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/completedRequests/"+model.getRequestId(),request);

                updateDatabaseValue.put("/Complaints/"+model.getComplaint().getComplaintId()+"/status",2);


                // delete data

                updateDatabaseValue.put("/Users/Manager/"+user.getUid()+"/pendingRequests/"+model.getRequestId(),null);
                updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/pendingRequests/"+model.getRequestId(),null);

                FirebaseDatabase.getInstance().getReference().updateChildren(updateDatabaseValue);

                final HashMap<String,Object> updateDatabaseValue1 = new HashMap<>();

                updateDatabaseValue1.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/completedRequests/"+model.getRequestId()+"/approvedDate",day+"/"+month+"/"+year);
                updateDatabaseValue1.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/completedRequests/"+model.getRequestId()+"/complaint/status",2);

                FirebaseDatabase.getInstance().getReference().updateChildren(updateDatabaseValue1);

            }
            }
        });

        decline_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> updateDatabaseValue = new HashMap<>();

                updateDatabaseValue.put("/Complaints/"+model.getComplaint().getComplaintId()+"/status",2);
                updateDatabaseValue.put("/Requests/"+model.getRequestId()+"/complaint/status",2);

                updateDatabaseValue.put("/Users/Manager/"+user.getUid()+"/pendingComplaints/"+model.getComplaint().getComplaintId()+"/status",2);

                updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/pendingComplaints/"+model.getComplaint().getComplaintId()+"/status",2);

                updateDatabaseValue.put("/Users/Manager/"+user.getUid()+"/pendingRequests/"+model.getRequestId()+"/complaint/status",2);

                updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/pendingRequests/"+model.getRequestId()+"/complaint/status",2);


                updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/pendingComplaints/"+model.getComplaint().getComplaintId()+"/status",2);

                // delete data

                updateDatabaseValue.put("/Users/Manager/"+user.getUid()+"/pendingRequests/"+model.getRequestId(),null);
                updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/pendingRequests/"+model.getRequestId(),null);

                FirebaseDatabase.getInstance().getReference().updateChildren(updateDatabaseValue);
            }
        });

        }

    }

}