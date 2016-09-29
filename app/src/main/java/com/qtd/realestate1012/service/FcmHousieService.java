package com.qtd.realestate1012.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.activity.MainActivity;
import com.qtd.realestate1012.constant.AppConstant;

/**
 * Created by DELL on 9/29/2016.
 */
public class FcmHousieService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        showNotification(remoteMessage);
    }

    private void showNotification(RemoteMessage remoteMessage) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder.setContentTitle()
//        builder.setContentText()
        builder.setSmallIcon(R.drawable.ic_home_white);
        builder.setAutoCancel(true);

        Intent intent = new Intent(this, MainActivity.class);
//        send data
//        intent.putExtra(ApiConstant)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(AppConstant.NOTIFICATION_ID, builder.build());
    }
}
