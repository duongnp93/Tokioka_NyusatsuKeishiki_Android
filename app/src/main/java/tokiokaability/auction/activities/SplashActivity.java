package tokiokaability.auction.activities;

import java.util.Timer;
import java.util.TimerTask;

import tokiokaability.auction.HTTPInfo.BannerInfo;
import tokiokaability.auction.HTTPInfo.HTTPService;
import tokiokaability.auction.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction.R;
import tokiokaability.auction.RealLuceAuctionApplication;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class SplashActivity extends BaseActivity {

	private ImageView imgSplash;
	
	//************************ SCREEN LIFECYCLE ******************
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		imgSplash = findViewById(R.id.imgSplash);
		Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
		fadeIn.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					public void run() {
						//setTransitAnimationType(TRANSIT_ANIMATION_NONE);
						directToActivity(LoginActivity.class);
					}
				}, 1000);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		imgSplash.setAnimation(fadeIn);

	}

	@Override
	protected void onResume() {
		super.onResume();
		NotificationManager nm  = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancelAll();
	}

}