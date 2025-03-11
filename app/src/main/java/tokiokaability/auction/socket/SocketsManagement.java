package tokiokaability.auction.socket;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import tokiokaability.auction.RealLuceAuctionApplication;
import tokiokaability.auction.socket.client.CheckingSocket;
import tokiokaability.auction.socket.client.CheckingSocketCallback;
import tokiokaability.auction.socket.client.CheckingSocketStatus;
import tokiokaability.auction.socket.client.SocketClient;
import tokiokaability.auction.socket.client.SocketConnectionCallback;
import tokiokaability.auction.socket.info.BaseInfo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class SocketsManagement {
	private List<SocketClient> socketClients;
	private SocketsManagementCallback socketsManagementCallback;
	private SocketsStatus socketsStatus;
	private CheckingSocket checkingSocket;
	private Context context;
	
	private Handler handler= new Handler(){
		
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
				// ソケットの受け取りに失敗したとき
				case SocketClient.MSG_SOCKETS_STATUS_DROPPED:
					if (checkingSocket != null)
						checkingSocket.connectionDroped();
					closeAll();

					socketsStatus = SocketsStatus.ConnectionDropped;
					if (socketsManagementCallback != null)
						socketsManagementCallback.onChangedStatus(socketsStatus);
					socketsStatus = SocketsStatus.Searching;
					if (socketsManagementCallback != null)
						socketsManagementCallback.onChangedStatus(socketsStatus);
					if (checkingSocket != null)
						checkingSocket.searchActivedGateway(false);
					break;

				// ソケットデータを受け取ったとき
				case SocketClient.MSG_SOCKETS_STATUS_RECEIVED:
					if (msg.getData().getInt("SocketClientIndex") < socketClients.size()) {
						SocketClient socketClient = socketClients.get(msg.getData().getInt("SocketClientIndex"));
						if (socketClient != null) {
							byte[] buffer = msg.getData().getByteArray("ReadData");
							ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
							byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
							BaseInfo objectInfo = socketClient.parseReceivedMessage(byteBuffer);
							if (socketClient.getReceivedCallback() != null)
								socketClient.getReceivedCallback().onReceivedData(socketClient.getParam(), objectInfo);
						}
					}
				default:
					break;
			}
			super.handleMessage(msg);
		}
	};
	
	
	/*
	public static final int MSG_SOCKETS_STATUS_CHANGED = 1;
	public static final int MSG_SOCKETS_STATUS_RECEIVED = 2;
	public static final int MSG_SOCKETS_STATUS_DROPPED = 3;
	*/
	
	public SocketsManagement(Context c){
		socketClients = new ArrayList<SocketClient>();
		this.context = c;

	}
	
	
	public void start(SocketsManagementCallback socketsManagementCallback, String[] gatewayIPs){
		Log.println(Log.INFO,"SocketsManagement","START");

		this.socketsManagementCallback = socketsManagementCallback;
		this.socketsStatus = SocketsStatus.Searching;
		this.socketsManagementCallback.onChangedStatus(this.socketsStatus);
		//Duongnp 20220923 comment cho nay
		this.checkingSocket = new CheckingSocket(gatewayIPs,
				RealLuceAuctionApplication.lane_id.equals("2")?RealLuceAuctionApplication.systemInfo.getChatPortNo():RealLuceAuctionApplication.systemInfo.getCheckPortNo()
				,context);
		this.checkingSocket.setCheckingSocketCallback(new ICheckingSocketCallback());
		checkingSocket.searchActivedGateway(true);

		//Duongnp 20220923 them cho nay
		//connectAll();
		
	}
	
	public void stop(){
		if (checkingSocket != null) {
			checkingSocket.stopChecking(false);
		}
		closeAll(false);

		this.socketsStatus = SocketsStatus.Disconnected;
		this.socketsManagementCallback.onChangedStatus(this.socketsStatus);
	}

	public void Dispose(){
		if (checkingSocket != null) {
			checkingSocket.stopChecking(false);
			checkingSocket = null;
		}
		socketClients.removeAll(socketClients);
		this.socketsManagementCallback = null;
	}


	public void startOne(SocketClient socketClient){
		Log.println(Log.INFO,"SocketsManagement","START ONE");
		
		SocketConnectionOne socketConnectionOne = new SocketConnectionOne();
		socketConnectionOne.setSocketClientIndex(socketClients.indexOf(socketClient));
		//Duongnp 20220923 comment cho nay
		if (this.checkingSocket.getStatus() == CheckingSocketStatus.Active)
			socketClient.connect(this.checkingSocket.getConnectingGatewayIndex(), socketConnectionOne);

		//Duongnp 20220923 them cho nay
//		socketClient.connect(0, socketConnectionOne);
	}
	
	public void stopOne(SocketClient socketClient){
		Log.println(Log.INFO,"SocketsManagement","STOP ONE");
		socketClient.close(false);
	}

	
	
	
	private void connectAll(){
		boolean hasEnabledSocket = false;
		
		Log.println(Log.INFO,"SocketsManagement","CONNECT ALL");
		
		for(int i=0;i<socketClients.size();i++){
			SocketClient socketClient = socketClients.get(i);
			//Duongnp 20220923 comment cho nay
			if (socketClient.isEnable()){
				hasEnabledSocket = true;
				socketClient.connect(this.checkingSocket.getConnectingGatewayIndex(), new SocketConnectionAll());
//				socketClient.connect(0, new SocketConnectionAll());
			}

			//Duongnp 20220923 them cho nay
//			hasEnabledSocket = true;
//			socketClient.connect(0, new SocketConnectionAll());
		}
			
		
		if (hasEnabledSocket == false){
			socketsStatus = SocketsStatus.CompletedConnectSuccessfully;
			socketsManagementCallback.onChangedStatus(socketsStatus);

		}
		
	}
	
	
	private void closeAll(){
		Log.println(Log.INFO,"SocketsManagement","CloseAll");

		if (checkingSocket != null) {
			checkingSocket.stopChecking(true);
		}
		for(int i=0;i<socketClients.size();i++){
			SocketClient socketClient = socketClients.get(i);
			socketClient.close(true);
		}
	}
	
	
	private void closeAll(boolean sync){
		Log.println(Log.INFO,"SocketsManagement","CloseAll SYN:"+ String.valueOf(sync));
		//Duongnp 20220923 comment cho nay
		checkingSocket.stopChecking(sync);
		for(int i=0;i<socketClients.size();i++){
			SocketClient socketClient = socketClients.get(i);
			socketClient.close(sync);
			socketClient.setReceivedCallback(null);
		}
	}
	
	
	public void addSocketClient(SocketClient socketClient, boolean start){
		socketClients.add(socketClient);
		if (start)
		{
			this.startOne(socketClient);
		}

	}
	
	public void removeSocketClient(SocketClient socketClient){
		socketClients.remove(socketClient);
		socketClient.close(false);
	}
	
	public void removeAll(){
		closeAll();
		socketClients.removeAll(socketClients);
	}
	
	
	public boolean isAllConnected(){
		for(int i=0;i<socketClients.size();i++){
			SocketClient socketClient = socketClients.get(i);
			if (socketClient.isEnable() && !socketClient.isConnected()){
				return false;
			}
		}
		return true;
	}
	
	
	final class SocketConnectionOne implements SocketConnectionCallback{
		int socketClientIndex;
		
		
		public int getSocketClientIndex() {
			return socketClientIndex;
		}

		public void setSocketClientIndex(int socketClientIndex) {
			this.socketClientIndex = socketClientIndex;
		}

		@Override
		public void onConnectSuccessfully() {
			SocketClient socketClient = socketClients.get(socketClientIndex);
			if (socketClient.isEnable() && socketsStatus == SocketsStatus.Reading && !socketClient.isReading()){
				socketClient.read(handler, socketClientIndex);
			}
			else if (socketClient.isEnable() &&  socketsStatus == SocketsStatus.Connecting ){
				if (isAllConnected()){
					// Starting Reading


					socketsStatus = SocketsStatus.CompletedConnectSuccessfully;
					socketsManagementCallback.onChangedStatus(socketsStatus);
					
					
					socketsStatus = SocketsStatus.Reading;
					socketsManagementCallback.onChangedStatus(socketsStatus);
					
					for(int i=0;i<socketClients.size();i++){
						SocketClient client = socketClients.get(i);
						if (client.isEnable()){
							client.read(handler, i);
						}
					}
					
				}
			}
		}

		@Override
		public void onConnectionFailed() {
			socketsStatus = SocketsStatus.ConnectionDropped;
			socketsManagementCallback.onChangedStatus(socketsStatus);
			checkingSocket.connectionDroped();
			closeAll();
			socketsStatus = SocketsStatus.Searching;
			socketsManagementCallback.onChangedStatus(socketsStatus);
			checkingSocket.searchActivedGateway(false);
		}

		@Override
		public void onClosed() {
			
		}
		
	}
	
	
	final class SocketConnectionAll implements SocketConnectionCallback{

		@Override
		public void onConnectSuccessfully() {
			//successfullConnectionCount++;
			if (isAllConnected()){
				
				socketsStatus = SocketsStatus.CompletedConnectSuccessfully;
				socketsManagementCallback.onChangedStatus(socketsStatus);
				
				socketsStatus = SocketsStatus.Reading;
				socketsManagementCallback.onChangedStatus(socketsStatus);
				
				for(int i=0;i<socketClients.size();i++){
					SocketClient socketClient = socketClients.get(i);
					if (socketClient.isEnable()){
						socketClient.read(handler, i);
					}
				}
				
			}
			
			
			
		}

		@Override
		public void onConnectionFailed() {
			socketsStatus = SocketsStatus.ConnectionDropped;
			socketsManagementCallback.onChangedStatus(socketsStatus);
			checkingSocket.connectionDroped();
			closeAll();
			socketsStatus = SocketsStatus.Searching;
			socketsManagementCallback.onChangedStatus(socketsStatus);
			checkingSocket.searchActivedGateway(false);
		}

		@Override
		public void onClosed() {
			
			
		}
		
	}
	
	final class ICheckingSocketCallback implements CheckingSocketCallback{

		@Override
		public void onFound() {
			socketsStatus = SocketsStatus.Connecting;
			socketsManagementCallback.onChangedStatus(socketsStatus);
			connectAll();
		}

		@Override
		public void onNotFound() {
			closeAll();
			
			socketsStatus = SocketsStatus.CompletedConnectFailed;
			socketsManagementCallback.onChangedStatus(socketsStatus);
			
		}

		@Override
		public void onDrop() {
			if (socketsStatus == SocketsStatus.Reading){
				socketsStatus = SocketsStatus.ConnectionDropped;
				socketsManagementCallback.onChangedStatus(socketsStatus);
			}
			closeAll();
			socketsStatus = SocketsStatus.Searching;
			socketsManagementCallback.onChangedStatus(socketsStatus);
			checkingSocket.searchActivedGateway(false);
			
		}
		
	}
}
