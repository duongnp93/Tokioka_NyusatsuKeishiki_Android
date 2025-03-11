package tokiokaability.auction.socket.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import tokiokaability.auction.RealLuceAuctionApplication;
import tokiokaability.auction.commons.Utilities;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class CheckingSocket {
	private int connectingGatewayIndex = -1;
	private String connectingGatewayAddress = "";
	private CheckingSocketStatus status = CheckingSocketStatus.Inactive;
	private String[] gatewayAddressList = new String[0];
	private int[] gatewayErrorList = new int[0];
	private CheckingSocketCallback checkingSocketCallback;
	private Thread checkingThread;
	private Socket socket;
	private InputStream inputStream; 
	private int checkingPortNo = 0;
	private Context context;
	
	//private int writeTimeout = 100;
	
	
	public CheckingSocketStatus getStatus() {
		return status;
	}

	public String[] getGatewayAddressList() {
		return gatewayAddressList;
	}
	public void setGatewayAddressList(String[] gatewayAddressList) {
		this.gatewayAddressList = gatewayAddressList;
	}
	public int getConnectingGatewayIndex() {
		return connectingGatewayIndex;
	}
	public String getConnectingGatewayAddress() {
		return connectingGatewayAddress;
	}
	public CheckingSocketCallback getCheckingSocketCallback() {
		return checkingSocketCallback;
	}
	public void setCheckingSocketCallback(
			CheckingSocketCallback checkingSocketCallback) {
		this.checkingSocketCallback = checkingSocketCallback;
	}
	

	public static final int MSG_CHECKING_SOCKET_FOUND= 3;
	public static final int MSG_CHECKING_SOCKET_NOTFOUND = 4;
	public static final int MSG_CHECKING_SOCKET_DROP= 5;
	
	
	
	public static final int MAX_ERROR_PER_GATEWAY = 2;
	
	private Handler handler= new Handler(){
		
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			
				case MSG_CHECKING_SOCKET_NOTFOUND:
					Log.println(Log.INFO,"Checking Port","GATEWAY NOT FOUND");
					Utilities.writeLog(context,RealLuceAuctionApplication.getConfiguration("error_log_file"),"[SocketTimeout] Gateway not found");
					status = CheckingSocketStatus.Inactive;
					if (checkingSocketCallback != null)
						checkingSocketCallback.onNotFound();
					break;
			
				case MSG_CHECKING_SOCKET_FOUND:
					Log.println(Log.INFO,"Checking Port","GATEWAY FOUND");
					status = CheckingSocketStatus.Active;
					if (checkingSocketCallback != null)
						checkingSocketCallback.onFound();
					//startChecking();
					break;
					
				case MSG_CHECKING_SOCKET_DROP:
					Log.println(Log.INFO,"Checking Port","GATEWAY DROP");
					Utilities.writeLog(context,RealLuceAuctionApplication.getConfiguration("error_log_file"),"[SocketTimeout] Gateway drop");
					stopChecking(true);
					status = CheckingSocketStatus.Inactive;
					if (checkingSocketCallback != null)
						checkingSocketCallback.onDrop();
					break;
				
			}
			super.handleMessage(msg);
		}
	};
	
	
	public CheckingSocket(String[] gatewayAddressList, int port,Context c){
		this.gatewayAddressList = gatewayAddressList;
		this.checkingPortNo = port;
		this.context = c;
	}
	
	public void searchActivedGateway(boolean resetErrorTable){
		
		Log.println(Log.INFO,"searchActivedGateway","Start Searching Gateway");
		
		if (resetErrorTable){
			this.gatewayErrorList = new int[gatewayAddressList.length];
			connectingGatewayIndex = -1;
			
			for(int i=0;i<gatewayErrorList.length;i++){
				gatewayErrorList[i] = 0;
			}
		}

		Thread searchingThread = new Thread(new SearchingThread());
		searchingThread.setPriority(8);
		searchingThread.start();

	}
	
	public void connectionDroped(){
		Log.println(Log.INFO,"searchActivedGateway","connectionDroped =======");
		gatewayErrorList[connectingGatewayIndex]++;
	}
	
	public void startChecking(){
		Log.println(Log.INFO,"searchActivedGateway","Start Checking Gateway");
		
		checkingThread = new Thread(new CheckingThread());
		checkingThread.setPriority(8);
		checkingThread.start();
	}
	
	
	public void stopChecking(boolean isSyn){
		
		Log.println(Log.INFO,"searchActivedGateway","STOP Checking Gateway");
		
		try {

			if (checkingThread != null){
				if (checkingThread.isAlive()){
					checkingThread.interrupt();
					if (inputStream != null){
						inputStream.close();
						inputStream = null;
					}
					while(isSyn == true && checkingThread.isAlive()){
						
					}
				}
				//checkingThread.interrupt();
				checkingThread = null;
			}
			
			if (inputStream != null){
				inputStream.close();
				inputStream = null;
			}
			
			if (this.socket != null ){
				this.socket.close();
				this.socket = null;
			}
			
		} catch (IOException e) {
			this.socket = null;
		}
	}
	
	

	
	
	final class SearchingThread implements Runnable{
		@Override
		public void run() {
			boolean foundGateway;


			Message msg = new Message();

			// tim 1 gateway co the ket noi duoc.
			foundGateway = false;
			while (!Thread.currentThread().isInterrupted() && gatewayAddressList.length > 0) {
				foundGateway = false;
				
				// tim gateway chua co nhieu hon 2 loi
				for(int i= 1;i<=gatewayAddressList.length;i++){
					if (gatewayErrorList[(connectingGatewayIndex + i)%gatewayAddressList.length] < MAX_ERROR_PER_GATEWAY ){
						foundGateway = true;
						connectingGatewayIndex  = (connectingGatewayIndex + i) % gatewayAddressList.length;
						break;
					}
				}
				
				if (!foundGateway){
					msg = new Message();
					msg.what = MSG_CHECKING_SOCKET_NOTFOUND;
					handler.sendMessage(msg);
					break;
				}
				
				// thu ket noi
				try{
					InetSocketAddress socketAddress =  new InetSocketAddress(gatewayAddressList[connectingGatewayIndex], checkingPortNo);
					
					
					Socket sk;
					socket = null;
					sk =new Socket();
					sk.connect(socketAddress,RealLuceAuctionApplication.getIntConfiguration("socket_timeout"));
					sk.setTcpNoDelay(true);
					sk.setSoTimeout(RealLuceAuctionApplication.systemInfo.getInspectTime());
					
					if (!Thread.currentThread().isInterrupted()){
						inputStream = sk.getInputStream();
						socket = sk;
						msg = new Message();
						msg.what = MSG_CHECKING_SOCKET_FOUND;
						handler.sendMessage(msg);
						break;
					}
					else{
						sk.shutdownInput();
						sk.shutdownOutput();
						sk.close();
					}
							
				}
				catch(Exception ex){
					gatewayErrorList[connectingGatewayIndex]++;
				}

			}

		}
	}
	
	
	
	
	
	
	final class CheckingThread implements Runnable{
		@Override
		public void run() {
			Message msg = new Message();
			byte[] readBuffer =  new byte[2];

			try{
				while (!Thread.currentThread().isInterrupted() && inputStream != null){
					inputStream.read(readBuffer);
				}
			}
			catch(SocketException ex){
				Log.e("CHUNGSAVI", "run: ",ex );

//				try{
//					inputStream.close();
//				}
//				catch (Exception e){
//					e.printStackTrace();
//				}
			}
			catch(Exception ex){
				if (!Thread.currentThread().isInterrupted()){
					msg = new Message();
					msg.what = MSG_CHECKING_SOCKET_DROP;
					handler.sendMessage(msg);
				}
				
			}
			
		}
	}
	
	
	
	
}
