package tokiokaability.auction.activities;

import java.util.ArrayList;
import java.util.List;

import tokiokaability.auction.R;
import tokiokaability.auction.RealLuceAuctionApplication;
import tokiokaability.auction.HTTPInfo.CarNameInfo;
import tokiokaability.auction.HTTPInfo.HTTPService;
import tokiokaability.auction.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction.HTTPInfo.ManufacturerInfo;
import tokiokaability.auction.HTTPInfo.PlaceInfo;
import tokiokaability.auction.commons.MessageBox;
import tokiokaability.auction.view.PlacesView;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItemNameActivity extends BaseActivity {

    private List<PlaceInfo> placeInfoList = null;
    private PlacesView blockPlace = null;

	private PlaceInfo placeInfo = null;

	private TextView lblMakerName;
	private TextView lblScreenTitle;

	private ListView lvCarNameList;
	private List<CarNameInfo> carNameList;

	private ManufacturerInfo makerItem;

	private int searchMode = 0;
	private int mode = 4;

	// ************************ SCREEN LIFECYCLE ******************

	/*
	 * Initialization
	 */
	@Override
	protected void initialization() {
		setContentView(R.layout.activity_item_name);
		super.initialization();

		// Intialize data.
		carNameList = new ArrayList<CarNameInfo>();

		// Get control.
        blockPlace = (PlacesView) this.findViewById(R.id.blockPlace);
		lblMakerName = (TextView) findViewById(R.id.lblMakerName);
		lvCarNameList = (ListView) findViewById(R.id.lvCarNameList);

		// Get search mode.
		searchMode = (Integer)  getActivityParam("SearchMode", 1);
		mode = (Integer) getActivityParam("Mode", 4);

		// Get maker
		makerItem = (ManufacturerInfo) getActivityParam("MakeInfo");

		// set value for maker label
		lblMakerName.setText(makerItem.getManufacturerName());
		lblScreenTitle = (TextView) findViewById(R.id.lblScreenTitle);

        // Invisible listview.
        blockPlace.setVisibility(View.INVISIBLE);
        lvCarNameList.setVisibility(ListView.INVISIBLE);

        if (searchMode == 7){
        	lblScreenTitle.setText(R.string.car_name_title_7);
        }
        else{
        	lblScreenTitle.setText(R.string.car_name_title);
        }        
        
        //Load data
        loadPlaces();

        lvCarNameList.setOnItemClickListener(listViewOnItemClick);

	}
	
	public OnItemClickListener listViewOnItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (validateToSearch()) {
				directToCarListActivity(position);
			}
		}
		
	};
	
	private void directToCarListActivity(int position) {

		setActivityParam("SearchMode", searchMode);
		setActivityParam("Mode", mode);
		setActivityParam("PlaceID", placeInfo.getIdNo());
		setActivityParam("ItemNo", "");
		setActivityParam("Freeword", "");
		setActivityParam("CornerId", "");
		setActivityParam("MakerID", makerItem.getManufacturerId());
		setActivityParam("CarName", carNameList.get(position).getCarName());
		setActivityParam("PlaceName", placeInfo.getName());
		setActivityParam("OnePriceFlg", placeInfo.getOnePriceFlg());
		setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);

		showActivity(ItemsActivity.class);
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

                    // if place info is null, selected index.
                    if (placeInfo == null) {
                        placeInfo = (PlaceInfo) getActivityParam("PlaceInfo");
                        if(placeInfoList != null && placeInfoList.size() > 0){
                            for(int i=0; i<placeInfoList.size(); i++){
                                PlaceInfo item = placeInfoList.get(i);
                                if(item.getIdNo().equalsIgnoreCase(placeInfo.getIdNo()) && item.getOnePriceFlg().equalsIgnoreCase(placeInfo.getOnePriceFlg())) {
                                    blockPlace.setSelectedIndex(i);
                                    break;
                                }
                            }
                        }
                        loadCarNameList();
                    }

                    
                    blockPlace.setOnPlaceSelectedListener(new PlacesView.OnPlaceSelectedListener() {
                        @Override
                        public void onPlaceSelected(int index) {
                            placeInfo = placeInfoList.get(index);
                            setActivityParam("PlaceInfo", placeInfo);
                            loadCarNameList();
                        }
                    });

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

        objService.getPlacesByLoginID(RealLuceAuctionApplication.loginID,(Integer)getActivityParam("Mode"));
    }

	// ************************ VALIDATION ***************************
	/*
	 * Validate searching
	 */
	private boolean validateToSearch() {
		if (placeInfo == null || placeInfo.getIdNo() == null
				|| placeInfo.getIdNo().length() <= 0) {
			MessageBox.show(getActivityContext(),
					R.string.car_name_search_message_invalidated_title,
					R.string.car_name_search_message_no_place);
			return false;
		}

		return true;
	}

	// ************************ BUSINESS LOGIC PROCESSING ****************

	/*
	 * Load car name list info from server
	 */
	private void loadCarNameList() {

		HTTPService objService = new HTTPService(new HTTPServiceCallback() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSucessfully(Object returnData) {

				hideProgressDialog();

				carNameList = (List<CarNameInfo>) returnData;
				lvCarNameList.setAdapter(new CarNameItemAdapter(
						getActivityContext(), carNameList));

				//car_name_message_no_car_name_title
				if (carNameList == null || carNameList.size() <= 0) {
					lvCarNameList.setVisibility(ListView.INVISIBLE);
					MessageBox.show(getActivityContext(),
							lblScreenTitle.getText().toString(),
							getActivityContext().getResources().getString(R.string.car_name_search_message_no_car_name));
				} else {
					lvCarNameList.setVisibility(ListView.VISIBLE);
				}
			}

			@Override
			public void onFailed(int errorCode) {
				hideProgressDialog();
				lvCarNameList.setVisibility(ListView.INVISIBLE);
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
		objService.getCarNameList(placeInfo.getIdNo(),
				makerItem.getManufacturerId(),
				RealLuceAuctionApplication.loginID, String.valueOf(searchMode), placeInfo.getOnePriceFlg());
	}


	// ************************ LISTENERS ********************************

	// ************************** SUB CLASSES *************************
	/**
	 * carName item adapter
	 */

	public class CarNameItemAdapter extends BaseAdapter {

		private Context mContext;
		private List<CarNameInfo> carNameList;

		public CarNameItemAdapter(Context context,
				List<CarNameInfo> carNameItems) {
			mContext = context;
			this.carNameList = carNameItems;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = inflater.inflate(
						R.layout.layout_car_name_selection_manufacturers_item,
						parent, false);
			}

			RelativeLayout blockCarName = (RelativeLayout) convertView
					.findViewById(R.id.blockCarName);

			// set back ground color.
			if (position % 2 > 0) {
				blockCarName.setBackgroundColor(Color.rgb(227, 224, 237));
			} else {
				blockCarName.setBackgroundColor(Color.WHITE);
			}

			// Get CarNameInfo.
			CarNameInfo carNameInfo = carNameList.get(position);

			// set data for cotrol.
			// Manufacture name
			TextView lblCarName = (TextView) convertView
					.findViewById(R.id.lblCarName);

			lblCarName.setText(carNameInfo.getCarNameDisp());

			// Count
			TextView lblCount = (TextView) convertView
					.findViewById(R.id.lblCount);

			lblCount.setText(String.format(
					getString(R.string.car_name_format_count_label),
					carNameInfo.getCount()));

			convertView.setTag(Integer.valueOf(position));
			//convertView.setOnClickListener(rowClick);

			return convertView;
		}

		@Override
		public int getCount() {
			return carNameList.size();
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
					directToCarListActivity((Integer) v.getTag());
				}
			}
		};

		private void directToCarListActivity(int position) {

			setActivityParam("SearchMode", searchMode);
			setActivityParam("Mode", mode);
			setActivityParam("PlaceID", placeInfo.getIdNo());
			setActivityParam("ItemNo", "");
			setActivityParam("Freeword", "");
			setActivityParam("CornerId", "");
			setActivityParam("MakerID", makerItem.getManufacturerId());
			setActivityParam("CarName", carNameList.get(position).getCarName());
			setActivityParam("PlaceName", placeInfo.getName());
			setActivityParam("OnePriceFlg", placeInfo.getOnePriceFlg());
			setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);

			showActivity(ItemsActivity.class);
		}

	}
}
