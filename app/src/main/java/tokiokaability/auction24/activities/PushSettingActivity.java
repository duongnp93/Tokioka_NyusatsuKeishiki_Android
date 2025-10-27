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

public class PushSettingActivity extends BaseActivity {
    TextView descripTxt,lblScreenTitle;
    RadioButton btnOK,btnDeny;


    @Override
    protected void initialization() {
        setContentView(R.layout.activity_push_setting);
        super.initialization();
        setStatusBar();
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
                if(pInfo.getPushFlg().equals("1")){
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
        srv.getPushFlag(RealLuceAuctionApplication.loginID,"0");
    }

    View.OnClickListener radioBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean checked = ((RadioButton)v).isChecked();
            String pushFlg = "";
            int id = v.getId();
            if (id == R.id.btnOK) {
                if (checked) {
                    pushFlg = "1";
                }
            } else if (id == R.id.btnDeny) {
                if (checked) {
                    pushFlg = "0";
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
            srv.updatePushFlag(RealLuceAuctionApplication.loginID,pushFlg,"0");
        }
    };
}
