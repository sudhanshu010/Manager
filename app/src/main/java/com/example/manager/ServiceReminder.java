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
import java.util.ArrayList;
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

            List<String>dept=new ArrayList<String>();
            List<String>type = new ArrayList<String>();
            List<String>UID= new ArrayList<String>();

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
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String date = dateFormat.format(calender.getTime());
                        String[] todayDate = date.split("/",4);

                        Log.i("AndroidQ Current Calender date", date);

                        for(int i=0;i<n;i++)
                        {
                            datesQ= dataSnapshot1.child(dept.get(i)).child(type.get(i)).child(UID.get(i)).child("nextServiceTime").getValue().toString();
                            String[] arrOfStr = datesQ.split("/",4);
                            int time = Integer.parseInt(arrOfStr[0])-Integer.parseInt(todayDate[0]) + 30*(Integer.parseInt(arrOfStr[1])-Integer.parseInt(todayDate[1])) + 365*(Integer.parseInt(arrOfStr[2])-Integer.parseInt(todayDate[2]) );
                            Log.i("AndroidQ", String.valueOf(time));
                            if(time<=5)
                            {
                                count++;
                            }
                        }

                        if(count>0){
                            Log.i("AndroidQ", "kuch h yaha jo nhi aa taha");
                            saveinDB("Service Reminder", "Service needed for the "+count+" machine(s)");
                            showNotification(count);

                        }
                        else
                        {
                            Log.i("AndroidQ", "Kuch h hi nhi yaha");
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

    public void showNotification(int count) {
        //TODO : Show notification here with text as "These {count} machine needs service."

        String subject,message;
        int type;



        subject = "service";
        message =  Integer.toString(count);  // "count will be here";


        //saveInDB(subject,message);

        Intent intent = new Intent( MyApplication.getAppContext(), BottomNavigationActivity.class);
        PendingIntent pi = PendingIntent.getActivity(MyApplication.getAppContext(), 101, intent, 0);

        NotificationManager nm = (NotificationManager) MyApplication.getAppContext().getSystemService(NOTIFICATION_SERVICE);

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();

            channel = new NotificationChannel("222", "my_channel", NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(MyApplication.getAppContext(), "222")
                        .setContentTitle(subject)
                        .setAutoCancel(true)

                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .setSummaryText("Reminder")
                                .setBigContentTitle(subject)
                                .bigText(message))

                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentText(message)
                        //.setColor(Color.BLUE)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setContentIntent(pi);

        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        nm.notify(101, builder.build());
    }

    public void saveinDB(String subject, String message)
    {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseHelper db = new DatabaseHelper(MyApplication.getAppContext(), user.getUid());
        String type="4";
        Log.i("NCheck ", "Yaha aa gya");
        boolean isInserted = db.insertData(type, subject, message,user.getUid());

        Log.i("NCheck", "dekh hua kya");

    }

}

