package tokiokaability.auction24.activities;

import tokiokaability.auction24.HTTPInfo.HTTPService;
import tokiokaability.auction24.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction24.R;
import tokiokaability.auction24.HTTPInfo.PlaceInfo;
import tokiokaability.auction24.RealLuceAuctionApplication;
import tokiokaability.auction24.commons.MessageBox;
import tokiokaability.auction24.view.PlacesView;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFreewordActivity extends BaseActivity {

    private List<PlaceInfo> placeInfoList = null;
    private PlacesView blockPlace = null;

	private EditText txtFreeword;
	private ImageButton btnSearch;
	private TextView lblScreenTitle;

	private static String CON_FREEWORD_SEARCH_PLACEID = "FreewordSearchPlaceID";
	private static String CON_FREEWORD_SEARCH_ONE_PRICE_FLG = "FreewordSearchOnePriceFlg";
	
	/*
	 * This static member is to save screen state
	 */
	private static String stateFreeword = "";
	private static PlaceInfo statePlace = null;

    private int searchMode = 0;
    private int mode = 0;
	private String onePriceFlg = "0";
	private ListView dropdownList = null;
	private TextView dropdownTxt = null;
	private ImageButton dropdownBtn = null;
	private RelativeLayout backGround;
	
	
	//************************ SCREEN LIFECYCLE ******************
	/*
	 * Initialization
	 */
	@Override
	protected void initialization(){
		setContentView(R.layout.activity_search_freeword);
		super.initialization();
		
        // Get search mode.
        searchMode = (Integer)getActivityParam("SearchMode", 1);
        mode = (Integer) getActivityParam("Mode", 1);


        //Get widgets
        //blockPlace = (PlacesView) this.findViewById(R.id.blockPlace);
        txtFreeword = (EditText) this.findViewById(R.id.txtFreeword);
        btnSearch =  (ImageButton) this.findViewById(R.id.btnSearch);

        //Set state
        //blockPlace.setVisibility(View.VISIBLE);
        txtFreeword.setText(stateFreeword);

        lblScreenTitle = (TextView) findViewById(R.id.lblScreenTitle);
        
        //Set OnClick OnClickListener
        btnSearch.setOnClickListener(btnSearchClick);
        txtFreeword.setOnEditorActionListener(txtFreewordNoAction);
		dropdownBtn = (ImageButton)findViewById(R.id.dropdownBtn);
		dropdownList = (ListView)findViewById(R.id.dropdownList);
		dropdownTxt = (TextView) findViewById(R.id.dropdownTxt);
		dropdownTxt.setOnClickListener(btnDropdownClick);
		backGround = (RelativeLayout) findViewById(R.id.background);
		backGround.setOnClickListener(backgroundClick);
		final ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,R.layout.simple_spinner_row,RealLuceAuctionApplication.systemInfo.getTypeList());
		dropdownList.setAdapter(typeAdapter);

		dropdownList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				view.setSelected(true);
				dropdownTxt.setText(typeAdapter.getItem(position));
				dropdownList.setVisibility(View.GONE);
				dropdownBtn.setImageResource(R.drawable.spinner_arr_down);
			}
		});

		dropdownList.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return event.getAction() == MotionEvent.ACTION_MOVE;
			}
		});

		dropdownBtn.setOnClickListener(btnDropdownClick);
        
//        if (searchMode == 7){
//        	setHelpResourceId(R.drawable.help_content_freeword_search_7);
//        	lblScreenTitle.setPadding(4, 0,0,0);
//        	lblScreenTitle.setTextSize(19);
//        	lblScreenTitle.setText(R.string.freeword_search_screen_title_7);
//        }
//        else{
//        	//setHelpResourceId(R.drawable.help_content_freeword_search);
//			setHelpResourceId(R.layout.type_search_help);
//        	lblScreenTitle.setText(R.string.freeword_search_screen_title);
//        }

        
        statePlace = null;
        if(Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT)setFont();
        //Load data
        loadPlaces();
        dropdownTxt.setText(typeAdapter.getItem(0));
        txtFreeword.setText("");
	}

	private OnClickListener btnDropdownClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(dropdownList.getVisibility() == View.GONE){
				dropdownList.setVisibility(View.VISIBLE);
				dropdownBtn.setImageResource(R.drawable.spinner_arr_up);
			}
			else{
				dropdownList.setVisibility(View.GONE);
				dropdownBtn.setImageResource(R.drawable.spinner_arr_down);
			}
		}
	};

	private  void setFont(){
		AssetManager am = getApplicationContext().getAssets();
		Typeface hiragino_kakugo_w6 = Typeface.createFromAsset(am,"fonts/hiragino_kakugo_w6.otf");
		Typeface hiragino_kakugo_w3 = Typeface.createFromAsset(am,"fonts/hiragino_kakugo_w3.otf");
		lblScreenTitle.setTypeface(hiragino_kakugo_w6);
		dropdownTxt.setTypeface(hiragino_kakugo_w3);
		txtFreeword.setTypeface(hiragino_kakugo_w3);
	}

	private OnClickListener backgroundClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(getActivityContext().getCurrentFocus().getWindowToken(),0);
		}
	};

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
					/*
                    blockPlace.setVisibility(View.VISIBLE);
                    blockPlace.setPlaces((ArrayList<PlaceInfo>) placeInfoList);
                    blockPlace.setSelectedIndex(getPlaceInfoIndex(-1));
                    blockPlace.setOnPlaceSelectedListener(new PlacesView.OnPlaceSelectedListener() {
                        @Override
                        public void onPlaceSelected(int index) {
                        	statePlace = placeInfoList.get(index);
                        	onePriceFlg = statePlace.getOnePriceFlg();
                        	setPlaceInfoIndex(statePlace);
                        }
                    });


                    if (blockPlace.getSelectedIndex() >= 0 && blockPlace.getSelectedIndex() < placeInfoList.size()){
                    	statePlace = placeInfoList.get(blockPlace.getSelectedIndex());
                    }
                    */
					statePlace = placeInfoList.get(0);
					btnSearch.setEnabled(statePlace.getOutsideOpenFlag().equals("1"));
					dropdownBtn.setEnabled(statePlace.getOutsideOpenFlag().equals("1"));
					dropdownTxt.setClickable(statePlace.getOutsideOpenFlag().equals("1"));
					//txtFreeword.setFocusable(statePlace.getOutsideOpenFlag().equals("1"));
                }
                
                hideProgressDialog();
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

        objService.getPlacesByLoginID(RealLuceAuctionApplication.loginID, mode);
    }
	
	
	//************************ VALIDATION ***************************
	/*
	 * Validate searching
	 */
	private boolean validateToSearch(){
		 if(statePlace == null || statePlace.getIdNo() == null  || statePlace.getIdNo().length() <= 0){
			 MessageBox.show(getActivityContext(), R.string.freeword_search_message_invalidated_title, R.string.freeword_search_message_no_place);
			 return false;
		 }
		 
		 if(txtFreeword.getText().toString() == null || txtFreeword.getText().toString().length() <= 0){
			 txtFreeword.requestFocus();
			 MessageBox.show(getActivityContext(), R.string.freeword_search_message_invalidated_title, R.string.freeword_search_message_no_freeword);
			 return false;
		 }
		 
		 return true;
		
	}

	
	//************************ BUSINESS LOGIC PROCESSING ****************
	/*
	 * Search product
	 */
	private void search(){
		
		stateFreeword = txtFreeword.getText().toString();
		setActivityParam("SearchMode",searchMode);
		setActivityParam("Mode", mode);
		setActivityParam("PlaceID",statePlace.getIdNo());
		setActivityParam("PlaceName",statePlace.getName());
		setActivityParam("ItemNo","");
		setActivityParam("Freeword",stateFreeword);
		setActivityParam("CornerId","");
		setActivityParam("BoxNo","");
		setActivityParam("Type",dropdownTxt.getText());
		setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
		
		showActivity(ItemsActivity.class);

	}

    /*
     * Get index of PlaceInfo on last login
     */
    private int getPlaceInfoIndex(int defaultValue){
        SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS, MODE_PRIVATE);

        String placeId;
        
        if (searchMode == 7){
        	placeId =  settings.getString(CON_FREEWORD_SEARCH_PLACEID, "");
        	onePriceFlg =  settings.getString(CON_FREEWORD_SEARCH_ONE_PRICE_FLG, "");
        }
        else{
        	placeId =  (String) getActivityParam("PlaceId");
        	onePriceFlg =  (String) getActivityParam("OnePriceFlg");
        }
    	if(((placeId == null) || (placeId.length() == 0)) && !(placeInfoList == null || placeInfoList.isEmpty())){
    		statePlace = placeInfoList.get(0);
    		placeId = placeInfoList.get(0).getIdNo();
        	onePriceFlg =  placeInfoList.get(0).getOnePriceFlg();
        	if(searchMode == 7){
        		setPlaceInfoIndex(placeInfoList.get(0));
        	}
        }
        for (int i = 0; i<placeInfoList.size(); i++){
            if(placeId.equalsIgnoreCase(placeInfoList.get(i).getIdNo()) & onePriceFlg.equalsIgnoreCase(placeInfoList.get(i).getOnePriceFlg())){
                return i;
            }
        }

    	if(defaultValue == -1){
    		statePlace = placeInfoList.get(0);
    		placeId = placeInfoList.get(0).getIdNo();
        	onePriceFlg =  placeInfoList.get(0).getOnePriceFlg();
        	if(searchMode == 7){
        		setPlaceInfoIndex(placeInfoList.get(0));
        	}
        	defaultValue = 0;
        }
        return defaultValue;
    }

   
    private void setPlaceInfoIndex(PlaceInfo placeInfo){
    	if (searchMode == 7){
	        SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS, MODE_PRIVATE);
	        SharedPreferences.Editor editor = settings.edit();
	        editor.putString(CON_FREEWORD_SEARCH_PLACEID, placeInfo.getIdNo());
	        editor.putString(CON_FREEWORD_SEARCH_ONE_PRICE_FLG, placeInfo.getOnePriceFlg());
	        editor.commit();
    	}
    }


   
	
	
	//************************ LISTENERS ********************************
	private OnClickListener btnSearchClick = new OnClickListener() {
    	public void onClick(View v) {
    		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(txtFreeword.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    		
    		if (validateToSearch()){
    			search();
    		}
       	}
	};
	
	private OnEditorActionListener txtFreewordNoAction = new OnEditorActionListener(){

		@Override
		public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
    		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(txtFreeword.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    		
    		if (validateToSearch()){
    			search();
    		}
			return true;
		}
		
	};
}
