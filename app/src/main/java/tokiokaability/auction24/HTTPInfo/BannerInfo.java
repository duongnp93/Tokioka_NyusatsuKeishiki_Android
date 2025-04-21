package tokiokaability.auction24.HTTPInfo;

import android.util.Base64;

import org.apache.http.NameValuePair;

import java.util.List;

public class BannerInfo extends BaseInfo{
	private String info = null;
	private String bannerUrl1 = null;
	private byte[] bannerImage1 = null;
	private String bannerUrlDelFlag1 = null;
	private String bannerUrl2 = null;
	private byte[] bannerImage2 = null;
	private String bannerUrlDelFlag2 = null;
    private String bannerUrl3 = null;
    private byte[] bannerImage3 = null;
    private String bannerUrlDelFlag3 = null;
    private String bannerUrl4 = null;

    public String getBannerUrl3() {
        return bannerUrl3;
    }

    public void setBannerUrl3(String bannerUrl3) {
        this.bannerUrl3 = bannerUrl3;
    }

    public byte[] getBannerImage3() {
        return bannerImage3;
    }

    public void setBannerImage3(byte[] bannerImage3) {
        this.bannerImage3 = bannerImage3;
    }

    public String getBannerUrlDelFlag3() {
        return bannerUrlDelFlag3;
    }

    public void setBannerUrlDelFlag3(String bannerUrlDelFlag3) {
        this.bannerUrlDelFlag3 = bannerUrlDelFlag3;
    }

    public String getBannerUrl4() {
        return bannerUrl4;
    }

    public void setBannerUrl4(String bannerUrl4) {
        this.bannerUrl4 = bannerUrl4;
    }

    public byte[] getBannerImage4() {
        return bannerImage4;
    }

    public void setBannerImage4(byte[] bannerImage4) {
        this.bannerImage4 = bannerImage4;
    }

    public String getBannerUrlDelFlag4() {
        return bannerUrlDelFlag4;
    }

    public void setBannerUrlDelFlag4(String bannerUrlDelFlag4) {
        this.bannerUrlDelFlag4 = bannerUrlDelFlag4;
    }

    public String getBannerUrl5() {
        return bannerUrl5;
    }

    public void setBannerUrl5(String bannerUrl5) {
        this.bannerUrl5 = bannerUrl5;
    }

    public byte[] getBannerImage5() {
        return bannerImage5;
    }

    public void setBannerImage5(byte[] bannerImage5) {
        this.bannerImage5 = bannerImage5;
    }

    public String getBannerUrlDelFlag5() {
        return bannerUrlDelFlag5;
    }

    public void setBannerUrlDelFlag5(String bannerUrlDelFlag5) {
        this.bannerUrlDelFlag5 = bannerUrlDelFlag5;
    }

    private byte[] bannerImage4 = null;
    private String bannerUrlDelFlag4 = null;
    private String bannerUrl5 = null;
    private byte[] bannerImage5 = null;
    private String bannerUrlDelFlag5 = null;
	private byte[] startImage = null;

	private String bannerUrlTop = null;

	public String getBannerUrlTop() {
		return bannerUrlTop;
	}

	public void setBannerUrlTop(String bannerUrlTop) {
		this.bannerUrlTop = bannerUrlTop;
	}

	public String getBannerUrlTopDelFlg() {
		return bannerUrlTopDelFlg;
	}

	public void setBannerUrlTopDelFlg(String bannerUrlTopDelFlg) {
		this.bannerUrlTopDelFlg = bannerUrlTopDelFlg;
	}

	private String bannerUrlTopDelFlg = null;

	public byte[] getLoginImage() {
		return loginImage;
	}

	public void setLoginImage(byte[] loginImage) {
		this.loginImage = loginImage;
	}

	public byte[] getLoginLogo() {
		return loginLogo;
	}

	public void setLoginLogo(byte[] loginLogo) {
		this.loginLogo = loginLogo;
	}

	private byte[] loginImage = null;
	private byte[] loginLogo = null;

	public String getBannerUrlTopContent() {
		return bannerUrlTopContent;
	}

	public void setBannerUrlTopContent(String bannerUrlTopContent) {
		this.bannerUrlTopContent = bannerUrlTopContent;
	}

	private String bannerUrlTopContent = null;
	public String getInfo() {
		return info;
	}
	public String getBannerUrl1() {
		return bannerUrl1;
	}
	public byte[] getBannerImage1() { return bannerImage1; }
	public String getBannerUrlDelFlag1() { return bannerUrlDelFlag1; }
	public String getBannerUrl2() {
		return bannerUrl2;
	}
	public byte[] getBannerImage2() { return bannerImage2; }
	public String getBannerUrlDelFlag2() { return bannerUrlDelFlag2; }
	public byte[] getStartImage() { return startImage; }

	public void setInfo(String info) {
		this.info = info;
	}
	public void setBannerUrl1(String banner_url_1) {
		this.bannerUrl1 = banner_url_1;
	}
	public void setBannerImage1(byte[] banner_image_1) {
		this.bannerImage1 = banner_image_1;
	}
	public void setBannerUrlDelFlag1(String banner_url_del_flag1) {
		this.bannerUrlDelFlag1 = banner_url_del_flag1;
	}
	public void setBannerUrl2(String banner_url_2) {
		this.bannerUrl2 = banner_url_2;
	}
	public void setBannerImage2(byte[] banner_image_2) {
		this.bannerImage2 = banner_image_2;
	}
	public void setBannerUrlDelFlag2(String banner_url_del_flag2) {
		this.bannerUrlDelFlag2 = banner_url_del_flag2;
	}
	public void setStartImage(byte[] start_image) {
		this.startImage = start_image;
	}



	public static final String TAG_STATUS = "Status";
	public static final String BANNER_URL_1 = "BannerUrl1";
	public static final String BANNER_IMAGE_1 = "BannerImage1";
	public static final String BANNER_URL_DEL_FLAG_1 = "BannerUrlDelFlag1";
	public static final String BANNER_URL_2 = "BannerUrl2";
	public static final String BANNER_IMAGE_2 = "BannerImage2";
	public static final String BANNER_URL_DEL_FLAG_2 = "BannerUrlDelFlag2";
	public static final String START_IMAGE = "StartImage";
	public static final String LOGIN_IMAGE = "LoginImage";
	public static final String LOGIN_LOGO = "LoginLogo";
    public static final String BANNER_URL_3 = "BannerUrl3";
    public static final String BANNER_IMAGE_3 = "BannerImage3";
    public static final String BANNER_URL_DEL_FLAG_3 = "BannerUrlDelFlag3";
    public static final String BANNER_URL_4 = "BannerUrl4";
    public static final String BANNER_IMAGE_4 = "BannerImage4";
    public static final String BANNER_URL_DEL_FLAG_4 = "BannerUrlDelFlag4";
    public static final String BANNER_URL_5 = "BannerUrl5";
    public static final String BANNER_IMAGE_5 = "BannerImage5";
    public static final String BANNER_URL_DEL_FLAG_5 = "BannerUrlDelFlag5";
	public static final String BANNER_URL_TOP = "BannerUrlTop";
	public static final String BANNER_URL_TOP_DEL_FLG = "BannerUrlTopDelFlg";
	public static final String BANNER_URL_TOP_CONTENT = "BannerUrlTopContent";


	public BannerInfo(String data) throws XMLParserException,InvalidDataException, ServerErrorException{
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
				else if(nvp.getName().equalsIgnoreCase(BANNER_URL_1)){
					this.setBannerUrl1(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(BANNER_IMAGE_1)){
					byte[] imageData = Base64.decode(nvp.getValue(),
							Base64.DEFAULT);
					if (imageData != null && imageData.length > 0)
						this.setBannerImage1(imageData);
					else
						this.setBannerImage1(null);
				}
				else if(nvp.getName().equalsIgnoreCase(BANNER_URL_DEL_FLAG_1)){
					this.setBannerUrlDelFlag1(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(BANNER_URL_2)){
					this.setBannerUrl2(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(BANNER_IMAGE_2)){
					byte[] imageData = Base64.decode(nvp.getValue(),
							Base64.DEFAULT);
					if (imageData != null && imageData.length > 0)
						this.setBannerImage2(imageData);
					else
						this.setBannerImage2(null);
				}
				else if(nvp.getName().equalsIgnoreCase(BANNER_URL_DEL_FLAG_2)){
					this.setBannerUrlDelFlag2(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(START_IMAGE)){
					byte[] imageData = Base64.decode(nvp.getValue(),
							Base64.DEFAULT);
					if (imageData != null && imageData.length > 0)
						this.setStartImage(imageData);
					else
						this.setStartImage(null);
				}
				else if(nvp.getName().equalsIgnoreCase(LOGIN_IMAGE)){
					byte[] imageData = Base64.decode(nvp.getValue(),
							Base64.DEFAULT);
					if (imageData != null && imageData.length > 0)
						this.setLoginImage(imageData);
					else
						this.setLoginImage(null);
				}
				else if(nvp.getName().equalsIgnoreCase(LOGIN_LOGO)){
					byte[] imageData = Base64.decode(nvp.getValue(),
							Base64.DEFAULT);
					if (imageData != null && imageData.length > 0)
						this.setLoginLogo(imageData);
					else
						this.setLoginLogo(null);
				}
                else if(nvp.getName().equalsIgnoreCase(BANNER_URL_3)){
                    this.setBannerUrl3(nvp.getValue());
                }
                else if(nvp.getName().equalsIgnoreCase(BANNER_IMAGE_3)){
                    byte[] imageData = Base64.decode(nvp.getValue(),
                            Base64.DEFAULT);
                    if (imageData != null && imageData.length > 0)
                        this.setBannerImage3(imageData);
                    else
                        this.setBannerImage3(null);
                }
                else if(nvp.getName().equalsIgnoreCase(BANNER_URL_DEL_FLAG_3)){
                    this.setBannerUrlDelFlag3(nvp.getValue());
                }
                else if(nvp.getName().equalsIgnoreCase(BANNER_URL_4)){
                    this.setBannerUrl4(nvp.getValue());
                }
                else if(nvp.getName().equalsIgnoreCase(BANNER_IMAGE_4)){
                    byte[] imageData = Base64.decode(nvp.getValue(),
                            Base64.DEFAULT);
                    if (imageData != null && imageData.length > 0)
                        this.setBannerImage4(imageData);
                    else
                        this.setBannerImage4(null);
                }
                else if(nvp.getName().equalsIgnoreCase(BANNER_URL_DEL_FLAG_4)){
                    this.setBannerUrlDelFlag4(nvp.getValue());
                }
                else if(nvp.getName().equalsIgnoreCase(BANNER_URL_5)){
                    this.setBannerUrl5(nvp.getValue());
                }
                else if(nvp.getName().equalsIgnoreCase(BANNER_IMAGE_5)){
                    byte[] imageData = Base64.decode(nvp.getValue(),
                            Base64.DEFAULT);
                    if (imageData != null && imageData.length > 0)
                        this.setBannerImage5(imageData);
                    else
                        this.setBannerImage5(null);
                }
                else if(nvp.getName().equalsIgnoreCase(BANNER_URL_DEL_FLAG_5)){
                    this.setBannerUrlDelFlag5(nvp.getValue());
                }
				else if(nvp.getName().equalsIgnoreCase(BANNER_URL_TOP)){
					this.setBannerUrlTop(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(BANNER_URL_TOP_DEL_FLG)){
					this.setBannerUrlTopDelFlg(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(BANNER_URL_TOP_CONTENT)){
					this.setBannerUrlTopContent(nvp.getValue());
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
