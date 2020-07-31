package com.example.manager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.manager.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

import xyz.hasnat.sweettoast.SweetToast;

public class MachineImageAdapter extends SliderViewAdapter<MachineImageAdapter.SliderAdapterVH> {
private Context context;
private LayoutInflater layoutInflater;
private int[] images ;

public MachineImageAdapter(Context context,int[] images) {
        this.context = context;
        this.images = images;
        }

@Override
public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_viewpager, null);
        return new SliderAdapterVH(inflate);
        }

@Override
public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        Glide.with(viewHolder.itemView)
        .load(images[position])
        .fitCenter()
        .into(viewHolder.imageViewBackground);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        SweetToast.info(context, "This is item in position " + position);
        }
        });
        }

@Override
public int getCount() {
        return images.length;
        }


public class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
    View itemView;
    ImageView imageViewBackground;

    public SliderAdapterVH(View itemView) {
        super(itemView);
        imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
        this.itemView = itemView;

    }
}
}
