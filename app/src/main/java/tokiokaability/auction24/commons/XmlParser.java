package tokiokaability.auction24.commons;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class XmlParser{
	
	String tagName = null;
	String tagValue = null;
	
	public List<NameValuePair> parse(String xmlDoc){
		
		List<NameValuePair> parserResult  = new ArrayList<NameValuePair>();
		try{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
		    
			Log.d("XXX",xmlDoc);
			xpp.setInput(new StringReader(xmlDoc));
			
			int eventType=xpp.getEventType();
        
			while(eventType!=XmlPullParser.END_DOCUMENT){
				if(eventType == XmlPullParser.START_TAG) {
		        	System.out.println("Start tag "+xpp.getName());
		        	tagName = xpp.getName();
		        } else if(eventType == XmlPullParser.END_TAG) {
		            System.out.println("End tag "+xpp.getName());
		        } else if(eventType == XmlPullParser.TEXT) {
		            System.out.println("Text "+xpp.getText());
		            tagValue = xpp.getText();
		        }
				if((tagName != null)&&(tagValue != null)){
					parserResult.add(new BasicNameValuePair(tagName,tagValue));
					tagName = null;
					tagValue = null;
				}
		        eventType = xpp.next();
			} 
		}catch(XmlPullParserException ex){
			ex.printStackTrace();
			parserResult = null;
		}catch(IOException ex){
			ex.printStackTrace();
			parserResult = null;
		}
		
		return parserResult;
	}
}