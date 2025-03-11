package tokiokaability.auction.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tokiokaability.auction.R;
import tokiokaability.auction.RealLuceAuctionApplication;
import tokiokaability.auction.HTTPInfo.HTTPService;
import tokiokaability.auction.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction.HTTPInfo.LaneInfo;
import tokiokaability.auction.HTTPInfo.LoginInfo;
import tokiokaability.auction.HTTPInfo.PlaceInfo;
import tokiokaability.auction.commons.MessageBox;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AuctionStatusActivity extends BaseActivity{

	private ImageButton btnUpdate = null;
    private List<PlaceInfo> placeInfoList = null;
    private ListView lvPlaceSelection = null;
    private HTTPServiceCallback objServiceCallback = null;
    private int selectedPlaceIndex;
    
    
    
  //************************ SCREEN LIFECYCLE ******************
    
	@Override
	protected void initialization(){
		setContentView(R.layout.activity_auction_status);
        super.initialization();
        
        
        btnUpdate = (ImageButton)findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(btnUpdateClick);
        
        lvPlaceSelection = (ListView) findViewById(R.id.lvPlaceInfoList);
        placeInfoList = new ArrayList<PlaceInfo>();
        
        loadPlaces();
	}
	
	
	

	
	
	
	
	
	
	//************************ VALIDATION ***************************
	
	/*
	 * Validate to direct to Auction Screen
	 */
	private boolean validatePlaceToAuction(){
		if (RealLuceAuctionApplication.systemInfo == null || RealLuceAuctionApplication.systemInfo.getStatus() != 0){
			MessageBox.show(getActivityContext(),R.string.error_title_webservice, R.string.error_message_system_info_not_available);
			return false;
		}
		
			
		Date date = new Date();
		int currentTime = date.getHours()*100 + date.getMinutes();
		if (currentTime < Integer.parseInt(RealLuceAuctionApplication.systemInfo.getGwStartTime()) || currentTime > Integer.parseInt( RealLuceAuctionApplication.systemInfo.getGwEndTime())){
			String startTime = RealLuceAuctionApplication.systemInfo.getGwStartTime().substring(0,2)+":" + RealLuceAuctionApplication.systemInfo.getGwStartTime().substring(2,4);
			String endTime = RealLuceAuctionApplication.systemInfo.getGwEndTime().substring(0,2)+":" + RealLuceAuctionApplication.systemInfo.getGwEndTime().substring(2,4);
			
			MessageBox.show(getActivityContext(),getString( R.string.place_info_message_work_time_title), 
							String.format(getString(R.string.place_info_message_work_time)));
			return false;
		}
		
		return true;
			
	}
	
	
	/*
	 * Check Login
	 */
	private void checkLoginAndShowAuction(){
		
		HTTPService objServiceCheckLogin = new HTTPService(new HTTPServiceCallback(){
			@Override
			public void onSucessfully(Object returnData) {
				LoginInfo loginInfo = (LoginInfo) returnData;
				
				if (loginInfo.getResult().equalsIgnoreCase("1")){
					setActivityParam("Place",placeInfoList.get(selectedPlaceIndex));
	
					setTransitAnimationType(TRANSIT_ANIMATION_FULL_LEFT);
					showActivity(AuctionActivity.class);
				}
				else if (loginInfo.getResult().equalsIgnoreCase("2")){
					MessageBox.show(getActivityContext(), R.string.login_error_title, R.string.login_error_message_no_place);
				}
				else if (loginInfo.getResult().equalsIgnoreCase("8")){
					MessageBox.show(getActivityContext(),R.string.login_error_title, R.string.login_error_message_blocked_account);
					
				}
				else if (loginInfo.getResult().equalsIgnoreCase("9")){
					MessageBox.show(getActivityContext(),R.string.login_error_title, R.string.login_error_message_wrong_username_password);
				}
				else{
					MessageBox.show(getActivityContext(),R.string.login_error_title, R.string.login_error_message_wrong_username_password);
				}
				
				selectedPlaceIndex = -1;
			}

			@Override
			public void onFailed(int errorCode) {	
				selectedPlaceIndex = -1;
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
					default:
						MessageBox.show(getActivityContext(),R.string.error_title_webservice, R.string.error_message_unknow);
						break;
					
				}
			}
	    	
	    });
		
		objServiceCheckLogin.checkLogin(RealLuceAuctionApplication.loginID, RealLuceAuctionApplication.password);
	}
	
	
	
	
	
	
	
	
	
	
	
	//************************ BUSINESS LOGIC PROCESSING ****************
	
	/*
	 * Load places from server
	 */
	private void loadPlaces(){
		objServiceCallback = new HTTPServiceCallback(){

			@SuppressWarnings("unchecked")
			@Override
			public void onSucessfully(Object returnData) {
				
				hideProgressDialog();
				placeInfoList = (List<PlaceInfo>) returnData;
				lvPlaceSelection.setAdapter(new PlaceSelectionItemAdapter(getActivityContext(), placeInfoList));
				if (placeInfoList == null || placeInfoList.size() <= 0){
					MessageBox.show(getActivityContext(), R.string.place_info_title, R.string.place_info_message_no_place);
				}
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
					default:
						MessageBox.show(getActivityContext(),R.string.error_title_webservice, R.string.error_message_unknow);
						break;
					
				}
			}

        };
        
        selectedPlaceIndex = -1;
        HTTPService objService = new HTTPService(objServiceCallback);
        objService.getAuctionStatus(RealLuceAuctionApplication.loginID);
	
        this.showProgressDialog(R.string.please_waiting);
	}
	
	
	
	
	
	
	
	//************************ LISTENERS ********************************
    
    public OnClickListener btnUpdateClick = new OnClickListener() {
    	public void onClick(View v) {
            loadPlaces();
    	}
    };
    
    
	
    
    
    
    
    
    
	//************************** SUB CLASSES *************************
    
	/*
	 * lvPlaces item adapter
	 */
	public class PlaceSelectionItemAdapter extends BaseAdapter {  
        
		private Context mContext; 
		private List<PlaceInfo> mPlaceInfoList;
       	        
        public PlaceSelectionItemAdapter(Context context, List<PlaceInfo> placeInfoList) {  
            mContext = context;
            mPlaceInfoList = placeInfoList;
        }  
      
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	TextView lblPlaceName = null;
        	TextView lblCampaign = null;
        	TextView lblTimeNo = null;
        	ImageButton btnPlaceSelection = null;
        	ListView lstLanes = null;
        	
        	
        	if(convertView == null){
        		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        		convertView = inflater.inflate(R.layout.layout_auction_status_place_item, parent, false);
        		
        		
        		btnPlaceSelection = (ImageButton)convertView.findViewById(R.id.btnPlaceSelect);
        		btnPlaceSelection.setOnClickListener(btnPlaceSelectClick);

        	}
    		// Set Place Name
    		lblPlaceName = (TextView) convertView.findViewById(R.id.lblPlaceName);
    		lblPlaceName.setText(mPlaceInfoList.get(position).getName());
    		
    		lblCampaign = (TextView) convertView.findViewById(R.id.lblCampaign);
    		lblCampaign.setText(mPlaceInfoList.get(position).getCampaign());
    		
    		
    		// Set TimeNo
    		lblTimeNo = (TextView) convertView.findViewById(R.id.lblTimeNo);
    		
    		String suffixStr = "";
            String timesNoStr = mPlaceInfoList.get(position).getTimesNo();
    		int timesNo = Integer.parseInt(timesNoStr);
    		int hunRem = timesNo % 100;
    	    int tenRem = timesNo % 10;

    	    if ( hunRem - tenRem == 10 )
    	    {
    	    	suffixStr = getString(R.string.auction_status_format_time);
    	    }
    	    else
    	    {
    	    	switch ( tenRem )
        	    {
    	    	    case 1:
    	    	    	suffixStr = getString(R.string.auction_status_format_time_1);
		    	    	break;
    	    	    case 2:
    	    	    	suffixStr = getString(R.string.auction_status_format_time_2);
		    	    	break;
    	    	    case 3:
    	    	    	suffixStr = getString(R.string.auction_status_format_time_3);
		    	    	break;
    	    	    default:
    	    	    	suffixStr = getString(R.string.auction_status_format_time);
		    	    	break;
        	    }
    	    }
    		//lblTimeNo.setText(String.format(mContext.getString(R.string.auction_status_format_time), mPlaceInfoList.get(position).getTimesNo()) );
    	    lblTimeNo.setText(String.format(suffixStr, timesNoStr));
    		
    		
    		lstLanes = (ListView)convertView.findViewById(R.id.lstLanes);
    		lstLanes.setAdapter(new LaneItemAdapter(getActivityContext(), mPlaceInfoList.get(position).getLanes()));
    		final ViewGroup.LayoutParams params = lstLanes.getLayoutParams();
    		params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mPlaceInfoList.get(position).getLanes().size()*35 + 35, getResources().getDisplayMetrics());
    		lstLanes.setLayoutParams(params);
    		lstLanes.requestLayout();
            
            
    		btnPlaceSelection = (ImageButton)convertView.findViewById(R.id.btnPlaceSelect);
    		btnPlaceSelection.setTag(position);
    		if (mPlaceInfoList.get(position).getRealFlag().equalsIgnoreCase("1"))
    			btnPlaceSelection.setVisibility(ImageButton.VISIBLE);
    		else
    			btnPlaceSelection.setVisibility(ImageButton.INVISIBLE);
        	
        	return convertView;
        }

		@Override
		public int getCount() {
			return mPlaceInfoList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		public OnClickListener btnPlaceSelectClick = new OnClickListener() {
	    	public void onClick(View v) {
	    		if (validatePlaceToAuction() && selectedPlaceIndex == -1){
						selectedPlaceIndex = (Integer)v.getTag();
						checkLoginAndShowAuction();
	    		}
	    	}
	    };
      }
	
	
	/*
	 * lvPlaces item adapter
	 */
	public class LaneItemAdapter extends BaseAdapter {  
        
		private Context mContext; 
		private List<LaneInfo> mLaneList;
		
		private static final int CON_ROW_TYPE_HEADER = 1;
		private static final int CON_ROW_TYPE_ITEM = 2;
       	        
        public LaneItemAdapter(Context context, List<LaneInfo> laneList) {  
            mContext = context;
            mLaneList = laneList;
        }  
      
        @Override 
        public int getViewTypeCount(){
        	if (mLaneList.size() > 0 ){
        		return 2;
        	}
        	else
        	{
        		return 1;
        	}
        }
        
        @Override 
        public int getItemViewType(int position){
        	if (position == 0)
        		return CON_ROW_TYPE_HEADER;
        	else
        		return CON_ROW_TYPE_ITEM;
        }
        
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	TextView lblLaneTitle = null;
        	ImageView imvLaneColor = null;
        	TextView lblProductNo = null;
        	TextView lblCornerName = null;
        	int rowType = getItemViewType(position);
        	
        	
        	
    		
    		if (rowType == CON_ROW_TYPE_ITEM){
    			if(convertView == null){
    				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    				convertView = inflater.inflate(R.layout.layout_auction_status_lane_item, null, true);
    			}
    		}
    		else{
    		
    			if(convertView == null){
    				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    				convertView = inflater.inflate(R.layout.layout_auction_status_lane_header, null, true);
    			}
    		}

    		convertView.setTag(position-1);
    		
        	if (rowType == CON_ROW_TYPE_ITEM){
	    		// Set Place Name
        		lblLaneTitle = (TextView) convertView.findViewById(R.id.lblLaneTitle);
        		lblLaneTitle.setText(mLaneList.get(position-1).getLaneMark());
	    		
        		lblProductNo = (TextView) convertView.findViewById(R.id.lblProductNo);
        		lblProductNo.setText(mLaneList.get(position-1).getItemNo());
        		
	      
        		lblCornerName = (TextView) convertView.findViewById(R.id.lblCornerName);
        		lblCornerName.setText(mLaneList.get(position-1).getCornerName());
        		
	    		
        		imvLaneColor = (ImageView) convertView.findViewById(R.id.imvLaneColor);
        		
        		switch((position - 1) % 4){
	        		case 0:
	        			imvLaneColor.setImageResource(R.drawable.auction_status_lane_red);
	        			break;
	        		case 1:
	        			imvLaneColor.setImageResource(R.drawable.auction_status_lane_blue);
	        			break;
	        		case 2:
	        			imvLaneColor.setImageResource(R.drawable.auction_status_lane_yellow);
	        			break;
	        		case 3:
	        			imvLaneColor.setImageResource(R.drawable.auction_status_lane_green);
	        			break;
        		}
        	}
        	
        		
        	return convertView;
        }

		@Override
		public int getCount() {
			return mLaneList.size() + 1;
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
	
	
	

}
