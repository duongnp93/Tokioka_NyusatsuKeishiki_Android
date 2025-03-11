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


public class CornerInfo extends BaseInfo {
	
	
	static String Name_result = "result";
	static String Name_Status = "Status";
	static String Name_Corner = "Corner";
	static String Name_CornerID = "CornerID";
	static String Name_CornerName = "CornerName";
	static String Name_StartItemNo = "StartItemNo";
	static String Name_EndItemNo = "EndItemNo";
	static String Name_StartBoxNo = "StartBoxNo";
	static String Name_EndBoxNo = "EndBoxNo";
	static String Name_StartTime = "StartTime";
	static String Name_Count = "Count";
	
	private String cornerID = "";
	private String cornerName = "";
	private String startItemNo = "";
	private String endItemNo = "";
	private String startBoxNo = "";
	private String endBoxNo = "";
	private String startTime = "";
	private String count = "0";

	
	
	
	public String getCornerID() {
		return cornerID;
	}

	public void setCornerID(String cornerID) {
		this.cornerID = cornerID;
	}

	public String getCornerName() {
		return cornerName;
	}

	public void setCornerName(String cornerName) {
		this.cornerName = cornerName;
	}

	public String getStartItemNo() {
		return startItemNo;
	}

	public void setStartItemNo(String startItemNo) {
		this.startItemNo = startItemNo;
	}

	public String getEndItemNo() {
		return endItemNo;
	}

	public void setEndItemNo(String endItemNo) {
		this.endItemNo = endItemNo;
	}

	public String getStartBoxNo() {
		return startBoxNo;
	}

	public void setStartBoxNo(String startBoxNo) {
		this.startBoxNo = startBoxNo;
	}

	public String getEndBoxNo() {
		return endBoxNo;
	}

	public void setEndBoxNo(String endBoxNo) {
		this.endBoxNo = endBoxNo;
	}
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	
	
	
	public CornerInfo(List<NameValuePair> parserResult) {

		for (NameValuePair nvp:parserResult){

			if(nvp.getName().equalsIgnoreCase(Name_CornerID)){
				this.setCornerID(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_CornerName)){
				this.setCornerName(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_StartItemNo)){
				this.setStartItemNo(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_EndItemNo)){
				this.setEndItemNo( nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_StartBoxNo)){
				this.setStartBoxNo(nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_EndBoxNo)){
				this.setEndBoxNo( nvp.getValue());
			}
			else if(nvp.getName().equalsIgnoreCase(Name_StartTime)){
				this.setStartTime(nvp.getValue()); 
			}
			else if(nvp.getName().equalsIgnoreCase(Name_Count)){
				this.setCount(nvp.getValue()); 
			}
		}
		
	}
	
    public static List<CornerInfo> getList(String data)throws XMLParserException,InvalidDataException, ServerErrorException{
    	List<CornerInfo> cornerInfoList = new ArrayList<CornerInfo>();
    	
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
		        	if (tagName.equalsIgnoreCase(Name_Corner)){
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
		        	else if(xpp.getName().equalsIgnoreCase(Name_Corner)){
		        		CornerInfo obj = new CornerInfo(parserResult);

		        		cornerInfoList.add(obj);
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
    	return cornerInfoList;
    }
    
}
