package tokiokaability.auction.activities;

import java.util.ArrayList;
import java.util.List;

import tokiokaability.auction.R;
import tokiokaability.auction.HTTPInfo.CornerInfo;
import tokiokaability.auction.HTTPInfo.HTTPService;
import tokiokaability.auction.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction.HTTPInfo.PlaceInfo;
import tokiokaability.auction.RealLuceAuctionApplication;
import tokiokaability.auction.commons.MessageBox;
import tokiokaability.auction.view.PlacesView;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
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


public class SearchCornerActivity extends BaseActivity {

    private List<PlaceInfo> placeInfoList = null;
    private PlacesView blockPlace = null;

	private ListView lvCorners;
	
	private List<CornerInfo> corners;
	private int searchMode;
	private int mode;
	private TextView lblScreenTitle;
	
	/*
	 * This static member is to save screen state
	 */
	private static PlaceInfo statePlace = null;
	private static String onePriceFlg = "0";

	public AssetManager am ;
	public Typeface hiragino_kaku_w3;
	public Typeface hiragino_kaku_w6 ;
	public Typeface hiragino_kaku_w8 ;
	public Typeface hiragino_marugo_w4 ;
	public Typeface hiragino_mincho_w3 ;
	public Typeface hiragino_mincho_w6 ;
	
	
	
	//************************ SCREEN LIFECYCLE ******************
	/*
	 * Initialization
	 */
	@Override
	protected void initialization(){
		setContentView(R.layout.activity_search_corner);
		super.initialization();

		//get Font
		am = getApplicationContext().getAssets();
		hiragino_kaku_w3 = Typeface.createFromAsset(am,"fonts/hiragino_kakugo_w3.otf");
		hiragino_kaku_w6 = Typeface.createFromAsset(am,"fonts/hiragino_kakugo_w6.otf");
		hiragino_kaku_w8 = Typeface.createFromAsset(am,"fonts/hiragino_kakugo_w8.otf");
		hiragino_marugo_w4 = Typeface.createFromAsset(am,"fonts/hiragino_marugo_w4.otf");
		hiragino_mincho_w3 = Typeface.createFromAsset(am,"fonts/hiragino_mincho_w3.otf");
		hiragino_mincho_w6 = Typeface.createFromAsset(am,"fonts/hiragino_mincho_w6.otf");

        //Get widgets
        //blockPlace = (PlacesView) this.findViewById(R.id.blockPlace);
		lvCorners = (ListView)this.findViewById(R.id.lvCornerList);

		statePlace = null;
        searchMode = (Integer)getActivityParam("SearchMode", 1);
        mode = (Integer) getActivityParam("Mode", 1);		
        onePriceFlg = (String) getActivityParam("OnePriceFlg");
		lblScreenTitle = (TextView)this.findViewById(R.id.lblScreenTitle);
		lblScreenTitle.setTypeface(hiragino_kaku_w6);

		loadPlaces();
		
		lvCorners.setOnItemClickListener(listViewOnItemClick);
	}
	
	public OnItemClickListener listViewOnItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (validateToSearch()){
				search(position);
    		}
		}
		
	};


	
	//************************ VALIDATION ***************************
	/*
	 * Validate searching
	 */
	private boolean validateToSearch(){
		 if(statePlace == null || statePlace.getIdNo() == null  || statePlace.getIdNo().length() <= 0){
			 MessageBox.show(getActivityContext(), R.string.corner_search_message_invalidated_title, R.string.corner_search_message_no_place);
			 return false;
		 }
		 
		 return true;
		
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
		        //Binding BlockPlace
				//blockPlace.setVisibility(View.VISIBLE);
		        placeInfoList = (ArrayList<PlaceInfo>)returnData;
				/*
		        blockPlace.setPlaces((ArrayList<PlaceInfo>) placeInfoList);
		        blockPlace.setSelectedIndex(getPlaceInfoIndex(-1));
		        blockPlace.setOnPlaceSelectedListener(new PlacesView.OnPlaceSelectedListener() {
		            @Override
		            public void onPlaceSelected(int index) {
		            	statePlace = placeInfoList.get(blockPlace.getSelectedIndex());
		            	onePriceFlg = statePlace.getOnePriceFlg();
		                loadCorners();
		            }
		        });
		        
		        if (blockPlace.getSelectedIndex() >= 0 && blockPlace.getSelectedIndex() < placeInfoList.size()){
		        	statePlace = placeInfoList.get(blockPlace.getSelectedIndex());
			        loadCorners();
		        }*/
				statePlace = placeInfoList.get(0);
				loadCorners();
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
        //blockPlace.setVisibility(View.INVISIBLE);
        lvCorners.setVisibility(ListView.INVISIBLE);
        showProgressDialog(R.string.please_waiting);

        objService.getPlacesByLoginID(RealLuceAuctionApplication.loginID,mode);
	}
		
	
	
	
	
	/*
	 * Load Corners
	 */
	private void loadCorners(){
		HTTPService objService = new HTTPService(new HTTPServiceCallback(){

			@SuppressWarnings("unchecked")
			@Override
			public void onSucessfully(Object returnData) {
				
				
				corners = (List<CornerInfo>) returnData;
				lvCorners.setAdapter(new CornersItemAdapter(getApplicationContext(), corners));
				if (corners == null || corners.size() <= 0){
					lvCorners.setVisibility(ListView.INVISIBLE);
					MessageBox.show(getActivityContext(), R.string.corner_search_message_no_corner_title, R.string.corner_search_message_no_corner);
				}
				else{
					lvCorners.setVisibility(ListView.VISIBLE);
				}
				hideProgressDialog();
			}

			
			@Override
			public void onFailed(int errorCode) {
				hideProgressDialog();
				lvCorners.setVisibility(ListView.INVISIBLE);
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
        objService.getCornersByPlaceId(statePlace.getIdNo(), statePlace.getOnePriceFlg());
	}
	
	/*
	 * Search product
	 */
	private void search(int cornerIndex){
		setActivityParam("PlaceID",statePlace.getIdNo());
		setActivityParam("ItemNo","");
		setActivityParam("Freeword","");
		setActivityParam("CornerId",corners.get(cornerIndex).getCornerID());
		setActivityParam("BoxNo","");
		setActivityParam("PlaceName",statePlace.getName());
		setActivityParam("SearchMode",searchMode);
		setActivityParam("Mode", mode);

		setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
		showActivity(ItemsActivity.class);
	}


    /*
     * Get index of PlaceInfo on last login
     */
    private int getPlaceInfoIndex(int defaultValue){
        String placeId =  (String) getActivityParam("PlaceId");
        for (int i = 0; i<placeInfoList.size(); i++){
            if(placeId.equalsIgnoreCase(placeInfoList.get(i).getIdNo()) && onePriceFlg.equalsIgnoreCase(placeInfoList.get(i).getOnePriceFlg())){
                return i;
            }
        }
        if(defaultValue == -1){
    		statePlace = placeInfoList.get(0);
    		placeId = placeInfoList.get(0).getIdNo();
        	onePriceFlg =  placeInfoList.get(0).getOnePriceFlg();
        	defaultValue = 0;
        }
        return defaultValue;
    }

   
	
	
	//************************ LISTENERS ********************************
	  
    
		//************************** SUB CLASSES *************************
	    
		/*
		 * lvCorner item adapter
		 */
		public class CornersItemAdapter extends BaseAdapter {  
	        
			private Context mContext; 
			private List<CornerInfo> cornerInfoList;
	       	private RelativeLayout blockCornerItem;
	        public CornersItemAdapter(Context context, List<CornerInfo> cornerInfoList) {  
	            mContext = context;
	            this.cornerInfoList = cornerInfoList;
	        }  
	      
	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	        	TextView lblCornerName = null;
	        	TextView lblStartTime = null;
	        	TextView lblCount = null;
	        	TextView lblBoxNoRange = null;

	        	//RelativeLayout blockCornerItem = null;

                //ColorStateList textColor = getResources().getColorStateList(R.drawable.list_item_text_color_default);
	        	
	        	if(convertView == null){
	        		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        		convertView = inflater.inflate(R.layout.layout_search_corner_item, parent, false);
	        		
	        	}
	        	
	        	blockCornerItem = (RelativeLayout)convertView.findViewById(R.id.blockCornerItem);
	        	if (position % 2 == 0){
					blockCornerItem.setBackgroundResource(R.drawable.custom_corner_item);
	        	}
	        	else{
					blockCornerItem.setBackgroundResource(R.drawable.custom_corner_item_orange);
	        	}
	        	

	    		lblCornerName = (TextView) convertView.findViewById(R.id.lblCornerName);
	    		lblCornerName.setText(cornerInfoList.get(position).getCornerName());
                //lblCornerName.setTextColor(textColor);
	    		
	    		lblCount = (TextView) convertView.findViewById(R.id.lblCount);
	    		lblCount.setText(cornerInfoList.get(position).getCount());
                //lblCount.setTextColor(textColor);
	    		
	    		lblStartTime = (TextView) convertView.findViewById(R.id.lblStartTime);
	    		lblStartTime.setText(cornerInfoList.get(position).getStartTime());
                //lblStartTime.setTextColor(textColor);
	    		
	    		lblBoxNoRange = (TextView) convertView.findViewById(R.id.lblBoxNoRange);
	    		lblBoxNoRange.setText(cornerInfoList.get(position).getStartItemNo() + "-" + cornerInfoList.get(position).getEndItemNo());
                //lblBoxNoRange.setTextColor(textColor);
	    		lblCornerName.setTypeface(hiragino_kaku_w3);
				lblStartTime.setTypeface(hiragino_kaku_w3);
				lblBoxNoRange.setTypeface(hiragino_kaku_w3);
				lblCount.setTypeface(hiragino_kaku_w3);
	    		
	    		convertView.setTag(Integer.valueOf(position));
	    		//convertView.setOnClickListener(rowClick);
	        	
	        	return convertView;
	        }

			@Override
			public int getCount() {
				return cornerInfoList.size();
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
		    		if (validateToSearch()){
						search((Integer)v.getTag());
		    		}
		    	}
		    };
			
	      }
}
