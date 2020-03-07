package com.example.manager.adapters;

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

import com.example.manager.ChatActivity;
import com.example.manager.R;
import com.example.manager.RequestStepIndicator;
import com.example.manager.models.Complaint;

import java.util.List;

public class PendingComplaintAdapter extends  RecyclerView.Adapter<PendingComplaintAdapter.MyHolder1>{

    Context c;
    List<Complaint> x ;

    public PendingComplaintAdapter(Context c, List<Complaint> x)                                               //Enter the type of data in the space for model
    {
        this.c = c;
        this.x = x;
    }

    @NonNull
    @Override
    public PendingComplaintAdapter.MyHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_complaint_item,null);
        return new PendingComplaintAdapter.MyHolder1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingComplaintAdapter.MyHolder1 myholder1, int position) {


        myholder1.pendingComplaintDate.setText(x.get(position).getCompletedDate());
        myholder1.pendingComplaintDescription.setText(x.get(position).getDescription());
        myholder1.pendingComplaintServicemanName.setText(x.get(position).getMechanic().getUserName());
        myholder1.pendingComplaintId.setText((int) x.get(position).getComplaintId());
        myholder1.pendingComplaintMachineId.setText(x.get(position).getMachine().getMachineId());


        Log.i("asdf","fgh");


    }

    @Override
    public int getItemCount() {
        return x.size();                                                                                   // Return item count from firebase
    }


    class MyHolder1 extends RecyclerView.ViewHolder{

        TextView pendingComplaintDate, pendingComplaintId, pendingComplaintServicemanName, pendingComplaintDescription, pendingComplaintMachineId;
        Button chatButton, statusButton;

        public MyHolder1(@NonNull View itemView) {
            super(itemView);

            pendingComplaintDate = itemView.findViewById(R.id.rm_pending_complaint_date);
            pendingComplaintId = itemView.findViewById(R.id.rm_pending_complaint_id);
            pendingComplaintDescription = itemView.findViewById(R.id.rm_pending_complaint_desc);
            pendingComplaintServicemanName = itemView.findViewById(R.id.rm_pending_complaint_serviceman);
            pendingComplaintMachineId = itemView.findViewById(R.id.rm_pending_complaint_machine_id);
            chatButton = itemView.findViewById(R.id.rm_chat_button);
            statusButton = itemView.findViewById(R.id.show_status);

            chatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Complaint complaint = x.get(getAdapterPosition());
                    Intent intent = new Intent(c, ChatActivity.class);
                    intent.putExtra("userid", complaint.getMechanic().getUserName());
                    intent.putExtra("complaintId", complaint.getComplaintId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    c.getApplicationContext().startActivity(intent);
                }
            });

            statusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Complaint complaint = x.get(getAdapterPosition());
                    Intent intent = new Intent(c, RequestStepIndicator.class);
                    intent.putExtra("status", complaint.getStatus());
                    intent.putExtra("generated date", complaint.getGeneratedDate());
                    intent.putExtra("serviceman", complaint.getMechanic().getUserName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    c.getApplicationContext().startActivity(intent);
                }
            });

        }
    }

}