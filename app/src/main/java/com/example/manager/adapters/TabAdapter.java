 package com.example.manager.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.manager.fragments.CompletedComplaintFragment;
import com.example.manager.fragments.PendingComplaintFragment;


 public class TabAdapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;

    public TabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PendingComplaintFragment pendingComplaintFragment = new PendingComplaintFragment();
                return pendingComplaintFragment;
            case 1:
                CompletedComplaintFragment completedComplaintFragment = new CompletedComplaintFragment();
                return completedComplaintFragment;

            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
