package com.example.manager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.manager.R;

import com.example.manager.models.Machine;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;

public class MachineAdapter extends FirebaseRecyclerPagingAdapter<Machine, MachineAdapter.MyMachinesHolder> {


    /**
     * Construct a new FirestorePagingAdapter from the given {@link DatabasePagingOptions}.
     *
     * @param options
     */

    Context c;
    public MachineAdapter(@NonNull DatabasePagingOptions<Machine> options,Context c) {
        super(options);

        this.c = c;
    }

    protected void onBindViewHolder(@NonNull MyMachinesHolder viewHolder, int position, @NonNull Machine model) {

        viewHolder.bind(model);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {

    }

    @NonNull
    @Override
    public MyMachinesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_machine_item,null);
        return new MyMachinesHolder(view);
    }

    public class MyMachinesHolder extends RecyclerView.ViewHolder {

        TextView machineId, department, serviceDate;

        public MyMachinesHolder(@NonNull View itemView) {
            super(itemView);

            machineId = itemView.findViewById(R.id.RecyclerView_MachineId);
            department = itemView.findViewById(R.id.RecyclerView_machine_department);
            serviceDate = itemView.findViewById(R.id.RecyclerView_date_of_last_service);
        }

        public void bind(Machine model)
        {
            machineId.setText(model.getMachineId());
            department.setText(model.getDepartment());
            serviceDate.setText(model.getDateOfInstallation());
        }
    }
}
