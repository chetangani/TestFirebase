package com.chetsgani.testfirebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.chetsgani.testfirebase.MainActivity.SHARED_PREF;
import static com.chetsgani.testfirebase.MainActivity.USER_NAME;

/**
 * Created by cgani on 28-Nov-16.
 */

public class NotifyListener extends Service {

    SharedPreferences sharedPreferences;
    public static boolean notificationlistener = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("debug", "Notification checking");
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

        //Opening shared preference
        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        notificationlistener = true;

        myRef.child("users").child(sharedPreferences.getString(USER_NAME, "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    User value = dataSnapshot.getValue(User.class);
                    Log.d("debug", "Value msg: "+value.getMsg());
                    String msg = value.getMsg();
                    if (!msg.equals("")) {
                        showNotification(msg);
                        msg = "";
                        value = new User(msg);
                        myRef.child("users").child(sharedPreferences.getString(USER_NAME, "")).setValue(value);
                        Log.d("debug", "Notification shown and changed values");
                    } else {
                        Log.d("debug", "no msg still");
                    }
                } catch (NullPointerException e) {
                    Log.d("debug", "Exception no msg still");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return START_STICKY;
    }

    private void showNotification(String msg){
        //Creating a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Intent intent = new Intent(this, SecondActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("Push Notification");
        builder.setContentText(msg);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    @Override
    public void onDestroy() {
        notificationlistener = false;
        Log.d("debug", "Service Stopped");
    }
}
