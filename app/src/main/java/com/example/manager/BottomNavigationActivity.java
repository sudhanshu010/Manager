package com.example.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.manager.fragments.HistoryFragment;
import com.example.manager.fragments.HomeFragment;
import com.example.manager.fragments.NotificationFragment;
import com.example.manager.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOurFragment(new HomeFragment());
        setContentView(R.layout.activity_bottom_navigation);
        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottom_bar);

        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_history));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_notification1));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_account));

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

                switch (item.getId()) {
                    case 1:
                        setOurFragment(new HomeFragment());
                        break;
                    case 2:
                        setOurFragment(new HistoryFragment());
                        break;
                    case 3:
                        setOurFragment(new NotificationFragment());
                        break;
                    case 4:
                        setOurFragment(new ProfileFragment());
                }

            }
        });
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Toast.makeText(BottomNavigationActivity.this, "showing item : " + item.getId(), Toast.LENGTH_SHORT).show();

            }
        });
        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                Toast.makeText(BottomNavigationActivity.this, "reselected item : " + item.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        bottomNavigation.show(1,true);


//        bottomNavigationView.setItemIconTintList(null);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId())
//                {
//                    case R.id.home:
//                        setOurFragment(new HomeFragment());
//                        return true;
//
//                    case R.id.history:
//                        setOurFragment(new HistoryFragment());
//                        return true;
//
//                    case R.id.notification:
//                        setOurFragment(new NotificationFragment());
//                        return true;
//
//                    case R.id.profile:
//                        setOurFragment(new ProfileFragment());
//                        return true;
//
//                    default:
//                        return false;
//                }
//
//            }
//        });
    }
    private void setOurFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainframe,fragment);
        fragmentTransaction.commit();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Fragment uploadType = getSupportFragmentManager().findFragmentById(R.id.mainframe);
//        if (uploadType != null) {
//            uploadType.onActivityResult(requestCode, resultCode, data);
//        }
    }
}

