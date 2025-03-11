package tokiokaability.auction.socket.info;

public class SimpleAuctionInfo extends BaseInfo{
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
	public int getPrice() {
		return Price;
	}
	public void setPrice(int price) {
		Price = price;
	}
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
	String code;   
	short PlaceNo;       
	short LaneNo;        
	int ItemNo;     
	int Price;     
	int tag;
}
