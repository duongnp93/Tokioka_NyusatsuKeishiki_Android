package tokiokaability.auction.HTTPInfo;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class CountryInfo extends BaseInfo {

    static String Name_result = "result";
    static String Name_Status = "Status";
    static String Name_Countries = "Countries";
    static String Name_CountryName = "Name";
    static String Name_CountryCode = "CountryCode";
    static String Name_GroupName = "GroupName";

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    String countryName;
    String countryCode;
    String groupName;

    public CountryInfo(){

    }


    public CountryInfo(List<NameValuePair> parserResult) {

        for (NameValuePair nvp:parserResult){

            if(nvp.getName().equalsIgnoreCase(Name_CountryName)){
                this.setCountryName(nvp.getValue());
            }
            else if(nvp.getName().equalsIgnoreCase(Name_CountryCode)){
                this.setCountryCode(nvp.getValue());
            }
            else if(nvp.getName().equalsIgnoreCase(Name_GroupName)){
                this.setGroupName(nvp.getValue());
            }
        }
    }

    public static List<CountryInfo> getList(String data)throws XMLParserException,InvalidDataException, ServerErrorException{
        List<CountryInfo> countryInfoList = new ArrayList<CountryInfo>();

        String tagName = null;
        String tagValue = null;

        List<NameValuePair> parserResult = null;
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(data));

            int eventType=xpp.getEventType();

            while(eventType!=XmlPullParser.END_DOCUMENT){
                if(eventType == XmlPullParser.START_TAG) {
                    tagName = xpp.getName();
                    if (tagName.equalsIgnoreCase(Name_Countries)){
                        parserResult  = new ArrayList<NameValuePair>();
                    }

                }else if(eventType == XmlPullParser.TEXT) {
                    tagValue = xpp.getText();
                }
                else if (eventType == XmlPullParser.END_TAG){
                    if(xpp.getName().equalsIgnoreCase(Name_Status)){
                        if (Integer.parseInt(tagValue) == -1)
                            throw new ServerErrorException("Server Error");
                    }
                    else if(xpp.getName().equalsIgnoreCase(Name_Countries)){
                        countryInfoList.add(new CountryInfo(parserResult));
                    }
                    else{
                        if((tagName != null)&&(tagValue != null)){
                            parserResult.add(new BasicNameValuePair(tagName,tagValue));

                        }
                    }
                    tagName = null;
                    tagValue = null;

                }

                eventType = xpp.next();
            }


        }catch(XmlPullParserException ex){
            ex.printStackTrace();
            throw new XMLParserException(ex.getMessage());
        }
        catch(ServerErrorException ex){
            ex.printStackTrace();
            throw ex;
        }
        catch(IOException ex){
            ex.printStackTrace();
            throw new XMLParserException(ex.getMessage());
        }
        return countryInfoList;
    }
}
