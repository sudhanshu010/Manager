package com.example.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.manager.adapters.TabAdapter;
import com.google.android.material.tabs.TabLayout;

public class ComplaintsTabActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints_tab);

        tabLayout=(TabLayout)findViewById(R.id.r_tabLayout);
        viewPager=(ViewPager)findViewById(R.id.r_viewpager);
        tabLayout.addTab(tabLayout.newTab().setText("PENDING"));
        tabLayout.addTab(tabLayout.newTab().setText("COMPLETED"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });
        final TabAdapter rTabAdapter = new TabAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(rTabAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
