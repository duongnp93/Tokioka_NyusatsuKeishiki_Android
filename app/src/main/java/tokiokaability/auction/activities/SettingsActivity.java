package tokiokaability.auction.activities;

import tokiokaability.auction.R;
import tokiokaability.auction.RealLuceAuctionApplication;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

public class SettingsActivity extends BaseActivity {
	
	private ImageButton btnShowPrice = null;
	//private SegmentedRadioGroup rdoMaxRowCountGroup = null;
	//private SegmentedRadioGroup rdoViewLanguage = null;
	private Button btnShowAgreement = null;
	private Button btnLogSetting = null;
	private boolean showPrice;
	private TextView lblScreenTitle;
	private Button btnChangePhoneSetting = null;
	Button btnLanguage = null,btnAucSetting = null,btnNyusatsuSetting = null;
	ImageButton btnBid,btnBid2;
	
	//************************ SCREEN LIFECYCLE ******************
		/*
		 * Initialization
		 */
		@Override
		protected void initialization(){
			setContentView(R.layout.activity_settings);
			super.initialization();
			
			SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS, MODE_PRIVATE);
	        
//	        rdoMaxRowCountGroup = (SegmentedRadioGroup) findViewById(R.id.rdoMaxRowCountGroup);
//	        rdoMaxRowCountGroup.setOnCheckedChangeListener(rdoMaxRowCountGroupChanged);
//
//			rdoViewLanguage = (SegmentedRadioGroup) findViewById(R.id.rdoViewLanguage);
//			rdoViewLanguage.setOnCheckedChangeListener(rdoViewLanguageChanged);
	        //btnShowPrice = (ImageButton)findViewById(R.id.btnShowPrice);
	        //btnShowPrice.setOnClickListener(btnShowPriceClick);
	        
	        btnShowAgreement = (Button)findViewById(R.id.btnShowAgreement);
	        btnShowAgreement.setOnClickListener(btnShowAgreementClick);
			lblScreenTitle = (TextView) findViewById(R.id.lblScreenTitle);
	        btnLogSetting = (Button)findViewById(R.id.btnShowLogSetting);
	        btnLogSetting.setOnClickListener(btnLogSettingClick);
			btnChangePhoneSetting = (Button)findViewById(R.id.btnPushSetting);
			btnChangePhoneSetting.setOnClickListener(btnChangePhoneClick);
			btnLanguage = findViewById(R.id.btnLanguageSetting);
			btnLanguage.setOnClickListener(btnLanguageSettingClick);
			btnNyusatsuSetting = findViewById(R.id.btnNyusatsuSetting);
			btnNyusatsuSetting.setOnClickListener(btnNyusatsuSettingClick);
			btnBid = this.findViewById(R.id.btnBit);
			btnBid.setVisibility(View.GONE);
			btnBid.setEnabled(true);
			btnBid2 = this.findViewById(R.id.btnBit2);
			btnBid2.setVisibility(View.GONE);
			btnBid2.setEnabled(true);
			btnBid.setTag("1");
			btnBid2.setTag("2");
			btnAucSetting = findViewById(R.id.btnAucSetting);
			btnAucSetting.setOnClickListener(btnAucSettingClick);
			if(RealLuceAuctionApplication.allPlaces.get(0).getRealFlag().equals("1")){
				btnBid.setVisibility(View.VISIBLE);
				btnBid2.setVisibility(View.VISIBLE);
			}
			else{
				btnBid.setVisibility(View.GONE);
				btnBid2.setVisibility(View.GONE);
			}
	        if (settings.getBoolean(RealLuceAuctionApplication.SETTINGS_SHOW_PRICE, true) == true){
	        	showPrice = true;

	        }
	        else{
	        	showPrice = false;

	        }
	        
	        

			String lang = settings.getString(RealLuceAuctionApplication.SETTINGS_VIEW_LANGUAGE, Locale.getDefault().getLanguage());


			if(Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT)
				setFont();

		}

	private void setFont(){
		AssetManager am = getApplicationContext().getAssets();
		Typeface yGB = Typeface.createFromAsset(am,"fonts/YUGOTHB.TTC");
		btnShowAgreement.setTypeface(yGB);
		lblScreenTitle.setTypeface(yGB);
		btnLogSetting.setTypeface(yGB);
		btnChangePhoneSetting.setTypeface(yGB);
		btnLanguage.setTypeface(yGB);
		btnAucSetting.setTypeface(yGB);
		btnNyusatsuSetting.setTypeface(yGB);
	}
		
		
		
		
		
		
		//************************ LISTENERS ********************************
		
		private OnClickListener btnShowPriceClick = new OnClickListener() {
		    	public void onClick(View v) {
		    		SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS, MODE_PRIVATE);
		    		SharedPreferences.Editor editor = settings.edit();
		    		
		    		showPrice = !showPrice;
		    		editor.putBoolean(RealLuceAuctionApplication.SETTINGS_SHOW_PRICE, showPrice);
		    		editor.commit();
		    		
			        if (settings.getBoolean(RealLuceAuctionApplication.SETTINGS_SHOW_PRICE, true) == true){
			        	btnShowPrice.setImageResource(R.drawable.settings_show_price_button_style);
			        }
			        else{
			        	btnShowPrice.setImageResource(R.drawable.settings_hide_price_button_style);
			        }
		    	}
		};
		


		private OnClickListener btnLogSettingClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				showActivity(SendLogActivity.class);
			}
		};

		private OnClickListener btnChangePhoneClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				showActivity(PushSettingActivity.class);
			}
		};
		private OnClickListener btnNyusatsuSettingClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				showActivity(NyusatsuPushSettingActivity.class);
			}
		};

	private OnClickListener btnLanguageSettingClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			directToActivity(LanguageSettingActivity.class);
		}
	};
	private OnClickListener btnAucSettingClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			directToActivity(AucSettingActivity.class);
		}
	};
		
		private OnClickListener btnShowAgreementClick = new OnClickListener() {
	    	public void onClick(View v) {
	    		RealLuceAuctionApplication.isTopAgree = false;
	    		setActivityParam("showAgreeButton", false);
	    		showActivity(AgreementActivity.class);
//				directToActivity(WebBidActivity.class);
	    	}
	};
}
