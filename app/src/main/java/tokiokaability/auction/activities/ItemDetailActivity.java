package tokiokaability.auction.activities;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import tokiokaability.auction.HTTPInfo.InquiryInfo;
import tokiokaability.auction.HTTPInfo.LoginInfo;
import tokiokaability.auction.HTTPInfo.PlaceInfo;
import tokiokaability.auction.R;
import tokiokaability.auction.RealLuceAuctionApplication;
import tokiokaability.auction.HTTPInfo.BaseInfo;
import tokiokaability.auction.HTTPInfo.ItemInfo;
import tokiokaability.auction.HTTPInfo.HTTPService;
import tokiokaability.auction.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction.commons.CustomImageAdapter;
import tokiokaability.auction.commons.MessageBox;
import tokiokaability.auction.commons.Utilities;
import tokiokaability.auction.dialog.CameraDialog;
import tokiokaability.auction.dialog.CorrectionDialog;
import tokiokaability.auction.dialog.ImageViewDialog;
import tokiokaability.auction.view.ViewPagerCustomDuration;

import android.Manifest;
import android.app.AlertDialog;
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
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;

import com.viewpagerindicator.CirclePageIndicator;

/**
 * Show the information of car
 *
 * @author HungTQ
 * @version 0.10
 * @since 2013.02.01
 */
public class ItemDetailActivity extends BaseActivity {

    static int CON_LARGE_WIDTH = 400;
    static int CON_LARGE_HEIGH = 300;

    // Declare variables
    private ImageButton btnPrevious;
    private ImageButton btnNext;
    private TextView lblScreenTitle;
    private ListView lsDetailInfor;


    private TextView lblSellNo2;


    private RelativeLayout tabDetailInfor;


    private int searchMode = 0;
    private int displayIndex = 0;
    private int showingTab = 0;
    private int displayFlg = 0;


    private ImageViewDialog imageViewDialog;
    private CameraDialog cameraDialog;
    private ArrayList<CustomImageAdapter.ImageModel> imgArr;
    TextView lblTradeStatus;
    ImageButton btnTradeStatus;
    ItemInfo currentItem = null;
    private DetailInforAdapter detailInforAdapter;
    AssetManager am;
    Typeface yGB;
    Typeface yGR;
    ViewPagerCustomDuration mPager;
    CirclePageIndicator indicator;
    CustomImageAdapter imgAdapter;
    ImageView img1, img2, img3, img4, img5, img6,img7,img8,img9,img10;
    ImageButton okiniriBtn, btnBid;
    Button correctionBtn;
    CorrectionDialog correctionDialog;
    ScrollView infoScroll;
    private PlaceInfo place;
    HorizontalScrollView imgList;
    RelativeLayout imgArea;
    float density, dpWidth, dpHeight;
    GestureDetector tapGestureDetector;
    ArrayList<String> itemList, placeList, sessionList;

    EditText txtValue = null;
    String db_memo;
    // ************************ SCREEN LIFECYCLE *************************

    /**
     * Initialization
     */
    @Override
    protected void initialization() {
        setContentView(R.layout.activity_item_detail);

        super.initialization();

        place = RealLuceAuctionApplication.allPlaces.get(0);

        // Get search mode.
        searchMode = (Integer) getActivityParam("SearchMode", 0);
        displayIndex = (Integer) getActivityParam("DisplayIndex", 0);
        String strItem = (String) getActivityParam("ItemList", "");
        String strPlace = (String) getActivityParam("PlaceList", "");
        String strSession = (String) getActivityParam("SessionList", "");
        itemList = new ArrayList<>(Arrays.asList((strItem).split(",")));
        placeList = new ArrayList<>(Arrays.asList((strPlace).split(",")));
        sessionList = new ArrayList<>(Arrays.asList((strSession).split(",")));
        btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        lblScreenTitle = (TextView) findViewById(R.id.lblScreenTitle);
        lsDetailInfor = (ListView) findViewById(R.id.lsDetailInfor);
        lblTradeStatus = findViewById(R.id.lblStatus);
        btnTradeStatus = findViewById(R.id.btnTradeStatus);

        lblSellNo2 = (TextView) findViewById(R.id.lblSellNo2);
        tabDetailInfor = (RelativeLayout) findViewById(R.id.tabDetailInfor);
        mPager = findViewById(R.id.detailImg);
        indicator = findViewById(R.id.indicator);
        indicator.setCentered(true);
        indicator.setFillColor(Color.parseColor("#024AA6"));
        indicator.setSnap(false);
        indicator.setRadius(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
        infoScroll = findViewById(R.id.infoScroll);
        imgArea = findViewById(R.id.imgArea);
        ViewTreeObserver viewTreeObserver = imgArea.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Display display = getWindowManager().getDefaultDisplay();
                DisplayMetrics outMetrics = new DisplayMetrics();
                display.getMetrics(outMetrics);

                density = getResources().getDisplayMetrics().density;
                dpWidth = outMetrics.widthPixels;
                dpHeight = outMetrics.heightPixels;
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) indicator.getLayoutParams();
                lp.height = (int) (dpWidth - 20 * density);
                indicator.setLayoutParams(lp);
                indicator.requestLayout();
                indicator.setPadding(0, (int) (dpWidth - 30 * density), 0, 0);
                indicator.requestLayout();
                RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) mPager.getLayoutParams();
                lp2.height = (int) (dpWidth - 20 * density);
                mPager.setLayoutParams(lp);
                mPager.requestLayout();
            }
        });


        txtValue = (EditText) findViewById(R.id.txtValue);

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);
        img6 = findViewById(R.id.img6);
        img7 = findViewById(R.id.img7);
        img8 = findViewById(R.id.img8);
        img9 = findViewById(R.id.img9);
        img10 = findViewById(R.id.img10);

        img1.setOnClickListener(smallImgClick);
        img2.setOnClickListener(smallImgClick);
        img3.setOnClickListener(smallImgClick);
        img4.setOnClickListener(smallImgClick);
        img5.setOnClickListener(smallImgClick);
        img6.setOnClickListener(smallImgClick);
        img7.setOnClickListener(smallImgClick);
        img8.setOnClickListener(smallImgClick);
        img9.setOnClickListener(smallImgClick);
        img10.setOnClickListener(smallImgClick);

        btnBid = findViewById(R.id.btnBit);
        btnBid2 = findViewById(R.id.btnBit2);
        btnBid.setTag("1");
        btnBid2.setTag("2");
        //btnBid.setOnClickListener(btnBitClick);
        okiniriBtn = findViewById(R.id.okiniriBtn);
        okiniriBtn.setOnClickListener(okiniriBtnClick);
        // Register event for Next, previous button.
        btnNext.setOnClickListener(btnNexClick);
        btnPrevious.setOnClickListener(btnPreviousClick);
        btnNext.setVisibility(ImageButton.INVISIBLE);
        btnPrevious.setVisibility(ImageButton.INVISIBLE);
        lblScreenTitle.setText(R.string.item_detail_screen_title);

        imgList = findViewById(R.id.imgLst);
        imgArr = new ArrayList<>();
        detailInforAdapter = new DetailInforAdapter(this);
        lsDetailInfor.setAdapter(detailInforAdapter);
        correctionBtn = findViewById(R.id.correctionBtn);
        correctionBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                correctionDialog.show(currentItem.getBoxNo()+"-"+currentItem.getBoxSubNo(),currentItem.getCorrection());
            }
        });
        imageViewDialog = new ImageViewDialog(this, new OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewDialog.dismiss();
            }
        });
        correctionDialog = new CorrectionDialog(this, new OnClickListener() {
            @Override
            public void onClick(View v) {
                correctionDialog.dismiss();
            }
        });

        cameraDialog = new CameraDialog(this, new CameraDialog.PictureCallback() {
            @Override
            public void onPictureReady(int imagePosition, byte[] data) {

            }
        });
        am = getApplicationContext().getAssets();
        yGB = Typeface.createFromAsset(am, "fonts/YUGOTHB.TTC");
        yGR = Typeface.createFromAsset(am, "fonts/YUGOTHR.TTC");
        this.setHelpResourceId(R.layout.detail_help);
        this.getItemDetail(displayIndex); // Set Data on activity
        //btnCaptureAllImage.setOnClickListener(btnCaptureAllImageClick);
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) setFont();
        tapGestureDetector = new GestureDetector(this, new TapGestureListener());
        indicator.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //v.getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
                tapGestureDetector.onTouchEvent(event);
                return true;
            }
        });
        lsDetailInfor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onTouchEvent(event);
                return true;
            }
        });
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("mPager", "Page Scrolled");
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("mPager", "Page Selected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                imgList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (mPager.getCurrentItem()) {
                            case 0:
                                imgList.smoothScrollTo((int) img1.getX(), 0);
                                break;
                            case 1:
                                imgList.smoothScrollTo((int) img2.getX(), 0);
                                break;
                            case 2:
                                imgList.smoothScrollTo((int) img3.getX(), 0);
                                break;
                            case 3:
                                imgList.smoothScrollTo((int) img4.getX(), 0);
                                break;
                            case 4:
                                imgList.smoothScrollTo((int) img5.getX(), 0);
                                break;
                            case 5:
                                imgList.smoothScrollTo((int) img6.getX(), 0);
                                break;
                            case 6:
                                imgList.smoothScrollTo((int) img7.getX(), 0);
                                break;
                            case 7:
                                imgList.smoothScrollTo((int) img8.getX(), 0);
                            case 8:
                                imgList.smoothScrollTo((int) img9.getX(), 0);
                            case 9:
                                imgList.smoothScrollTo((int) img10.getX(), 0);
                                break;
                        }
                    }
                }, 100);
            }
        });

        validateButtons(false);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) lsDetailInfor.getLayoutParams();
        lp.height = (int) (getResources().getDimension(R.dimen._100sdp) * 8.5);
        lsDetailInfor.setLayoutParams(lp);
        lsDetailInfor.requestLayout();
//        infoScroll.smoothScrollTo(0, 0);
    }

    private View.OnClickListener btnBitClick = new View.OnClickListener() {
        public void onClick(View v) {
            btnBid.setEnabled(false);
            Utilities.writeLog(getActivityContext(), RealLuceAuctionApplication.getConfiguration("info_log_file"), "[TopSearchScreen] RealAuction Button Pressed");
            PlaceInfo p = RealLuceAuctionApplication.allPlaces.get(0);

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
                objService.login(RealLuceAuctionApplication.loginID, "", "02-00-00-00-00-00", "2", RealLuceAuctionApplication.publicIPAddress, "", "1",
                        RealLuceAuctionApplication.countryCode);
            }

            checkLoginAndShowAuction();
        }
    };

    private void checkLoginAndShowAuction() {

        HTTPService objServiceCheckLogin = new HTTPService(new HTTPServiceCallback() {
            @Override
            public void onSucessfully(Object returnData) {
                LoginInfo loginInfo = (LoginInfo) returnData;

                if (loginInfo.getResult().equalsIgnoreCase("1")) {
                    setActivityParam("Place", RealLuceAuctionApplication.allPlaces.get(0));

                    setActivityParam("From", "TopSearch");
                    setTransitAnimationType(TRANSIT_ANIMATION_FULL_LEFT);
                    showActivity(AuctionActivity.class);

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

    @Override
    protected void onResume() {
        super.onResume();
        PlaceInfo p = RealLuceAuctionApplication.allPlaces.get(0);
        if (p.getRealFlag().equals("1")) {
            btnBid.setVisibility(View.VISIBLE);
            btnBid2.setVisibility(View.VISIBLE);
        } else {
            btnBid.setVisibility(View.GONE);
            btnBid2.setVisibility(View.GONE);
        }
    }

    private void setFont() {
        lblScreenTitle.setTypeface(yGB);
        lblSellNo2.setTypeface(yGB);
        correctionBtn.setTypeface(yGB);
    }

    OnClickListener okiniriBtnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String delFlg = "";
            if (currentItem.getOkiniriFlg().equals("1")) {
                okiniriBtn.setImageResource(R.drawable.okiniri);
                currentItem.setOkiniriFlg("0");
                delFlg = "1";
                RealLuceAuctionApplication.currentItemOkiniri = "0";
            } else {
                okiniriBtn.setImageResource(R.drawable.okiniri_chk);
                currentItem.setOkiniriFlg("1");
                RealLuceAuctionApplication.currentItemOkiniri = "1";
                delFlg = "0";
            }
            HTTPService okiniriSrv = new HTTPService(new HTTPServiceCallback() {
                @Override
                public void onSucessfully(Object returnData) {
                    hideProgressDialog();
                    RealLuceAuctionApplication.okiniriOffsetLst.add(RealLuceAuctionApplication.detailOffset);
                    RealLuceAuctionApplication.okiniriLst.add(currentItem.getOkiniriFlg());
                }

                @Override
                public void onFailed(int errorCode) {
                    hideProgressDialog();
                }
            });
            showProgressDialog(R.string.please_waiting);
            okiniriSrv.updateOkiniri(currentItem.getPlaceId(), currentItem.getTimesNo(), String.valueOf(currentItem.getItemNo()), RealLuceAuctionApplication.loginID, delFlg);
        }
    };

    @Override
    public void onChildActivityClosed(BaseActivity fromActivity) {
        super.onChildActivityClosed(fromActivity);
        this.getItemDetail(displayIndex); // Set Data on activity
    }

    /**
     * 背景タップでキーボードを隠す
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        return false;
    }

    // ************************ BUSINESS LOGIC PROCESSING ****************


    View.OnClickListener smallImgClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(Integer.parseInt(v.getTag().toString()));
        }
    };

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
            imageFromByteArray(Utilities.rotaledByteArr(itemInfo.getImage1()), 1);
        } else {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(nophoto);
            imgArr.add(imgModel);
            imageFromByteArray(nophoto, 1);
        }
        if (itemInfo.getImage2() != null) {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(Utilities.rotaledByteArr(itemInfo.getImage2()));
            imgArr.add(imgModel);
            imageFromByteArray(Utilities.rotaledByteArr(itemInfo.getImage2()), 2);
        } else {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(nophoto);
            imgArr.add(imgModel);
            imageFromByteArray(nophoto, 2);
        }
        if (itemInfo.getImage3() != null) {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(Utilities.rotaledByteArr(itemInfo.getImage3()));
            imgArr.add(imgModel);
            imageFromByteArray(Utilities.rotaledByteArr(itemInfo.getImage3()), 3);
        } else {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(nophoto);
            imgArr.add(imgModel);
            imageFromByteArray(nophoto, 3);
        }
        if (itemInfo.getImage4() != null) {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(Utilities.rotaledByteArr(itemInfo.getImage4()));
            imgArr.add(imgModel);
            imageFromByteArray(Utilities.rotaledByteArr(itemInfo.getImage4()), 4);
        } else {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(nophoto);
            imgArr.add(imgModel);
            imageFromByteArray(nophoto, 4);
        }
        if (itemInfo.getImage5() != null) {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(Utilities.rotaledByteArr(itemInfo.getImage5()));
            imgArr.add(imgModel);
            imageFromByteArray(Utilities.rotaledByteArr(itemInfo.getImage5()), 5);
        } else {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(nophoto);
            imgArr.add(imgModel);
            imageFromByteArray(nophoto, 5);
        }
        if (itemInfo.getImage6() != null) {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(Utilities.rotaledByteArr(itemInfo.getImage6()));
            imgArr.add(imgModel);
            imageFromByteArray(Utilities.rotaledByteArr(itemInfo.getImage6()), 6);
        } else {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(nophoto);
            imgArr.add(imgModel);
            imageFromByteArray(nophoto, 6);
        }
        if (itemInfo.getImage7() != null) {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(Utilities.rotaledByteArr(itemInfo.getImage7()));
            imgArr.add(imgModel);
            imageFromByteArray(Utilities.rotaledByteArr(itemInfo.getImage7()), 7);
        } else {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(nophoto);
            imgArr.add(imgModel);
            imageFromByteArray(nophoto, 7);
        }
        if (itemInfo.getImage8() != null) {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(Utilities.rotaledByteArr(itemInfo.getImage8()));
            imgArr.add(imgModel);
            imageFromByteArray(Utilities.rotaledByteArr(itemInfo.getImage8()), 8);
        } else {
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(nophoto);
            imgArr.add(imgModel);
            imageFromByteArray(nophoto, 8);
        }

        if (itemInfo.getImage9() != null) {
            img9.setVisibility(View.VISIBLE);
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(Utilities.rotaledByteArr(itemInfo.getImage9()));
            imgArr.add(imgModel);
            imageFromByteArray(Utilities.rotaledByteArr(itemInfo.getImage9()), 9);
        } else if(!itemInfo.getCornerID().equals("5")){
            img9.setVisibility(View.VISIBLE);
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(nophoto);
            imgArr.add(imgModel);
            imageFromByteArray(nophoto, 9);
        }
        else{
            img9.setVisibility(View.GONE);
        }
        if (itemInfo.getImage10() != null) {
            img10.setVisibility(View.VISIBLE);
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(Utilities.rotaledByteArr(itemInfo.getImage10()));
            imgArr.add(imgModel);
            imageFromByteArray(Utilities.rotaledByteArr(itemInfo.getImage10()), 10);
        } else if(!itemInfo.getCornerID().equals("5")){
            img10.setVisibility(View.VISIBLE);
            CustomImageAdapter.ImageModel imgModel = new CustomImageAdapter.ImageModel();
            imgModel.setByteArr(nophoto);
            imgArr.add(imgModel);
            imageFromByteArray(nophoto, 10);
        }
        else{
            img10.setVisibility(View.GONE);
        }

        if (imgAdapter == null) {
            imgAdapter = new CustomImageAdapter(getActivityContext(), imgArr);

            mPager.setAdapter(imgAdapter);
            mPager.setScrollable(false);
            indicator.setViewPager(mPager);
        } else {
            imgAdapter.setImageModelArrayList(imgArr);
            imgAdapter.notifyDataSetChanged();
        }
        mPager.setCurrentItem(0);

    }

    private void validateButtons(boolean isEnabled) {
        if (itemList == null) {
            btnNext.setEnabled(false);
            btnPrevious.setEnabled(false);

            return;
        }

        if (displayIndex <= 0)
            btnPrevious.setVisibility(View.INVISIBLE);
        else
            btnPrevious.setVisibility(View.VISIBLE);

        if (displayIndex >= itemList.size() - 1)
            btnNext.setVisibility(View.INVISIBLE);
        else
            btnNext.setVisibility(View.VISIBLE);


        if (isEnabled == false) {
            btnNext.setEnabled(false);
            btnPrevious.setEnabled(false);

            //btnCaptureAllImage.setEnabled(false);
        } else {
            btnNext.setEnabled(true);
            btnPrevious.setEnabled(true);

            //btnCaptureAllImage.setEnabled(true);

        }

    }

    void imageFromByteArray(byte[] byteArray, int position) {
        switch (position) {
            case 1:
                img1.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                break;
            case 2:
                img2.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                break;
            case 3:
                img3.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                break;
            case 4:
                img4.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                break;
            case 5:
                img5.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                break;
            case 6:
                img6.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                break;
            case 7:
                img7.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                break;
            case 8:
                img8.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
            case 9:
                img9.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
            case 10:
                img10.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                break;
        }


    }

    /**
     * 箱番と枝番を"箱番-枝番"という形で出力
     */
    private String getBoxAndSub(ItemInfo item) {

        return item.getBoxNo() + "-" + item.getBoxSubNo();
    }

    /**
     * Load list car info from server
     */
    private void getItemDetail(final int index) {

        // Get selected car
        if (index < 0 || index >= itemList.size())
            return;

        validateButtons(false);
        //final ItemInfo currItem = itemList.get(index);
        lsDetailInfor.setAdapter(null);
		/*
		lblSellNo.setText(currItem.getItemNo() + ":"+currItem.getSellName());
		detailInforAdapter.setItemInfor(null);
		detailInforAdapter.notifyDataSetChanged();
		blockCorrectionDetail.setVisibility(View.GONE);
*/
        HTTPService objService = new HTTPService(new HTTPServiceCallback() {

            @Override
            public void onSucessfully(Object returnData) {
                currentItem = (ItemInfo) returnData; // Get car information
                getInquiryInfo(displayIndex);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) lsDetailInfor.getLayoutParams();
                lp.height = (int) (getResources().getDimension(R.dimen._100sdp) * (currentItem.getCornerID().equals("5")?8.2:8.5));
                lsDetailInfor.setLayoutParams(lp);
                lsDetailInfor.requestLayout();
                lblSellNo2.setText(currentItem.getBoxNo() + "-" + currentItem.getBoxSubNo() + ":" + currentItem.getBrand());
                displayIndex = index;
                if (currentItem.getTimesNo().equals(RealLuceAuctionApplication.systemInfo.getSessionList().get(0))) {
                    okiniriBtn.setVisibility(View.VISIBLE);
                    if (currentItem.getOkiniriFlg().equals("1")) {
                        okiniriBtn.setImageResource(R.drawable.okiniri_chk);
                    } else {
                        okiniriBtn.setImageResource(R.drawable.okiniri);
                    }
                } else {
                    okiniriBtn.setVisibility(View.GONE);
                }
                if(currentItem.getCorrection().length() > 0){
                    correctionBtn.setVisibility(View.VISIBLE);
                }
                else{
                    correctionBtn.setVisibility(View.GONE);
                }
                loadViewPager(currentItem);
                hideProgressDialog(); // Hide process dialog
                validateButtons(true);
                infoScroll.smoothScrollTo(0, 0);
                if(currentItem.getSellingResultBK().equals("1")){
                    if(currentItem.getSellingResult().equals("1")){
                        String holdPrice = currentItem.getHoldPrice();
                        String reholdPrice = currentItem.getReholdPrice();
                        if(currentItem.getJishaFlg().equals("1")){
                            if(holdPrice == null || holdPrice.equals("") || holdPrice.equals("0")){
                                btnTradeStatus.setImageDrawable(getActivityContext().getResources().getDrawable(R.drawable.item_koshosuru_button_style));
                                btnTradeStatus.setVisibility(View.VISIBLE);
                                lblTradeStatus.setVisibility(View.GONE);
                            }
                            else{
                                if(reholdPrice == null || reholdPrice.equals("") || reholdPrice.equals("0")){
                                    lblTradeStatus.setTextColor(Color.parseColor("#0066ff"));
                                    lblTradeStatus.setBackground(getResources().getDrawable(R.drawable.title_koshoblue));
                                    lblTradeStatus.setText(getString(R.string.list_Horyu_kosho_chu));
                                    lblTradeStatus.setVisibility(View.VISIBLE);
                                    btnTradeStatus.setVisibility(View.GONE);
                                }
                                else{
                                    btnTradeStatus.setImageDrawable(getActivityContext().getResources().getDrawable(R.drawable.item_saikosho_button_style));
                                    btnTradeStatus.setVisibility(View.VISIBLE);
                                    lblTradeStatus.setVisibility(View.GONE);
                                }

                            }
                        }
                        else{
                            if(holdPrice == null || holdPrice.equals("") || holdPrice.equals("0")){
                                lblTradeStatus.setTextColor(Color.parseColor("#0066ff"));
                                lblTradeStatus.setBackground(getResources().getDrawable(R.drawable.title_koshoblue));
                                lblTradeStatus.setText(getString(R.string.list_Horyu_kosho_mae));
                                lblTradeStatus.setVisibility(View.VISIBLE);
                                btnTradeStatus.setVisibility(View.GONE);
                            }
                            else{
                                if(reholdPrice == null || reholdPrice.equals("") || reholdPrice.equals("0")){
                                    btnTradeStatus.setImageDrawable(getActivityContext().getResources().getDrawable((R.drawable.item_koshoari_button_style)));
                                    btnTradeStatus.setVisibility(View.VISIBLE);
                                    lblTradeStatus.setVisibility(View.GONE);
                                }
                                else{
                                    btnTradeStatus.setVisibility(View.GONE);
                                    lblTradeStatus.setVisibility(View.VISIBLE);
                                    lblTradeStatus.setTextColor(Color.parseColor("#ff0000"));
                                    lblTradeStatus.setBackground(getResources().getDrawable(R.drawable.title_koshored));
                                    lblTradeStatus.setText(getString(R.string.list_saikoshochu));
                                }
                            }
                        }
                    }
                    else if(currentItem.getSellingResult().equals("2")){
                        btnTradeStatus.setVisibility(View.GONE);
                        lblTradeStatus.setVisibility(View.VISIBLE);
                        lblTradeStatus.setTextColor(Color.parseColor("#ff6600"));
                        lblTradeStatus.setBackground(getResources().getDrawable(R.drawable.title_koshoorange));
                        lblTradeStatus.setText(getString(R.string.list_Horyu_kosho_hiki));
                    }
                    else if(currentItem.getSellingResult().equals("6")){
                        btnTradeStatus.setVisibility(View.GONE);
                        lblTradeStatus.setVisibility(View.VISIBLE);
                        lblTradeStatus.setTextColor(Color.parseColor("#ff0000"));
                        lblTradeStatus.setBackground(getResources().getDrawable(R.drawable.title_koshored));
                        lblTradeStatus.setText(getString(R.string.list_Horyu_kosho_seiritsu));
                    }
                }
                else{
                    btnTradeStatus.setVisibility(View.GONE);
                    lblTradeStatus.setVisibility(View.GONE);
                }
                if(!(searchMode == 2 || searchMode == 3)){
                    btnTradeStatus.setVisibility(View.GONE);
                    lblTradeStatus.setVisibility(View.GONE);
                }
                btnTradeStatus.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setActivityParam("SearchMode", searchMode);
                        setActivityParam("ItemInfo", currentItem);
                        // Call 保留交渉画面.
                        setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
                        showActivity(RegisterNegotiationActivity.class);
                    }
                });
            }

            @Override
            public void onFailed(int errorCode) {
                // Hide process dialog.
                hideProgressDialog();
                validateButtons(true);
                // Show error content
                catchSystemErrors(errorCode);
            }
        });

        showProgressDialog(R.string.please_waiting);

        objService.getItemDetails(placeList.get(index), sessionList.get(index),
                itemList.get(index),
                RealLuceAuctionApplication.loginID, String.valueOf(searchMode));
    }

    /**
     * Load inquiry info from server
     */
    private void getInquiryInfo(final int index) {

        // Get selected car
        if (index < 0 || index >= itemList.size())
            return;

        HTTPService objService = new HTTPService(new HTTPServiceCallback() {

            @Override
            public void onSucessfully(Object returnData) {
                InquiryInfo inquiryInfo = (InquiryInfo) returnData; // Get Inquiry information

                try{
                    displayFlg = Integer.parseInt(inquiryInfo.getDisplayFlg());
                }catch (Exception ignored){

                }

                detailInforAdapter.setItemInfor(currentItem);
                detailInforAdapter.notifyDataSetChanged();
                lsDetailInfor.setAdapter(detailInforAdapter);
            }

            @Override
            public void onFailed(int errorCode) {
                // Hide process dialog.
                hideProgressDialog();
                validateButtons(true);
                // Show error content
                catchSystemErrors(errorCode);
            }
        });

        showProgressDialog(R.string.please_waiting);
        String boxNo = currentItem.getBoxNo();
        String subNo = null;
        try {
            // 2桁へ変換
            subNo = String.format("%02d", Integer.parseInt(currentItem.getBoxSubNo()));
        } catch (Exception ignored) {
        }
        objService.getInquiry(RealLuceAuctionApplication.loginID, placeList.get(index), sessionList.get(index), boxNo + subNo);
    }


    private void catchSystemErrors(int errorCode) {

        switch (errorCode) {
            case HTTPService.ERROR_NETWORK:
                MessageBox.show(getActivityContext(),
                        R.string.error_title_webservice,
                        R.string.error_message_xml_connect_gateway);
                break;
            case HTTPService.ERROR_XMLPARSER:
                MessageBox.show(getActivityContext(),
                        R.string.error_title_webservice,
                        R.string.error_message_xml_parser);
                break;
            case HTTPService.ERROR_RETURN_DATA_INVALID:
                MessageBox.show(getActivityContext(),
                        R.string.error_title_webservice,
                        R.string.error_message_return_data_invalid);
                break;
            case HTTPService.ERROR_SERVER_ERROR:
                MessageBox.show(getActivityContext(),
                        R.string.error_title_webservice,
                        R.string.error_message_server_error);
                break;
            case HTTPService.ERROR_CANCEL:
                break;
            default:
                MessageBox.show(getActivityContext(),
                        R.string.error_title_webservice,
                        R.string.error_message_unknow);
                break;
        }
    }


    /**
     * Create confirm message.
     * メッセージ用メソッド
     */
    private void confirmMessage(int titleId, int messageId, String okText, String cancelText,
                                DialogInterface.OnClickListener okClick,
                                DialogInterface.OnClickListener cancelClick) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set value.
        builder.setMessage(messageId);
        builder.setTitle(titleId);
        builder.setCancelable(false);
        builder.setPositiveButton(okText, okClick);
        builder.setNegativeButton(cancelText, cancelClick);

        // Create dialog and show to screen.
        AlertDialog alert = builder.create();
        alert.show();
        // Must call show() prior to fetching views
        TextView messageView = (TextView)alert.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);

        TextView titleView = (TextView)alert.findViewById(this.getResources().getIdentifier("alertTitle", "id", "android"));
        if (titleView != null) {
            titleView.setGravity(Gravity.CENTER);
        }
    }


    /**
     * Submit
     * メモ登録
     */
    private void memoRagister() {
        HTTPService httpService = new HTTPService(new HTTPServiceCallback() {

            @Override
            public void onSucessfully(Object returnData) {

                BaseInfo resultInfo = (BaseInfo) returnData;

                // Hidden progress dialog.
                hideProgressDialog();

                if (resultInfo.getStatus() != 0) {

                    // 通信エラー
                    MessageBox
                            .show(getActivityContext(),
                                    R.string.memo_message_error_title,
                                    R.string.memo_message_error
                                    );
                } else {
                    // 登録完了
                    db_memo = txtValue.getText().toString().replace(",","，");
                    txtValue.setText(txtValue.getText().toString().replace(",","，"));
                    MessageBox
                            .show(getActivityContext(),
                                    R.string.memo_message_title,
                                    R.string.memo_message_complet
//                                    }
                    );
                }
            }

            @Override
            public void onFailed(int errorCode) {

                // Hidden progress dialog.
                hideProgressDialog();

                // Set enable controls register, unregister.
//                setEnableControls(true);

                switch (errorCode) {
                    case HTTPService.ERROR_NETWORK:
                        MessageBox.show(getActivityContext(),
                                R.string.error_title_webservice,
                                R.string.error_message_xml_connect_gateway);
                        break;
                    case HTTPService.ERROR_XMLPARSER:
                        MessageBox.show(getActivityContext(),
                                R.string.error_title_webservice,
                                R.string.error_message_xml_parser);
                        break;
                    case HTTPService.ERROR_RETURN_DATA_INVALID:
                        MessageBox.show(getActivityContext(),
                                R.string.error_title_webservice,
                                R.string.error_message_return_data_invalid);
                        break;
                    case HTTPService.ERROR_SERVER_ERROR:
                        MessageBox.show(getActivityContext(),
                                R.string.error_title_webservice,
                                R.string.error_message_server_error);
                        break;
                    case HTTPService.ERROR_CANCEL:
                        break;
                    default:
                        MessageBox.show(getActivityContext(),
                                R.string.error_title_webservice,
                                R.string.error_message_unknow);
                        break;
                }
            }
        });

        // Show progress
        showProgressDialog(R.string.please_waiting);

        String sellNo = null;
        try {
            // 2桁へ変換
            sellNo = currentItem.getBoxNo() + String.format("%02d", Integer.parseInt(currentItem.getBoxSubNo()));
        } catch (Exception ignored) {

        }

        httpService.memoRagister(RealLuceAuctionApplication.loginID, currentItem.getPlaceId(), currentItem.getTimesNo(),sellNo, txtValue.getText().toString().replace(",","，"));
    }

    /**
     * メモ内容破棄の確認必要ボタン（列挙）
     * 戻る、次商品、前商品、事前入札、問い合わせ
     */
    public enum ScreenTransitionFlg{
        Back_btn,
        Next_btn,
        Preview_btn,
        Absence_btn,
        Contact_btn,
    }

    public static ScreenTransitionFlg ScreenTransitionFlg;

    /**
     * メモの編集内容破棄確認用メソッド
      */
    private void backProcessing() {
        if (txtValue == null) {
            return;
        }

        if (txtValue.getText().toString().equals(db_memo)) {
            ScreenTransition();
            return;
        }

        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(txtValue.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);


        confirmMessage(R.string.memo_message_title,
                R.string.contact_message_confirm_back,
                getString(R.string.memo_ok),
                getString(R.string.memo_cancel),
                screenTransitionokButtonOfCancelClick, cancelClick);
    }

    private void back() {
        super.onBackPressed();
    }
    
    private void ScreenTransition() {

        switch (ScreenTransitionFlg){
            case Next_btn:
                // If current item is less than the last item,
                if (displayIndex < itemList.size() - 1) {
                    getItemDetail(displayIndex + 1);
                    RealLuceAuctionApplication.detailOffset++;
                    return;
                }
                break;
            case Preview_btn:
                // If current item is greater than the first item,
                if (displayIndex > 0) {
                    getItemDetail(displayIndex - 1);
                    RealLuceAuctionApplication.detailOffset--;
                    return;
                }
                break;
            case Absence_btn:
                // Set itemList into activity parameter.
                setActivityParam("ItemInfo", currentItem);
                setActivityParam("DisplayIndex", displayIndex);

                // Call 不在入札登録画面.
                setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
                showActivity(RegisterAbsenceBidActivity.class);
                break;
            case Contact_btn:
                // Set itemList into activity parameter.
                setActivityParam("boxNo", currentItem.getBoxNo());
                setActivityParam("boxSubNo", currentItem.getBoxSubNo());
                setActivityParam("placeId", placeList.get(displayIndex));
                setActivityParam("sessionNo", sessionList.get(displayIndex));

                // Call 問い合わせ画面.
                setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
                showActivity(ContactActivity.class);
                break;
            default:
                back();
        }

    }


    // ************************ LISTENERS ********************************

    /**
     * Tap on the Next Car button
     */
    public OnClickListener btnNexClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            ScreenTransitionFlg = ItemDetailActivity.ScreenTransitionFlg.Next_btn;
            backProcessing();

        }
    };

    /**
     * Tap on the Preview Car button
     */
    public OnClickListener btnPreviousClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            ScreenTransitionFlg = ItemDetailActivity.ScreenTransitionFlg.Preview_btn;
            backProcessing();
        }
    };

    /**
     * Tap on the Absence Price button
     */
    public OnClickListener btnAbsencePriceClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            ScreenTransitionFlg = ItemDetailActivity.ScreenTransitionFlg.Absence_btn;
            backProcessing();
        }
    };

    /**
     * Tap on the Contact button
     */
    public OnClickListener btnContactClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            ScreenTransitionFlg = ItemDetailActivity.ScreenTransitionFlg.Contact_btn;
            backProcessing();
        }
    };


    /**
     * Tap on the MemoRagister button
     * メモの登録ボタンクリックイベント（編集中）
     */
    public OnClickListener btnMemoRagisterClick = new OnClickListener() {

        // メモの登録ボタン用
        @Override
        public void onClick(View v) {

            String txt_memo = txtValue.getText().toString();
            String dbMemo = db_memo;

            if (txtValue == null) {
                return;
            }
            if (txt_memo.equals(dbMemo)){
                return;
            }
            else{

                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(txtValue.getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);

                // Display confirm messagebox.
                // Show confirm dialog
                // 確認メッセージ
                confirmMessage(R.string.memo_message_title,
                        R.string.memo_message_confirmat,
                        getString(R.string.memo_ok),
                        getString(R.string.memo_cancel),
                        okButtonOfRegisterMemoClick, cancelClick);
            }

        }
    };

    /**
     * OKボタン（メモ登録）
     */
    public DialogInterface.OnClickListener okButtonOfRegisterMemoClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            memoRagister();
        }
    };

    /**
     * キャンセルボタン（ダイアログを閉じる）
     */
    public DialogInterface.OnClickListener cancelClick = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    public OnClickListener btnCaptureAllImageClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            cameraDialog.show(CameraDialog.ShootingType.Continuous, 1, currentItem);
        }
    };

    /**
     * メモ内容破棄する時（確認メッセージのOKボタン）
     */
    public DialogInterface.OnClickListener screenTransitionokButtonOfCancelClick = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            ScreenTransition();
        }
    };



    /*************************** DETAIL INFOR ********************************************/


    public enum DetailItemType {
        SessionNo,
        AuctionDate,
        BoxNo,
        AbsenceState,
        ItemName,
        SeriResult,
        StartPrice,
        LastPrice,
        SeriTime,
        AskingPrice,
        AuctionResult,
        AbsenteePrice,
        Details,
        Brand,
        SellingSort,
        CornerNm,
        SubCat,
        Material,
        Line,
        Model1,
        Model2,
        SerialNo,
        RefPrice,
        Rank,
        Accessories,
        Bless,
        IndividualNo,
        Color,
        Size,
        Instrument,
        Comments,
        Contact ,
        Memo,
        QR,
        QR2,
        QR3,
        QR4,
        Size2,
        Size3,
        Size4
    }

    public class DetailItem {
        public DetailItemType rowType;
        public String label;
        public String[] values;
        public int innerRowCount;

        public DetailItem(DetailItemType type, int labelId, int innerRowCount) {
            rowType = type;
            label = getResources().getString(labelId);
            values = null;
            this.innerRowCount = innerRowCount;
        }
    }

    public final class DetailInforAdapter extends BaseAdapter {

        private Context mContext;
        private ItemInfo itemInfor;
        private ArrayList<DetailItem> rows;
        // 文字一覧の項目数
        private int itemCount;
        private boolean isDialog = false;

        public DetailInforAdapter(Context context) {
            mContext = context;
            //this.itemInfor = itemInfor;
            rows = new ArrayList<DetailItem>();
        }

        public void appendDialogType() {
            isDialog = true;
        }

        public void setItemInfor(ItemInfo item) {
            this.itemInfor = item;
            rows = new ArrayList<DetailItem>();
            if(item.getCornerID().equals("5")){
                rows.add(new DetailItem(DetailItemType.AbsenteePrice, R.string.hoseki_detail_1, 1));
                rows.add(new DetailItem(DetailItemType.StartPrice, R.string.hoseki_detail_2_1, 1));
                if(searchMode == 2){
                    rows.add(new DetailItem(DetailItemType.AskingPrice, R.string.hoseki_detail_2_2, 1));
                }
                rows.add(new DetailItem(DetailItemType.Brand, R.string.hoseki_detail_3, 1));
                rows.add(new DetailItem(DetailItemType.Model1, R.string.hoseki_detail_4, 1));
                rows.add(new DetailItem(DetailItemType.Model2, R.string.hoseki_detail_5, 1));
                rows.add(new DetailItem(DetailItemType.Comments, R.string.hoseki_detail_6, 1));
                rows.add(new DetailItem(DetailItemType.Material, R.string.hoseki_detail_7, 1));
                rows.add(new DetailItem(DetailItemType.IndividualNo, R.string.hoseki_detail_8, 1));
                rows.add(new DetailItem(DetailItemType.Line, R.string.hoseki_detail_9, 1));
                rows.add(new DetailItem(DetailItemType.QR, R.string.hoseki_detail_10, 1));
                rows.add(new DetailItem(DetailItemType.QR2, R.string.hoseki_detail_11, 1));
//                rows.add(new DetailItem(DetailItemType.QR3, R.string.hoseki_detail_12, 1));
//                rows.add(new DetailItem(DetailItemType.QR4, R.string.hoseki_detail_13, 1));
                rows.add(new DetailItem(DetailItemType.Accessories, R.string.hoseki_detail_14, 1));
                rows.add(new DetailItem(DetailItemType.Details, R.string.hoseki_detail_15, 1));
                rows.add(new DetailItem(DetailItemType.Size, R.string.hoseki_detail_16, 1));
                rows.add(new DetailItem(DetailItemType.Size2, R.string.hoseki_detail_17, 1));
//                rows.add(new DetailItem(DetailItemType.Size3, R.string.hoseki_detail_18, 1));
//                rows.add(new DetailItem(DetailItemType.Size4, R.string.hoseki_detail_19, 1));
                rows.add(new DetailItem(DetailItemType.CornerNm, R.string.hoseki_detail_20, 1));
                rows.add(new DetailItem(DetailItemType.SubCat, R.string.hoseki_detail_21, 1));
                rows.add(new DetailItem(DetailItemType.BoxNo, R.string.hoseki_detail_22, 1));
                rows.add(new DetailItem(DetailItemType.SellingSort, R.string.hoseki_detail_23, 1));
                rows.add(new DetailItem(DetailItemType.SeriResult, R.string.hoseki_detail_24, 1));
                rows.add(new DetailItem(DetailItemType.Contact, R.string.hoseki_detail_25, 1));
                rows.add(new DetailItem(DetailItemType.Memo, R.string.hoseki_detail_26, 1));
            }
            else{
                rows.add(new DetailItem(DetailItemType.AbsenteePrice, R.string.detai_row_1, 1));
                rows.add(new DetailItem(DetailItemType.StartPrice, R.string.detai_row_21, 1));
                if(searchMode == 2){
                    rows.add(new DetailItem(DetailItemType.AskingPrice, R.string.detai_row_27, 1));
                }
                rows.add(new DetailItem(DetailItemType.Brand, R.string.detai_row_6, 1));
                rows.add(new DetailItem(DetailItemType.Model1, R.string.detai_row_9, 1));
                rows.add(new DetailItem(DetailItemType.Model2, R.string.detai_row_10, 1));
                if(item.getCornerID().equals("5"))
                    rows.add(new DetailItem(DetailItemType.Comments, R.string.detai_row_20, 1));
                rows.add(new DetailItem(DetailItemType.Line, R.string.detai_row_8, 1));
                rows.add(new DetailItem(DetailItemType.SerialNo, R.string.detai_row_11, 1));
                rows.add(new DetailItem(DetailItemType.Material, R.string.detai_row_7, 1));
                rows.add(new DetailItem(DetailItemType.Accessories, R.string.detai_row_14, 1));
                rows.add(new DetailItem(DetailItemType.IndividualNo, R.string.detai_row_16, 1));
                rows.add(new DetailItem(DetailItemType.Details, R.string.detai_row_2, 1));
                if(!item.getCornerID().equals("5"))
                    rows.add(new DetailItem(DetailItemType.Comments, R.string.detai_row_20, 1));
                rows.add(new DetailItem(DetailItemType.Instrument, R.string.detai_row_19, 1));
                rows.add(new DetailItem(DetailItemType.Color, R.string.detai_row_17, 1));
                rows.add(new DetailItem(DetailItemType.Size, R.string.detai_row_18, 1));
                rows.add(new DetailItem(DetailItemType.Rank, R.string.detai_row_13, 1));
                rows.add(new DetailItem(DetailItemType.CornerNm, R.string.detai_row_5, 1));
                rows.add(new DetailItem(DetailItemType.SubCat, R.string.detai_row_23, 1));
                rows.add(new DetailItem(DetailItemType.BoxNo, R.string.detai_row_3, 1));
                rows.add(new DetailItem(DetailItemType.SellingSort, R.string.detai_row_4, 1));
                rows.add(new DetailItem(DetailItemType.SeriResult, R.string.detai_row_22, 1));
                rows.add(new DetailItem(DetailItemType.Contact, R.string.detai_row_24, 1));
                rows.add(new DetailItem(DetailItemType.Memo, R.string.detai_row_25, 1));
            }

//			rows.add(new DetailItem(DetailItemType.RefPrice,R.string.detai_row_12,1));
//			rows.add(new DetailItem(DetailItemType.Bless,R.string.detai_row_15,1));


            itemCount = rows.size();
        }

        @Override
        public int getItemViewType(int position) {
            switch (rows.get(position).rowType) {
                case SessionNo:
                case AuctionDate:
                case BoxNo:
                case ItemName:
                case SeriResult:
                case StartPrice:

                case LastPrice:
                case SeriTime:

                case Details:

                case AskingPrice:
                case AuctionResult:
                case Brand:
                case SellingSort:
                case CornerNm:
                case SubCat:
                case Material:
                case Line:
                case Model1:
                case Model2:
                case SerialNo:
                case RefPrice:
                case Rank:
                case Accessories:
                case Bless:
                case IndividualNo:
                case Color:
                case Size:
                case Instrument:
                case QR:
                case QR2:
                case QR3:
                case QR4:
                case Size2:
                case Size3:
                case Size4:
                case Comments:
                    return 0;
                case AbsenteePrice:
                    return isDialog ? 0 : 1;
                case AbsenceState:
                    return 4;
                case Contact:
                    return 5;
                case Memo:
                    return 6;
                default:
                    return 0;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 6;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView lblLabel = null;
            TextView lblValue = null;


            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                switch (getItemViewType(position)) {
                    case 0:
                        convertView = inflater.inflate(R.layout.view_item_detail_rowinfo_simple, parent, false);
                        break;
                    case 1:
                        convertView = inflater.inflate(R.layout.view_item_detail_rowinfo_button, parent, false);
                        Button btn = (Button) convertView.findViewById(R.id.btnAbsencePrice);
                        btn.setOnClickListener(btnAbsencePriceClick);
                        break;
                    case 2:
                        convertView = inflater.inflate(R.layout.view_item_detail_rowinfo_selling_point1, parent, false);
                        break;
                    case 3:
                        convertView = inflater.inflate(R.layout.view_item_detail_rowinfo_selling_point2, parent, false);
                        break;
                    case 4:
                        convertView = inflater.inflate(R.layout.view_item_detail_rowinfo_special_report, parent, false);
                        break;
                    case 5:
                        convertView = inflater.inflate(R.layout.view_item_detail_rowinfo_button, parent, false);
                        btn = (Button) convertView.findViewById(R.id.btnAbsencePrice);
                        btn.setOnClickListener(btnContactClick);
                        break;
                    case 6:
                        convertView = inflater.inflate(R.layout.view_item_detail_rowinfo_memo_list, parent, false);
                        btn = (Button) convertView.findViewById(R.id.btnMemoRagister);
                        btn.setOnClickListener(btnMemoRagisterClick);
                        break;

                }
            }

            lblLabel = (TextView) convertView.findViewById(R.id.lblLabel);
            if (lblLabel != null) {
                lblLabel.setText(rows.get(position).label);
            }

            switch (rows.get(position).rowType) {
                case ItemName:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getItemName() != null)
                        lblValue.setText(itemInfor.getItemName());
                    else
                        lblValue.setText("");
                    break;

                case SeriResult:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getResultName() != null)
                        lblValue.setText(itemInfor.getResultName());
                    else
                        lblValue.setText("");
                    break;

                case StartPrice:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getStartPrice() != null)
                        lblValue.setText(Utilities.formatPrice(this.mContext, itemInfor.getStartPrice()));
                    else
                        lblValue.setText("- 円");
                    break;
                case AskingPrice:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getAskingPrice() != null)
                        lblValue.setText(Utilities.formatPrice(this.mContext, itemInfor.getAskingPrice()));
                    else
                        lblValue.setText("- 円");
                    break;
                case LastPrice:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getLastPrice() != null)
                        lblValue.setText(Utilities.formatPrice(mContext, itemInfor.getLastPrice()));
                    else
                        lblValue.setText("- 円");
                    break;
                case Contact:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    Button btn = (Button) convertView.findViewById(R.id.btnAbsencePrice);
                    btn.setText(getString(R.string.contact_btn_text));
                    if(displayFlg == 0){
                        lblValue.setText(R.string.contact_unregistered);
                    }
                    else if(displayFlg == 1){
                        lblValue.setText(R.string.contact_unanswered);
                    }
                    else if(displayFlg == 2){
                        lblValue.setText(R.string.contact_answered);
                    }
                    displayFlg = 0;

                    String currentSessionNoStr = RealLuceAuctionApplication.allPlaces.get(0).getTimesNo();
                    String currentItemSessionNoStr = currentItem.getTimesNo();
                    try{
                        btn.setEnabled(Integer.parseInt(currentSessionNoStr) <= Integer.parseInt(currentItemSessionNoStr)); // 質問ボタン
                    }catch (Exception ignored){
                    }
                    btn.setVisibility(View.VISIBLE);
                    break;
                case AbsenteePrice:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    btn = (Button) convertView.findViewById(R.id.btnAbsencePrice);
                    if (itemInfor != null) {

                        String tmp = itemInfor.getAbsencePrice();
                        if (itemInfor.getAbsenceFlag() == 2) {
                            lblValue.setText(getString(R.string.item_detail_none_nobid));
                        } else {
                            lblValue.setText((tmp == null || tmp.equals("") || tmp.equals("0")) ? getString(R.string.item_detail_none_string) : Utilities.formatPrice(mContext, tmp)); // 不在入札価格
                        }
                        btn.setEnabled(itemInfor.getResult().equals("0") && itemInfor.getAbsenceFlag() == 1); // 登録ボタン（不在入札）
                        btn.setVisibility(itemInfor.getAbsenceFlag() == 2 ? View.GONE : View.VISIBLE);
                    } else
                        lblValue.setText("");
                    break;


                case Details:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getDetails() != null) {
                        lblValue.setText(itemInfor.getDetails());
                    } else
                        lblValue.setText("");
                    break;
                case BoxNo:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getBoxNo() != null && itemInfor.getBoxSubNo() != null) {
                        lblValue.setText(itemInfor.getBoxNo() + "-" + itemInfor.getBoxSubNo());
                    } else
                        lblValue.setText("");
                    break;

                case SellingSort:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getSellingSort() != null) {
                        lblValue.setText(itemInfor.getSellingSort());
                    } else
                        lblValue.setText("");
                    break;
                case CornerNm:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getCornerNm() != null) {
                        lblValue.setText(itemInfor.getCornerNm());
                    } else
                        lblValue.setText("");
                    break;
                case SubCat:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getSubCat() != null) {
                        lblValue.setText(itemInfor.getSubCat());
                    } else
                        lblValue.setText("");
                    break;
                case Brand:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getBrand() != null) {
                        lblValue.setText(itemInfor.getBrand());
                    } else
                        lblValue.setText("");
                    break;
                case Material:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getMaterial() != null) {
                        lblValue.setText(itemInfor.getMaterial());
                    } else
                        lblValue.setText("");
                    break;
                case Line:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getLine() != null) {
                        lblValue.setText(itemInfor.getLine());
                    } else
                        lblValue.setText("");
                    break;
                case RefPrice:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getRefPrice() != null) {
                        lblValue.setText(Utilities.formatPrice(this.mContext, itemInfor.getRefPrice()));
                    } else
                        lblValue.setText("0 円");
                    break;
                case Model1:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getModel1() != null) {
                        lblValue.setText(itemInfor.getModel1());
                    } else
                        lblValue.setText("");
                    break;
                case Model2:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getModel2() != null) {
                        lblValue.setText(itemInfor.getModel2());
                    } else
                        lblValue.setText("");
                    break;
                case SerialNo:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    lblValue.setTextIsSelectable(true);
                    if (itemInfor != null && itemInfor.getSerialNo() != null) {
                        lblValue.setText(itemInfor.getSerialNo());
                    } else
                        lblValue.setText("");
                    break;
                case QR:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getQR() != null) {
                        lblValue.setText(itemInfor.getQR());
                    } else
                        lblValue.setText("");
                    break;
                case QR2:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getQR2() != null) {
                        lblValue.setText(itemInfor.getQR2());
                    } else
                        lblValue.setText("");
                    break;
                case QR3:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getQR3() != null) {
                        lblValue.setText(itemInfor.getQR3());
                    } else
                        lblValue.setText("");
                    break;
                case QR4:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getQR4() != null) {
                        lblValue.setText(itemInfor.getQR4());
                    } else
                        lblValue.setText("");
                    break;
                case Rank:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getRank() != null) {
                        lblValue.setText(itemInfor.getRank());
                    } else
                        lblValue.setText("");
                    break;
                case Accessories:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getAccessories() != null) {
                        lblValue.setText(itemInfor.getAccessories());
                    } else
                        lblValue.setText("");
                    break;
                case Bless:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getBless() != null) {
                        lblValue.setText(itemInfor.getBless());
                    } else
                        lblValue.setText("");
                    break;
                case IndividualNo:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getIndividualNo() != null) {
                        lblValue.setText(itemInfor.getIndividualNo());
                    } else
                        lblValue.setText("");
                    break;
                case Color:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getColor() != null) {
                        lblValue.setText(itemInfor.getColor());
                    } else
                        lblValue.setText("");
                    break;
                case Size:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getSize() != null) {
                        lblValue.setText(itemInfor.getSize());
                    } else
                        lblValue.setText("");
                    break;
                case Size2:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getSize2() != null) {
                        lblValue.setText(itemInfor.getSize2());
                    } else
                        lblValue.setText("");
                    break;
                case Size3:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getSize3() != null) {
                        lblValue.setText(itemInfor.getSize3());
                    } else
                        lblValue.setText("");
                    break;
                case Size4:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getSize4() != null) {
                        lblValue.setText(itemInfor.getSize4());
                    } else
                        lblValue.setText("");
                    break;
                case Instrument:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getInstrument() != null) {
                        lblValue.setText(itemInfor.getInstrument());
                    } else
                        lblValue.setText("");
                    break;
                case Comments:
                    lblValue = (TextView) convertView.findViewById(R.id.lblValue);
                    if (itemInfor != null && itemInfor.getComments() != null) {
                        lblValue.setText(itemInfor.getComments());
                    } else
                        lblValue.setText("");
                    break;
                case Memo:
                    btn = (Button) convertView.findViewById(R.id.btnMemoRagister);
                    txtValue = (EditText) convertView.findViewById(R.id.txtValue);

                    txtValue.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            infoScroll.requestDisallowInterceptTouchEvent(true);
                            tabDetailInfor.requestDisallowInterceptTouchEvent(true);
                            lsDetailInfor.requestDisallowInterceptTouchEvent(true);
                            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                                case MotionEvent.ACTION_UP:
                                    infoScroll.requestDisallowInterceptTouchEvent(false);
                                    break;
                            }
                            return false;
                        }
                    });
                    currentSessionNoStr = RealLuceAuctionApplication.allPlaces.get(0).getTimesNo();
                    currentItemSessionNoStr = currentItem.getTimesNo();
                    try{
                        btn.setEnabled(Integer.parseInt(currentSessionNoStr) <= Integer.parseInt(currentItemSessionNoStr)); // 登録ボタン（メモ）
//                        txtValue.setEnabled(Integer.parseInt(currentSessionNoStr) <= Integer.parseInt(currentItemSessionNoStr)); // メモ入力欄
                        if(Integer.parseInt(currentSessionNoStr) <= Integer.parseInt(currentItemSessionNoStr)){

                            txtValue.setFocusableInTouchMode(true);
                        }
                        else{
                            txtValue.setFocusableInTouchMode(false);
                            txtValue.clearFocus();
                        }
                    }catch (Exception ignored){
                    }
                    btn.setVisibility(View.VISIBLE);

                    if (itemInfor != null && itemInfor.getMemo() != null) {
                        txtValue.setText(itemInfor.getMemo());
                        db_memo = itemInfor.getMemo();
                    } else {
                        txtValue.setText("");
                        db_memo = "";
                    }
                    break;
            }

            lblLabel.setTypeface(yGR);

            switch (rows.get(position).rowType) {
                case Memo:
                    txtValue.setTypeface(yGR);
                    break;
                default:
                    lblValue.setTypeface(yGR);
                    break;
            }

            return convertView;
        }

        // 文字一覧の項目数
        @Override
        public int getCount() {
            return itemCount;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


    }

    public class ImageItemView {
        //		private ImageButton btnCaptureImage;
//		private ImageButton btnDeleteImage;
        private ImageView imvImage;
        //private RelativeLayout blockImage;
    }

    // 画像の削除
    public void deleteImage(int pos) {
        final int position = pos;
        HTTPService objService = new HTTPService(new HTTPServiceCallback() {
            @Override
            public void onSucessfully(Object returnData) {
                hideProgressDialog();
                BaseInfo retObj = (BaseInfo) returnData;
                if (retObj.getStatus() != 0) {
                    MessageBox.show(ItemDetailActivity.this.getActivityContext(), R.string.error_title_webservice, R.string.error_message_server_error);
                } else {
                    switch (position) {
                        case 1:
                            currentItem.setImage1(null);
                            break;
                        case 2:
                            currentItem.setImage2(null);
                            break;
                        case 3:
                            currentItem.setImage3(null);
                            break;
                        case 4:
                            currentItem.setImage4(null);
                            break;
                        case 5:
                            currentItem.setImage5(null);
                            break;
                        case 6:
                            currentItem.setImage6(null);
                            break;
                    }

                }
            }

            @Override
            public void onFailed(int errorCode) {
                hideProgressDialog();
                switch (errorCode) {
                    case HTTPService.ERROR_NETWORK:
                        MessageBox.show(ItemDetailActivity.this.getActivityContext(), R.string.error_title_webservice, R.string.error_message_xml_connect_gateway);
                        break;
                    case HTTPService.ERROR_XMLPARSER:
                        MessageBox.show(ItemDetailActivity.this.getActivityContext(), R.string.error_title_webservice, R.string.error_message_xml_parser);
                        break;
                    case HTTPService.ERROR_RETURN_DATA_INVALID:
                        MessageBox.show(ItemDetailActivity.this.getActivityContext(), R.string.error_title_webservice, R.string.error_message_return_data_invalid);
                        break;
                    case HTTPService.ERROR_SERVER_ERROR:
                        MessageBox.show(ItemDetailActivity.this.getActivityContext(), R.string.error_title_webservice, R.string.error_message_server_error);
                        break;
                    case HTTPService.ERROR_CANCEL:
                        break;
                    default:
                        MessageBox.show(ItemDetailActivity.this.getActivityContext(), R.string.error_title_webservice, R.string.error_message_unknow);
                        break;

                }
            }

        });

        showProgressDialog(R.string.please_waiting);
        objService.setImage(currentItem.getPlaceId(), currentItem.getTimesNo(), String.valueOf(currentItem.getItemNo()), position, null);
    }

    /*
     * Create confirm message.
     */
    private void deleteConfirmMessage(int titleId, int messageId, String okText,
                                      String cancelText, int pos) {
        final int position = pos;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set value.
        builder.setMessage(messageId);
        builder.setTitle(titleId);
        builder.setCancelable(false);
        builder.setPositiveButton(okText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Yesボタンが押された時の処理
                deleteImage(position);
            }
        });
        builder.setNegativeButton(cancelText, null);

        // Create dialog and show to screen.
        AlertDialog alert = builder.create();
        alert.show();
        // Must call show() prior to fetching views
        TextView messageView = (TextView) alert.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);

        TextView titleView = (TextView) alert.findViewById(this.getResources().getIdentifier("alertTitle", "id", "android"));
        if (titleView != null) {
            titleView.setGravity(Gravity.CENTER);
        }
    }

    private class TapGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            imageViewDialog.show(Integer.toString(currentItem.getItemNo()),
                    Integer.toString(mPager.getCurrentItem() + 1), currentItem.getTimesNo());
            return super.onSingleTapConfirmed(e);
        }
//							   @Override
//							   public boolean onFling(MotionEvent e1, MotionEvent e2, float
//					 distanceX, float distanceY){
//
//								   if(distanceX < 0){
//									   int position = mPager.getCurrentItem() + 1;
//									   if(position < mPager.getAdapter().getCount()){
//										   mPager.setCurrentItem(position);
//									   }
//								   }else if(distanceX > 0){
//									   int position = mPager.getCurrentItem() - 1;
//									   if(position >= 0){
//										   mPager.setCurrentItem(position);
//									   }
//								   }
//								   return true;
//			}
    }

    /**
     * 戻るボタン
     */
    @Override
    public void onBackPressed() {
        ScreenTransitionFlg = ItemDetailActivity.ScreenTransitionFlg.Back_btn;
        backProcessing();
    }


}
