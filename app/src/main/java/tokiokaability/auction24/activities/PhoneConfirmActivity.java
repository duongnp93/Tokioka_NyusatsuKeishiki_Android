package tokiokaability.auction24.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import tokiokaability.auction24.CountryListAdapter;
import tokiokaability.auction24.HTTPInfo.AnnounceInfo;
import tokiokaability.auction24.HTTPInfo.CountryInfo;
import tokiokaability.auction24.HTTPInfo.HTTPService;
import tokiokaability.auction24.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction24.HTTPInfo.LoginInfo;
import tokiokaability.auction24.HTTPInfo.PlaceInfo;
import tokiokaability.auction24.HTTPInfo.SystemInfo;
import tokiokaability.auction24.R;
import tokiokaability.auction24.RealLuceAuctionApplication;
import tokiokaability.auction24.commons.MessageBox;
import tokiokaability.auction24.commons.Utilities;

public class PhoneConfirmActivity extends BaseActivity {
    ImageButton btnSelect;
    Button btnConfirm = null;
    Button btnAgree = null;
    RelativeLayout bgrLayout = null;
    EditText txtPhone = null;
    TextView txtDescrip = null;
    TextView txtTopTile = null;
    TextView txtVersion = null;
    private LoginInfo loginInfo = null;
    private int serviceCount = 0;
    public static final int REQUEST_READ_PHONE_STATE = 1;
    EditText txtCountry = null;
    public ExpandableListView lstCountry;
    ExpandableListAdapter CountryAdapter;
    List<String> lstHeader;
    LinkedHashMap<String,List<String>> lstCountryDetail;
    int lastGroupExpandedPostion = -1;
    List<CountryInfo> countries;
    TelephonyManager tm;


    @Override
    protected void initialization() {
        setContentView(R.layout.activity_phone_confirm);
        super.initialization();
        btnConfirm = findViewById(R.id.btnConfirm);
        btnAgree = findViewById(R.id.btnAgree);
        bgrLayout = findViewById(R.id.background);
        txtPhone = findViewById(R.id.txtPhone);
        txtDescrip = findViewById(R.id.txtDescrip);
        txtTopTile = findViewById(R.id.top_title);
        bgrLayout.setOnClickListener(backgroundClick);
        btnConfirm.setOnClickListener(btnConfirmClick);
        btnConfirm.setEnabled(false);
        txtCountry = findViewById(R.id.txtCountry);

//        if(txtPhone.requestFocus()){
//            if(txtPhone.getText().length() == 0)
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        }
        txtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txtPhone.setText(RealLuceAuctionApplication.loginID);
        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
                setActivityParam("showAgreeButton", false);
                RealLuceAuctionApplication.isTopAgree = true;
                directToActivity(AgreementActivity.class);
            }
        });
        //txtCountry = findViewById(R.id.txtCountry);
        txtVersion = (TextView)findViewById(R.id.versionTxt);
        String version = RealLuceAuctionApplication.getConfiguration("app_version");
        if(version.length() >= 3)
            txtVersion.setText("ver "+version.charAt(0)+"."+version.charAt(1)+"."+version.charAt(2));
        else txtVersion.setText("ver 1.0.0");
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT)  setFont();
        txtCountry.setText(RealLuceAuctionApplication.countryName);
        txtCountry.setOnClickListener(btnSelectClick);
        lstCountry = findViewById(R.id.lstCountry);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)

            lstCountry.setIndicatorBoundsRelative(convertDpToPixel(253),convertDpToPixel(293));
        else
            lstCountry.setIndicatorBounds(convertDpToPixel(253),convertDpToPixel(293));
        btnSelect = findViewById(R.id.btnSelect);
        btnSelect.setOnClickListener(btnSelectClick);
        lstCountry.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //lstCountry.setSelectedGroup(0);
                if(lastGroupExpandedPostion != -1 && lastGroupExpandedPostion != groupPosition)
                    lstCountry.collapseGroup(lastGroupExpandedPostion);
                lastGroupExpandedPostion = groupPosition;

            }
        });


        lstCountry.setOnChildClickListener(countryClick);
        getCountry();
        checkInput();
    }

    public int convertDpToPixel(float dp){
        DisplayMetrics metrics = getResources().getDisplayMetrics() ;
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }



    private void checkInput(){
        if(txtPhone.getText().length() >= 1 && txtCountry.getText().length()>0){
            btnConfirm.setEnabled(true);
        }
        else{
            btnConfirm.setEnabled(false);
        }
    }

    private View.OnClickListener btnSelectClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(getActivityContext().getCurrentFocus().getWindowToken(),0);
            if(lstCountry.getVisibility() == View.VISIBLE){
                lstCountry.setVisibility(View.GONE);
                btnSelect.setImageResource(R.drawable.spinner_arr_down);
            }
            else{
                lstCountry.setVisibility(View.VISIBLE);
                btnSelect.setImageResource(R.drawable.spinner_arr_up);
            }
        }
    };

    void getCountry(){
        showProgressDialog(R.string.please_waiting);
        HTTPService objService = new HTTPService(new HTTPServiceCallback() {
            @Override
            public void onSucessfully(Object returnData) {
                hideProgressDialog();
                countries = (List<CountryInfo>)returnData;
                lstCountryDetail = getCountries();
                lstHeader = new ArrayList<String>(lstCountryDetail.keySet());
                CountryAdapter = new CountryListAdapter(getActivityContext(),lstHeader,lstCountryDetail);
                lstCountry.setAdapter(CountryAdapter);
            }

            @Override
            public void onFailed(int errorCode) {
                MessageBox.show(getActivityContext(),R.string.get_ctr_failed_title,R.string.get_ctr_failed_content);
            }
        });
        objService.getCountry();
    }

    ExpandableListView.OnChildClickListener countryClick = new ExpandableListView.OnChildClickListener()
    {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            lstCountry.setVisibility(View.GONE);
            btnSelect.setImageResource(R.drawable.spinner_arr_down);
            txtCountry.setText((String) CountryAdapter.getChild(groupPosition, childPosition));
            RealLuceAuctionApplication.countryName = (String) CountryAdapter.getChild(groupPosition, childPosition);
            for (int i = 0; i < countries.size(); i++) {
                if (countries.get(i).getCountryName().equals((String) CountryAdapter.getChild(groupPosition, childPosition))) {
                    RealLuceAuctionApplication.countryCode = countries.get(i).getCountryCode();
                    break;
                }

            }
            checkInput();
            if (txtPhone.getText().length() == 0)
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
            return true;
        }
    };

    private void setFont() {
        AssetManager am = getApplicationContext().getAssets();
        Typeface hiragino_w3 = Typeface.createFromAsset(am, "fonts/hiragino_kakugo_w3.otf");
        Typeface hiragino_w6 = Typeface.createFromAsset(am, "fonts/hiragino_kakugo_w6.otf");
        txtPhone.setTypeface(hiragino_w6);
        txtDescrip.setTypeface(hiragino_w6);
        btnConfirm.setTypeface(hiragino_w6);
        btnAgree.setTypeface(hiragino_w6);
        txtTopTile.setTypeface(hiragino_w6);
        txtCountry.setTypeface(hiragino_w6);
        txtVersion.setTypeface(hiragino_w3);
    }

    private View.OnClickListener backgroundClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(getActivityContext().getCurrentFocus().getWindowToken(),0);
        }
    };
    private View.OnClickListener btnConfirmClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(getActivityContext().getCurrentFocus().getWindowToken(),0);
            Utilities.writeLog(getActivityContext(),RealLuceAuctionApplication.getConfiguration("info_log_file"),"[LoginScreen] LoginButton Pressed +"+RealLuceAuctionApplication.countryCode+" "+txtPhone.getText());
            if(isOnline()){
                showProgressDialog(R.string.please_waiting);
                login();
            }
            else{
                MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.error_message_internet_error);
                return;
            }
//            Dialog authSuccessDialog = new AuthSuccessDialog(getActivityContext());
//            authSuccessDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//            authSuccessDialog.show();
        }
    };

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void login() {
        tm = (TelephonyManager) getBaseContext().getSystemService(SplashActivity.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivityContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},REQUEST_READ_PHONE_STATE);
        }
        else{
            HTTPService objService = new HTTPService(new HTTPServiceCallback() {
                @Override
                public void onSucessfully(Object returnData) {
                    //SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS , MODE_PRIVATE);
                    //SharedPreferences.Editor editor = settings.edit();
                    //editor.putString("workingGateway", RealLuceAuctionApplication.baseURL);
                    //editor.commit();
                    loginInfo = (LoginInfo) returnData;

                    if (loginInfo.getResult().equalsIgnoreCase("1") || loginInfo.getResult().equalsIgnoreCase("3")) {


                        // get system information from server
                        HTTPService objService = new HTTPService(new HTTPServiceCallback() {
                            @Override
                            public void onSucessfully(Object returnData) {
                                RealLuceAuctionApplication.systemInfo = (SystemInfo) returnData;


                                if (RealLuceAuctionApplication.systemInfo.getStatus() != 0) {
                                    MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_server_error);

                                } else {
                                    if (RealLuceAuctionApplication.systemInfo.getAppVersion().length() > 0 && Integer.parseInt(RealLuceAuctionApplication.systemInfo.getAppVersion()) <= RealLuceAuctionApplication.getIntConfiguration("app_version")) {

                                        serviceCount = 0;
                                        loadPlaces();
                                        //loadMemberType();


                                    } else {
                                        MessageBox.show(getActivityContext(), R.string.login_error_message_wrong_app_version_title, R.string.login_error_message_wrong_app_version, new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                                try {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                } catch (android.content.ActivityNotFoundException anfe) {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                }
                                            }
                                        },R.string.go_to_store);

                                    }
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
                                    case HTTPService.ERROR_CANCEL:
                                        break;
                                    default:
                                        MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_unknow);
                                        break;

                                }

                                RealLuceAuctionApplication.systemInfo = null;
                                hideProgressDialog();
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
                        MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_blocked_account);

                    } else if (loginInfo.getResult().equalsIgnoreCase("9")) {

                        MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_wrong_username_password);
                        Utilities.writeLog(getActivityContext(), RealLuceAuctionApplication.getConfiguration("error_log_file"), "[LoginScreen] ID or Password is invalid");
                    } else {

                        MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_wrong_username_password);
                        Utilities.writeLog(getActivityContext(), RealLuceAuctionApplication.getConfiguration("error_log_file"), "[LoginScreen] ID or Password is invalid");
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
                    hideProgressDialog();
                }

            });

            objService.login(txtPhone.getText().toString(), "", tm.getDeviceId(), "0", RealLuceAuctionApplication.publicIPAddress,"","0",RealLuceAuctionApplication.countryCode);
        }


    }

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
                loadMemberType();

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
                hideProgressDialog();
            }

        };


        HTTPService objService = new HTTPService(objServiceCallback);
        objService.getPlacesByLoginID(txtPhone.getText().toString(),5);

    }

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
                    hideProgressDialog();
                    Utilities.writeLog(getActivityContext(),RealLuceAuctionApplication.getConfiguration("error_log_file"),"[LoginScreen] Cannot connect to Server");
                }
            }

        };


        HTTPService objService = new HTTPService(objServiceCallback);
        objService.getAnnounce(memberType);

    }

    public void loginSuccessful(){

        RealLuceAuctionApplication.loginID = txtPhone.getText().toString();
        RealLuceAuctionApplication.password = loginInfo.getCode();

        // register APN
//		GCMRegistrar.checkDevice(getActivityContext());
//		GCMRegistrar.checkManifest(getActivityContext());
        //final String regId = GCMRegistrar.getRegistrationId(getActivityContext());
        //if (regId.equals("")) {
//		GCMRegistrar.register(getActivityContext(), RealLuceAuctionApplication.getConfiguration("gcm_sender_id"));
        //}
        if(RealLuceAuctionApplication.fireBaseToken != null) registToken();
        //Redirect to next screen

        if (loginInfo.getResult().equalsIgnoreCase("1") ){
            RealLuceAuctionApplication.loginID = txtPhone.getText().toString();
            if(loginInfo.getNewbie().equals("1")){
                setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
                directToActivity(AuthenticalActivity.class);
            }
            else{
                setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
                setActivityParam("Mode", 5);
                directToActivity(TopSearchActivity.class);
            }
        }
        else{
            setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
            setActivityParam("showAgreeButton", true);
            directToActivity(AgreementActivity.class);
        }
        hideProgressDialog();
    }

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
                hideProgressDialog();
            }

        });

        objService.getMemberType(txtPhone.getText().toString());
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

                            if (loginInfo.getResult().equalsIgnoreCase("1") || loginInfo.getResult().equalsIgnoreCase("3")) {


                                // get system information from server
                                HTTPService objService = new HTTPService(new HTTPServiceCallback() {
                                    @Override
                                    public void onSucessfully(Object returnData) {
                                        RealLuceAuctionApplication.systemInfo = (SystemInfo) returnData;


                                        if (RealLuceAuctionApplication.systemInfo.getStatus() != 0) {
                                            MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_server_error);

                                        } else {
                                            if (RealLuceAuctionApplication.systemInfo.getAppVersion().length() > 0 && Integer.parseInt(RealLuceAuctionApplication.systemInfo.getAppVersion()) <= RealLuceAuctionApplication.getIntConfiguration("app_version")) {

                                                serviceCount = 0;
                                                loadPlaces();
                                                //loadMemberType();


                                            } else {
                                                MessageBox.show(getActivityContext(), R.string.login_error_message_wrong_app_version_title, R.string.login_error_message_wrong_app_version);

                                            }
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
                                            case HTTPService.ERROR_CANCEL:
                                                break;
                                            default:
                                                MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_unknow);
                                                break;

                                        }

                                        RealLuceAuctionApplication.systemInfo = null;
                                        hideProgressDialog();
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
                                MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_blocked_account);

                            } else if (loginInfo.getResult().equalsIgnoreCase("9")) {
                                MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_wrong_username_password);
                                Utilities.writeLog(getActivityContext(), RealLuceAuctionApplication.getConfiguration("error_log_file"), "[LoginScreen] ID or Password is invalid");
                            } else {
                                MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_wrong_username_password);
                                Utilities.writeLog(getActivityContext(), RealLuceAuctionApplication.getConfiguration("error_log_file"), "[LoginScreen] ID or Password is invalid");
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
                            hideProgressDialog();
                        }

                    });

                    objService.login(txtPhone.getText().toString(), "", tm.getDeviceId(), "0", RealLuceAuctionApplication.publicIPAddress,"","0",RealLuceAuctionApplication.countryCode);
                }
                break;
        }
        return;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public LinkedHashMap<String, List<String>> getCountries(){
        LinkedHashMap<String,List<String>> countriesListDetail = new LinkedHashMap<>();
        List<String> title = new ArrayList<String>();
        List<String> ctrByGroup = new ArrayList<String>();
        List<List<String>> allCountry = new ArrayList<List<String>>();
        for(int i = 0;i< countries.size();i++){
            String groupName = countries.get(i).getGroupName();
            String countryName = countries.get(i).getCountryName();
            if(!title.contains(groupName)){
                title.add(groupName);
                if(ctrByGroup.size() > 0){
                    allCountry.add(ctrByGroup);
                    ctrByGroup = new ArrayList<String>();
                }
            }
            ctrByGroup.add(countryName);
        }
        if(ctrByGroup.size() > 0){
            allCountry.add(ctrByGroup);
        }
        for(int i = 0;i< title.size();i++){
            countriesListDetail.put(title.get(i),allCountry.get(i));
        }
        return countriesListDetail;
    }


}
