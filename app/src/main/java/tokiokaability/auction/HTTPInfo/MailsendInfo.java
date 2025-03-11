package tokiokaability.auction.HTTPInfo;

import java.util.List;

import org.apache.http.NameValuePair;

public class MailsendInfo extends BaseInfo{
    private String result = null;



    public String getResult() {
        return result;
    }



    public void setResult(String info) {
        this.result = info;
    }



    public static final String TAG_STATUS = "Status";
    public static final String TAG_RESULT = "Result";



    public MailsendInfo(String data) throws XMLParserException,InvalidDataException, ServerErrorException{
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
                else if(nvp.getName().equalsIgnoreCase(TAG_RESULT)){
                    this.setResult(nvp.getValue());
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
