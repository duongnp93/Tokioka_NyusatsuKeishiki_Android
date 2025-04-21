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

public class ManufacturerInfo extends BaseInfo {

	public String getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public ManufacturerInfo() {

	}

	public ManufacturerInfo(List<NameValuePair> parseResult) {
		for (NameValuePair value : parseResult) {

			if (value.getName().equalsIgnoreCase(NAME_MANUFACTURER_ID)) {
				this.setManufacturerId(value.getValue());
			} else if (value.getName().equalsIgnoreCase(NAME_MANFACTURER_NAME)) {
				this.setManufacturerName(value.getValue());
			} else if (value.getName().equalsIgnoreCase(NAME_COUNT)) {
				this.setCount(value.getValue());
			}
		}
	}

	public static List<ManufacturerInfo> getManufacturerList(String xmlData)
			throws XMLParserException, InvalidDataException,
			ServerErrorException {

		List<ManufacturerInfo> manufacturerInfoList = new ArrayList<ManufacturerInfo>();

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
					if (tagName.equalsIgnoreCase(NAME_MANUFACTURER_ITEM)) {
						parserResult = new ArrayList<NameValuePair>();
					}

				} else if (eventType == XmlPullParser.TEXT) {
					tagValue = xpp.getText();
				} else if (eventType == XmlPullParser.END_TAG) {
					if (xpp.getName().equalsIgnoreCase(TAG_STATUS)) {
						if (Integer.parseInt(tagValue) == -1)
							throw new ServerErrorException("Server Error");
					} else if (xpp.getName().equalsIgnoreCase(
							NAME_MANUFACTURER_ITEM)) {
						ManufacturerInfo obj = new ManufacturerInfo(
								parserResult);
						manufacturerInfoList.add(obj);
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
		return manufacturerInfoList;
	}

	static String NAME_MANUFACTURER_ID = "MakerID";
	static String NAME_MANUFACTURER_ITEM = "Maker";
	static String NAME_STATUS = "Status";
	static String NAME_MANFACTURER_NAME = "MakerName";
	static String NAME_COUNT = "Count";

	// メーカーID
	private String manufacturerId = "0";
	// メーカー名
	private String manufacturerName = "";

	// 台数
	private String count = "0";

}
