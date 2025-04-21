package tokiokaability.auction24.HTTPInfo;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import tokiokaability.auction24.RealLuceAuctionApplication;
import tokiokaability.auction24.socket.client.ActivedSocketClient;
import tokiokaability.auction24.socket.client.DeactivedSocketClient;
import tokiokaability.auction24.socket.info.PlaceBitInfo;


public class LaneInfo extends BaseInfo {
	
	
	public static String Name_result = "result";
	public static String Name_Status = "Status";
	public static String Name_Lane = "Lane";
	public static String Name_LaneID = "LaneID";
	public static String Name_LaneMark = "LaneMark";
	public static String Name_LaneNo = "LaneNo";
	public static String Name_PortNo = "PortNo";
	public static String Name_PortNoSimple = "PortNoSimple";
	public static String Name_ItemNo = "ItemNo";
	public static String Name_CornerName = "CornerName";
	public static String Name_TimesNo = "TimesNo";
    public static String Name_GatewayIP = "GatewayIP";
	public static String Name_VoiceServerIP = "VoiceServerIP";
	public static String Name_VoicePortNo = "VoicePortNo";
	
	private String placeId = "";
	private String timesNo = "";
    private static String[] gatewayIP = new String[0];
	private static String voiceServerIP;

	private int voicePortNo = 0;
	//Lane
	private String laneId = "0";
	private String laneMark = "";
	private String laneNo = "0";
	private int portNo = 0;
	private int portNoSimple = 0;
	private String itemNo = "";
	private String CornerName = "";
	//Car

	private ItemInfo currentItem = null;
	private ItemInfo nextItem = null;
	
	//status
	private boolean isOpen = true;
	private long currentPrice = 0;
	private long startPrice = 0;
	private DeactivedSocketClient deactiveSocket = null;
	private ActivedSocketClient activeSocket = null;
	private int currentProductNo = 0;
	
	
	

	public int getCurrentProductNo() {
		return currentProductNo;
	}
	public void setCurrentProductNo(int currentProductNo) {
		this.currentProductNo = currentProductNo;
	}

	/*
	public DeactivedSocketClient getDeactiveSocket() {
		return deactiveSocket;
	}
	public void setDeactiveSocket(DeactivedSocketClient deactiveSocket) {
		this.deactiveSocket = deactiveSocket;
	}
	*/
	public ActivedSocketClient getActiveSocket() {
		return activeSocket;
	}
	public void setActiveSocket(ActivedSocketClient activeSocket) {
		this.activeSocket = activeSocket;
	}
	public long getStartPrice() {
		return startPrice;
	}
	public void setStartPrice(long startPrice) {
		this.startPrice = startPrice;
	}
	public long getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(long currentPrice) {
		this.currentPrice = currentPrice;
	}
	public boolean isOpen() {
		return isOpen;
	}
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
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

	
	
	public String getCornerName() {
		return CornerName;
	}
	public void setCornerName(String cornerName) {
		CornerName = cornerName;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getLaneId() {
		return laneId;
	}
	public void setLaneId(String laneId) {
		this.laneId = laneId;
	}
	public String getLaneMark() {
		return laneMark;
	}
	public void setLaneMark(String laneMark) {
		this.laneMark = laneMark;
	}
	public String getLaneNo() {
		return laneNo;
	}
	public void setLaneNo(String laneNo) {
		this.laneNo = laneNo;
	}
	public int getPortNo() {
		return portNo;
	}
	public void setPortNo(int portNo) {
		this.portNo = portNo;
	}
	public int getPortNoSimple() {
		return portNoSimple;
	}
	public void setPortNoSimple(int portNoSimple) {
		this.portNoSimple = portNoSimple;
	}

    public static String[] getGatewayIP() {
        return gatewayIP;
    }
    public static void setGatewayIP(String[] gatewayIP) {
        LaneInfo.gatewayIP = gatewayIP;
    }

	public static String getVoiceServerIP(){return  voiceServerIP;}
	public static void setVoiceServerIP(String voiceServerIP){LaneInfo.voiceServerIP = voiceServerIP;}

	public int getVoicePortNo(){return this.voicePortNo;}
	public void setVoicePortNo(int voicePortNo){this.voicePortNo = voicePortNo;}

    public ItemInfo getCurrentItem() {
		return currentItem;
	}
	public void setCurrentItem(ItemInfo currentItem) {
		this.currentItem = currentItem;
	}
	public ItemInfo getNextItem() {
		return nextItem;
	}
	public void setNextItem(ItemInfo nextItem) {
		this.nextItem = nextItem;
	}
	public LaneInfo(List<NameValuePair> parserResult) {

		for (NameValuePair nvp:parserResult){

			if(nvp.getName().equalsIgnoreCase(Name_LaneID)){
				this.setLaneId(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_LaneMark)){
				this.setLaneMark(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_LaneNo)){
				this.setLaneNo(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_TimesNo)){
				this.setTimesNo(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_PortNo)){
				this.setPortNo(Integer.parseInt( nvp.getValue()));
			}
			else if(nvp.getName().equalsIgnoreCase(Name_PortNoSimple)){
				this.setPortNoSimple(Integer.parseInt( nvp.getValue())); 
			}
			else if(nvp.getName().equalsIgnoreCase(Name_ItemNo)){
				this.setItemNo( nvp.getValue()); 
			}
			else if(nvp.getName().equalsIgnoreCase(Name_CornerName)){
				this.setCornerName( nvp.getValue()); 
			}
			else if(nvp.getName().equalsIgnoreCase(Name_VoicePortNo)){
				if (nvp.getValue() != null && nvp.getValue().length() > 0)
					this.setVoicePortNo(Integer.parseInt( nvp.getValue()));
				else
					this.setVoicePortNo(0);
			}
		}
		
	}
	
    public static List<LaneInfo> getList(String data, String placeId, String timesNo)throws XMLParserException,InvalidDataException, ServerErrorException{
    	List<LaneInfo> placeInfoList = new ArrayList<LaneInfo>();
    	
    	String tagName = null;
		String tagValue = null;
		
		List<NameValuePair> parserResult = null;
		try{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
		    
			xpp.setInput(new StringReader(data));
	
			int eventType=xpp.getEventType();
        
			while(eventType!=XmlPullParser.END_DOCUMENT){
				if(eventType == XmlPullParser.START_TAG) {
		        	tagName = xpp.getName();
		        	if (tagName.equalsIgnoreCase(Name_Lane)){
		        		parserResult  = new ArrayList<NameValuePair>();
		        	}
		        	
		        }else if(eventType == XmlPullParser.TEXT) {
		            tagValue = xpp.getText();
		        }
		        else if (eventType == XmlPullParser.END_TAG){
		        	if(xpp.getName().equalsIgnoreCase(Name_Status)){
						if (Integer.parseInt(tagValue) == -1)
							throw new ServerErrorException("Server Error");
					}
		        	else if(xpp.getName().equalsIgnoreCase(Name_Lane)){
		        		LaneInfo obj = new LaneInfo(parserResult);
		        		obj.setPlaceId(placeId);
		        		obj.setTimesNo(timesNo);
		        		obj.setOpen(true);
	        			placeInfoList.add(obj);
		        	}
                    else if(xpp.getName().equalsIgnoreCase(Name_GatewayIP)){
                        String IPs = tagValue;
                        setGatewayIP(IPs.split("\\|"));
                    }
					else if(xpp.getName().equalsIgnoreCase(Name_VoiceServerIP)){
						setVoiceServerIP(tagValue);
					}
		        	else{
			        	if(parserResult != null && (tagName != null)&&(tagValue != null)){
							parserResult.add(new BasicNameValuePair(tagName,tagValue));
							
						}
		        	}
		        	tagName = null;
		        	tagValue = null;

		        }
				
		        eventType = xpp.next();
			} 
			
			
		}catch(XmlPullParserException ex){
			ex.printStackTrace();
			throw new XMLParserException(ex.getMessage());
		}
		catch(ServerErrorException ex){
			ex.printStackTrace();
			throw ex;
		}
		catch(IOException ex){
			ex.printStackTrace();
			throw new XMLParserException(ex.getMessage());
		}
    	return placeInfoList;
    }
	
    

	/**
	 * プッシュ通知処理
	 */
    public boolean placeBit(PlaceInfo place){
    	PlaceBitInfo placeBitInfo = new PlaceBitInfo();
    	
    	placeBitInfo.setCode("11");
    	placeBitInfo.setItemNo(this.getCurrentItem().getItemNo());
    	placeBitInfo.setLaneNo(Short.parseShort( this.getLaneNo()));
    	placeBitInfo.setPlaceNo(Short.parseShort(place.getPlaceNo()));
    	placeBitInfo.setPrice(this.getCurrentPrice());
    	placeBitInfo.setMemberNo(Integer.parseInt(place.getMemberNo()));
    	placeBitInfo.setMemberID(String.format("%-"+20+"s",RealLuceAuctionApplication.loginID));
    	return activeSocket.send(placeBitInfo.getRawData());
    }

    
    
}
