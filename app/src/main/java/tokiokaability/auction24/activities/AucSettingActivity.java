package tokiokaability.auction24.activities;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import tokiokaability.auction24.HTTPInfo.HTTPService;
import tokiokaability.auction24.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction24.HTTPInfo.PushFlgInfo;
import tokiokaability.auction24.R;
import tokiokaability.auction24.RealLuceAuctionApplication;

public class AucSettingActivity extends BaseActivity {
    TextView descripTxt,lblScreenTitle;
    RadioButton btnOK,btnDeny;


    @Override
    protected void initialization() {
        setContentView(R.layout.activity_auc_setting);
        super.initialization();
        descripTxt = findViewById(R.id.descriptionTxt);
        btnOK = findViewById(R.id.btnOK);
        btnDeny = findViewById(R.id.btnDeny);
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
    }

    void reloadText(){
        lblScreenTitle.setText(R.string.language_setting_title);
        descripTxt.setText(R.string.language_setting_Description);
        btnOK.setText(R.string.language_setting_ok);
        btnDeny.setText(R.string.language_setting_deny);
    }

    private void setFont(){
        AssetManager am = getApplicationContext().getAssets();
        Typeface yGB = Typeface.createFromAsset(am,"fonts/YUGOTHB.TTC");
        Typeface yGR = Typeface.createFromAsset(am,"fonts/YUGOTHR.TTC");
        btnOK.setTypeface(yGR);
        btnDeny.setTypeface(yGR);
        lblScreenTitle.setTypeface(yGB);
        descripTxt.setTypeface(yGR);
    }

    void getPushFlg(){
        HTTPService srv = new HTTPService(new HTTPServiceCallback() {
            @Override
            public void onSucessfully(Object returnData) {
                hideProgressDialog();
                PushFlgInfo pInfo = (PushFlgInfo) returnData;
                if(pInfo.getLanFlg().equals("0")){
                    btnOK.setChecked(true);
                }
                else{
                    btnDeny.setChecked(true);
                }
            }

            @Override
            public void onFailed(int errorCode) {
                hideProgressDialog();
            }
        });
        showProgressDialog(R.string.please_waiting);
        srv.getMemoFlag(RealLuceAuctionApplication.loginID);
    }

    View.OnClickListener radioBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean checked = ((RadioButton)v).isChecked();
            String lanFlg = "";
//            SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS, MODE_PRIVATE);
//            SharedPreferences.Editor editor = settings.edit();
            int id = v.getId();
            if (id == R.id.btnOK) {
                if (checked) {
                    lanFlg = "0";
//                        editor.putString(RealLuceAuctionApplication.SETTINGS_VIEW_LANGUAGE, "ja");
//                        setLocale("ja");
                }
            } else if (id == R.id.btnDeny) {
                if (checked) {
                    lanFlg = "1";
//                        editor.putString(RealLuceAuctionApplication.SETTINGS_VIEW_LANGUAGE, "en");
//                        setLocale("en");
                }
            }
//            editor.commit();
//            reloadText();
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
            srv.updateAucFlag(RealLuceAuctionApplication.loginID,lanFlg);
        }
    };
}
