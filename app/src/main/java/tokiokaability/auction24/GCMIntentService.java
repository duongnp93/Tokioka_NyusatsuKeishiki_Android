package tokiokaability.auction24;

import tokiokaability.auction24.activities.BaseActivity;
import tokiokaability.auction24.activities.SplashActivity;

//import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import androidx.core.app.NotificationCompat;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService{

	public GCMIntentService(){
		super(RealLuceAuctionApplication.getConfiguration("gcm_sender_id"));
		
	}
	
	@Override
	protected void onError(Context arg0, String arg1) {
	}

	@Override
	protected void onMessage(Context context, Intent arg1) {
		Bundle data = arg1.getExtras();
		
		String message = data.getString("alert");
		int badge = Integer.valueOf(data.getString("badge"));
        generateNotification(context, message,badge);

	}

	@Override
	protected void onRegistered(Context arg0, String registrationToken) {

		//HTTPService objService = new HTTPService(null);
		
		//objService.registerNotification(RealLuceAuctionApplication.loginID, registrationToken);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
	}

//	@TargetApi(26)
//	private void createChannel(NotificationManager notificationManager,String Id){
//		String name = "Auction Information";
//		String description = "Notification for auction information";
//		int important = NotificationManager.IMPORTANCE_DEFAULT;
//		NotificationChannel mChannel = new NotificationChannel(Id,name,important);
//		mChannel.setDescription(description);
//		mChannel.enableLights(true);
//		mChannel.setLightColor(Color.BLUE);
//		notificationManager.createNotificationChannel(mChannel);
//	}

	private  void generateNotification(Context context, String message, int number) {
        int icon = R.drawable.app_icon_maru;
        long when = System.currentTimeMillis();
        
        
  
        if (BaseActivity.isRunningBackground()){
	        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder nb = new NotificationCompat.Builder(context);
//	        String Id = "CHANNEL_NOTIFICATION_FOREGROUND";


			Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	        //Notification notification = new Notification(icon, message, when);
	        String title = context.getString(R.string.app_name);

	        //notification.setLatestEventInfo(context, title, message, intent);
//			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
//				createChannel(notificationManager,Id);
//				//nb.setChannelId(Id);
//			}
			Intent notificationIntent;
			notificationIntent = new Intent(context, SplashActivity.class);
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
			PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
			nb.setContentIntent(intent);
			nb.setWhen(when)
					.setSmallIcon(icon)
					.setContentTitle(title)
					.setContentText(message)
					.setAutoCancel(true)
					.setNumber(number)
					.setSound(alarmSound)
					.setPriority(NotificationCompat.PRIORITY_HIGH);
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
