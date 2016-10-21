package com.qtd.realestate1012.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.qtd.realestate1012.R;
import com.qtd.realestate1012.activity.MainActivity;
import com.qtd.realestate1012.constant.ApiConstant;
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
        ArrayMap<String, String> arrayMap = (ArrayMap<String, String>) remoteMessage.getData();

        String idHouse = arrayMap.valueAt(0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Housie - " + getString(R.string.newHomeMatches));
        builder.setContentText(getString(R.string.newHome));
        builder.setSmallIcon(R.drawable.ic_home_white);
        builder.setVibrate(new long[]{
                0, 200, 200, 600, 600
        });
        builder.mNotification.flags = Notification.FLAG_AUTO_CANCEL;

        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(AppConstant.ACTION_NOTIFICATION);
        Bundle bundle = new Bundle();
        bundle.putString(ApiConstant._ID_HOUSE, idHouse);
        intent.putExtras(bundle);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(AppConstant.NOTIFICATION_ID, builder.build());
    }
}
