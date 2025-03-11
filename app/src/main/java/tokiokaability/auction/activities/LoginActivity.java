package tokiokaability.auction.activities;

import java.util.ArrayList;
import java.util.Locale;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;

import tokiokaability.auction.HTTPInfo.BannerInfo;
import tokiokaability.auction.HTTPInfo.MailsendInfo;
import tokiokaability.auction.R;
import tokiokaability.auction.RealLuceAuctionApplication;
import tokiokaability.auction.HTTPInfo.AnnounceInfo;
import tokiokaability.auction.HTTPInfo.HTTPService;
import tokiokaability.auction.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction.HTTPInfo.LoginInfo;
import tokiokaability.auction.HTTPInfo.PlaceInfo;
import tokiokaability.auction.HTTPInfo.PosterInfo;
import tokiokaability.auction.HTTPInfo.SystemInfo;
import tokiokaability.auction.commons.MessageBox;
import tokiokaability.auction.commons.Utilities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;

import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;



public class LoginActivity extends BaseActivity {


	private Button btnLogin = null;

	private EditText txtLoginID = null;
	private EditText txtPassword = null;
	private CheckBox chkSavePassword = null;

	private LoginInfo loginInfo = null;
	private int serviceCount = 0;

	private LinearLayout imgBackground = null;
	private RelativeLayout blockHeader = null;
	private RelativeLayout blockBody = null;
	private Button requestPasswordBtn = null;
	private LinearLayout bannerArea;
	BannerInfo bannerInfo = null;
	MailsendInfo mailsendInfo = null;
	private TextView txtVersion;
	public static final String SETTINGS_SHOW_AGREEMENT = "login_show_agreement";
	public static final int REQUEST_READ_PHONE_STATE = 1;
	TelephonyManager tm;
	ImageView bannerImg;
	RelativeLayout toiawaseArea;
	//************************ SCREEN LIFECYCLE ******************
	/*
	 * Initialization
	 */
	@Override
	protected void initialization() {
		setContentView(R.layout.activity_login);

		super.initialization();

		SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS, MODE_PRIVATE);
		//overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
		//setContentView(R.layout.activity_login);

		//lblScreenTitle = (TextView) findViewById(R.id.lblScreenTitle);



		txtLoginID = (EditText) findViewById(R.id.txtLoginID);




		txtPassword = (EditText) findViewById(R.id.txtPassword);
		txtPassword.setOnEditorActionListener(txtPasswordEditorAction);

		chkSavePassword = (CheckBox) findViewById(R.id.chkBoxSavePassword);

		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(btnLoginClick);


		//btnRegister.setOnClickListener(btnRegisterClick);

		imgBackground = findViewById(R.id.bgrLayout);
		blockHeader = (RelativeLayout) findViewById(R.id.blockHeader);
		blockBody = (RelativeLayout) findViewById(R.id.blockBody);
		bannerImg = findViewById(R.id.bannerTop);
		bannerImg.setOnClickListener(btnBannerClick);
		imgBackground.setOnClickListener(backgroundClick);
		blockHeader.setOnClickListener(backgroundClick);
		blockBody.setOnClickListener(backgroundClick);
		toiawaseArea = findViewById(R.id.toiawaseArea);
		chkSavePassword.setChecked(settings.getBoolean("save_password", false));
		txtLoginID.setText(settings.getString("username", ""));
		txtPassword.setText(settings.getString("password", ""));
		bannerArea = findViewById(R.id.blockBanners);
		txtLoginID.requestFocus();
		requestPasswordBtn = (Button) findViewById(R.id.requestPassBtn);
		requestPasswordBtn.setOnClickListener(requestPasswordOnClick);
		txtVersion = (TextView)findViewById(R.id.versionTxt);
		String version = RealLuceAuctionApplication.getConfiguration("app_version");
		if(version.length() >= 3)
		txtVersion.setText("ver "+version.charAt(0)+"."+version.charAt(1)+"."+version.charAt(2));
		else txtVersion.setText("ver 1.0.0");
        /*
        if (getIntSetting(SETTINGS_SHOW_AGREEMENT,0) == 0){
        	MessageBox.show(getActivityContext(), R.string.login_agreement_title, R.string.login_agreement_content,
        					new DialogInterface.OnDismissListener(){

								@Override
								public void onDismiss(DialogInterface arg0) {
									setIntSetting(SETTINGS_SHOW_AGREEMENT,1);
									
								}
        		
        	} ,R.string.login_agreement_agreed);
        	
        }
        */
		if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) setFont();
		// バナー取得
		//Duongnp Added


		getBanner();
	}

	private void setFont() {
		AssetManager am = getApplicationContext().getAssets();
		Typeface yGB = Typeface.createFromAsset(am, "fonts/YUGOTHB.TTC");
		Typeface yGL = Typeface.createFromAsset(am, "fonts/YUGOTHL.TTC");
		Typeface yGM = Typeface.createFromAsset(am, "fonts/YUGOTHM.TTC");
		Typeface yGR = Typeface.createFromAsset(am, "fonts/YUGOTHR.TTC");
        txtLoginID.setTypeface(yGB);
        txtPassword.setTypeface(yGB);
		btnLogin.setTypeface(yGB);
		chkSavePassword.setTypeface(yGB);
		requestPasswordBtn.setTypeface(yGB);
		txtVersion.setTypeface(yGR);
	}

	
/*
	public void onChildActivityClosed(BaseActivity fromActivity){
		super.onChildActivityClosed(fromActivity);
		if (fromActivity.getClass().isInstance(fromActivity)){
			setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
			directToActivity(SearchProductNoActivity.class);
		}
	}
	*/


	//************************ VALIDATION ***************************
	/*
	 * Validate to login
	 */
	private boolean validateLogin() {
		String strLoginID = txtLoginID.getText().toString();
		String strPassword = txtPassword.getText().toString();

		if (strLoginID.length() == 0) {
			txtLoginID.requestFocus();
			MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_username_required);
			return false;
		}
		if (strPassword.length() == 0) {
			txtPassword.requestFocus();
			MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_password_required);
			return false;
		}

		return true;
	}


	//************************ BUSINESS LOGIC PROCESSING ****************
	/*
	 * Login
	 */
	private void login() {


		btnLogin.setEnabled(false);
		txtLoginID.setEnabled(false);
		txtPassword.setEnabled(false);
		chkSavePassword.setEnabled(false);
		

//		tm = (TelephonyManager) getBaseContext().getSystemService(SplashActivity.TELEPHONY_SERVICE);
//
//
//		if (ActivityCompat.checkSelfPermission(getActivityContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//
//			ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},REQUEST_READ_PHONE_STATE);
//		}
//		else{
//			HTTPService objService = new HTTPService(new HTTPServiceCallback() {
//				@Override
//				public void onSucessfully(Object returnData) {
//					//SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS , MODE_PRIVATE);
//					//SharedPreferences.Editor editor = settings.edit();
//					//editor.putString("workingGateway", RealLuceAuctionApplication.baseURL);
//					//editor.commit();
//					loginInfo = (LoginInfo) returnData;
//					RealLuceAuctionApplication.memberName = loginInfo.getMemberName();
//					if (loginInfo.getResult().equalsIgnoreCase("1") || loginInfo.getResult().equalsIgnoreCase("3")) {
//
//
//						// get system information from server
//						HTTPService objService = new HTTPService(new HTTPServiceCallback() {
//							@Override
//							public void onSucessfully(Object returnData) {
//								RealLuceAuctionApplication.systemInfo = (SystemInfo) returnData;
//
//
//								if (RealLuceAuctionApplication.systemInfo.getStatus() != 0) {
//									MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_server_error);
//									btnLogin.setEnabled(true);
//									txtLoginID.setEnabled(true);
//									txtPassword.setEnabled(true);
//									chkSavePassword.setEnabled(true);
//								} else {
//									if (RealLuceAuctionApplication.systemInfo.getAppVersion().length() > 0 && Integer.parseInt(RealLuceAuctionApplication.systemInfo.getAppVersion()) <= RealLuceAuctionApplication.getIntConfiguration("app_version")) {
//
//										serviceCount = 0;
//										loadAnnounce("1");
//
//
//
//									} else {
//										MessageBox.show(getActivityContext(), R.string.login_error_message_wrong_app_version_title, R.string.login_error_message_wrong_app_version);
//										btnLogin.setEnabled(true);
//										txtLoginID.setEnabled(true);
//										txtPassword.setEnabled(true);
//										chkSavePassword.setEnabled(true);
//									}
//								}
//							}
//
//							@Override
//							public void onFailed(int errorCode) {
//
//								btnLogin.setEnabled(true);
//								txtLoginID.setEnabled(true);
//								txtPassword.setEnabled(true);
//								chkSavePassword.setEnabled(true);
//
//								switch (errorCode) {
//									case HTTPService.ERROR_NETWORK:
//										MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_xml_connect_gateway);
//										break;
//									case HTTPService.ERROR_XMLPARSER:
//										MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_xml_parser);
//										break;
//									case HTTPService.ERROR_RETURN_DATA_INVALID:
//										MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_return_data_invalid);
//										break;
//									case HTTPService.ERROR_SERVER_ERROR:
//										MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_server_error);
//										break;
//									case HTTPService.ERROR_CANCEL:
//										break;
//									default:
//										MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_unknow);
//										break;
//
//								}
//
//								RealLuceAuctionApplication.systemInfo = null;
//							}
//
//						});
//
//						objService.getSystemInfo();
//
//					}
//				/*
//				else if (loginInfo.getResult().equalsIgnoreCase("2")){
//					btnLogin.setEnabled(true);
//					txtLoginID.setEnabled(true);
//					txtPassword.setEnabled(true);
//					chkSavePassword.setEnabled(true);
//					MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_no_place);
//				}
//				*/
//					else if (loginInfo.getResult().equalsIgnoreCase("8")) {
//						btnLogin.setEnabled(true);
//						txtLoginID.setEnabled(true);
//						txtPassword.setEnabled(true);
//						chkSavePassword.setEnabled(true);
//						MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_blocked_account);
//
//					} else if (loginInfo.getResult().equalsIgnoreCase("9")) {
//						btnLogin.setEnabled(true);
//						txtLoginID.setEnabled(true);
//						txtPassword.setEnabled(true);
//						chkSavePassword.setEnabled(true);
//						MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_wrong_username_password);
//						Utilities.writeLog(getActivityContext(), RealLuceAuctionApplication.getConfiguration("error_log_file"), "[LoginScreen] ID or Password is invalid");
//					} else {
//						btnLogin.setEnabled(true);
//						txtLoginID.setEnabled(true);
//						txtPassword.setEnabled(true);
//						chkSavePassword.setEnabled(true);
//						MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_wrong_username_password);
//						Utilities.writeLog(getActivityContext(), RealLuceAuctionApplication.getConfiguration("error_log_file"), "[LoginScreen] ID or Password is invalid");
//					}
//
//
//				}
//
//				@Override
//				public void onFailed(int errorCode) {
//					btnLogin.setEnabled(true);
//					txtLoginID.setEnabled(true);
//					txtPassword.setEnabled(true);
//					chkSavePassword.setEnabled(true);
//
//					switch (errorCode) {
//						case HTTPService.ERROR_NETWORK:
//							MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_xml_connect_gateway);
//							break;
//						case HTTPService.ERROR_XMLPARSER:
//							MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_xml_parser);
//							break;
//						case HTTPService.ERROR_RETURN_DATA_INVALID:
//							MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_return_data_invalid);
//							break;
//						case HTTPService.ERROR_SERVER_ERROR:
//							MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_server_error);
//							break;
//						default:
//							MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_unknow);
//							break;
//
//					}
//				}
//
//			});
//
//			objService.login(txtLoginID.getText().toString(), txtPassword.getText().toString(), tm.getDeviceId(), "0", RealLuceAuctionApplication.publicIPAddress,"","0",RealLuceAuctionApplication.countryCode);
//		}
		HTTPService objService = new HTTPService(new HTTPServiceCallback() {
			@Override
			public void onSucessfully(Object returnData) {
				//SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS , MODE_PRIVATE);
				//SharedPreferences.Editor editor = settings.edit();
				//editor.putString("workingGateway", RealLuceAuctionApplication.baseURL);
				//editor.commit();
				loginInfo = (LoginInfo) returnData;
				RealLuceAuctionApplication.memberName = loginInfo.getMemberName();
				RealLuceAuctionApplication.member_no = loginInfo.getMemberNo();
				if (loginInfo.getResult().equalsIgnoreCase("1") || loginInfo.getResult().equalsIgnoreCase("3")) {
					SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS, MODE_PRIVATE);
					SharedPreferences.Editor editor = settings.edit();
					String lang = loginInfo.getLanFlg().equals("1")?"en":"ja";
					setLocale(lang);
					if (lang.equals("ja")) {
						editor.putString(RealLuceAuctionApplication.SETTINGS_VIEW_LANGUAGE, "ja");
					} else {
						editor.putString(RealLuceAuctionApplication.SETTINGS_VIEW_LANGUAGE, "en");
					}
					editor.commit();
					// get system information from server
					HTTPService objService = new HTTPService(new HTTPServiceCallback() {
						@Override
						public void onSucessfully(Object returnData) {
							RealLuceAuctionApplication.systemInfo = (SystemInfo) returnData;


							if (RealLuceAuctionApplication.systemInfo.getStatus() != 0) {
								MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_server_error);
								btnLogin.setEnabled(true);
								txtLoginID.setEnabled(true);
								txtPassword.setEnabled(true);
								chkSavePassword.setEnabled(true);
							} else {
								if (RealLuceAuctionApplication.systemInfo.getAppVersion().length() > 0 && Integer.parseInt(RealLuceAuctionApplication.systemInfo.getAppVersion()) <= RealLuceAuctionApplication.getIntConfiguration("app_version")) {

									serviceCount = 0;
									loadAnnounce("1");



								} else {
									MessageBox.show(getActivityContext(), R.string.login_error_message_wrong_app_version_title, R.string.login_error_message_wrong_app_version);
									btnLogin.setEnabled(true);
									txtLoginID.setEnabled(true);
									txtPassword.setEnabled(true);
									chkSavePassword.setEnabled(true);
								}
							}
						}

						@Override
						public void onFailed(int errorCode) {

							btnLogin.setEnabled(true);
							txtLoginID.setEnabled(true);
							txtPassword.setEnabled(true);
							chkSavePassword.setEnabled(true);

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
								case HTTPService.ERROR_CANCEL:
									break;
								default:
									MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_unknow);
									break;

							}

							RealLuceAuctionApplication.systemInfo = null;
						}

					});

					objService.getSystemInfo();

				}
				/*
				else if (loginInfo.getResult().equalsIgnoreCase("2")){
					btnLogin.setEnabled(true);
					txtLoginID.setEnabled(true);
					txtPassword.setEnabled(true);
					chkSavePassword.setEnabled(true);
					MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_no_place);
				}
				*/
				else if (loginInfo.getResult().equalsIgnoreCase("8")) {
					btnLogin.setEnabled(true);
					txtLoginID.setEnabled(true);
					txtPassword.setEnabled(true);
					chkSavePassword.setEnabled(true);
					MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_blocked_account);

				} else if (loginInfo.getResult().equalsIgnoreCase("9")) {
					btnLogin.setEnabled(true);
					txtLoginID.setEnabled(true);
					txtPassword.setEnabled(true);
					chkSavePassword.setEnabled(true);
					MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_wrong_username_password);
					Utilities.writeLog(getActivityContext(), RealLuceAuctionApplication.getConfiguration("error_log_file"), "[LoginScreen] ID or Password is invalid");
				} else {
					btnLogin.setEnabled(true);
					txtLoginID.setEnabled(true);
					txtPassword.setEnabled(true);
					chkSavePassword.setEnabled(true);
					MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_wrong_username_password);
					Utilities.writeLog(getActivityContext(), RealLuceAuctionApplication.getConfiguration("error_log_file"), "[LoginScreen] ID or Password is invalid");
				}


			}

			@Override
			public void onFailed(int errorCode) {
				btnLogin.setEnabled(true);
				txtLoginID.setEnabled(true);
				txtPassword.setEnabled(true);
				chkSavePassword.setEnabled(true);

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

		objService.login(txtLoginID.getText().toString(), txtPassword.getText().toString(), "02-00-00-00-00-00", "0", RealLuceAuctionApplication.publicIPAddress,"","0",RealLuceAuctionApplication.countryCode);

	}

	private void registToken(){
		HTTPService objService = new HTTPService(null);
		objService.registerNotification(RealLuceAuctionApplication.loginID, RealLuceAuctionApplication.fireBaseToken);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode){
			case REQUEST_READ_PHONE_STATE:
				if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
					HTTPService objService = new HTTPService(new HTTPServiceCallback() {
						@Override
						public void onSucessfully(Object returnData) {
							//SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS , MODE_PRIVATE);
							//SharedPreferences.Editor editor = settings.edit();
							//editor.putString("workingGateway", RealLuceAuctionApplication.baseURL);
							//editor.commit();
							loginInfo = (LoginInfo) returnData;
							RealLuceAuctionApplication.memberName = loginInfo.getMemberName();
							if (loginInfo.getResult().equalsIgnoreCase("1") || loginInfo.getResult().equalsIgnoreCase("3")) {


								// get system information from server
								HTTPService objService = new HTTPService(new HTTPServiceCallback() {
									@Override
									public void onSucessfully(Object returnData) {
										RealLuceAuctionApplication.systemInfo = (SystemInfo) returnData;


										if (RealLuceAuctionApplication.systemInfo.getStatus() != 0) {
											MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_server_error);
											btnLogin.setEnabled(true);
											txtLoginID.setEnabled(true);
											txtPassword.setEnabled(true);
											chkSavePassword.setEnabled(true);
										} else {
											if (RealLuceAuctionApplication.systemInfo.getAppVersion().length() > 0 && Integer.parseInt(RealLuceAuctionApplication.systemInfo.getAppVersion()) <= RealLuceAuctionApplication.getIntConfiguration("app_version")) {

												serviceCount = 0;
												loadAnnounce("1");

											} else {
												MessageBox.show(getActivityContext(), R.string.login_error_message_wrong_app_version_title, R.string.login_error_message_wrong_app_version);
												btnLogin.setEnabled(true);
												txtLoginID.setEnabled(true);
												txtPassword.setEnabled(true);
												chkSavePassword.setEnabled(true);
											}
										}
									}

									@Override
									public void onFailed(int errorCode) {

										btnLogin.setEnabled(true);
										txtLoginID.setEnabled(true);
										txtPassword.setEnabled(true);
										chkSavePassword.setEnabled(true);

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
											case HTTPService.ERROR_CANCEL:
												break;
											default:
												MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_unknow);
												break;

										}

										RealLuceAuctionApplication.systemInfo = null;
									}

								});

								objService.getSystemInfo();

							}
				/*
				else if (loginInfo.getResult().equalsIgnoreCase("2")){
					btnLogin.setEnabled(true);
					txtLoginID.setEnabled(true);
					txtPassword.setEnabled(true);
					chkSavePassword.setEnabled(true);
					MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_no_place);
				}
				*/
							else if (loginInfo.getResult().equalsIgnoreCase("8")) {
								btnLogin.setEnabled(true);
								txtLoginID.setEnabled(true);
								txtPassword.setEnabled(true);
								chkSavePassword.setEnabled(true);
								MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_blocked_account);

							} else if (loginInfo.getResult().equalsIgnoreCase("9")) {
								btnLogin.setEnabled(true);
								txtLoginID.setEnabled(true);
								txtPassword.setEnabled(true);
								chkSavePassword.setEnabled(true);
								MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_wrong_username_password);
								Utilities.writeLog(getActivityContext(), RealLuceAuctionApplication.getConfiguration("error_log_file"), "[LoginScreen] ID or Password is invalid");
							} else {
								btnLogin.setEnabled(true);
								txtLoginID.setEnabled(true);
								txtPassword.setEnabled(true);
								chkSavePassword.setEnabled(true);
								MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_wrong_username_password);
								Utilities.writeLog(getActivityContext(), RealLuceAuctionApplication.getConfiguration("error_log_file"), "[LoginScreen] ID or Password is invalid");
							}


						}

						@Override
						public void onFailed(int errorCode) {
							btnLogin.setEnabled(true);
							txtLoginID.setEnabled(true);
							txtPassword.setEnabled(true);
							chkSavePassword.setEnabled(true);

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

					objService.login(txtLoginID.getText().toString(), txtPassword.getText().toString(), tm.getDeviceId(), "0", RealLuceAuctionApplication.publicIPAddress,"","0",
							RealLuceAuctionApplication.countryCode);
				}
				else{
					btnLogin.setEnabled(true);

				}
				break;
		}
		return;
	}

	/*
	 * Load member type
	 */
	private void loadMemberType(){
		
        //setIntSetting(LoginActivity.SETTINGS_SHOW_AGREEMENT,0);
        HTTPService objService = new HTTPService(new HTTPServiceCallback(){
			@Override
			public void onSucessfully(Object returnData) {
				LoginInfo obj= (LoginInfo) returnData;
				//loadPoster(obj.getMemberType());
				loadAnnounce(obj.getMemberType());
				
			}

			@Override
			public void onFailed(int errorCode) {
				if (serviceCount >= 0){
					serviceCount = -1000;
					switch(errorCode){
						case HTTPService.ERROR_NETWORK:
							MessageBox.show(getActivityContext(),R.string.error_title_webservice, R.string.error_message_xml_connect_gateway);
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
				
			}
        	
        });
        
        objService.getMemberType(txtLoginID.getText().toString());
	}
	
	
	/*
	 * Load places from server
	 */
	private void loadPlaces(){
		HTTPServiceCallback objServiceCallback = new HTTPServiceCallback(){

			@SuppressWarnings("unchecked")
			@Override
			public void onSucessfully(Object returnData) {
				
				RealLuceAuctionApplication.allPlaces = (ArrayList<PlaceInfo>) returnData;
				/*
				serviceCount++;
				if (serviceCount == 3){
					loginSuccessful();
				}
				*/
				//loadMemberType();
				loadAnnounce("0");
			}

			
			@Override
			public void onFailed(int errorCode) {
				if (serviceCount >= 0){
					serviceCount = -1000;
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
				
			}

        };
        
        
        HTTPService objService = new HTTPService(objServiceCallback);
        objService.getPlacesByLoginID(txtLoginID.getText().toString(),5);

	}
	
	
	/*
	 * Load Poster from server
	 */
	private void loadPoster(final String memberType){
		HTTPServiceCallback objServiceCallback = new HTTPServiceCallback(){

			@SuppressWarnings("unchecked")
			@Override
			public void onSucessfully(Object returnData) {
                RealLuceAuctionApplication.posterImage = ((PosterInfo) returnData).getImage();
                RealLuceAuctionApplication.posterURL = ((PosterInfo) returnData).getURL();
                /*
				serviceCount++;
				if (serviceCount == 3){
					loginSuccessful();
				}
				*/
                loadAnnounce(memberType);
			}

			
			@Override
			public void onFailed(int errorCode) {
				if (serviceCount >= 0){
					serviceCount = -1000;
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
			}

        };
        
        
        HTTPService objService = new HTTPService(objServiceCallback);
        objService.getPoster(memberType);
	
	}

	
	

	/*
	 * Load Poster from server
	 */
	private void loadAnnounce(String memberType){
		HTTPServiceCallback objServiceCallback = new HTTPServiceCallback(){

			@SuppressWarnings("unchecked")
			@Override
			public void onSucessfully(Object returnData) {
				
                RealLuceAuctionApplication.announceInfo = ((AnnounceInfo) returnData).getInfo();
				/*
				serviceCount++;
				if (serviceCount == 3){
					loginSuccessful();
				}
				*/
                loginSuccessful();
			}

			
			@Override
			public void onFailed(int errorCode) {
				if (serviceCount >= 0){
					serviceCount = -1000;
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
					Utilities.writeLog(getActivityContext(),RealLuceAuctionApplication.getConfiguration("error_log_file"),"[LoginScreen] Cannot connect to Server");
				}
			}

        };
        
        
        HTTPService objService = new HTTPService(objServiceCallback);
        objService.getAnnounce(memberType);

	}


	public void loginSuccessful(){
		
		
		// save login information
		SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS, MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		
		if (chkSavePassword.isChecked()){
			editor.putBoolean("save_password", true);
			editor.putString("password", txtPassword.getText().toString());
		}
		else{
			editor.remove("save_password");
			//editor.remove("username");
			editor.remove("password");
			
		}
		editor.putString("username", txtLoginID.getText().toString());
		editor.commit();
		
		RealLuceAuctionApplication.loginID = txtLoginID.getText().toString();
		RealLuceAuctionApplication.password = txtPassword.getText().toString();

		
		// register APN
//		GCMRegistrar.checkDevice(getActivityContext());
//		GCMRegistrar.checkManifest(getActivityContext());
		//final String regId = GCMRegistrar.getRegistrationId(getActivityContext());
		//if (regId.equals("")) {
//		GCMRegistrar.register(getActivityContext(), RealLuceAuctionApplication.getConfiguration("gcm_sender_id"));
		//}
		if(RealLuceAuctionApplication.fireBaseToken != null) registToken();
		//Redirect to next screen
		if (loginInfo.getDouiFlg().equalsIgnoreCase("1") ){
			setTransitAnimationType(TRANSIT_ANIMATION_NONE);
			setActivityParam("Mode", 5);
			directToActivity(TopSearchActivity.class);
		}
		else{
			setTransitAnimationType(TRANSIT_ANIMATION_NONE);
			setActivityParam("showAgreeButton", true);
			directToActivity(AgreementActivity.class);
		}
	}

	/*
	 * Get Banner from server
	 */
	private void getBanner() {
		HTTPServiceCallback objServiceCallback = new HTTPServiceCallback() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSucessfully(Object returnData) {

				bannerInfo = ((BannerInfo) returnData);

				// バナー１の処理
				if(bannerInfo.getBannerUrlDelFlag1().equals("0") && bannerInfo.getBannerImage1()!= null && bannerInfo.getBannerUrl1()!= null){

					bannerArea.setVisibility(View.VISIBLE);
					bannerImg.setImageBitmap(BitmapFactory.decodeByteArray(bannerInfo.getBannerImage1(),0,bannerInfo.getBannerImage1().length));
				}else{
					bannerArea.setVisibility(View.INVISIBLE);
				}

				// バナー２の処理
				if(bannerInfo.getBannerUrlTopDelFlg().equals("0")&& bannerInfo.getBannerUrlTop()!= null &&bannerInfo.getBannerUrlTopContent()!= null){
					toiawaseArea.setVisibility(View.VISIBLE);
					requestPasswordBtn.setText(bannerInfo.getBannerUrlTopContent());
				}else{
					toiawaseArea.setVisibility(View.INVISIBLE);
				}

			}
			@Override
			public void onFailed(int errorCode) {
			}

		};
		HTTPService objService = new HTTPService(objServiceCallback);
		objService.getBanner();
	}


	public void requestPassword(){
		String url = "";
		url = bannerInfo.getBannerUrlTop();
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(txtLoginID.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(txtPassword.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
	}

	//************************ LISTENERS ********************************

	private OnClickListener backgroundClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			if(getActivityContext().getCurrentFocus()!=null)
				im.hideSoftInputFromWindow(getActivityContext().getCurrentFocus().getWindowToken(),0);
		}
	};
	
	private OnClickListener btnLoginClick = new OnClickListener() {
	    	public void onClick(View v) {
	    		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(txtLoginID.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); 
	    		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(txtPassword.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				Utilities.writeLog(getActivityContext(),RealLuceAuctionApplication.getConfiguration("info_log_file"),"[LoginScreen] LoginButton Pressed");
	    		if(validateLogin() == true){
	    			if(isOnline()){
	    				login();
	    			}
	    			else{
	    				MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.error_message_internet_error);
	    				return;
	    			}
	    		}
	    		
	    	}
	};

	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

	    return cm.getActiveNetworkInfo() != null && 
	       cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}
	
	
	private OnClickListener btnRegisterClick = new OnClickListener() {
    	public void onClick(View v) {
    		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(txtLoginID.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); 
    		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(txtPassword.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    		

    		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(RealLuceAuctionApplication.getConfiguration("register_url")));
    		startActivity(browserIntent);
    	}
	};

	// バナータップ時の処理
	private OnClickListener btnBannerClick = new OnClickListener() {
		public void onClick(View v) {
			String url = "";
			url = bannerInfo.getBannerUrl1();
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(txtLoginID.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(txtPassword.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(browserIntent);
		}
	};
	
	private OnEditorActionListener txtPasswordEditorAction = new OnEditorActionListener(){

		@Override
		public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(txtLoginID.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); 
    		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(txtPassword.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    		
    		if(validateLogin() == true){
    			login();
    		}
			return true;
		}
		
	};


	private OnClickListener requestPasswordOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			requestPassword();
		}
	};


}