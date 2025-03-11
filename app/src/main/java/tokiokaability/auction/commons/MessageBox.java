package tokiokaability.auction.commons;

import tokiokaability.auction.R;
import tokiokaability.auction.RealLuceAuctionApplication;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Layout;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;


public class MessageBox {
	
	public static void show(Context context, int titleId, int messageId){
		show(context,context.getString(titleId),context.getString(messageId),null,RealLuceAuctionApplication.getAppContext().getString(R.string.ok));
	}
	
	public static void show(Context context, String title, String message){
		show(context,title,message,null,context.getString(R.string.ok));
	}
	
	public static void show(Context context, int titleId, int messageId, DialogInterface.OnDismissListener onDismissListener){
		show(context,context.getString(titleId),context.getString(messageId),onDismissListener,context.getString(R.string.ok));
	}
	
	
	public static void show(Context context, String title, String message, DialogInterface.OnDismissListener onDismissListener){
		show(context,title,message,onDismissListener,RealLuceAuctionApplication.getAppContext().getString(R.string.ok));
	}
	
	
	public static void show(Context context, int titleId, int messageId, DialogInterface.OnDismissListener onDismissListener, int okTitleId){
		show(context,context.getString(titleId),context.getString(messageId),onDismissListener,context.getString(okTitleId));
	}
	
	
	public static void show(Context context, String title, String message, DialogInterface.OnDismissListener onDismissListener, String okTitle){
		try{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			AlertDialog alertDialog = null; 
			
			builder.setTitle(title);
			builder.setMessage(message);
			builder.setCancelable(false);
			builder.setPositiveButton(okTitle, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			
			alertDialog = builder.create();
			if (onDismissListener != null){
				alertDialog.setOnDismissListener(onDismissListener);
			}
			alertDialog.show();
			
			//Center textView
			// Must call show() prior to fetching views
			TextView messageView = (TextView)alertDialog.findViewById(android.R.id.message);
			messageView.setGravity(Gravity.CENTER);

			TextView titleView = (TextView)alertDialog.findViewById(context.getResources().getIdentifier("alertTitle", "id", "android"));
			if (titleView != null) {
			    titleView.setGravity(Gravity.CENTER);
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public static void showWithTwoButton(Context context, String title, String message, DialogInterface.OnClickListener okClick, String okTitle, String cancelTitle){
		try{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			AlertDialog alertDialog = null;

			builder.setTitle(title);
			builder.setMessage(message);
			builder.setCancelable(true);
			builder.setPositiveButton(okTitle,okClick);
			builder.setNegativeButton(cancelTitle, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			alertDialog = builder.create();
			alertDialog.show();

			//Center textView
			// Must call show() prior to fetching views
			TextView messageView = (TextView)alertDialog.findViewById(android.R.id.message);
			messageView.setGravity(Gravity.CENTER);

			TextView titleView = (TextView)alertDialog.findViewById(context.getResources().getIdentifier("alertTitle", "id", "android"));
			if (titleView != null) {
				titleView.setGravity(Gravity.CENTER);
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}

	}

	public static void showWithTwoButtonTwoAction(Context context, String title, String message, DialogInterface.OnClickListener okClick, DialogInterface.OnClickListener cancelClick, String okTitle, String cancelTitle){
		try{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			AlertDialog alertDialog = null;

			builder.setTitle(title);
			builder.setMessage(message);
			builder.setCancelable(true);
			builder.setPositiveButton(okTitle,okClick);
			builder.setNegativeButton(cancelTitle,cancelClick);
			alertDialog = builder.create();
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.show();

			//Center textView
			// Must call show() prior to fetching views
			TextView messageView = (TextView)alertDialog.findViewById(android.R.id.message);

			messageView.setGravity(Gravity.CENTER);
			messageView.setTextSize(14);
			TextView titleView = (TextView)alertDialog.findViewById(context.getResources().getIdentifier("alertTitle", "id", "android"));
			if (titleView != null) {
				titleView.setGravity(Gravity.CENTER);
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}

	}
}
