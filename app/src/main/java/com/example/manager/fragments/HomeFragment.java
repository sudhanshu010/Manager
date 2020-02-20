package com.example.manager.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

         viewPager= (ViewPager)view.findViewById(R.id.viewpager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().getApplicationContext());
        viewPager.setAdapter(viewPagerAdapter);



        //dots in viewpager
        sliderdotspanel = (LinearLayout) view.findViewById(R.id.slider_dots);

        dotscount=viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(getActivity().getApplicationContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderdotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.nonactive_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){

                    Activity activity = getActivity();
                    if(activity!=null)
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot));
                }

                Activity activity = getActivity();
                if(activity!=null)
                dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.nonactive_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //timer in viewpager
       autoScroll();

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


    final long DELAY = 1000;//delay in milliseconds before auto sliding starts.
    final long PERIOD = 4000; //time in milliseconds between sliding.


    private void autoScroll(){
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if(viewPager.getCurrentItem()==0)
                {
                    viewPager.setCurrentItem(1);
                }
                else if(viewPager.getCurrentItem()==1)
                {
                    viewPager.setCurrentItem(0);
                }
            }
        };

        timer = new Timer(); // creating a new thread
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY, PERIOD);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
