package tokiokaability.auction.HTTPInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import tokiokaability.auction.RealLuceAuctionApplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AsyncRequest extends Thread {
	private int command;
	private List<NameValuePair> postValuePairs;
	private String url;
	private HTTPService httpService;
	private static List<AsyncRequest> asyncRequests = null;
	private String tag;

	public static List<AsyncRequest> getAsyncRequests() {
		return asyncRequests;
	}

	
	public static void setAsyncRequests(List<AsyncRequest> asyncRequests) {
		AsyncRequest.asyncRequests = asyncRequests;
	}
	public void setPostValuePairs(List<NameValuePair> postValuePairs) {
		this.postValuePairs = postValuePairs;
	}

	public List<NameValuePair> getPostValuePairs() {
		return this.postValuePairs;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public String getURL(){
		return url;
	}
	
	public void setURL(String url){
		this.url = url;
	}


	public AsyncRequest(int command, String url, HTTPService service) {
		this.command = command;
		this.url = url;
		this.httpService = service;
	}

	public static AsyncRequest createAsyncRequestInstance(int command,String url, HTTPService service) {
		if (asyncRequests == null)
			asyncRequests = new ArrayList<AsyncRequest>();
		AsyncRequest asyncRequest = new AsyncRequest(command, url, service);
		asyncRequest.setPriority(7);
		asyncRequests.add(asyncRequest);
		return asyncRequest;
	}
	
	@Override
	public void start(){
		this.setTag(UUID.randomUUID().toString());
		super.start();
	}
	
	
	// Implement interface
	@Override
	public void run() {
		Message msg = new Message();
		int returnErrorCode = HTTPService.NO_ERROR;
		String returnString = "";
		
		Log.d("run", url);
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 30000);
		HttpConnectionParams.setSoTimeout(params, 30000);
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(postValuePairs,HTTP.UTF_8));
			HttpResponse response = httpClient.execute(httpPost);
			returnString = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException ex) {
			ex.printStackTrace();
			returnErrorCode = HTTPService.ERROR_NETWORK;
		} catch (IOException ex) {
			ex.printStackTrace();
			returnErrorCode = HTTPService.ERROR_NETWORK;
		} catch (Exception ex) {
			ex.printStackTrace();
			returnErrorCode = HTTPService.ERROR_UNKNOW;
		}

		Bundle bundle = new Bundle();
		bundle.putString("returnString", returnString);
		bundle.putString("tag", this.getTag());
		msg.setData(bundle);
		
		if (!Thread.currentThread().isInterrupted()){
			msg.what = returnErrorCode;
			//Log.d("run1", ""+msg.what);
			handlerService.sendMessage(msg);
		}
		else{
			msg.what = HTTPService.ERROR_CANCEL;
			//Log.d("run2", ""+msg.what);
			handlerService.sendMessage(msg);
		}
	}
	
	
	private final static Handler handlerService = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			String tag = bundle.getString("tag");
			AsyncRequest obj = null;
			int returnCode = msg.what;
			
			//Log.d("VAO obj", ""+obj+" tag : "+tag);
			
			//Log.d("VAO DAY111111", RealLuceAuctionApplication.baseURL+msg.what);
			
			boolean isReturnData = false;
			
			if (returnCode == HTTPService.ERROR_NETWORK || returnCode == HTTPService.ERROR_UNKNOW || returnCode == HTTPService.ERROR_CANCEL) {
				
				//Log.d("VAO DAY3333", RealLuceAuctionApplication.baseURL);
				
				if (RealLuceAuctionApplication.isDefaultURL) {
					if (RealLuceAuctionApplication.isConneted == false) {
						RealLuceAuctionApplication.isConneted = true;
						//Log.d("VAO DAY", "1");
						isReturnData = true;
						HTTPService.countService = 0;
					}
					else {
						RealLuceAuctionApplication.isConneted = false;
						HTTPService.restartAllServices(RealLuceAuctionApplication.isDefaultURL);
						RealLuceAuctionApplication.baseURL = RealLuceAuctionApplication.defaultBaseURL;
					}
				}
				else {
					RealLuceAuctionApplication.isConneted = false;
					HTTPService.restartAllServices(RealLuceAuctionApplication.isDefaultURL);
					RealLuceAuctionApplication.baseURL = RealLuceAuctionApplication.defaultBaseURL;
				}
			}
			else {
				
				
				isReturnData = true;
				
				//Log.d("VAODAY 22323232",RealLuceAuctionApplication.baseURL);
				
				RealLuceAuctionApplication.isConneted = true;
				if (RealLuceAuctionApplication.baseURL != RealLuceAuctionApplication.defaultBaseURL) {
					RealLuceAuctionApplication.isDefaultURL = false;
				}
			}


			if (isReturnData) {
				
				for(int i= 0;i< asyncRequests.size();i++){
					
					Log.d("TAG",asyncRequests.get(i).getTag()+" Tag"+ tag +" object "+asyncRequests.get(i));
					
					if (asyncRequests.get(i).getTag().equalsIgnoreCase(tag)){
						
						
						obj = asyncRequests.get(i);
						//Log.d("VAO TRONG", "OK");
						asyncRequests.remove(obj);
						break;
					}
				}
				
				//Log.d("VAO DAY1111112232322", ""+obj);
				
				if (obj !=null){
					obj.returnData(bundle.getString("returnString"), returnCode);
				}
	
				obj = null;
				super.handleMessage(msg);
			}
		}
	};
	


	public void returnData(String returnString, int returnCode){
		Object returnObject = null;
		int returnErrorCode = HTTPService.NO_ERROR;
		
		
		Log.d("returnData", ""+returnCode);
		
		if (returnCode == HTTPService.NO_ERROR){
			try{
				switch (command) {
					case HTTPService.COMMAND_SYSTEM_INFOR:
						returnObject = new SystemInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
			
					case HTTPService.COMMAND_LOGIN:
						returnObject = new LoginInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
			
					case HTTPService.COMMAND_CHECK_LOGIN:
						returnObject = new LoginInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
			
					case HTTPService.COMMAND_GET_PLACE:
						returnObject = (List<PlaceInfo>) PlaceInfo.getList(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
			
					case HTTPService.COMMAND_GET_LANES:
						returnObject =  LaneInfo.getList(returnString, httpService.getPlaceId(), httpService.getTimesNo());
						returnErrorCode = HTTPService.NO_ERROR;
						break;
			
					case HTTPService.COMMAND_GET_ITEM:
						httpService.getObjItem().appendItemInfo(returnString);
						returnObject = httpService.getObjItem();
						returnErrorCode = HTTPService.NO_ERROR;
						break;
			
					case HTTPService.COMMAND_GET_ITEM_IMAGE:
						httpService.getObjItem().appendItemInfo(returnString);
						returnObject = httpService.getObjItem();
						returnErrorCode = HTTPService.NO_ERROR;
						break;
			
					case HTTPService.COMMAND_GET_ITEM_SPEC:
						httpService.getObjItem().appendItemInfo(returnString);
						returnObject = httpService.getObjItem();
						returnErrorCode = HTTPService.NO_ERROR;
						break;
			
					case HTTPService.COMMAND_GET_ITEMS:
						returnObject = ItemInfo.getList(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
			
					case HTTPService.COMMAND_GET_CORNERS:
						returnObject =  CornerInfo.getList(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					case HTTPService.COMMAND_GET_ITEMS_DETAIL:
						httpService.getObjItem().appendItemInfo(returnString);
						returnObject = httpService.getObjItem();
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					case HTTPService.COMMAND_REGISTER_BID_OFFLINE:
						returnObject = new BaseInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					case HTTPService.COMMAND_REGISTER_ITEM_MARK:
						returnObject = new BaseInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
						
					case HTTPService.COMMAND_REGISTER_APN_TOKEN:
						returnObject = new BaseInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
						
					case HTTPService.COMMAND_REGISTER_AGREEMENT:
						returnObject = new BaseInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
						
					case HTTPService.COMMAND_GET_MEMBER_TYPE:
						returnObject = new LoginInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;

					case HTTPService.COMMAND_GET_MAKERS_ITEMS:
						returnObject = ManufacturerInfo.getManufacturerList(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;

					case HTTPService.COMMAND_GET_ITEM_NAME_ITEMS:
						returnObject = CarNameInfo.getCarNameList(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
						
					case HTTPService.COMMAND_GET_AUCTION_STATUS:
						returnObject = PlaceInfo.getAuctionStatus(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;	
						
					case HTTPService.COMMAND_GET_POSTER:
						returnObject = new PosterInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
						
					case HTTPService.COMMAND_GET_ANNOUNCE:
						returnObject = new AnnounceInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;

					case HTTPService.COMMAND_GET_BANNER:
						returnObject = new BannerInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					
					case HTTPService.COMMAND_PREVIEW_REQUEST:
						returnObject = PreviewRequestInfo.getList(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
						
					case HTTPService.COMMAND_REGIST_PREVIEW_REQUEST:
						returnObject = new BaseInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
						
					case HTTPService.COMMAND_GET_REQUEST_RULE_URL:
						returnObject = new RuleURLInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
						
					case HTTPService.COMMAND_REGISTER_ONE_PRICE:
						returnObject = new BaseInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;

					case HTTPService.COMMAND_SET_IMAGE:
						returnObject = new BaseInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					case HTTPService.COMMAND_FORGET_PASSWORD:
						returnObject = new MailsendInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					case HTTPService.COMMAND_SENDLOG:
						returnObject = new SendLogInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					case HTTPService.COMMAND_GETIMAGE:
						returnObject = new ImageInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					case HTTPService.COMMAND_GETCOUNTRY:
						returnObject = (List<CountryInfo>)CountryInfo.getList(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					case HTTPService.COMMAND_GETSEARCHINFO:
						returnObject = new SearchInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					case HTTPService.COMMAND_UPDATEOKINIRI:
						//returnObject = new SearchInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					case HTTPService.COMMAND_UPDATEPUSHFLAG:
						//returnObject = new SearchInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					case HTTPService.COMMAND_GETPUSHFLAG:
						returnObject = new PushFlgInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					case HTTPService.COMMAND_UPDATELANGUAGEFLAG:
						//returnObject = new SearchInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					case HTTPService.COMMAND_GETLANGUAGEFLAG:
						returnObject = new PushFlgInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					case HTTPService.COMMAND_GET_INQUIRY:
						returnObject = new InquiryInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					case HTTPService.COMMAND_SUBMIT_INQUIRY:
						returnObject = new BaseInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					case HTTPService.COMMAND_GET_RESULT_DISPLAY_INTERVAL:
						returnObject = new ResultDisplayIntervalInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					case HTTPService.COMMAND_MEMO_UPDATE:
						returnObject = new ResultDisplayIntervalInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					case HTTPService.COMMAND_GET_RESTTIME:
						returnObject = new RestInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					case HTTPService.COMMAND_GET_NEGOTIATION:
						returnObject = new NegotiationInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					case HTTPService.COMMAND_REGISTER_SELLING_RESULT:
						returnObject = new BaseInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					case HTTPService.COMMAND_UPDATE_REHOLDPRICE:
						returnObject = new BaseInfo(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					case HTTPService.COMMAND_GETSEISANSHO:
						returnObject = ItemInfo.getList(returnString);
						returnErrorCode = HTTPService.NO_ERROR;
						break;
					default:
						returnErrorCode = HTTPService.ERROR_UNKNOW;
						break;
				}
			}catch (XMLParserException e) {
				e.printStackTrace();
				returnErrorCode = HTTPService.ERROR_XMLPARSER;
			} catch (InvalidDataException e) {
				e.printStackTrace();
				returnErrorCode = HTTPService.ERROR_XMLPARSER;
			} catch (ServerErrorException e) {
				e.printStackTrace();
				returnErrorCode = HTTPService.ERROR_UNKNOW;
			}
		}
		else{
			returnErrorCode = returnCode;
			returnObject = null;
		}
		
		if (httpService.getCallback() != null){
			if (returnErrorCode == HTTPService.NO_ERROR) {
				httpService.getCallback().onSucessfully(returnObject);
			} else {
				httpService.getCallback().onFailed(returnErrorCode);
			}
			
			//httpService.setCallback(null);
			
		}
		httpService = null;
		
	}
	
	
	

	
	protected void finalize() throws Throwable {
        super.finalize();
	}

}