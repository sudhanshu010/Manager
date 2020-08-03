package com.example.manager;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.renderscript.RenderScript;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.manager.DialogBox.ComplaintDescriptionDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.Nullable;

import xyz.hasnat.sweettoast.SweetToast;

import static com.example.manager.App.CHANNEL_ID;

public class BackgroundService extends Service implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{


        // important global variables
        GoogleApiClient mGoogleApiClient;
        LocationCallback mLocationCallbacks;
        LocationRequest mLocationRequest;
        AudioManager audioManager;
private boolean gotOutOfCampusForFirstTime = false;
final double radiusToCheck = 10.0; // in meter1


@Override
public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // do nothing
        }

@Override
public void onConnectionSuspended(int i) {
        // do nothing
        }

@Override
public void onConnected(@Nullable Bundle bundle) {

        // get my current location
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // update location every 10 seconds

        // now if we have the authority to look into user's current location, do update get it
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest,mLocationCallbacks,null);
        }

        }

@Override
public void onCreate() {
        super.onCreate();


        // the following code needs to be run only once in the entire lifecycle of this service class
        mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addApi(LocationServices.API)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .build();

        // setting the locationCallback functionality
        mLocationCallbacks = new LocationCallback(){

@Override
public void onLocationResult(LocationResult locationResult) {
        super.onLocationResult(locationResult);

        if(locationResult == null){
        return;
        }

        Location location = locationResult.getLastLocation();
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();

        // if the location is inside the defined fence, do following
        if(!isInCampus(latitude,longitude)){
        gotOutOfCampusForFirstTime = true;
            FirebaseAuth.getInstance().signOut();
            stopSelf();

        // phone is in the campus, switch to silence mode, if not already
        // try to put the phone to vibrate mode
//        try{
//        if(audioManager.getRingerMode() != AudioManager.RINGER_MODE_VIBRATE){
//        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//        }
//        }
//        catch (Exception e){
//        // do nothing
//        }
//
//        // try to put the phone in silent mode
//        try{
//        if(audioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT){
//        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//        }
//
//        }
//        catch (Exception e){
//        // do nothing
//        }
        }

        else{
        // put phone into general mode, if not already (only once)
        /*
         * The user can keep either on silent
         * or general,
         * but for the first time, the user gets out of campus,
         * the phone is set to general mode!
         * */
//            FirebaseAuth.getInstance().signOut();
//            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        // else the user is free to set anything
        }
        }
        };
        }

// the following two function checks if the current location is in geo-fence area

// function to find distance between two latitude and longitude
public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(Math.abs(lat2 - lat1));
        double lonDistance = Math.toRadians(Math.abs(lon2 - lon1));

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c * 1000; // distance in meter

        distance = Math.pow(distance, 2);
        return Math.sqrt(distance);
        }

private boolean isInCampus(double x,double y){

// the lat and long of : Webel-It Park
final double X = 28.2698496;
final double Y = 76.1503516;

        // radius up to 200 m is checked
        if(getDistance(X,Y,x,y) <= radiusToCheck){
        return true;
        }
        else{
        return false;
        }
        }


@Override
public int onStartCommand(Intent intent, int flags, int startId) {


        // to keep the activity alive in foreground, show an notification, stating the app status
//        Intent notificationIntent = new Intent(this,LoginActivity.class);
//        Intent[] listOfIntents = new Intent[1];
//        listOfIntents[0] = notificationIntent;
//        PendingIntent pendingIntent = PendingIntent.getActivities(
//        this,0,listOfIntents,0);
//        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
//        .setContentTitle("Background Service Active")
//        .setContentText("Tap to return")
//        .setSmallIcon(R.mipmap.ic_launcher_round)
//        .setContentIntent(pendingIntent)
//        .build();
//
//        startForeground(1,notification);
        if (Build.VERSION.SDK_INT >= 27) {
                String CHANNEL_ID = "my_channel_01";
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);

                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

                Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("")
                        .setContentText("").build();

                startForeground(1, notification);
        }

        // finally connect the googleApiClient
        if(this.mGoogleApiClient != null){
        mGoogleApiClient.connect();
        }

        return START_STICKY; // this will ensure the auto-start of the service
        }

@Override
public void onDestroy() {
        super.onDestroy();

        if(mGoogleApiClient.isConnected()){
        mGoogleApiClient.disconnect();
        }
        Intent intent =  new Intent(getApplicationContext(),ServiceStopActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        }

@Nullable
@Override
public IBinder onBind(Intent intent) {
        return null;
        }
        }