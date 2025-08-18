package tokiokaability.auction24.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.CountDownTimer;
import androidx.core.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tokiokaability.auction24.HTTPInfo.HTTPService;
import tokiokaability.auction24.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction24.R;
import tokiokaability.auction24.RealLuceAuctionApplication;
import tokiokaability.auction24.commons.MessageBox;
import tokiokaability.auction24.dialog.AuthSuccessDialog;

public class ChaneAuthenticalActivity extends BaseActivity{
    RelativeLayout background;
    TextView txtTitle,txtDes1,txtDes2;
    EditText txtCode1,txtCode2,txtCode3,txtCode4;
    Button btnResend;
    String newPhone = "";
    Dialog authSuccessDialog;
    String newCountryCode = "";
    String newCountryName = "";
    @Override
    protected void initialization() {
        setContentView(R.layout.activity_change_authentical);
        super.initialization();
        background = findViewById(R.id.background);
        txtTitle = findViewById(R.id.txtTitle);
        txtDes1 = findViewById(R.id.txtDescrip1);
        txtDes2 = findViewById(R.id.txtDescrip2);
        txtCode1 = findViewById(R.id.txtCode1);
        txtCode2 = findViewById(R.id.txtCode2);
        txtCode3 = findViewById(R.id.txtCode3);
        txtCode4 = findViewById(R.id.txtCode4);
        btnResend = findViewById(R.id.btnResend);
        background.setOnClickListener(backgroundClick);
        btnResend.setOnClickListener(btnResendClick);
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) setFont();
        if(txtCode1.requestFocus()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        txtCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput(txtCode1);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txtCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput(txtCode2);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput(txtCode3);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput(txtCode4);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newPhone = (String)getActivityParam("NewPhone");
        newCountryCode = (String)getActivityParam("NewCountryCode");
        newCountryName = (String)getActivityParam("NewCountryName");
    }
    private void setFont() {
        AssetManager am = getApplicationContext().getAssets();
        Typeface hiragino_w3 = Typeface.createFromAsset(am, "fonts/hiragino_kakugo_w3.otf");
        Typeface hiragino_w6 = Typeface.createFromAsset(am, "fonts/hiragino_kakugo_w6.otf");
        txtTitle.setTypeface(hiragino_w6);
        txtDes1.setTypeface(hiragino_w6);
        txtCode1.setTypeface(hiragino_w6);
        txtCode2.setTypeface(hiragino_w6);
        txtCode3.setTypeface(hiragino_w6);
        txtDes2.setTypeface(hiragino_w3);
        txtCode4.setTypeface(hiragino_w6);
        btnResend.setTypeface(hiragino_w6);
    }

    private View.OnClickListener backgroundClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(getActivityContext().getCurrentFocus().getWindowToken(),0);
        }
    };
    public void checkInput(EditText editText){
        int id = editText.getId();
        if (id == R.id.txtCode1) {
            if (txtCode1.getText().length() > 0) {
                setFocusForEditText();
            }
        } else if (id == R.id.txtCode2) {
            if (txtCode2.getText().length() > 0) {
                setFocusForEditText();
            }
        } else if (id == R.id.txtCode3) {
            if (txtCode3.getText().length() > 0) {
                setFocusForEditText();
            }
        } else if (id == R.id.txtCode4) {
            if (txtCode4.getText().length() > 0) {
                setFocusForEditText();
            }
        }
        if(txtCode1.getText().length()>0 && txtCode2.getText().length() > 0 && txtCode3.getText().length() > 0 && txtCode4.getText().length() > 0){
            String authCode = txtCode1.getText().toString()+txtCode2.getText().toString()+txtCode3.getText().toString()+txtCode4.getText().toString();
            if(authCode.equals(RealLuceAuctionApplication.password)){
                authSuccessDialog = new AuthSuccessDialog(getActivityContext());
                authSuccessDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                authSuccessDialog.show();
                new CountDownTimer(3000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        HTTPService objService = new HTTPService(new HTTPServiceCallback() {

                            @Override
                            public void onSucessfully(Object returnData) {
                                //showActivity(TopSearchActivity.class);
                                currentFunction = MENU_CURRENT_FUNCTION_SEARCH;
                                resetMainMenuButtons();
                                RealLuceAuctionApplication.loginID = newPhone;
                                RealLuceAuctionApplication.countryCode = newCountryCode;
                                RealLuceAuctionApplication.countryName = newCountryName;
                                setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
                                setActivityParam("Mode", 5);
                                showActivity(TopSearchActivity.class);

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
                        if (ActivityCompat.checkSelfPermission(getActivityContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                            objService.login(newPhone, "", tm.getDeviceId(), "0", RealLuceAuctionApplication.publicIPAddress,RealLuceAuctionApplication.allPlaces.get(0).getMemberNo(),"1",newCountryCode);
                        }
                        //showActivity(SettingsActivity.class);
                        //authSuccessDialog.dismiss();
                    }
                }.start();
            }
            else{
                MessageBox.show(getActivityContext(),R.string.verification_failed_title, R.string.verify_failed_content);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authSuccessDialog!=null && authSuccessDialog.isShowing())
            authSuccessDialog.dismiss();
    }

    public void setFocusForEditText(){
        if(txtCode1.getText().length() == 0)
            txtCode1.requestFocus();
        else if(txtCode2.getText().length() == 0)
            txtCode2.requestFocus();
        else if(txtCode3.getText().length() == 0)
            txtCode3.requestFocus();
        else if(txtCode4.getText().length() == 0)
            txtCode4.requestFocus();
    }

    private void resend(){
        showProgressDialog(R.string.please_waiting);
        HTTPService objService = new HTTPService(new HTTPServiceCallback() {

            @Override
            public void onSucessfully(Object returnData) {
                //showActivity(TopSearchActivity.class);
                //loadPlaces();
                hideProgressDialog();
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
        if (ActivityCompat.checkSelfPermission(getActivityContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            objService.login(newPhone, "", tm.getDeviceId(), "0", RealLuceAuctionApplication.publicIPAddress,RealLuceAuctionApplication.allPlaces.get(0).getMemberNo(),"0",RealLuceAuctionApplication.countryCode);
        }
    }

    View.OnClickListener btnResendClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resend();
        }
    };
}
