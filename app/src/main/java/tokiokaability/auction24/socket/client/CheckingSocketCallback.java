package tokiokaability.auction24.socket.client;

public interface CheckingSocketCallback {
	public void onFound();
	public void onNotFound();
	public void onDrop();
}
