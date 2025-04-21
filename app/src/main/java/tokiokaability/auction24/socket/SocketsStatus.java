package tokiokaability.auction24.socket;

public enum SocketsStatus {
	init,
	Searching,
	Connecting,
	CompletedConnectSuccessfully,
	CompletedConnectFailed,
	ConnectionDropped,
	//Reconnecting,
	//CompletedReconnectSuccessfully,
	//CompletedReconnectFailed,
	Reading,
	Disconnected
}
