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
import com.example.manager.holders.MyMachinesHolder;
import com.example.manager.models.Machine;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;

import java.util.List;

public class MachineAdapter extends FirebaseRecyclerPagingAdapter<Machine, MyMachinesHolder> {


    /**
     * Construct a new FirestorePagingAdapter from the given {@link DatabasePagingOptions}.
     *
     * @param options
     */
    public MachineAdapter(@NonNull DatabasePagingOptions<Machine> options) {
        super(options);
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
}
