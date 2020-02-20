package com.example.inventory.responsibleMan.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventory.R;
import com.example.inventory.model.Complaint;
import com.example.inventory.responsibleMan.RMChatActivity;

import java.util.List;

public class RMPendingComplaintAdapter extends  RecyclerView.Adapter<RMPendingComplaintAdapter.MyHolder1>{

    Context c;
    List<Complaint> x ;

    public RMPendingComplaintAdapter(Context c, List<Complaint> x)                                               //Enter the type of data in the space for model
    {
        this.c = c;
        this.x = x;
    }

    @NonNull
    @Override
    public RMPendingComplaintAdapter.MyHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rm_pending_complaint_item,null);
        return new RMPendingComplaintAdapter.MyHolder1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RMPendingComplaintAdapter.MyHolder1 myholder1, int position) {


        myholder1.pendingComplaintDate.setText(x.get(position).getComplaintGeneratedDate());
        myholder1.pendingComplaintDescription.setText(x.get(position).getComplaintDescription());
        myholder1.pendingComplaintServicemanName.setText(x.get(position).getServicemanName());
        myholder1.pendingComplaintId.setText(x.get(position).getComplaintId());
        myholder1.pendingComplaintMachineId.setText(x.get(position).getComplaintMachineId());


        Log.i("asdf","fgh");


    }

    @Override
    public int getItemCount() {
        return x.size();                                                                                   // Return item count from firebase
    }


    class MyHolder1 extends RecyclerView.ViewHolder{

        TextView pendingComplaintDate, pendingComplaintId, pendingComplaintServicemanName, pendin