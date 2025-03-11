package tokiokaability.auction.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Calendar;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import tokiokaability.auction.HTTPInfo.PlaceInfo;
import tokiokaability.auction.R;
import tokiokaability.auction.RealLuceAuctionApplication;

public class WebBidActivity  extends BaseActivity {
    WebView webView;
    String nyusatsu_url = "";
    TextView lblScreenTitle;
    int searchMode;
    String boxNo,boxNoTo,freeWord,brandList,subCatList,rankList;
    private ImageButton btnBack;
    public AssetManager am ;
    public Typeface yGR,yGB;
    ImageButton btnBid,btnBid2;

    @Override
    protected void initialization() {
        super.initialization();
        setContentView(R.layout.web_auction);
        webView = findViewById(R.id.webViewArea);
        this.btnBack = findViewById(R.id.btnBack);
        lblScreenTitle = findViewById(R.id.lblScreenTitle);
        nyusatsu_url = RealLuceAuctionApplication.getConfiguration("NYUSATSU_URL");
        searchMode = (Integer) getActivityParam("SearchMode", 1);
        boxNo = (String) getActivityParam("BoxNo", "");
        boxNoTo = (String) getActivityParam("BoxNoTo", "");
        freeWord = (String) getActivityParam("Freeword", "");
        subCatList = (String) getActivityParam("SubCatList","");
        brandList = (String) getActivityParam("BrandList","");
        rankList = (String) getActivityParam("RankList","");
        setScreenTitle();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(generateUrl());
        btnBack.setOnClickListener(btnBackClick);
        btnBid = findViewById(R.id.btnBit);
        btnBid2 = findViewById(R.id.btnBit2);
        btnBid.setTag("1");
        btnBid2.setTag("2");
    }

    @Override
    protected void onResume() {
        super.onResume();
        PlaceInfo p = RealLuceAuctionApplication.allPlaces.get(0);
        if(p.getRealFlag().equals("1")){
            btnBid.setVisibility(View.VISIBLE);
            btnBid2.setVisibility(View.VISIBLE);
        }
        else{
            btnBid.setVisibility(View.GONE);
            btnBid2.setVisibility(View.GONE);
        }
    }

    private void setScreenTitle() {
        am = getApplicationContext().getAssets();
        yGB = Typeface.createFromAsset(am,"fonts/YUGOTHB.TTC");
        yGR = Typeface.createFromAsset(am,"fonts/YUGOTHR.TTC");
        String title = null;
        if (searchMode == 1) {
            title = this.getString(R.string.items_info_result_list_title);
        } else if (searchMode == 2) {
            title = this.getString(R.string.items_info_product_list_title);
        } else if (searchMode == 3) {

            title = this.getString(R.string.items_info_successfully_bit_title);
        } else if (searchMode == 4) {
            title = this.getString(R.string.items_info_bit_list_title);
        } else if (searchMode == 5) {
            title = this.getString(R.string.items_info_mylist_list_title);
        } else if (searchMode == 6) {
            title = this.getString(R.string.items_info_mylist_notification_title);
        } else if (searchMode == 7) {
            title = this.getString(R.string.items_info_mylist_rate_free_word_title);
            lblScreenTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        }

        lblScreenTitle.setText(title);
        if(Build.VERSION.SDK_INT!= Build.VERSION_CODES.KITKAT)
            lblScreenTitle.setTypeface(yGB);
    }
    String generateUrl(){
        String result = "";
        String hmacData = String.format("%s",RealLuceAuctionApplication.loginID);
        SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS, MODE_PRIVATE);
        String langFlg = settings.getString(RealLuceAuctionApplication.SETTINGS_VIEW_LANGUAGE,"").equals("en")?"1":"0";
        String encodedParam = encode("TokiokaAbility",hmacData);
        result = String.format("%s?phoneFlg=1&loginId=%s&searchMode=%d&cornerId=5&Brand=%s&MedCat=%s&Rank=%s&FreeWord=%s&FromBox=%s&ToBox=%s"
                ,nyusatsu_url,RealLuceAuctionApplication.loginID
                ,searchMode
                ,brandList.replace("'","").replace("  "," ")
                ,subCatList.replace("'","")
                ,rankList.replace("'","")
                ,freeWord.replace("'","")
                ,boxNo.replace("'","")
                ,boxNoTo.replace("'","")
                );
        return result;
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //directToActivity(TopSearchActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("document.getElementById('detailModal').style.display", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    if(value.equals("\"block\"")){
                        webView.evaluateJavascript("document.getElementById('detailCloseBtn').click()",null);
                    }
                    else{
                        directToActivity(TopSearchActivity.class);
                    }
                }
            });
        }
    }
}
