package tokiokaability.auction.HTTPInfo;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


public class PlaceInfo extends BaseInfo {
	
	static String Name_result = "result";
	static String Name_Status = "Status";
	static String Name_Place = "Place";
	static String Name_PlaceID = "PlaceID";
	static String Name_PlaceNo = "PlaceNo";
	static String Name_PlaceName = "PlaceName";
	static String Name_MemberNo = "MemberNo";
	static String Name_TimesNo = "TimesNo";
	static String Name_AuthorityFlag = "AuthorityFlag";
	static String Name_HoldDate = "HoldDate";
	static String Name_StartTime = "HoldingStartTime";
	static String Name_Campaign = "Campaign";
	static String Name_HoldDate2 = "HoldDate2";
	static String Name_StartTime2 = "HoldingStartTime2";
	static String Name_Campaign2 = "Campaign2";
	static String Name_HoldDate3 = "HoldDate3";
	static String Name_StartTime3 = "HoldingStartTime3";
	static String Name_Campaign3 = "Campaign3";
	static String Name_HoldDate4 = "HoldDate4";
	static String Name_StartTime4 = "HoldingStartTime4";
	static String Name_Campaign4 = "Campaign4";
	static String Name_HoldDate5 = "HoldDate5";
	static String Name_StartTime5 = "HoldingStartTime5";
	static String Name_Campaign5 = "Campaign5";
	static String Name_CreditLimit = "CreditLimit";

	static String Name_OutsideOpenFlag = "OutsideOpenFlag";
	static String Name_RealFlag = "RealFlag";
	static String Name_OpenedFlag = "OpenedFlag";
	static String Name_VoicePortNo = "VoicePortNo";
	static String Name_SellCount = "SellCount";
	static  String Name_AuctionEndFlg = "AuctionEndFlag";
	static  String Name_AuctionEndFlg5 = "AuctionEndFlagHoseki";
	static  String Name_AuctionStartFlg = "AuctionStartFlag";
	
	static String Name_AuctionStatus = "AuctionStatus";

	static String Name_OnePriceFlg = "OnePriceFlag";
	
	
    public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getPlaceNo() {
		return placeNo;
	}
	public void setPlaceNo(String placeNo) {
		this.placeNo = placeNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}
	public String getTimesNo() {
		return timesNo;
	}
	public void setTimesNo(String timesNo) {
		this.timesNo = timesNo;
	}
	public String getAuthorityFlag() {
		return authorityFlag;
	}
	public void setAuthorityFlag(String authorityFlag) {
		this.authorityFlag = authorityFlag;
	}
	public String getHoldDate() {
		return holdDate;
	}
	public void setHoldDate(String holdDate) {
		this.holdDate = holdDate;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}
	public String getCampaign() {
		return campaign;
	}
	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}
	public String getOutsideOpenFlag() {
		return outsideOpenFlag;
	}
	public void setOutSideOpenFlag(String outsideOpenFlag) {
		this.outsideOpenFlag = outsideOpenFlag;
	}
	public String getRealFlag() {
		return realFlag;
	}
	public void setRealFlag(String realFlag) {
		this.realFlag = realFlag;
	}
	public String getOpenedFlag() {
		return openedFlag;
	}
	public void setOpenedFlag(String openedFlag) {
		this.openedFlag = openedFlag;
	}
	public String getVoicePortNo() {
		return voicePortNo;
	}
	public void setVoicePortNo(String voicePortNoValue) {
		this.voicePortNo = voicePortNoValue;
	}
	public List<LaneInfo> getLanes() {
		return lanes;
	}
	public void setLanes(List<LaneInfo> lanes) {
		this.lanes = lanes;
	}
	
	public String getSellCount() {
		return sellCount;
	}
	public void setSellCount(String sellCount) {
		this.sellCount = sellCount;
	}
	public String getOnePriceFlg() {
		return onePriceFlg;
	}
	public void setOnePriceFlg(String onePriceFlag) {
		this.onePriceFlg = onePriceFlag;
	}
	public String getAuctionEndFlg() {
		return auctionEndFlg;
	}
	public void setAuctionEndFlg(String auctionEndFlg) {
		this.auctionEndFlg = auctionEndFlg;
	}

	public String getHoldDate2() {
		return holdDate2;
	}

	public void setHoldDate2(String holdDate2) {
		this.holdDate2 = holdDate2;
	}

	public String getStartTime2() {
		return startTime2;
	}

	public void setStartTime2(String startTime2) {
		this.startTime2 = startTime2;
	}

	public String getCampaign2() {
		return campaign2;
	}

	public void setCampaign2(String campaign2) {
		this.campaign2 = campaign2;
	}

	public String getHoldDate3() {
		return holdDate3;
	}

	public void setHoldDate3(String holdDate3) {
		this.holdDate3 = holdDate3;
	}

	public String getStartTime3() {
		return startTime3;
	}

	public void setStartTime3(String startTime3) {
		this.startTime3 = startTime3;
	}

	public String getCampaign3() {
		return campaign3;
	}

	public void setCampaign3(String campaign3) {
		this.campaign3 = campaign3;
	}

	public String getHoldDate4() {
		return holdDate4;
	}

	public void setHoldDate4(String holdDate4) {
		this.holdDate4 = holdDate4;
	}

	public String getStartTime4() {
		return startTime4;
	}

	public void setStartTime4(String startTime4) {
		this.startTime4 = startTime4;
	}

	public String getCampaign4() {
		return campaign4;
	}

	public void setCampaign4(String campaign4) {
		this.campaign4 = campaign4;
	}

	public String getHoldDate5() {
		return holdDate5;
	}

	public void setHoldDate5(String holdDate5) {
		this.holdDate5 = holdDate5;
	}

	public String getStartTime5() {
		return startTime5;
	}

	public void setStartTime5(String startTime5) {
		this.startTime5 = startTime5;
	}

	public String getCampaign5() {
		return campaign5;
	}

	public void setCampaign5(String campaign5) {
		this.campaign5 = campaign5;
	}

	String idNo = "0";
    String placeNo = "0";
    String name = "";
    String memberNo = "0";
    String timesNo = "0";
    String authorityFlag = "0";

    String holdDate = "";
    String startTime = "";
	String campaign = "";

	String holdDate2 = "";
	String startTime2 = "";
	String campaign2 = "";

	String holdDate3 = "";
	String startTime3 = "";
	String campaign3 = "";

	String holdDate4 = "";
	String startTime4 = "";
	String campaign4 = "";

	String holdDate5 = "";
	String startTime5 = "";
	String campaign5 = "";

    String creditLimit = "";

	String outsideOpenFlag = "0";
    String realFlag = "0";
	String openedFlag = "0";
    String voicePortNo ="0";
    String sellCount = "";

	public String getAuctionStartFlg() {
		return auctionStartFlg;
	}

	public void setAuctionStartFlg(String auctionStartFlg) {
		this.auctionStartFlg = auctionStartFlg;
	}

	String onePriceFlg = "0";



	String auctionEndFlg = "0";
	String auctionEndFlg5 = "0";
	String auctionStartFlg = "0";
    List<LaneInfo> lanes = null;

	public String getAuctionEndFlg5() {
		return auctionEndFlg5;
	}

	public void setAuctionEndFlg5(String auctionEndFlg5) {
		this.auctionEndFlg5 = auctionEndFlg5;
	}

	public PlaceInfo(){
    	
    }
    
    
    public PlaceInfo(List<NameValuePair> parserResult) {

		for (NameValuePair nvp:parserResult){

			if(nvp.getName().equalsIgnoreCase(Name_PlaceID)){
				this.setIdNo(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_PlaceNo)){
				this.setPlaceNo(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_PlaceName)){
				this.setName(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_MemberNo)){
				this.setMemberNo(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_TimesNo)){
				this.setTimesNo(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_AuthorityFlag)){
				this.setAuthorityFlag(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_HoldDate)){
				this.setHoldDate(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_StartTime)){
				this.setStartTime(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_Campaign)){
				this.setCampaign(nvp.getValue());
			}

			else if(nvp.getName().equalsIgnoreCase(Name_HoldDate2)){
				this.setHoldDate2(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_StartTime2)){
				this.setStartTime2(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_Campaign2)){
				this.setCampaign2(nvp.getValue());
			}

			else if(nvp.getName().equalsIgnoreCase(Name_HoldDate3)){
				this.setHoldDate3(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_StartTime3)){
				this.setStartTime3(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_Campaign3)){
				this.setCampaign3(nvp.getValue());
			}

			else if(nvp.getName().equalsIgnoreCase(Name_HoldDate4)){
				this.setHoldDate4(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_StartTime4)){
				this.setStartTime4(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_Campaign4)){
				this.setCampaign4(nvp.getValue());
			}

			else if(nvp.getName().equalsIgnoreCase(Name_HoldDate5)){
				this.setHoldDate5(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_StartTime5)){
				this.setStartTime5(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_Campaign5)){
				this.setCampaign5(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_CreditLimit)){
				this.setCreditLimit(nvp.getValue());
			}

			else if(nvp.getName().equalsIgnoreCase(Name_OutsideOpenFlag)){
				this.setOutSideOpenFlag(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_RealFlag)){
				this.setRealFlag(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_OpenedFlag)){
				this.setOpenedFlag(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_VoicePortNo)){
				this.setVoicePortNo(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_SellCount)){
				this.setSellCount(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_OnePriceFlg)){
				String stringVal = nvp.getName();
				if (stringVal != null && !stringVal.equals("")) {
					this.setOnePriceFlg(nvp.getValue());
				}
				else {
					this.setOnePriceFlg("0");
				}
				
			}
			else if(nvp.getName().equalsIgnoreCase(Name_AuctionEndFlg)){
				this.setAuctionEndFlg(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_AuctionEndFlg5)){
				this.setAuctionEndFlg5(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_AuctionStartFlg)){
				this.setAuctionStartFlg(nvp.getValue());
			}
		}
		
	}
    
    
    
    
    public static List<PlaceInfo> getList(String data)throws XMLParserException,InvalidDataException, ServerErrorException{
    	List<PlaceInfo> placeInfoList = new ArrayList<PlaceInfo>();
    	
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
		        	if (tagName.equalsIgnoreCase(Name_Place)){
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
		        	else if(xpp.getName().equalsIgnoreCase(Name_Place)){
	        			placeInfoList.add(new PlaceInfo(parserResult));
		        	}
		        	else{
			        	if((tagName != null)&&(tagValue != null)){
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
    
    
    
    
    public static List<PlaceInfo> getAuctionStatus(String data)throws XMLParserException,InvalidDataException, ServerErrorException{
    	List<PlaceInfo> placeInfoList = new ArrayList<PlaceInfo>();
    	List<LaneInfo> laneInfoList = new ArrayList<LaneInfo>();
    	
    	String tagName = null;
		String tagValue = null;
		
		List<NameValuePair> parserResult = null;
		List<NameValuePair> parserResultLane = null;
		try{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
		    
			xpp.setInput(new StringReader(data));
	
			int eventType=xpp.getEventType();
        
			while(eventType!=XmlPullParser.END_DOCUMENT){
				if(eventType == XmlPullParser.START_TAG) {
		        	tagName = xpp.getName();
		        	if (tagName.equalsIgnoreCase(Name_Place)){
		        		parserResult  = new ArrayList<NameValuePair>();
		        		parserResultLane = new ArrayList<NameValuePair>();
		        		laneInfoList = new ArrayList<LaneInfo>();
		        	}
		        	else if (tagName.equalsIgnoreCase(LaneInfo.Name_Lane)){
		        		parserResultLane = new ArrayList<NameValuePair>();
		        	}
		        	
		        }else if(eventType == XmlPullParser.TEXT) {
		            tagValue = xpp.getText();
		        }
		        else if (eventType == XmlPullParser.END_TAG){
		        	if(xpp.getName().equalsIgnoreCase(Name_Status)){
						if (Integer.parseInt(tagValue) == -1)
							throw new ServerErrorException("Server Error");
					}
		        	else if(xpp.getName().equalsIgnoreCase(Name_Place)){
		        		PlaceInfo tmp = new PlaceInfo(parserResult);
		        		tmp.setLanes(laneInfoList);
	        			placeInfoList.add(tmp);
		        	}
		        	else if(xpp.getName().equalsIgnoreCase(LaneInfo.Name_Lane)){
		        		laneInfoList.add(new LaneInfo(parserResultLane));
		        	}
		        	else{
			        	if(parserResult != null && parserResultLane!= null && (tagName != null)&&(tagValue != null)){
							parserResult.add(new BasicNameValuePair(tagName,tagValue));
							parserResultLane.add(new BasicNameValuePair(tagName,tagValue));
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
    
    
    
    
    
}
