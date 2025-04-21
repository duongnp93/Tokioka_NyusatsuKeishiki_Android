package tokiokaability.auction24.HTTPInfo;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class CarNameInfo extends BaseInfo {
	static String CAR_ITEM = "Car";
	static String CAR_NAME = "CarName";
	static String CAR_NAME_DISPLAY = "CarNameDisp";

	public String getCarNameDisp() {
		return carNameDisp;
	}

	public void setCarNameDisp(String carNameDisp) {
		this.carNameDisp = carNameDisp;
	}

	static String NAME_COUNT = "Count";

	// 車名
	private String carName = "";

	private String carNameDisp;

	// 台数
	private String count = "0";

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public CarNameInfo(List<NameValuePair> parseResult) {
		for (NameValuePair value : parseResult) {
			if (value.getName().equalsIgnoreCase(CAR_NAME)) {
				this.setCarName(value.getValue());
			} else if (value.getName().equalsIgnoreCase(NAME_COUNT)) {
				this.setCount(value.getValue());
			} else if (value.getName().equalsIgnoreCase(CAR_NAME_DISPLAY)) {
				this.setCarNameDisp(value.getValue());
			}
		}
	}

	public static List<CarNameInfo> getCarNameList(String xmlData)
			throws XMLParserException, InvalidDataException,
			ServerErrorException {

		List<CarNameInfo> carNameList = new ArrayList<CarNameInfo>();

		String tagName = null;
		String tagValue = null;

		List<NameValuePair> parserResult = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();

			xpp.setInput(new StringReader(xmlData));

			int eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					tagName = xpp.getName();
					if (tagName.equalsIgnoreCase(CAR_ITEM)) {
						parserResult = new ArrayList<NameValuePair>();
					}

				} else if (eventType == XmlPullParser.TEXT) {
					tagValue = xpp.getText();
				} else if (eventType == XmlPullParser.END_TAG) {
					if (xpp.getName().equalsIgnoreCase(TAG_STATUS)) {
						if (Integer.parseInt(tagValue) == -1)
							throw new ServerErrorException("Server Error");
					} else if (xpp.getName().equalsIgnoreCase(CAR_ITEM)) {
						CarNameInfo obj = new CarNameInfo(parserResult);
						carNameList.add(obj);
					} else {
						if (parserResult != null && (tagName != null) && (tagValue != null)) {
							parserResult.add(new BasicNameValuePair(tagName,
									tagValue));
						}
					}
					tagName = null;
					tagValue = null;
				}
				eventType = xpp.next();
			}

		} catch (XmlPullParserException ex) {
			ex.printStackTrace();
			throw new XMLParserException(ex.getMessage());
		} catch (ServerErrorException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new XMLParserException(ex.getMessage());
		}
		return carNameList;
	}

}
