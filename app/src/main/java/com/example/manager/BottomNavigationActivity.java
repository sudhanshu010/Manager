package com.example.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.manager.fragments.RecentChatFragment;
import com.example.manager.fragments.HomeFragment;
import com.example.manager.fragments.NotificationFragment;
import com.example.manager.fragments.ProfileFragment;

import java.util.Calendar;

public class BottomNavigationActivity extends AppCompatActivity {

    int old_id = 1;
    public static final String PREFS = "service_reminder";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOurFragment(new HomeFragment(),1,1);
        setContentView(R.layout.activity_bottom_navigation);

        SharedPreferences settings = getSharedPreferences(PREFS, MODE_PRIVATE);

        sharedPreferences = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String s1 = sharedPreferences.getString("service_reminder","");

        if(!s1.equals("Initiated")) {
            Log.i("bhai", "nhi jaanta");

            // Set alarm here

            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            Calendar c = Calendar.getInstance();
            c.set(Calendar.SECOND,0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.HOUR_OF_DAY, 14);

            Intent intent = new Intent(this, ServiceReminder.class);
            PendingIntent pd = PendingIntent.getBroadcast(this, 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC, c.getTimeInMillis(),24*60*60*1000, pd);
            Log.i("alarm is set","bhai");

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("service_reminder", "Initiated");
            editor.apply();


        }
        else
        {
            Log.i("ha bhai", "jaanta h");
            Log.i("bhai", s1);
        }



        final MeowBottomNavigation bottomNavigation = findViewById(R.id.bottom_bar);



        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_chat));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_notification1));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_account));
        bottomNavigation.setCount(2,"5");
        bottomNavigation.setCount(3,"10");
        bottomNavigation.setCount(4,"1");

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

                switch (item.getId()) {
                    case 1:
                        setOurFragment(new HomeFragment(),old_id,1);

                        getSupportActionBar().setLogo(R.drawable.ic_home_toolbar);
                        old_id = 1;
                        break;
                    case 2:
                        setOurFragment(new RecentChatFragment(),old_id,2);
                        old_id = 2;
                        bottomNavigation.clearCount(2);
                        break;
                    case 3:
                        setOurFragment(new NotificationFragment(),old_id,3);
                        old_id = 3;
                        bottomNavigation.clearCount(3);
                        break;
                    case 4:
                        setOurFragment(new ProfileFragment(),old_id,4);
                        bottomNavigation.clearCount(4);
                        old_id = 4;
                }

            }
        });
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {


            }
        });
        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

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
    private void setOurFragment(Fragment fragment,int old_id,int new_id)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        if(old_id<new_id)
        {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        else if(new_id<old_id)
        {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right);
        }

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

