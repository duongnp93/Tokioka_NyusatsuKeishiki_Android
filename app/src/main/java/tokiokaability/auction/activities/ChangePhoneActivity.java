package tokiokaability.auction.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
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

import tokiokaability.auction.CountryListAdapter;
import tokiokaability.auction.HTTPInfo.CountryInfo;
import tokiokaability.auction.HTTPInfo.HTTPService;
import tokiokaability.auction.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction.HTTPInfo.LoginInfo;
import tokiokaability.auction.R;
import tokiokaability.auction.RealLuceAuctionApplication;
import tokiokaability.auction.commons.MessageBox;

public class ChangePhoneActivity extends  BaseActivity {
    RelativeLayout background = null;
    TextView txtTitle = null;
    TextView txtDes1,txtDes2,txtDes3,txtOldPhone;
    EditText edtNewPhone;
    Button changeBtn;
    EditText txtCountry = null;
    public ExpandableListView lstCountry;
    ExpandableListAdapter CountryAdapter;
    List<String> lstHeader;
    LinkedHashMap<String,List<String>> lstCountryDetail;
    int lastGroupExpandedPostion = -1;
    ImageButton btnSelect;
    List<CountryInfo> countries;
    String newCountryCode = null;
    String newCountryName = null;
    @Override
    protected void initialization() {
        setContentView(R.layout.activity_change_phone);
        super.initialization();
        background = findViewById(R.id.background);
        txtTitle = findViewById(R.id.txtTitle);
        txtDes1 = findViewById(R.id.txtDescrip1);
        txtDes2 = findViewById(R.id.txtDescrip2);
        txtDes3 = findViewById(R.id.txtDescrip3);
        edtNewPhone = findViewById(R.id.txtNewPhone);
        changeBtn = findViewById(R.id.btnChange);
        txtOldPhone = findViewById(R.id.txtOldPhone);
        background.setOnClickListener(backgroundClick);
        changeBtn.setOnClickListener(changeBtnClick);
        txtCountry = findViewById(R.id.txtCountry);
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) setFont();
        changeBtn.setEnabled(false);
        edtNewPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkInput();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txtOldPhone.setText(RealLuceAuctionApplication.loginID);
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
                if(lastGroupExpandedPostion != -1 && lastGroupExpandedPostion != groupPosition)
                    lstCountry.collapseGroup(lastGroupExpandedPostion);
                lastGroupExpandedPostion = groupPosition;
            }
        });
        lstCountry.setOnChildClickListener(countryClick);
        getCountry();
        checkInput();
        newCountryCode = RealLuceAuctionApplication.countryCode;
        newCountryName = RealLuceAuctionApplication.countryName;
    }

    public int convertDpToPixel(float dp){
        DisplayMetrics metrics = getResources().getDisplayMetrics() ;
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }



    private void checkInput(){
        if(edtNewPhone.getText().length() >= 1 && txtCountry.getText().length()>0){
            changeBtn.setEnabled(true);
        }
        else{
            changeBtn.setEnabled(false);
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
            newCountryName = (String) CountryAdapter.getChild(groupPosition, childPosition);
            for (int i = 0; i < countries.size(); i++) {
                if (countries.get(i).getCountryName().equals((String) CountryAdapter.getChild(groupPosition, childPosition))) {
                    newCountryCode = countries.get(i).getCountryCode();
                    break;
                }

            }
            checkInput();
            if (edtNewPhone.getText().length() == 0)
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
        txtTitle.setTypeface(hiragino_w6);
        txtDes1.setTypeface(hiragino_w6);
        changeBtn.setTypeface(hiragino_w6);
        edtNewPhone.setTypeface(hiragino_w6);
        txtOldPhone.setTypeface(hiragino_w6);
        txtDes2.setTypeface(hiragino_w3);
        txtDes3.setTypeface(hiragino_w3);
        txtCountry.setTypeface(hiragino_w6);
    }

    private View.OnClickListener backgroundClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(getActivityContext().getCurrentFocus().getWindowToken(),0);
        }
    };

    private View.OnClickListener changeBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(getActivityContext().getCurrentFocus().getWindowToken(),0);
            //showActivity(ChaneAuthenticalActivity.class);
            showProgressDialog(R.string.please_waiting);
            changePhone();
        }
    };

    private void changePhone(){
        HTTPService objService = new HTTPService(new HTTPServiceCallback() {

            @Override
            public void onSucessfully(Object returnData) {
                //showActivity(TopSearchActivity.class);
                LoginInfo loginInfo = (LoginInfo) returnData;
                if (loginInfo.getResult().equalsIgnoreCase("1") || loginInfo.getResult().equalsIgnoreCase("3")) {
                    if(loginInfo.getNewbie().equals("1")){
                        RealLuceAuctionApplication.password = loginInfo.getCode();
                        setActivityParam("NewPhone",edtNewPhone.getText().toString());
                        setActivityParam("NewCountryCode",newCountryCode);
                        setActivityParam("NewCountryName",newCountryName);
                        showActivity(ChaneAuthenticalActivity.class);
                    }
                    else {
                        MessageBox.show(getActivityContext(),"","This phone number is used");
                    }
                    hideProgressDialog();
                }
                else{
                    MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.connection_confirm_reconnect);
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
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(SplashActivity.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivityContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            objService.login(edtNewPhone.getText().toString(), "", tm.getDeviceId(), "0", RealLuceAuctionApplication.publicIPAddress,RealLuceAuctionApplication.allPlaces.get(0).getMemberNo(),"0",
                    newCountryCode);
        }
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
