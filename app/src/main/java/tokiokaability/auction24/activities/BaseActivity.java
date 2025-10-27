package tokiokaability.auction24.activities;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import tokiokaability.auction24.*;
import tokiokaability.auction24.HTTPInfo.HTTPService;
import tokiokaability.auction24.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction24.HTTPInfo.LoginInfo;
import tokiokaability.auction24.HTTPInfo.PlaceInfo;
import tokiokaability.auction24.commons.MessageBox;
import tokiokaability.auction24.commons.Utilities;
import tokiokaability.auction24.dialog.HelpDialog;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;

import android.system.Os;
import android.telephony.TelephonyManager;
import android.view.*;
import android.view.WindowManager.LayoutParams;
import android.view.View.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.view.animation.*;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public abstract class BaseActivity extends Activity {

	private int subMenuStatus;


	private final Class<?> topScreenClass = TopSearchActivity.class;

	private RelativeLayout subMenu;
	private RelativeLayout subMenuList;
	private RelativeLayout subMenuRate;
	private RelativeLayout subMenuSettings;

	private ImageButton btnSearchMenuCollapse;
	private ImageButton btnMainMenuSearch;
	private ImageButton btnMainMenuBit;
	private ImageButton btnMainMenuList;
	private ImageButton btnMainMenuRate;
	private ImageButton btnMainMenuSetting;


	private ImageButton btnListMenuSuccessfullyBit;
	private ImageButton btnListMenuProduct;
	private ImageButton btnListMenuBit;
	private ImageButton btnListMenuMyList;
	private ImageButton btnListMenuNotification;

	private ImageButton btnRateMenuFreeword;
	private ImageButton btnRateMenuCarName;

	private ImageButton btnSettingsMenuConfiguration;
	private ImageButton btnSettingsMenuMonitor;

	private Button btnLogoff;
	private ImageButton btnBack;
	private ImageButton btnHelp;

	protected ProgressDialog progressDialog;

	private final static int MENU_STATUS_SHOWING_SEARCH = 1;
	private final static int MENU_STATUS_SHOWING_LIST = 2;
	private final static int MENU_STATUS_SHOWING_RATE = 3;
	private final static int MENU_STATUS_SHOWING_SETTINGS = 4;
	private final static int MENU_STATUS_HIDING = 5;
	private final static int MENU_STATUS_DISABLED = 6;


	private final static int MENU_SUBTYPE_LIST = 2;
	private final static int MENU_SUBTYPE_RATE = 3;
	private final static int MENU_SUBTYPE_SETTINGS = 4;


	public final static int MENU_CURRENT_FUNCTION_SEARCH = 1;
	public final static int MENU_CURRENT_FUNCTION_BIT = 5;
	public final static int MENU_CURRENT_FUNCTION_LIST = 16;
	public final static int MENU_CURRENT_FUNCTION_LIST_SUCCESSFULLY_BIT = 6;
	public final static int MENU_CURRENT_FUNCTION_LIST_PRODUCT = 7;
	public final static int MENU_CURRENT_FUNCTION_LIST_BIT = 8;
	public final static int MENU_CURRENT_FUNCTION_LIST_MY_LIST = 9;
	public final static int MENU_CURRENT_FUNCTION_LIST_NOTIFICATION = 10;
	public final static int MENU_CURRENT_FUNCTION_RATE_FREEWORD = 12;
	public final static int MENU_CURRENT_FUNCTION_RATE_CAR_NAME = 13;
	public final static int MENU_CURRENT_FUNCTION_SETTINGS_CONFIGURATION = 14;
	public final static int MENU_CURRENT_FUNCTION_SETTINGS_MONITOR = 15;
	public final static int MENU_CURRENT_FUNCTION_ITEM_SEARCH = 17;

	public static int currentFunction = MENU_CURRENT_FUNCTION_SEARCH;

	protected static List<BaseActivity> screenStack = new ArrayList<BaseActivity>();
	protected static Hashtable<String, Object> transferData = new Hashtable<String, Object>();
	protected int helpResourceId = 0;
	protected HelpDialog helpDialog;
	protected PopupWindow notificationDialog;
	protected static String notificationMessage = "";

	public static int transitAnimationType = 0;
	public final static int TRANSIT_ANIMATION_NONE = 0;
	public final static int TRANSIT_ANIMATION_FADEINOUT = 1;
	public final static int TRANSIT_ANIMATION_FULL_LEFT = 2;
	public final static int TRANSIT_ANIMATION_FULL_RIGHT = 3;
	public final static int TRANSIT_ANIMATION_FULL_UP = 4;
	public final static int TRANSIT_ANIMATION_FULL_DOWN = 5;


	public final static int UICOMMAND_SHOW_NOTIFICATION = 1;


	public boolean isRunningInBackground;
	public ImageButton btnBid;
	public ImageButton btnBid2;
	//private long mLastClickTime = 0;
	// ロケールの設定
	void setLocale(String lang) {

		Locale locale = new Locale(lang);
		Locale.setDefault(locale);

		// デフォルトの設定を取得
		//Configuration config = getResources().getConfiguration(); // この指定だと、画像の言語切替による変更が反映しない時がある
		Configuration config = new Configuration();
		config.locale = locale;
		// フォントは端末の設定からとってくる。この指定がないと端末設定でフォントサイズを変更しても大きさが変わらない
		config.fontScale = getResources().getConfiguration().fontScale;
		getResources().updateConfiguration(config, null);

	}

	//************************ SCREEN LIFECYCLE ******************
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		fillTransitAnimation();

		if (!(LoginActivity.class.isInstance(this) || SplashActivity.class.isInstance(this) || AgreementActivity.class.isInstance(this)) && (RealLuceAuctionApplication.loginID == null || RealLuceAuctionApplication.loginID.length() <= 0)) {
			logoff();
		}


		// 言語表示の設定
		SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS, MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		String lang = settings.getString(RealLuceAuctionApplication.SETTINGS_VIEW_LANGUAGE, Locale.getDefault().getLanguage());
		setLocale(lang);
		if (lang.equals("ja")) {
			editor.putString(RealLuceAuctionApplication.SETTINGS_VIEW_LANGUAGE, "ja");
		} else {
			editor.putString(RealLuceAuctionApplication.SETTINGS_VIEW_LANGUAGE, "en");
		}


		initialization();

		screenStack.add(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		isRunningInBackground = false;
		if (subMenu != null) {
			subMenu.setVisibility(RelativeLayout.INVISIBLE);
		}

		if (notificationMessage != null && notificationMessage.length() > 0) {
			TextView lblMessage = (TextView) notificationDialog.getContentView().findViewById(R.id.lblMessage);
			lblMessage.setText(notificationMessage);

			findViewById(R.id.blockHeader).post(new Runnable() {
				public void run() {
					notificationDialog.showAsDropDown(findViewById(R.id.blockHeader), 0, 0 - findViewById(R.id.blockHeader).getHeight());
				}
			});

		}
	}

	/*
	 * Initialization
	 */
	protected void initialization() {

		this.btnMainMenuSearch = findViewById(R.id.btnMainMenuSearch);
		this.btnMainMenuBit = findViewById(R.id.btnMainMenuBit);
		this.btnMainMenuList = findViewById(R.id.btnMainMenuList);
		btnBid = findViewById(R.id.btnBit);
		btnBid2 = findViewById(R.id.btnBit2);


		if(btnBid!=null){
			btnBid.setOnClickListener(btnMainMenuBitClick);
			btnBid.setTag("1");
		}

		if(btnBid2!=null){
			btnBid2.setOnClickListener(btnMainMenuBitClick);
			btnBid2.setTag("2");
		}

		this.btnMainMenuSetting =findViewById(R.id.btnMainMenuSetting);
		if(btnMainMenuSearch!= null){
			this.btnMainMenuSearch.setOnClickListener(btnMainMenuSearchClick);
//			this.btnMainMenuBit.setOnClickListener(btnMainMenuBitClick);
			this.btnMainMenuBit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					if(btnBid.getVisibility() != View.VISIBLE){
						btnBid.setVisibility(View.VISIBLE);
						btnBid2.setVisibility(View.VISIBLE);
					}
					else{
						btnBid.setVisibility(View.GONE);
						btnBid2.setVisibility(View.GONE);
					}

				}
			});
			this.btnMainMenuList.setOnClickListener(btnMainMenuListClick);
			this.btnMainMenuSetting.setOnClickListener(btnMainMenuSettingsClick);
			resetMainMenuButtons();
		}

		this.btnLogoff = (Button) this.findViewById(R.id.btnLogoff);
		if (this.btnLogoff != null) {
			//this.btnLogoff.setOnClickListener(btnLogoffClick);
			this.btnLogoff.setOnTouchListener(btnLogoffTouchListener);
		}

		this.btnBack = (ImageButton) this.findViewById(R.id.btnBack);
		if (this.btnBack != null) {
			this.btnBack.setOnClickListener(btnBackClick);
		}

		this.btnHelp = (ImageButton) this.findViewById(R.id.btnHelp);
		if (this.btnHelp != null) {
			this.btnHelp.setOnClickListener(btnHelpClick);
		}

		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);

		LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View popupView = layoutInflater.inflate(R.layout.dialog_notification, null);
		notificationDialog = new PopupWindow(popupView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		Button btnDismiss = (Button) popupView.findViewById(R.id.btnClose);
		btnDismiss.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				notificationDialog.dismiss();
				notificationMessage = "";
			}
		});
		if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) setFont();

	}

	private void setFont() {
		AssetManager am = getApplicationContext().getAssets();
		Typeface hiragino_kaku_w6 = Typeface.createFromAsset(am, "fonts/hiragino_kakugo_w6.otf");
		if (this.btnLogoff != null) btnLogoff.setTypeface(hiragino_kaku_w6);
	}

	private View.OnTouchListener btnLogoffTouchListener = new OnTouchListener() {
		Rect rect = null;

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					btnLogoff.setTextColor(Color.parseColor("#80ffffff"));
					rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
					break;

				case MotionEvent.ACTION_UP:
					btnLogoff.setTextColor(Color.parseColor("#ffffff"));
					if (rect.contains((int) (event.getX()), (int) (event.getY()))) {
						HTTPService objService = new HTTPService(new HTTPServiceCallback() {

							@Override
							public void onSucessfully(Object returnData) {

							}

							@Override
							public void onFailed(int errorCode) {
								switch (errorCode) {
									case HTTPService.ERROR_NETWORK:
										MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_xml_connect_gateway);
										break;
									case HTTPService.ERROR_XMLPARSER:
										MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_xml_parser);
										break;
									case HTTPService.ERROR_RETURN_DATA_INVALID:
										MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_return_data_invalid);
										break;
									case HTTPService.ERROR_SERVER_ERROR:
										MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_server_error);
										break;
									default:
										MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_unknow);
										break;

								}
							}
						});
						final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(SplashActivity.TELEPHONY_SERVICE);
						//objService.login(RealLuceAuctionApplication.loginID,"",tm.getDeviceId(),"4");
						logoff();
					}
					break;
			}
			return true;
		}
	};

	/*
	 * Close activity or log out.
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// スタート画面なら画面を閉じる、それ以外なら戻るボタンを押したときと同じ動作
		//if (RealLuceAuctionApplication.loginID == null){
		if (this instanceof SplashActivity) {
			finish();
		} else {
			closeActivity();
		}
	}

	/*
	 * Called when child activity close by call "closeActivity" method
	 * Cac activity nen implement method nay de nhan du lieu tra ve va xu ly.
	 */
	public void onChildActivityClosed(BaseActivity fromActivity) {

	}

	public void onClosing() {

	}

	public void onClosed() {

	}
	public void setStatusBar(){
		if(android.os.Build.VERSION.SDK_INT > 14){
			ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.statusBar), (v, insets) -> {
				Insets statusBars = insets.getInsets(WindowInsetsCompat.Type.statusBars());
				v.getLayoutParams().height = statusBars.top;
				v.requestLayout();
				return insets;
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (RealLuceAuctionApplication.loginID == null || RealLuceAuctionApplication.loginID.length() <= 0) {
			directToActivity(LoginActivity.class);
		}
		hideProgressDialog();
		fillTransitAnimation();
		super.onActivityResult(requestCode, resultCode, data);
	}


	@Override
	protected void onPause() {
		super.onPause();
		isRunningInBackground = true;
	}

	@Override
	protected void onStop() {
		if (helpDialog != null && helpDialog.isShowing()) {
			helpDialog.dismiss();
		}
		if (notificationDialog != null && notificationDialog.isShowing()) {
			notificationDialog.dismiss();
		}
		if (progressDialog != null && progressDialog.isShowing()) {
			hideProgressDialog();
		}


		super.onStop();
		isRunningInBackground = true;


	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		screenStack.remove(this);

		notificationDialog = null;
		helpDialog = null;
		progressDialog = null;

		if (btnSearchMenuCollapse != null)
			btnSearchMenuCollapse.setOnClickListener(null);
		btnSearchMenuCollapseClick = null;

		if (btnMainMenuSearch != null)
			btnMainMenuSearch.setOnClickListener(null);
		btnMainMenuSearchClick = null;

		if (btnMainMenuBit != null)
			btnMainMenuBit.setOnClickListener(null);
		btnMainMenuBitClick = null;

		if (btnMainMenuList != null)
			btnMainMenuList.setOnClickListener(null);
		btnMainMenuListClick = null;

		if (btnMainMenuRate != null)
			btnMainMenuRate.setOnClickListener(null);
		btnMainMenuRateClick = null;

		if (btnMainMenuSetting != null)
			btnMainMenuSetting.setOnClickListener(null);
		btnMainMenuSettingsClick = null;


		if (btnListMenuSuccessfullyBit != null)
			btnListMenuSuccessfullyBit.setOnClickListener(null);
		btnListMenuSuccessfullyBitClick = null;

		if (btnListMenuProduct != null)
			btnListMenuProduct.setOnClickListener(null);
		btnListMenuProductClick = null;

		if (btnListMenuBit != null)
			btnListMenuBit.setOnClickListener(null);
		btnListMenuBitClick = null;

		if (btnListMenuMyList != null)
			btnListMenuMyList.setOnClickListener(null);
		btnListMenuMyListClick = null;

		if (btnListMenuNotification != null)
			btnListMenuNotification.setOnClickListener(null);
		btnListMenuNotificationClick = null;

		if (btnRateMenuFreeword != null)
			btnRateMenuFreeword.setOnClickListener(null);
		btnRateMenuFreewordClick = null;

		if (btnRateMenuCarName != null)
			btnRateMenuCarName.setOnClickListener(null);
		btnRateMenuCarNameClick = null;

		if (btnSettingsMenuConfiguration != null)
			btnSettingsMenuConfiguration.setOnClickListener(null);
		btnSettingsMenuConfigurationClick = null;

		if (btnSettingsMenuMonitor != null)
			btnSettingsMenuMonitor.setOnClickListener(null);
		btnSettingsMenuMonitorClick = null;

		if (btnLogoff != null)
			btnLogoff.setOnClickListener(null);
		//btnLogoffClick = null;
		btnLogoffTouchListener = null;

		if (btnBack != null)
			btnBack.setOnClickListener(null);
		btnBackClick = null;

		if (btnHelp != null)
			btnHelp.setOnClickListener(null);
		btnHelpClick = null;

		showMenuAnimationListener = null;
		hideMenuAnimationListener = null;
		UIThreadHandler = null;

	}


	//******************** BASE METHODS ****************************
	protected Activity getActivityContext() {
		return this;
	}

	/*
	 * Clear History and log out.
	 */
	protected void logoff() {
		clearActivityHistory();
		screenStack.remove(this);
		//RealLuceAuctionApplication.clearData();
		currentFunction = MENU_CURRENT_FUNCTION_SEARCH;
		directToActivity(LoginActivity.class);
	}

	/*
	 * Finish all activities in stack. Except current activity
	 */
	protected void clearActivityHistory() {
		for (int i = screenStack.size() - 1; i >= 0; i--) {
			BaseActivity activity = screenStack.get(i);
			if (activity != this) {
				screenStack.remove(activity);
				activity.finish();
			}
		}
	}


	protected void directToActivity(Class<?> activityClass) {
		//if (!this.getClass().toString().equals(activityClass.toString())){
		clearActivityHistory();
		screenStack.remove(this);
		Intent intent = new Intent(this, activityClass);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
		finish();
		//}
	}

	protected void clearActivityParam(String key) {
		BaseActivity.transferData.remove(key);
	}


	protected void setActivityParam(String key, Object value) {
		if (value != null) {
			BaseActivity.transferData.put(key, value);
		} else {
			BaseActivity.transferData.remove(key);
		}
	}

	/*
	 * Get data de tra ve activity truoc day.
	 */
	protected Object getActivityParam(String key) {
		return BaseActivity.transferData.get(key);
	}


	/*
	 * Get data de tra ve activity truoc day. Neu khong co thi tra ve defaultValue
	 */
	protected Object getActivityParam(String key, Object defaultValue) {
		if (BaseActivity.transferData.containsKey(key))
			return BaseActivity.transferData.get(key);
		else
			return defaultValue;

	}


	/*
	 * Add activity to top of stack, and show it on screen.
	 * set data de truyen sang man hinh moi, neu can
	 */
	protected void showActivity(Class<?> activityClass) {
		Intent intent = new Intent(this, activityClass);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(intent, 1);

	}



	/*
	 * Remove this activity from stack, hidden it from screen, finish activity
	 */
	protected void closeActivity() {
		onClosing();


		if (screenStack.size() > 1) {
			screenStack.remove(this);
			screenStack.get(screenStack.size() - 1).onChildActivityClosed(this);
			if(this instanceof SearchBoxNoActivity){
				currentFunction = MENU_CURRENT_FUNCTION_SEARCH;
				Intent intent = new Intent(this, topScreenClass);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
			}
			fillTransitAnimation();
			this.finish();
			onClosed();
		} else if (topScreenClass.isInstance(this)) {
			this.logoff();
		} else {
			screenStack.remove(this);
			currentFunction = MENU_CURRENT_FUNCTION_SEARCH;
			Intent intent = new Intent(this, topScreenClass);
			if(this instanceof  LanguageSettingActivity){
				currentFunction = MENU_CURRENT_FUNCTION_SETTINGS_CONFIGURATION;
				intent = new Intent(this, SettingsActivity.class);
			}
			if (this instanceof LoginActivity) {
				this.finish();
				onClosed();
				return;
			}

			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			finish();
			onClosed();
		}


	}


	/*
	 * Set Help Resource Id
	 */
	protected void setHelpResourceId(int helpResourseId) {
		this.helpResourceId = helpResourseId;
	}


	/*
	 * Show Progress Dialog on top screen with message
	 */
	protected void showProgressDialog(int messageId) {
		progressDialog.setMessage(getText(messageId));
		progressDialog.show();
	}

	/*
	 * Hide Progress Dialog, if it is showing.
	 */
	protected void hideProgressDialog() {
		if(progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();
	}


	/*
	 * Set Transit Animation Type
	 */
	public void setTransitAnimationType(int type) {
		transitAnimationType = type;
	}


	public static Activity getTopActivity() {
		if (screenStack != null && screenStack.size() > 0) {
			return screenStack.get(screenStack.size() - 1);
		} else {
			return null;
		}
	}


	public static boolean isRunningBackground() {
		BaseActivity activity = (BaseActivity) getTopActivity();
		if (activity == null || activity.isRunningInBackground) {
			return true;
		} else {
			return false;
		}
	}


	//******************* PRIVATE METHODS ******************************
	private void disableMenuButtons() {
		this.btnSearchMenuCollapse.setEnabled(false);
		this.btnMainMenuSearch.setEnabled(false);
	}

	private void enableMenuButtons() {
		this.btnSearchMenuCollapse.setEnabled(true);
		this.btnMainMenuSearch.setEnabled(true);
	}

	private void fillTransitAnimation() {
		switch (transitAnimationType) {
			case TRANSIT_ANIMATION_NONE:
				overridePendingTransition(0, 0);
				break;
			case TRANSIT_ANIMATION_FADEINOUT:
				//overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
				overridePendingTransition(0, 0);
				break;
			case TRANSIT_ANIMATION_FULL_LEFT:
				overridePendingTransition(R.anim.pull_in_from_right, R.anim.pull_out_to_left);
				break;
			case TRANSIT_ANIMATION_FULL_RIGHT:
				overridePendingTransition(R.anim.pull_in_from_left, R.anim.pull_out_to_right);
				break;
			case TRANSIT_ANIMATION_FULL_UP:
				overridePendingTransition(R.anim.pull_in_from_bottom, R.anim.hold);
				break;
			case TRANSIT_ANIMATION_FULL_DOWN:
				overridePendingTransition(R.anim.hold, R.anim.pull_out_to_bottom);
				break;
		}
	}


	/*
	 * Show Sub Menu
	 */
	private void showSubMenu(int subMenuType) {

		if (subMenuStatus != MENU_STATUS_DISABLED) {

			switch (subMenuType) {
				case MENU_SUBTYPE_LIST:
					this.subMenuList.setVisibility(RelativeLayout.VISIBLE);
					this.subMenuRate.setVisibility(RelativeLayout.GONE);
					//this.subMenuSettings.setVisibility(RelativeLayout.GONE);
					break;

				case MENU_SUBTYPE_RATE:
					this.subMenuList.setVisibility(RelativeLayout.GONE);
					this.subMenuRate.setVisibility(RelativeLayout.VISIBLE);
					//this.subMenuSettings.setVisibility(RelativeLayout.GONE);
					break;

				case MENU_SUBTYPE_SETTINGS:
					this.subMenuList.setVisibility(RelativeLayout.GONE);
					this.subMenuRate.setVisibility(RelativeLayout.GONE);
					//this.subMenuSettings.setVisibility(RelativeLayout.VISIBLE);
					break;


			}

			if (subMenuStatus == MENU_STATUS_HIDING) {
				subMenu.setVisibility(RelativeLayout.VISIBLE);
				Animation animation = AnimationUtils.loadAnimation(getActivityContext(), R.anim.show_sub_menu);
				animation.setAnimationListener(showMenuAnimationListener);
				subMenu.startAnimation(animation);
			}


			switch (subMenuType) {
				case MENU_SUBTYPE_LIST:
					subMenuStatus = MENU_STATUS_SHOWING_LIST;
					break;

				case MENU_SUBTYPE_RATE:
					subMenuStatus = MENU_STATUS_SHOWING_RATE;
					break;

				case MENU_SUBTYPE_SETTINGS:
					subMenuStatus = MENU_STATUS_SHOWING_SETTINGS;
					break;
			}
		}
	}


	protected void setIntSetting(String key, int value) {
		SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS, MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();

		editor.putInt(key, value);
		editor.commit();
	}


	protected int getIntSetting(String key, int defaultValue) {
		SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS, MODE_PRIVATE);

		return settings.getInt(key, defaultValue);
	}


	protected void setStringSetting(String key, String value) {
		SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS, MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();

		editor.putString(key, value);
		editor.commit();
	}


	protected String getStringSetting(String key, String defaultValue) {
		SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS, MODE_PRIVATE);

		return settings.getString(key, defaultValue);
	}


	/*
	 * Hide Sub Menu
	 */
	private void hideSubMenu() {
		if (subMenuStatus == MENU_STATUS_SHOWING_SEARCH || subMenuStatus == MENU_STATUS_SHOWING_LIST || subMenuStatus == MENU_STATUS_SHOWING_RATE || subMenuStatus == MENU_STATUS_SHOWING_SETTINGS) {
			subMenuStatus = MENU_STATUS_HIDING;
			Animation animation = AnimationUtils.loadAnimation(getActivityContext(), R.anim.hide_sub_menu);
			animation.setAnimationListener(hideMenuAnimationListener);
			subMenu.startAnimation(animation);
		}

	}

	public void resetMainMenuButtons() {

		this.btnMainMenuSearch.setImageResource(R.drawable.mn_home);
		this.btnMainMenuBit.setImageResource(R.drawable.mn_auc);
		this.btnMainMenuList.setImageResource(R.drawable.mn_search);
		this.btnMainMenuSetting.setImageResource(R.drawable.mn_setting);

		switch (currentFunction) {
			case MENU_CURRENT_FUNCTION_SEARCH:
				this.btnMainMenuSearch.setImageResource(R.drawable.mn_home_act);
				break;
			case MENU_CURRENT_FUNCTION_BIT:
				this.btnMainMenuBit.setImageResource(R.drawable.mn_auc);
				break;
			case MENU_CURRENT_FUNCTION_LIST_SUCCESSFULLY_BIT:
			case MENU_CURRENT_FUNCTION_LIST_PRODUCT:
			case MENU_CURRENT_FUNCTION_LIST_BIT:
			case MENU_CURRENT_FUNCTION_LIST_MY_LIST:
			case MENU_CURRENT_FUNCTION_ITEM_SEARCH:
			case MENU_CURRENT_FUNCTION_LIST:
				this.btnMainMenuList.setImageResource(R.drawable.mn_search_act);
				break;

			case MENU_CURRENT_FUNCTION_RATE_FREEWORD:
			case MENU_CURRENT_FUNCTION_RATE_CAR_NAME:
				//this.btnMainMenuRate.setImageResource(R.drawable.menu_actived_rate_button);
				break;

			case MENU_CURRENT_FUNCTION_SETTINGS_CONFIGURATION:
			case MENU_CURRENT_FUNCTION_SETTINGS_MONITOR:
				this.btnMainMenuSetting.setImageResource(R.drawable.mn_setting_act);
				break;

		}

	}

	//******************** LISTENERS ********************
	public OnClickListener btnBackClick = new OnClickListener() {
		public void onClick(View v) {
			InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			View focusingView = getActivityContext().getCurrentFocus();
			if(focusingView!=null){
				im.hideSoftInputFromWindow(focusingView.getWindowToken(),0);
			}
			onBackPressed();
		}
	};



	private OnClickListener btnHelpClick = new OnClickListener() {
		public void onClick(View v) {
			if (helpResourceId > 0 && (helpDialog == null || helpDialog.isShowing() == false)) {
				helpDialog = new HelpDialog(getActivityContext(), new OnClickListener() {
					@Override
					public void onClick(View v) {
						helpDialog.dismiss();
					}

				}, helpResourceId);

				helpDialog.show();
			}
		}
	};


	private OnClickListener btnSearchMenuCollapseClick = new OnClickListener() {
		public void onClick(View v) {
			hideSubMenu();
		}
	};

	private OnClickListener btnMainMenuSearchClick = new OnClickListener() {
		public void onClick(View v) {
			hideSubMenu();
			BaseActivity topActivity = (BaseActivity) BaseActivity.getTopActivity();
			if (currentFunction != MENU_CURRENT_FUNCTION_SEARCH || !TopSearchActivity.class.isInstance(topActivity)) {
				currentFunction = MENU_CURRENT_FUNCTION_SEARCH;
				resetMainMenuButtons();
				setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
				directToActivity(TopSearchActivity.class);
			}
		}
	};

	private OnClickListener btnMainMenuBitClick = new OnClickListener() {
		public void onClick(View v) {
//			if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
//				return;
//			}
//			mLastClickTime = SystemClock.elapsedRealtime();
			RealLuceAuctionApplication.lane_id = v.getTag().toString();
			hideSubMenu();
			loadPlaces();

    	}
	};


	private void loadPlaces(){
		showProgressDialog(R.string.please_waiting);
		HTTPServiceCallback objServiceCallback = new HTTPServiceCallback(){

			@SuppressWarnings("unchecked")
			@Override
			public void onSucessfully(Object returnData) {
				//hideProgressDialog();
				RealLuceAuctionApplication.allPlaces = (ArrayList<PlaceInfo>) returnData;
				PlaceInfo p = RealLuceAuctionApplication.allPlaces.get(0);
				if(p.getAuthorityFlag().equals("1")){
					checkLoginAndShowAuction();
				}
				else{
					MessageBox.show(getActivityContext(),R.string.auction_start_information,R.string.cannot_bid);
				}
			}


			@Override
			public void onFailed(int errorCode) {
				hideProgressDialog();
				switch(errorCode){
					case HTTPService.ERROR_NETWORK:
						MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_xml_connect_gateway);
						break;
					case HTTPService.ERROR_XMLPARSER:
						MessageBox.show(getActivityContext(),R.string.error_title_webservice, R.string.error_message_xml_parser);
						break;
					case HTTPService.ERROR_RETURN_DATA_INVALID:
						MessageBox.show(getActivityContext(),R.string.error_title_webservice, R.string.error_message_return_data_invalid);
						break;
					case HTTPService.ERROR_SERVER_ERROR:
						MessageBox.show(getActivityContext(),R.string.error_title_webservice, R.string.error_message_server_error);
						break;
					case HTTPService.ERROR_CANCEL:
						break;
					default:
						MessageBox.show(getActivityContext(),R.string.error_title_webservice, R.string.error_message_unknow);
						break;
				}
			}

		};

		HTTPService objService = new HTTPService(objServiceCallback);
		objService.getPlacesByLoginID(RealLuceAuctionApplication.loginID,5);
	}

	private void checkLoginAndShowAuction() {

		HTTPService objServiceCheckLogin = new HTTPService(new HTTPServiceCallback() {
			@Override
			public void onSucessfully(Object returnData) {
				LoginInfo loginInfo = (LoginInfo) returnData;

				if (loginInfo.getResult().equalsIgnoreCase("1")) {
					if((RealLuceAuctionApplication.allPlaces.get(0).getAuctionEndFlg().equals("1")&& RealLuceAuctionApplication.lane_id.equals("1"))
					||(RealLuceAuctionApplication.allPlaces.get(0).getAuctionEndFlg5().equals("1")&& RealLuceAuctionApplication.lane_id.equals("2"))){

						MessageBox.show(getActivityContext(), getString(R.string.auction_end_information), getString(R.string.auction_all_lane_close), new DialogInterface.OnDismissListener(){
							@Override
							public void onDismiss(DialogInterface dialog) {
								hideProgressDialog();
								return;
							}
						});
					}
					else{
						if (currentFunction != MENU_CURRENT_FUNCTION_BIT) {
							currentFunction = MENU_CURRENT_FUNCTION_BIT;
						}

						setActivityParam("Place", RealLuceAuctionApplication.allPlaces.get(0));

						setActivityParam("From", "TopSearch");
						setTransitAnimationType(TRANSIT_ANIMATION_FULL_LEFT);
//						hideProgressDialog();
						showActivity(AuctionActivity.class);
					}


				} else if (loginInfo.getResult().equalsIgnoreCase("2")) {
					MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_no_place);
				} else if (loginInfo.getResult().equalsIgnoreCase("8")) {
					MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_blocked_account);

				} else if (loginInfo.getResult().equalsIgnoreCase("9")) {
					MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_wrong_username_password);
				} else {
					MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_wrong_username_password);
				}

			}

			@Override
			public void onFailed(int errorCode) {
				switch (errorCode) {
					case HTTPService.ERROR_NETWORK:
						MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_xml_connect_gateway);
						break;
					case HTTPService.ERROR_XMLPARSER:
						MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_xml_parser);
						break;
					case HTTPService.ERROR_RETURN_DATA_INVALID:
						MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_return_data_invalid);
						break;
					case HTTPService.ERROR_SERVER_ERROR:
						MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_server_error);
						break;
					default:
						MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_unknow);
						break;

				}
			}

		});

		objServiceCheckLogin.checkLogin(RealLuceAuctionApplication.loginID, RealLuceAuctionApplication.password);
	}
	
	private OnClickListener btnMainMenuListClick = new OnClickListener() {
    	public void onClick(View v) {
			if (currentFunction != MENU_CURRENT_FUNCTION_ITEM_SEARCH) {
				currentFunction = MENU_CURRENT_FUNCTION_ITEM_SEARCH;
				resetMainMenuButtons();
				PlaceInfo p = RealLuceAuctionApplication.allPlaces.get(0);
				setActivityParam("KakoFlag","0");
				setActivityParam("SearchMode",1);
				setActivityParam("Mode", 1);
				setActivityParam("PlaceId",p.getIdNo());
				showActivity(SearchBoxNoActivity.class);
			}

    	}
	};
	
	private OnClickListener btnMainMenuRateClick = new OnClickListener() {
    	public void onClick(View v) {
    		if (subMenuStatus == MENU_STATUS_SHOWING_RATE){
    			hideSubMenu();
    		}
    		else{
    			showSubMenu(MENU_SUBTYPE_RATE);
    		}
    	}
	};
	
	
	private OnClickListener btnMainMenuSettingsClick = new OnClickListener() {
    	public void onClick(View v) {
			/*
    		if (subMenuStatus == MENU_STATUS_SHOWING_SETTINGS){
    			hideSubMenu();
    		}
    		else{
    			showSubMenu(MENU_SUBTYPE_SETTINGS);
    		}
    		*/
//			hideSubMenu();
			if (currentFunction != MENU_CURRENT_FUNCTION_SETTINGS_CONFIGURATION){
				currentFunction = MENU_CURRENT_FUNCTION_SETTINGS_CONFIGURATION;
				resetMainMenuButtons();
				setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
				directToActivity(SettingsActivity.class);
			}
    	}
	};
	
	
	
	
	/*
	private OnClickListener btnSearchMenuProductClick = new OnClickListener() {
    	public void onClick(View v) {
    		hideSubMenu();
    		if (currentFunction != MENU_CURRENT_FUNCTION_SEARCH_PRODUCT_NO){
				currentFunction = MENU_CURRENT_FUNCTION_SEARCH_PRODUCT_NO;
				resetMainMenuButtons();
				setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
				setActivityParam("SearchMode",1);
				setActivityParam("Mode", 1);
				directToActivity(SearchProductNoActivity.class);
    		}
    	}
	};
	
	private OnClickListener btnSearchMenuFreewordClick = new OnClickListener() {
    	public void onClick(View v) {
    		hideSubMenu();
    		if (currentFunction != MENU_CURRENT_FUNCTION_SEARCH_FREEWORD || !SearchFreewordActivity.class.isInstance(this)){
				currentFunction = MENU_CURRENT_FUNCTION_SEARCH_FREEWORD;
				resetMainMenuButtons();
				setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
				setActivityParam("SearchMode",1);
				setActivityParam("Mode", 2);
				directToActivity(SearchFreewordActivity.class);
    		}
    	
    	}
	};
	
	private OnClickListener btnSearchMenuCornerClick = new OnClickListener() {
    	public void onClick(View v) {
    		hideSubMenu();
    		if (currentFunction != MENU_CURRENT_FUNCTION_SEARCH_CORNER || !SearchCornerActivity.class.isInstance(this)){
	    		currentFunction = MENU_CURRENT_FUNCTION_SEARCH_CORNER;
	    		resetMainMenuButtons();
	    		setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
	    		setActivityParam("SearchMode",1);
	    		setActivityParam("Mode", 3);
	    		directToActivity(SearchCornerActivity.class);
    		}
    	}
	};
	
	private OnClickListener btnSearchMenuCarNameClick = new OnClickListener() {
    	public void onClick(View v) {
    		hideSubMenu();
    		if (currentFunction != MENU_CURRENT_FUNCTION_SEARCH_CAR_NAME){
	    		currentFunction = MENU_CURRENT_FUNCTION_SEARCH_CAR_NAME;
	    		resetMainMenuButtons();
	    		setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
	    		setActivityParam("SearchMode",1);
	    		setActivityParam("Mode", 4);
	    		directToActivity(ManufacturersActivity.class);
    		}
    	}
	};
	*/
	
	
	private OnClickListener btnListMenuSuccessfullyBitClick = new OnClickListener() {
    	public void onClick(View v) {
    		hideSubMenu();
    		
    		if (currentFunction != MENU_CURRENT_FUNCTION_LIST_SUCCESSFULLY_BIT){
	    		currentFunction = MENU_CURRENT_FUNCTION_LIST_SUCCESSFULLY_BIT;
	    		resetMainMenuButtons();
	    		setActivityParam("Mode", 0);
	    		setActivityParam("SearchMode",3);
	    		setActivityParam("PlaceID","");
	    		setActivityParam("PlaceName", "");
	    		setActivityParam("ItemNo","");
	    		setActivityParam("Freeword","");
	    		setActivityParam("CornerId","");
	    		setActivityParam("MakerID","");
	    		setActivityParam("CarName","");
	    		setActivityParam("OnePriceFlg","0");
	    		setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
	    		directToActivity(ItemsActivity.class);
    		}
    	}
	};
	
	private OnClickListener btnListMenuProductClick = new OnClickListener() {
    	public void onClick(View v) {
    		hideSubMenu();
    		
    		if (currentFunction != MENU_CURRENT_FUNCTION_LIST_PRODUCT){
	    		currentFunction = MENU_CURRENT_FUNCTION_LIST_PRODUCT;
	    		resetMainMenuButtons();
	    		setActivityParam("Mode", 0);
	    		setActivityParam("SearchMode",2);
	    		setActivityParam("PlaceName", "");
	    		setActivityParam("PlaceID","");
	    		setActivityParam("ItemNo","");
	    		setActivityParam("Freeword","");
	    		setActivityParam("CornerId","1");
	    		setActivityParam("MakerID","");
	    		setActivityParam("CarName","");
	    		setActivityParam("OnePriceFlg","0");
	    		setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
	    		directToActivity(ItemsActivity.class);
    		}
    	}
	};
	
	private OnClickListener btnListMenuBitClick = new OnClickListener() {
    	public void onClick(View v) {
    		hideSubMenu();
    		
    		if (currentFunction != MENU_CURRENT_FUNCTION_LIST_BIT){
	    		currentFunction = MENU_CURRENT_FUNCTION_LIST_BIT;
	    		resetMainMenuButtons();
	    		setActivityParam("Mode", 0);
	    		setActivityParam("SearchMode",4);
	    		setActivityParam("PlaceID","");
	    		setActivityParam("PlaceName", "");
	    		setActivityParam("ItemNo","");
	    		setActivityParam("Freeword","");
	    		setActivityParam("CornerId","");
	    		setActivityParam("MakerID","");
	    		setActivityParam("CarName","");
	    		setActivityParam("OnePriceFlg","0");
	    		setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
	    		directToActivity(ItemsActivity.class);
    		}
    	}
	};
	
	private OnClickListener btnListMenuMyListClick = new OnClickListener() {
    	public void onClick(View v) {
    		hideSubMenu();
    		if (currentFunction != MENU_CURRENT_FUNCTION_LIST_MY_LIST){
	    		currentFunction = MENU_CURRENT_FUNCTION_LIST_MY_LIST;
	    		resetMainMenuButtons();
	    		setActivityParam("Mode", 0);
	    		setActivityParam("SearchMode",5);
	    		setActivityParam("PlaceID","");
	    		setActivityParam("PlaceName", "");
	    		setActivityParam("ItemNo","");
	    		setActivityParam("Freeword","");
	    		setActivityParam("CornerId","");
	    		setActivityParam("MakerID","");
	    		setActivityParam("CarName","");
	    		setActivityParam("OnePriceFlg","0");
	    		setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
	    		directToActivity(ItemsActivity.class);
    		}
    	}
	};
	
	
	private OnClickListener btnListMenuNotificationClick = new OnClickListener() {
    	public void onClick(View v) {
    		hideSubMenu();
    		if (currentFunction != MENU_CURRENT_FUNCTION_LIST_NOTIFICATION){
	    		currentFunction = MENU_CURRENT_FUNCTION_LIST_NOTIFICATION;
	    		resetMainMenuButtons();
	    		setActivityParam("Mode", 0);
	    		setActivityParam("SearchMode",6);
	    		setActivityParam("PlaceName", "");
	    		setActivityParam("PlaceID","");
	    		setActivityParam("ItemNo","");
	    		setActivityParam("Freeword","");
	    		setActivityParam("CornerId","");
	    		setActivityParam("MakerID","");
	    		setActivityParam("CarName","");
	    		setActivityParam("OnePriceFlg","0");
	    		setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
	    		directToActivity(ItemsActivity.class);
    		}
    	}
	};
	
	
	private OnClickListener btnRateMenuFreewordClick = new OnClickListener() {
    	public void onClick(View v) {
    		hideSubMenu();
    		BaseActivity topActivity = (BaseActivity) BaseActivity.getTopActivity();
    		if (currentFunction != MENU_CURRENT_FUNCTION_RATE_FREEWORD || !SearchFreewordActivity.class.isInstance(topActivity)){
				currentFunction = MENU_CURRENT_FUNCTION_RATE_FREEWORD;
				resetMainMenuButtons();
				setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
				
				setActivityParam("SearchMode", 7);
				setActivityParam("Mode", 6);
				directToActivity(SearchFreewordActivity.class);
    		}
    	}
	};
	
	
	private OnClickListener btnRateMenuCarNameClick = new OnClickListener() {
    	public void onClick(View v) {
            hideSubMenu();
            BaseActivity topActivity = (BaseActivity) BaseActivity.getTopActivity();
            if (currentFunction != MENU_CURRENT_FUNCTION_RATE_CAR_NAME || !ManufacturersActivity.class.isInstance(topActivity)){
                currentFunction = MENU_CURRENT_FUNCTION_RATE_CAR_NAME;
                resetMainMenuButtons();
                setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);

                setActivityParam("SearchMode", 7);
                setActivityParam("Mode", 7);
                directToActivity(ManufacturersActivity.class);
            }
    	}
	};
	
	
	private OnClickListener btnSettingsMenuConfigurationClick = new OnClickListener() {
    	public void onClick(View v) {
    		hideSubMenu();
    		if (currentFunction != MENU_CURRENT_FUNCTION_SETTINGS_CONFIGURATION){
				currentFunction = MENU_CURRENT_FUNCTION_SETTINGS_CONFIGURATION;
				resetMainMenuButtons();
				setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
				directToActivity(SettingsActivity.class);
    		}
    	}
	};
	
	
	private OnClickListener btnSettingsMenuMonitorClick = new OnClickListener() {
    	public void onClick(View v) {
    		hideSubMenu();
    		if (currentFunction != MENU_CURRENT_FUNCTION_SETTINGS_MONITOR){
				currentFunction = MENU_CURRENT_FUNCTION_SETTINGS_MONITOR;
				resetMainMenuButtons();
				setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
				directToActivity(AuctionStatusActivity.class);
    		}
    	}
	};
	
	
	/*
	 * Sub Menu Animation
	 */
	private Animation.AnimationListener showMenuAnimationListener = new Animation.AnimationListener(){

		@Override
		public void onAnimationStart(Animation animation) {
			disableMenuButtons();
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			enableMenuButtons();
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			
		}
		
	};
	
	
	/*
	 * Sub Menu Animation
	 */
	private Animation.AnimationListener hideMenuAnimationListener = new Animation.AnimationListener(){

		@Override
		public void onAnimationStart(Animation animation) {
			disableMenuButtons();
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			subMenu.setVisibility(RelativeLayout.INVISIBLE);
			enableMenuButtons();
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			
		}
		
	};




	public  Handler UIThreadHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch(msg.what){
			case UICOMMAND_SHOW_NOTIFICATION:
					try{
						Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//						Uri alarmSound = null;
						MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), alarmSound);
						mediaPlayer.start();
					}
					catch (Exception ex){
						Utilities.writeLog(getActivityContext(),RealLuceAuctionApplication.getConfiguration("error_log_file"),ex.getMessage());
					}
					TextView lblMessage = (TextView) notificationDialog.getContentView().findViewById(R.id.lblMessage);
					lblMessage.setText(msg.getData().getString("Message"));
					notificationMessage = msg.getData().getString("Message");
					if (!notificationDialog.isShowing()){
						//notificationDialog.showAtLocation(findViewById(R.id.btnLogoff),Gravity.TOP|Gravity.LEFT ,0,0);
						findViewById(R.id.blockHeader).post(new Runnable() {
							public void run() {
								notificationDialog.showAsDropDown(findViewById(R.id.blockHeader), 0, 0-findViewById(R.id.blockHeader).getHeight());

							}
						});
					}
					break;
			}
			super.handleMessage(msg);
		}
	};

	
	protected void finalize() throws Throwable {
	         super.finalize();

	 }
}
