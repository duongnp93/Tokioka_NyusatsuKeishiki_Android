package tokiokaability.auction.socket.client;

public interface CheckingSocketCallback {
	public void onFound();
	public void onNotFound();
	public void onDrop();
}
