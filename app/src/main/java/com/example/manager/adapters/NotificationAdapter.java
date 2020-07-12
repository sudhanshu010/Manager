package com.example.manager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.manager.R;
import com.example.manager.models.NotificationModel;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private ArrayList<NotificationModel> notifications;

    public NotificationAdapter(ArrayList<NotificationModel>models) {
        notifications = models;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_cardview,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {

        NotificationModel x = notifications.get(position);
        holder.subject.setText(x.getSubject());
        holder.message.setText(x.getMessage());


    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView subject, message,timeAgo;
        int type;
        CardView cardView;

        public ViewHolder(View view) {
            super(view);

            subject = view.findViewById(R.id.notificationCard_subject);
            message = view.findViewById(R.id.notificationCard_message);
            timeAgo = view.findViewById(R.id.notificationCard_timeAgo);
            cardView = view.findViewById(R.id.notificationCard_cardview);
        }
    }

}