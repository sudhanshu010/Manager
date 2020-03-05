package com.example.manager.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.manager.R;
import com.example.manager.models.Machine;

import java.util.List;

public class MachineAdapter extends RecyclerView.Adapter<MachineAdapter.MyHolder1>{

    Context c;
    List<Machine> x ;

    public MachineAdapter(Context c, List<Machine> x)  //Enter the type of data in the space for model
    {
        this.c = c;
        this.x = x;
    }

    @NonNull
    @Override
    public MachineAdapter.MyHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_machine_item,null);
        return new MachineAdapter.MyHolder1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MachineAdapter.MyHolder1 myholder1, int position) {


        myholder1.machineId.setText(x.get(position).getMachineId());
        myholder1.department.setText(x.get(position).getDepartment());
        myholder1.serviceDate.setText("18/01/2020");


        Log.i("asdf","fgh");


    }

    @Override
    public int getItemCount() {
        Log.i("size",String.valueOf(x.size()));
        return x.size();// Return item count from firebase
    }


    class MyHolder1 extends RecyclerView.ViewHolder{

        TextView machineId, department, serviceDate;

        public MyHolder1(@NonNull View itemView) {
            super(itemView);

            machineId = itemView.findViewById(R.id.RecyclerView_MachineId);
            department = itemView.findViewById(R.id.RecyclerView_machine_department);
            serviceDate = itemView.findViewById(R.id.RecyclerView_date_of_last_service);
        }
    }

}
