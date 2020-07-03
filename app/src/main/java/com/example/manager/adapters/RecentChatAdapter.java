package com.example.manager.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager.ChatActivity;
import com.example.manager.R;
import com.example.manager.models.Chat;
import com.example.manager.models.Complaint;
import com.example.manager.models.Mechanic;
import com.example.manager.models.Request;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecentChatAdapter extends FirebaseRecyclerPagingAdapter<Complaint, RecentChatAdapter.MyHolder> {

    Context c;
    View fragmentView;

    public RecentChatAdapter(@NonNull DatabasePagingOptions<Complaint> options, Context c,View fragmentView) {
        super(options);
        this.c = c;
        this.fragmentView = fragmentView;
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
        TextView senderName,complaintId,unreadmesg,lastMessage;
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
            lastMessage = itemView.findViewById(R.id.last_message);
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

            if (getItemCount() == 0) {
                // do empty view thing.
            } else {
                complaintid = String.valueOf(model.getComplaintId());
                complaintId.setText(complaintid);
                senderName.setText(model.getMechanic().getUserName());

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                fuser = FirebaseAuth.getInstance().getCurrentUser();
                reference = FirebaseDatabase.getInstance().getReference("Complaints").child(complaintid).child("Chats");

                reference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        Chat chat = dataSnapshot.getValue(Chat.class);
                        lastMessage.setText(chat.getMessage());
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Chat chat = dataSnapshot.getValue(Chat.class);
                        lastMessage.setText(chat.getMessage());
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (getAdapterPosition() == getItemCount() - 1) {
                            ShimmerFrameLayout shimmerFrameLayout = fragmentView.findViewById(R.id.shimmer_container);
                            RecyclerView recyclerView = fragmentView.findViewById(R.id.recent_chat_rv);
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                        }
                        if (!dataSnapshot.exists()) {
                            itemView.setVisibility(View.GONE);
                        } else {
                            itemView.setVisibility(View.VISIBLE);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        }
    }
}
