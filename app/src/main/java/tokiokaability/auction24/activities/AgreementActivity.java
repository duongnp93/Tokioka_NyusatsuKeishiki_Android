package tokiokaability.auction24.activities;

import tokiokaability.auction24.R;
import tokiokaability.auction24.RealLuceAuctionApplication;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AgreementActivity  extends BaseActivity {
	//ImageButton btnAgree = null;


	boolean showAgreeButton;

	private ImageButton btnBack;
	private TextView lblScreenTitle;
	WebView keiyakuArea;
	RelativeLayout douiArea;
	ImageView douiImg;
	Button douiBtn;

	//************************ SCREEN LIFECYCLE ******************
	/*
	 * Initialization
	 */
	@Override
	protected void initialization(){
		setContentView(R.layout.activity_agreement);
		setStatusBar();
		showAgreeButton = (Boolean)getActivityParam("showAgreeButton",false);

	    this.btnBack = (ImageButton) this.findViewById(R.id.btnBack);

	   	this.lblScreenTitle = (TextView) findViewById(R.id.lblScreenTitle);
		keiyakuArea = findViewById(R.id.keiyakuArea);
		douiArea = findViewById(R.id.douiArea);
		douiBtn = findViewById(R.id.douiBtn);
		douiImg = findViewById(R.id.douiImg);
		super.initialization();
		btnBid.setVisibility(View.GONE);
		btnBid.setEnabled(true);
		btnBid2.setVisibility(View.GONE);
		btnBid2.setEnabled(true);
		if(showAgreeButton){
			douiArea.setVisibility(View.VISIBLE);
		}
		else{
			douiArea.setVisibility(View.GONE);
			if(RealLuceAuctionApplication.allPlaces.get(0).getRealFlag().equals("1")){
				btnBid.setVisibility(View.VISIBLE);
				btnBid2.setVisibility(View.VISIBLE);
			}
			else{
				btnBid.setVisibility(View.GONE);
				btnBid2.setVisibility(View.GONE);
			}
		}
		keiyakuArea.getSettings().setJavaScriptEnabled(true);
		keiyakuArea.setWebViewClient(new WebViewClient());
		keiyakuArea.loadUrl("http://18.180.66.204/tokioka_dev2/keiyaku.html");
		douiBtn.setOnClickListener(douiBtnClick);

//		if(RealLuceAuctionApplication.allPlaces.get(0).getRealFlag().equals("1")){
//			btnBid.setVisibility(View.VISIBLE);
//		}
//		else{
//			btnBid.setVisibility(View.GONE);
//		}
        if(Build.VERSION.SDK_INT!= Build.VERSION_CODES.KITKAT)
        	setFont();
	}

	OnClickListener douiBtnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Handler handle = new Handler();
			handle.postDelayed(new Runnable() {
				@Override
				public void run() {
					douiImg.setImageResource(R.drawable.mn_keiyaku_check);
					setTransitAnimationType(TRANSIT_ANIMATION_NONE);
					setActivityParam("Mode", 5);
					directToActivity(TopSearchActivity.class);
				}
			},500);
		}
	};

	private void setFont(){
		AssetManager am = getApplicationContext().getAssets();
		Typeface yGB = Typeface.createFromAsset(am,"fonts/YUGOTHB.TTC");


		lblScreenTitle.setTypeface(yGB);
	}

	@Override
	public void onBackPressed()
	{
		if (showAgreeButton){
			this.logoff();
		}
		else{
			super.onBackPressed();
		}
	}
	
	


}
