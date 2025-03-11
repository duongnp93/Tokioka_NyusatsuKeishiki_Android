package tokiokaability.auction.activities;


import java.util.List;

import tokiokaability.auction.R;
import tokiokaability.auction.RealLuceAuctionApplication;
import tokiokaability.auction.HTTPInfo.HTTPService;
import tokiokaability.auction.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction.HTTPInfo.PlaceInfo;
import tokiokaability.auction.commons.MessageBox;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PlaceSelectionActivity extends BaseActivity {

	private ListView lvPlaces;
	private List<PlaceInfo> places;
	private PlaceInfo selectedPlace;
	
	//************************ SCREEN LIFECYCLE ******************
	/*
	 * Initialization
	 */
	@Override
	protected void initialization(){
		setContentView(R.layout.activity_place_selection);
		super.initialization();
		
		lvPlaces = (ListView) this.findViewById(R.id.lvPlaces);
		
		selectedPlace = (PlaceInfo)getActivityParam("Place");

		loadPlaces();
		
		// set animation khi quay tro ve.
		setTransitAnimationType(TRANSIT_ANIMATION_FULL_DOWN);
	}
	
	
	
	
	
	
	
	//************************ BUSINESS LOGIC PROCESSING ****************
	/*
	 * Load places from server
	 */
	private void loadPlaces(){
        HTTPService objService = new HTTPService(new HTTPServiceCallback(){

			@SuppressWarnings("unchecked")
			@Override
			public void onSucessfully(Object returnData) {
				
				
				places = (List<PlaceInfo>) returnData;
				lvPlaces.setAdapter(new PlacesItemAdapter(getApplicationContext(), places));
				if (places == null || places.size() <= 0){
					MessageBox.show(getActivityContext(), R.string.place_info_title, R.string.place_info_message_no_place);
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

        objService.getPlacesByLoginID(RealLuceAuctionApplication.loginID,(Integer)getActivityParam("GetPlacesMode"));
	}
	
	
	
	
	
	
	
	//************************** SUB CLASSES *************************
	/*
	 * lvPlaces item adapter
	 */
	public class PlacesItemAdapter extends BaseAdapter {  
        
		private Context mContext; 
		private List<PlaceInfo> mPlaceInfoList;
       	        
        public PlacesItemAdapter(Context context, List<PlaceInfo> placeInfoList) {  
            mContext = context;
            mPlaceInfoList = placeInfoList;
        }  
      
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	TextView lblPlaceName = null;
        	ImageView imvChecked = null;
        	
        	if(convertView == null){
        		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        		convertView = inflater.inflate(R.layout.layout_place_selection_places_item, parent, false);

        	}
    		// Set Place Name
    		lblPlaceName = (TextView) convertView.findViewById(R.id.lblPlaceName);
    		lblPlaceName.setText(mPlaceInfoList.get(position).getName());
    		
      
    		imvChecked = (ImageView) convertView.findViewById(R.id.imvChecked);
    		if (selectedPlace != null && selectedPlace.getIdNo().equals(mPlaceInfoList.get(position).getIdNo()) && selectedPlace.getOnePriceFlg().equals(mPlaceInfoList.get(position).getOnePriceFlg())){
    			imvChecked.setVisibility(ImageView.VISIBLE);
    		}
    		else
    		{
    			imvChecked.setVisibility(ImageView.INVISIBLE);
    		}
    		
    		//convertView.setBackgroundDrawable(new ColorDrawable(Color.alpha(0)));
    		//convertView.setFocusable(false);
    		convertView.setTag(position);
    		convertView.setOnClickListener(rowClick);
        	
        	return convertView;
        }

		@Override
		public int getCount() {
			return places.size();
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
	    		View convertView = v;
	    		//ImageView imvChecked = null;
	    		int position = (Integer)convertView.getTag();
	    		
	    		selectedPlace = places.get(position);
	    		
	    		setActivityParam("Place",selectedPlace);
	    		/*
	    		imvChecked = (ImageView) convertView.findViewById(R.id.imvChecked);
	    		if (selectedPlace != null && selectedPlace.getPlaceNo().equals(mPlaceInfoList.get(position).getPlaceNo())){
	    			imvChecked.setVisibility(ImageView.VISIBLE);
	    		}
	    		else
	    		{
	    			imvChecked.setVisibility(ImageView.INVISIBLE);
	    		}
	    		*/
	    		lvPlaces.invalidateViews();
	    		closeActivity();
	    	}
	    };
      }
}
