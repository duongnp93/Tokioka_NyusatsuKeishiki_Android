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

import tokiokaability.auction24.crypt.Crypt;

public class PreviewRequestInfo {
	private String PlaceName; // 会場名 - ADDED 2014/02/19
	private String HoldDate; // 開催日（MM/DD） - ADDED 2014/02/19
	private String LoginID;	// ログインID
	private String PlaceID;	// 会場ID
	private String TimesNo;	// 回次番号
	private String ItemNo;	// 出品番号
	private String CarName;	// 車名
	private String PersonName;	// 担当者名
	private String TEL;	// TEL
	private String TEL2;	// 携帯TEL
	private String FAX;	// FAX
	private int RequestStatus;	// 下見依頼申込状態 (０：未申込、１：申込中、２：申込完了、３：申込失敗)
	private int OutsideFlag;	// 外装フラグ
	private int InsideFlag;	// 内装フラグ
	private int SeatFlag;	// シート形状フラグ
	private int CapacityFlag;	// 乗車定員フラグ
	private int DriveFlag;	// 駆動フラグ
	private int AudioFlag;	// オーディオフラグ
	private int EngineFlag;	// エンジンフラグ
	private int WheelFlag;	// ホイールフラグ
	private int ElectricFlag;	// 電装フラグ
	private int TireFlag;	// タイヤ溝フラグ
	private int PowerSteeringFlag;	// パワステフラグ
	private int SizeFlag;	// ボディ寸法フラグ
	private String RequestMemo;	// その他
	
	
	static String RESULT_TAG = "result";
	static String STATUS_TAG = "Status";
	static String ITEM_TAG = "Item";
	
	static String PlaceName_TAG = "PlaceName"; // ADDED 2014/02/19
	static String HoldDate_TAG  = "HoldDate"; // ADDED 2014/02/19
	static String TimesNo_TAG = "TimesNo";
	static String ItemNo_TAG = "ItemNo";
	static String CarName_TAG = "CarName";	// 車名
	static String PersonName_TAG = "PersonName";	// 担当者名
	static String TEL_TAG = "TEL";	// TEL
	static String TEL2_TAG = "TEL2";	// 携帯TEL - ADDED 2014/02/19
	static String FAX_TAG = "FAX";	// FAX
	static String RequestStatus_TAG = "RequestStatus";	// 下見依頼申込状態 (０：未申込、１：申込中、２：申込完了、３：申込失敗)
	static String OutsideFlag_TAG = "OutsideFlag";	// 外装フラグ
	static String InsideFlag_TAG = "InsideFlag";	// 内装フラグ
	static String SeatFlag_TAG = "SeatFlag";	// シート形状フラグ
	static String CapacityFlag_TAG = "CapacityFlag";	// 乗車定員フラグ
	static String DriveFlag_TAG = "DriveFlag";	// 駆動フラグ
	static String AudioFlag_TAG = "AudioFlag";	// オーディオフラグ
	static String EngineFlag_TAG = "EngineFlag";	// エンジンフラグ
	static String WheelFlag_TAG = "WheelFlag";	// ホイールフラグ
	static String ElectricFlag_TAG = "ElectricFlag";	// 電装フラグ
	static String TireFlag_TAG = "TireFlag";	// タイヤ溝フラグ
	static String PowerSteeringFlag_TAG = "PowerSteeringFlag";	// パワステフラグ
	static String SizeFlag_TAG = "SizeFlag";	// ボディ寸法フラグ
	static String RequestMemo_TAG = "RequestMemo";	// その他
	
	private Crypt mcrypt = new Crypt();
	
	public PreviewRequestInfo(List<NameValuePair> parserResult) throws ServerErrorException, InvalidDataException {
		try {
			for (NameValuePair nvp:parserResult){
				if (nvp.getName().equalsIgnoreCase(PlaceName_TAG)) {
					this.setPlaceName(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(HoldDate_TAG)) {
					this.setHoldDate(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(TimesNo_TAG)) {
					this.setTimesNo(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(ItemNo_TAG)) {
					this.setItemNo(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(PersonName_TAG)) {
					
					
					String decrypted = null;
	            	try {
					 decrypted = new String( mcrypt.decrypt( nvp.getValue() ) );
					} catch (Exception e) {
						this.setPersonName(null);
					}
					
					this.setPersonName(decrypted);
				}
				else if (nvp.getName().equalsIgnoreCase(TEL_TAG)) {
					
					String decrypted = null;
	            	try {
					 decrypted = new String( mcrypt.decrypt( nvp.getValue() ) );
					} catch (Exception e) {
						this.setTEL(null);
					}
	            	
					this.setTEL(decrypted);
				}
				else if (nvp.getName().equalsIgnoreCase(TEL2_TAG)) {
					
					String decrypted = null;
	            	try {
					 decrypted = new String( mcrypt.decrypt( nvp.getValue() ) );
					} catch (Exception e) {
						this.setTEL2(null);
					}
	            	
					this.setTEL2(decrypted);
				}
				else if (nvp.getName().equalsIgnoreCase(FAX_TAG)) {
					
					String decrypted = null;
	            	try {
					 decrypted = new String( mcrypt.decrypt( nvp.getValue() ) );
					} catch (Exception e) {
						this.setFAX(null);
					}
					
					this.setFAX(decrypted);
				}
				else if (nvp.getName().equalsIgnoreCase(CarName_TAG)) {
					this.setCarName(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(PersonName_TAG)) {
					this.setPersonName(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(OutsideFlag_TAG)){
					if (Integer.parseInt(nvp.getValue()) == -1)
						throw new ServerErrorException("Server Error");
					else
						this.setOutsideFlag(Integer.parseInt(nvp.getValue()));
				}
				else if(nvp.getName().equalsIgnoreCase(InsideFlag_TAG)){
					if (Integer.parseInt(nvp.getValue()) == -1)
						throw new ServerErrorException("Server Error");
					else
						this.setInsideFlag(Integer.parseInt(nvp.getValue()));
				}
				else if(nvp.getName().equalsIgnoreCase(SeatFlag_TAG)){
					if (Integer.parseInt(nvp.getValue()) == -1)
						throw new ServerErrorException("Server Error");
					else
						this.setSeatFlag(Integer.parseInt(nvp.getValue()));
				}
				else if(nvp.getName().equalsIgnoreCase(CapacityFlag_TAG)){
					if (Integer.parseInt(nvp.getValue()) == -1)
						throw new ServerErrorException("Server Error");
					else
						this.setCapacityFlag(Integer.parseInt(nvp.getValue()));
				}
				else if(nvp.getName().equalsIgnoreCase(DriveFlag_TAG)){
					if (Integer.parseInt(nvp.getValue()) == -1)
						throw new ServerErrorException("Server Error");
					else
						this.setDriveFlag(Integer.parseInt(nvp.getValue()));
				}
				else if(nvp.getName().equalsIgnoreCase(AudioFlag_TAG)){
					if (Integer.parseInt(nvp.getValue()) == -1)
						throw new ServerErrorException("Server Error");
					else
						this.setAudioFlag(Integer.parseInt(nvp.getValue()));
				}
				else if(nvp.getName().equalsIgnoreCase(EngineFlag_TAG)){
					if (Integer.parseInt(nvp.getValue()) == -1)
						throw new ServerErrorException("Server Error");
					else
						this.setEngineFlag(Integer.parseInt(nvp.getValue()));
				}
				else if(nvp.getName().equalsIgnoreCase(WheelFlag_TAG)){
					if (Integer.parseInt(nvp.getValue()) == -1)
						throw new ServerErrorException("Server Error");
					else
						this.setWheelFlag(Integer.parseInt(nvp.getValue()));
				}
				else if(nvp.getName().equalsIgnoreCase(ElectricFlag_TAG)){
					if (Integer.parseInt(nvp.getValue()) == -1)
						throw new ServerErrorException("Server Error");
					else
						this.setElectricFlag(Integer.parseInt(nvp.getValue()));
				}
				else if(nvp.getName().equalsIgnoreCase(TireFlag_TAG)){
					if (Integer.parseInt(nvp.getValue()) == -1)
						throw new ServerErrorException("Server Error");
					else
						this.setTireFlag(Integer.parseInt(nvp.getValue()));
				}
				else if(nvp.getName().equalsIgnoreCase(PowerSteeringFlag_TAG)){
					if (Integer.parseInt(nvp.getValue()) == -1)
						throw new ServerErrorException("Server Error");
					else
						this.setPowerSteeringFlag(Integer.parseInt(nvp.getValue()));
				}
				else if(nvp.getName().equalsIgnoreCase(SizeFlag_TAG)){
					if (Integer.parseInt(nvp.getValue()) == -1)
						throw new ServerErrorException("Server Error");
					else
						this.setSizeFlag(Integer.parseInt(nvp.getValue()));
				}
				else if (nvp.getName().equalsIgnoreCase(RequestMemo_TAG)) {
					this.setRequestMemo(nvp.getValue());
				}
			}
		}
		catch(ServerErrorException ex){
			throw ex;
		}
		catch(Exception ex){
			ex.printStackTrace();
			throw new InvalidDataException(ex.getMessage());
		}
	}
	
	
	public PreviewRequestInfo() {
		super();
	}


	public static List<PreviewRequestInfo> getList(String data) throws XMLParserException,InvalidDataException, ServerErrorException{
    	List<PreviewRequestInfo> previewRequestList = new ArrayList<PreviewRequestInfo>();
    	
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
		        	if (tagName.equalsIgnoreCase(ITEM_TAG)){
		        		parserResult  = new ArrayList<NameValuePair>();
		        	}
		        	
		        }else if(eventType == XmlPullParser.TEXT) {
		            tagValue = xpp.getText();
		        }
		        else if (eventType == XmlPullParser.END_TAG){
		        	if(xpp.getName().equalsIgnoreCase(STATUS_TAG)){
						if (Integer.parseInt(tagValue) == -1)
							throw new ServerErrorException("Server Error");
					}
		        	else if(xpp.getName().equalsIgnoreCase(ITEM_TAG)){
		        		PreviewRequestInfo obj = new PreviewRequestInfo(parserResult);

		        		previewRequestList.add(obj);
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
    	return previewRequestList;
    }
	
	public String getPlaceName() {
		return PlaceName;
	}
	public void setPlaceName(String placeName) {
		PlaceName = placeName;
	}
	public String getHoldDate() {
		return HoldDate;
	}
	public void setHoldDate(String holdDate) {
		HoldDate = holdDate;
	}
	public String getLoginID() {
		return LoginID;
	}
	public void setLoginID(String loginID) {
		LoginID = loginID;
	}
	public String getPlaceID() {
		return PlaceID;
	}
	public void setPlaceID(String placeID) {
		PlaceID = placeID;
	}
	public String getTimesNo() {
		return TimesNo;
	}
	public void setTimesNo(String timesNo) {
		TimesNo = timesNo;
	}
	public String getItemNo() {
		return ItemNo;
	}
	public void setItemNo(String itemNo) {
		ItemNo = itemNo;
	}
	public String getCarName() {
		return CarName;
	}
	public void setCarName(String carName) {
		CarName = carName;
	}
	public String getPersonName() {
		return PersonName;
	}
	public void setPersonName(String personName) {
		PersonName = personName;
	}
	public String getTEL() {
		return TEL;
	}
	public void setTEL(String tEL) {
		TEL = tEL;
	}
	public String getTEL2() {
		return TEL2;
	}
	public void setTEL2(String tEL2) {
		TEL2 = tEL2;
	}
	public String getFAX() {
		return FAX;
	}
	public void setFAX(String fAX) {
		FAX = fAX;
	}
	public int getRequestStatus() {
		return RequestStatus;
	}
	public void setRequestStatus(int requestStatus) {
		RequestStatus = requestStatus;
	}
	public int getOutsideFlag() {
		return OutsideFlag;
	}
	public void setOutsideFlag(int outsideFlag) {
		OutsideFlag = outsideFlag;
	}
	public int getInsideFlag() {
		return InsideFlag;
	}
	public void setInsideFlag(int insideFlag) {
		InsideFlag = insideFlag;
	}
	public int getSeatFlag() {
		return SeatFlag;
	}
	public void setSeatFlag(int seatFlag) {
		SeatFlag = seatFlag;
	}
	public int getCapacityFlag() {
		return CapacityFlag;
	}
	public void setCapacityFlag(int capacityFlag) {
		CapacityFlag = capacityFlag;
	}
	public int getDriveFlag() {
		return DriveFlag;
	}
	public void setDriveFlag(int driveFlag) {
		DriveFlag = driveFlag;
	}
	public int getAudioFlag() {
		return AudioFlag;
	}
	public void setAudioFlag(int audioFlag) {
		AudioFlag = audioFlag;
	}
	public int getEngineFlag() {
		return EngineFlag;
	}
	public void setEngineFlag(int engineFlag) {
		EngineFlag = engineFlag;
	}
	public int getWheelFlag() {
		return WheelFlag;
	}
	public void setWheelFlag(int wheelFlag) {
		WheelFlag = wheelFlag;
	}
	public int getElectricFlag() {
		return ElectricFlag;
	}
	public void setElectricFlag(int electricFlag) {
		ElectricFlag = electricFlag;
	}
	public int getTireFlag() {
		return TireFlag;
	}
	public void setTireFlag(int tireFlag) {
		TireFlag = tireFlag;
	}
	public int getPowerSteeringFlag() {
		return PowerSteeringFlag;
	}
	public void setPowerSteeringFlag(int powerSteeringFlag) {
		PowerSteeringFlag = powerSteeringFlag;
	}
	public int getSizeFlag() {
		return SizeFlag;
	}
	public void setSizeFlag(int sizeFlag) {
		SizeFlag = sizeFlag;
	}
	public String getRequestMemo() {
		return RequestMemo;
	}
	public void setRequestMemo(String requestMemo) {
		RequestMemo = requestMemo;
	}
}
