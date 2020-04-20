package com.example.manager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        FirebaseMessaging.getInstance().subscribeToTopic("manager")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (!task.isSuccessful()) {
                            Log.d("firebaseInstance","Can't register to manager");
                        }

                        Log.d("firebaseInstance", "Registered to manager");
                    }
                });
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();

        String subject,message;
        subject = data.get("subject").toString();
        message = data.get("message").toString();

        Intent intent = new Intent(getApplicationContext(), BottomNavigationActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 101, intent, 0);

        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

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
                new NotificationCompat.Builder(getApplicationContext(), "222")
                        .setContentTitle(subject)
                        .setAutoCancel(true)
//                        .setLargeIcon(((BitmapDrawable)getDrawable(R.drawable.lmis_logo)).getBitmap())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .setSummaryText("Manager")
                                .setBigContentTitle(subject)
                                .bigText(message))

                        //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.electro))
                        .setContentText(message)
                        //.setColor(Color.BLUE)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setContentIntent(pi);

        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        nm.notify(101, builder.build());

    }
}
