package tokiokaability.auction;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import androidx.annotation.NonNull;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

import tokiokaability.auction.activities.BaseActivity;
import tokiokaability.auction.activities.LoginActivity;
import tokiokaability.auction.activities.TopSearchActivity;
import tokiokaability.auction.commons.Utilities;

public class FCMIntentService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        RealLuceAuctionApplication.fireBaseToken = refreshedToken;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getData().size() > 0){
            try{
                Map<String,String> params = remoteMessage.getData();
                JSONObject object = new JSONObject(params);
                BaseActivity activity = (BaseActivity) BaseActivity.getTopActivity();
                if(activity!= null && activity  instanceof TopSearchActivity){
                    TopSearchActivity topSearchActivity = (TopSearchActivity) activity;
                    topSearchActivity.reLoadAnnounce();
                }
                generateNotification(getApplicationContext(),object.getString("alert"),object.getString("badge"));
            }
            catch (Exception ex){
                ex.getMessage();
            }
            //generateNotification(getApplicationContext(),remoteMessage.getNotification().getBody(),remoteMessage.getData().get("badge"));
        }
    }

    	@TargetApi(26)
	private void createChannel(NotificationManager notificationManager,String Id){
		String name = "Auction Information";
		String description = "Notification for auction information";
		int important = NotificationManager.IMPORTANCE_DEFAULT;
		NotificationChannel mChannel = new NotificationChannel(Id,name,important);
		mChannel.setDescription(description);
		mChannel.enableLights(true);
		mChannel.setLightColor(Color.BLUE);
		notificationManager.createNotificationChannel(mChannel);
	}

    private  void generateNotification(Context context, String message,String number) {
        int icon = R.drawable.app_icon_maru;
        int icon_O = R.drawable.ic_stat_name;
        long when = System.currentTimeMillis();



        if (BaseActivity.isRunningBackground()){
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            String Id = context.getResources().getString(R.string.notification_channel);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
                createChannel(notificationManager,Id);
            }
            NotificationCompat.Builder nb = new NotificationCompat.Builder(context,Id);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            //Notification notification = new Notification(icon, message, when);
            String title = context.getString(R.string.app_name);

            //notification.setLatestEventInfo(context, title, message, intent);


            Intent notificationIntent;
            notificationIntent = new Intent(context, LoginActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
            nb.setContentIntent(intent);
            nb.setWhen(when)
                    .setSmallIcon(icon_O)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setNumber(Integer.parseInt(number))
                    .setSound(alarmSound)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setColor(Color.TRANSPARENT);
//		    notification.sound = alarmSound;
//	        notification.number= number;
//	        notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(0, nb.build());

        }
        else{
            BaseActivity activity = (BaseActivity) BaseActivity.getTopActivity();
            Message msg = new Message();
            Bundle data = new Bundle();

            msg.what = BaseActivity.UICOMMAND_SHOW_NOTIFICATION;
            data.putString("Message", message);
            msg.setData(data);
            activity.UIThreadHandler.sendMessage(msg);
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            //Notification notification = new Notification(icon, "", when);
            //notification.setLatestEventInfo(context, "", "", null);
            //notificationManager.notify(0, notification);
            notificationManager.cancelAll();
        }
    }
}
