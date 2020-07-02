package com.example.manager.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager.ChatActivity;
import com.example.manager.R;
import com.example.manager.models.Chat;
import com.example.manager.models.Complaint;
import com.example.manager.models.Mechanic;
import com.example.manager.models.Request;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecentChatAdapter extends FirebaseRecyclerPagingAdapter<Complaint, RecentChatAdapter.MyHolder> {

    Context c;

    public RecentChatAdapter(@NonNull DatabasePagingOptions<Complaint> options, Context c) {
        super(options);
        this.c = c;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecentChatAdapter.MyHolder viewHolder, int position, @NonNull Complaint model) {
        viewHolder.bind(model);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {

    }

    @NonNull
    @Override
    public RecentChatAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_chat_item,null);
        return new RecentChatAdapter.MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        CircleImageView senderImg;
        TextView senderName,complaintId,unreadmesg;
        LinearLayout chatLL;
        String complaintid;
        DatabaseReference reference;
        FirebaseUser fuser;
        Mechanic mechanic;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            senderImg = itemView.findViewById(R.id.mechanic_image);
            complaintId = itemView.findViewById(R.id.complain_id);
            senderName = itemView.findViewById(R.id.mechanic_name);
            unreadmesg = itemView.findViewById(R.id.unread_mesg_no);
            chatLL = itemView.findViewById(R.id.chat_ll);

            chatLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataSnapshot dataSnapshot = getItem(getAdapterPosition());
                    Complaint complaint = null;
                    if (dataSnapshot != null) {
                        complaint = dataSnapshot.getValue(Complaint.class);
                    }
                    if(complaint!=null && complaint.getMechanic()!=null) {
                        Intent intent = new Intent(c, ChatActivity.class);
                        intent.putExtra("userid", complaint.getMechanic().getUid());
                        intent.putExtra("complaintId", String.valueOf(complaint.getComplaintId()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        c.getApplicationContext().startActivity(intent);
                    }
                }
            });

        }

        public void bind(final Complaint model) {

            complaintid = String.valueOf(model.getComplaintId());
            mechanic = model.getMechanic();
            FirebaseDatabase firebaseDatabase =  FirebaseDatabase.getInstance();
            fuser = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("Complaints").child(complaintid).child("Chats");
            final HashMap<Integer,String> map= new HashMap<Integer,String>();
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Chat chat;
                    if(dataSnapshot.exists()) {
                        for(DataSnapshot ds : dataSnapshot.getChildren())
                        {
                            chat = ds.getValue(Chat.class);
                            if(chat!=null  && (chat.getSender().equals(fuser.getUid()) || chat.getReceiver().equals(fuser.getUid())) && !map.containsValue(complaintid) && mechanic!=null)
                            {
                                senderName.setText(model.getMechanic().getUserName());
                                complaintId.setText(String.valueOf(model.getComplaintId()));
                                map.put(1,String.valueOf(model.getComplaintId()));
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }
}
