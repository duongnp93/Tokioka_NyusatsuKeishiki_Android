package tokiokaability.auction24.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import androidx.core.app.ActivityCompat;

import android.provider.Browser;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import org.apache.commons.codec.binary.Hex;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import tokiokaability.auction24.BannerImageAdapter;
import tokiokaability.auction24.BannerImageAdapter.ImageModel;
import tokiokaability.auction24.HTTPInfo.AnnounceInfo;
import tokiokaability.auction24.HTTPInfo.BannerInfo;
import tokiokaability.auction24.HTTPInfo.HTTPService;
import tokiokaability.auction24.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction24.HTTPInfo.LoginInfo;
import tokiokaability.auction24.HTTPInfo.PlaceInfo;
import tokiokaability.auction24.R;
import tokiokaability.auction24.RealLuceAuctionApplication;
import tokiokaability.auction24.commons.MessageBox;
import tokiokaability.auction24.commons.Utilities;
import tokiokaability.auction24.view.ViewPagerCustomDuration;

//import android.webkit.WebView;


public class TopSearchActivity extends BaseActivity {

    private static final String TOP_SEARCH_PLACE_ID = "TopSearchPlaceId";
    private static final String TOP_SEARCH_ONE_PRICE_FLG = "TopSearchOnePriceFlg";

    private List<PlaceInfo> placeInfoList = null;

    private ImageView blockAds = null;
    private ImageButton btnBit = null;
    private ImageButton btnBit2 = null;

    private RelativeLayout blockPlace = null;

    private LinearLayout blockSearch = null;
    private Button btnProductSearch = null;




    private TextView lblTopTitle = null;
    private TextView lblNoticeTitle = null;


    private BannerInfo bannerInfo = null;
    private ViewPagerCustomDuration mPager;
    private ArrayList<ImageModel> imageModelArrayList;
    private CirclePageIndicator indicator;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private RelativeLayout blockBanner;


    Handler handler;
    Runnable Update;
    Timer swipeTimer;

    Button kakoBtn,logoffBtn,mypageBtn,soubaBtn;
    TextView kaisaiTxt1,kaisaiTxt2,kaisaiTxt3,kaisaiTxt4,placeHeaderTxt,tokeiTxt,kadenTxt,bagTxt,wlTxt,jwTxt;
    ImageButton tokeiBtn,kadenBtn,bagBtn,wlBtn,jwBtn;
    ListView lvAnnounce;
    ImageButton btnmenu1,btnMenu2,btnMenu3,btnMenu4;
    //************************ SCREEN LIFECYCLE ******************
    /*
     * Initialization
     */
    @Override
    protected void initialization() {
        setContentView(R.layout.activity_top_search);
        super.initialization();



        blockPlace = (RelativeLayout) this.findViewById(R.id.blockPlace);

        blockSearch = (LinearLayout) this.findViewById(R.id.blockSearch);

        btnBit = (ImageButton) this.findViewById(R.id.btnBit);
        btnBit2 = (ImageButton) this.findViewById(R.id.btnBit2);
        btnBit.setTag("1");
        btnBit2.setTag("2");
        btnProductSearch = (Button) this.findViewById(R.id.btnProductSearch);




        lblTopTitle = (TextView) this.findViewById(R.id.top_title);
        lblNoticeTitle = (TextView) this.findViewById(R.id.lblNoticeTitle);


        blockBanner = this.findViewById(R.id.blockBanner);
        mPager = findViewById(R.id.pager);
        indicator = findViewById(R.id.indicator);
        indicator.setCentered(true);
        indicator.setFillColor(Color.parseColor("#024AA6"));
        indicator.setPageColor(Color.parseColor("#ffffff"));
        indicator.setSnap(false);
        currentPage = 0;

        btnBit.setVisibility(View.GONE);
        btnBit2.setVisibility(View.GONE);

        blockPlace.setVisibility(View.INVISIBLE);

        btnProductSearch.setEnabled(false);

        btnBit.setEnabled(true);
        btnBit2.setEnabled(true);
        //btnBit.setOnClickListener(btnBitClick);
        btnProductSearch.setOnClickListener(btnProductSearchClick);


        lblTopTitle.setText(RealLuceAuctionApplication.memberName);
        setHelpResourceId(R.layout.bid_help);
        kakoBtn = findViewById(R.id.kakoBtn);
        kakoBtn.setOnClickListener(btnKakoClick);
        soubaBtn = findViewById(R.id.soubaBtn);
        soubaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(RealLuceAuctionApplication.systemInfo.getSoubaUrl().length() == 0)
                    return;
                String url = RealLuceAuctionApplication.systemInfo.getSoubaUrl()+
                        encode("TokiokaAbility",RealLuceAuctionApplication.loginID);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
        logoffBtn= findViewById(R.id.btnLogoff);
        kaisaiTxt1= findViewById(R.id.kaisai1Txt);
        kaisaiTxt2= findViewById(R.id.kaisai2Txt);
        kaisaiTxt3= findViewById(R.id.kaisai3Txt);
        kaisaiTxt4= findViewById(R.id.kaisai4Txt);

        tokeiTxt= findViewById(R.id.tokeiTxt);
        kadenTxt= findViewById(R.id.kadenTxt);
        bagTxt= findViewById(R.id.bagTxt);
        wlTxt= findViewById(R.id.wlTxt);
        jwTxt= findViewById(R.id.jwTxt);


        tokeiBtn= findViewById(R.id.btnTokei);
        kadenBtn= findViewById(R.id.btnKaden);
        bagBtn= findViewById(R.id.btnBag);
        wlBtn= findViewById(R.id.btnWal);
        jwBtn= findViewById(R.id.btnJw);


        tokeiBtn.setOnClickListener(btnCornerClick);
        kadenBtn.setOnClickListener(btnCornerClick);
        bagBtn.setOnClickListener(btnCornerClick);
        wlBtn.setOnClickListener(btnCornerClick);
        jwBtn.setOnClickListener(btnCornerClick);


        ViewTreeObserver viewTreeObserver = blockPlace.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener);

        lvAnnounce = findViewById(R.id.lvAnnounce);
        placeHeaderTxt= findViewById(R.id.placeHeaderTxt);

        btnmenu1= findViewById(R.id.btnMenu1);
        btnMenu2= findViewById(R.id.btnMenu2);
        btnMenu3= findViewById(R.id.btnMenu3);
        btnMenu4= findViewById(R.id.btnMenu4);

        btnmenu1.setOnClickListener(btnMenuClick);
        btnMenu2.setOnClickListener(btnMenuClick);
        btnMenu3.setOnClickListener(btnMenuClick);
        btnMenu4.setOnClickListener(btnMenuClick);
        mypageBtn = findViewById(R.id.btnMyPage);
        mypageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActivity(MyPageActivity.class);
            }
        });
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) fontSetting();

        bindingPlaces(false);
        bindingAnnounce();
        getBanner();
    }

    ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            blockPlace.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            LinearLayout.LayoutParams rel_btn = (LinearLayout.LayoutParams) blockPlace.getLayoutParams();
            int height = blockPlace.getHeight();
            int resize_h = (int)getResources().getDimension(R.dimen.top_palce_minheight);
            if(height < resize_h) {
                rel_btn.height = resize_h;
                blockPlace.setLayoutParams(rel_btn);
            }
        }
    };

    View.OnClickListener btnKakoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int placeIndex = getPlaceInfoIndex(-1);
            if (placeIndex >= 0 && placeIndex <placeInfoList.size()){
                setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
                setActivityParam("SearchMode",1);
                setActivityParam("Mode", 1);
                setActivityParam("PlaceId",placeInfoList.get(placeIndex).getIdNo());
                setActivityParam("OnePriceFlg",placeInfoList.get(placeIndex).getOnePriceFlg());
                setActivityParam("KakoFlag","1");
                showActivity(SearchBoxNoActivity.class);
                currentFunction = MENU_CURRENT_FUNCTION_ITEM_SEARCH;
                resetMainMenuButtons();
            }
        }
    };

    View.OnClickListener btnMenuClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int SearchMode = 0;
            switch (Integer.parseInt(v.getTag().toString())){
                case 1:
                    SearchMode = 3;
                    break;
                case 2:
                    SearchMode = 2;
                    break;
                case 3:
                    SearchMode = 4;
                    break;
                case 4:
                    SearchMode = 5;
                    break;
            }
            PlaceInfo p = RealLuceAuctionApplication.allPlaces.get(0);
            setActivityParam("PlaceID",p.getIdNo());
            setActivityParam("ItemNo","");
            setActivityParam("Freeword","");
            setActivityParam("CornerId","");
            setActivityParam("BoxNo","");
            setActivityParam("BoxNoTo","");
            setActivityParam("SearchMode",SearchMode);
            setActivityParam("KakoFlag","0");
            setActivityParam("CornerList","");
            setActivityParam("SubCatList","");
            setActivityParam("BrandList","");
            setActivityParam("RankList","");
            setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
            showActivity(ItemsActivity.class);
            currentFunction = MENU_CURRENT_FUNCTION_LIST;
            resetMainMenuButtons();
        }
    };


    View.OnClickListener btnCornerClick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            PlaceInfo p = RealLuceAuctionApplication.allPlaces.get(0);
            setActivityParam("PlaceID",p.getIdNo());
            setActivityParam("ItemNo","");
            setActivityParam("Freeword","");
            setActivityParam("CornerId",v.getTag().toString());
            setActivityParam("BoxNo","");
            setActivityParam("BoxNoTo","");
            setActivityParam("SearchMode",1);
            setActivityParam("KakoFlag","0");
            setActivityParam("CornerList","");
            setActivityParam("SubCatList","");
            setActivityParam("BrandList","");
            setActivityParam("RankList","");
            setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
            if(v.getTag().toString().equals("5")){
                showActivity(WebBidActivity.class);
            }
            else{
                currentFunction = MENU_CURRENT_FUNCTION_LIST;
                showActivity(ItemsActivity.class);
                resetMainMenuButtons();
            }
        }
    };



    //************************ BUSINESS LOGIC PROCESSING ****************

    /*
     * Binding Ads
     */
    private void bindingAds() {
        if (RealLuceAuctionApplication.posterImage != null && blockAds != null) {
            Bitmap bitmapPoster = BitmapFactory.decodeByteArray(RealLuceAuctionApplication.posterImage, 0, RealLuceAuctionApplication.posterImage.length);
            blockAds.setImageBitmap(bitmapPoster);
        }
    }

    /*
     * Get place info
     */
    private void bindingPlaces(final boolean isShowDialog) {

        placeInfoList = RealLuceAuctionApplication.allPlaces;
        if(placeInfoList != null){
            setActivityParam("Place", placeInfoList.get(getPlaceInfoIndex(-1)));


            if (placeInfoList != null && placeInfoList.size() > 0) {
                blockPlace.setVisibility(View.VISIBLE);
                bindingPlaceInfo(placeInfoList.get(0));
            }
        }

        HTTPServiceCallback objServiceCallback = new HTTPServiceCallback() {

            @SuppressWarnings("unchecked")
            @Override
            public void onSucessfully(Object returnData) {
                if(isShowDialog)
                    hideProgressDialog();
                //for test
                //returnData = null;

                RealLuceAuctionApplication.allPlaces = (ArrayList<PlaceInfo>) returnData;
                placeInfoList = RealLuceAuctionApplication.allPlaces;
                if (placeInfoList != null && placeInfoList.size() > 0) {
                    setActivityParam("Place", placeInfoList.get(0));
                    blockPlace.setVisibility(View.VISIBLE);
                    bindingPlaceInfo(placeInfoList.get(0));
                }
                else{
                    MessageBox.showWithTwoButtonTwoAction(getActivityContext(), "", getString(R.string.top_cannot_getdata), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            bindingPlaces(true);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            logoff();
                        }
                    },getString(R.string.top_reconnect),getString(R.string.top_back_to_login));

                }
            }


            @Override
            public void onFailed(int errorCode) {
                if(isShowDialog)
                    hideProgressDialog();
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

            }

        };


        HTTPService objService = new HTTPService(objServiceCallback);
        objService.getPlacesByLoginID(RealLuceAuctionApplication.loginID, 5);
        if(isShowDialog)
            showProgressDialog(R.string.please_waiting);
    }

    /*
     * Binding Announce
     */
    private void bindingAnnounce() {
        String[] listAnnounce = new String[]{};
        if(RealLuceAuctionApplication.announceInfo != null){
           listAnnounce = RealLuceAuctionApplication.announceInfo.split(",");
        }
        if (listAnnounce.length > 0) {
            ArrayList<String> listDate = new ArrayList<String>();
            ArrayList<String> listInfo = new ArrayList<String>();
            for (int i = 0; i < listAnnounce.length; i++) {
                if (i % 2 == 0) {
                    listDate.add(listAnnounce[i]);
                } else {
                    String infoStr = listAnnounce[i];
                    listInfo.add(infoStr.replace("\\n\\r", System.getProperty("line.separator")));
                }
            }
            AnnouceAdapter aAdapter = new AnnouceAdapter();
            aAdapter.setmContext(getActivityContext());
            aAdapter.setInfoList(listInfo);
            lvAnnounce.setAdapter(aAdapter);
        }
    }

    /*
     * Binding Place information  to Place panel
     */
    private void bindingPlaceInfo(PlaceInfo placeInfo) {
        kaisaiTxt1.setText(placeInfo.getHoldDate2()+" "+placeInfo.getStartTime2()+" "+placeInfo.getCampaign2());
        kaisaiTxt2.setText(placeInfo.getHoldDate3()+" "+placeInfo.getStartTime3()+" "+placeInfo.getCampaign3());
        kaisaiTxt3.setText(placeInfo.getHoldDate4()+" "+placeInfo.getStartTime4()+" "+placeInfo.getCampaign4());
        kaisaiTxt4.setText(placeInfo.getHoldDate5()+" "+placeInfo.getStartTime5()+" "+placeInfo.getCampaign5());
        if (btnBit != null) {
            if (placeInfo.getRealFlag().equalsIgnoreCase("1")){
                //lblRealFlag.setText(this.getString(R.string.top_search_real_time_text));
                btnBit.setVisibility(View.VISIBLE);
            } else {
                //lblRealFlag.setText("");
                //Duongnp Added 2018/09/18
                btnBit.setVisibility(View.GONE);
            }
        }
        if (btnBit2 != null) {
            if (placeInfo.getRealFlag().equalsIgnoreCase("1")){
                //lblRealFlag.setText(this.getString(R.string.top_search_real_time_text));
                btnBit2.setVisibility(View.VISIBLE);
            } else {
                //lblRealFlag.setText("");
                //Duongnp Added 2018/09/18
                btnBit2.setVisibility(View.GONE);
            }
        }

        if (!placeInfo.getIdNo().equalsIgnoreCase("0")) {
            if (btnProductSearch != null) {
                btnProductSearch.setEnabled(true);

            }

        } else {
            if (btnProductSearch != null) {
                btnProductSearch.setEnabled(false);
            }
        }
    }


    /*
     * Save PlaceInfo
     */
    private void setPlaceInfoIndex(int value) {
        SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(TOP_SEARCH_PLACE_ID, placeInfoList.get(value).getIdNo());
        editor.putString(TOP_SEARCH_ONE_PRICE_FLG, placeInfoList.get(value).getOnePriceFlg());
        editor.commit();
    }


    /*
     * Get index of PlaceInfo on last login
     */
    private int getPlaceInfoIndex(int defaultValue) {
        SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS, MODE_PRIVATE);
        String placeId = settings.getString(TOP_SEARCH_PLACE_ID, "");
        if (((placeId == null) || (placeId.length() == 0)) && !(placeInfoList == null || placeInfoList.isEmpty())) {
            placeId = placeInfoList.get(0).getIdNo();
            setPlaceInfoIndex(0);
        }
        String onePriceFlg = settings.getString(TOP_SEARCH_ONE_PRICE_FLG, "0");
        for (int i = 0; i < placeInfoList.size(); i++) {
            if (placeId.equalsIgnoreCase(placeInfoList.get(i).getIdNo()) && onePriceFlg.equalsIgnoreCase(placeInfoList.get(i).getOnePriceFlg())) {
                return i;
            }
        }
        if (defaultValue == -1 && !(placeInfoList == null || placeInfoList.isEmpty())) {
            placeId = placeInfoList.get(0).getIdNo();
            setPlaceInfoIndex(0);
            defaultValue = 0;
        }
        return defaultValue;
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnBit.setEnabled(true);
        btnBit2.setEnabled(true);
        if(currentFunction!= MENU_CURRENT_FUNCTION_SEARCH){
            currentFunction = MENU_CURRENT_FUNCTION_SEARCH;
            resetMainMenuButtons();
        }
    }

    /*
     * Check Login
     */
    private void checkLoginAndShowAuction() {

        HTTPService objServiceCheckLogin = new HTTPService(new HTTPServiceCallback() {
            @Override
            public void onSucessfully(Object returnData) {
                LoginInfo loginInfo = (LoginInfo) returnData;

                if (loginInfo.getResult().equalsIgnoreCase("1")) {
                    if((placeInfoList.get(0).getAuctionEndFlg().equals("1")&& RealLuceAuctionApplication.lane_id.equals("1"))
                    ||(placeInfoList.get(0).getAuctionEndFlg5().equals("1")&& RealLuceAuctionApplication.lane_id.equals("2"))){

                        MessageBox.show(getActivityContext(), getString(R.string.auction_end_information), getString(R.string.auction_all_lane_close), new DialogInterface.OnDismissListener(){
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                hideProgressDialog();
                                return;
                            }
                        });
                    }
                    else{
                        setActivityParam("Place", placeInfoList.get(getPlaceInfoIndex(-1)));

                        setActivityParam("From", "TopSearch");
                        setTransitAnimationType(TRANSIT_ANIMATION_FULL_LEFT);
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

    //Duongnp Added Custom fonts


    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected  void onDestroy() {
        super.onDestroy();
        if(swipeTimer != null){
            swipeTimer.cancel();
        }
    }

    private void fontSetting() {
        AssetManager am = getApplicationContext().getAssets();
        Typeface yGB = Typeface.createFromAsset(am, "fonts/YUGOTHB.TTC");
        Typeface yGL = Typeface.createFromAsset(am, "fonts/YUGOTHL.TTC");
        Typeface yGM = Typeface.createFromAsset(am, "fonts/YUGOTHM.TTC");
        Typeface yGR = Typeface.createFromAsset(am, "fonts/YUGOTHR.TTC");
        kakoBtn.setTypeface(yGB);
        soubaBtn.setTypeface(yGB);
        kaisaiTxt1.setTypeface(yGR);
        kaisaiTxt2.setTypeface(yGR);
        kaisaiTxt3.setTypeface(yGR);
        kaisaiTxt4.setTypeface(yGR);

        tokeiTxt.setTypeface(yGR);
        kadenTxt.setTypeface(yGR);
        bagTxt.setTypeface(yGR);
        wlTxt.setTypeface(yGR);
        jwTxt.setTypeface(yGR);


        lblTopTitle.setTypeface(yGB);
        logoffBtn.setTypeface(yGB);
        placeHeaderTxt.setTypeface(yGB);
        btnProductSearch.setTypeface(yGB);
        lblNoticeTitle.setTypeface(yGB);
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

    //******************** LISTENERS ********************

    /** THANHLV **/
    private View.OnClickListener blockAdsClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (RealLuceAuctionApplication.posterURL != null) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(RealLuceAuctionApplication.posterURL));
                startActivity(browserIntent);
            }
        }
    };


    private View.OnClickListener btnBitClick = new View.OnClickListener() {
        public void onClick(View v) {
            btnBit.setEnabled(false);
            Utilities.writeLog(getActivityContext(), RealLuceAuctionApplication.getConfiguration("info_log_file"), "[TopSearchScreen] RealAuction Button Pressed");
            PlaceInfo p = placeInfoList.get(0);

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
            //final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(SplashActivity.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(getActivityContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                objService.login(RealLuceAuctionApplication.loginID, "", "02-00-00-00-00-00", "2", RealLuceAuctionApplication.publicIPAddress,"","1",
                        RealLuceAuctionApplication.countryCode);
            }

            checkLoginAndShowAuction();
        }
    };

    private View.OnClickListener btnProductSearchClick = new View.OnClickListener() {
        public void onClick(View v) {
        	int placeIndex = getPlaceInfoIndex(-1); 
        	if (placeIndex >= 0 && placeIndex <placeInfoList.size()){
				setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
				setActivityParam("SearchMode",1);
				setActivityParam("Mode", 1);
				setActivityParam("PlaceId",placeInfoList.get(placeIndex).getIdNo());
				setActivityParam("OnePriceFlg",placeInfoList.get(placeIndex).getOnePriceFlg());
                setActivityParam("KakoFlag","0");
				showActivity(SearchBoxNoActivity.class);
                currentFunction = MENU_CURRENT_FUNCTION_ITEM_SEARCH;
				resetMainMenuButtons();
        	}
        }
    };

    private View.OnClickListener btnFreeWordSearchClick = new View.OnClickListener() {
        public void onClick(View v) {
        	int placeIndex = getPlaceInfoIndex(-1); 
        	if (placeIndex >= 0 && placeIndex <placeInfoList.size()){
				setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
				setActivityParam("SearchMode",1);
				setActivityParam("Mode", 2);
				setActivityParam("PlaceId",placeInfoList.get(placeIndex).getIdNo());
				setActivityParam("OnePriceFlg",placeInfoList.get(placeIndex).getOnePriceFlg());
				showActivity(SearchFreewordActivity.class);
        	}
        }
    };

    private View.OnClickListener btnCornerSearchClick = new View.OnClickListener() {
        public void onClick(View v) {
        	int placeIndex = getPlaceInfoIndex(-1); 
        	if (placeIndex >= 0 && placeIndex <placeInfoList.size()){
	    		setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
	    		setActivityParam("SearchMode",1);
	    		setActivityParam("Mode", 3);
	    		setActivityParam("PlaceId",placeInfoList.get(placeIndex).getIdNo());
				setActivityParam("OnePriceFlg",placeInfoList.get(placeIndex).getOnePriceFlg());
	    		showActivity(SearchCornerActivity.class);

//                setActivityParam("PlaceID",placeInfoList.get(placeIndex).getIdNo());
//                setActivityParam("ItemNo","");
//                setActivityParam("Freeword","");
//                setActivityParam("CornerId","1");
//                setActivityParam("BoxNo","");
//                setActivityParam("PlaceName",placeInfoList.get(placeIndex).getName());
//                setActivityParam("SearchMode",1);
//                setActivityParam("Mode", 3);
//
//                setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
//                showActivity(ItemsActivity.class);
	        }
        }
    };

    private View.OnClickListener btnCarSearchClick = new View.OnClickListener() {
        public void onClick(View v) {
        	int placeIndex = getPlaceInfoIndex(-1); 
        	if (placeIndex >= 0 && placeIndex <placeInfoList.size()){
	    		setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
	    		setActivityParam("SearchMode",1);
	    		setActivityParam("Mode", 4);
	    		setActivityParam("PlaceId",placeInfoList.get(placeIndex).getIdNo());
				setActivityParam("OnePriceFlg",placeInfoList.get(placeIndex).getOnePriceFlg());
	    		showActivity(ManufacturersActivity.class);
        	}
        }
    };

    //Duongnp Added bannerImage
    void getBanner(){
        showProgressDialog(R.string.please_waiting);
        HTTPService objBannerService = new HTTPService(new HTTPServiceCallback() {
            @Override
            public void onSucessfully(Object returnData) {
                imageModelArrayList = new ArrayList<ImageModel>();
                bannerInfo = (BannerInfo) returnData;
                hideProgressDialog();
                //ImageModel imgModel1 = new ImageModel();
                ImageModel imgModel2 = new ImageModel();
                ImageModel imgModel3 = new ImageModel();
                ImageModel imgModel4 = new ImageModel();
                ImageModel imgModel5 = new ImageModel();
                NUM_PAGES = 0;
//                if(bannerInfo.getBannerUrlDelFlag1().equals("0") && bannerInfo.getBannerImage1().length > 0){
//                    imgModel1.setByteArr(bannerInfo.getBannerImage1());
//                    imgModel1.setBannerUrl(bannerInfo.getBannerUrl1());
//                    imageModelArrayList.add(imgModel1);
//                    NUM_PAGES++;
//                }
                if(bannerInfo.getBannerUrlDelFlag2().equals("0") && bannerInfo.getBannerImage2().length > 0){
                    imgModel2.setByteArr(bannerInfo.getBannerImage2());
                    imgModel2.setBannerUrl(bannerInfo.getBannerUrl2());
                    imageModelArrayList.add(imgModel2);
                    NUM_PAGES++;
                }
                if(bannerInfo.getBannerUrlDelFlag3().equals("0") && bannerInfo.getBannerImage3().length > 0){
                    imgModel3.setByteArr(bannerInfo.getBannerImage3());
                    imgModel3.setBannerUrl(bannerInfo.getBannerUrl3());
                    imageModelArrayList.add(imgModel3);
                    NUM_PAGES++;
                }
                if(bannerInfo.getBannerUrlDelFlag4().equals("0") && bannerInfo.getBannerImage4().length > 0){
                    imgModel4.setByteArr(bannerInfo.getBannerImage4());
                    imgModel4.setBannerUrl(bannerInfo.getBannerUrl4());
                    imageModelArrayList.add(imgModel4);
                    NUM_PAGES++;
                }
                if(bannerInfo.getBannerUrlDelFlag5().equals("0") && bannerInfo.getBannerImage5().length > 0){
                    imgModel5.setByteArr(bannerInfo.getBannerImage5());
                    imgModel5.setBannerUrl(bannerInfo.getBannerUrl5());
                    imageModelArrayList.add(imgModel5);
                    NUM_PAGES++;
                }
                if(imageModelArrayList.size() == 0){
                    blockBanner.setVisibility(View.INVISIBLE);
                }
                else{
                    blockBanner.setVisibility(View.VISIBLE);
                    final BannerImageAdapter bAdapter = new BannerImageAdapter(TopSearchActivity.this,imageModelArrayList);
                    mPager.setAdapter(bAdapter);
                    mPager.setScrollDuration(500);
                    indicator.setViewPager(mPager);

                    indicator.setRadius(3*getResources().getDisplayMetrics().density);
                    mPager.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            String uri = imageModelArrayList.get(currentPage == 0?0:currentPage-1).getBannerUrl();
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            browserIntent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());
                            startActivity(browserIntent);
                            return false;
                        }
                    });

                    // Auto start of viewpager
                    handler = new Handler();
                    Update = new Runnable() {
                        public void run() {
                            if (currentPage == NUM_PAGES) {
                                currentPage = 0;
//                                addFirstImage(bAdapter);
//                                mPager.setCurrentItem(currentPage, true);
//                                for(int i=0;i<NUM_PAGES-1;i++)
//                                    addAndRemoveFirstImage(bAdapter);
//                                currentPage = 0;
                            }
                            mPager.setCurrentItem(currentPage, true);
                            currentPage++;
                        }
                    };
                    swipeTimer  = new Timer();
                    swipeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(Update);
                        }
                    }, 2500, 2500);
                }

            }

            @Override
            public void onFailed(int errorCode) {
                hideProgressDialog();
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

        objBannerService.getStartImage();
    }

    public void reLoadAnnounce(){
        HTTPServiceCallback objServiceCallback = new HTTPServiceCallback(){

            @SuppressWarnings("unchecked")
            @Override
            public void onSucessfully(Object returnData) {
                RealLuceAuctionApplication.announceInfo = ((AnnounceInfo) returnData).getInfo();
                bindingAnnounce();
            }


            @Override
            public void onFailed(int errorCode) {
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
        };


        HTTPService objService = new HTTPService(objServiceCallback);
        objService.getAnnounce("0");

    }


    public class AnnouceAdapter extends BaseAdapter{

        private ArrayList<String> infoList;
        private Context mContext;
        public ArrayList<String> getInfoList() {
            return infoList;
        }

        public void setInfoList(ArrayList<String> infoList) {
            this.infoList = infoList;
        }

        public Context getmContext() {
            return mContext;
        }

        public void setmContext(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return infoList.size();
        }

        @Override
        public Object getItem(int position) {
            return infoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AnnounceHolder aHolder;
            if(convertView == null){
                LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
                convertView = inflater.inflate(R.layout.announce_list_item,parent,false);
                aHolder = new AnnounceHolder();
                aHolder.txtContent = convertView.findViewById(R.id.txtContent);
                AssetManager am = getApplicationContext().getAssets();
                Typeface yGB = Typeface.createFromAsset(am, "fonts/YUGOTHB.TTC");
                aHolder.txtContent.setTypeface(yGB);
                convertView.setTag(aHolder);
            }
            else{
                aHolder = (AnnounceHolder) convertView.getTag();
            }
            aHolder.txtContent.setText(infoList.get(position));
            return convertView;
        }

        class AnnounceHolder{
            TextView txtContent;
        }
    }
}
