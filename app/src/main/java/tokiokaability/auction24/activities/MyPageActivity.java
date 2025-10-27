package tokiokaability.auction24.activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import tokiokaability.auction24.HTTPInfo.HTTPService;
import tokiokaability.auction24.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction24.HTTPInfo.ItemInfo;
import tokiokaability.auction24.R;
import tokiokaability.auction24.RealLuceAuctionApplication;

public class MyPageActivity extends BaseActivity {
    TextView descripTxt,lblScreenTitle;
    RadioButton btnOK,btnDeny;
    private TextView kaisaibiTxt;
    private ImageButton btnSelect;
    private TextView seisanLink;
    private ListView kaisaibiList;
    private String CurrentSessionNo;
    @Override
    protected void initialization() {
        setContentView(R.layout.activity_mypage);
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
        kaisaibiTxt = findViewById(R.id.kaisaibiSearchTxt);
        btnSelect = findViewById(R.id.selectBtn);
        seisanLink = findViewById(R.id.seisanLink);
        kaisaibiList = findViewById(R.id.kaisaibiList);
        if(RealLuceAuctionApplication.allPlaces.get(0).getRealFlag().equals("1")){
            btnBid.setVisibility(View.VISIBLE);
            btnBid2.setVisibility(View.VISIBLE);
        }
        else{
            btnBid.setVisibility(View.GONE);
            btnBid2.setVisibility(View.GONE);
        }
        kaisaibiTxt.setText(RealLuceAuctionApplication.systemInfo.getKaisaibiList().get(0));
        CurrentSessionNo = RealLuceAuctionApplication.systemInfo.getSessionList().get(0);
        if(Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT)
            setFont();
        final ArrayAdapter<String> kaisaibiAdapter = new ArrayAdapter<String>(this,R.layout.simple_spinner_row,RealLuceAuctionApplication.systemInfo.getKaisaibiList());
        kaisaibiList.setAdapter(kaisaibiAdapter);
        kaisaibiList.setOnItemClickListener(KaisaibiSelect);
        btnSelect.setOnClickListener(btnSelectClickListener);
        seisanLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = String.format(RealLuceAuctionApplication.getConfiguration("MYPAGE_URL"),CurrentSessionNo,
                        encode("TokiokaAbility",RealLuceAuctionApplication.loginID));
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
        getSeisansho();
    }

    public String encode(String key, String data) {
        String result = "";
        try{
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] finalByte = sha256_HMAC.doFinal(data.getBytes("UTF-8"));

            result = new String(Hex.encodeHex(finalByte));
        }
        catch (Exception ex){
            result = "";
        }
        return  result;
    }

    public AdapterView.OnItemClickListener KaisaibiSelect = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            kaisaibiList.setVisibility(View.GONE);
            btnSelect.setImageResource(R.drawable.spinner_arr_down);
            if(kaisaibiTxt.getText().equals(RealLuceAuctionApplication.systemInfo.getKaisaibiList().get(position))){
                return;
            }
            else{
                kaisaibiTxt.setText(RealLuceAuctionApplication.systemInfo.getKaisaibiList().get(position));
                CurrentSessionNo = RealLuceAuctionApplication.systemInfo.getSessionList().get(position);
                getSeisansho();
            }
        }
    };
    public View.OnClickListener btnSelectClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(kaisaibiList.getVisibility() == View.VISIBLE){
                kaisaibiList.setVisibility(View.GONE);
                btnSelect.setImageResource(R.drawable.spinner_arr_down);
            }
            else{
                kaisaibiList.setVisibility(View.VISIBLE);
                btnSelect.setImageResource(R.drawable.spinner_arr_up);
            }
        }
    };

    private void getSeisansho(){
        HTTPService httpObj = new HTTPService(new HTTPServiceCallback() {
            @Override
            public void onSucessfully(Object returnData) {
                hideProgressDialog();
                if(ItemInfo.RecordCount > 0){
                    seisanLink.setVisibility(View.VISIBLE);
                }
                else{
                    seisanLink.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailed(int errorCode) {
                hideProgressDialog();
                seisanLink.setVisibility(View.INVISIBLE);
            }
        });
        showProgressDialog(R.string.please_waiting);
        httpObj.getSeisansho(RealLuceAuctionApplication.allPlaces.get(0).getIdNo(),CurrentSessionNo,encode("TokiokaAbility",RealLuceAuctionApplication.loginID));
    }

    private void setFont(){
        AssetManager am = getApplicationContext().getAssets();
        Typeface yGB = Typeface.createFromAsset(am,"fonts/YUGOTHB.TTC");
        Typeface yGR = Typeface.createFromAsset(am,"fonts/YUGOTHR.TTC");
        lblScreenTitle.setTypeface(yGB);
        descripTxt.setTypeface(yGR);
        seisanLink.setTypeface(yGB);
    }




}
