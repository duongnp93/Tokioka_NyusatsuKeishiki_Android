package tokiokaability.auction.activities;

import java.util.ArrayList;
import java.util.List;

import tokiokaability.auction.R;
import tokiokaability.auction.RealLuceAuctionApplication;
import tokiokaability.auction.HTTPInfo.HTTPService;
import tokiokaability.auction.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction.HTTPInfo.ManufacturerInfo;
import tokiokaability.auction.HTTPInfo.PlaceInfo;
import tokiokaability.auction.commons.MessageBox;
import tokiokaability.auction.view.PlacesView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ManufacturersActivity extends BaseActivity {

	// Private variable

    private List<PlaceInfo> placeInfoList = null;
    private PlacesView blockPlace = null;

	private ListView lvMakerList;
	private TextView lblScreenTitle;

	private List<ManufacturerInfo> manufacturerList;

	private static String CON_MANUFACTURER_PLACEID = "ManufacturerPlaceID";
	private static String CON_MANUFACTURER_ONE_PRICE_FLG = "ManufacturerOnePriceFlg";
	
	private int searchMode = 0;
	private int mode = 0;
	private static PlaceInfo statePlace = null;
	private String onePriceFlg = "";
	
	// ************************ SCREEN LIFECYCLE ******************

	/*
	 * Initialization
	 */
	@Override
	protected void initialization() {
		setContentView(R.layout.activity_manufacturers);
		super.initialization();

		// Initialize data and control
		manufacturerList = new ArrayList<ManufacturerInfo>();

        //Get widgets
        blockPlace = (PlacesView) this.findViewById(R.id.blockPlace);
		lvMakerList = (ListView) findViewById(R.id.lvMakerList);
		lblScreenTitle = (TextView) findViewById(R.id.lblScreenTitle);

		// invisible list
        blockPlace.setVisibility(View.INVISIBLE);
		lvMakerList.setVisibility(ListView.INVISIBLE);


		// Getting searchMode
		searchMode = (Integer) getActivityParam("SearchMode", 1);
		mode = (Integer)getActivityParam("Mode", 4);
		onePriceFlg = (String)getActivityParam("OnePriceFlg", "0");
		
        if (searchMode == 7){
        	lblScreenTitle.setText(R.string.manufacturer_title_7);
        }
        else{
        	lblScreenTitle.setText(R.string.manufacturer_title);
        }    		
		
		statePlace = null;
        //Load data
        loadPlaces();
        
        
        lvMakerList.setOnItemClickListener(listViewOnItemClick);
	}

	
	public OnItemClickListener listViewOnItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (validateToSearch()) {
				directToItemNameActivity(position);
			}
		}
		
	};
	
	private void directToItemNameActivity(int position) {
		setActivityParam("MakeInfo", manufacturerList.get(position));

		setActivityParam("PlaceInfo", statePlace);
		setActivityParam("Mode",mode);
		setActivityParam("SearchMode",searchMode);

		// Set animation
		setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);

		// Transfer to carName screen.
		showActivity(ItemNameActivity.class);
	}
	
    @Override
    public void onChildActivityClosed(BaseActivity fromActivity) {
        super.onChildActivityClosed(fromActivity);
        PlaceInfo placeInfo = (PlaceInfo) getActivityParam("PlaceInfo");
        if (placeInfo != null) {
            if(placeInfoList != null && placeInfoList.size() > 0){
                for(int i=0; i<placeInfoList.size(); i++){
                    PlaceInfo item = placeInfoList.get(i);
                    if(item.getIdNo().equalsIgnoreCase(placeInfo.getIdNo()) && item.getOnePriceFlg().equalsIgnoreCase(placeInfo.getOnePriceFlg())) {
                    	statePlace = placeInfo;
                    	blockPlace.setSelectedIndex(i);
                    	onePriceFlg = statePlace.getOnePriceFlg();
                    	loadManufacturerList();
                        break;
                    }
                }
            }

        }
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
                    blockPlace.setVisibility(View.VISIBLE);
                    blockPlace.setPlaces((ArrayList<PlaceInfo>) placeInfoList);
                    blockPlace.setSelectedIndex(getPlaceInfoIndex(-1));
                    blockPlace.setOnPlaceSelectedListener(new PlacesView.OnPlaceSelectedListener() {
                        @Override
                        public void onPlaceSelected(int index) {
                            statePlace = placeInfoList.get(index);
                            onePriceFlg = statePlace.getOnePriceFlg();
                            setPlaceInfoIndex(statePlace);
                            loadManufacturerList();
                        }
                    });

                    if (blockPlace.getSelectedIndex() >= 0 && blockPlace.getSelectedIndex() < placeInfoList.size()){
                    	statePlace = placeInfoList.get(blockPlace.getSelectedIndex());
                        loadManufacturerList();
                    }
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

        objService.getPlacesByLoginID(RealLuceAuctionApplication.loginID,mode);
    }


	// ************************ VALIDATION ***************************
	/*
	 * Validate searching
	 */
	private boolean validateToSearch() {
		if (statePlace == null || statePlace.getIdNo() == null
				|| statePlace.getIdNo().length() <= 0) {
			MessageBox.show(getActivityContext(),
					R.string.manufacturer_search_message_invalidated_title,
					R.string.manufacturer_search_message_no_place);
			return false;
		}

		return true;

	}


	// ************************ BUSINESS LOGIC PROCESSING ****************

	/*
	 * Load manufacturer list info from server
	 */
	private void loadManufacturerList() {

		HTTPService objService = new HTTPService(new HTTPServiceCallback() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSucessfully(Object returnData) {

				hideProgressDialog();

				manufacturerList = (List<ManufacturerInfo>) returnData;
				lvMakerList.setAdapter(new ManufacturersItemAdapter(
						getActivityContext(), manufacturerList));

				if (manufacturerList == null || manufacturerList.size() <= 0) {
					lvMakerList.setVisibility(ListView.INVISIBLE);
					MessageBox
							.show(getActivityContext(),
									R.string.manufacturer_message_no_manufacturer_title,
									R.string.manufacturer_search_message_no_manufacturer);
				} else {
					lvMakerList.setVisibility(ListView.VISIBLE);
				}
			}

			@Override
			public void onFailed(int errorCode) {
				hideProgressDialog();
				lvMakerList.setVisibility(ListView.INVISIBLE);
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
				default:
					MessageBox.show(getActivityContext(),
							R.string.error_title_webservice,
							R.string.error_message_unknow);
					break;

				}
			}

		});
		showProgressDialog(R.string.please_waiting);

		// Call service.
		objService.getManufactureList(statePlace.getIdNo(),RealLuceAuctionApplication.loginID, String.valueOf(searchMode), onePriceFlg);
	}

    /*
     * Get index of PlaceInfo on last login
     */
    private int getPlaceInfoIndex(int defaultValue){
        SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS, MODE_PRIVATE);

        String placeId;
        
        if (searchMode == 7){
        	placeId =  settings.getString(CON_MANUFACTURER_PLACEID, "");
        	onePriceFlg =  settings.getString(CON_MANUFACTURER_ONE_PRICE_FLG, "0");
        }
        else{
        	placeId =  (String) getActivityParam("PlaceId");
        	onePriceFlg =  (String) getActivityParam("OnePriceFlg");
        }
    	if(((placeId == null) || (placeId.length() == 0)) && !(placeInfoList == null || placeInfoList.isEmpty())){
    		statePlace = placeInfoList.get(0);
        	placeId =statePlace.getIdNo();
        	onePriceFlg =  statePlace.getOnePriceFlg();
        	setPlaceInfoIndex(statePlace);
        }
        for (int i = 0; i<placeInfoList.size(); i++){
            if(placeId.equalsIgnoreCase(placeInfoList.get(i).getIdNo()) && onePriceFlg.equalsIgnoreCase(placeInfoList.get(i).getOnePriceFlg())){
                return i;
            }
        }

        if(defaultValue == -1){
        	statePlace = placeInfoList.get(0);
        	placeId =statePlace.getIdNo();
        	onePriceFlg =  statePlace.getOnePriceFlg();
        	setPlaceInfoIndex(statePlace);
        	defaultValue = 0;
        }
        return defaultValue;
    }

   
    private void setPlaceInfoIndex(PlaceInfo placeInfo){
    	if (searchMode == 7){
	        SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS, MODE_PRIVATE);
	        SharedPreferences.Editor editor = settings.edit();
	        editor.putString(CON_MANUFACTURER_PLACEID, placeInfo.getIdNo());
	        editor.putString(CON_MANUFACTURER_ONE_PRICE_FLG, placeInfo.getOnePriceFlg());
	        editor.commit();
    	}
    }

    

	// ************************ LISTENERS ********************************

	// ************************** SUB CLASSES *************************
	/**
	 * Manufacture item adapter
	 */

	public class ManufacturersItemAdapter extends BaseAdapter {

		private Context mContext;
		private List<ManufacturerInfo> makerList;

		public ManufacturersItemAdapter(Context context,
				List<ManufacturerInfo> makerItems) {
			mContext = context;
			this.makerList = makerItems;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = inflater.inflate(
						R.layout.layout_manufacturers_manufacturer_item,
						parent, false);
			}

			RelativeLayout blockMaker = (RelativeLayout) convertView
					.findViewById(R.id.blockMaker);

			// set back ground color.
			if (position % 2 > 0) {
				blockMaker.setBackgroundColor(Color.rgb(227, 224, 237));
			} else {
				blockMaker.setBackgroundColor(Color.WHITE);
			}

			// Get manufacturer.
			ManufacturerInfo manufacturerInfo = makerList.get(position);

			// set data for cotrol.
			// Manufacture name
			TextView lblMakerName = (TextView) convertView
					.findViewById(R.id.lblMakerName);

			lblMakerName.setText(manufacturerInfo.getManufacturerName());

			// Count
			TextView lblCount = (TextView) convertView
					.findViewById(R.id.lblCount);

			lblCount.setText(String.format(
					getString(R.string.manufacturer_format_count_label),
					manufacturerInfo.getCount()));

			convertView.setTag(Integer.valueOf(position));
			//convertView.setOnClickListener(rowClick);

			return convertView;
		}

		@Override
		public int getCount() {
			return makerList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public OnClickListener rowClick = new OnClickListener() {
			public void onClick(View v) {
				if (validateToSearch()) {
					directToItemNameActivity((Integer) v.getTag());
				}
			}
		};

		private void directToItemNameActivity(int position) {
			setActivityParam("MakeInfo", makerList.get(position));

			setActivityParam("PlaceInfo", statePlace);
			setActivityParam("Mode",mode);
			setActivityParam("SearchMode",searchMode);

			// Set animation
			setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);

			// Transfer to carName screen.
			showActivity(ItemNameActivity.class);
		}

	}
}
