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
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.firebase.database.DataSnapshot;

import org.parceler.Parcels;

import java.util.List;

public class PendingComplaintAdapter extends FirebaseRecyclerPagingAdapter<Complaint, PendingComplaintAdapter.PendingComplaintHolder> {

    /**
     * Construct a new FirestorePagingAdapter from the given {@link DatabasePagingOptions}.
     *
     * @param options
     */
    Context c;

    public PendingComplaintAdapter(@NonNull DatabasePagingOptions<Complaint> options, Context c)                                               //Enter the type of data in the space for model
    {
        super(options);
        this.c = c;
    }



    @Override
    protected void onBindViewHolder(@NonNull PendingComplaintAdapter.PendingComplaintHolder myholder1, int position,@NonNull Complaint model) {

        myholder1.bind(model);
    }

    protected void onLoadingStateChanged(@NonNull LoadingState state){

    }

    @NonNull
    @Override
    public PendingComplaintAdapter.PendingComplaintHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_complaint_item,null);
        return new PendingComplaintAdapter.PendingComplaintHolder(view);
    }



    class PendingComplaintHolder extends RecyclerView.ViewHolder{

        TextView pendingComplaintDate, pendingComplaintId, pendingComplaintServicemanName, pendingComplaintDescription, pendingComplaintMachineId;
        Button chatButton, statusButton;

        public PendingComplaintHolder(@NonNull View itemView) {
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
                    DataSnapshot dataSnapshot = getItem(getAdapterPosition());
                    Complaint complaint = null;
                    if (dataSnapshot != null) {
                        complaint = dataSnapshot.getValue(Complaint.class);
                    }
                    Intent intent = new Intent(c, ChatActivity.class);
                    intent.putExtra("userid", complaint.getMechanic().getUid());
                    intent.putExtra("complaintId", String.valueOf(complaint.getComplaintId()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    c.getApplicationContext().startActivity(intent);
                }
            });

            statusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataSnapshot dataSnapshot = getItem(getAdapterPosition());
                    Complaint complaint = null;
                    if (dataSnapshot != null) {
                        complaint = dataSnapshot.getValue(Complaint.class);
                    }

                    Intent intent = new Intent(c, RequestStepIndicator.class);
                    intent.putExtra("complaint", Parcels.wrap(complaint));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    c.getApplicationContext().startActivity(intent);
                }
            });

        }
        public void bind (Complaint model) {
            pendingComplaintDate.setText(model.getGeneratedDate());
            pendingComplaintId.setText(String.valueOf(model.getComplaintId()));
            pendingComplaintDescription.setText(model.getDescription());
            pendingComplaintServicemanName.setText(model.getMechanic().getUserName());
            pendingComplaintMachineId.setText(model.getMachine().getMachineId());

        }
    }

}