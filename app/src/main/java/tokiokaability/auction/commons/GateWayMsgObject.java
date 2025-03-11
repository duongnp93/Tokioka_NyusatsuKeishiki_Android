package tokiokaability.auction.commons;


//GW Message Object
public class GateWayMsgObject
{
	String MsgCode;
	short PlaceNo;
	short LaneNo;
	long  ProductNo;   // use long to contain Uint32, because there is no unsigned type in java or android
	long CurrentPrice;
	char TeiseiFlag;  // remember char in java has 2 bytes
	char StartFlag;
	char UrikiriFlag;
	char LampFlag;
	long LastMemberNo;
	char SeriResult;
	long NextProductNo;
	char State;
	long StartPrice;



	String LastMemberID;
	char AudioInSeriFlag;
	
	
	public String getMsgCode(){
		return MsgCode;
	}
	
	public void setMsgCode(String msgCode){
		MsgCode = msgCode;
	}
	
	public short getPlaceNo(){
		return PlaceNo;
	}
	
	public void setPlaceNo(short placeNo){
		PlaceNo = placeNo;
	}
	
	public short getLaneNo(){
		return LaneNo;
	}
	
	public void setLaneNo(short laneNo){
		LaneNo = laneNo;
	}
	
	public long getProductNo(){
		return ProductNo;
	}
	
	public void setProductNo(long productNo){
		ProductNo = productNo;
	}
	
	public long getCurrentPrice(){
		return CurrentPrice;
	}
	
	public void setCurrentPrice(long currentPrice){
		CurrentPrice = currentPrice;
	}
	
	public char getTeiseiFlag(){
		return TeiseiFlag;
	}
	
	public void setTeiseiFlag(char teiseiFlag){
		TeiseiFlag = teiseiFlag;
		
	}
	
	public char getStartFlag(){
		return StartFlag;
	}
	
	public void setStartFlag(char startFlag){
		StartFlag = startFlag;
	}
	
	public char getUrikiriFlag(){
		return UrikiriFlag;
	}
	
	public void setUrikiriFlag(char urikiriFlag){
		UrikiriFlag = urikiriFlag;
	}
	
	public char getLampFlag(){
		return LampFlag;
	}
	
	public void setLampFlag(char lampFlag){
		LampFlag = lampFlag;
	}
	
	public long getLastMemberNo(){
		return LastMemberNo;
	}
	
	public void setLastMemberNo(long lastMemberNo){
		LastMemberNo = lastMemberNo;
	}
	
	public char getSeriResult(){
		return SeriResult;
	}
	
	public void setSeriResult(char seriResult){
		SeriResult = seriResult;
	}
	
	public long getNextProductNo(){
		return NextProductNo;
	}
	
	public void setNextProductNo(long nextProductNo){
		NextProductNo = nextProductNo;
	}
	
	
	public char getState(){
		return State;
	}
	
	public void setState(char state){
		State = state;
	}
	
	public long getStartPrice(){
		return StartPrice;
	}
	
	public void setStartPrice(long startPrice){
		StartPrice = startPrice;
	}

	public char getAudioInSeriFlag(){
		return AudioInSeriFlag;
	}
	
	public void setAudioInSeriFlag(char audioInSeriFlag){
		AudioInSeriFlag = audioInSeriFlag;
	}

	public String getLastMemberID() {
		return LastMemberID;
	}

	public void setLastMemberID(String lastMemberID) {
		LastMemberID = lastMemberID;
	}
		
}