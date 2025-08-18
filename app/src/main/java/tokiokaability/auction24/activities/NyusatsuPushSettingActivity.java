package tokiokaability.auction24.activities;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tokiokaability.auction24.HTTPInfo.HTTPService;
import tokiokaability.auction24.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction24.HTTPInfo.PushFlgInfo;
import tokiokaability.auction24.R;
import tokiokaability.auction24.RealLuceAuctionApplication;
import tokiokaability.auction24.commons.MessageBox;

public class NyusatsuPushSettingActivity extends BaseActivity {
    TextView descripTxt,lblScreenTitle;
    RadioButton btnOK,btnDeny,btnOkiniri;
    CheckBox chkPush,chkMail;
    EditText edtMail;
    Button registBtn;
    String mail = null;
    RelativeLayout mainLayout;
    @Override
    protected void initialization() {
        setContentView(R.layout.activity_push_nyusatsu_setting);
        super.initialization();
        descripTxt = findViewById(R.id.descriptionTxt);
        mainLayout = findViewById(R.id.mainLayout);
        btnOK = findViewById(R.id.btnOK);
        btnOkiniri = findViewById(R.id.btnOkiniri);
        btnDeny = findViewById(R.id.btnDeny);
        chkPush = findViewById(R.id.chkPush);
        chkMail = findViewById(R.id.chkMail);
        edtMail = findViewById(R.id.txtMail);
        registBtn = findViewById(R.id.btnRegister);
        chkPush.setOnCheckedChangeListener(chkChangeListener);
        chkMail.setOnCheckedChangeListener(chkChangeListener);
        lblScreenTitle = findViewById(R.id.lblScreenTitle);
        btnBid.setVisibility(View.GONE);
        btnBid.setEnabled(true);
        btnBid2.setVisibility(View.GONE);
        btnBid2.setEnabled(true);
        if(RealLuceAuctionApplication.allPlaces.get(0).getRealFlag().equals("1")){
            btnBid.setVisibility(View.VISIBLE);
            btnBid2.setVisibility(View.VISIBLE);
        }
        else{
            btnBid.setVisibility(View.GONE);
            btnBid2.setVisibility(View.GONE);
        }
        addListener();
        getPushFlg();
        if(Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT)
            setFont();
    }

    void addListener(){
        btnOK.setOnClickListener(radioBtnClick);
        btnDeny.setOnClickListener(radioBtnClick);
        btnOkiniri.setOnClickListener(radioBtnClick);
        registBtn.setOnClickListener(btnRegistClick);
        mainLayout.setOnClickListener(bgrClick);
    }

    View.OnClickListener bgrClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if(getActivityContext().getCurrentFocus()!= null)
                im.hideSoftInputFromWindow(getActivityContext().getCurrentFocus().getWindowToken(),0);
        }
    };
    View.OnClickListener btnRegistClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mail.equals(edtMail.getText().toString()))return;
            String pushFlg = "";
            if(btnOK.isChecked()){
                if(chkPush.isChecked() && chkMail.isChecked())
                    pushFlg = "3";
                else if(!chkPush.isChecked() && chkMail.isChecked())
                    pushFlg = "2";
                else if(chkPush.isChecked() && !chkMail.isChecked())
                    pushFlg = "1";
                else
                    pushFlg = "0";
            }
            else if(btnOkiniri.isChecked()){
                if(chkPush.isChecked() && chkMail.isChecked())
                    pushFlg = "6";
                else if(!chkPush.isChecked() && chkMail.isChecked())
                    pushFlg = "5";
                else if(chkPush.isChecked() && !chkMail.isChecked())
                    pushFlg = "4";
                else
                    pushFlg = "7";
            }
            else if(btnDeny.isChecked()){
                if(chkPush.isChecked() && chkMail.isChecked())
                    pushFlg = "11";
                else if(!chkPush.isChecked() && chkMail.isChecked())
                    pushFlg = "9";
                else if(chkPush.isChecked() && !chkMail.isChecked())
                    pushFlg = "10";
                else
                    pushFlg = "8";
            }

            HTTPService srv = new HTTPService(new HTTPServiceCallback() {
                @Override
                public void onSucessfully(Object returnData) {
                    hideProgressDialog();
                    MessageBox.show(getActivityContext(),"","メールを更新しました。");
                }

                @Override
                public void onFailed(int errorCode) {
                    hideProgressDialog();
                }
            });
            showProgressDialog(R.string.please_waiting);
            srv.updatePushFlagWithMail(RealLuceAuctionApplication.loginID,pushFlg,"1",edtMail.getText().toString());
        }
    };

    CompoundButton.OnCheckedChangeListener chkChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            String pushFlg = "";
            if(btnOK.isChecked()){
                if(chkPush.isChecked() && chkMail.isChecked())
                    pushFlg = "3";
                else if(!chkPush.isChecked() && chkMail.isChecked())
                    pushFlg = "2";
                else if(chkPush.isChecked() && !chkMail.isChecked())
                    pushFlg = "1";
                else
                    pushFlg = "0";
            }
            else if(btnOkiniri.isChecked()){
                if(chkPush.isChecked() && chkMail.isChecked())
                    pushFlg = "6";
                else if(!chkPush.isChecked() && chkMail.isChecked())
                    pushFlg = "5";
                else if(chkPush.isChecked() && !chkMail.isChecked())
                    pushFlg = "4";
                else
                    pushFlg = "7";
            }
            else if(btnDeny.isChecked()){
                if(chkPush.isChecked() && chkMail.isChecked())
                    pushFlg = "11";
                else if(!chkPush.isChecked() && chkMail.isChecked())
                    pushFlg = "9";
                else if(chkPush.isChecked() && !chkMail.isChecked())
                    pushFlg = "10";
                else
                    pushFlg = "8";
            }

            HTTPService srv = new HTTPService(new HTTPServiceCallback() {
                @Override
                public void onSucessfully(Object returnData) {
                    hideProgressDialog();
                }

                @Override
                public void onFailed(int errorCode) {
                    hideProgressDialog();
                }
            });
            showProgressDialog(R.string.please_waiting);
            srv.updatePushFlag(RealLuceAuctionApplication.loginID,pushFlg,"1");
        }
    };

    private void setFont(){
        AssetManager am = getApplicationContext().getAssets();
        Typeface yGB = Typeface.createFromAsset(am,"fonts/YUGOTHB.TTC");
        Typeface yGR = Typeface.createFromAsset(am,"fonts/YUGOTHR.TTC");
        btnOK.setTypeface(yGR);
        btnDeny.setTypeface(yGR);
        btnOkiniri.setTypeface(yGR);
        lblScreenTitle.setTypeface(yGB);
        descripTxt.setTypeface(yGR);
        chkPush.setTypeface(yGR);
        chkMail.setTypeface(yGR);
    }

    void getPushFlg(){
        HTTPService srv = new HTTPService(new HTTPServiceCallback() {
            @Override
            public void onSucessfully(Object returnData) {
                hideProgressDialog();
                PushFlgInfo pInfo = (PushFlgInfo) returnData;
                edtMail.setText(pInfo.getMail());
                mail = pInfo.getMail();
                if(pInfo.getPushFlg().equals("2")){
                    btnOK.setChecked(true);
                    chkMail.setChecked(true);
                    chkPush.setChecked(false);
                }
                else if(pInfo.getPushFlg().equals("1")){
                    btnOK.setChecked(true);
                    chkMail.setChecked(false);
                    chkPush.setChecked(true);
                }
                else if(pInfo.getPushFlg().equals("3")){
                    btnOK.setChecked(true);
                    chkMail.setChecked(true);
                    chkPush.setChecked(true);
                }
                else if(pInfo.getPushFlg().equals("5")){
                    btnOkiniri.setChecked(true);
                    chkMail.setChecked(true);
                    chkPush.setChecked(false);
                }
                else if(pInfo.getPushFlg().equals("4")){
                    btnOkiniri.setChecked(true);
                    chkMail.setChecked(false);
                    chkPush.setChecked(true);
                }
                else if(pInfo.getPushFlg().equals("6")){
                    btnOkiniri.setChecked(true);
                    chkMail.setChecked(true);
                    chkPush.setChecked(true);
                }
                else if(pInfo.getPushFlg().equals("7")){
                    btnOkiniri.setChecked(true);
                    chkMail.setChecked(false);
                    chkPush.setChecked(false);
                }
                else if(pInfo.getPushFlg().equals("8")){
                    btnDeny.setChecked(true);
                    chkMail.setChecked(false);
                    chkPush.setChecked(false);
                }
                else if(pInfo.getPushFlg().equals("9")){
                    btnDeny.setChecked(true);
                    chkMail.setChecked(true);
                    chkPush.setChecked(false);
                }
                else if(pInfo.getPushFlg().equals("10")){
                    btnDeny.setChecked(true);
                    chkMail.setChecked(false);
                    chkPush.setChecked(true);
                }
                else if(pInfo.getPushFlg().equals("11")){
                    btnDeny.setChecked(true);
                    chkMail.setChecked(true);
                    chkPush.setChecked(true);
                }
                else{
                    btnOK.setChecked(true);
                    chkMail.setChecked(false);
                    chkPush.setChecked(false);
                }
            }

            @Override
            public void onFailed(int errorCode) {
                hideProgressDialog();
            }
        });
        showProgressDialog(R.string.please_waiting);
        srv.getPushFlag(RealLuceAuctionApplication.loginID,"1");
    }

    View.OnClickListener radioBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean checked = ((RadioButton)v).isChecked();
            String pushFlg = "";
            int id = v.getId();
            if (id == R.id.btnOK) {
                if (checked) {
                    if (chkPush.isChecked() && chkMail.isChecked())
                        pushFlg = "3";
                    else if (!chkPush.isChecked() && chkMail.isChecked())
                        pushFlg = "2";
                    else if (chkPush.isChecked() && !chkMail.isChecked())
                        pushFlg = "1";
                    else
                        pushFlg = "0";
                }
            } else if (id == R.id.btnOkiniri) {
                if (checked) {
                    if (chkPush.isChecked() && chkMail.isChecked())
                        pushFlg = "6";
                    else if (!chkPush.isChecked() && chkMail.isChecked())
                        pushFlg = "5";
                    else if (chkPush.isChecked() && !chkMail.isChecked())
                        pushFlg = "4";
                    else
                        pushFlg = "7";
                }
            } else if (id == R.id.btnDeny) {
                if (checked) {
                    if (chkPush.isChecked() && chkMail.isChecked())
                        pushFlg = "11";
                    else if (!chkPush.isChecked() && chkMail.isChecked())
                        pushFlg = "9";
                    else if (chkPush.isChecked() && !chkMail.isChecked())
                        pushFlg = "10";
                    else
                        pushFlg = "8";
                }
            }

            HTTPService srv = new HTTPService(new HTTPServiceCallback() {
                @Override
                public void onSucessfully(Object returnData) {
                    hideProgressDialog();
                }

                @Override
                public void onFailed(int errorCode) {
                    hideProgressDialog();
                }
            });
            showProgressDialog(R.string.please_waiting);
            srv.updatePushFlag(RealLuceAuctionApplication.loginID,pushFlg,"1");
        }
    };
}
