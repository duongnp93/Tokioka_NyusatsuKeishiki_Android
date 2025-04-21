package tokiokaability.auction24.socket.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import tokiokaability.auction24.socket.info.BaseInfo;




public abstract class SocketClient {
	
	private Socket socket;
	private int port;
	private String[] address;
	private SocketReceivedCallback receivedCallback;
	private SocketConnectionCallback socketConnectionCallback;
	private Object param;
	private int ReceivedMessageSize;
	private OutputStream outputStream;
	private InputStream inputStream;
	private int addressIndex;
	private boolean isEnable;
	private SocketStatus socketStatus;
	
	
	private Handler handlerReceiving;
	private int tag;
	private Thread readingThread;
	private Thread connectingThread;
	private int readingTimeout = 0;
	
	
	public int isReadingTimeout() {
		return readingTimeout;
	}
	public void setReadingTimeout(int readingTimeout) {
		this.readingTimeout = readingTimeout;
	}
	

	public static final int MSG_SOCKETS_STATUS_RECEIVED = 2;
	public static final int MSG_SOCKETS_STATUS_DROPPED = 3;

	public int getReceivedMessageSize() {
		return ReceivedMessageSize;
	}
	public void setReceivedMessageSize(int receivedMessageSize) {
		ReceivedMessageSize = receivedMessageSize;
	}
	
	
	
	
	
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
	public Handler getHandlerReceiving() {
		return handlerReceiving;
	}
	public void setHandlerReceiving(Handler handlerReceiving) {
		this.handlerReceiving = handlerReceiving;
	}
	public boolean isEnable() {
		return isEnable;
	}
	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}
	public int getAddressIndex() {
		return addressIndex;
	}
	public void setAddressIndex(int addressIndex) {
		this.addressIndex = addressIndex;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String[] getAddress() {
		return address;
	}
	public void setAddress(String[] address) {
		this.address = address;
	}
	public SocketReceivedCallback getReceivedCallback() {
		return receivedCallback;
	}
	public void setReceivedCallback(SocketReceivedCallback receivedCallback) {
		this.receivedCallback = receivedCallback;
	}
	public Object getParam() {
		return param;
	}
	public void setParam(Object param) {
		this.param = param;
	}
	public OutputStream getOutputStream() {
		return outputStream;
	}
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	public InputStream getInputStream() {
		return inputStream;
	}

	
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public SocketClient(int port, String[] address, Object param, SocketReceivedCallback receivedCallback){
		this.setPort(port);
		this.setAddress(address);
		this.setParam(param);
		this.setReceivedCallback(receivedCallback);
		socketStatus = SocketStatus.init;
	}
	
	public void connect(int gatewayIndex,SocketConnectionCallback callback) {
		this.socketConnectionCallback = callback;
		this.addressIndex = gatewayIndex;


		handlerConnecting = new Handler(){

			@Override
			public void handleMessage(Message msg){
				if (socketStatus == SocketStatus.connecting){
					switch(msg.what){
						case MSG_CONNECT_SUCCESSFULLY:
							Log.println(Log.INFO, "SocketClient", "Connected ");
							socketStatus = SocketStatus.connected;
							socketConnectionCallback.onConnectSuccessfully();
							break;
						case MSG_CONNECT_FAILED:

							Log.println(Log.INFO, "SocketClient", "Failed ====");
							socketStatus = SocketStatus.closed;
							socketConnectionCallback.onConnectionFailed();
							break;
						default:
							break;
					}
					super.handleMessage(msg);
				}
			}
		};

		//Log.println(Log.INFO, "SocketClient", "Connecting to "+getAddress()[getAddressIndex()] + " Port:" + String.valueOf(getPort()));
		socketStatus = SocketStatus.connecting;
		connectingThread = new AnsynConnect();
		
		
		connectingThread.start();
	}
	
	public void reconnect(){
		if (this.socketConnectionCallback != null && this.addressIndex > -1){


			handlerConnecting = new Handler(){

				@Override
				public void handleMessage(Message msg){
					if (socketStatus == SocketStatus.connecting){
						switch(msg.what){
							case MSG_CONNECT_SUCCESSFULLY:
								Log.println(Log.INFO, "SocketClient", "Connected ");
								socketStatus = SocketStatus.connected;
								socketConnectionCallback.onConnectSuccessfully();
								break;
							case MSG_CONNECT_FAILED:

								Log.println(Log.INFO, "SocketClient", "Failed ====");
								socketStatus = SocketStatus.closed;
								socketConnectionCallback.onConnectionFailed();
								break;
							default:
								break;
						}
						super.handleMessage(msg);
					}
				}
			};

			//Log.println(Log.INFO, "SocketClient", "Reconnecting to "+getAddress()[getAddressIndex()] + " Port:" + String.valueOf(getPort()));
			socketStatus = SocketStatus.connecting;
			connectingThread = new AnsynConnect();
			connectingThread.start();
		}
	}
	
	public void close(boolean isSyn){
		
		//Log.println(Log.INFO, "SocketClient", "Close "+getAddress()[getAddressIndex()]);
		
		try {
			socketStatus = SocketStatus.closing;
			if (connectingThread != null){
				if (connectingThread.isAlive()){
					connectingThread.interrupt();
					while(isSyn == true && connectingThread.isAlive()){
						
					}
				}
				connectingThread = null;
			}
			
			if (readingThread != null){
				if (readingThread.isAlive()){
					readingThread.interrupt();
					if (inputStream != null){
						inputStream.close();
						inputStream = null;
					}
					while(isSyn == true && readingThread.isAlive()){
						
					}
				}
				readingThread = null;
			}

			handlerConnecting = null;
			
			if (inputStream != null){
				inputStream.close();
				inputStream = null;
			}
			
			if (outputStream != null){
				outputStream.close();
				outputStream = null;
			}
			
			if (this.socket != null ){
				if (this.socket.isConnected())
					this.socket.close();
				this.socket.close();
				this.socket = null;
			}
			if (socketConnectionCallback != null)
				socketConnectionCallback.onClosed();
			
		} catch (IOException e) {
			this.socket = null;
		}
		socketStatus = SocketStatus.closed;
	}
	
	
	public boolean send(byte[] data){
		try {
			if (this.socket != null && this.socket.isConnected() && !this.socket.isClosed() && this.outputStream != null){
				this.outputStream.write(data);
				return true;
			}
		} catch (IOException e) {
			return false;
		}
		return false;
	}
	
	public void read(Handler handler, int tag){
		handlerReceiving = handler;
		this.tag = tag;
		socketStatus = SocketStatus.reading;
		readingThread = new Thread(new ReadingThread());
		readingThread.setPriority(8);
		Log.println(Log.INFO, "SocketClient", "Start Reading ");
		readingThread.start();
	}
	
	public boolean isReading(){
		if (socketStatus == SocketStatus.reading){
			return true;
		}
		else
			return false;
	}
	
	public boolean isConnected(){
		if (socketStatus == SocketStatus.connected || socketStatus == SocketStatus.reading){
			return true;
		}
		else
			return false;
	}
	
	
	public abstract BaseInfo parseReceivedMessage(ByteBuffer buffer);
	
	
	
	
	
	
	private Handler handlerConnecting = null;
	
	private final int MSG_CONNECT_SUCCESSFULLY = 1;
	private final int MSG_CONNECT_FAILED = 2;
	
	final class AnsynConnect extends Thread{

		@Override
		public void run() {
			Socket sk;
			Message msg = new Message();
			try {
				InetSocketAddress socketAddress =  new InetSocketAddress(getAddress()[getAddressIndex()], getPort());// InetSocketAddress. .createUnresolved(getAddress()[getAddressIndex()], getPort());
				
				socket = null;
				sk =new Socket();
				sk.connect(socketAddress,3000);
				//sk.setKeepAlive(true);
				sk.setTcpNoDelay(true);
				//if (readingTimeout > 0)
				//	sk.setSoTimeout(readingTimeout);
				
				if (socketStatus == SocketStatus.connecting && !Thread.currentThread().isInterrupted()){
					setInputStream(sk.getInputStream());
					setOutputStream(sk.getOutputStream());
					socket = sk;
					msg.what = MSG_CONNECT_SUCCESSFULLY;
				}
				else{
					sk.shutdownInput();
					sk.shutdownOutput();
					sk.close();
				}
				
				
			} catch (UnknownHostException ex) {
				ex.printStackTrace();
				msg.what = MSG_CONNECT_FAILED;
			} catch (IOException ex) {
				ex.printStackTrace();
				msg.what = MSG_CONNECT_FAILED;
			}
			catch (Exception ex){
				ex.printStackTrace();
				msg.what = MSG_CONNECT_FAILED;
			}

			if (socketStatus == SocketStatus.connecting)
				handlerConnecting.sendMessage(msg);
			//else
				//socket = null;
		}
		
	}
	

	
	final class ReadingThread implements Runnable{
		@Override
		public void run() {
			Message message = null;
			byte[] buffer = new byte[ReceivedMessageSize];
			Bundle bundle;
			int sizeRead;
			int start;

			
			try {
				sizeRead = 0;
				start = 0;
				while (!Thread.currentThread().isInterrupted() && socket !=null && socket.isConnected() && !socket.isClosed()) {
					sizeRead = inputStream.read(buffer, start, ReceivedMessageSize-start);
					start = start + sizeRead;
					
					if (!Thread.currentThread().isInterrupted() && start  >= ReceivedMessageSize){
						message = new Message();
						message.what = MSG_SOCKETS_STATUS_RECEIVED;
						bundle = new Bundle();
						bundle.putByteArray("ReadData", buffer.clone());
						bundle.putInt("SocketClientIndex", tag);
						message.setData(bundle);
						handlerReceiving.sendMessage(message);
						start = 0;
						
					}
				}
			} catch (IOException ex) {
				if (!Thread.currentThread().isInterrupted() && socket !=null && socket.isConnected() && !socket.isClosed()){
					message = new Message();
					message.what = MSG_SOCKETS_STATUS_DROPPED;
					handlerReceiving.sendMessage(message);
				}
			}
			catch(Exception ex){
				if (!Thread.currentThread().isInterrupted() && socket !=null && socket.isConnected() && !socket.isClosed()){
					message = new Message();
					message.what = MSG_SOCKETS_STATUS_DROPPED;
					handlerReceiving.sendMessage(message);
				}
				
			}

		}
		
	}
}
