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
import com.example.manager.models.Request;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
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

        public MyHolder(@NonNull final View itemView) {
            super(itemView);

            requestid1 = itemView.findViewById(R.id.request_id1);
            serviceman1 = itemView.findViewById(R.id.serviceman1);
            complain_id = itemView.findViewById(R.id.complain_id);
            description = itemView.findViewById(R.id.description);
            accept_button = itemView.findViewById(R.id.accept_button);
            decline_button = itemView.findViewById(R.id.decline_button);

        }

        public void  bind(Request model)
        {
            serviceman1.setText("vikas");
            requestid1.setText(String.valueOf(model.getRequestId()));
            complain_id.setText(String.valueOf(model.getComplaint().getComplaintId()));
            description.setText(model.getDescription());

            FirebaseDatabase firebaseDatabase;
            DatabaseReference complaintReference,loadValue;

            complaintReference = FirebaseDatabase.getInstance().getReference("Complaints").child(String.valueOf(model.getComplaint().getComplaintId()));
            loadValue = FirebaseDatabase.getInstance().getReference("Users").child("Mechanic").child(model.getComplaint().getMechanic().getUid()).child("load");


//            loadValue.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    load = dataSnapshot.getValue().toString();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });

//        myholder.accept_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            if(x.get(position).isStatus())
//            {
//                final HashMap<String,Object> updateDatabaseValue = new HashMap<>();
//
//                //add data
//                Calendar cal = Calendar.getInstance();
//                int year = cal.get(Calendar.YEAR);
//                int month = cal.get(Calendar.MONTH);
//                month = month+1;
//                int day = cal.get(Calendar.DAY_OF_MONTH);
//
//
//                updateDatabaseValue.put("/Complaints/"+x.get(position).getComplaintId()+"/complaintCompletedDate",day+"/"+month+"/"+year);
//                updateDatabaseValue.put("/Users/ResponsibleMan/"+x.get(position).getResponsible()+"/completedComplaintList/"+x.get(position).getComplaintId(),"true");
//                updateDatabaseValue.put("/Users/ServiceMan/"+x.get(position).getServiceMan()+"/completedComplaintList/"+x.get(position).getComplaintId(),"true");
//                updateDatabaseValue.put("/Users/ServiceMan/"+x.get(position).getServiceMan()+"/completedRequestList/"+x.get(position).getRequestid(),"true");
//                updateDatabaseValue.put("/Users/ServiceMan/"+x.get(position).getServiceMan()+"/load",Integer.parseInt(load)-1);
//                updateDatabaseValue.put("/Complaints/"+x.get(position).getComplaintId()+"/status",5);
//
//                // delete data
//                updateDatabaseValue.put("/Users/ResponsibleMan/"+x.get(position).getResponsible()+"/pendingComplaintList/"+x.get(position).getComplaintId(),null);
//                updateDatabaseValue.put("/Users/ResponsibleMan/"+x.get(position).getResponsible()+"/pendingRequestList/"+x.get(position).getRequestid(),null);
//                updateDatabaseValue.put("/Users/ServiceMan/"+x.get(position).getServiceMan()+"/pendingComplaintList/"+x.get(position).getComplaintId(),null);
//                updateDatabaseValue.put("/Users/ServiceMan/"+x.get(position).getServiceMan()+"/pendingRequestList/"+x.get(position).getRequestid(),null);
//
//                FirebaseDatabase.getInstance().getReference("Complaints")
//                        .child(x.get(position).getComplaintId()).child("complaintMachineId").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        String machineId = dataSnapshot.getValue().toString();
//                        Log.i("machineId",machineId);
//                        updateDatabaseValue.put("/machines/"+machineId+"/pastRecords/"+x.get(position).getRequestid(),"true");
//                        FirebaseDatabase.getInstance().getReference().updateChildren(updateDatabaseValue);
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//
//
//
//                Intent i = new Intent(c, RatingActivity.class);
//                i.putExtra("serviceManUid",x.get(position).getServiceMan());
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                c.startActivity(i);
//
//            }
//            else
//            {
//                final HashMap<String,Object> updateDatabaseValue = new HashMap<>();
//
//                //add data
//
//                updateDatabaseValue.put("/Users/ServiceMan/"+x.get(position).getServiceMan()+"/completedRequestList/"+x.get(position).getRequestid(),"true");
//                updateDatabaseValue.put("/Complaints/"+x.get(position).getComplaintId()+"/status",2);
//
//
//                // delete data
//
//                updateDatabaseValue.put("/Users/ResponsibleMan/"+x.get(position).getResponsible()+"/pendingRequestList/"+x.get(position).getRequestid(),null);
//                updateDatabaseValue.put("/Users/ServiceMan/"+x.get(position).getServiceMan()+"/pendingRequestList/"+x.get(position).getRequestid(),null);
//
//                FirebaseDatabase.getInstance().getReference("Complaints")
//                        .child(x.get(position).getComplaintId()).child("complaintMachineId").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        String machineId = dataSnapshot.getValue().toString();
//                        Log.i("machineId",machineId);
//                        updateDatabaseValue.put("/machines/" + machineId + "/pastRecords/" + x.get(position).getRequestid(), "true");
//                        FirebaseDatabase.getInstance().getReference().updateChildren(updateDatabaseValue);
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//
//                });
//            }
//            }
//        });
//
//        myholder.decline_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                HashMap<String,Object> updateDatabaseValue = new HashMap<>();
//
//                updateDatabaseValue.put("/Complaints/"+x.get(position).getComplaintId()+"/status",2);
//
//                // delete data
//
//                updateDatabaseValue.put("/Users/ResponsibleMan/"+x.get(position).getResponsible()+"/pendingRequestList/"+x.get(position).getRequestid(),null);
//                updateDatabaseValue.put("/Users/ServiceMan/"+x.get(position).getServiceMan()+"/pendingRequestList/"+x.get(position).getRequestid(),null);
//
//                FirebaseDatabase.getInstance().getReference().updateChildren(updateDatabaseValue);
//            }
//        });

        }

    }

}