package tokiokaability.auction24.HTTPInfo;

import java.util.List;

import org.apache.http.NameValuePair;

import android.util.Base64;

public class PosterInfo extends BaseInfo{
	private byte[] image = null;
    

    
	public byte[] getImage() {
		return image;
	}


	public void setImage(byte[] image) {
		this.image = image;
	}

	private String url;

	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}

	public static final String TAG_STATUS = "Status";
	public static final String TAG_IMAGE = "Image";
	public static final String TAG_URL = "Url";


	
	public PosterInfo(String data) throws XMLParserException,InvalidDataException, ServerErrorException{
		List<NameValuePair> parserResult;
		
		try{
			parserResult = BaseInfo.parse(data);
			
			for (NameValuePair nvp:parserResult){
				if(nvp.getName().equalsIgnoreCase(TAG_STATUS)){
					if (Integer.parseInt(nvp.getValue()) == -1)
						throw new ServerErrorException("Server Error");
					else
						this.setStatus(Integer.parseInt(nvp.getValue()));
				}
				else if(nvp.getName().equalsIgnoreCase(TAG_IMAGE)){
					byte[] imageData = Base64.decode(nvp.getValue(),Base64.DEFAULT);
					if (imageData != null && imageData.length > 0)
						this.setImage(imageData);
					else
						this.setImage(null);
				}
				// Adding URL - THANHLV
				else if(nvp.getName().equalsIgnoreCase(TAG_URL)){
					if (nvp.getValue() == null)
						this.setURL(null);
					else
						this.setURL(nvp.getValue().toString());
				}
			}
		}
		catch(XMLParserException ex){
			ex.printStackTrace();
			throw ex;
		}
		catch(ServerErrorException ex){
			throw ex;
		}
		catch(Exception ex){
			ex.printStackTrace();
			throw new InvalidDataException(ex.getMessage());
		}
	}
}
