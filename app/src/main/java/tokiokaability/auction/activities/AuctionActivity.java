package tokiokaability.auction.activities;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tokiokaability.auction.HTTPInfo.InquiryInfo;
import tokiokaability.auction.HTTPInfo.RestInfo;
import tokiokaability.auction.HTTPInfo.ResultDisplayIntervalInfo;
import tokiokaability.auction.commons.CustomImageAdapter;
import tokiokaability.auction.commons.Utilities;
import jp.co.rasis.library.audio.*;

import tokiokaability.auction.R;
import tokiokaability.auction.RealLuceAuctionApplication;
import tokiokaability.auction.HTTPInfo.ItemInfo;
import tokiokaability.auction.HTTPInfo.HTTPService;
import tokiokaability.auction.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction.HTTPInfo.LaneInfo;
import tokiokaability.auction.HTTPInfo.LoginInfo;
import tokiokaability.auction.HTTPInfo.PlaceInfo;
import tokiokaability.auction.commons.MessageBox;
import tokiokaability.auction.dialog.ConnectionDialog;
import tokiokaability.auction.dialog.ItemDetailDialog;
import tokiokaability.auction.dialog.SuccessfullBitDialog;
import tokiokaability.auction.socket.SocketsManagement;
import tokiokaability.auction.socket.SocketsManagementCallback;
import tokiokaability.auction.socket.SocketsStatus;
import tokiokaability.auction.socket.client.ActivedSocketClient;
import tokiokaability.auction.socket.client.SocketReceivedCallback;
import tokiokaability.auction.socket.info.AuctionInfo;
import tokiokaability.auction.view.ViewPagerCustomDuration;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;


public class AuctionActivity extends BaseActivity implements
        Runnable {

    private ImageButton btnMode = null;

    private ImageButton btnSeriVoice = null;

    private TextView imvStatusStart = null;
    private ImageView imvStatusSellOut = null;
    private TextView imvStatusSucessfullBit = null;
    private TextView imvStatusDecide = null;
    private TextView imvStatusHoRyuu = null;
    private TextView imvStatusHiki = null;
    private TextView imvStatusCorrect = null;
    private TextView imvStatusTopBit = null;

    private TextView lblCurrentPriceMan = null;
    private TextView lblLabelYen = null;

    private TextView lblBrandName = null;
    private TextView lblItemName = null;
    private TextView lblState = null;
    private TextView lblModel = null;
    private Button btnPush = null;
    private RelativeLayout blockLaneImageBorder = null;
    private RelativeLayout blockActionDetail = null;

    private SuccessfullBitDialog successfullBitDialog;
    private ItemDetailDialog itemDetailDialog;
    private ConnectionDialog connectionDialog;
    private List<LaneInfo> lstLanes;
    private int activedLaneIndex;
    private SocketsManagement socketsManagement;
    private Map<String, MediaPlayer> mediaPlayers = null;
    private boolean isViewMode;
    private boolean isAllLaneClosed = false;
    private boolean firstResume = true;
    private Date pausedTime;
    private PlaceInfo place;
    private TextView lblMemberId;
    private TextView lblUnder;
    private TextView lblOver, lblAbsenceBidPrice,lblmemoshort,lblMemolong;
    private ImageButton btnBack, btnRate;
    TextView txtBoxNo, txtRank, txtAcc;
    boolean bidDekiru = false;
    ItemInfo currentItem;
    private long currentAskingPrice;
    private AudioManager audio;
    // 会場音声のオブジェクト
    private SeriAudio seriAudio = null;
    // オーディオのオンオフフラグ
    private boolean IsTurnOnSeriAudio = false;

    public static int currentRate = 1;

    ViewPagerCustomDuration mPager;
    CirclePageIndicator indicator;
    private ArrayList<CustomImageAdapter.ImageModel> imgArr;
    CustomImageAdapter imgAdapter;
    Button detailBtn;
    TextView lblCountdown;
    float density;
    float dpHeight;
    float dpWidth;
    public int retryCount = 0;
    public boolean preConnectAudio = true;
    public int voicePortNo;

    private long startTime = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private volatile boolean stopRun = false;
    private long targetTime = 0;
    boolean onetimeVoice = false;
    LinearLayout kyuKeLinear;
    TextView txtrestDes1,txtRestTime,txtRestDes2,txtRestSellNo,txtRestDes3,txtRestDes4;
    ImageView restBack;
    //************************ SCREEN LIFECYCLE ******************
    /*
     * Initialization
     */
    @Override
    protected void initialization() {
        setContentView(R.layout.activity_auction);
        super.initialization();

        btnMode = (ImageButton) findViewById(R.id.btnMode);

        btnSeriVoice = (ImageButton) findViewById(R.id.btnSeriVoice);

        imvStatusStart = (TextView) findViewById(R.id.imvStatusStart);
        imvStatusSellOut = (ImageView) findViewById(R.id.imvStatusSellOut);
        imvStatusSucessfullBit = (TextView) findViewById(R.id.imvStatusSuccessfullBit);
        imvStatusDecide = (TextView) findViewById(R.id.imvStatusDecide);
        imvStatusHoRyuu = (TextView) findViewById(R.id.imvStatusHoRyuu);
        imvStatusHiki = (TextView) findViewById(R.id.imvStatusHiki);
        imvStatusCorrect = (TextView) findViewById(R.id.imvStatusCorrect);
        imvStatusTopBit = (TextView) findViewById(R.id.imvStatusTopBit);
        detailBtn = findViewById(R.id.bid_detail_btn);
        kyuKeLinear = findViewById(R.id.kyuke);
        txtrestDes1 = findViewById(R.id.txtDes1);
        txtRestDes2 = findViewById(R.id.txtDes2);
        txtRestTime = findViewById(R.id.txtTime);
        txtRestSellNo = findViewById(R.id.txtSellNo);
        txtRestDes3 = findViewById(R.id.rest_des3);
        txtRestDes4= findViewById(R.id.rest_des4);
        restBack = findViewById(R.id.restBack);
        lblCurrentPriceMan = (TextView) findViewById(R.id.lblCurrentPriceMan);
        lblLabelYen = (TextView) findViewById(R.id.lblLabelYen);

        lblBrandName = (TextView) findViewById(R.id.lblBrandName);
        btnPush = findViewById(R.id.btnPush);
        blockLaneImageBorder = (RelativeLayout) findViewById(R.id.blockLaneImageBorder);
        ViewTreeObserver viewTreeObserver = blockLaneImageBorder.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getDisplaySize();
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) blockLaneImageBorder.getLayoutParams();
                lp.height = (int) (dpWidth);
                blockLaneImageBorder.setLayoutParams(lp);
                blockLaneImageBorder.requestLayout();
                RelativeLayout.LayoutParams c_lp = (RelativeLayout.LayoutParams) indicator.getLayoutParams();
                indicator.setPadding(0, (int) (dpWidth - 70 * density - 0.5f), 0, 0);
                RelativeLayout.LayoutParams b_lp = (RelativeLayout.LayoutParams) btnPush.getLayoutParams();
                RelativeLayout.LayoutParams t_lp = (RelativeLayout.LayoutParams) lblItemName.getLayoutParams();
                b_lp.topMargin = (int) (dpHeight / 10);
                t_lp.topMargin = (int) (dpHeight / 10 - 10 * density);
                btnPush.setLayoutParams(b_lp);
                lblItemName.setLayoutParams(t_lp);
                btnPush.requestLayout();
                lblItemName.requestLayout();
            }
        });
        //blockCorrectionDetail = (RelativeLayout)findViewById(R.id.blockCorrectionDetail);
        blockActionDetail = (RelativeLayout) findViewById(R.id.blockActionDetail);
        //lblCorrectDetail = (TextView)findViewById(R.id.lblCorrectDetail);
        lblItemName = (TextView) findViewById(R.id.lblItemName);
        lblState = (TextView) findViewById(R.id.lblState);
        lblModel = (TextView) findViewById(R.id.lblModel);
        lblMemberId = (TextView) findViewById(R.id.lblMemberId);
        lblOver = (TextView) findViewById(R.id.lblOver);
        lblUnder = (TextView) findViewById(R.id.lblUnder);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        lblAbsenceBidPrice = findViewById(R.id.lblAbsenceBidPrice);
        mPager = findViewById(R.id.auctionImg);
        indicator = findViewById(R.id.indicator);
        indicator.setCentered(true);
        indicator.setFillColor(Color.parseColor("#024AA6"));
        indicator.setRadius(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
        indicator.setSnap(false);
        place = (PlaceInfo) getActivityParam("Place");
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        imgArr = new ArrayList<>();
        txtBoxNo = findViewById(R.id.boxNoTxt);
        txtRank = findViewById(R.id.txtRank);
        btnRate = findViewById(R.id.rateBtn);
        txtAcc = findViewById(R.id.lblAcc);
        btnRate.setOnClickListener(btnRateClick);
        String timesNoStr = place.getTimesNo();
        lblCountdown = findViewById(R.id.lblCountdown);

        btnSeriVoice.setOnClickListener(btnSeriVoiceClick);
        btnPush.setOnClickListener(btnPushClick);
        btnMode.setOnClickListener(btnModeClick);
        lblmemoshort = findViewById(R.id.lblMemoshort);
        lblMemolong = findViewById(R.id.lblMemolong);
        lblMemolong.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                lblmemoshort.setVisibility(View.VISIBLE);
                lblMemolong.setVisibility(View.GONE);
            }
        });
        lblmemoshort.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                lblMemolong.setVisibility(View.VISIBLE);
                lblmemoshort.setVisibility(View.GONE);
            }
        });
        //btnYen.setOnClickListener(btnRateClick);
        //btnDollar.setOnClickListener(btnRateClick);
        //btnEuro.setOnClickListener(btnRateClick);
        //btnYuan.setOnClickListener(btnRateClick);
        currentRate = 1;
        ChangeCurrentRate();

        getTargetTime();

        successfullBitDialog = new SuccessfullBitDialog(this, new OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.writeLog(getActivityContext(), RealLuceAuctionApplication.getConfiguration("info_log_file"), "[BidSuccess] Next button pressed");
                successfullBitDialog.dismiss();
                stopRun = true;
            }
        });

        itemDetailDialog = new ItemDetailDialog(this, new OnClickListener() {
            @Override
            public void onClick(View v) {
                itemDetailDialog.dismiss();
            }
        });

        detailBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                itemDetailDialog.show(currentItem);
            }
        });

        connectionDialog = new ConnectionDialog(this,
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        connectionDialog.dismiss();
                        closeActivity();
                    }

                },
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        socketsManagement.start(new SocketsManagmentStatus(), LaneInfo.getGatewayIP());
                    }

                }
        );


        //btnSeriVoice.setImageResource(R.drawable.auction_serivoice_on_button_style);
        btnSeriVoice.setImageResource(R.drawable.auction_serivoice_on_button_style);
        btnMode.setImageResource(R.drawable.auction_mode_auction_button_style);
        IsTurnOnSeriAudio = true;
        retryCount = 0;
        changeLane(-1);
        changeItem(-1, 0);
        changeNextItem(-1);
        updateMode(false);
        loadLanes();
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) setFont();

        // set animation khi quay tro ve.
        if (getActivityParam("From").equals("TopSearch"))
            setTransitAnimationType(TRANSIT_ANIMATION_FULL_RIGHT);
        else
            setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);

    }

    private void setFont() {
        AssetManager am = getApplicationContext().getAssets();
        Typeface yGR = Typeface.createFromAsset(am, "fonts/YUGOTHR.TTC");
        Typeface yGB = Typeface.createFromAsset(am, "fonts/hiragino_kakugo_w6.otf");


        imvStatusStart.setTypeface(yGB);

        imvStatusSucessfullBit.setTypeface(yGB);
        imvStatusDecide.setTypeface(yGB);
        imvStatusHoRyuu.setTypeface(yGB);
        imvStatusHiki.setTypeface(yGB);
        imvStatusCorrect.setTypeface(yGB);
        imvStatusTopBit.setTypeface(yGB);


        lblCurrentPriceMan.setTypeface(yGB);
        //lblLabelYen.setTypeface(hiragino_w3);

        lblBrandName.setTypeface(yGR);


        //lblCorrectDetail.setTypeface(hiragino_w3);
        lblItemName.setTypeface(yGR);
        lblState.setTypeface(yGR);
        lblModel.setTypeface(yGR);
        lblUnder.setTypeface(yGR);
        lblOver.setTypeface(yGB);
        lblMemberId.setTypeface(yGR);
        lblAbsenceBidPrice.setTypeface(yGB);
        txtAcc.setTypeface(yGR);
        txtBoxNo.setTypeface(yGB);
        txtRank.setTypeface(yGB);
        lblCountdown.setTypeface(yGB);
        txtrestDes1.setTypeface(yGB);
        txtRestTime.setTypeface(yGB);
        txtRestDes3.setTypeface(yGR);
        txtRestDes4.setTypeface(yGR);
        txtRestDes2.setTypeface(yGR);
        txtRestSellNo.setTypeface(yGB);
        lblmemoshort.setTypeface(yGB);
        lblMemolong.setTypeface(yGB);
    }

    @Override
    protected void onResume() {
        super.onResume();

        btnBack.setEnabled(true);
        btnSeriVoice.setEnabled(true);
        btnMode.setEnabled(true);
        if (firstResume)
            isViewMode = false;
        isAllLaneClosed = false;

        blockLaneImageBorder.setPadding(0, 0, 0, 0);
        //blockCorrectionDetail.setVisibility(View.INVISIBLE);

        if (!firstResume) {
            Date currentTime = new Date();
            if (currentTime.getTime() - pausedTime.getTime() >= 1000 * RealLuceAuctionApplication.getIntConfiguration("app_time_out")) {
                RealLuceAuctionApplication.loginID = null;
                RealLuceAuctionApplication.password = null;
                RealLuceAuctionApplication.isConneted = true;
                RealLuceAuctionApplication.isDefaultURL = true;
                RealLuceAuctionApplication.baseURL = RealLuceAuctionApplication.defaultBaseURL;
                finish();
                return;
            }

            //socketsManagement.start(new SocketsManagmentStatus(), LaneInfo.getGatewayIP());
            loadLanes();
        }

        firstResume = false;

    }

    @Override
    protected void onPause() {

        btnSeriVoice.setEnabled(false);
        btnPush.setEnabled(false);
        btnMode.setEnabled(false);

        HTTPService.cancelAll();
        successfullBitDialog.dismiss();
        stopRun = true;
        connectionDialog.dismiss();

        if (socketsManagement != null) {
            socketsManagement.stop();
        }

        if (mediaPlayers != null) {
            mediaPlayers.get("start").release();
            mediaPlayers.get("end").release();
            mediaPlayers.get("lane_change").release();
            mediaPlayers.get("mode").release();
            mediaPlayers.get("push").release();
            mediaPlayers.get("success").release();
            mediaPlayers.get("urikiri").release();
            mediaPlayers.get("nagare").release();
            mediaPlayers = null;
        }
        pausedTime = new Date();

        super.onPause();
    }

    protected void onStop() {
        super.onStop();

    }

    public void onClosing() {
        super.onClosing();

        if (seriAudio != null) {
            seriAudio.Disconnect();
            seriAudio = null;
        }

        btnSeriVoice.setEnabled(false);
        btnPush.setEnabled(false);
        btnMode.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        if (socketsManagement != null) {
            socketsManagement.Dispose();
            socketsManagement = null;
        }

        if (lstLanes != null) {
            for (int i = 0; i < lstLanes.size(); i++) {
                lstLanes.get(i).setActiveSocket(null);
            }
            lstLanes.clear();
        }

        connectionDialog.backClickListener = null;
        connectionDialog.connectListener = null;

        successfullBitDialog = null;
        connectionDialog = null;
        btnPush.setOnClickListener(null);
        btnPushClick = null;
        btnSeriVoice.setOnClickListener(null);
        btnSeriVoiceClick = null;
        seriAudioListener = null;
        btnMode.setOnClickListener(null);
        btnModeClick = null;
        objServiceCheckLogin = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                return true;

            case KeyEvent.KEYCODE_BACK:
                closeActivity();
                return true;
            default:
                return false;
        }
    }

    /**
     * Load Get Target Time from server
     */
    private void getTargetTime() {

        HTTPService objService = new HTTPService(new HTTPServiceCallback() {

            @Override
            public void onSucessfully(Object returnData) {
                ResultDisplayIntervalInfo resultDisplayIntervalInfo = (ResultDisplayIntervalInfo) returnData; // Get Inquiry information

                double targetSec = resultDisplayIntervalInfo.getInterval();
                targetTime = (int)(targetSec * 1000);
            }

            @Override
            public void onFailed(int errorCode) {
                // Hide process dialog.
                hideProgressDialog();
                targetTime = 0;
            }
        });

        showProgressDialog(R.string.please_waiting);
        objService.getResultDisplayInterval();
    }


    //************************ BUSINESS LOGIC PROCESSING ****************

    public static String getMoneySympol(Character tag) {
        switch (tag) {
            case '¥':
                return "¥";
            case '＄':
                return "＄";
            case '€':
                return "€";
            case '元':
                return "元";
        }
        return "";
    }


    private void ChangeCurrentRate() {
        switch (currentRate) {
            case 1:
                btnRate.setImageResource(R.drawable.new_yen);
                break;
            case 2:
                btnRate.setImageResource(R.drawable.new_dollar);
                break;
            case 3:
                btnRate.setImageResource(R.drawable.new_euro);
                break;
            case 4:
                btnRate.setImageResource(R.drawable.new_yuan);
                break;
            case 5:
                btnRate.setImageResource(R.drawable.new_hkd);
                break;
        }
        if (lstLanes == null || lstLanes.size() <= 0 || lstLanes.get(activedLaneIndex).getCurrentItem() == null) {
            lblCurrentPriceMan.setText("");
        } else {
            LaneInfo lane = lstLanes.get(activedLaneIndex);
            lblCurrentPriceMan.setText(getDisplayPrice(lane.getCurrentPrice(), lane.getCurrentItem().getRateDollar(), lane.getCurrentItem().getRateEuro(), lane.getCurrentItem().getRateYuan(), lane.getCurrentItem().getRateHKD()));
        }
    }


    public static String getDisplayPrice(long price, double rateDollar, double rateEuro, double rateYuan, double rateHKD) {
        double displayPrice = 0;
        DecimalFormat formatter = new DecimalFormat("#,###,###");

        if (price == 99999999)
            return "999999999";

        switch (currentRate) {
            case 1:
                displayPrice = price;
                formatter = new DecimalFormat("#,###,###");
                break;
            case 2:
                displayPrice = price * rateDollar;
                //20200619 更新した
                //formatter = new DecimalFormat("#,###,###");
                //displayPrice = price * rateDollar;
                formatter = new DecimalFormat("#,###,###.00");
                break;
            case 3:
                displayPrice = price * rateEuro;
                formatter = new DecimalFormat("#,###,###.00");
                break;
            case 4:
                displayPrice = price * rateYuan;
                formatter = new DecimalFormat("#,###,###");
                break;
            case 5:
                displayPrice = price * rateHKD;
                formatter = new DecimalFormat("#,###,###");
                break;

        }
        if (currentRate != 3)
            return formatter.format(displayPrice);
        else {
            String ret = formatter.format(displayPrice);
            ret = ret.replace('.', ':');
            ret = ret.replace(',', '.');
            ret = ret.replace(':', ',');
            return ret;
        }

    }


    /**
     * Check if allow placing bit, enable Push button else disable Push button
     *
     * @param allowPlaceBit
     */
    private void updateMode(boolean allowPlaceBit) {
        if (RealLuceAuctionApplication.systemInfo.getMode().equalsIgnoreCase("1")
                || isViewMode
                || lstLanes == null
                || lstLanes.size() <= 0
                || activedLaneIndex < 0
                || activedLaneIndex >= lstLanes.size()
                || allowPlaceBit == false
        ) {
            btnPush.setEnabled(false);
        } else {
            btnPush.setEnabled(true);
        }

    }


    /**
     * Change lane.
     *
     * @param laneIndex
     */
    private void changeLane(int laneIndex) {
//		if (activedLaneIndex == laneIndex)
//			return;


        btnPush.setEnabled(false);

        if (activedLaneIndex >= 0 && lstLanes != null && lstLanes.size() > 0) {
            LaneInfo lane = lstLanes.get(activedLaneIndex);

            lane.getActiveSocket().setEnable(false);
            socketsManagement.stopOne(lane.getActiveSocket());

        }


        if (laneIndex == -1) {
            activedLaneIndex = laneIndex;
        } else {
            if (activedLaneIndex != -1) {
                playSound("lane_change");
            }

            activedLaneIndex = laneIndex;
            LaneInfo lane = lstLanes.get(activedLaneIndex);
            lane.setOpen(true);

            lane.getActiveSocket().setEnable(true);
            socketsManagement.startOne(lane.getActiveSocket());

            if (LaneInfo.getVoiceServerIP() == null || LaneInfo.getVoiceServerIP().length() == 0 || lane.getVoicePortNo() <= 0) {
                btnSeriVoice.setVisibility(View.INVISIBLE);
            } else {
                btnSeriVoice.setVisibility(View.VISIBLE);
                if (seriAudio == null) {
                    seriAudio = new SeriAudio();
                }
                if (IsTurnOnSeriAudio) {
                    //btnSeriVoice.setEnabled(false);
                    seriAudio.Connect(LaneInfo.getVoiceServerIP(), lane.getVoicePortNo(), seriAudioListener);
                }
            }

            lane.setCurrentItem(null);
            lane.setNextItem(null);

            changeItem(-1, 0);
            changeNextItem(-1);
        }

    }


    /**
     * 現在商品の変更
     *
     * @param itemNo
     */
    private void changeItem(int itemNo, long currentPrice) {
        LaneInfo lane;

        if (itemNo > 0 && lstLanes != null && lstLanes.size() > 0) {
            lane = lstLanes.get(activedLaneIndex);


            if (lane.getNextItem() != null && lane.getNextItem().getItemNo() == itemNo) {
                lane.setCurrentItem(lane.getNextItem());
                onetimeVoice = false;
                if (lane.getCurrentItem().getBoxNo() != null) {
                    if(lane.getCurrentItem().getOtherLaneSellNo().length() > 0){
                        int otherLaneSellNo = Integer.parseInt(lane.getCurrentItem().getOtherLaneSellNo());
                        if(RealLuceAuctionApplication.lane_id.equals("1")){
                            txtBoxNo.setText(lane.getCurrentItem().getBoxNo() + "-" + lane.getCurrentItem().getBoxSubNo()
                            +" ("+getString(R.string.top_jewel)+" : "+otherLaneSellNo/100+"-"+otherLaneSellNo%100+")");
                        }
                        else {
                            txtBoxNo.setText(lane.getCurrentItem().getBoxNo() + "-" + lane.getCurrentItem().getBoxSubNo()
                                    +" ("+getString(R.string.top_tokei)+" : "+otherLaneSellNo/100+"-"+otherLaneSellNo%100+")");
                        }
                    }
                    else{
                        txtBoxNo.setText(lane.getCurrentItem().getBoxNo() + "-" + lane.getCurrentItem().getBoxSubNo());
                    }
                } else txtBoxNo.setText("");
                if (lane.getCurrentItem().getRank() != null) {
                    txtRank.setText(lane.getCurrentItem().getRank());
                } else {
                    txtRank.setText("");
                }
                if (lane.getCurrentItem().getAbsenceBidPrice() != null && lane.getCurrentItem().getAbsenceBidPrice().length() > 0
                        && Integer.parseInt(lane.getCurrentItem().getAbsenceBidPrice()) > 0) {
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    try {
                        lblAbsenceBidPrice.setText(getString(R.string.register_absence_bid_title) + "：" + formatter.format(Integer.parseInt(lane.getCurrentItem().getAbsenceBidPrice())) + getString(R.string.item_detail_yen_string));
                    } catch (Exception ex) {
                        lblAbsenceBidPrice.setText("");
                    }
                } else {
                    lblAbsenceBidPrice.setText("");
                }
                if(lane.getCurrentItem().getMemoFlg().equals("1") && lane.getCurrentItem().getMemo().length() > 0){
                    lblmemoshort.setText(String.format("%s%s",getActivityContext().getString(R.string.auc_memo_tite)
                            ,lane.getCurrentItem().getMemo().replaceAll("[\r\n]","")));
                    lblMemolong.setText(String.format("%s%s",getActivityContext().getString(R.string.auc_memo_tite)
                            ,lane.getCurrentItem().getMemo().replaceAll("[\r\n]","")));
                    lblmemoshort.setVisibility(View.VISIBLE);
                }
                else{
                    lblmemoshort.setVisibility(View.GONE);
                    lblMemolong.setVisibility(View.GONE);
                }


                if (lane.getCurrentItem().getBrand() != null)
                    lblItemName.setText(lane.getCurrentItem().getBrand());
                else
                    lblItemName.setText("");
                if (lane.getCurrentItem().getSerialNo() != null)
                    lblModel.setText(lane.getCurrentItem().getSerialNo());
                else
                    lblModel.setText("");
                if (lane.getCurrentItem().getModel1() != null)
                    lblState.setText(lane.getCurrentItem().getModel1());
                else
                    lblState.setText("");
                if (lane.getCurrentItem().getAccessories() != null)
                    txtAcc.setText(lane.getCurrentItem().getAccessories());
                else
                    txtAcc.setText("");
                loadViewPager(lane.getCurrentItem());
                currentItem = lane.getCurrentItem();
            } else {
                ItemInfo item = new ItemInfo();
                item.setItemNo(itemNo);
                lane.setCurrentItem(item);


                lblBrandName.setText("");
                lblItemName.setText("");
                lblState.setText("");
                lblModel.setText("");
                txtAcc.setText("");
                lblAbsenceBidPrice.setText("");
                lblMemolong.setText("");
                lblmemoshort.setText("");
                lblmemoshort.setVisibility(View.GONE);
                lblMemolong.setVisibility(View.GONE);
                txtRank.setText("");
                txtBoxNo.setText("");
                loadItemInfo(lane, item, currentPrice);
                loadItemImages(lane, item);
            }

        } else {


            lblBrandName.setText("");
            lblItemName.setText("");
            lblState.setText("");
            lblModel.setText("");
            lblAbsenceBidPrice.setText("");
            txtRank.setText("");
            txtBoxNo.setText("");
            txtAcc.setText("");
            lblMemolong.setText("");
            lblmemoshort.setText("");
            lblmemoshort.setVisibility(View.GONE);
            lblMemolong.setVisibility(View.GONE);
        }

        //drawImageBorder(false);

        lblCurrentPriceMan.setText("");
        lblLabelYen.setText("");

        imvStatusStart.setAlpha(0.0f);
        imvStatusSellOut.setAlpha(0.0f);
        imvStatusSucessfullBit.setAlpha(0.0f);
        imvStatusDecide.setAlpha(0.0f);
        imvStatusHoRyuu.setAlpha(0.0f);
        imvStatusHiki.setAlpha(0.0f);
        imvStatusCorrect.setAlpha(0.0f);
//        imvStatusTopBit.setAlpha(0.0f);

    }


    /**
     * 次の商品に変更して読み込む
     *
     * @param itemNo
     */
    private void changeNextItem(int itemNo) {
        LaneInfo lane;

        if (itemNo > 0 && lstLanes != null && lstLanes.size() > 0) {
            lane = lstLanes.get(activedLaneIndex);
            ItemInfo item = new ItemInfo();
            item.setItemNo(itemNo);
            lane.setNextItem(item);
            loadItemInfo(lane, item, 0);
            loadItemImages(lane, item);
        }
        // 商品詳細のポップアップを閉じる
        if (itemDetailDialog.isShowing()) {
            itemDetailDialog.dismiss();
        }
    }


    /**
     * レーン情報取得
     * Load Lanes from server.
     * Init socket client for all lanes
     * Start lane at index 0
     */
    private void loadLanes() {
        HTTPService objService = new HTTPService(new HTTPServiceCallback() {
            @SuppressWarnings("unchecked")
            @Override
            public void onSucessfully(Object returnData) {
                lstLanes = (List<LaneInfo>) returnData;

                if (lstLanes == null || lstLanes.size() <= 0) {
                    MessageBox.show(getActivityContext(), R.string.place_info_title, R.string.place_info_message_no_place);
                    Utilities.writeLog(getActivityContext(), RealLuceAuctionApplication.getConfiguration("error_log_file"), "[BidScreen] No bid info available");
                } else {
                    if (socketsManagement != null) {
                        socketsManagement.Dispose();
                        socketsManagement = null;
                    }
                    socketsManagement = new SocketsManagement(getActivityContext());
                    socketsManagement.removeAll();
                    for (int i = 0; i < lstLanes.size(); i++) {
                        LaneInfo lane = lstLanes.get(i);
                        ActivedSocketClient activeSocket;

                        // 接続先ゲートウェイのIPアドレスとポート番号の指定
                        activeSocket = new ActivedSocketClient(lane.getPortNo(), LaneInfo.getGatewayIP(), i, new ActivedLaneReceived());
                        activeSocket.setEnable(false);
                        lane.setActiveSocket(activeSocket);
                        socketsManagement.addSocketClient(activeSocket, false);
                        voicePortNo = lane.getVoicePortNo();
                        socketsManagement.start(new SocketsManagmentStatus(), LaneInfo.getGatewayIP());
                    }
                    changeLane(0);
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
                    case HTTPService.ERROR_CANCEL:
                        break;
                    default:
                        MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_unknow);
                        break;

                }
                Utilities.writeLog(getActivityContext(), RealLuceAuctionApplication.getConfiguration("error_log_file"), "[BidScreen] Cannot connect to server");
            }

        });


        showProgressDialog(R.string.please_waiting);

        objService.getLanesByPlaceId(place.getIdNo(), place.getTimesNo(),RealLuceAuctionApplication.lane_id);
    }


    void loadViewPager(ItemInfo itemInfo) {
        imgArr.clear();

        Drawable drawable = getResources().getDrawable(R.drawable.auction_no_photo);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] nophoto = stream.toByteArray();
        if (itemInfo.getImage1() != null) {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(Utilities.rotaledByteArr(itemInfo.getImage1()));
            imgArr.add(imgModel);

        } else {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(nophoto);
            imgArr.add(imgModel);
        }
        if (itemInfo.getImage2() != null) {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(Utilities.rotaledByteArr(itemInfo.getImage2()));
            imgArr.add(imgModel);

        }
//		else{
//			CustomImageAdapter.ImageModel imgModel =new CustomImageAdapter.ImageModel();
//			imgModel.setByteArr(nophoto);
//			imgArr.add(imgModel);
//		}
        if (itemInfo.getImage3() != null) {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(Utilities.rotaledByteArr(itemInfo.getImage3()));
            imgArr.add(imgModel);

        }
//		else{
//			CustomImageAdapter.ImageModel imgModel =new CustomImageAdapter.ImageModel();
//			imgModel.setByteArr(nophoto);
//			imgArr.add(imgModel);
//		}
        if (itemInfo.getImage4() != null) {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(Utilities.rotaledByteArr(itemInfo.getImage4()));
            imgArr.add(imgModel);

        }
//		else{
//			CustomImageAdapter.ImageModel imgModel =new CustomImageAdapter.ImageModel();
//			imgModel.setByteArr(nophoto);
//			imgArr.add(imgModel);
//		}
        if (itemInfo.getImage5() != null) {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(Utilities.rotaledByteArr(itemInfo.getImage5()));
            imgArr.add(imgModel);

        }
//		else{
//			CustomImageAdapter.ImageModel imgModel =new CustomImageAdapter.ImageModel();
//			imgModel.setByteArr(nophoto);
//			imgArr.add(imgModel);
//		}
        if (itemInfo.getImage6() != null) {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(Utilities.rotaledByteArr(itemInfo.getImage6()));
            imgArr.add(imgModel);

        }
        if (itemInfo.getImage7() != null) {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(Utilities.rotaledByteArr(itemInfo.getImage7()));
            imgArr.add(imgModel);

        }
        if (itemInfo.getImage8() != null) {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(Utilities.rotaledByteArr(itemInfo.getImage8()));
            imgArr.add(imgModel);

        }
//        if (itemInfo.getImage9() != null) {
//            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
//            imgModel.setByteArr(Utilities.rotaledByteArr(itemInfo.getImage9()));
//            imgArr.add(imgModel);
//
//        }
//        if (itemInfo.getImage10() != null) {
//            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
//            imgModel.setByteArr(Utilities.rotaledByteArr(itemInfo.getImage10()));
//            imgArr.add(imgModel);
//
//        }
//		else{
//			CustomImageAdapter.ImageModel imgModel =new CustomImageAdapter.ImageModel();
//			imgModel.setByteArr(nophoto);
//			imgArr.add(imgModel);
//		}
        if (imgArr.size() <= 1)
            indicator.setVisibility(View.INVISIBLE);
        else {
            indicator.setVisibility(View.VISIBLE);
        }

        if (imgAdapter == null) {
            imgAdapter = new CustomImageAdapter(getActivityContext(), imgArr);

            mPager.setAdapter(imgAdapter);
            indicator.setViewPager(mPager);
        } else {
            imgAdapter.setImageModelArrayList(imgArr);
            imgAdapter.notifyDataSetChanged();
        }

        mPager.setCurrentItem(0);

    }


    /**
     * Load  info from server
     *
     * @param lane
     * @param item
     */
    private void loadItemInfo(LaneInfo lane, ItemInfo item, final long currentPrice) {

        HTTPService objService = new HTTPService(new HTTPServiceCallback() {
            @Override
            public void onSucessfully(Object returnData) {
                ItemInfo objItem = (ItemInfo) returnData;
                if (lstLanes.size() > 0 && activedLaneIndex >= 0) {
                    LaneInfo lane = lstLanes.get(activedLaneIndex);

                    if (objItem == lane.getCurrentItem()) {
                        onetimeVoice = false;
                        if (lane.getCurrentItem().getBoxNo() != null) {
//                            txtBoxNo.setText(lane.getCurrentItem().getBoxNo() + "-" + lane.getCurrentItem().getBoxSubNo());
                            if(lane.getCurrentItem().getOtherLaneSellNo().length() > 0){
                                int otherLaneSellNo = Integer.parseInt(lane.getCurrentItem().getOtherLaneSellNo());
                                if(RealLuceAuctionApplication.lane_id.equals("1")){
                                    txtBoxNo.setText(lane.getCurrentItem().getBoxNo() + "-" + lane.getCurrentItem().getBoxSubNo()
                                            +" ("+getString(R.string.top_jewel)+" : "+otherLaneSellNo/100+"-"+otherLaneSellNo%100+")");
                                }
                                else {
                                    txtBoxNo.setText(lane.getCurrentItem().getBoxNo() + "-" + lane.getCurrentItem().getBoxSubNo()
                                            +" ("+getString(R.string.top_tokei)+" : "+otherLaneSellNo/100+"-"+otherLaneSellNo%100+")");
                                }
                            }
                            else{
                                txtBoxNo.setText(lane.getCurrentItem().getBoxNo() + "-" + lane.getCurrentItem().getBoxSubNo());
                            }
                        } else txtBoxNo.setText("");
                        if (lane.getCurrentItem().getRank() != null) {
                            txtRank.setText(lane.getCurrentItem().getRank());
                        } else {
                            txtRank.setText("");
                        }
                        if (lane.getCurrentItem().getAbsenceBidPrice() != null && lane.getCurrentItem().getAbsenceBidPrice().length() > 0
                                && Integer.parseInt(lane.getCurrentItem().getAbsenceBidPrice()) > 0) {
                            DecimalFormat formatter = new DecimalFormat("#,###,###");
                            lblAbsenceBidPrice.setText(getString(R.string.bid_fuzai_title) + formatter.format(Integer.parseInt(lane.getCurrentItem().getAbsenceBidPrice())) + getString(R.string.item_detail_yen_string));
                        } else {
                            lblAbsenceBidPrice.setText("");

                        }
                        if(lane.getCurrentItem().getMemoFlg().equals("1") && lane.getCurrentItem().getMemo().length() > 0){
                            lblmemoshort.setText(String.format("%s%s",getActivityContext().getString(R.string.auc_memo_tite),lane.getCurrentItem().getMemo().replaceAll("[\r\n]","")));
                            lblMemolong.setText(String.format("%s%s",getActivityContext().getString(R.string.auc_memo_tite),lane.getCurrentItem().getMemo().replaceAll("[\r\n]","")));
                            lblmemoshort.setVisibility(View.VISIBLE);
                        }
                        else{
                            lblmemoshort.setVisibility(View.GONE);
                            lblMemolong.setVisibility(View.GONE);
                        }
                        if (lane.getCurrentItem().getBrand() != null)
                            lblItemName.setText(lane.getCurrentItem().getBrand());
                        else
                            lblItemName.setText("");
                        if (lane.getCurrentItem().getSerialNo() != null)
                            lblModel.setText(lane.getCurrentItem().getSerialNo());
                        else
                            lblModel.setText("");
                        if (lane.getCurrentItem().getModel1() != null)
                            lblState.setText(lane.getCurrentItem().getModel1());
                        else
                            lblState.setText("");
                        if (lane.getCurrentItem().getAccessories() != null)
                            txtAcc.setText(lane.getCurrentItem().getAccessories());
                        else
                            txtAcc.setText("");
                        try {
                            if (currentPrice >= Long.parseLong(lane.getCurrentItem().getAskingPrice())) {
                                lblOver.setVisibility(View.VISIBLE);
                            } else {
                                lblOver.setVisibility(View.GONE);
                            }
                        } catch (Exception ignored) {
                        }
                        if(lane.getCurrentItem().getLastBidMemberNo().equals(RealLuceAuctionApplication.member_no)) {
                            imvStatusTopBit.setAlpha(1.0f);
                        }
                        else{
                            imvStatusTopBit.setAlpha(0.0f);
                        }

                        if (Integer.toString(lane.getCurrentItem().getItemNo()) != null) {
                            currentItem = lane.getCurrentItem();
                            if(kyuKeLinear.getVisibility() != View.VISIBLE)
                            detailBtn.setVisibility(View.VISIBLE);
                            // ダイアログが開いてたら開き直す
                            if (successfullBitDialog.isShowing()) {
                                startTimerThread();
                                //successfullBitDialog.show(lane, currentRate);
                            }
                        } else {

                        }
//						if (lane.getCurrentItem().getCorrectionDetail() != null)
//							lblCorrectDetail.setText(lane.getCurrentItem().getCorrectionDetail());
                    }
                }
            }

            @Override
            public void onFailed(int errorCode) {

            }


        });
        objService.getItemInfo(lane, item);

    }

    /**
     * サーバーから商品情報の読み込み
     *
     * @param lane
     * @param item
     */
    private void loadItemImages(LaneInfo lane, ItemInfo item) {

        HTTPService objService = new HTTPService(new HTTPServiceCallback() {
            @Override
            public void onSucessfully(Object returnData) {
                ItemInfo objItem = (ItemInfo) returnData;
                if (lstLanes.size() > 0) {
                    LaneInfo lane = lstLanes.get(activedLaneIndex);


                    if (objItem == lane.getCurrentItem()) {
                        loadViewPager(objItem);
                        // ダイアログが開いてたら開き直す
                        if (successfullBitDialog.isShowing()) {
                            startTimerThread();
                            //successfullBitDialog.show(lane, currentRate);
                        }
                    }
                }
            }

            @Override
            public void onFailed(int errorCode) {
            }


        });
        objService.getImages(lane, item, 0, "medium");

    }

    /**
     * Play Sound in background
     *
     * @param soundName
     */
    private void playSound(String soundName) {
        MediaPlayer mediaPlayer;
        int maxVolume = 100;
        int currVolume = 20;
        float log1 = (float) (1 - Math.log(maxVolume - currVolume) / Math.log(maxVolume));
        if (mediaPlayers == null) {
            // soundNameに対応する音声の取得
            mediaPlayers = new HashMap<String, MediaPlayer>();

            mediaPlayer = MediaPlayer.create(RealLuceAuctionApplication.getAppContext(), R.raw.start);
            mediaPlayer.setVolume(log1, log1);
            mediaPlayers.put("start", mediaPlayer);

            mediaPlayer = MediaPlayer.create(RealLuceAuctionApplication.getAppContext(), R.raw.end);
            mediaPlayer.setVolume(log1, log1);
            mediaPlayers.put("end", mediaPlayer);

            mediaPlayer = MediaPlayer.create(RealLuceAuctionApplication.getAppContext(), R.raw.lane_change);
            mediaPlayer.setVolume(log1, log1);
            mediaPlayers.put("lane_change", mediaPlayer);

            mediaPlayer = MediaPlayer.create(RealLuceAuctionApplication.getAppContext(), R.raw.mode);
            mediaPlayer.setVolume(log1, log1);
            mediaPlayers.put("mode", mediaPlayer);

            mediaPlayer = MediaPlayer.create(RealLuceAuctionApplication.getAppContext(), R.raw.push);
            mediaPlayer.setVolume(log1, log1);
            mediaPlayers.put("push", mediaPlayer);

            mediaPlayer = MediaPlayer.create(RealLuceAuctionApplication.getAppContext(), R.raw.success);
            mediaPlayer.setVolume(log1, log1);
            mediaPlayers.put("success", mediaPlayer);

            mediaPlayer = MediaPlayer.create(RealLuceAuctionApplication.getAppContext(), R.raw.urikiri);
            mediaPlayer.setVolume(log1, log1);
            mediaPlayers.put("urikiri", mediaPlayer);

            mediaPlayer = MediaPlayer.create(RealLuceAuctionApplication.getAppContext(), R.raw.nagare);
            mediaPlayer.setVolume(log1, log1);
            mediaPlayers.put("nagare", mediaPlayer);
        }
        mediaPlayer = mediaPlayers.get(soundName);

        // メディアオブジェクトが存在して、音声オンなら音声を鳴らす
        if (mediaPlayer != null && IsTurnOnSeriAudio) {
            mediaPlayer.setVolume(log1, log1);
            mediaPlayer.start();
        }

    }

    /**
     * Draw border
     * @param showBorder
     */
//	private void drawImageBorder(boolean showBorder){
//		if (showBorder){
//			//blockLaneImageBorder.setPadding(15, 15, 15, 15);
//			blockCorrectionDetail.setVisibility(View.VISIBLE);
//		}
//		else{
//			//blockLaneImageBorder.setPadding(0, 0, 0, 0);
//			blockCorrectionDetail.setVisibility(View.INVISIBLE);
//		}
//
//	}


    /**
     * 現在価格の変更
     * Change Current Price
     *
     * @param price
     */
    private void changeCurrentPrice(long price) {
        LaneInfo lane = lstLanes.get(activedLaneIndex);
        lane.setCurrentPrice(price);

        if (lane.getCurrentItem() != null) {
            // 現在号車情報の表示
            lblCurrentPriceMan.setText(getDisplayPrice(price, lane.getCurrentItem().getRateDollar(), lane.getCurrentItem().getRateEuro(), lane.getCurrentItem().getRateYuan(), lane.getCurrentItem().getRateHKD()));
        } else {
            // 現在号車情報が取得できなければ、接続エラーとして扱う
            hideProgressDialog();
            connectionDialog.showConfirmConnecting();
        }

    }

    /**
     * Show Successfully notification Dialog
     */
    private void showSuccessfullNotification() {
        startTimerThread();
        successfullBitDialog.show(lstLanes.get(activedLaneIndex), currentRate);
    }


    /***
     * AndroidはGingerBreadで追加になった"StrictMode"※が3.0以降はデフォルトで有効になっており、メインスレッド(UIスレッド)でネットワーク処理を行うと同例外がスローされるのである。
     * 回避するため、AsyncTaskクラスを継承して、ネットワーク処理などを別のスレッドで実行することクラスを作成
     * @author rasisvietnam
     *
     */
    private class AsyncTaskManager extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {
            LaneInfo lane = lstLanes.get(activedLaneIndex);
            //playSound("push");
            lane.placeBit(place);

            return null;
        }

    }


    //************************ LISTENERS ********************************


    /**
     * 応札ボタン押下処理
     * RateClicked
     */
    public OnClickListener btnRateClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (currentRate == 5)
                currentRate = 1;
            else
                currentRate++;
            ChangeCurrentRate();
        }
    };

    /**
     * 応札ボタン押下処理
     * Push Clicked
     */
    public OnClickListener btnPushClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            playSound("push");
            btnBack.setEnabled(false);
            btnMode.setEnabled(false);
            LaneInfo lane = lstLanes.get(activedLaneIndex);
            if (lane.getCurrentItem() != null && lane.getCurrentItem().getItemNo() > 0) {
                Utilities.writeLog(getActivityContext(), RealLuceAuctionApplication.getConfiguration("info_log_file"), "[BidScreen] Bid button pressed | " + lblCurrentPriceMan.getText());
                new AsyncTaskManager().execute();
            }
        }
    };

    /*
     * 音声のオン・オフの処理
     */
    public OnClickListener btnSeriVoiceClick = new OnClickListener() {
        public void onClick(View v) {
            LaneInfo lane = lstLanes.get(activedLaneIndex);
            if (IsTurnOnSeriAudio) {

                if (seriAudio != null) {
                    seriAudio.Disconnect();
                }

                btnSeriVoice.setImageResource(R.drawable.auction_serivoice_off_button_style);
                IsTurnOnSeriAudio = false;
                preConnectAudio = false;
            } else {
                //Duongnp Added
//				btnSeriVoice.setImageResource(R.drawable.auction_serivoice_on_button_style);
//				IsTurnOnSeriAudio = true;

                if (LaneInfo.getVoiceServerIP() != null && LaneInfo.getVoiceServerIP().length() > 0 && lane.getVoicePortNo() > 0) {
                    //btnSeriVoice.setEnabled(false);
                    if (seriAudio == null) {
                        seriAudio = new SeriAudio();
                    }
                    seriAudio.Connect(LaneInfo.getVoiceServerIP(), lane.getVoicePortNo(), seriAudioListener);
                    btnSeriVoice.setImageResource(R.drawable.auction_serivoice_on_button_style);
                    IsTurnOnSeriAudio = true;
                    preConnectAudio = true;
                }

            }
        }
    };


    public SeriAudioListener seriAudioListener = new SeriAudioListener() {
        @Override
        public void OnConnectSuccess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnSeriVoice.setEnabled(true);
                }
            });

        }

        @Override
        public void OnConnectFailed() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnSeriVoice.setEnabled(true);
                    //Toast.makeText(getBaseContext(), getString(R.string.auction_connect_voice_failed), Toast.LENGTH_LONG).show();
                    //preConnectAudio = IsTurnOnSeriAudio;
                    btnSeriVoice.setImageResource(R.drawable.auction_serivoice_off_button_style);
                    IsTurnOnSeriAudio = false;
                }
            });

        }

        @Override
        public void OnInitDeviceFailed() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnSeriVoice.setEnabled(true);
                    //preConnectAudio = IsTurnOnSeriAudio;
                    //Toast.makeText(getBaseContext(), getString(R.string.auction_start_audio_failed), Toast.LENGTH_LONG).show();
                    btnSeriVoice.setImageResource(R.drawable.auction_serivoice_off_button_style);
                    IsTurnOnSeriAudio = false;
                }
            });
        }

        @Override
        public void OnDropped() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnSeriVoice.setEnabled(true);
                    //preConnectAudio = IsTurnOnSeriAudio;
                    //Toast.makeText(getBaseContext(), getString(R.string.auction_connect_voice_dropped), Toast.LENGTH_LONG).show();
                    btnSeriVoice.setImageResource(R.drawable.auction_serivoice_off_button_style);
                    IsTurnOnSeriAudio = false;
                }
            });

        }
    };


    // 参加・観戦の切り替え
    public OnClickListener btnModeClick = new OnClickListener() {

        @Override
        public void onClick(View arg0) {

            if (isViewMode == false) {
                isViewMode = !isViewMode;
                playSound("mode");
                if (isViewMode) {
                    btnMode.setImageResource(R.drawable.auction_mode_watch_button_style);
                } else {
                    btnMode.setImageResource(R.drawable.auction_mode_auction_button_style);
                }

                updateMode(bidDekiru);
            } else {
                btnMode.setEnabled(false);
                isViewMode = !isViewMode;
                playSound("mode");
                if (isViewMode) {
                    btnMode.setImageResource(R.drawable.auction_mode_watch_button_style);
                } else {
                    btnMode.setImageResource(R.drawable.auction_mode_auction_button_style);
                }

                updateMode(bidDekiru);
                btnMode.setEnabled(true);
            }
        }

    };


    HTTPService objServiceCheckLogin = new HTTPService(new HTTPServiceCallback() {
        @Override
        public void onSucessfully(Object returnData) {
            LoginInfo loginInfo = (LoginInfo) returnData;

            if (loginInfo.getResult().equalsIgnoreCase("1")) {

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
            btnMode.setEnabled(true);
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

    });

    private void releaseBtn() {
        btnBack.setEnabled(true);
        btnMode.setEnabled(true);
    }

    void getDisplaySize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        density = getResources().getDisplayMetrics().density;
        dpWidth = outMetrics.widthPixels;
        dpHeight = outMetrics.heightPixels;
    }
    //************************** SUB CLASSES *************************


    /*
     * Handle socket messages and update UI.
     */
    final class SocketsManagmentStatus implements SocketsManagementCallback {

        @Override
        public void onChangedStatus(SocketsStatus socketsStatus) {

            switch (socketsStatus) {
                case init:
                    break;
                // 接続中
                case Searching:
                case Connecting:

                    if (!connectionDialog.isShowing() && !progressDialog.isShowing()) {
                        showProgressDialog(R.string.connecting_gateway);
                    } else if (connectionDialog.isShowing()) {
                        connectionDialog.showConnecting();
                    }
                    break;
                // 接続成功
                case CompletedConnectSuccessfully:

                    HTTPService objService = new HTTPService(new HTTPServiceCallback() {

                        @Override
                        public void onSucessfully(Object returnData) {
                            retryCount = 0;
                            LaneInfo lane = lstLanes.get(activedLaneIndex);
                            if (IsTurnOnSeriAudio || preConnectAudio) {
                                if (seriAudio == null) {
                                    seriAudio = new SeriAudio();
                                }
                                seriAudio.Connect(LaneInfo.getVoiceServerIP(), lane.getVoicePortNo(), seriAudioListener);
                                btnSeriVoice.setImageResource(R.drawable.auction_serivoice_on_button_style);
                                IsTurnOnSeriAudio = true;
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
                    //final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(SplashActivity.TELEPHONY_SERVICE);
                    objService.login(RealLuceAuctionApplication.loginID, "", "02-00-00-00-00-00", "3", RealLuceAuctionApplication.publicIPAddress, "", "1", RealLuceAuctionApplication.countryCode);
                    hideProgressDialog();
                    if (connectionDialog.isShowing()) {
                        connectionDialog.dismiss();
                    }
                    break;
                // 接続エラー
                case CompletedConnectFailed:
                    if (retryCount < 20) {
                        hideProgressDialog();
                        retryCount++;
                        socketsManagement.start(new SocketsManagmentStatus(), LaneInfo.getGatewayIP());
                    } else {
                        // 開催前ならその旨をダイアログで表示
                        if (!place.getRealFlag().equals("1")) {
                            MessageBox.show(getActivityContext(), getString(R.string.not_today_title), String.format(getString(R.string.not_today)), new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    closeActivity();
                                }
                            });
                        } else if ((place.getAuctionEndFlg().equals("0")&& RealLuceAuctionApplication.lane_id.equals("1"))
                        ||(place.getAuctionEndFlg5().equals("0")&& RealLuceAuctionApplication.lane_id.equals("2"))) {
                            hideProgressDialog();
                            MessageBox.show(getActivityContext(), getString(R.string.auction_start_information), String.format(getString(R.string.not_today)), new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    closeActivity();
                                }
                            });
                        } else if ((place.getAuctionEndFlg().equals("1")&& RealLuceAuctionApplication.lane_id.equals("1"))
                        ||(place.getAuctionEndFlg5().equals("1")&& RealLuceAuctionApplication.lane_id.equals("2"))) {
                            hideProgressDialog();
                            MessageBox.show(getActivityContext(), getString(R.string.auction_end_information), getString(R.string.auction_all_lane_close), new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    closeActivity();
                                }
                            });
                        } else {
                            hideProgressDialog();
                            connectionDialog.showConfirmConnecting();
                        }
                    }

                    break;
                case ConnectionDropped:
                    //progressDialog.setMessage(getText(R.string.connecting_gateway));
                    //progressDialog.show();
                    break;
					
					/*
				case Reconnecting:
					progressDialog.setMessage(getText(R.string.try_connecting_gateway));
					if (!connectionDialog.isShowing())
						progressDialog.show();
					break;
				case CompletedReconnectSuccessfully:
					progressDialog.dismiss();
					connectionDialog.dismiss();
					break;
				case CompletedReconnectFailed:
					progressDialog.dismiss();
					connectionDialog.showConfirmConnecting();
					break;
					*/
                // 切断(オークション終了時)
                case Disconnected:
                    hideProgressDialog();
                    if (connectionDialog.isShowing()) {
                        connectionDialog.dismiss();
                    }
                    break;
            }
        }

    }

    public void getRestTime() {

        HTTPService objService = new HTTPService(new HTTPServiceCallback() {

            @Override
            public void onSucessfully(Object returnData) {
                hideProgressDialog();
                RestInfo restInfo = (RestInfo) returnData; // Get Inquiry information
                txtRestTime.setText(String.format("【%s:%s】",restInfo.getRestHour(),restInfo.getRestMinute()));
                txtRestSellNo.setText(String.format("%s-%s】",restInfo.getRestBox().length() == 4?restInfo.getRestBox():" "+restInfo.getRestBox(),restInfo.getRestBoxSub()));
                kyuKeLinear.setVisibility(View.VISIBLE);
                restBack.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailed(int errorCode) {
                // Hide process dialog.
                hideProgressDialog();
                //targetTime = 0;
            }
        });

        showProgressDialog(R.string.please_waiting);
        objService.getRestTime(RealLuceAuctionApplication.lane_id);
    }

    /*
     * Handle Socket message for receving data. update UI.
     */
    class ActivedLaneReceived implements SocketReceivedCallback {

//		int DEBUG_LASTITEMNO = -1;
//		int DEBUG_CURRENTITEMNO = -1;
//		int DEBUG_NEXTITEMNO = -1;


        // 現在価格など、ソケットデータを受け取ったときの処理
        @Override
        public void onReceivedData(Object param, Object returnData) {
            AuctionInfo auctionInfo = (AuctionInfo) returnData;
            LaneInfo lane = lstLanes.get(activedLaneIndex);
            if(auctionInfo.getBidCountdown() > 0){
                lblCountdown.setText(Integer.toString(auctionInfo.getBidCountdown()));
                lblCountdown.setVisibility(View.VISIBLE);
            }
            else{
                lblCountdown.setVisibility(View.GONE);
            }
            if (!auctionInfo.getCode().equalsIgnoreCase("01"))
                return;

/*
			if (DEBUG_LASTITEMNO != auctionInfo.getItemNo()) {
				DEBUG_LASTITEMNO = auctionInfo.getItemNo();
				if (lane.getCurrentItem() == null || DEBUG_CURRENTITEMNO == 1001) {
					DEBUG_CURRENTITEMNO = 1002;
					DEBUG_NEXTITEMNO = 1001;
				} else {
					DEBUG_CURRENTITEMNO = 1001;
					DEBUG_NEXTITEMNO = 1002;
				}
			}

			auctionInfo.setItemNo(DEBUG_CURRENTITEMNO);
			auctionInfo.setNextItemNo(DEBUG_NEXTITEMNO);
*/
            if(auctionInfo.getItemNo() == 99999){
                getRestTime();
                mPager.setVisibility(View.INVISIBLE);
                detailBtn.setVisibility(View.INVISIBLE);
                lblCurrentPriceMan.setVisibility(View.INVISIBLE);
                //return;
            }
            else{
                kyuKeLinear.setVisibility(View.GONE);
                restBack.setVisibility(View.GONE);
                mPager.setVisibility(View.VISIBLE);
                detailBtn.setVisibility(View.VISIBLE);
                lblCurrentPriceMan.setVisibility(View.VISIBLE);
            }
            switch (auctionInfo.getState()) {
                case '0':// オークション開催前、開催

                    lane.setStartPrice(auctionInfo.getStartPrice());


                    // change Item
                    if (lane.getCurrentItem() == null || lane.getCurrentItem().getItemNo() != auctionInfo.getItemNo()) {
                        changeItem(auctionInfo.getItemNo(), auctionInfo.getPrice());


                    }

                    // change Next Item
                    if (lane.getNextItem() == null || lane.getNextItem().getItemNo() != auctionInfo.getNextItemNo()) {
                        changeNextItem(auctionInfo.getNextItemNo());
                    }
                    String LastMemberID = auctionInfo.getLastMemberID().replace(" ", "");
                    if (LastMemberID.length() > 3) {
                        for (int i = 0; i < LastMemberID.length() - 3; i++) {
                            LastMemberID = LastMemberID.replaceFirst(String.valueOf(LastMemberID.charAt(i)), "*");
                        }
                    } else if (LastMemberID.length() == 3) {
                        for (int i = 0; i < 2; i++) {
                            LastMemberID = LastMemberID.replaceFirst(String.valueOf(LastMemberID.charAt(i)), "*");
                        }
                    } else if (LastMemberID.length() == 2) {
                        for (int i = 0; i < 1; i++) {
                            LastMemberID = LastMemberID.replaceFirst(String.valueOf(LastMemberID.charAt(i)), "*");
                        }
                    } else if (LastMemberID.length() == 1) {
                        LastMemberID = LastMemberID.replaceFirst(String.valueOf(LastMemberID.charAt(0)), "*");
                    }

                    lblMemberId.setText(LastMemberID);
                    // I'm  top bit
                    if (auctionInfo.getLastMemberNo() == Integer.parseInt(place.getMemberNo())) {
                        imvStatusTopBit.setAlpha(1.0f);
                    } else {
                        imvStatusTopBit.setAlpha(0.0f);
                    }

                    // 現在号車情報がないとき、オークションが終了している
                    if (lane.getCurrentItem() == null) {
                        isAllLaneClosed = true;
                        socketsManagement.stop();
                        RealLuceAuctionApplication.allPlaces.get(0).setAuctionEndFlg("1");
                        MessageBox.show(getActivityContext(), R.string.auction_end_information, R.string.auction_all_lane_close, new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                closeActivity();
                            }

                        });
                        break;
                    }

                    changeCurrentPrice(auctionInfo.getPrice());
                    if (lane.getCurrentItem() != null && lane.getCurrentItem().getAskingPrice().length() > 0) {
                        if (auctionInfo.getPrice() >= Long.parseLong(lane.getCurrentItem().getAskingPrice())) {
                            lblOver.setVisibility(View.VISIBLE);
                        } else {
                            lblOver.setVisibility(View.GONE);
                        }
                    }


                    // set Status
                    switch (auctionInfo.getSeri()) {
                        case '0': // 落札前
                            // 売り切り後
                            if (auctionInfo.getFlagSellout() == '1') {
                                imvStatusStart.setAlpha(0.0f);
                                imvStatusSellOut.setAlpha(0.0f);
                                imvStatusSucessfullBit.setAlpha(0.0f);
                                imvStatusDecide.setAlpha(0.0f);
                                imvStatusHoRyuu.setAlpha(0.0f);
                                imvStatusHiki.setAlpha(0.0f);
                            }
                            // スタート
                            else if (auctionInfo.getFlagStart() == '1') {
                                imvStatusStart.setAlpha(1.0f);
                                imvStatusSellOut.setAlpha(0.0f);
                                imvStatusSucessfullBit.setAlpha(0.0f);
                                imvStatusDecide.setAlpha(0.0f);
                                imvStatusHoRyuu.setAlpha(0.0f);
                                imvStatusHiki.setAlpha(0.0f);
                                //setMarginLeft(imvStatusTopBit,70);
                            } else {
                                lblMemberId.setText("");
                                imvStatusStart.setAlpha(0.0f);
                                imvStatusSellOut.setAlpha(0.0f);
                                imvStatusSucessfullBit.setAlpha(0.0f);
                                imvStatusDecide.setAlpha(0.0f);
                                imvStatusHoRyuu.setAlpha(0.0f);
                                imvStatusHiki.setAlpha(0.0f);
                            }
                            break;
                        case '1': // 保留
                            imvStatusStart.setAlpha(0.0f);
                            imvStatusSellOut.setAlpha(0.0f);
                            imvStatusSucessfullBit.setAlpha(0.0f);
                            imvStatusDecide.setAlpha(0.0f);
                            imvStatusHoRyuu.setAlpha(1.0f);
                            imvStatusHiki.setAlpha(0.0f);
                            playSound("nagare");
                            releaseBtn();
                            break;
                        case '2': // 引き
                            imvStatusStart.setAlpha(0.0f);
                            imvStatusSellOut.setAlpha(0.0f);
                            imvStatusSucessfullBit.setAlpha(0.0f);
                            imvStatusDecide.setAlpha(0.0f);
                            imvStatusHoRyuu.setAlpha(0.0f);
                            imvStatusHiki.setAlpha(1.0f);
                            playSound("nagare");
                            releaseBtn();
                            break;
                        case '5': // 決定
                            imvStatusStart.setAlpha(0.0f);
                            imvStatusSellOut.setAlpha(0.0f);
                            imvStatusSucessfullBit.setAlpha(0.0f);
                            imvStatusDecide.setAlpha(1.0f);
                            imvStatusHoRyuu.setAlpha(0.0f);
                            imvStatusHiki.setAlpha(0.0f);
                            playSound("end");
                            releaseBtn();
                            break;
                        case '3':
                        case '4':
                            // 落札
                            if (auctionInfo.getLastMemberNo() == Integer.parseInt(place.getMemberNo())) {
                                imvStatusStart.setAlpha(0.0f);
                                imvStatusSellOut.setAlpha(0.0f);
                                imvStatusSucessfullBit.setAlpha(1.0f);
                                imvStatusDecide.setAlpha(0.0f);
                                imvStatusHoRyuu.setAlpha(0.0f);
                                imvStatusHiki.setAlpha(0.0f);
                                //setMarginLeft(imvStatusTopBit,82);
                                playSound("success");
                                showSuccessfullNotification();
                                releaseBtn();

                            }
                            // 決定
                            else {
                                imvStatusStart.setAlpha(0.0f);
                                imvStatusSellOut.setAlpha(0.0f);
                                imvStatusSucessfullBit.setAlpha(0.0f);
                                imvStatusDecide.setAlpha(1.0f);
                                imvStatusHoRyuu.setAlpha(0.0f);
                                imvStatusHiki.setAlpha(0.0f);
                                playSound("end");
                                releaseBtn();
                            }
                            break;
                        default:
                            break;
                    }


                    // IN SERI
                    if (auctionInfo.getInSeri() == '1' && auctionInfo.getBidCountdown() == 0) {
                        playSound("start");
                    } else if (auctionInfo.getInSeri() == '2') {
//                        if(!onetimeVoice){
//                            playSound("urikiri");
//                            onetimeVoice = true;
//                        }
//                        else
                        if(auctionInfo.getBidCountdown() == 0){
                            playSound("urikiri");
                        }
                    }


                    // 10 リスト訂正 set 2nd state
                    // 訂正データの場合には、車画像の周りに赤枠を表示する。
                    if (auctionInfo.getFlagCorrection() == '1') {
                        imvStatusCorrect.setAlpha(1.0f);

                        //drawImageBorder(true);
                        //lblCorrectDetail.setText(lane.getCurrentItem().getCorrectionDetail());
                    } else {
                        imvStatusCorrect.setAlpha(0.0f);
                        ;
                        //drawImageBorder(false);
                    }

                    bidDekiru = auctionInfo.getSeri() == '0' && auctionInfo.getFlagStart() == '1';
                    updateMode(auctionInfo.getSeri() == '0' && auctionInfo.getFlagStart() == '1');


                    lane.setOpen(true);

                    break;
                case '1':// １：オークション終了（現在のレーン）
                    if (lane.isOpen()) {
                        lane.setOpen(false);
                        updateMode(false);
                        //MessageBox.show(getActivityContext(), R.string.auction_end_information, R.string.auction_current_lane_close);
                        MessageBox.show(getActivityContext(), R.string.auction_end_information, R.string.auction_current_lane_close, new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                closeActivity();
                            }

                        });
                    }
                    break;

                case '2':// ２：オークション終了（全レーン）
                    if (!isAllLaneClosed) {
                        isAllLaneClosed = true;
                        socketsManagement.stop();
                        RealLuceAuctionApplication.allPlaces.get(0).setAuctionEndFlg("1");
                        MessageBox.show(getActivityContext(), R.string.auction_end_information, R.string.auction_all_lane_close, new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                closeActivity();
                            }

                        });
                    }
                    break;
            }

        }

        public void setMarginLeft(TextView v, int left) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(55, 25);
            lp.setMargins(left, 0, 5, 7);
            v.setLayoutParams(lp);
        }
    }

    private void startTimerThread() {
        stopRun = false;
        startTime = System.currentTimeMillis();
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        // 10 msec order
        int period = 10;

        while (!stopRun && targetTime > 0) {
            // sleep: period msec
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                e.printStackTrace();
                stopRun = true;
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    long endTime = System.currentTimeMillis();
                    // カウント時間 = 経過時間 - 開始時間
                    long diffTime = (endTime - startTime);
                    // 表示時間 = ターゲット時間 - カウント時間
                    long setTime = Math.max(targetTime - diffTime, 0);
                    if (setTime == 0) {
                        successfullBitDialog.dismiss();
                        stopRun = true;
                    }
                }
            });
        }
    }
}