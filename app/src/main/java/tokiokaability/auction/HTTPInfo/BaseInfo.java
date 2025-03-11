package tokiokaability.auction.HTTPInfo;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class BaseInfo {
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public static final String TAG_STATUS = "Status";
	public static final String TAG_MESSAGE = "ErrorMessage";
	public static final String TAG_MESSAGE_ENG = "ErrorMessageEnglish";
	private int status;
	private String message;
	private String messageEnglish;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageEnglish() {
		return messageEnglish;
	}

	public void setMessageEnglish(String messageEnglish) {
		this.messageEnglish = messageEnglish;
	}

	public static List<NameValuePair> parse(String xmlDoc)
			throws XMLParserException {
		String tagName = null;
		String tagValue = null;

		List<NameValuePair> parserResult = new ArrayList<NameValuePair>();
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();

			xpp.setInput(new StringReader(xmlDoc));

			int eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					tagName = xpp.getName();
				} else if (eventType == XmlPullParser.TEXT && tagName != null
						&& tagValue == null) {
					tagValue = xpp.getText();
				}
				if ((tagName != null) && (tagValue != null)) {
					parserResult.add(new BasicNameValuePair(tagName, tagValue));
					tagName = null;
					tagValue = null;
				}
				eventType = xpp.next();
			}
		} catch (XmlPullParserException ex) {
			throw new XMLParserException(ex.getMessage());
		} catch (IOException ex) {
			throw new XMLParserException(ex.getMessage());
		}

		return parserResult;
	}

	public static List<NameValuePair> parseNullValue(String xmlDoc,String excepTag)
			throws XMLParserException {
		String tagName = null;
		String tagValue = null;

		List<NameValuePair> parserResult = new ArrayList<NameValuePair>();
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();

			xpp.setInput(new StringReader(xmlDoc));

			int eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					tagName = xpp.getName();
				} else if (eventType == XmlPullParser.TEXT && tagName != null
						&& tagValue == null) {
					tagValue = xpp.getText();
				}
				if ((tagName != null) && (tagValue != null)) {
					parserResult.add(new BasicNameValuePair(tagName, tagValue));
					tagName = null;
					tagValue = null;
				}
				else if ((tagName != null)&&(tagName.equals(excepTag)) && (tagValue == null) && eventType != XmlPullParser.START_TAG) {
					parserResult.add(new BasicNameValuePair(xpp.getName(), ""));
				}



				eventType = xpp.next();
			}
		} catch (XmlPullParserException ex) {
			throw new XMLParserException(ex.getMessage());
		} catch (IOException ex) {
			throw new XMLParserException(ex.getMessage());
		}

		return parserResult;
	}

	public BaseInfo() {
		super();
	}

	/*
	 * parse error xml data.
	 * */
	public BaseInfo(String data) throws XMLParserException,
			InvalidDataException, ServerErrorException {
		List<NameValuePair> parserResult;

		try {
			parserResult = BaseInfo.parse(data);

			for (NameValuePair nvp : parserResult) {
				if (nvp.getName().equalsIgnoreCase(TAG_STATUS)) {
					this.setStatus(Integer.parseInt(nvp.getValue()));
				} else if (nvp.getName().equalsIgnoreCase(TAG_MESSAGE)) {
					this.setMessage(nvp.getValue());
				} else if (nvp.getName().equalsIgnoreCase(TAG_MESSAGE_ENG)) {
					this.setMessageEnglish(nvp.getValue());
				}
			}
		} catch (XMLParserException ex) {
			ex.printStackTrace();
			throw ex;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InvalidDataException(ex.getMessage());
		}
	}
}