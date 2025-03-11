package tokiokaability.auction.socket.info;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PlaceBitInfo extends BaseInfo{
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
	public int getMemberNo() {
		return MemberNo;
	}
	public void setMemberNo(int memberNo) {
		MemberNo = memberNo;
	}

	public String getMemberID() {
		return MemberID;
	}

	public void setMemberID(String memberID) {
		MemberID = memberID;
	}

	String code; // 2 byte = 11
	short PlaceNo; // 2 byte
	short LaneNo; // 2 byte
	int ItemNo; // 4 byte
	long Price; // 4 byte
	int MemberNo; // 4 byte
	String MemberID;
	
	public byte[] getRawData(){
		ByteBuffer data = ByteBuffer.allocate(42);
		data.order(ByteOrder.LITTLE_ENDIAN);
		
		data.put(0,	(byte)'1');
		data.put(1, (byte)'1');
		data.putShort(2, this.getPlaceNo());
		data.putShort(4, this.getLaneNo());
		data.putInt(6, this.getItemNo());
		data.putLong(10, this.getPrice());
		data.putInt(18, this.getMemberNo());
		data.position(22);
		data.put(getMemberID().getBytes());
		return data.array();
	}
}
