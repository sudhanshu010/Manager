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
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.manager.R;
import com.example.manager.RequestStepIndicator;
import com.example.manager.models.Complaint;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.firebase.database.DataSnapshot;

import org.parceler.Parcels;

import java.util.List;

public class CompletedComplaintAdapter extends FirebaseRecyclerPagingAdapter<Complaint, CompletedComplaintAdapter.CompletedComplaintHolder> {

    /**
     * Construct a new FirestorePagingAdapter from the given {@link DatabasePagingOptions}.
     *
     * @param options
     */
    Context c;
    private final int[] mColors = {R.color.list_color_1,R.color.list_color_2,R.color.list_color_3,R.color.list_color_4,R.color.list_color_5,
            R.color.list_color_6,R.color.list_color_7,R.color.list_color_8,R.color.list_color_9,R.color.list_color_10,R.color.list_color_11};

public CompletedComplaintAdapter(DatabasePagingOptions<Complaint> options, Context c)                                               //Enter the type of data in the space for model
        {
            super(options);
            this.c = c;

        }

@NonNull


@Override
protected void onBindViewHolder(@NonNull CompletedComplaintAdapter.CompletedComplaintHolder myholder1, int position, Complaint model) {
    int bgColor = ContextCompat.getColor(c, mColors[position % 12]);
    myholder1.cardView.setCardBackgroundColor(bgColor);
    myholder1.bind(model);
    }

    protected void onLoadingStateChanged(@NonNull LoadingState state){

    }

    @Override
    public CompletedComplaintAdapter.CompletedComplaintHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_complaint_item,null);
        return new CompletedComplaintAdapter.CompletedComplaintHolder(view);
    }


class CompletedComplaintHolder extends RecyclerView.ViewHolder{

    TextView pendingComplaintDate, pendingComplaintId, pendingComplaintServicemanName, pendingComplaintDescription, pendingComplaintMachineId;
    Button statusButton;
    CardView cardView;
    public CompletedComplaintHolder(@NonNull View itemView) {
        super(itemView);

        pendingComplaintDate = itemView.findViewById(R.id.rm_complated_complaint_date);
        pendingComplaintId = itemView.findViewById(R.id.rm_complated_complaint_id);
        pendingComplaintDescription = itemView.findViewById(R.id.rm_complated_complaint_desc);
        pendingComplaintServicemanName = itemView.findViewById(R.id.rm_complated_complaint_serviceman);
        pendingComplaintMachineId = itemView.findViewById(R.id.rm_complated_complaint_machine_id);
        statusButton = itemView.findViewById(R.id.rm_status_button);
        cardView = itemView.findViewById(R.id.cardview_com);
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
    public void bind(Complaint model) {

        pendingComplaintDate.setText(model.getCompletedDate());
        pendingComplaintDescription.setText(model.getDescription());
        pendingComplaintServicemanName.setText(model.getMechanic().getUserName());
        pendingComplaintId.setText(String.valueOf(model.getComplaintId()));
        pendingComplaintMachineId.setText(model.getMachine().getMachineId());

    }
}

}