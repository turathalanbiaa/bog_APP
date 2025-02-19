package com.example.blog.controller.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.blog.MainActivity;
import com.example.blog.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.example.blog.controller.notification.NotificationUtils.CHANNEL2_ID;

public class FireBaseService extends FirebaseMessagingService {

    private static final String TAG = "Firebase_MSG";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        String chId=CHANNEL2_ID,postId="";
        if(remoteMessage.getNotification().getChannelId()!= null)
            chId=remoteMessage.getNotification().getChannelId();

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Log.d(TAG, "post id: " + remoteMessage.getData().get("id"));
            postId=remoteMessage.getData().get("id");
        }

            sendNotification(postId,chId,remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        Log.d(TAG, "chID: "+remoteMessage.getNotification().getChannelId());
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "onMessageReceived: "+remoteMessage.getNotification().getTitle());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }

    private void sendNotification(String postId,String chID,String messageTitle,String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        if(!postId.equals("")){
        intent.putExtra("notify",1);
        intent.putExtra("postId",postId);}
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        //create channels
//        NotificationUtils nu=new NotificationUtils(getApplicationContext());
//        nu.createChannels();

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, chID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
