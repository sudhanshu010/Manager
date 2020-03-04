package com.example.manager.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager.R;
import com.example.manager.models.Machine;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;

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
