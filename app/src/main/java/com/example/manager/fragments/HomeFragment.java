package com.example.manager.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.example.manager.ComplaintsTabActivity;
import com.example.manager.GenerateQRActivity;
import com.example.manager.PendingRequestActivity;
import com.example.manager.R;
import com.example.manager.ScanQRActivity;
import com.example.manager.adapters.ViewPagerAdapter;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment {

    CardView scan;
    CardView generate, pendingComplaints,pending_approval_request;
    ViewPager viewPager;
    LinearLayout sliderdotspanel;
    private int dotscount;
    private ImageView[] dots;
    Timer timer;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.home_fragment, container, false);


        final Toolbar toolbar=view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(R.drawable.ic_home_toolbar);
        toolbar.setTitleTextAppearance(getActivity(),R.style.TitleTextAppearance);

        //Image Slider
        SliderView sliderView = view.findViewById(R.id.imageSlider);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity());

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEOUTROTATIONTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(Color.parseColor("#275F73"));
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(2); //set scroll delay in seconds :
        sliderView.startAutoCycle();


        scan =(CardView) view.findViewById(R.id.scan);
        generate = (CardView) view.findViewById(R.id.generate);
        pendingComplaints = view.findViewById(R.id.pendingComplaints);
        pending_approval_request = view.findViewById(R.id.pending_approval_request);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDetail();
            }
        });
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), GenerateQRActivity.class);
                startActivity(intent);
            }
        });


        pendingComplaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ComplaintsTabActivity.class);
                startActivity(intent);
            }
        });

        pending_approval_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity().getApplicationContext(), PendingRequestActivity.class);
                startActivity(intent);

            }
        });


        return view;

    }
    public void updateDetail() {
        Intent intent = new Intent(getActivity().getApplicationContext(), ScanQRActivity.class);
        startActivity(intent);
    }



}
