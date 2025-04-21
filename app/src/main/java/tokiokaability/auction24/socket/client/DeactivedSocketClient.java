package tokiokaability.auction24.socket.client;

import java.nio.ByteBuffer;

import tokiokaability.auction24.socket.info.BaseInfo;
import tokiokaability.auction24.socket.info.SimpleAuctionInfo;

public class DeactivedSocketClient extends SocketClient{

	public DeactivedSocketClient(int port, String[] address, Object param,
			SocketReceivedCallback receivedCallback) {
		super(port, address, param, receivedCallback);

		this.setReadingTimeout(1000*60*3);
		this.setReceivedMessageSize(14);
	}

	@Override
	public BaseInfo parseReceivedMessage(ByteBuffer buffer) {
		SimpleAuctionInfo auctionInfo = new SimpleAuctionInfo();
		
		auctionInfo.setCode("02");           // 01. 電文識別子（’01’固定）Char 2 byte 
	    auctionInfo.setPlaceNo(buffer.getShort(2));          // 02. 会場番号 Short 2 byte
	    auctionInfo.setLaneNo(buffer.getShort(4));           // 03. レーン番号	Short 2 byte
	    auctionInfo.setItemNo(buffer.getInt(6));           // 04. 出品番号	UInt32 4 byte
	    auctionInfo.setPrice(buffer.getInt(10));         // 05. 現在価格	UInt32 4 byte
	    

		return auctionInfo;
	}

}
