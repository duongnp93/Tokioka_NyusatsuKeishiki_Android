package tokiokaability.auction24.HTTPInfo;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

public class SearchInfo extends BaseInfo {

    ArrayList<String> lstCat,lstMedCat,lstBrand,lstRank;

    public ArrayList<String> getLstCat() {
        return lstCat;
    }

    public void setLstCat(ArrayList<String> lstCat) {
        this.lstCat = lstCat;
    }

    public ArrayList<String> getLstMedCat() {
        return lstMedCat;
    }

    public void setLstMedCat(ArrayList<String> lstMedCat) {
        this.lstMedCat = lstMedCat;
    }

    public ArrayList<String> getLstBrand() {
        return lstBrand;
    }

    public void setLstBrand(ArrayList<String> lstBrand) {
        this.lstBrand = lstBrand;
    }

    public ArrayList<String> getLstRank() {
        return lstRank;
    }

    public void setLstRank(ArrayList<String> lstRank) {
        this.lstRank = lstRank;
    }

    private final String TAG_CATEGORY = "Category";
    private final String TAG_MEDCAT = "SubCat";
    private  final  String TAG_BRAND = "Brand";
    private final String TAG_RANK = "Rank";
    public SearchInfo(String data) throws XMLParserException,InvalidDataException, ServerErrorException{
        List<NameValuePair> parserResult;
        lstCat = new ArrayList<>();
        lstMedCat = new ArrayList<>();
        lstBrand = new ArrayList<>();
        lstRank = new ArrayList<>();
        try{
            parserResult = BaseInfo.parseNullValue(data,TAG_BRAND);

            for (NameValuePair nvp:parserResult){
                if(nvp.getName().equalsIgnoreCase(TAG_STATUS)){
                    if (Integer.parseInt(nvp.getValue()) == -1)
                        throw new ServerErrorException("Server Error");
                    else
                        this.setStatus(Integer.parseInt(nvp.getValue()));
                }
                else if(nvp.getName().equalsIgnoreCase(TAG_CATEGORY)){
                    lstCat.add(nvp.getValue());
                }
                else if(nvp.getName().equalsIgnoreCase(TAG_MEDCAT)){
                    lstMedCat.add(nvp.getValue());
                }
                else if(nvp.getName().equalsIgnoreCase(TAG_BRAND)){
                    lstBrand.add(nvp.getValue());
                }
                else if(nvp.getName().equalsIgnoreCase(TAG_RANK)){
                    lstRank.add(nvp.getValue());
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
