package tokiokaability.auction24.socket.client;

public interface SocketConnectionCallback {
	public void onConnectSuccessfully();
	public void onConnectionFailed();
	public void onClosed();
}
