package tokiokaability.auction24.HTTPInfo;

import android.util.Base64;

import org.apache.http.NameValuePair;

import java.util.List;

public class ImageInfo extends BaseInfo {
    public byte[] getImg1() {
        return img1;
    }

    public void setImg1(byte[] img1) {
        this.img1 = img1;
    }

    public byte[] getImg2() {
        return img2;
    }

    public void setImg2(byte[] img2) {
        this.img2 = img2;
    }

    public byte[] getImg3() {
        return img3;
    }

    public void setImg3(byte[] img3) {
        this.img3 = img3;
    }

    public byte[] getImg4() {
        return img4;
    }

    public void setImg4(byte[] img4) {
        this.img4 = img4;
    }

    public byte[] getImg5() {
        return img5;
    }

    public void setImg5(byte[] img5) {
        this.img5 = img5;
    }

    public byte[] getImg6() {
        return img6;
    }

    public void setImg6(byte[] img6) {
        this.img6 = img6;
    }

    public byte[] getImg7() {
        return img7;
    }

    public void setImg7(byte[] img7) {
        this.img7 = img7;
    }

    public byte[] getImg8() {
        return img8;
    }

    public void setImg8(byte[] img8) {
        this.img8 = img8;
    }

    public byte[] getImg9() {
        return img9;
    }

    public void setImg9(byte[] img9) {
        this.img9 = img9;
    }

    public byte[] getImg10() {
        return img10;
    }

    public void setImg10(byte[] img10) {
        this.img10 = img10;
    }

    private byte[] img1;
    private byte[] img2;
    private byte[] img3;
    private byte[] img4;
    private byte[] img5;
    private byte[] img6;
    private byte[] img7;
    private byte[] img8;
    private byte[] img9;
    private byte[] img10;

    private String Name_Img1 = "Image1";
    private String Name_Img2 = "Image2";
    private String Name_Img3 = "Image3";
    private String Name_Img4 = "Image4";
    private String Name_Img5 = "Image5";
    private String Name_Img6 = "Image6";
    private String Name_Img7 = "Image7";
    private String Name_Img8 = "Image8";
    private String Name_Img9 = "Image9";
    private String Name_Img10 = "Image10";


    public ImageInfo(String data) throws XMLParserException,InvalidDataException, ServerErrorException{
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
                else if(nvp.getName().equalsIgnoreCase(Name_Img1)){
                    byte[] imageData = Base64.decode(nvp.getValue(),
                            Base64.DEFAULT);
                    if (imageData != null && imageData.length > 0)
                        this.setImg1(imageData);
                    else
                        this.setImg1(null);
                }
                else if(nvp.getName().equalsIgnoreCase(Name_Img2)){
                    byte[] imageData = Base64.decode(nvp.getValue(),
                            Base64.DEFAULT);
                    if (imageData != null && imageData.length > 0)
                        this.setImg2(imageData);
                    else
                        this.setImg2(null);
                }
                else if(nvp.getName().equalsIgnoreCase(Name_Img3)){
                    byte[] imageData = Base64.decode(nvp.getValue(),
                            Base64.DEFAULT);
                    if (imageData != null && imageData.length > 0)
                        this.setImg3(imageData);
                    else
                        this.setImg3(null);
                }
                else if(nvp.getName().equalsIgnoreCase(Name_Img4)){
                    byte[] imageData = Base64.decode(nvp.getValue(),
                            Base64.DEFAULT);
                    if (imageData != null && imageData.length > 0)
                        this.setImg4(imageData);
                    else
                        this.setImg4(null);
                }
                else if(nvp.getName().equalsIgnoreCase(Name_Img5)){
                    byte[] imageData = Base64.decode(nvp.getValue(),
                            Base64.DEFAULT);
                    if (imageData != null && imageData.length > 0)
                        this.setImg5(imageData);
                    else
                        this.setImg5(null);
                }
                else if(nvp.getName().equalsIgnoreCase(Name_Img6)){
                    byte[] imageData = Base64.decode(nvp.getValue(),
                            Base64.DEFAULT);
                    if (imageData != null && imageData.length > 0)
                        this.setImg6(imageData);
                    else
                        this.setImg6(null);
                }
                else if(nvp.getName().equalsIgnoreCase(Name_Img7)){
                    byte[] imageData = Base64.decode(nvp.getValue(),
                            Base64.DEFAULT);
                    if (imageData != null && imageData.length > 0)
                        this.setImg7(imageData);
                    else
                        this.setImg7(null);
                }
                else if(nvp.getName().equalsIgnoreCase(Name_Img8)){
                    byte[] imageData = Base64.decode(nvp.getValue(),
                            Base64.DEFAULT);
                    if (imageData != null && imageData.length > 0)
                        this.setImg8(imageData);
                    else
                        this.setImg8(null);
                }
                else if(nvp.getName().equalsIgnoreCase(Name_Img9)){
                    byte[] imageData = Base64.decode(nvp.getValue(),
                            Base64.DEFAULT);
                    if (imageData != null && imageData.length > 0)
                        this.setImg9(imageData);
                    else
                        this.setImg9(null);
                }
                else if(nvp.getName().equalsIgnoreCase(Name_Img10)){
                    byte[] imageData = Base64.decode(nvp.getValue(),
                            Base64.DEFAULT);
                    if (imageData != null && imageData.length > 0)
                        this.setImg10(imageData);
                    else
                        this.setImg10(null);
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
