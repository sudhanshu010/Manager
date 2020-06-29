package com.example.manager.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager.R;
import com.example.manager.models.PastRecord;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;

import java.util.List;

public class ShowDetailsAdapter extends FirebaseRecyclerPagingAdapter<PastRecord, ShowDetailsAdapter.MyHolder> {

    Context c;
    /**
     * Construct a new FirestorePagingAdapter from the given {@link DatabasePagingOptions}.
     *
     * @param options
     */
    private final int[] mColors = {R.color.list_color_2,R.color.list_color_3,R.color.list_color_4,R.color.list_color_5,
            R.color.list_color_6,R.color.list_color_7,R.color.list_color_8,R.color.list_color_9,R.color.list_color_10,R.color.list_color_11};


    public ShowDetailsAdapter(DatabasePagingOptions<PastRecord> options, Context c) {
        super(options);
        this.c = c;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_details_item, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myholder, int position, PastRecord model) {
        int bgColor = ContextCompat.getColor(c, mColors[position % 10]);
        myholder.cardview.setCardBackgroundColor(bgColor);
        myholder.bind(model);
    }

    @Override
    protected  void  onLoadingStateChanged(LoadingState state) {

    }

    class MyHolder extends RecyclerView.ViewHolder {


        TextView date;
        CardView cardview;
        TextView CompliantId;
        TextView mechanic;
        TextView Description;
        LinearLayout ll_hide;

        public MyHolder(@NonNull final View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.RecyclerView_Date);
            CompliantId = itemView.findViewById(R.id.RecyclerView_ComplaintId);
            mechanic = itemView.findViewById(R.id.RecyclerView_AgentId);
            Description = itemView.findViewById(R.id.RecyclerView_Description);
            ll_hide = itemView.findViewById(R.id.ll_hide);
            cardview = itemView.findViewById(R.id.cardview);

//            cardview.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    PastRecord past = pastRecords.get(getAdapterPosition());
//                    past.setExpanded(!past.isExpanded());
//                    notifyItemChanged(getAdapterPosition());
//
//                }
//            });

        }
        public void bind(final PastRecord model)
        {
            date.setText(model.getServiceDate());
            CompliantId.setText(model.getComplaintId());
            mechanic.setText(model.getServiceMan());
            Description.setText(model.getDescription());
        }

    }

}