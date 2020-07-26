package com.example.manager.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.manager.R;
import com.example.manager.models.NotificationModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final int[] mColors = {R.color.list_color_11,R.color.list_color_10,R.color.list_color_9,R.color.list_color_8,
            R.color.list_color_7,R.color.list_color_6,R.color.list_color_5,R.color.list_color_4,R.color.list_color_3,R.color.list_color_2,R.color.list_color_1};

    private final int[] images = {R.drawable.ic_notification_img,R.drawable.ic_plane,R.drawable.ic_service_reminder_img,R.drawable.ic_speaker_img};
    private ArrayList<NotificationModel> notifications;
    Context c;
    public NotificationAdapter(ArrayList<NotificationModel>models,Context c) {
        notifications = models;
        this.c = c;
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
        int bgColor = ContextCompat.getColor(c, mColors[position % 10]);
        holder.imageView.setCircleBackgroundColor(bgColor);
        holder.imageView.setImageResource(images[position % 4]);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView subject, message,timeAgo;
        int type;
        CardView cardView;
        CircleImageView imageView;

        public ViewHolder(View view) {
            super(view);

            subject = view.findViewById(R.id.notificationCard_subject);
            message = view.findViewById(R.id.notificationCard_message);
            timeAgo = view.findViewById(R.id.notificationCard_timeAgo);
//            cardView = view.findViewById(R.id.notificationCard_cardview);
            imageView = view.findViewById(R.id.notification_img);

        }
    }

}