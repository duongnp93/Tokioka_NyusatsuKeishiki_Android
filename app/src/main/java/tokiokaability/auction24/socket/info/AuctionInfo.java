package tokiokaability.auction24.socket.info;

public class AuctionInfo extends BaseInfo{
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public short getPlaceNo() {
		return PlaceNo;
	}
	public void setPlaceNo(short placeNo) {
		PlaceNo = placeNo;
	}
	public short getLaneNo() {
		return LaneNo;
	}
	public void setLaneNo(short laneNo) {
		LaneNo = laneNo;
	}
	public int getItemNo() {
		return ItemNo;
	}
	public void setItemNo(int itemNo) {
		ItemNo = itemNo;
	}
	public long getPrice() {
		return Price;
	}
	public void setPrice(long price) {
		Price = price;
	}
	public char getFlagCorrection() {
		return flagCorrection;
	}
	public void setFlagCorrection(char flagCorrection) {
		this.flagCorrection = flagCorrection;
	}
	public char getFlagStart() {
		return flagStart;
	}
	public void setFlagStart(char flagStart) {
		this.flagStart = flagStart;
	}
	public char getFlagSellout() {
		return flagSellout;
	}
	public void setFlagSellout(char flagSellout) {
		this.flagSellout = flagSellout;
	}
	public char getFlagLamp() {
		return flagLamp;
	}
	public void setFlagLamp(char flagLamp) {
		this.flagLamp = flagLamp;
	}
	public int getLastMemberNo() {
		return LastMemberNo;
	}
	public void setLastMemberNo(int lastMemberNo) {
		LastMemberNo = lastMemberNo;
	}
	public char getSeri() {
		return Seri;
	}
	public void setSeri(char seri) {
		Seri = seri;
	}
	public int getNextItemNo() {
		return NextItemNo;
	}
	public void setNextItemNo(int nextItemNo) {
		NextItemNo = nextItemNo;
	}
	public char getState() {
		return State;
	}
	public void setState(char state) {
		State = state;
	}
	public long getStartPrice() {
		return StartPrice;
	}
	public void setStartPrice(long startPrice) {
		StartPrice = startPrice;
	}
	public char getInSeri() {
		return inSeri;
	}
	public void setInSeri(char inSeri) {
		this.inSeri = inSeri;
	}
	public void setAskingPrice(long askingPrice){ AskingPrice = askingPrice;}
	public long getAskingPrice(){return AskingPrice;}
	public String getLastMemberID() {
		return LastMemberID;
	}

	public void setLastMemberID(String lastMemberID) {
		LastMemberID = lastMemberID;
	}

	public int getBidCountdown() {
		return bidCountdown;
	}

	public void setBidCountdown(int bidCountdown) {
		this.bidCountdown = bidCountdown;
	}

	String code;
	short PlaceNo;       
	short LaneNo;        
	int ItemNo;     
	long Price;
	char flagCorrection;       
	char flagStart;         
	char flagSellout;       
	char flagLamp;         
	int LastMemberNo;  
	char Seri;            
	int NextItemNo;    
	char State;               
	long StartPrice;
	char inSeri;
	long AskingPrice;
	String LastMemberID;
	int bidCountdown;
}
