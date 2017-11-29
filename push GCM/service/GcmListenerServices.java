package com.strategy.intecom.vtc.vgift.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.android.gms.gcm.GcmListenerService;
import com.strategy.intecom.vtc.vgift.MainActivity;
import com.strategy.intecom.vtc.vgift.R;
import com.strategy.intecom.vtc.vgift.common.AppBase;

import java.util.Random;

/**
 * Created by Thuy Chi on 5/17/16.
 */
public class GcmListenerServices extends GcmListenerService {

    public GcmListenerServices() {

    }

    @Override
    public void onMessageReceived(String from, Bundle data) {

        String message = data.getString("message");

        AppBase.showLog("GCM" + " onMessageReceived -------- : " + from + " --------- : " + message);
        initShowNotification(2, "luanpv-titile", message);
    }

    @Override
    public void onDeletedMessages() {
    }

    @Override
    public void onMessageSent(String msgId) {
        AppBase.showLog("GCM:" + " onMessageSent -------- : " + msgId);
    }

    @Override
    public void onSendError(String msgId, String error) {
    }

    /**
     * Show Big content Notification
     *
     * @param msg -->> Message
     **/
    public void initShowNotification(int type, String title, String msg) {

        Bitmap icon1 = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                getApplicationContext()).setAutoCancel(true)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_ALL)
                .setLargeIcon(icon1).setContentText(msg);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(msg);
        bigText.setBigContentTitle(title);
        //bigText.setSummaryText("Por: " + msg);
        mBuilder.setStyle(bigText);
        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(getApplicationContext(),
                MainActivity.class);
        //resultIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        resultIntent.putExtra("type", type);
        resultIntent.putExtra("message", msg);

        // The stack builder object will contain an artificial back
        // stack for
        // the
        // started Activity.
        // getApplicationContext() ensures that navigating backward from
        // the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());

        // Adds the back stack for the Intent (but not the Intent
        // itself)
        stackBuilder.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the
        // stack
        int notiID = new Random().nextInt(1000);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
//        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);// nhiều noti và sẽ chỉ lấy về bundle của noti cuối cùng
        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), notiID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);//noti nào sẽ lấy bundle của noti đó
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(notiID, mBuilder.build());
    }
}
