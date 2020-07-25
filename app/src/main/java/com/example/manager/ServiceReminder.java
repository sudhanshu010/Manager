package com.example.manager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.shapes.OvalShape;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static android.content.Context.NOTIFICATION_SERVICE;

public class ServiceReminder extends BroadcastReceiver {
    FirebaseUser user;
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("AndroidQ in ", "inside onRecieve");

        user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Manager").child(user.getUid()).child("myMachines");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            List<String>dept;
            List<String>type;
            List<String>UID;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    dept.add(ds.child("department").getValue().toString());
                    type.add(ds.child("type").getValue().toString());
                    UID.add(ds.child("machineId").getValue().toString());
                }

                Log.i("AndroidQ data fetchef once", "true");

                final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("comparisonMachine");
                final int n = dept.size();

                reference1.addListenerForSingleValueEvent(new ValueEventListener() {

                    String datesQ;
                    int count = 0;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {


                        Calendar calender = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
                        String date = dateFormat.format(calender.getTime());
                        String[] todayDate = date.split("/",4);

                        Log.i("AndroidQ Current Calender date", date);

                        for(int i=0;i<n;i++)
                        {
                            datesQ= dataSnapshot1.child(dept.get(i)).child(type.get(i)).child(UID.get(i)).child("nextServiceDate").getValue().toString();
                            String[] arrOfStr = datesQ.split("/",4);

                            int time = Integer.parseInt(todayDate[0])-Integer.parseInt(arrOfStr[0]) + 30*(Integer.parseInt(todayDate[1])-Integer.parseInt(arrOfStr[1])) + 365*(Integer.parseInt(todayDate[2])-Integer.parseInt(arrOfStr[2]));
                            if(time<=5)
                            {
                                count++;
                            }
                        }

                        if(count>0){
                            showNotification(count);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });



    }

    public void showNotification(int count)
    {
        //TODO : Show notification here with text as "These {count} machine needs service."
    }


}
