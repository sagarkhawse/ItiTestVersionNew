package com.skteam.ititest.fcm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.skteam.ititest.R;
import com.skteam.ititest.prefrences.SharedPre;
import com.skteam.ititest.ui.home.HomeActivity;

import java.io.IOException;
import java.util.Map;

public class NotificationManager extends FirebaseMessagingService {
    private SharedPre sharedPre;
    private boolean isMuted=false,SendNotification=true;
    private Uri uri;
    private String title,type,body;
    private Intent intent;
    private static final int NOTIFICATION_ID = 3;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        sendRegistrationToServer(s);
    }

    private void sendRegistrationToServer(String token) {

    }
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        sharedPre = SharedPre.getInstance(this);
        isMuted = sharedPre.isNotificationMuted();
        if (remoteMessage != null) {
            try {
                if (isMuted) {
                    uri = null;
                } else {
                    String sound = sharedPre.getNotificationSound();
                    if (sound == null || sound.equals("")) {
                        uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    } else {
                        uri = Uri.parse(sharedPre.getNotificationSound());
                    }
                }
            } catch (Exception e) {

            }
            if (sharedPre.getFirebaseDeviceToken() != null && !sharedPre.getFirebaseDeviceToken().isEmpty()) {
                super.onMessageReceived(remoteMessage);
                try {
                    title = getString(R.string.app_name);
                    type = remoteMessage.getNotification().getTitle();
                    body = remoteMessage.getNotification().getBody();
                    String tag = remoteMessage.getNotification().getTag();
                    Map<String, String> map = remoteMessage.getData();
                    handleDataMessage(map);
                    if (SendNotification) {
                        showNotification(title, body, intent);
                    }

                } catch (Exception e) {
                    Intent intent = new Intent(this, HomeActivity.class);
                    showNotification(title, body, intent);
                }
            }


        }
    }
    private void handleDataMessage(Map<String, String> json){
        Log.e("CIC INSTITUTE FCM", "push json: " + json.toString());

        try {
            if (json != null) {
                String title = json.get("title");
                String message = json.get("body");
                String badge = json.get("badge");
            }
        } catch (Exception e) {
            Log.e("CIC INSTITUTE FCM", "Exception: " + e.getMessage());
        }
    }
    public void showNotification(String title, String body, Intent intent) {
        Bitmap logo;
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(HomeActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
        // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //to be able to launch your activity from the notification
        @SuppressLint("WrongConstant")
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.app_name));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.mipmap.ic_iti);
            builder.setColor(getResources().getColor(R.color.transparent));
        } else {
            builder.setSmallIcon(R.mipmap.ic_iti);
        }
        logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_iti);
        builder.setContentTitle(title)
                .setContentText(body)
                .setLargeIcon(logo)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setColorized(true)
                .setAutoCancel(true)
                .setSound(null)
                .setNotificationSilent()
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setDefaults(Notification.BADGE_ICON_LARGE)
                .setLights(1, 1, 1)
                .setOngoing(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = android.app.NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.app_name), name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            channel.setSound(null, null);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            android.app.NotificationManager notificationManager = getSystemService(android.app.NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        } else {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            Notification notificationCompat = builder.build();
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            managerCompat.notify(NOTIFICATION_ID, notificationCompat);
        }


        if (!sharedPre.isNotificationMuted()) {
            playSound(this);
        }


    }

    public void playSound(Context context) {
        AudioManager myAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int i = myAudioManager.getRingerMode();

        if (myAudioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE)
            uri = Uri.parse(sharedPre.getNotificationSound());

        if (sharedPre.getNotificationSound() == null || sharedPre.getNotificationSound().equals("")) {
            uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
            mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
