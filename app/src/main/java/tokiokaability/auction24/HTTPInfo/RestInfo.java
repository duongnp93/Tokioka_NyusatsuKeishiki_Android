package tokiokaability.auction24.HTTPInfo;

import org.apache.http.NameValuePair;

import java.util.List;

public class RestInfo extends BaseInfo{

    private String restHour;
    private String restMinute;
    private String restBox;
    private String restBoxSub;

    public String getRestHour() {
        return restHour;
    }

    public void setRestHour(String restHour) {
        this.restHour = restHour;
    }

    public String getRestMinute() {
        return restMinute;
    }

    public void setRestMinute(String restMinute) {
        this.restMinute = restMinute;
    }

    public String getRestBox() {
        return restBox;
    }

    public void setRestBox(String restBox) {
        this.restBox = restBox;
    }

    public String getRestBoxSub() {
        return restBoxSub;
    }

    public void setRestBoxSub(String restBoxSub) {
        this.restBoxSub = restBoxSub;
    }

    public static final String TAG_STATUS = "Status";
    public static final String TAG_RESTHOUR = "RestHour";
    public static final String TAG_RESTMINUTE = "RestMinute";
    public static final String TAG_RESTBOX = "RestBox";
    public static final String TAG_RESTBOXSUB = "RestBoxSub";


    public RestInfo(String data) throws XMLParserException,InvalidDataException, ServerErrorException{
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
                else if(nvp.getName().equalsIgnoreCase(TAG_RESTHOUR)){
                    if (nvp.getValue() == null)
                        this.setRestHour(null);
                    else
                        this.setRestHour(nvp.getValue());
                }
                else if(nvp.getName().equalsIgnoreCase(TAG_RESTMINUTE)){
                    if (nvp.getValue() == null)
                        this.setRestMinute(null);
                    else
                        this.setRestMinute(nvp.getValue());
                }
                else if(nvp.getName().equalsIgnoreCase(TAG_RESTBOX)){
                    if (nvp.getValue() == null)
                        this.setRestBox(null);
                    else
                        this.setRestBox(nvp.getValue());
                }
                else if(nvp.getName().equalsIgnoreCase(TAG_RESTBOXSUB)){
                    if (nvp.getValue() == null)
                        this.setRestBoxSub(null);
                    else
                        this.setRestBoxSub(nvp.getValue());
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
