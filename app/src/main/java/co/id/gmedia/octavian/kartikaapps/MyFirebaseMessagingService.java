package co.id.gmedia.octavian.kartikaapps;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import co.id.gmedia.octavian.kartikaapps.activity.ActivityDetailPiutang;
import co.id.gmedia.octavian.kartikaapps.util.AppSharedPreferences;
import co.id.gmedia.octavian.kartikaapps.util.Constant;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        AppSharedPreferences.setFcmId(this, s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = "Kartika Apps";
        String body = "anda mendapat notifikasi";
        String type = "";

        if(remoteMessage.getData() != null){
            title = remoteMessage.getData().get("title");
            body = remoteMessage.getData().get("body");
            if(remoteMessage.getData().containsKey("type")){
                type = remoteMessage.getData().get("type");
            }
        }

        if(remoteMessage.getNotification() != null){
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
        }

        Log.d(Constant.TAG, "Notif : " + title + " - " + body);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = getResources().getString(R.string.notification_channel_id);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    new NotificationChannel(NOTIFICATION_CHANNEL_ID, title,
                            NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription(title);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(title)
                .setContentTitle(title)
                .setContentText(body);
        //     .setPriority(Notification.PRIORITY_MAX)
                /*.setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setContentInfo(remoteMessage.getNotification().getTag());*/
        //.setContentInfo(remoteMessage.getData().get("key_1"));

        // notification click action
        Intent notificationIntent;
        if(!AppSharedPreferences.isLoggedIn(this)){
            notificationIntent = new Intent(this, LoginActivity.class);
        }
        else if(type != null && !type.isEmpty()){
            switch (type){
                case "Info":{
                    notificationIntent = new Intent(this, FragmentInfo.class);
                    break;
                }
                default:{
                    notificationIntent = new Intent(this, MainActivity.class);
                    break;
                }
            }
        }
        else if(remoteMessage.getData().containsKey("id_bayar_piutang")){
            notificationIntent = new Intent(this, ActivityDetailPiutang.class);
        }
        else{
            notificationIntent = new Intent(this, MainActivity.class);
        }

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent resultPendingIntent = PendingIntent.getActivity
                (this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(1, notificationBuilder.build());
    }

}
