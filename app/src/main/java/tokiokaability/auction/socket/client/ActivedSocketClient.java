package tokiokaability.auction.socket.client;

import java.nio.ByteBuffer;

import tokiokaability.auction.socket.info.AuctionInfo;
import tokiokaability.auction.socket.info.BaseInfo;

public class ActivedSocketClient extends SocketClient {

	public ActivedSocketClient(int port, String[] address, Object param,
			SocketReceivedCallback receivedCallback) {
		super(port, address, param, receivedCallback);

		this.setReadingTimeout(10000);
		
		this.setReceivedMessageSize(70);
	}

	@Override
	public BaseInfo parseReceivedMessage(ByteBuffer buffer) {
		AuctionInfo auctionInfo = new AuctionInfo();
		
	    auctionInfo.setCode(String.format("%c%c",(char)buffer.get(0),(char)buffer.get(1) ));	// 01. 電文識別子（’01’固定）Char 2 byte 
	    auctionInfo.setPlaceNo(buffer.getShort(2));          					// 02. 会場番号 Short 2 byte
	    auctionInfo.setLaneNo(buffer.getShort(4));								// 03. レーン番号	Short 2 byte
	    auctionInfo.setItemNo(buffer.getInt(6));					           	// 04. 出品番号	UInt32 4 byte
	    auctionInfo.setPrice(buffer.getLong(10));         						// 05. 現在価格	UInt64 8 byte
	    auctionInfo.setFlagCorrection((char)buffer.get(18));    					// 06. 訂正フラグ（０：訂正なし、１：訂正あり）Char 1 byte
	    auctionInfo.setFlagStart((char)buffer.get(19));         					// 07. スタートフラグ ０：未、１：スタート）	Char 1 byte
	    auctionInfo.setFlagSellout((char)buffer.get(20));       // 08. うりきりフラグ （０：うりきり前、１：うりきり後）	Char 1 byte
	    auctionInfo.setFlagLamp((char)buffer.get(21));          // 09. ランプ区分　（０：全消灯　 １：赤点灯　 ２：黄点灯　 ３：緑点灯）	Char 1 byte
	    auctionInfo.setLastMemberNo(buffer.getInt(22));   // 10. 最終応札会員番号	UInt32 4 byte
	    auctionInfo.setSeri((char)buffer.get(26));          // 11. セリ結果区分（０：落札前　 １：保留　 ２：引き　 ５：会場落札　 ３，４：リアル落札）	Char 2 byte
	    auctionInfo.setNextItemNo(buffer.getInt(27));    // 12. 次の出品番号	UInt32 4 byte
	    auctionInfo.setState((char)buffer.get(31));         // 13. オークション状態（０：オークション開催前、開催中　１：オークション終了（現在のレーン）　２：オークション終了（全レーン））Char 1 byte
	    auctionInfo.setStartPrice(buffer.getInt(32)); // 14. スタート価格 UInt64 8 byte
	    auctionInfo.setInSeri((char)buffer.get(40)); // 14. セリ中効果音 char 1 byte
		auctionInfo.setAskingPrice(buffer.getInt(41));
		byte[] receiveArr = new byte[buffer.remaining()];
		buffer.get(receiveArr);
		byte[] byteArr = java.util.Arrays.copyOfRange(receiveArr,49,69);
		String lastMemberID = new String(byteArr);
		auctionInfo.setLastMemberID(lastMemberID);
		auctionInfo.setBidCountdown((char)buffer.get(69));
		return auctionInfo;
	}

}
