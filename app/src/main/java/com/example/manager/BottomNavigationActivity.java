package com.example.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.example.manager.fragments.HistoryFragment;
import com.example.manager.fragments.HomeFragment;
import com.example.manager.fragments.NotificationFragment;
import com.example.manager.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private HistoryFragment historyFragment;
    private NotificationFragment notificationFragment;
    private ProfileFragment profileFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        bottomNavigationView = findViewById(R.id.bottom_bar);
        homeFragment = new HomeFragment();
        historyFragment = new HistoryFragment();
        notificationFragment = new NotificationFragment();
        profileFragment= new ProfileFragment();
        bottomNavigationView.setItemIconTintList(null);
        setOurFragment(homeFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        setOurFragment(homeFragment);
                        return true;

                    case R.id.history:
                        setOurFragment(historyFragment);
                        return true;

                    case R.id.notification:
                        setOurFragment(notificationFragment);
                        return true;

                    case R.id.profile:
                        setOurFragment(profileFragment);
                        return true;

                    default:
                        return false;
                }

            }
        });
    }
    private void setOurFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainframe,fragment);
        fragmentTransaction.commit();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment uploadType = getSupportFragmentManager().findFragmentById(R.id.mainframe);
        if (uploadType != null) {
            uploadType.onActivityResult(12, resultCode, data);
        }
    }
}

