package tokiokaability.auction24.activities;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import tokiokaability.auction24.BrandAdapter;
import tokiokaability.auction24.CategoryAdapter;
import tokiokaability.auction24.HTTPInfo.LoginInfo;
import tokiokaability.auction24.HTTPInfo.SearchInfo;
import tokiokaability.auction24.R;
import tokiokaability.auction24.RealLuceAuctionApplication;
import tokiokaability.auction24.HTTPInfo.HTTPService;
import tokiokaability.auction24.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction24.HTTPInfo.PlaceInfo;
import tokiokaability.auction24.commons.MessageBox;
import tokiokaability.auction24.commons.Utilities;
import tokiokaability.auction24.view.PlacesView;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import static android.view.View.GONE;

public class SearchBoxNoActivity extends BaseActivity {

    private List<PlaceInfo> placeInfoList = null;
    private PlacesView blockPlace = null;



	private String onePriceFlg = "0";
	private TextView screenTitle;
	private RelativeLayout backGround;
	/*
	 * This static member is to save screen state
	 */
	private static String stateBoxNo = "";
	private static PlaceInfo statePlace = null;
	LinearLayout kaisaiGroup,kaisaiGroup2;
	Button catBtn,hakoBtn,kensakuBtn;
	View catBottomLine,hakoBottomLine;
	TextView kaisaiTitle,catTitle,medCatTitle,brandTitle,rankTitle,freeTitle,hakoTitle,kaisaiTxt,catTxt,medCatTxt,brandTxt,rankTxt,hakoManaka,kaisaiTitle2,kaisaiTxt2;
	EditText freeTxt,hakoTxt,hakoToTxt;
	ImageButton kaisaiClear,kaisaiClear2,catClear,medCatClear,brandClear,rankClear,freeClear,hakoClear;
	ImageView kaisaiArrow,kaisaiArrow2,catArrow,medCatArrow,brandArrow,rankArrow;
	RelativeLayout catKensaku,hakoKensaku,kaisaiRel,kaisaiRel2,catRel,medCatRel,brandRel,rankRel;
	ListView kaisaiLst,kaisaiLst2,catLst,medCatLst,brandLst,rankLst;
	CategoryAdapter catAdapter;
	BrandAdapter kaisaiAdapter,kaisaiAdapter2,medCatAdapter,brandAdapter,rankAdapter;
	String kakoFlag;
	ImageButton btnBid,btnBid2;
	ScrollView kensakuScroll;
	boolean hakoSearch = false;
	String lang = "ja";
	String cornerId = "1";
	//************************ SCREEN LIFECYCLE ******************
	/*
	 * Initialization
	 */
	@Override
	protected void initialization(){
		setContentView(R.layout.activity_search_boxno);
		super.initialization();
		setStatusBar();


		backGround = (RelativeLayout) findViewById(R.id.mainLayout);
		backGround.setOnClickListener(backgroundClick);



		screenTitle = (TextView) findViewById(R.id.lblScreenTitle);
		kakoFlag = (String) getActivityParam("KakoFlag", "0");

        statePlace = null;
		this.initCom();
		if(kakoFlag.equals("1")){
			screenTitle.setText(getResources().getString(R.string.kako_kensaku_title));
			kaisaiGroup.setVisibility(View.VISIBLE);
			kaisaiGroup2.setVisibility(View.VISIBLE);
		}
		else{
			screenTitle.setText(getResources().getString(R.string.sell_number_search));
			kaisaiGroup.setVisibility(GONE);
			kaisaiGroup2.setVisibility(GONE);
		}
		this.addListener();
		SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS, MODE_PRIVATE);
		lang = settings.getString(RealLuceAuctionApplication.SETTINGS_VIEW_LANGUAGE, Locale.getDefault().getLanguage());
		setKaisaiList();
		setKaisaiList2();
        //Load data
		if(Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT)setFont();
		setHelpResourceId(R.layout.type_search_help);
        loadPlaces();

	}

	void initCom(){

		kaisaiGroup = findViewById(R.id.kaisaiGroup);
		kaisaiGroup2 = findViewById(R.id.kaisaiGroup2);
		catBtn = findViewById(R.id.categoryBtn);
		hakoBtn = findViewById(R.id.hakoBtn);
		catBottomLine = findViewById(R.id.categoryBotLine);
		hakoBottomLine = findViewById(R.id.hakoBotLine);

		catKensaku = findViewById(R.id.catKensaku);
		hakoKensaku = findViewById(R.id.hakoKensaku);
		kensakuBtn = findViewById(R.id.kensakuBtn);

		kaisaiTitle = findViewById(R.id.kaisaiTitle);
		kaisaiTitle2 = findViewById(R.id.kaisaiTitle2);
		catTitle = findViewById(R.id.catTitle);
		medCatTitle = findViewById(R.id.medCatTitle);
		brandTitle = findViewById(R.id.brandTitle);
		rankTitle = findViewById(R.id.rankTitle);
		freeTitle = findViewById(R.id.freeTitle);

		kaisaiTxt = findViewById(R.id.kaisaiTxt);
		kaisaiTxt2 = findViewById(R.id.kaisaiTxt2);
		catTxt = findViewById(R.id.catTxt);
		medCatTxt = findViewById(R.id.medCatTxt);
		brandTxt = findViewById(R.id.brandTxt);
		rankTxt = findViewById(R.id.rankTxt);
		freeTxt = findViewById(R.id.freeTxt);

		kaisaiClear = findViewById(R.id.kaisaiClearBtn);
		kaisaiClear2 = findViewById(R.id.kaisaiClearBtn2);
		catClear = findViewById(R.id.catClearBtn);
		medCatClear = findViewById(R.id.medCatClearBtn);
		brandClear = findViewById(R.id.brandClearBtn);
		rankClear = findViewById(R.id.rankClearBtn);
		freeClear = findViewById(R.id.freeClearBtn);

		kaisaiRel = findViewById(R.id.kaisaiRel);
		kaisaiRel2 = findViewById(R.id.kaisaiRel2);
		catRel = findViewById(R.id.catRel);
		medCatRel = findViewById(R.id.medCatRel);
		brandRel = findViewById(R.id.brandRel);
		rankRel = findViewById(R.id.rankRel);

		kaisaiArrow = findViewById(R.id.kaisaiArrow);
		kaisaiArrow2 = findViewById(R.id.kaisaiArrow2);
		catArrow = findViewById(R.id.catArrow);
		medCatArrow = findViewById(R.id.medCatArrow);
		brandArrow = findViewById(R.id.brandArrow);
		rankArrow = findViewById(R.id.rankArrow);

		kaisaiLst = findViewById(R.id.kaisaiLst);
		kaisaiLst2 = findViewById(R.id.kaisaiLst2);
		catLst = findViewById(R.id.catLst);
		medCatLst = findViewById(R.id.medCatLst);
		brandLst = findViewById(R.id.brandLst);
		rankLst = findViewById(R.id.rankLst);

		hakoTitle = findViewById(R.id.hakoTitle);
		hakoTxt = findViewById(R.id.hakoTxt);
		hakoToTxt = findViewById(R.id.hakoToTxt);
		hakoClear = findViewById(R.id.hakoClearBtn);

		btnBid = findViewById(R.id.btnBit);
		btnBid2 = findViewById(R.id.btnBit2);
		btnBid.setTag("1");
		btnBid2.setTag("2");
		kensakuScroll = findViewById(R.id.kensakuScroll);
		hakoManaka = findViewById(R.id.hakoManaka);
	}

	void addListener(){
		catBtn.setOnClickListener(tabClickListener);
		hakoBtn.setOnClickListener(tabClickListener);

		kaisaiRel.setOnClickListener(kaisaiListClick);
		kaisaiRel2.setOnClickListener(kaisaiListClick2);
		catRel.setOnClickListener(cornerListClick);
		medCatRel.setOnClickListener(medCatListClick);
		brandRel.setOnClickListener(brandListClick);

		rankRel.setOnClickListener(rankListClick);

		kaisaiLst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(kaisaiAdapter.getLstCheck().get(position).equals("0"))
					kaisaiAdapter.updateChkList(kaisaiAdapter.getLstValue().get(position),"1");
				else
					kaisaiAdapter.updateChkList(kaisaiAdapter.getLstValue().get(position),"0");
				kaisaiAdapter.notifyDataSetChanged();
				kaisaiTxt.setText(kaisaiAdapter.getCurrentValue());
				getSearchInfo("","","",true,true,true);
			}
		});

		kaisaiLst2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(kaisaiAdapter2.getLstCheck().get(position).equals("0"))
					kaisaiAdapter2.updateChkList(kaisaiAdapter2.getLstValue().get(position),"1");
				else
					kaisaiAdapter2.updateChkList(kaisaiAdapter2.getLstValue().get(position),"0");
				kaisaiAdapter2.notifyDataSetChanged();
				kaisaiTxt2.setText(kaisaiAdapter2.getCurrentValue());
			}
		});

		catLst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				catTxt.setText(catAdapter.getLstCat().get(position));
				cornerId = String.valueOf(position +1);
				catLst.setVisibility(GONE);
				catArrow.setImageResource(R.drawable.arr_down);
				getSearchInfo("'"+catTxt.getText().toString()+"'","","",false,true,true);
			}
		});

		medCatLst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(medCatAdapter.getLstCheck().get(position).equals("0"))
					medCatAdapter.updateChkList(medCatAdapter.getLstValue().get(position),"1");
				else
					medCatAdapter.updateChkList(medCatAdapter.getLstValue().get(position),"0");
				medCatAdapter.notifyDataSetChanged();
				medCatTxt.setText(medCatAdapter.getCurrentValue());
				String catSend = catTxt.getText().toString().length() > 0?"'"+catTxt.getText().toString()+"'":"";
				getSearchInfo(catSend,medCatAdapter.getUpdateableValue(),"",false,false,true);
			}
		});

		brandLst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(brandAdapter.getLstCheck().get(position).equals("0"))
					brandAdapter.updateChkList(brandAdapter.getLstValue().get(position),"1");
				else
					brandAdapter.updateChkList(brandAdapter.getLstValue().get(position),"0");
				brandAdapter.notifyDataSetChanged();
				brandTxt.setText(brandAdapter.getCurrentValue());
				String catSend = catTxt.getText().toString().length() > 0?"'"+catTxt.getText().toString()+"'":"";
				getSearchInfo(catSend,medCatAdapter.getUpdateableValue(),brandAdapter.getUpdateableValue(),false,false,false);
			}
		});

		rankLst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(rankAdapter.getLstCheck().get(position).equals("0"))
					rankAdapter.updateChkList(rankAdapter.getLstValue().get(position),"1");
				else
					rankAdapter.updateChkList(rankAdapter.getLstValue().get(position),"0");
				rankAdapter.notifyDataSetChanged();
				rankTxt.setText(rankAdapter.getCurrentValue());
			}
		});

		kensakuBtn.setOnClickListener(kensakuClick);

		kaisaiClear.setOnClickListener(btnClearClick);
		kaisaiClear2.setOnClickListener(btnClearClick);
		catClear.setOnClickListener(btnClearClick);
		medCatClear.setOnClickListener(btnClearClick);
		brandClear.setOnClickListener(btnClearClick);
		rankClear.setOnClickListener(btnClearClick);
		freeClear.setOnClickListener(btnClearClick);
		hakoClear.setOnClickListener(btnClearClick);

		//btnBid.setOnClickListener(btnBitClick);
		catKensaku.setOnClickListener(backgroundClick);

		hakoKensaku.setOnClickListener(backgroundClick);
		//kensakuScroll.requestDisallowInterceptTouchEvent(true);
	}

	View.OnClickListener kaisaiListClick = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			if(getActivityContext().getCurrentFocus()!= null)
				im.hideSoftInputFromWindow(getActivityContext().getCurrentFocus().getWindowToken(),0);
			if(kaisaiLst.getVisibility() == View.VISIBLE){
				kaisaiLst.setVisibility(GONE);
				kaisaiArrow.setImageResource(R.drawable.arr_down);
			}
			else{
				kaisaiLst.setVisibility(View.VISIBLE);
				medCatLst.setVisibility(GONE);
				catLst.setVisibility(GONE);
				brandLst.setVisibility(GONE);
				rankLst.setVisibility(GONE);
				kaisaiArrow.setImageResource(R.drawable.arr_up);
			}
		}
	};

	View.OnClickListener kaisaiListClick2 = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			if(getActivityContext().getCurrentFocus()!= null)
				im.hideSoftInputFromWindow(getActivityContext().getCurrentFocus().getWindowToken(),0);
			if(kaisaiLst2.getVisibility() == View.VISIBLE){
				kaisaiLst2.setVisibility(GONE);
				kaisaiArrow2.setImageResource(R.drawable.arr_down);
			}
			else{
				kaisaiLst2.setVisibility(View.VISIBLE);
				kaisaiArrow2.setImageResource(R.drawable.arr_up);
			}
		}
	};

	View.OnClickListener cornerListClick = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			if(getActivityContext().getCurrentFocus()!= null)
				im.hideSoftInputFromWindow(getActivityContext().getCurrentFocus().getWindowToken(),0);
			if(catLst.getVisibility() == View.VISIBLE){
				catLst.setVisibility(GONE);
				catArrow.setImageResource(R.drawable.arr_down);
			}
			else{

				catLst.setVisibility(View.VISIBLE);
				medCatLst.setVisibility(GONE);
				brandLst.setVisibility(GONE);
				rankLst.setVisibility(GONE);
				kaisaiLst.setVisibility(GONE);
				catArrow.setImageResource(R.drawable.arr_up);
			}
		}
	};

	View.OnClickListener medCatListClick = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			if(getActivityContext().getCurrentFocus()!= null)
				im.hideSoftInputFromWindow(getActivityContext().getCurrentFocus().getWindowToken(),0);
			if(medCatLst.getVisibility() == View.VISIBLE){
				medCatLst.setVisibility(GONE);
				medCatArrow.setImageResource(R.drawable.arr_down);
			}
			else{
				medCatLst.setVisibility(View.VISIBLE);
				catLst.setVisibility(GONE);
				brandLst.setVisibility(GONE);
				rankLst.setVisibility(GONE);
				kaisaiLst.setVisibility(GONE);
				medCatArrow.setImageResource(R.drawable.arr_up);
			}
		}
	};

	View.OnClickListener brandListClick = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			if(getActivityContext().getCurrentFocus()!= null)
				im.hideSoftInputFromWindow(getActivityContext().getCurrentFocus().getWindowToken(),0);
			if(brandLst.getVisibility() == View.VISIBLE){
				brandLst.setVisibility(GONE);
				brandArrow.setImageResource(R.drawable.arr_down);
			}
			else{
				brandLst.setVisibility(View.VISIBLE);
				medCatLst.setVisibility(GONE);
				catLst.setVisibility(GONE);
				rankLst.setVisibility(GONE);
				kaisaiLst.setVisibility(GONE);
				brandArrow.setImageResource(R.drawable.arr_up);
			}

		}
	};

	View.OnClickListener rankListClick = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			if(getActivityContext().getCurrentFocus()!= null)
				im.hideSoftInputFromWindow(getActivityContext().getCurrentFocus().getWindowToken(),0);
			if(rankLst.getVisibility() == View.VISIBLE){
				rankLst.setVisibility(GONE);
				rankArrow.setImageResource(R.drawable.arr_down);
			}
			else{
				rankLst.setVisibility(View.VISIBLE);
				medCatLst.setVisibility(GONE);
				brandLst.setVisibility(GONE);
				catLst.setVisibility(GONE);
				kaisaiLst.setVisibility(GONE);
				rankArrow.setImageResource(R.drawable.arr_up);
			}
		}
	};

	OnClickListener tabClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			if(v.getTag().equals("1")){
				hakoBtn.setTextColor(getResources().getColor(R.color.mn_tab_black));
				hakoBottomLine.setVisibility(GONE);
				hakoKensaku.setVisibility(GONE);

				catBtn.setTextColor(getResources().getColor(R.color.mn_tab_green));
				catBottomLine.setVisibility(View.VISIBLE);
				catKensaku.setVisibility(View.VISIBLE);
				//hakoTxt.setText("");
				hakoSearch = false;
			}
			else{
				catBtn.setTextColor(getResources().getColor(R.color.mn_tab_black));
				catBottomLine.setVisibility(GONE);
				catKensaku.setVisibility(GONE);

				hakoBtn.setTextColor(getResources().getColor(R.color.mn_tab_green));
				hakoBottomLine.setVisibility(View.VISIBLE);
				hakoKensaku.setVisibility(View.VISIBLE);

				catLst.setVisibility(GONE);
				brandLst.setVisibility(GONE);
				rankLst.setVisibility(GONE);
				//freeTxt.setText("");
				hakoSearch = true;
				//getSearchInfo("","",true,true);
			}
		}
	};

	OnClickListener kensakuClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			catLst.setVisibility(GONE);
			catArrow.setImageResource(R.drawable.arr_down);
			medCatLst.setVisibility(GONE);
			medCatArrow.setImageResource(R.drawable.arr_down);
			brandLst.setVisibility(GONE);
			brandArrow.setImageResource(R.drawable.arr_down);
			rankLst.setVisibility(GONE);
			rankArrow.setImageResource(R.drawable.arr_down);
			search();
		}
	};


	OnClickListener btnClearClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int tag = Integer.parseInt(v.getTag().toString());
			switch (tag){
				case 1:
					getSearchInfo("","","",true,true,true);
					break;
				case 2:
					String catSend = catTxt.getText().toString().length() > 0?"'"+catTxt.getText().toString()+"'":"";
					getSearchInfo(catSend,"","",false,false,true);
					break;
				case 3:
					rankAdapter.resetChkList();
					rankAdapter.notifyDataSetChanged();
					rankTxt.setText("");
					break;
				case 4:
					freeTxt.setText("");
					break;
				case 5:
					hakoTxt.setText("");
					hakoToTxt.setText("");
					break;
				case 6:
					medCatTxt.setText("");
					String catSend_med = catTxt.getText().toString().length() > 0?"'"+catTxt.getText().toString()+"'":"";
					getSearchInfo(catSend_med,medCatAdapter.getUpdateableValue(),"",false,true,true);
				case 7:
					kaisaiTxt.setText("");
					setKaisaiList();
					getSearchInfo("","","",true,true,true);
					break;
				case 8:
					kaisaiTxt2.setText("");
					setKaisaiList2();
					break;
			}
		}
	};



	private  void setFont(){
		AssetManager am = getApplicationContext().getAssets();
		Typeface yGB = Typeface.createFromAsset(am,"fonts/YUGOTHB.TTC");
		screenTitle.setTypeface(yGB);
		catBtn.setTypeface(yGB);
		hakoBtn.setTypeface(yGB);
		kensakuBtn.setTypeface(yGB);
		kaisaiTitle.setTypeface(yGB);
		kaisaiTxt.setTypeface(yGB);
		kaisaiTitle2.setTypeface(yGB);
		kaisaiTxt2.setTypeface(yGB);
		catTitle.setTypeface(yGB);
		catTxt.setTypeface(yGB);
		medCatTitle.setTypeface(yGB);
		medCatTxt.setTypeface(yGB);
		brandTitle.setTypeface(yGB);
		brandTxt.setTypeface(yGB);
		rankTitle.setTypeface(yGB);
		rankTxt.setTypeface(yGB);
		freeTitle.setTypeface(yGB);
		freeTxt.setTypeface(yGB);
		hakoTitle.setTypeface(yGB);
		hakoTxt.setTypeface(yGB);
		hakoToTxt.setTypeface(yGB);
		hakoManaka.setTypeface(yGB);
	}

    /*
	 * Load places from server
	 */
    private void loadPlaces(){
        HTTPService objService = new HTTPService(new HTTPServiceCallback(){

            @SuppressWarnings("unchecked")
            @Override
            public void onSucessfully(Object returnData) {
                //Binding BlockPlace
                placeInfoList = (ArrayList<PlaceInfo>)returnData;
                if(placeInfoList!=null && placeInfoList.size()>0){
					statePlace = placeInfoList.get(0);
                }
                if(statePlace.getRealFlag().equals("1")){
                	btnBid.setVisibility(View.VISIBLE);
					btnBid2.setVisibility(View.VISIBLE);
				}
                else{
                	btnBid.setVisibility(GONE);
					btnBid2.setVisibility(GONE);
				}
                getSearchInfo("","","",true,true,true);

            }
            @Override
            public void onFailed(int errorCode) {
                hideProgressDialog();

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
            }

        });

        showProgressDialog(R.string.please_waiting);

        objService.getPlacesByLoginID(RealLuceAuctionApplication.loginID,(Integer)getActivityParam("Mode"));
    }

	private View.OnClickListener btnBitClick = new View.OnClickListener() {
		public void onClick(View v) {
			btnBid.setEnabled(false);
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

	private void checkLoginAndShowAuction() {

		HTTPService objServiceCheckLogin = new HTTPService(new HTTPServiceCallback() {
			@Override
			public void onSucessfully(Object returnData) {
				LoginInfo loginInfo = (LoginInfo) returnData;

				if (loginInfo.getResult().equalsIgnoreCase("1")) {
					setActivityParam("Place", statePlace);

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

    void getSearchInfo(final String cornerLst, final String medCatList,final String brandList, final boolean resetCorner, final boolean resetMedCat,final boolean resetBrand){
		HTTPService objService = new HTTPService(new HTTPServiceCallback(){

			@SuppressWarnings("unchecked")
			@Override
			public void onSucessfully(Object returnData) {
				hideProgressDialog();
				SearchInfo sInfo = (SearchInfo) returnData;
				if(resetCorner){
					catAdapter = new CategoryAdapter();
					catAdapter.setmContext(getActivityContext());
					catAdapter.setLstCat(sInfo.getLstCat());
					catLst.setAdapter(catAdapter);
					catTxt.setText("");
				}

				if(resetMedCat){
					medCatAdapter = new BrandAdapter();
					medCatAdapter.setmContext(getActivityContext());
					medCatAdapter.setLstValue(sInfo.getLstMedCat());
					medCatAdapter.resetChkList();
					medCatLst.setAdapter(medCatAdapter);
					medCatTxt.setText("");
				}

				if(resetBrand){
					brandAdapter = new BrandAdapter();
					brandAdapter.setmContext(getActivityContext());
					brandAdapter.setLstValue(sInfo.getLstBrand());
					brandAdapter.resetChkList();
					brandLst.setAdapter(brandAdapter);
					brandTxt.setText("");
				}

				rankAdapter = new BrandAdapter();
				rankAdapter.setmContext(getActivityContext());
				rankAdapter.setLstValue(sInfo.getLstRank());
				rankAdapter.resetChkList();
				rankLst.setAdapter(rankAdapter);
				rankTxt.setText("");
			}
			@Override
			public void onFailed(int errorCode) {
				hideProgressDialog();

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
			}

		});

		objService.getSearchInfo(statePlace.getIdNo(),getSessionNo(),cornerLst,medCatList,brandList);
	}


	private String getSessionNo(){
		String sessionNo = "";
		if(kakoFlag.equals("1")){
			if(kaisaiTxt.getText().length() > 0 && kaisaiAdapter!= null && !hakoSearch){
				sessionNo = kaisaiAdapter.getUpdateableSessionValue();
			}
			else{
				List<String> sessionArr = RealLuceAuctionApplication.systemInfo.getSessionList();
				if(sessionArr.size()>1){
					for (int i=1;i<sessionArr.size();i++){
						sessionNo+=sessionNo.length()>0?","+sessionArr.get(i):sessionArr.get(i);
					}
				}
			}
		}
		else{
			sessionNo = RealLuceAuctionApplication.systemInfo.getSessionList().get(0);
		}
		return sessionNo;
	}

	private String getSessionNo2(){
		String sessionNo = "";
		if(kakoFlag.equals("1")){
			if(kaisaiTxt2.getText().length() > 0 && kaisaiAdapter2!= null && hakoSearch){
				sessionNo = kaisaiAdapter2.getUpdateableSessionValue();
			}
			else{
				List<String> sessionArr = RealLuceAuctionApplication.systemInfo.getSessionList();
				if(sessionArr.size()>1){
					for (int i=1;i<sessionArr.size();i++){
						sessionNo+=sessionNo.length()>0?","+sessionArr.get(i):sessionArr.get(i);
					}
				}
			}
		}
		else{
			sessionNo = RealLuceAuctionApplication.systemInfo.getSessionList().get(0);
		}
		return sessionNo;
	}

	private OnClickListener backgroundClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			if(getActivityContext().getCurrentFocus()!= null)
				im.hideSoftInputFromWindow(getActivityContext().getCurrentFocus().getWindowToken(),0);
			kaisaiLst.setVisibility(GONE);
			kaisaiArrow.setImageResource(R.drawable.arr_down);
			kaisaiLst2.setVisibility(GONE);
			kaisaiArrow2.setImageResource(R.drawable.arr_down);
			catLst.setVisibility(GONE);
			catArrow.setImageResource(R.drawable.arr_down);
			medCatLst.setVisibility(GONE);
			medCatArrow.setImageResource(R.drawable.arr_down);
			brandLst.setVisibility(GONE);
			brandArrow.setImageResource(R.drawable.arr_down);
			rankLst.setVisibility(GONE);
			rankArrow.setImageResource(R.drawable.arr_down);
		}
	};
	//************************ VALIDATION ***************************
	/*
	 * Validate searching
	 */
	private boolean validateToSearch(){
		 if(statePlace == null || statePlace.getIdNo() == null  || statePlace.getIdNo().length() <= 0){
			 MessageBox.show(getActivityContext(), R.string.product_search_message_invalidated_title, R.string.product_search_message_no_place);
			 return false;
		 }
		 

		 
		 return true;
		
	}

	private boolean checkInputCat(){
		if(brandTxt.getText().length() == 0
		&& catTxt.getText().length() == 0
		&& medCatTxt.getText().length() == 0
		&& kaisaiTxt.getText().length() == 0
		&& rankTxt.getText().length() == 0
		&& freeTxt.getText().length() == 0){
			MessageBox.show(getActivityContext(),R.string.sendlog_error_title,R.string.search_zenken_confirm);
			return false;
		}
		return true;
	}
	private boolean checkInputHako(){
		if(kaisaiTxt2.getText().length() == 0
				&& hakoTxt.getText().length() == 0
				&& hakoToTxt.getText().length() == 0){
			MessageBox.show(getActivityContext(),R.string.sendlog_error_title,R.string.search_zenken_confirm);
			return false;
		}
		return true;
	}


	//************************ BUSINESS LOGIC PROCESSING ****************
	/*
	 * Search product
	 */
	private void search(){

		String catSend = catTxt.getText().toString().length() > 0?"'"+catTxt.getText().toString()+"'":"";
		setActivityParam("SearchMode",1);
		setActivityParam("Mode", 1);
		setActivityParam("PlaceID",statePlace.getIdNo());
		setActivityParam("ItemNo","");
		if(!hakoSearch){
			if(kakoFlag.equals("1") && !checkInputCat())
				return;
			setActivityParam("Freeword",freeTxt.getText().toString());
			setActivityParam("CornerList",catSend);
			setActivityParam("SubCatList",medCatAdapter.getUpdateableValue());
			setActivityParam("BrandList",brandAdapter.getUpdateableSpaceValue());
			setActivityParam("RankList",rankAdapter.getUpdateableValue());
			setActivityParam("BoxNo","");
			setActivityParam("BoxNoTo","");
			setActivityParam("SessionList",getSessionNo());
			setActivityParam("SessionHosekiList",getSessionNo());
			hakoTxt.setText("");
			hakoToTxt.setText("");
			kaisaiTxt2.setText("");
			setKaisaiList2();
			setActivityParam("CornerId",cornerId);
		}
		else{
			if(kakoFlag.equals("1") && !checkInputHako())
				return;
			setActivityParam("Freeword","");
			setActivityParam("CornerList","");
			setActivityParam("SubCatList","");
			setActivityParam("BrandList","");
			setActivityParam("RankList","");
			setActivityParam("BoxNo",hakoTxt.getText().toString());
			setActivityParam("BoxNoTo",hakoToTxt.getText().toString());
			if(!hakoTxt.getText().toString().isEmpty() && Integer.parseInt(hakoTxt.getText().toString()) > 500){
				setActivityParam("CornerId","5");
			}
			else{
				setActivityParam("CornerId","1");
			}
			setActivityParam("SessionList",getSessionNo2());
			setActivityParam("SessionHosekiList",getSessionNo2());
			setKaisaiList();
			kaisaiTxt.setText("");
			freeTxt.setText("");
			getSearchInfo("","","",true,true,true);
		}

		setActivityParam("Type","");

		setActivityParam("KakoFlag",kakoFlag);
		setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);

//		if(catSend.equals("'宝石'") || (hakoTxt.getText().length() > 0 && Integer.valueOf(hakoTxt.getText().toString()) >= 500)){
//			showActivity(WebBidActivity.class);
//		}
//		else{
//			showActivity(ItemsActivity.class);
//		}
		showActivity(WebBidActivity.class);

	}

	private void setKaisaiList(){
		kaisaiAdapter = new BrandAdapter();
		kaisaiAdapter.setmContext(getActivityContext());
		ArrayList<String> kaisaiStringList = new ArrayList<String>();
		String format = lang.equals("ja")?"%s年%s月":"%s-%s";
		for(int i=1;i<RealLuceAuctionApplication.systemInfo.getKaisaibiList().size();i++){
			String[] str = RealLuceAuctionApplication.systemInfo.getKaisaibiList().get(i).split("-");
			String s = String.format(format,str[0],str[1]);
			kaisaiStringList.add(s);
		}
		List<String> lstSession = RealLuceAuctionApplication.systemInfo.getSessionList().subList(1,RealLuceAuctionApplication.systemInfo.getSessionList().size());
		kaisaiAdapter.setLstSession(new ArrayList<String>(lstSession));
		kaisaiAdapter.setLstValue(kaisaiStringList);
		kaisaiAdapter.resetChkList();
		kaisaiLst.setAdapter(kaisaiAdapter);
	}
	private void setKaisaiList2(){
		kaisaiAdapter2 = new BrandAdapter();
		kaisaiAdapter2.setmContext(getActivityContext());
		ArrayList<String> kaisaiStringList = new ArrayList<String>();
		String format = lang.equals("ja")?"%s年%s月":"%s-%s";
		for(int i=1;i<RealLuceAuctionApplication.systemInfo.getKaisaibiList().size();i++){
			String[] str = RealLuceAuctionApplication.systemInfo.getKaisaibiList().get(i).split("-");
			String s = String.format(format,str[0],str[1]);
			kaisaiStringList.add(s);
		}
		List<String> lstSession = RealLuceAuctionApplication.systemInfo.getSessionList().subList(1,RealLuceAuctionApplication.systemInfo.getSessionList().size());
		kaisaiAdapter2.setLstSession(new ArrayList<String>(lstSession));
		kaisaiAdapter2.setLstValue(kaisaiStringList);
		kaisaiAdapter2.resetChkList();
		kaisaiLst2.setAdapter(kaisaiAdapter2);
	}


	@Override
	public void onClosed() {
		super.onClosed();
		clearActivityParam("KakoFlag");
	}
}
