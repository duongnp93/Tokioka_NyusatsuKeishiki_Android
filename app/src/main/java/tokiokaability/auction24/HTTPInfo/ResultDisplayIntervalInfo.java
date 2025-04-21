package tokiokaability.auction24.HTTPInfo;

import org.apache.http.NameValuePair;

import java.util.List;


public class ResultDisplayIntervalInfo extends BaseInfo {

	public double getInterval() {
		return m_interval;
	}
	public void setInterval(double interval) { m_interval = interval; }

	private double m_interval = 0;


	public static final String TAG_STATUS = "Status";
	public static final String TAG_INTERVAL = "Interval";

	public ResultDisplayIntervalInfo(){
		super();
	}

	public ResultDisplayIntervalInfo(String data) throws XMLParserException,InvalidDataException, ServerErrorException{
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
				else if(nvp.getName().equalsIgnoreCase(TAG_INTERVAL)){
					try {
						this.setInterval(Double.parseDouble(nvp.getValue()));
					}
					catch (Exception e){
						this.setInterval(0);
					}
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
