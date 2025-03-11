package tokiokaability.auction.HTTPInfo;

import org.apache.http.NameValuePair;

import java.util.List;


public class NegotiationInfo extends BaseInfo {

	public static String Name_result = "result";
	public static String Name_Status = "Status";
	public static String Name_AskingPrice = "AskingPrice";
	public static String Name_LastPrice = "LastPrice";
	public static String Name_HoldPrice = "HoldPrice";
	public static String Name_ReHoldPrice = "ReHoldPrice";
	public static String Name_HoldFlg = "HoldFlg";
	public static String Name_AbsenceBid = "AbsenceBidPrice";
	public static String Name_Memo = "Memo";
	public static String Name_ErrorMess = "ErrorMessage";

	private String askingPrice = "";
	private String lastPrice = "";
	private String holdPrice = "";
	private String reholdPrice = "";
	private String absenceBidPrice = "";
	private String memo = "";





	public String getReholdPrice() {
		return reholdPrice;
	}

	public void setReholdPrice(String reholdPrice) {
		this.reholdPrice = reholdPrice;
	}

	public String getHoldFlg() {
		return holdFlg;
	}

	public void setHoldFlg(String holdFlg) {
		this.holdFlg = holdFlg;
	}

	private String holdFlg = "";

	public String getAskingPrice() {
		return askingPrice;
	}

	public void setAskingPrice(String askingPrice) {
		this.askingPrice = askingPrice;
	}

	public String getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(String lastPrice) {
		this.lastPrice = lastPrice;
	}

	public String getHoldPrice() {
		return holdPrice;
	}

	public void setHoldPrice(String holdPrice) {
		this.holdPrice = holdPrice;
	}

	public String getAbsenceBidPrice() {
		return absenceBidPrice;
	}

	public void setAbsenceBidPrice(String absenceBidPrice) {
		this.absenceBidPrice = absenceBidPrice;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public NegotiationInfo(String data) throws XMLParserException, InvalidDataException, ServerErrorException {
		List<NameValuePair> parserResult;

		try {
			parserResult = BaseInfo.parse(data);

			for (NameValuePair nvp : parserResult) {
				if (nvp.getName().equalsIgnoreCase(TAG_STATUS)) {
					if (Integer.parseInt(nvp.getValue()) == -1) {
						throw new ServerErrorException("Server Error");
					} else {
						this.setStatus(Integer.parseInt(nvp.getValue()));
					}
				} else if (nvp.getName().equalsIgnoreCase(Name_AskingPrice)) {
					this.setAskingPrice(nvp.getValue());
				} else if (nvp.getName().equalsIgnoreCase(Name_LastPrice)) {
					this.setLastPrice(nvp.getValue());
				} else if (nvp.getName().equalsIgnoreCase(Name_HoldPrice)) {
					this.setHoldPrice(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_HoldFlg)) {
					this.setHoldFlg(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_ReHoldPrice)) {
					this.setReholdPrice(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_AbsenceBid)) {
					this.setAbsenceBidPrice(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_Memo)) {
					this.setMemo(nvp.getValue());
				}
			}
		} catch (XMLParserException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (ServerErrorException ex) {
			throw ex;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InvalidDataException(ex.getMessage());
		}
	}
}
