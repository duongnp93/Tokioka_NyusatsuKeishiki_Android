package tokiokaability.auction24.HTTPInfo;

import org.apache.http.NameValuePair;

import java.util.List;

public class PushFlgInfo extends BaseInfo {
    private String pushFlg = null;
    private String lanFlg = null;
    private String mail = null;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPushFlg() {
        return pushFlg;
    }



    public void setPushFlg(String info) {
        this.pushFlg = info;
    }

    public String getLanFlg() {
        return lanFlg;
    }

    public void setLanFlg(String lanFlg) {
        this.lanFlg = lanFlg;
    }

    public static final String TAG_STATUS = "Status";
    public static final String TAG_RESULT = "FavoriteFlg";
    public static final String TAG_LANGUAGE = "LanguageFlg";
    public static final String TAG_MAIL = "MailBK";



    public PushFlgInfo(String data) throws XMLParserException,InvalidDataException, ServerErrorException{
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
                    this.setPushFlg(nvp.getValue());
                }
                else if(nvp.getName().equalsIgnoreCase(TAG_LANGUAGE)){
                    this.setLanFlg(nvp.getValue());
                }
                else if(nvp.getName().equalsIgnoreCase(TAG_MAIL)){
                    this.setMail(nvp.getValue());
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
