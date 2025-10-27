package tokiokaability.auction24.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tokiokaability.auction24.HTTPInfo.LoginInfo;
import tokiokaability.auction24.HTTPInfo.PlaceInfo;
import tokiokaability.auction24.R;
import tokiokaability.auction24.RealLuceAuctionApplication;
import tokiokaability.auction24.HTTPInfo.ItemInfo;
import tokiokaability.auction24.HTTPInfo.HTTPService;
import tokiokaability.auction24.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction24.commons.MessageBox;
import tokiokaability.auction24.commons.Utilities;
import tokiokaability.auction24.dialog.CorrectionDialog;
import tokiokaability.auction24.dialog.ImageViewDialog;
import tokiokaability.auction24.dialog.NegotiationDialog;
import tokiokaability.auction24.view.AutoFitTextView;
import tokiokaability.auction24.view.RoundRectCornerImageView;
//import rasis.realluceauction.view.ImageSliderView;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.TypedValue;
//import android.util.Log;
//import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
//import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

public class ItemsActivity extends BaseActivity {

	private ImageButton btnPrevious;
	private ImageButton btnNext;
	private TextView lblScreenTitle;
	private TextView lblPageNo;
	private ListView lvItemInfoList;
	private ImageButton btnSort,btnSort2;
	private ImageButton btnImageFilter;
	private String[] displayItemArray = null;



	private int currentPage = 0;
	private int searchMode;
	private int brandItemCount = 0;
	private int totalPage = 0;
	private ArrayList<ItemInfo> itemInfoList;
	private String placeId;
	private String itemNo;
	private String boxNo;
	private String boxNoTo;
	private String freeWord;
	private String cornerId;
	private String type;
	private int maxRowCount = 0;
	private String carName = "";
	private String sortId = "0";
	private String sortOrder = "0";
	private String koshoQuery = "0";
	private int mode;
	private AlertDialog dialogDetails = null,dialogDetails2 = null;
	private ImageViewDialog imageViewDialog;

	private TextView lblTotalPrice;
	private TextView lblValue;
	private TextView kaisaibiTxt,cornerTxt,cornerCenterTxt;
	private ImageView dolMark;
	private ImageButton btnSelect,btnCornerSelect,btnCornerCenterSelect;
	private ListView kaisaibiList,lstCorner,lstCenterCorner;
	private TextView lblItemCount,lblDisplayItems,lblItemCount2;
	private RelativeLayout kaisaibiSearchLayout;
	//private boolean isDisplayMoney;
    private boolean hasPicture; // has picture = All,
	int dialogId1,dialogId2;
	private SharedPreferences settings;

	public AssetManager am ;
	public Typeface yGR,yGB;
	ImageButton btnBid,btnBid2;
	private String CurrentSessionNo;
	String cornerList,subCatList,brandList,rankList,kakoFlag;

	private boolean isScroll = true;
	private int currentPositon;
	private CorrectionDialog correctionDialog;
	private NegotiationDialog negDialog;
	private CheckBox chkShowKosho;
	private Button btnKoshoType;
	int currentKoshoIndex;
	private String currentItemList;
	// ************************ SCREEN LIFECYCLE ******************
	/*
	 * Initialization
	 */
	@Override
	protected void initialization() {
		setContentView(R.layout.activity_items);
		super.initialization();
		setStatusBar();
		currentFunction = MENU_CURRENT_FUNCTION_LIST;
		// Initial
		settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS,
				MODE_PRIVATE);

		itemInfoList = new ArrayList<ItemInfo>();

		am = getApplicationContext().getAssets();
		yGB = Typeface.createFromAsset(am,"fonts/YUGOTHB.TTC");
		yGR = Typeface.createFromAsset(am,"fonts/YUGOTHR.TTC");
		// Get control
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
		lblScreenTitle = (TextView) findViewById(R.id.lblScreenTitle);
		lvItemInfoList = (ListView) findViewById(R.id.lvItemInfoList);
		lblPageNo = (TextView) findViewById(R.id.lblPageNo);
		btnImageFilter = (ImageButton) findViewById(R.id.btnImageFilter);
		btnSort = (ImageButton) findViewById(R.id.btnSort);
		btnSort2 = findViewById(R.id.btnSort2);
		if(Build.VERSION.SDK_INT!=Build.VERSION_CODES.KITKAT){
			lblScreenTitle.setTypeface(yGB);
			lblPageNo.setTypeface(yGR);
		}

		lblTotalPrice = (TextView) findViewById(R.id.lblTotalPrice);
		lblValue = (TextView) findViewById(R.id.lblValue);
		kaisaibiTxt = (TextView) findViewById(R.id.kaisaibiSearchTxt);
		cornerTxt = findViewById(R.id.cornerSearchTxt);
		cornerCenterTxt = findViewById(R.id.cornerCenterTxt);
		cornerTxt.setText("時計");
		cornerCenterTxt.setText("時計");
		dolMark = (ImageView) findViewById(R.id.searchDollar);
		btnSelect = (ImageButton) findViewById(R.id.selectBtn);
		btnCornerSelect = findViewById(R.id.cornerSelectBtn);
		btnCornerCenterSelect = findViewById(R.id.cornerCenterSelectBtn);
		lblItemCount = (TextView) findViewById(R.id.lblItemCount);
		lblItemCount2 = (TextView) findViewById(R.id.lblItemCount2);
		kaisaibiSearchLayout = (RelativeLayout)findViewById(R.id.kaisaibiSearchLayout);
		kaisaibiList = (ListView) findViewById(R.id.kaisaibiList);
		lstCorner = findViewById(R.id.cornerList);
		lstCenterCorner = findViewById(R.id.cornerCenterList);
		btnBid = findViewById(R.id.btnBit);
		btnBid2 = findViewById(R.id.btnBit2);
		btnBid.setTag("1");
		btnBid2.setTag("2");
		//btnBid.setOnClickListener(btnBitClick);
		final ArrayAdapter<String> kaisaibiAdapter = new ArrayAdapter<String>(this,R.layout.simple_spinner_row,RealLuceAuctionApplication.systemInfo.getKaisaibiList());
		kaisaibiList.setAdapter(kaisaibiAdapter);
		kaisaibiList.setOnItemClickListener(KaisaibiSelect);
		final ArrayAdapter<String> cornerAdapter = new ArrayAdapter<>(this,R.layout.simple_spinner_row,new String[]{"時計","宝石"});
		lstCorner.setAdapter(cornerAdapter);
		lstCorner.setOnItemClickListener(cornerSelect);

		lstCenterCorner.setAdapter(cornerAdapter);
		lstCenterCorner.setOnItemClickListener(centerCornerSelect);

		btnSelect.setOnClickListener(btnSelectClickListener);
		btnCornerSelect.setOnClickListener(btnCornerSelectClickListener);
		btnCornerCenterSelect.setOnClickListener(btnCornerCenterSelectClickListener);
		// Register event for Next, previous button.
		btnNext.setOnClickListener(btnNexClick);
		btnPrevious.setOnClickListener(btnPreviousClick);
		chkShowKosho = findViewById(R.id.chkShowKosho);
		chkShowKosho.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				btnSort.setVisibility(View.GONE);
				if(!b){
					sortId = "0";
					sortOrder = "0";
					koshoQuery = "3";
					lblDisplayItems.setText(displayItemArray[2]);
					btnKoshoType.setVisibility(View.VISIBLE);
					btnSort2.setVisibility(View.VISIBLE);
					lblDisplayItems.setVisibility(View.VISIBLE);
					lblItemCount2.setVisibility(View.GONE);
					dialogId2+=2;
					dialogId1+=2;
				}
				else{
					sortId = "0";
					sortOrder = "0";
					koshoQuery = "0";
					btnKoshoType.setVisibility(View.GONE);
					btnSort2.setVisibility(View.GONE);
					lblDisplayItems.setVisibility(View.GONE);
					//lblItemCount2.setVisibility(View.VISIBLE);
					dialogId1+=2;
				}
				getItemList(1,true,0);
			}
		});
		// Get initialize from activity parameter.
		currentItemList = "";
		itemNo = (String) getActivityParam("ItemNo", "");
		boxNo = (String) getActivityParam("BoxNo", "");
		boxNoTo = (String) getActivityParam("BoxNoTo", "");
		freeWord = (String) getActivityParam("Freeword", "");
		searchMode = (Integer) getActivityParam("SearchMode", 0);
		placeId = (String) getActivityParam("PlaceID", "");
		cornerId = (String) getActivityParam("CornerId", "");
		mode = (Integer) getActivityParam("Mode", 0);
		type = (String) getActivityParam("Type");
		cornerList = (String) getActivityParam("CornerList","");
		subCatList = (String) getActivityParam("SubCatList","");
		brandList = (String) getActivityParam("BrandList","");
		rankList = (String) getActivityParam("RankList","");
		kakoFlag = (String) getActivityParam("KakoFlag","0");
		placeId = RealLuceAuctionApplication.allPlaces.get(0).getIdNo();
		btnKoshoType = findViewById(R.id.btnKoshoType);
		btnKoshoType.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				showDialog(dialogId2);
			}
		});
		// Getting maxRow item from private data.
		maxRowCount = this.getMaxRowCount();
		lblDisplayItems = findViewById(R.id.lblDisplayItems);
		// Getting display money status from private data.
		//isDisplayMoney = this.getDisplayMoneyMode();

		currentPage = 1;
        hasPicture = true;
        btnImageFilter.setImageResource(R.drawable.item_switch_ari_button_style);
        kaisaibiTxt.setText(RealLuceAuctionApplication.systemInfo.getKaisaibiList().get(0));
        if(kakoFlag.equals("1")){
			CurrentSessionNo = (String) getActivityParam("SessionList","");
		}
        else{
			CurrentSessionNo = RealLuceAuctionApplication.systemInfo.getSessionList().get(0);
		}

		// setTilte screen
		setScreenTitle();

		// Help button.
		setHelpResourceId(R.layout.list_help);

		setFonts();

		// Set animation
		setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);

		// Invisibily controls.
		this.setVisiblilityControls(View.INVISIBLE);
		correctionDialog = new CorrectionDialog(this, new OnClickListener() {
			@Override
			public void onClick(View view) {
				correctionDialog.dismiss();
			}
		});
		negDialog = new NegotiationDialog(this, new OnClickListener() {
			@Override
			public void onClick(View view) {
				negDialog.dismiss();
			}
		});
		/*
		// Set Invisible/visible for place selecttion
		// based on search mode.
		setVisibleForPlaceSelection();
		*/

		// Create SpecDetailDialog Activity
		imageViewDialog = new ImageViewDialog(this, new OnClickListener() {
			@Override
			public void onClick(View v) {
				imageViewDialog.dismiss();
			}
		});

        btnImageFilter.setOnClickListener(btnImageFilterClick);

		btnSort.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(dialogId1);

			}
		});
		btnSort2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				showDialog(dialogId1);
			}
		});
		if((searchMode == 2 || searchMode == 3)&&!chkShowKosho.isChecked()){
			sortId = "8";
			sortOrder ="3";
			//lblDisplayItems.setText(displayItemArray[2]);
		}
		// Call Business process.
		getItemList(currentPage,true,0);
		
		//lvItemInfoList.setOnItemClickListener(listViewOnItemClick);
		lvItemInfoList.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if(isScroll && currentPage<totalPage && firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount>0 ){
					isScroll = false;
					int index = lvItemInfoList.getFirstVisiblePosition();
					//getItemList(currentPage+1,false,0);
					if(chkShowKosho.isChecked() && currentKoshoIndex>index){
						getItemList(currentPage+1,false,currentKoshoIndex);
						if(lvItemInfoList.getLastVisiblePosition() > currentKoshoIndex)
							currentKoshoIndex = 0;
					}

					else
						getItemList(currentPage+1,false,index);
				}
			}
		});
		displayItemArray = getResources().getStringArray(
				R.array.items_info_list_sort_item_by_horyu_kosho_disp);
		lblDisplayItems.setText(displayItemArray[2]);
		dialogId1 = 1;
		dialogId2 = 2;
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

	void setFonts(){
		if(Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT){
			lblTotalPrice.setTypeface(yGR);
			lblValue.setTypeface(yGR);
			kaisaibiTxt.setTypeface(yGR);
			lblPageNo.setTypeface(yGR);
			lblItemCount.setTypeface(yGR);
			lblItemCount2.setTypeface(yGR);
			btnKoshoType.setTypeface(yGB);
		}
	}

	public OnItemClickListener KaisaibiSelect = new OnItemClickListener() {
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
                currentPage = 1;
                lblValue.setText("0");
                isScroll = false;
                getItemList(currentPage,true,0);
            }
        }
    };

	public OnItemClickListener cornerSelect = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(position == 1){
				cornerTxt.setText("宝石");
				lstCorner.setVisibility(View.GONE);
				setActivityParam("SessionHosekiList",CurrentSessionNo);
				showActivity(WebBidActivity.class);
			}
		}
	};

	public OnItemClickListener centerCornerSelect = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(position == 1){
				cornerCenterTxt.setText("宝石");
				lstCenterCorner.setVisibility(View.GONE);
				setActivityParam("SessionHosekiList",CurrentSessionNo);
				showActivity(WebBidActivity.class);
			}
		}
	};

	public OnClickListener btnSelectClickListener = new OnClickListener() {
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

	public OnClickListener btnCornerSelectClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(lstCorner.getVisibility() == View.VISIBLE){
				lstCorner.setVisibility(View.GONE);
				btnCornerSelect.setImageResource(R.drawable.spinner_arr_down);
			}
			else{
				lstCorner.setVisibility(View.VISIBLE);
				btnCornerSelect.setImageResource(R.drawable.spinner_arr_up);
			}
		}
	};
	public OnClickListener btnCornerCenterSelectClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(lstCenterCorner.getVisibility() == View.VISIBLE){
				lstCenterCorner.setVisibility(View.GONE);
				btnCornerCenterSelect.setImageResource(R.drawable.spinner_arr_down);
			}
			else{
				lstCenterCorner.setVisibility(View.VISIBLE);
				btnCornerCenterSelect.setImageResource(R.drawable.spinner_arr_up);
			}
		}
	};

	public OnItemClickListener listViewOnItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			RealLuceAuctionApplication.currentItemOkiniri = itemInfoList.get(position).getOkiniriFlg();
			// Get current row selected.
			//int position = (Integer) v.getTag();

			setActivityParam("ItemList", ItemInfo.ItemLst);
			setActivityParam("PlaceList", ItemInfo.PlaceLst);
			setActivityParam("SessionList", ItemInfo.SessionLst);

			// Set Current position into activity parameter.
			setActivityParam("DisplayIndex", position);
			currentPositon = position;
			setActivityParam("SearchMode", searchMode);
			// Set animation
			setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);

			// Call car detail activity.
			showActivity(ItemDetailActivity.class);
		}
		
	};

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
		if(lvItemInfoList!= null){
			CarsItemAdapter carAdapter = (CarsItemAdapter) lvItemInfoList.getAdapter();
			if(carAdapter!= null && carAdapter.getCount()> 0){
				if(RealLuceAuctionApplication.okiniriLst.size() > 0){
					for(int i=0;i<RealLuceAuctionApplication.okiniriLst.size();i++){
						int offset = RealLuceAuctionApplication.okiniriOffsetLst.get(i);
						if(currentPositon+offset > carAdapter.getCount()-1) continue;
						carAdapter.getItemList().get(currentPositon+offset).setOkiniriFlg(RealLuceAuctionApplication.okiniriLst.get(i));
					}
				}
				if(RealLuceAuctionApplication.fuzaiLst.size() > 0 && searchMode == 4){
					for(int i=0;i<RealLuceAuctionApplication.fuzaiLst.size();i++){
						int offset = RealLuceAuctionApplication.fuzaiOffsetLst.get(i);
						if(currentPositon+offset > carAdapter.getCount()-1) continue;
						carAdapter.getItemList().get(currentPositon+offset).setAbsenceBidPrice(RealLuceAuctionApplication.fuzaiLst.get(i));
					}
				}
				carAdapter.notifyDataSetChanged();
			}
			RealLuceAuctionApplication.detailOffset = 0;
			RealLuceAuctionApplication.okiniriLst = new ArrayList<>();
			RealLuceAuctionApplication.okiniriOffsetLst = new ArrayList<>();
			RealLuceAuctionApplication.fuzaiLst = new ArrayList<>();
			RealLuceAuctionApplication.fuzaiOffsetLst = new ArrayList<>();
		}
	}

	@Override
	public void onClosed() {
		super.onClosed();
		clearActivityParam("CornerList");
		clearActivityParam("SubCatList");
		clearActivityParam("BrandList");
		clearActivityParam("RankList");
		clearActivityParam("KakoFlag");
	}

	@Override
	public void onChildActivityClosed(BaseActivity fromActivity) {
		super.onChildActivityClosed(fromActivity);
		if(chkShowKosho.isChecked() && RealLuceAuctionApplication.isUpdatedKosho){
			this.getItemList(1,true,currentKoshoIndex);
			RealLuceAuctionApplication.isUpdatedKosho = false;
		}
	}

	/*
	 * Create dialog for current activity.
	 */
	@Override
	protected Dialog onCreateDialog(int id) {

		/*
		 * final String[] sortItemSeven = { "【デフォルト】", "▲年式（昇順）", "▼年式（降順）",
		 * "▲落札金額（昇順）", "▼落札金額（降順）", "▲車検（昇順）", "▼車検（降順）", "▲走行距離（昇順）",
		 * "▼走行距離（降順）" };
		 */

		// final String[] sortItemSeven =
		//final String[] sortItemSeven = getResources().getStringArray(
		//		R.array.items_info_list_sort_item_by_seven);
		/*
		 * final String[] sortItemNotSeven = { "【デフォルト】", "▲開催日＋出品番号（昇順）",
		 * "▼開催日＋出品番号（降順）", "▲年式（昇順）", "▼年式（降順）", "▲走行距離（昇順）", "▼走行距離（降順）" };
		 */
		String[] sortItemNotSeven = getResources().getStringArray(
				R.array.items_info_list_sort_item_by_not_seven);
		// 保留交渉一覧の並び替えリスト
		final String[] sortItemByhoryu_kosho = getResources().getStringArray(
				R.array.items_info_list_sort_item_by_horyu_kosho);
		/*
		 * ContextThemeWrapper ctw = new ContextThemeWrapper(this,
		 * R.style.DialogTheme); AlertDialog.Builder dialogbuilder = new
		 * AlertDialog.Builder(ctw); // AlertDialog.Builder dialogbuilder = new
		 * AlertDialog.Builder(this);
		 *
		 * // set title
		 * dialogbuilder.setTitle(getString(R.string.items_info_sort_dialog_title
		 * )); dialogbuilder.setInverseBackgroundForced(true); if
		 * (searchMode.equalsIgnoreCase("7")) {
		 *
		 * dialogbuilder.setSingleChoiceItems(new ArrayAdapter<String>(this,
		 * android.R.layout.simple_list_item_single_choice, sortItemSeven), 0,
		 * onDialogItemClick); } else {
		 *
		 * dialogbuilder.setSingleChoiceItems(new ArrayAdapter<String>(this,
		 * android.R.layout.simple_list_item_single_choice, sortItemNotSeven),
		 * 0, onDialogItemClick); }
		 */

		AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);

		LayoutInflater inflater = getLayoutInflater();
		final View alertView = inflater.inflate(
				R.layout.items_info_sort_dialog, null);
		dialogbuilder.setView(alertView);

		TextView txtTitle = (TextView) alertView.findViewById(R.id.txtTitle);

		txtTitle.setText(getString(R.string.items_info_sort_dialog_title));

		ListView lv = (ListView) alertView.findViewById(R.id.alert_list);

//		if (searchMode == 7) {//
//			lv.setAdapter(new ArrayAdapter<String>(this,
//					R.layout.car_info_alert_dialog_items, sortItemSeven));
//		} else {

//		}
//		if ((searchMode == 2 || searchMode == 3) && !chkShowKosho.isChecked()) {
//
//		}
//		else{
//
//		}
//		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);



		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		if(id%2 == 1){
			if(!kakoFlag.equals("1")){
				sortItemNotSeven = Arrays.copyOfRange(sortItemNotSeven,0,9);
			}
			lv.setAdapter(new ArrayAdapter<String>(this,
					R.layout.items_info_sort_dialog_part, sortItemNotSeven));
			lv.setItemChecked(0, true);
			lv.setOnItemClickListener(onItemClick);
			dialogDetails = dialogbuilder.create();
			dialogDetails.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			return dialogDetails;
		}
		txtTitle.setText(getString(R.string.items_info_sort_horyu_kosho_dialog_title));
		lv.setAdapter(new ArrayAdapter<String>(this,
				R.layout.items_info_sort_dialog_part, sortItemByhoryu_kosho));

		lv.setItemChecked(2, true);

		lv.setOnItemClickListener(onItemClick);
		dialogDetails2 = dialogbuilder.create();
		dialogDetails2.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return dialogDetails2;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);
	}

	private int getMaxRowCount() {
//		return settings == null ? 30 : settings.getInt(
//				RealLuceAuctionApplication.SETTINGS_MAX_ROW_COUNT, 30);
		return 30;
	}

	private boolean getDisplayMoneyMode() {
		return settings == null ? true : settings.getBoolean(
				RealLuceAuctionApplication.SETTINGS_SHOW_PRICE, true);
	}

	/*
	 * Set title for screen.
	 */
	private void setScreenTitle() {
		String title = null;

		btnImageFilter.setVisibility(View.INVISIBLE);
		if (searchMode == 1) {
			title = this.getString(R.string.items_info_result_list_title);
		} else if (searchMode == 2) {
            title = this.getString(R.string.items_info_product_list_title);
			btnImageFilter.setVisibility(View.VISIBLE);
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

	/*
	 * Set visibility for controls.
	 */
	private void setVisiblilityControls(int mode) {
		btnNext.setVisibility(View.INVISIBLE);
		btnPrevious.setVisibility(View.INVISIBLE);
		lblPageNo.setVisibility(mode);
		lvItemInfoList.setVisibility(mode);
		if((searchMode != 2 && searchMode != 3) || chkShowKosho.isChecked()){
			btnKoshoType.setVisibility(View.GONE);
			btnSort2.setVisibility(View.GONE);
			btnSort.setVisibility(mode);
			lblItemCount2.setVisibility(View.VISIBLE);
			lblItemCount.setVisibility(View.GONE);
		}
		else{
			lblDisplayItems.setVisibility(mode);
			btnKoshoType.setVisibility(mode);
			btnSort2.setVisibility(mode);
			btnSort.setVisibility(View.GONE);
			lblItemCount.setVisibility(View.VISIBLE);
			lblItemCount2.setVisibility(View.GONE);
		}
		if (searchMode == 1) {
			lblTotalPrice.setVisibility(View.GONE);
			lblValue.setVisibility(View.GONE);
			lblPageNo.setVisibility(View.VISIBLE);
			dolMark.setVisibility(View.GONE);
			kaisaibiSearchLayout.setVisibility(View.GONE);
			if(cornerId.length() > 0){
				cornerCenterTxt.setVisibility(View.GONE);
				btnCornerCenterSelect.setVisibility(View.GONE);
			}
		} else if (searchMode == 2) {
//			lblTotalPrice.setVisibility(View.GONE);
//			lblValue.setVisibility(View.GONE);
//			lblPageNo.setVisibility(View.VISIBLE);
//			dolMark.setVisibility(View.GONE);
//			kaisaibiSearchLayout.setVisibility(View.GONE);
			lblTotalPrice.setVisibility(View.VISIBLE);
			lblValue.setVisibility(View.VISIBLE);
			lblPageNo.setVisibility(View.GONE);
			dolMark.setVisibility(View.GONE);
			kaisaibiSearchLayout.setVisibility(View.VISIBLE);
			cornerCenterTxt.setVisibility(View.GONE);
			btnCornerCenterSelect.setVisibility(View.GONE);
		} else if (searchMode == 3) {
			lblTotalPrice.setVisibility(View.VISIBLE);
			lblValue.setVisibility(View.VISIBLE);
			lblPageNo.setVisibility(View.GONE);
			dolMark.setVisibility(View.GONE);
			kaisaibiSearchLayout.setVisibility(View.VISIBLE);
			cornerCenterTxt.setVisibility(View.GONE);
			btnCornerCenterSelect.setVisibility(View.GONE);
		} else if (searchMode == 4) {
			lblTotalPrice.setVisibility(View.GONE);
			lblValue.setVisibility(View.GONE);
			lblPageNo.setVisibility(View.VISIBLE);
			dolMark.setVisibility(View.GONE);
			kaisaibiSearchLayout.setVisibility(View.GONE);

		} else if (searchMode == 5) {
			lblTotalPrice.setVisibility(View.GONE);
			lblValue.setVisibility(View.GONE);
			lblPageNo.setVisibility(View.VISIBLE);
			dolMark.setVisibility(View.GONE);
			kaisaibiSearchLayout.setVisibility(View.GONE);
		} else if (searchMode == 6) {
			lblTotalPrice.setVisibility(View.GONE);
			lblValue.setVisibility(View.GONE);
			lblPageNo.setVisibility(View.VISIBLE);
			dolMark.setVisibility(View.GONE);
			kaisaibiSearchLayout.setVisibility(View.GONE);
		} else if (searchMode == 7) {
			lblTotalPrice.setVisibility(View.GONE);
			lblValue.setVisibility(View.GONE);
			lblPageNo.setVisibility(View.VISIBLE);
			dolMark.setVisibility(View.GONE);
			kaisaibiSearchLayout.setVisibility(View.GONE);
		}
		if(kakoFlag.equals("1")){
			cornerCenterTxt.setVisibility(View.GONE);
			btnCornerCenterSelect.setVisibility(View.GONE);
		}
	}



	/**
	 * Reset value.
	 * */
	private void resetDefaultValueforSort() {
		sortId = "0";
		sortOrder = "0";
	}

	// ************************ BUSINESS LOGIC PROCESSING ****************
	/*
	 * Load list car info from server
	 */
	private void getItemList(int pageIndex, final boolean isRefreshList, final int scrollIndex) {
		HTTPService objService = new HTTPService(new HTTPServiceCallback() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSucessfully(Object returnData) {

				// Hide process dialog.
				hideProgressDialog();
				if(ItemInfo.Message.length() > 0){
					MessageBox.show(getActivityContext(),"",ItemInfo.Message);
					return;
				}
				int pos = 0;
				int y = 0;
				//Duongnp modified 2018/10/10
				if(isRefreshList){
					itemInfoList = (ArrayList<ItemInfo>) returnData;
				}
				else {
					// 現在のスクロール位置を記憶
					if(currentItemList.length() > 0 && !currentItemList.equals(ItemInfo.ItemLst)){
						MessageBox.show(getActivityContext(), "", getResources().getString(R.string.list_need_reload), new DialogInterface.OnDismissListener() {
							@Override
							public void onDismiss(DialogInterface dialogInterface) {
								currentItemList = "";
								getItemList(1,true,0);
							}
						});
						return;
					}
					pos = lvItemInfoList.getFirstVisiblePosition();
					y = lvItemInfoList.getChildAt(0).getTop();
					CarsItemAdapter carAdapter= (CarsItemAdapter) lvItemInfoList.getAdapter();
					itemInfoList =((ArrayList<ItemInfo>) carAdapter.getItemList());
					itemInfoList.addAll((ArrayList<ItemInfo>) returnData);
				}
				currentItemList = ItemInfo.ItemLst;

				// Set item to ListView.
				lvItemInfoList.setAdapter(new CarsItemAdapter(
						getApplicationContext(), itemInfoList));

				brandItemCount = ItemInfo.RecordCount;

				int pageNo = ItemInfo.PageNo;

				if (currentPage != pageNo) {
					currentPage = pageNo;
					if (currentPage < 1) {
						currentPage = 1;
					}
				}
				//lvItemInfoList.setSelectionFromTop(pos, y);
				if(scrollIndex >= 0)
					lvItemInfoList.setSelection(scrollIndex);
				// If itemInfoList is null, display message box.
				if (itemInfoList == null | itemInfoList.size() < 1) {

					lvItemInfoList.setVisibility(ListView.INVISIBLE);
					lblValue.setText("0");
					lblItemCount.setText("");
					lblItemCount2.setText("");
					// Show messageBox: items_info_message_title
					MessageBox.show(getActivityContext(),
							lblScreenTitle.getText().toString(),
							getActivityContext().getResources().getString(R.string.items_info_message_no_car));
				} else {

					// Set visible control.
					setVisiblilityControls(TextView.VISIBLE);

					// Call update pageNo label
					setPagePageNoLabel();
                    lblValue.setText(String.format("%,d",ItemInfo.TotalPrice)+getString(R.string.item_detail_yen_string));

					//Duongnp Modified
					// Set visible next control
//					int visibleMode = (currentPage < totalPage) ? Button.VISIBLE
//							: Button.INVISIBLE;
					btnNext.setVisibility(View.INVISIBLE);

					// set visible previous control.
//					visibleMode = (currentPage > 1) ? Button.VISIBLE
//							: Button.INVISIBLE;
					btnPrevious.setVisibility(View.INVISIBLE);
				}
				isScroll = true;
			}

			@Override
			public void onFailed(int errorCode) {

				// Hide process dialog.
				hideProgressDialog();

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

		showProgressDialog(R.string.please_waiting);

		/*
		 * objService.getItemList(searchMode, placeId, itemNo,
		 * RealLuceAuctionApplication.loginID, freeWord, cornerId, maxRowCount,
		 * pageIndex);
		 */
		if(searchMode == 2 || searchMode == 3 || searchMode == 4 || searchMode == 5){
			cornerId = "1";
		}
		objService.getItemList(String.valueOf( searchMode), placeId, itemNo, boxNo,boxNoTo,
				RealLuceAuctionApplication.loginID, freeWord, cornerId,
				maxRowCount, pageIndex, sortId, sortOrder, hasPicture,CurrentSessionNo,type,cornerList,subCatList,brandList,rankList,chkShowKosho.isChecked()?"1":"0",koshoQuery);
	}

	/*
	 * Calculate page number.
	 */
	private void setPagePageNoLabel() {
		int beginItem = (currentPage - 1) * maxRowCount + 1;
		int endItem = beginItem + maxRowCount - 1;

		//
		if (endItem > brandItemCount) {
			endItem = brandItemCount;
		}

		// If start item is greater than total Item, begin item is set to 1.
		if (beginItem > endItem) {
			beginItem = 1;
		}

		// Calculate total page. If brandItemCount is equal 0, total page is 1,
		// else (total item -1 / number item per page) + 1
		totalPage = (brandItemCount == 0) ? 1
				: ((brandItemCount - 1) / maxRowCount) + 1;

		String pageFormat;

		if (beginItem == endItem) {
			pageFormat = brandItemCount > 0 ? String.format("%s/%s %s", beginItem,
					brandItemCount, this.getString(R.string.count_items)) : "";
		} else {
			//Duongnp Modified 2018/10/10
			pageFormat = brandItemCount > 0 ? String.format("%s/%s %s",
					endItem, brandItemCount, this.getString(R.string.count_items)) : "";
		}

		lblPageNo.setText(pageFormat);
		lblItemCount.setText(pageFormat);
		lblItemCount2.setText(pageFormat);
	}

	// ************************** LISTENER *************************


    public OnClickListener btnImageFilterClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (hasPicture) {
                hasPicture = false;
                btnImageFilter.setImageResource(R.drawable.item_switch_nashi_button_style);
            }
            else
            {
                hasPicture = true;
                btnImageFilter.setImageResource(R.drawable.item_switch_ari_button_style);
            }
            getItemList(1,true,0);

        }
    };

	/**
	 * Button next
	 * */

	public OnClickListener btnNexClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// If current page is less than total page,
			// Increase currentPage value, call business process.
			if (currentPage < totalPage) {
				getItemList(currentPage + 1,true,0);
			}
		}
	};

	/*
	 * Set visibility for controls.
	 */
	public OnClickListener btnPreviousClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// If current page is greater than one page,
			// Decrease currentPage value, call business process.
			if (currentPage > 1) {
				getItemList(currentPage - 1,true,0);
			}
		}
	};


	/*
	 * Selected item on dialog.
	 */

	public OnItemClickListener onItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (dialogDetails != null && dialogDetails.isShowing()) {
				switch (arg2) {
				case 0:
					sortId = "0";
					sortOrder = "0";
					break;
				case 1:
					sortId = "1";
					sortOrder = "1";
					break;
				case 2:
					sortId = "1";
					sortOrder = "2";
					break;
				case 3:
					sortId = "2";
					sortOrder = "1";
					break;
				case 4:
					sortId = "2";
					sortOrder = "2";
					break;
				case 5:
					sortId = "3";
					sortOrder = "1";
					break;
				case 6:
					sortId = "3";
					sortOrder = "2";
					break;
					case 7:
						sortId = "4";
						sortOrder = "1";
						break;
					case 8:
						sortId = "4";
						sortOrder = "2";
						break;
					case 9:
						sortId = "5";
						sortOrder = "1";
						break;
					case 10:
						sortId = "5";
						sortOrder = "2";
						break;
				default:
					sortId = "0";
					sortOrder = "0";
				}
			}else if (dialogDetails2 != null && dialogDetails2.isShowing()) {
				// 保留交渉一覧の場合
				//sortId = "8";
				lblDisplayItems.setText(displayItemArray[arg2]);
				switch (arg2) {
					case 0:
						koshoQuery = "1";
						break;
//					case 1:
//						sortOrder = "4";
//						break;
					case 1:
						koshoQuery = "2";
						break;
					case 2:
						koshoQuery = "3";
						break;
//					case 2:
//						sortOrder = "3";
//						break;
					default:
						sortOrder = "0";
						break;
				}
			}
			else {
				switch (arg2) {
				case 0:
					sortId = "0";
					sortOrder = "0";
					break;
				case 1:
					sortId = "2";
					sortOrder = "1";
					break;
				case 2:
					sortId = "2";
					sortOrder = "2";
					break;
				case 3:
					sortId = "4";
					sortOrder = "1";
					break;
				case 4:
					sortId = "4";
					sortOrder = "2";
					break;
				case 5:
					sortId = "5";
					sortOrder = "1";
					break;
				case 6:
					sortId = "5";
					sortOrder = "2";
					break;

				case 7:
					sortId = "3";
					sortOrder = "1";
					break;
				case 8:
					sortId = "3";
					sortOrder = "2";
					break;
				default:
					sortId = "0";
					sortOrder = "0";
				}
			}

			currentPage = 1;
			getItemList(currentPage,true,0);
			if(dialogDetails!= null)dialogDetails.dismiss();
			if(dialogDetails2!= null)dialogDetails2.dismiss();
		}
	};

	/*
	 * public DialogInterface.OnClickListener onDialogItemClick = new
	 * DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) { if
	 * (!searchMode.equalsIgnoreCase("7")) { switch (which) { case 0: sortId =
	 * "0"; sortOrder = "0"; break; case 1: sortId = "1"; sortOrder = "1";
	 * break; case 2: sortId = "1"; sortOrder = "2"; break; case 3: sortId =
	 * "2"; sortOrder = "1"; break; case 4: sortId = "2"; sortOrder = "2";
	 * break; case 5: sortId = "3"; sortOrder = "1"; break; case 6: sortId =
	 * "3"; sortOrder = "2"; break; default: sortId = "0"; sortOrder = "0"; } }
	 * else { switch (which) { case 0: sortId = "0"; sortOrder = "0"; break;
	 * case 1: sortId = "2"; sortOrder = "1"; break; case 2: sortId = "2";
	 * sortOrder = "2"; break; case 3: sortId = "4"; sortOrder = "1"; break;
	 * case 4: sortId = "4"; sortOrder = "2"; break; case 5: sortId = "5";
	 * sortOrder = "1"; break; case 6: sortId = "5"; sortOrder = "2"; break;
	 * 
	 * case 7: sortId = "3"; sortOrder = "1"; break; case 8: sortId = "3";
	 * sortOrder = "2"; break; default: sortId = "0"; sortOrder = "0"; } }
	 * 
	 * getItemList(currentPage); dialog.dismiss(); } };
	 */

	// ************************** SUB CLASSES *************************
	/*
	 * Item adapter
	 */
	final class CarsItemAdapter extends BaseAdapter {

		private Context mContext;
		private List<ItemInfo> itemList;

		public CarsItemAdapter(Context context, List<ItemInfo> itemInfoList) {
			mContext = context;
			itemList = itemInfoList;
		}

		@Override
		public int getCount() {
			return itemInfoList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
		@Override
		public View getView(final int position, View convertView, final ViewGroup parent){

			// Get current itemInfor
			final ItemInfo itemInfo = itemList.get(position);

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.layout_items_brand_item,parent, false);
			}

			
			TextView lblBoxNo = getTextView(convertView, R.id.lblBoxNo);
			TextView lblResultName = getTextView(convertView,R.id.lblResultName);
			TextView lblState = getTextView(convertView,R.id.lblState);
			TextView lblLastPrice = getTextView(convertView, R.id.lblLastPrice);
			TextView lblItemName = getTextView(convertView, R.id.lblItemName);
			ImageButton btnTradeStatus =(ImageButton) convertView.findViewById(R.id.btnTradeStatus);
			btnTradeStatus.setOnClickListener(btnTradeStatusClick);
			TextView lblTradeStatus = getTextView(convertView,R.id.lblStatus);
			lblTradeStatus.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					negDialog.show(itemInfo.getBoxNo()+"-"+itemInfo.getBoxSubNo(),itemInfo.getFinalBidPrice(),itemInfo.getHoldPrice(),searchMode == 3?"0":itemInfo.getHoldFlg(),itemInfo.getReholdPrice());
				}
			});
			RoundRectCornerImageView imvImage1 =convertView.findViewById(R.id.imvImage1);

			final ImageButton okiniriBtn = convertView.findViewById(R.id.okiniriBtn);
			Button btnCorrection = convertView.findViewById(R.id.correctionBtn);
			btnCorrection.setText(getString(R.string.correction_btn));
			TextView txtFuzai_l = convertView.findViewById(R.id.txtFuzai_l);

			if(Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT){
				lblBoxNo.setTypeface(yGR);
				//lblBrandName.setTypeface(hiragino_kaku_w3);
				lblItemName.setTypeface(yGR);
				lblState.setTypeface(yGR);
				lblResultName.setTypeface(yGR);
				lblLastPrice.setTypeface(yGR);
				txtFuzai_l.setTypeface(yGR);
				btnCorrection.setTypeface(yGB);
			}

			if(itemInfo.getCorrection().length() > 0){
				btnCorrection.setVisibility(View.VISIBLE);
				btnCorrection.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						correctionDialog.show(itemInfo.getBoxNo()+"-"+itemInfo.getBoxSubNo(),itemInfo.getCorrection());
					}
				});
			}
			else{
				btnCorrection.setVisibility(View.GONE);
			}
			if (lblBoxNo != null)
				lblBoxNo.setText(itemInfo.getBoxNo()+"-"+itemInfo.getBoxSubNo());
			if(lblItemName!= null)
				lblItemName.setText(itemInfo.getBrand());
			if (lblLastPrice != null){
                String result = itemInfo.getResult();
				String lastPrice = itemInfo.getLastPrice();
//                if(searchMode == 1|| searchMode == 5){
//                	if(result.equals("オークション前")){
//                		lblLastPrice.setText("オークション前");
//                		lblLastPrice.setTextColor(getResources().getColor(R.color.mn_black));
//					}
//                	else if(result.equals("落札しました！")||result.equals("保留後落札")){
//						lblLastPrice.setText("落札しました！");
//						lblLastPrice.setTextColor(getResources().getColor(R.color.mn_red));
//					}
//                	else{
//                		if(kakoFlag.equals("1")){
//                			if(!lastPrice.equals(" - "))
//								lblLastPrice.setText("最終応札価格："+String.format("%,d",Long.parseLong(lastPrice))+"円");
//							else
//								lblLastPrice.setText("最終応札価格：-円");
//							lblLastPrice.setTextColor(getResources().getColor(R.color.mn_black));
//						}
//                		else{
//							lblLastPrice.setText("オークション終了");
//							lblLastPrice.setTextColor(getResources().getColor(R.color.mn_blue));
//						}
//					}
//				}
//                else{

                	if(lastPrice!= null&&lastPrice.length()>0){
                		if(searchMode==5||searchMode==1||searchMode==2||searchMode==3){
                			if(result.equals("落札しました！")||result.equals("保留後落札")){
								lblLastPrice.setText(((searchMode==5||searchMode==1)?getString(R.string.list_osatsu_kin):getString(R.string.list_raku_kin))+(lastPrice.equals(" - ")?lastPrice:String.format("%,d",Long.parseLong(lastPrice)))+getString(R.string.item_detail_yen_string));
								lblLastPrice.setTextColor(getResources().getColor(R.color.mn_red));
							}
                			else if(result.equals("オークション終了")){
								lblLastPrice.setText(((searchMode==5||searchMode==1)?getString(R.string.list_osatsu_kin):getString(R.string.list_horyu_kin))+(lastPrice.equals(" - ")?lastPrice:String.format("%,d",Long.parseLong(lastPrice)))+getString(R.string.item_detail_yen_string));
								lblLastPrice.setTextColor(getResources().getColor((searchMode==5||searchMode==1)?R.color.mn_black:R.color.mn_blue));
							}
                			else if(result.equals("引き")){
								lblLastPrice.setText(getString(R.string.list_hiki_txt));
								lblLastPrice.setTextColor(getResources().getColor(R.color.mn_black));
							}
							else if(result.equals("オークション前")){
								lblLastPrice.setText(getString(R.string.list_auction_mae));
								lblLastPrice.setTextColor(getResources().getColor(R.color.mn_black));
							}
						}
                		else if(searchMode == 4){
                			if(itemInfo.getAbsenceBidPrice()!=null && itemInfo.getAbsenceBidPrice().length() > 0)
							lblLastPrice.setText(String.format("%,d",Long.parseLong(itemInfo.getAbsenceBidPrice()))+getString(R.string.item_detail_yen_string));
							lblLastPrice.setTextColor(getResources().getColor(R.color.mn_black));
						}
					}
                	else{
                		if(result.equals("不在登録")){
                			if(itemInfo.getAbsenceBidPrice()!=null && itemInfo.getAbsenceBidPrice().length()>0){
								lblLastPrice.setText(String.format("%,d",Long.parseLong(itemInfo.getAbsenceBidPrice()))+getString(R.string.item_detail_yen_string));
								lblLastPrice.setTextColor(getResources().getColor(R.color.mn_black));
							}
                			else{
								lblLastPrice.setText(" - "+getString(R.string.item_detail_yen_string));
								lblLastPrice.setTextColor(getResources().getColor(R.color.mn_black));
							}
						}
                		else if(result.equals("引き")){
							lblLastPrice.setText(getString(R.string.list_hiki_txt));
							lblLastPrice.setTextColor(getResources().getColor(R.color.mn_black));
						}
						else if(result.equals("オークション前")){
							lblLastPrice.setText(getString(R.string.list_auction_mae));
							lblLastPrice.setTextColor(getResources().getColor(R.color.mn_black));
						}
                		else{
							lblLastPrice.setText(" -"+getString(R.string.item_detail_yen_string));
							lblLastPrice.setTextColor(getResources().getColor(R.color.mn_black));
						}
					}
				}
//            }


			if((searchMode == 1||searchMode == 5) && !kakoFlag.equals("1")
					&& itemInfo.getAbsenceBidPrice()!=null && itemInfo.getAbsenceBidPrice().length()>0
					&& !itemInfo.getAbsenceBidPrice().equals("0")){
				txtFuzai_l.setVisibility(View.VISIBLE);
				txtFuzai_l.setText(String.format(getResources().getString(R.string.list_fuzai_t),String.format("%,d",Long.parseLong(itemInfo.getAbsenceBidPrice()))));
			}
			else{
				txtFuzai_l.setVisibility(View.GONE);
			}
			if(kakoFlag.equals("1")){
				lblState.setText(itemInfo.getBidDate());
			}
			else{
				try{
					lblState.setText(String.format("%,d",Long.parseLong(itemInfo.getStartPrice())) + getString(R.string.item_detail_yen_string));
				}
				catch (Exception ex){
					lblState.setText("0"+getString(R.string.item_detail_yen_string));
				}
			}


			if(itemInfo.getImage1() != null) {
				imvImage1.setImageBitmap(BitmapFactory.decodeByteArray(Utilities.rotaledByteArr(itemInfo.getImage1()), 0, Utilities.rotaledByteArr(itemInfo.getImage1()).length));
			}else{
				imvImage1.setImageResource(R.drawable.auction_no_photo);
			}

			lblResultName.setText(itemInfo.getSerialNo());
			lblResultName.setTextIsSelectable(true);
			if(itemInfo.getOkiniriFlg()!=null && itemInfo.getOkiniriFlg().equals("1")){
				okiniriBtn.setImageResource(R.drawable.okiniri_chk);
			}
			else{
				okiniriBtn.setImageResource(R.drawable.okiniri);
			}

			okiniriBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					HTTPService httpService = new HTTPService(new HTTPServiceCallback() {
						@Override
						public void onSucessfully(Object returnData) {
							hideProgressDialog();
						}

						@Override
						public void onFailed(int errorCode) {
							hideProgressDialog();
						}
					});
					String flgToUpdate = "";
					if(itemInfo.getOkiniriFlg().equals("1")){
						itemInfo.setOkiniriFlg("0");
						okiniriBtn.setImageResource(R.drawable.okiniri);
						flgToUpdate = "1";
					}else{
						itemInfo.setOkiniriFlg("1");
						okiniriBtn.setImageResource(R.drawable.okiniri_chk);
						flgToUpdate = "0";
					}

					showProgressDialog(R.string.please_waiting);
					httpService.updateOkiniri(RealLuceAuctionApplication.allPlaces.get(0).getIdNo(),RealLuceAuctionApplication.systemInfo.getSessionList().get(0),
							Integer.toString(itemInfo.getItemNo()) ,
							RealLuceAuctionApplication.loginID,flgToUpdate);
				}
			});
			if(!itemInfo.getTimesNo().equals(RealLuceAuctionApplication.allPlaces.get(0).getTimesNo())){
			    okiniriBtn.setVisibility(View.GONE);
            }
			else{
			    okiniriBtn.setVisibility(View.VISIBLE);
            }
			if(itemInfo.getSellingResultBK().equals("1")){
				if(itemInfo.getSellingResult().equals("1")){
					String holdPrice = itemInfo.getHoldPrice();
					String reholdPrice = itemInfo.getReholdPrice();
					if(searchMode == 2){
						if(holdPrice == null || holdPrice.equals("") || holdPrice.equals("0")){
							btnTradeStatus.setImageDrawable(getActivityContext().getResources().getDrawable(R.drawable.item_koshosuru_button_style));
							btnTradeStatus.setVisibility(View.VISIBLE);
							lblTradeStatus.setVisibility(View.GONE);
						}
						else{
							if(reholdPrice == null || reholdPrice.equals("")||reholdPrice.equals("0")){
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
					else if(searchMode == 3){
						btnTradeStatus.setVisibility(View.VISIBLE);
						if(holdPrice == null || holdPrice.equals("") || holdPrice.equals("0")){
							lblTradeStatus.setTextColor(Color.parseColor("#0066ff"));
							lblTradeStatus.setBackground(getResources().getDrawable(R.drawable.title_koshoblue));
							lblTradeStatus.setText(getString(R.string.list_Horyu_kosho_mae));
							lblTradeStatus.setVisibility(View.VISIBLE);
							btnTradeStatus.setVisibility(View.GONE);
						}
						else{
							if(reholdPrice == null || reholdPrice.equals("")||reholdPrice.equals("0")){
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
					else{
						btnTradeStatus.setVisibility(View.GONE);
						lblTradeStatus.setVisibility(View.GONE);
					}
				}
				else if(itemInfo.getSellingResult().equals("2")){
					btnTradeStatus.setVisibility(View.GONE);
					lblTradeStatus.setVisibility(View.VISIBLE);
					lblTradeStatus.setTextColor(Color.parseColor("#ff6600"));
					lblTradeStatus.setBackground(getResources().getDrawable(R.drawable.title_koshoorange));
					lblTradeStatus.setText(getString(R.string.list_Horyu_kosho_hiki));
				}
				else if(itemInfo.getSellingResult().equals("6")){
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
			btnTradeStatus.setTag(position);
			convertView.setTag(position);
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					RealLuceAuctionApplication.currentItemOkiniri = itemInfoList.get(position).getOkiniriFlg();
					// Get current row selected.
					//int position = (Integer) v.getTag();

					setActivityParam("ItemList", ItemInfo.ItemLst);
					setActivityParam("PlaceList", ItemInfo.PlaceLst);
					setActivityParam("SessionList", ItemInfo.SessionLst);

					// Set Current position into activity parameter.
					setActivityParam("DisplayIndex", position);
					currentPositon = position;
					setActivityParam("SearchMode", searchMode);
					// Set animation
					setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);

					// Call car detail activity.
					showActivity(ItemDetailActivity.class);
				}
			});
			return convertView;
		}
		public OnClickListener btnTradeStatusClick = new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Get current row selected.
				int position = (Integer) v.getTag();
				currentKoshoIndex = lvItemInfoList.getFirstVisiblePosition();
				// Set activity parameter.
				setActivityParam("SearchMode", searchMode);
				setActivityParam("ItemInfo", itemList.get(position));
				setActivityParam("SortOrder", sortOrder);

				// Set animation
				setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);

				// Call car detail activity.
				showActivity(RegisterNegotiationActivity.class);
			}
		};


		public List<ItemInfo> getItemList(){
			return this.itemList;
		}

		private TextView getTextView(View convertview, int resourceId) {
			TextView textView = (TextView) convertview.findViewById(resourceId);

			// If textView is null, return null;
			if (textView == null)
				return null;

			return textView;
		}
		

		private AutoFitTextView getAutoFitTextView(View convertview, int resourceId) {
			AutoFitTextView textView = (AutoFitTextView) convertview.findViewById(resourceId);

			// If textView is null, return null;
			if (textView == null)
				return null;

			return textView;
		}

		private void setGreyColorBackground(TextView textview) {
			// textview.setBackgroundResource(R.color.items_info_background_grey_color_label);
			if (textview != null)
				textview.setBackgroundColor(Color.rgb(255, 236, 236));
		}

		private void setWhiteColorBackground(TextView textView) {
			// textView(R.color.items_info_background__white_color_label);
			if (textView != null)
				textView.setBackgroundColor(Color.WHITE);
		}

		// **************LISTENER OF SUBCLASS*************************
		public OnClickListener onRowClick = new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Get current row selected.
				int position = (Integer) v.getTag();

				// Set itemList into activity parameter.
				setActivityParam("ItemList", ItemInfo.ItemLst);
				setActivityParam("PlaceList", ItemInfo.PlaceLst);
				setActivityParam("SessionList", ItemInfo.SessionLst);

				// Set Current position into activity parameter.
				setActivityParam("DisplayIndex", position);

				setActivityParam("SearchMode", searchMode);
				// Set animation
				setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);

				// Call car detail activity.
				showActivity(ItemDetailActivity.class);
			}
		};
	}

}
