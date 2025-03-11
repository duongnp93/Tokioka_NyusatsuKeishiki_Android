package tokiokaability.auction.socket.client;

public interface SocketConnectionCallback {
	public void onConnectSuccessfully();
	public void onConnectionFailed();
	public void onClosed();
}
