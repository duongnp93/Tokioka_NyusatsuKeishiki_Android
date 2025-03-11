package tokiokaability.auction.commons;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;



public class WebServiceDownloader{
	String WebServiceURL = null;
	
	public WebServiceDownloader(String wsURL){
		WebServiceURL  = wsURL;
	}
	
	public String getData(List<NameValuePair> postValuePairs){
		String strDataFromWebService = null;

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(WebServiceURL);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(postValuePairs));
			HttpResponse response = httpClient.execute(httpPost);
			strDataFromWebService = EntityUtils.toString(response.getEntity());
			
		} catch (ClientProtocolException ex) {
			ex.printStackTrace();
			// For error notification
			strDataFromWebService = null;
		} catch (IOException ex) {
			ex.printStackTrace();
			// For error notification
			strDataFromWebService = null;
		}

		return strDataFromWebService;		
	}
}

