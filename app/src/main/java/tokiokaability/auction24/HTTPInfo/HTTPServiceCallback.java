package tokiokaability.auction24.HTTPInfo;

public interface HTTPServiceCallback {
	public void onSucessfully(Object returnData);
	public void onFailed(int errorCode);
}
