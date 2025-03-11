package tokiokaability.auction.HTTPInfo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

public class SystemInfo extends BaseInfo{
	
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getTestPlaceId() {
		return testPlaceId;
	}
	public void setTestPlaceId(String testPlaceId) {
		this.testPlaceId = testPlaceId;
	}
	public String getGwStartTime() {
		return gwStartTime;
	}
	public void setGwStartTime(String gwStartTime) {
		if (gwStartTime != null)
			this.gwStartTime = gwStartTime.replace(":", "");
		else
			this.gwStartTime = null;
	}
	public String getGwEndTime() {
		return gwEndTime;
	}
	public void setGwEndTime(String gwEndTime) {
		if (gwEndTime != null)
			this.gwEndTime = gwEndTime.replace(":", "");
		else
			this.gwEndTime = null;
	}

	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getVoiceFlag() {
		return voiceFlag;
	}
	public void setVoiceFlag(String voiceFlag_Value) {
		this.voiceFlag = voiceFlag_Value;
	}
	public String getVoiceServerIP() {
		return voiceServerIP;
	}
	public void setVoiceServerIP(String voiceServerIP_Value) {
		this.voiceServerIP = voiceServerIP_Value;
	}
	public int getCheckPortNo() {
		return checkPortNo;
	}
	public void setCheckPortNo(int checkPortNo_Value) {
		this.checkPortNo = checkPortNo_Value;
	}
	public int getInspectTime() {
		return inspectTime;
	}
	public void setInspectTime(int inspectTime) {
		this.inspectTime = inspectTime;
	}


	private String mode = "0"; // 0: Normal; 1: No bid; 2: Log mode

	private String testPlaceId = "";
	private String gwStartTime = "";
	private String gwEndTime = "";
	private String appVersion = "0";
    private String voiceFlag = "0";
    private String voiceServerIP = "";
	private String soubaUrl = "";
    private int checkPortNo = 0;
    private int chatPortNo = 0;
    private int inspectTime = 1000;


	public int getChatPortNo() {
		return chatPortNo;
	}

	public void setChatPortNo(int chatPortNo) {
		this.chatPortNo = chatPortNo;
	}

	public String getSoubaUrl() {
		return soubaUrl;
	}

	public void setSoubaUrl(String soubaUrl) {
		this.soubaUrl = soubaUrl;
	}

	public List<String> getTypeList() {
		return TypeList;
	}

	public List<String> getKaisaibiList() {
		return KaisaibiList == null? new ArrayList<String>():KaisaibiList;
	}

	public List<String> getSessionList() {
		return SessionList;
	}

	private List<String> TypeList;
    private List<String> KaisaibiList;
    private List<String> SessionList;
    
	public static final String TAG_STATUS = "Status";

	public static final String TAG_MODE = "Mode";
	public static final String TAG_TESTPLACEID = "TestPlaceId";
	public static final String TAG_GWSTARTTIME = "GwStartTime";
	public static final String TAG_GWENDTIME = "GwEndTime";
	public static final String TAG_APPVERSION = "AndroidAppVersion";
	static String Name_VoiceFlag = "VoiceFlag";
	static String Name_VoiceServerIP = "VoiceServerIP";
	static String Name_CheckPortNo = "CheckPortNo";
	public static final String Name_InspectTime = "InspectTime";
	public static final String TAG_HOLDINGDATE = "Date";
	public static  final String TAG_SESSIONNO = "SessionNo";
	public static  final String TAG_SOUBAURL = "SoubaUrl";
	public static  final String TAG_TYPENAME = "Typename";
	public static  final String TAG_CHATPORT = "ChatPort";
	
	public SystemInfo(String data) throws XMLParserException,InvalidDataException, ServerErrorException{
		List<NameValuePair> parserResult;
		KaisaibiList = new ArrayList<String>();
		TypeList = new ArrayList<String>();
		SessionList = new ArrayList<String>();
		try{
			parserResult = BaseInfo.parse(data);
			
			for (NameValuePair nvp:parserResult){
				if(nvp.getName().equalsIgnoreCase(TAG_STATUS)){
					if (Integer.parseInt(nvp.getValue()) == -1)
						throw new ServerErrorException("Server Error");
					else
						this.setStatus(Integer.parseInt(nvp.getValue()));
				}

				else if(nvp.getName().equalsIgnoreCase(TAG_MODE)){
					this.setMode(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(TAG_TESTPLACEID)){
					this.setTestPlaceId(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(TAG_GWSTARTTIME)){
					this.setGwStartTime(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(TAG_GWENDTIME)){
					this.setGwEndTime(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(TAG_APPVERSION)){
					this.setAppVersion(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(Name_VoiceFlag)){
					this.setVoiceFlag(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(Name_VoiceServerIP)){
					this.setVoiceServerIP(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(Name_CheckPortNo)){
					this.setCheckPortNo(Integer.parseInt( nvp.getValue()));
				}				
				else if(nvp.getName().equalsIgnoreCase(Name_InspectTime)){
					this.setInspectTime(Integer.parseInt(nvp.getValue()));
				}
				else if(nvp.getName().equalsIgnoreCase(TAG_TYPENAME)){
					this.TypeList.add(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(TAG_HOLDINGDATE)){
					this.KaisaibiList.add(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(TAG_SESSIONNO)){
					this.SessionList.add(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(TAG_SOUBAURL)){
					this.setSoubaUrl(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(TAG_CHATPORT)){
					this.setChatPortNo(Integer.parseInt(nvp.getValue()));
				}
			}
		}
		catch(XMLParserException ex){
			ex.printStackTrace();
			throw ex;
		}
		catch(ServerErrorException ex){
			throw ex;
		}
		catch(Exception ex){
			ex.printStackTrace();
			throw new InvalidDataException(ex.getMessage());
		}
	}
}
