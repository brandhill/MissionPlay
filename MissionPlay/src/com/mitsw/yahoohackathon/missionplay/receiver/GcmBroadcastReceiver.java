package com.mitsw.yahoohackathon.missionplay.receiver;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mitsw.yahoohackathon.missionplay.R;
import com.mitsw.yahoohackathon.missionplay.activity.HomeActivity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GcmBroadcastReceiver extends BroadcastReceiver {
    static final String TAG = "GcmBroadcastReceiver";
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    Context ctx;

    @Override
    public void onReceive(Context context, Intent intent) {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        ctx = context;
        String messageType = gcm.getMessageType(intent);
        if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
            //sendNotification("Send error: " + intent.getExtras().toString());
            sendNotification("有新任務囉！快來試試吧！");
        } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
            //sendNotification("Deleted messages on server: " + intent.getExtras().toString());
            sendNotification("有新任務囉！快來試試吧！");
        } else {
            //sendNotification("Received: " + intent.getExtras().toString());
            sendNotification("有新任務囉！快來試試吧！");
        }
        setResultCode(Activity.RESULT_OK);
    }

    // Put the GCM message into a notification and post it.
    private void sendNotification(String msg) {
        
        Log.d(TAG, "GCM : "+msg);
        
        mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                new Intent(ctx, HomeActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                ctx).setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Mission Play")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}