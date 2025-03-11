package tokiokaability.auction.HTTPInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Base64;
import android.util.Log;
import tokiokaability.auction.RealLuceAuctionApplication;
import tokiokaability.auction.crypt.Crypt;

public class HTTPService {

	// constan
	public static final int NO_ERROR = 0;
	public static final int ERROR_UNKNOW = 1;
	public static final int ERROR_NETWORK = 20001;
	public static final int ERROR_XMLPARSER = 20002;
	public static final int ERROR_RETURN_DATA_INVALID = 20003;
	public static final int ERROR_SERVER_ERROR = 20004;
	public static final int ERROR_CANCEL = 20005;

	public static final int COMMAND_SYSTEM_INFOR = 1;
	public static final int COMMAND_LOGIN = 2;
	public static final int COMMAND_GET_PLACE = 3;
	public static final int COMMAND_GET_LANES = 4;
	public static final int COMMAND_GET_ITEM = 5;
	public static final int COMMAND_GET_ITEM_IMAGE = 6;
	public static final int COMMAND_GET_ITEM_SPEC = 7;
	public static final int COMMAND_CHECK_LOGIN = 8;
	public static final int COMMAND_GET_ITEMS = 9;
	public static final int COMMAND_GET_CORNERS = 10;
	public static final int COMMAND_GET_ITEMS_DETAIL = 11;
	public static final int COMMAND_REGISTER_BID_OFFLINE = 12;
	public static final int COMMAND_REGISTER_ITEM_MARK = 13;
	public static final int COMMAND_REGISTER_APN_TOKEN = 14;
	public static final int COMMAND_REGISTER_AGREEMENT = 15;
	public static final int COMMAND_GET_MEMBER_TYPE = 16;
	public static final int COMMAND_GET_MAKERS_ITEMS = 17;
	public static final int COMMAND_GET_ITEM_NAME_ITEMS = 18;
	public static final int COMMAND_GET_AUCTION_STATUS = 19;
	public static final int COMMAND_GET_POSTER = 20;
	public static final int COMMAND_GET_ANNOUNCE = 21;
	public static final int COMMAND_PREVIEW_REQUEST = 22;
	public static final int COMMAND_REGIST_PREVIEW_REQUEST = 23;
	public static final int COMMAND_GET_REQUEST_RULE_URL = 24;
	public static final int COMMAND_REGISTER_ONE_PRICE = 25;
	public static final int COMMAND_GATEWAY_CHECK = 26;
	public static final int COMMAND_SET_IMAGE = 27;
	public static final int COMMAND_GET_BANNER = 28;
	public static final int COMMAND_FORGET_PASSWORD = 29;
	public static final int COMMAND_SENDLOG = 30;
	public static final int COMMAND_GETIMAGE = 31;
	public static final int COMMAND_GETCOUNTRY = 32;
	public static final int COMMAND_GETSEARCHINFO = 33;
	public static final int COMMAND_UPDATEOKINIRI = 34;
	public static final int COMMAND_UPDATEPUSHFLAG = 35;
	public static final int COMMAND_GETPUSHFLAG = 36;
	public static final int COMMAND_GETLANGUAGEFLAG = 37;
	public static final int COMMAND_UPDATELANGUAGEFLAG = 38;
	public static final int COMMAND_GET_INQUIRY = 39;
	public static final int COMMAND_SUBMIT_INQUIRY = 40;
	public static final int COMMAND_GET_RESULT_DISPLAY_INTERVAL = 41;
	public static final int COMMAND_MEMO_UPDATE = 42;
	public static final int COMMAND_GET_RESTTIME = 43;
	public static final int COMMAND_GET_NEGOTIATION = 44;
	public static final int COMMAND_REGISTER_SELLING_RESULT = 45;
	public static final int COMMAND_UPDATE_REHOLDPRICE = 46;
	public static final int COMMAND_GETSEISANSHO = 47;
	// Property

	private HTTPServiceCallback callback;

	public HTTPServiceCallback getCallback() {
		return callback;
	}

	public void setCallback(HTTPServiceCallback callback) {
		this.callback = callback;
	}

	// Private
	
	private String baseUrl;
	private LaneInfo objLane;
	private ItemInfo objItem;
	private String placeId;
	private String timesNo;
	public static int countService;

	public LaneInfo getObjLane() {
		return objLane;
	}

	public void setObjLane(LaneInfo objLane) {
		this.objLane = objLane;
	}

	public ItemInfo getObjItem() {
		return objItem;
	}

	public void setObjItem(ItemInfo objItem) {
		this.objItem = objItem;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getTimesNo() {
		return timesNo;
	}

	public void setTimesNo(String timesNo) {
		this.timesNo = timesNo;
	}

	// Contructor
	public HTTPService(HTTPServiceCallback callback) {
		this.callback = callback;
		baseUrl = RealLuceAuctionApplication.baseURL;
	}

	

	public static void cancelAll() {
		List<AsyncRequest> list = AsyncRequest.getAsyncRequests();
		for (AsyncRequest  asyncRequest: list) {
			if (asyncRequest.isAlive()) {
				asyncRequest.interrupt();
			}
		}
	}

	public static void restartAllServices(boolean isDefaultURL) {
		
		Log.d("restartAllServices", ""+isDefaultURL);
    	/*
		if (!isDefaultURL) {
			List<AsyncRequest> list = AsyncRequest.getAsyncRequests();
			for (AsyncRequest  asyncRequest: list) {
				if (asyncRequest.isAlive()) {
					asyncRequest.interrupt();
				}
				
				asyncRequest.setURL(asyncRequest.getURL().replace(RealLuceAuctionApplication.baseURL, RealLuceAuctionApplication.defaultBaseURL));
			}
			
			RealLuceAuctionApplication.baseURL = RealLuceAuctionApplication.defaultBaseURL;
			
			for (AsyncRequest  asyncRequest: list) {
				countService++;
					asyncRequest.run();
			}
			
			countService = 0;
			RealLuceAuctionApplication.isDefaultURL = true;
		}
		else {
			
			if (countService == RealLuceAuctionApplication.backupURLs.size()) {
				RealLuceAuctionApplication.isConneted = false;
				RealLuceAuctionApplication.baseURL = RealLuceAuctionApplication.defaultBaseURL;
			}
			else {
				List<AsyncRequest> list = AsyncRequest.getAsyncRequests();
				for (AsyncRequest  asyncRequest: list) {
					if (asyncRequest.isAlive()) {
						asyncRequest.interrupt();
					}
					
					Log.d("restartAllServices", ""+asyncRequest.getURL());
					
					asyncRequest.setURL(asyncRequest.getURL().replace(RealLuceAuctionApplication.baseURL, RealLuceAuctionApplication.backupURLs.get(countService)));
				}
				
				Log.d("restartAllServices", ""+RealLuceAuctionApplication.backupURLs.get(countService)+"-"+countService);
				
				RealLuceAuctionApplication.baseURL = RealLuceAuctionApplication.backupURLs.get(countService);
				
				for (AsyncRequest  asyncRequest: list) {
					countService++;
					asyncRequest.run();
				}
			}
			
			countService = 0;
			RealLuceAuctionApplication.isDefaultURL = false;
			
		}
		*/
		List<AsyncRequest> list = AsyncRequest.getAsyncRequests();
		//Log.d("run", String.valueOf(list.size()));
		for (AsyncRequest  asyncRequest: list) {
			if (asyncRequest.isAlive()) {
				asyncRequest.interrupt();
			}
			
			Log.d("restartAllServices", ""+asyncRequest.getURL());
			
			asyncRequest.setURL(asyncRequest.getURL().replace(RealLuceAuctionApplication.baseURL, RealLuceAuctionApplication.backupURLs.get(0)));
		}
		
		Log.d("restartAllServices", ""+RealLuceAuctionApplication.backupURLs.get(0)+"-"+0);
		
		RealLuceAuctionApplication.baseURL = RealLuceAuctionApplication.backupURLs.get(0);
		
		for (AsyncRequest  asyncRequest: list) {
			asyncRequest.run();
		}
	}
	
	public void setBaseUrl(String url) {
		this.baseUrl = url;
	}


	// public method
	/*
	 * サーバー情報の取得
	 */
	public void getSystemInfo() {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_SYSTEM_INFOR,this.baseUrl + "HP_GetSystemInfo.php", this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>(1);
		
		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
		data.add(new BasicNameValuePair("LanguageKbn","1"));
		asyncRequest.setPostValuePairs(data);
		asyncRequest.start();

	}

	/*
	 * ログイン
	 */
	public void login(String username, String password, String macAddr, String resultKbn,String ipAddress, String memberNo, String authFlag, String countryCode) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_LOGIN,this.baseUrl + "HP_Login.php", this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		
		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("Format","XML"));
		data.add(new BasicNameValuePair("LoginID",username));
		data.add(new BasicNameValuePair("Password",password));
		data.add(new BasicNameValuePair("MacAddress",macAddr));
		String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
		data.add(new BasicNameValuePair("LanguageKbn","1"));
		data.add(new BasicNameValuePair("ResultKbn",resultKbn));
		data.add(new BasicNameValuePair("IPAddress",ipAddress));
		data.add(new BasicNameValuePair("MemberNo",memberNo));
		data.add(new BasicNameValuePair("AuthFlg",authFlag));
        data.add(new BasicNameValuePair("CountryCode",countryCode));
		asyncRequest.setPostValuePairs(data);
		
		asyncRequest.start();

	}

	/*
	 * ログインチェック
	 */
	public void checkLogin(String username, String password) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_CHECK_LOGIN,this.baseUrl + "HP_LoginCheck.php", this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>(0);
		
		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("Format","XML"));
		data.add(new BasicNameValuePair("LoginID",username));
		data.add(new BasicNameValuePair("Password",password));
		String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
		data.add(new BasicNameValuePair("LanguageKbn","1"));
		
		asyncRequest.setPostValuePairs(data);
		asyncRequest.start();

	}

	/*
	 * 会場情報の取得
	 */
	public void getPlacesByLoginID(String loginID, int mode) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GET_PLACE,this.baseUrl + "HP_GetPlaceList.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();

		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("LoginID",loginID));
		data.add(new BasicNameValuePair("Mode", String.valueOf(mode)));

		String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
		data.add(new BasicNameValuePair("LanguageKbn","1"));

		asyncRequest.setPostValuePairs(data);
		asyncRequest.start();

	}

	/*
	 * 落札画面待機時間の取得
	 */
	public void getResultDisplayInterval() {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GET_RESULT_DISPLAY_INTERVAL,this.baseUrl + "getResultDisplayInterval.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		asyncRequest.setPostValuePairs(data);
		asyncRequest.start();
	}

	/*
	 * レーン情報(サーバーのIPアドレス等)を取得
	 */
	public void getLanesByPlaceId(String placeId, String timesNo,String laneId) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GET_LANES,this.baseUrl + "HP_GetLaneList.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>(0);
		
		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("PlaceID",placeId));
		data.add(new BasicNameValuePair("LaneID",laneId));
		asyncRequest.setPostValuePairs(data);
		String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
		data.add(new BasicNameValuePair("LanguageKbn","1"));

		this.placeId = placeId;
		this.timesNo = timesNo;

		asyncRequest.start();
	}

	/*
	 * 商品情報の取得(リアル時に利用)
	 */
	public void getItemInfo(LaneInfo objLane, ItemInfo objItem) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GET_ITEM,this.baseUrl + "HP_GetItem.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>(0);
		
		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("PlaceID",objLane.getPlaceId()));
		data.add(new BasicNameValuePair("SessionNo",objLane.getTimesNo()));
		data.add(new BasicNameValuePair("SellNo", String.valueOf(objItem.getItemNo())));
		data.add(new BasicNameValuePair("LoginID", RealLuceAuctionApplication.loginID));
        String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
        data.add(new BasicNameValuePair("LanguageKbn",langKbn));
		
		asyncRequest.setPostValuePairs(data);

		this.objLane = objLane;
		this.objItem = objItem;
		asyncRequest.start();
	}


	public void getImages(LaneInfo objLane, ItemInfo objItem, int position, String sizeType) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GET_ITEM_IMAGE,this.baseUrl + "HP_GetImage.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		
		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("PlaceID",objLane.getPlaceId()));
		data.add(new BasicNameValuePair("SessionNo",objLane.getTimesNo()));
		data.add(new BasicNameValuePair("SellNo", String.valueOf(objItem.getItemNo())));
		data.add(new BasicNameValuePair("Position", String.valueOf(position)));
		data.add(new BasicNameValuePair("SizeType",sizeType));
		String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
		data.add(new BasicNameValuePair("LanguageKbn",langKbn));

		asyncRequest.setPostValuePairs(data);

		this.objLane = objLane;
		this.objItem = objItem;
	
		asyncRequest.setPriority(3);
		asyncRequest.start();
	}



	public void getImages(ItemInfo objItem, int position, String sizeType) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GET_ITEM_IMAGE,this.baseUrl + "HP_GetImage.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();

		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("PlaceID",objItem.getPlaceId()));
		data.add(new BasicNameValuePair("SessionNo",objItem.getTimesNo()));
		data.add(new BasicNameValuePair("SellNo", String.valueOf(objItem.getItemNo())));
		data.add(new BasicNameValuePair("Position", String.valueOf(position)));
		data.add(new BasicNameValuePair("SizeType",sizeType));
		String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
		data.add(new BasicNameValuePair("LanguageKbn","1"));

		asyncRequest.setPostValuePairs(data);

		this.objLane = objLane;
		this.objItem = objItem;

		asyncRequest.setPriority(3);
		asyncRequest.start();
	}

/*
	public void getCarSpecImage(LaneInfo objLane, ItemInfo objItem) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GET_ITEM_SPEC,this.baseUrl + "RCAGetCarListImage.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>(1);
		
		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("PlaceID",objLane.getPlaceId()));
		data.add(new BasicNameValuePair("TimesNo",objLane.getTimesNo()));
		data.add(new BasicNameValuePair("ItemNo", String.valueOf(objItem.getItemNo())));
		asyncRequest.setPostValuePairs(data);

		this.objLane = objLane;
		this.objItem = objItem;
		asyncRequest.start();
	}
*/
	
	/*
	 * 商品リストの取得
	 */
	public void getItemList(String searchMode, String placeId, String itemNo, String boxNo, String boxNoTo,
			String loginId, String freeWord, String cornerId, int MaxRowCount,
			int pageNo, String sortId,String sortOrder, Boolean hasPicture,
							String sessionNo, String type,String cornerList,
							String subCatList,String brandList,String rankList,String koshoFlg,String koshoQuery) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GET_ITEMS,this.baseUrl + "HP_GetItemList.php",this);
		ArrayList<NameValuePair>data = new ArrayList<NameValuePair>();

		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("PlaceID",placeId));
		data.add(new BasicNameValuePair("ItemNo", itemNo));
		data.add(new BasicNameValuePair("BoxNo", boxNo));
		data.add(new BasicNameValuePair("BoxNoTo", boxNoTo));
		data.add(new BasicNameValuePair("SearchMode",searchMode));
		data.add(new BasicNameValuePair("LoginID",loginId));
		data.add(new BasicNameValuePair("FreeWord",freeWord));
		data.add(new BasicNameValuePair("CornerID",cornerId));
		data.add(new BasicNameValuePair("MaxRowCount",String.valueOf(MaxRowCount)));
		data.add(new BasicNameValuePair("PageNo", String.valueOf(pageNo)));
		data.add(new BasicNameValuePair("SortID", sortId));
		data.add(new BasicNameValuePair("SortOrder",sortOrder));
		data.add(new BasicNameValuePair("CornerList",cornerList));
		data.add(new BasicNameValuePair("SubCatList",subCatList));
		data.add(new BasicNameValuePair("BrandList",brandList));
		data.add(new BasicNameValuePair("RankList",rankList));
		data.add(new BasicNameValuePair("KoshoFlg",koshoFlg));
		data.add(new BasicNameValuePair("KoshoQuery",koshoQuery));
		if (hasPicture != null) {
			if (hasPicture)
				data.add(new BasicNameValuePair("HasPicture", "1"));
			else
				data.add(new BasicNameValuePair("HasPicture", "0"));
		}
		String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
		data.add(new BasicNameValuePair("LanguageKbn","1"));
		data.add(new BasicNameValuePair("SessionNo",sessionNo));
		data.add(new BasicNameValuePair("Type",type));
		asyncRequest.setPostValuePairs(data);

		asyncRequest.start();
	}
	
	

	public void getPreviewRequestContent(String placeId, String timesNo, String itemNo,  String loginId) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_PREVIEW_REQUEST,this.baseUrl + "RCAGetRequestContents.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>(1);
		
		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("PlaceID", placeId));
		data.add(new BasicNameValuePair("TimesNo", timesNo));
		data.add(new BasicNameValuePair("ItemNo", itemNo));
		data.add(new BasicNameValuePair("LoginID", loginId));
		
		String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
		data.add(new BasicNameValuePair("LanguageKbn","1"));
		
		asyncRequest.setPostValuePairs(data);

		asyncRequest.start();
	}
	
	public void getRequestRuleURLContent(String placeId) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GET_REQUEST_RULE_URL,this.baseUrl + "RCAGetRequestRuleURL.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>(1);
		
		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("PlaceID", placeId));
		String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
		data.add(new BasicNameValuePair("LanguageKbn","1"));
		asyncRequest.setPostValuePairs(data);

		asyncRequest.start();
	}
	
	public void registPreviewRequestContent(PreviewRequestInfo previewRequestInfo) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_REGIST_PREVIEW_REQUEST,this.baseUrl + "RCARegistRequestContents.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>(1);
		
		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		
		data.add(new BasicNameValuePair("LoginID", previewRequestInfo.getLoginID()));
		data.add(new BasicNameValuePair("PlaceID", previewRequestInfo.getPlaceID()));
		data.add(new BasicNameValuePair("TimesNo", previewRequestInfo.getTimesNo()));
		data.add(new BasicNameValuePair("ItemNo", previewRequestInfo.getItemNo()));
		data.add(new BasicNameValuePair("CarName", previewRequestInfo.getCarName()));
		
		
		Crypt mcrypt = new Crypt();
		String encryptedPersonName = null;
		try {
			encryptedPersonName = Base64.encodeToString(mcrypt.encrypt(previewRequestInfo.getPersonName()), Base64.DEFAULT);
		} catch (Exception e) {
		}

		String encryptedTEL = null;
		try {
			encryptedTEL = Base64.encodeToString(mcrypt.encrypt(previewRequestInfo.getTEL()), Base64.DEFAULT);
		} catch (Exception e) {
		}

		String encryptedTEL2 = null;
		try {
			encryptedTEL2 = Base64.encodeToString(mcrypt.encrypt(previewRequestInfo.getTEL2()), Base64.DEFAULT);
		} catch (Exception e) {
		}

		String encryptedFAX = null;
		try {
			encryptedFAX = Base64.encodeToString(mcrypt.encrypt(previewRequestInfo.getFAX()), Base64.DEFAULT);
		} catch (Exception e) {
		}

		data.add(new BasicNameValuePair("PersonName", encryptedPersonName));
		data.add(new BasicNameValuePair("TEL", encryptedTEL));
		data.add(new BasicNameValuePair("TEL2", encryptedTEL2));
		data.add(new BasicNameValuePair("FAX", encryptedFAX));
		
		
		
		/*data.add(new BasicNameValuePair("PersonName", previewRequestInfo.getPersonName()));
		data.add(new BasicNameValuePair("TEL", previewRequestInfo.getTEL()));
		data.add(new BasicNameValuePair("TEL2", previewRequestInfo.getTEL2()));
		data.add(new BasicNameValuePair("FAX", previewRequestInfo.getFAX()));*/
		data.add(new BasicNameValuePair("OutsideFlag", String.valueOf(previewRequestInfo.getOutsideFlag())));
		data.add(new BasicNameValuePair("InsideFlag", String.valueOf(previewRequestInfo.getInsideFlag())));
		data.add(new BasicNameValuePair("SeatFlag", String.valueOf(previewRequestInfo.getSeatFlag())));
		data.add(new BasicNameValuePair("CapacityFlag", String.valueOf(previewRequestInfo.getCapacityFlag())));
		data.add(new BasicNameValuePair("DriveFlag", String.valueOf(previewRequestInfo.getDriveFlag())));
		data.add(new BasicNameValuePair("AudioFlag", String.valueOf(previewRequestInfo.getAudioFlag())));
		data.add(new BasicNameValuePair("EngineFlag", String.valueOf(previewRequestInfo.getEngineFlag())));
		data.add(new BasicNameValuePair("WheelFlag", String.valueOf(previewRequestInfo.getWheelFlag())));
		data.add(new BasicNameValuePair("ElectricFlag", String.valueOf(previewRequestInfo.getElectricFlag())));
		data.add(new BasicNameValuePair("TireFlag", String.valueOf(previewRequestInfo.getTireFlag())));
		data.add(new BasicNameValuePair("PowerSteeringFlag", String.valueOf(previewRequestInfo.getPowerSteeringFlag())));
		data.add(new BasicNameValuePair("SizeFlag", String.valueOf(previewRequestInfo.getSizeFlag())));
		data.add(new BasicNameValuePair("RequestMemo", String.valueOf(previewRequestInfo.getRequestMemo())));

		asyncRequest.setPostValuePairs(data);

		asyncRequest.start();
	}
	
	
	
	

	/**
	 * 商品の詳細情報を取得
	 */
	public void getItemDetails(String placeId, String timesNo, String itemNo,  String loginId, String searchMode) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GET_ITEMS_DETAIL,this.baseUrl + "HP_GetItemDetail.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>(1);
		
		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("PlaceID", placeId));
		data.add(new BasicNameValuePair("SessionNo", timesNo));
		data.add(new BasicNameValuePair("SellNo", itemNo));
		data.add(new BasicNameValuePair("LoginID", loginId));
		data.add(new BasicNameValuePair("SearchMode", searchMode));

		String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
		data.add(new BasicNameValuePair("LanguageKbn","1"));
		
		asyncRequest.setPostValuePairs(data);

		this.objItem = new ItemInfo();
		asyncRequest.start();
	}

	/*
	 * コーナー情報の取得
	 */
	public void getCornersByPlaceId(String placeId, String onePriceFlg) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GET_CORNERS,this.baseUrl + "HP_GetCornerList.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>(1);
		
		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("PlaceID",placeId));
		//data.add(new BasicNameValuePair("OnePriceFlag", onePriceFlg));
        String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
        data.add(new BasicNameValuePair("LanguageKbn","1"));

		asyncRequest.setPostValuePairs(data);


		asyncRequest.start();
	}
	/*
	 * 不在登録
	 */
	public void registerBidOffline(String placeId, String timesNo,
			String itemNo, String loginId, String price) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_REGISTER_BID_OFFLINE,this.baseUrl + "HP_RegistAbsenceBid.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		
		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("PlaceID",placeId));
		data.add(new BasicNameValuePair("TimesNo",timesNo));
		data.add(new BasicNameValuePair("ItemNo", itemNo));
		data.add(new BasicNameValuePair("LoginID",loginId));
		data.add(new BasicNameValuePair("Price", price));
		String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
		data.add(new BasicNameValuePair("LanguageKbn","1"));
		asyncRequest.setPostValuePairs(data);

		asyncRequest.start();
	}
	
	/**
	 * 

	 */
	public void registerItemMark(ItemInfo objItem, String itemMark, String loginId) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_REGISTER_ITEM_MARK,this.baseUrl + "RCARegistItemMark.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		
		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("PlaceID",objItem.getPlaceId()));
		data.add(new BasicNameValuePair("TimesNo",objItem.getTimesNo()));
		data.add(new BasicNameValuePair("ItemNo", String.valueOf(objItem.getItemNo())));
		data.add(new BasicNameValuePair("LoginID",loginId));
		data.add(new BasicNameValuePair("Mode", itemMark));
		asyncRequest.setPostValuePairs(data);



		asyncRequest.start();
	}
	
	

	public void registerNotification(String loginId,String apnToken) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_REGISTER_APN_TOKEN,this.baseUrl + "HP_RegistDevice.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		
		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("LoginID",loginId));
		data.add(new BasicNameValuePair("DeviceToken", apnToken));
		String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
		data.add(new BasicNameValuePair("LanguageKbn","1"));
		asyncRequest.setPostValuePairs(data);

		asyncRequest.start();
	}
	
	
	public void registerAgreement(String loginId, String macAddress) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_REGISTER_AGREEMENT,this.baseUrl + "HP_RegistAgree.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		
		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("LoginID",loginId));
		data.add(new BasicNameValuePair("MacAddress", macAddress));
		String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
		data.add(new BasicNameValuePair("LanguageKbn","1"));
		asyncRequest.setPostValuePairs(data);

		asyncRequest.start();
	}
			
	public void getMemberType(String loginId) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GET_MEMBER_TYPE, this.baseUrl + "HP_GetMemberType.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		
		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("LoginID",loginId));
		String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
		data.add(new BasicNameValuePair("LanguageKbn","1"));
		asyncRequest.setPostValuePairs(data);

		asyncRequest.start();
	}	
	
	
	public void getManufactureList(String placeId, String loginId,String searchMode, String onePriceFlg) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GET_MAKERS_ITEMS, this.baseUrl + "RCAGetMakerList.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();


		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("PlaceID",placeId));
		data.add(new BasicNameValuePair("LoginID",loginId));
		data.add(new BasicNameValuePair("SearchMode",searchMode));
		data.add(new BasicNameValuePair("OnePriceFlag", onePriceFlg));
		asyncRequest.setPostValuePairs(data);
		asyncRequest.start();
	}
	
	
	public void getCarNameList(String placeId, String makerId, String loginId, String searchMode, String onePriceFlg) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GET_ITEM_NAME_ITEMS, this.baseUrl + "RCAGetCarNameList.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		

		data.add(new BasicNameValuePair("SystemKey", RealLuceAuctionApplication.getConfiguration("system_key")));

		data.add(new BasicNameValuePair("PlaceID",placeId));
		data.add(new BasicNameValuePair("MakerID",makerId));
		data.add(new BasicNameValuePair("LoginID",loginId));
		data.add(new BasicNameValuePair("SearchMode",searchMode));
		data.add(new BasicNameValuePair("OnePriceFlag", onePriceFlg));
		asyncRequest.setPostValuePairs(data);

		asyncRequest.start();
	}		
	
	public void getAuctionStatus(String loginID) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GET_AUCTION_STATUS, this.baseUrl + "RCAGetAuctionStatusList.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();

		data.add(new BasicNameValuePair("SystemKey", RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("LoginID",loginID));
		
		String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
		data.add(new BasicNameValuePair("LanguageKbn","1"));
		
		asyncRequest.setPostValuePairs(data);
		
		asyncRequest.start();
	}	
	
	public void getPoster(String memberType) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GET_POSTER, this.baseUrl + "RCAGetPoster.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();

		data.add(new BasicNameValuePair("SystemKey", RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("MemberType",memberType));
		data.add(new BasicNameValuePair("DeviceType","2"));
		asyncRequest.setPostValuePairs(data);
		
		asyncRequest.start();
	}
	
	public void getAnnounce(String memberType) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GET_ANNOUNCE, this.baseUrl + "HP_GetAnnounce.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();

		data.add(new BasicNameValuePair("SystemKey", RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("MemberType",memberType));
		String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
		data.add(new BasicNameValuePair("LanguageKbn","1"));
		asyncRequest.setPostValuePairs(data);
		
		asyncRequest.start();
	}

	public void getBanner() {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GET_BANNER, this.baseUrl + "HP_GetBanner.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();

		data.add(new BasicNameValuePair("SystemKey", RealLuceAuctionApplication.getConfiguration("system_key")));
		String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
		data.add(new BasicNameValuePair("LanguageKbn","1"));
		asyncRequest.setPostValuePairs(data);

		asyncRequest.start();
	}

	public void getStartImage() {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GET_BANNER, this.baseUrl + "HP_GetStartImage.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();

		data.add(new BasicNameValuePair("SystemKey", RealLuceAuctionApplication.getConfiguration("system_key")));
		String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
		data.add(new BasicNameValuePair("LanguageKbn","1"));
		asyncRequest.setPostValuePairs(data);

		asyncRequest.start();
	}


	public void setImage(String placeId, String timesNo, String itemNo, int position, byte[] imageData) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_SET_IMAGE, this.baseUrl + "HP_SetImage.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();

		String imageEncoded;

		if (imageData != null)
			imageEncoded = Base64.encodeToString(imageData,0);
		else
			imageEncoded = "";
		data.add(new BasicNameValuePair("SystemKey", RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("PlaceID",placeId));
		data.add(new BasicNameValuePair("SessionNo",timesNo));
		data.add(new BasicNameValuePair("SellNo",itemNo));
		data.add(new BasicNameValuePair("Position", String.valueOf(position)));
		data.add(new BasicNameValuePair("ImageData",imageEncoded));
		String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
		data.add(new BasicNameValuePair("LanguageKbn","1"));
		asyncRequest.setPostValuePairs(data);

		asyncRequest.start();
	}

	public void requestPassword(String loginID){
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_FORGET_PASSWORD, this.baseUrl + "HP_ForgetPassword.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("SystemKey" , RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("LoginID",loginID));
		asyncRequest.setPostValuePairs(data);
		asyncRequest.start();
	}

	public void sendLog(String loginID, String infoLog,String errorLog){
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_SENDLOG, this.baseUrl + "HP_AppLog.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("SystemKey" , RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("LoginID",loginID));
		data.add(new BasicNameValuePair("InfoLog",infoLog));
		data.add(new BasicNameValuePair("ErrorLog",errorLog));
		asyncRequest.setPostValuePairs(data);
		asyncRequest.start();
	}

	public void getImage(String placeId,String sessionNo,String sellNo, String position){
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GETIMAGE, this.baseUrl + "HP_GetImage.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("SystemKey" , RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("PlaceID",placeId));
		data.add(new BasicNameValuePair("SessionNo",sessionNo));
		data.add(new BasicNameValuePair("SellNo",sellNo));
		data.add(new BasicNameValuePair("Position",position));
		data.add(new BasicNameValuePair("SizeType","verylarge"));
		asyncRequest.setPostValuePairs(data);
		asyncRequest.start();
	}

	public void getCountry(){
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GETCOUNTRY, this.baseUrl + "HP_GetCountry.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("SystemKey" , RealLuceAuctionApplication.getConfiguration("system_key")));
		asyncRequest.setPostValuePairs(data);
		asyncRequest.start();
	}

	public void getSearchInfo(String placeId,String sessionNo,String cornerLst,String medCatLst,String brandLst){
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GETSEARCHINFO, this.baseUrl + "GetSearchInfo.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("SystemKey" , RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("PlaceID",placeId));
		data.add(new BasicNameValuePair("SessionNo",sessionNo));
		data.add(new BasicNameValuePair("CornerList",cornerLst));
		data.add(new BasicNameValuePair("BrandList",brandLst));
		data.add(new BasicNameValuePair("SubCatList",medCatLst));
		data.add(new BasicNameValuePair("LoginID",RealLuceAuctionApplication.loginID));
		asyncRequest.setPostValuePairs(data);
		asyncRequest.start();
	}

	public void updateOkiniri(String placeId,String sessionNo,String sellNo,String loginID,String delFlg){
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_UPDATEOKINIRI, this.baseUrl + "UpdateFavorite.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("SystemKey" , RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("PlaceID",placeId));
		data.add(new BasicNameValuePair("SessionNo",sessionNo));
		data.add(new BasicNameValuePair("ItemNo",sellNo));
		data.add(new BasicNameValuePair("LoginID",loginID));
		data.add(new BasicNameValuePair("DelFlg",delFlg));
		asyncRequest.setPostValuePairs(data);
		asyncRequest.start();
	}

	public void updatePushFlag(String loginID,String pushFlg,String type){
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_UPDATEPUSHFLAG, this.baseUrl + "UpdatePushSetting.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("SystemKey" , RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("LoginID",loginID));
		data.add(new BasicNameValuePair("FavoriteFlg",pushFlg));
		data.add(new BasicNameValuePair("Type",type));
		asyncRequest.setPostValuePairs(data);
		asyncRequest.start();
	}
	public void updatePushFlagWithMail(String loginID,String pushFlg,String type,String mail){
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_UPDATEPUSHFLAG, this.baseUrl + "UpdatePushSetting.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("SystemKey" , RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("LoginID",loginID));
		data.add(new BasicNameValuePair("FavoriteFlg",pushFlg));
		data.add(new BasicNameValuePair("Type",type));
		data.add(new BasicNameValuePair("Mail",mail));
		asyncRequest.setPostValuePairs(data);
		asyncRequest.start();
	}

	public void getPushFlag(String loginID,String type){
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GETPUSHFLAG, this.baseUrl + "GetPushSetting.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("SystemKey" , RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("LoginID",loginID));
		data.add(new BasicNameValuePair("Type",type));
		asyncRequest.setPostValuePairs(data);
		asyncRequest.start();
	}

	public void updateLanguageFlag(String loginID,String lanFlg){
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_UPDATELANGUAGEFLAG, this.baseUrl + "UpdateLanguageSetting.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("SystemKey" , RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("LoginID",loginID));
		data.add(new BasicNameValuePair("LanguageFlg",lanFlg));
		asyncRequest.setPostValuePairs(data);
		asyncRequest.start();
	}

	public void updateAucFlag(String loginID,String lanFlg){
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_UPDATELANGUAGEFLAG, this.baseUrl + "UpdateAucSetting.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("SystemKey" , RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("LoginID",loginID));
		data.add(new BasicNameValuePair("AucFlg",lanFlg));
		asyncRequest.setPostValuePairs(data);
		asyncRequest.start();
	}

	public void getLanguageFlag(String loginID){
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GETLANGUAGEFLAG, this.baseUrl + "GetLanguageSetting.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("SystemKey" , RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("LoginID",loginID));
		asyncRequest.setPostValuePairs(data);
		asyncRequest.start();
	}

	public void getMemoFlag(String loginID){
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GETLANGUAGEFLAG, this.baseUrl + "GetAucSetting.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("SystemKey" , RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("LoginID",loginID));
		asyncRequest.setPostValuePairs(data);
		asyncRequest.start();
	}

	/*
	 * 問い合わせ情報取得
	 */
	public void getInquiry(String loginID,String placeID,String sessionNo,String sellNo){
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GET_INQUIRY, this.baseUrl + "getInquiry.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("SystemKey" , RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("LoginID",loginID));
		data.add(new BasicNameValuePair("PlaceID",placeID));
		data.add(new BasicNameValuePair("SessionNo",sessionNo));
		data.add(new BasicNameValuePair("SellNo",sellNo));
		asyncRequest.setPostValuePairs(data);
		asyncRequest.start();
	}

	/*
	 * 問い合わせ情報登録
	 */
	public void submitInquiry(String loginId, String placeId,
								   String sessionNo, String sellNo, String question) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_SUBMIT_INQUIRY,this.baseUrl + "updateInquiry.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();

		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("LoginID",loginId));
		data.add(new BasicNameValuePair("PlaceID",placeId));
		data.add(new BasicNameValuePair("SessionNo",sessionNo));
		data.add(new BasicNameValuePair("SellNo",sellNo));
		data.add(new BasicNameValuePair("Question",question));

		asyncRequest.setPostValuePairs(data);

		asyncRequest.start();
	}

	/*
	 * メモ登録
	 */
	public void memoRagister(String loginId, String placeId,
							 String sessionNo, String sellNo, String memo) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_MEMO_UPDATE,this.baseUrl + "UpdateMemo.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();

		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("LoginID",loginId));
		data.add(new BasicNameValuePair("PlaceID",placeId));
		data.add(new BasicNameValuePair("SessionNo",sessionNo));
		data.add(new BasicNameValuePair("SellNo",sellNo));
		data.add(new BasicNameValuePair("Memo",memo));

		asyncRequest.setPostValuePairs(data);

		asyncRequest.start();
	}

	public void getRestTime(String lane_id) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GET_RESTTIME,this.baseUrl + "GetRestTime.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();

		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("LaneId",lane_id));
		asyncRequest.setPostValuePairs(data);

		asyncRequest.start();
	}


	protected void finalize() throws Throwable {
        super.finalize();
	}
	
	/**
	 * Push One Price

	 * @return HttpServiceCallback: auto
	 */
	public void registOnePrice(String placeId, String timesNo,
			String itemNo, String loginId, String price) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_REGISTER_ONE_PRICE,this.baseUrl + "RCARegistOnePriceBid.php",this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		
		data.add(new BasicNameValuePair("SystemKey",RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("PlaceID",placeId));
		data.add(new BasicNameValuePair("TimesNo",timesNo));
		data.add(new BasicNameValuePair("ItemNo", itemNo));
		data.add(new BasicNameValuePair("LoginID",loginId));
		data.add(new BasicNameValuePair("Price",price));

		String langKbn = Locale.getDefault().getLanguage().equals("ja") ? "0" : "1";
		data.add(new BasicNameValuePair("LanguageKbn","1"));
		asyncRequest.setPostValuePairs(data);

		asyncRequest.start();
	}

	/**
	 * 保留交渉情報を取得
	 * @param placeId
	 * @param sessionNo
	 * @param sellNo
	 */
	public void getNegotiationInfo(String placeId, String sessionNo, String sellNo) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GET_NEGOTIATION, this.baseUrl + "GetNegotiationInfo.php", this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();

		data.add(new BasicNameValuePair("SystemKey", RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("PlaceID", placeId));
		data.add(new BasicNameValuePair("SessionNo", sessionNo));
		data.add(new BasicNameValuePair("SellNo", sellNo));
		data.add(new BasicNameValuePair("LoginID", RealLuceAuctionApplication.loginID));
		asyncRequest.setPostValuePairs(data);

		asyncRequest.start();
	}

	/**
	 * 交渉価格を更新
	 * @param placeId
	 * @param sessionNo
	 * @param sellNo
	 * @param holdPrice
	 * @param resultFlg
	 */
	public void updateSellingResult(String placeId, String sessionNo, String sellNo, String holdPrice, String holdFlg,String resultFlg) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_REGISTER_SELLING_RESULT, this.baseUrl + "UpdateSellingResult.php", this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();

		data.add(new BasicNameValuePair("SystemKey", RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("PlaceID", placeId));
		data.add(new BasicNameValuePair("SessionNo", sessionNo));
		data.add(new BasicNameValuePair("SellNo", sellNo));
		data.add(new BasicNameValuePair("HoldPrice", holdPrice));
		data.add(new BasicNameValuePair("HoldFlg", holdFlg));
		data.add(new BasicNameValuePair("ResultFlg", resultFlg));
		data.add(new BasicNameValuePair("LoginId", RealLuceAuctionApplication.loginID));
		asyncRequest.setPostValuePairs(data);

		asyncRequest.start();
	}

	public void updateReholdPrice(String placeId, String sessionNo, String sellNo, String reholdPrice) {
		AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_UPDATE_REHOLDPRICE, this.baseUrl + "UpdateReHoldPrice.php", this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();

		data.add(new BasicNameValuePair("SystemKey", RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("PlaceID", placeId));
		data.add(new BasicNameValuePair("SessionNo", sessionNo));
		data.add(new BasicNameValuePair("SellNo", sellNo));
		data.add(new BasicNameValuePair("ReHoldPrice", reholdPrice));
		data.add(new BasicNameValuePair("LoginId", RealLuceAuctionApplication.loginID));
		asyncRequest.setPostValuePairs(data);

		asyncRequest.start();
	}

	public void getSeisansho(String placeId, String sessionNo, String loginId){
			AsyncRequest asyncRequest = AsyncRequest.createAsyncRequestInstance(COMMAND_GETSEISANSHO, this.baseUrl + "HP_GetSeisansho.php", this);
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("SystemKey", RealLuceAuctionApplication.getConfiguration("system_key")));
		data.add(new BasicNameValuePair("PlaceID", placeId));
		data.add(new BasicNameValuePair("SessionNo", sessionNo));
		data.add(new BasicNameValuePair("LoginID", loginId));
		asyncRequest.setPostValuePairs(data);

		asyncRequest.start();
	}
			
}