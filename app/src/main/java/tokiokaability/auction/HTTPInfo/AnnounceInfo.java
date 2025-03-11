package tokiokaability.auction.HTTPInfo;

import java.util.List;

import org.apache.http.NameValuePair;

public class AnnounceInfo extends BaseInfo{
	private String info = null;
    

    
	public String getInfo() {
		return info;
	}



	public void setInfo(String info) {
		this.info = info;
	}



	public static final String TAG_STATUS = "Status";
	public static final String TAG_INFO = "Info";


	
	public AnnounceInfo(String data) throws XMLParserException,InvalidDataException, ServerErrorException{
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
				else if(nvp.getName().equalsIgnoreCase(TAG_INFO)){
					this.setInfo(nvp.getValue());
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
