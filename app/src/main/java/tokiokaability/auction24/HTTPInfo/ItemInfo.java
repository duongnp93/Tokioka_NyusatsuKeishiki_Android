package tokiokaability.auction24.HTTPInfo;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Base64;


public class ItemInfo extends BaseInfo {
	private int itemNo = 0;
	private byte[] image1 = null;
	private byte[] image2 = null;
    private byte[] image3 = null;
    private byte[] image4 = null;
    private byte[] image5 = null;
	private byte[] image6 = null;
	private byte[] image7 = null;
	private byte[] image8 = null;
	private byte[] image9 = null;
	private byte[] image10 = null;



	private String ItemName = "";//品名: 		item_nm
	private String BrandName = "";//ブランド名: 	brand_nm
	private String KindName = ""; //品種:			kind_nm
	private String Rank = "";//状態:			state				????????????value????????????????
	private String ListPrice = "";//定価:			list_price




	private String BoxNo = ""; // 箱番
	private String BoxSubNo = ""; // 枝番

	private String Result = ""; // セリ結果
	private String ResultName = ""; // 結果名
	private String StartPrice = ""; // スタート価格   //スタート価格: str_price
	private String LastPrice = ""; // 落札価格  //最終応札価格: last_bid_price
	private String StartTime = ""; // セリ開始時間  //せり開始時間: t_selling_time.selling_start_time
	private String AbsencePrice = ""; // 不在入札価格
	private String BidCount = ""; // 応札回数
	/* Tho moi them */
	public static int PageNo = 0; // ページ番号（１～）
	public static int RecordCount = 0; // 検索結果件数　※返却件数ではなく、総検索結果件数

	public static int TotalPrice = 0;
	public static  String Message = "";
	public static  String PlaceLst = "";
	public static  String SessionLst = "";
	public static  String ItemLst = "";

	private String placeId = ""; // 会場ID
	private String placeName = ""; // 会場名
	private String timesNo = ""; // 回次番号
	private String holdDate = ""; // 開催日
	private int CorrectionCount = 0; // 訂正回数
	private String CorrectionDetail = ""; // 訂正内容
	private int AbsenceFlag = 0;
	private int RegistFlag = 0; // 写真登録可否フラグ

	private int MemberType = 0;
	private String SellMemberNo = "";
	private String OkiniriFlg = "";
	private String CornerNm = "";
	private double RateDollar = 1.0;
	private double RateEuro = 1.0;
	private double RateYuan = 1.0;
	private double RateHKD = 1.0;

	public String getHoldPrice() {
		return HoldPrice;
	}

	public void setHoldPrice(String holdPrice) {
		HoldPrice = holdPrice;
	}

	public String getSellingResult() {
		return SellingResult;
	}

	public void setSellingResult(String sellingResult) {
		SellingResult = sellingResult;
	}

	public String getSellingResultBK() {
		return SellingResultBK;
	}

	public void setSellingResultBK(String sellingResultBK) {
		SellingResultBK = sellingResultBK;
	}

	private String HoldPrice = "";  // 交渉金額
	private String SellingResult = "";  // セリ結果区分
	private String SellingResultBK = "";  // セリ結果区分バックアップ
	private String JishaFlg = "";
	private String HoldFlg = "";
	private String ReholdPrice="";
	private String FinalBidPrice="";

	public String getFinalBidPrice() {
		return FinalBidPrice;
	}

	public void setFinalBidPrice(String finalBidPrice) {
		FinalBidPrice = finalBidPrice;
	}

	public String getMemoFlg() {
		return MemoFlg;
	}

	public void setMemoFlg(String memoFlg) {
		MemoFlg = memoFlg;
	}

	private String MemoFlg = "";

	public String getHoldFlg() {
		return HoldFlg;
	}

	public void setHoldFlg(String holdFlg) {
		HoldFlg = holdFlg;
	}

	public String getReholdPrice() {
		return ReholdPrice;
	}

	public void setReholdPrice(String reholdPrice) {
		ReholdPrice = reholdPrice;
	}

	public String getOkiniriFlg() {
		return OkiniriFlg;
	}

	public void setOkiniriFlg(String okiniriFlg) {
		OkiniriFlg = okiniriFlg;
	}

	public String getCornerNm() {
		return CornerNm;
	}

	public void setCornerNm(String cornerNm) {
		CornerNm = cornerNm;
	}

	public String getJishaFlg() {
		return JishaFlg;
	}

	public void setJishaFlg(String jishaFlg) {
		JishaFlg = jishaFlg;
	}

	String BidDate = "";
	String Brand ="";
	String Material ="";
	String Model1="";
	String Model2 = "";
	String SerialNo ="";
	String Accessories ="";
	String Bless="";
	String IndividualNo = "";
	String Color ="";
	String Size ="";
	String Instrument="";
	String Details = "";
	String Comments ="";
	String Memo ="";
	String SellingSort ="";
	String AbsenceBidPrice="";
	String CornerID = "";
	String Line = "";
	String RefPrice = "";
	String SubCat = "";

	public String getCorrection() {
		return Correction;
	}

	public void setCorrection(String correction) {
		Correction = correction;
	}

	String Correction = "";

	public String getSubCat() {
		return SubCat;
	}

	public void setSubCat(String subCat) {
		SubCat = subCat;
	}

	public String getAuctionResult() {
		return AuctionResult;
	}

	public void setAuctionResult(String auctionResult) {
		AuctionResult = auctionResult;
	}

	public String getAskingPrice() {
		return AskingPrice;
	}

	public void setAskingPrice(String askingPrice) {
		AskingPrice = askingPrice;
	}

	public String getCornerID() {
		return CornerID;
	}

	public void setCornerID(String cornerID) {
		CornerID = cornerID;
	}

	public String getLine() {
		return Line;
	}

	public void setLine(String line) {
		Line = line;
	}

	private String AuctionResult = "";
	private String AskingPrice = "";
	private String OtherLaneSellNo = "";

	public String getLastBidMemberNo() {
		return LastBidMemberNo;
	}

	public void setLastBidMemberNo(String lastBidMemberNo) {
		LastBidMemberNo = lastBidMemberNo;
	}

	private String LastBidMemberNo = "";


	private String QR = "";
	private String QR2 = "";
	private String QR3 = "";
	private String QR4 = "";
	private String Size2 = "";
	private String Size3 = "";
	private String Size4 = "";


	public String getQR() {
		return QR;
	}

	public void setQR(String QR) {
		this.QR = QR;
	}

	public String getQR2() {
		return QR2;
	}

	public void setQR2(String QR2) {
		this.QR2 = QR2;
	}

	public String getQR3() {
		return QR3;
	}

	public void setQR3(String QR3) {
		this.QR3 = QR3;
	}

	public String getQR4() {
		return QR4;
	}

	public void setQR4(String QR4) {
		this.QR4 = QR4;
	}

	public String getSize2() {
		return Size2;
	}

	public void setSize2(String size2) {
		Size2 = size2;
	}

	public String getSize3() {
		return Size3;
	}

	public void setSize3(String size3) {
		Size3 = size3;
	}

	public String getSize4() {
		return Size4;
	}

	public void setSize4(String size4) {
		Size4 = size4;
	}

	/**
	 * One Price params
	 * @author LAMLT
	 */
	private String OnePrice = "";
	private String OnePriceStatusKbn = ""; //０	出品中、１	申込中、２	自社落札、３	他社落札、４	出品取消、５	制約取消、9	流れ
	private String OnePriceStatusKbnName = ""; //０	出品中、１	申込中、２	自社落札、３	他社落札、４	出品取消、５	制約取消、9	流れ
	
	public int getMemberType() {
		return MemberType;
	}

	public void setMemberType(int memberType) {
		MemberType = memberType;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getTimesNo() {
		return timesNo;
	}

	public void setTimesNo(String timesNo) {
		this.timesNo = timesNo;
	}

	public String getHoldDate() {
		return holdDate;
	}

	public void setHoldDate(String holdDate) {
		this.holdDate = holdDate;
	}

	public int getCorrectionCount() {
		return CorrectionCount;
	}

	public void setCorrectionCount(int correctionCount) {
		CorrectionCount = correctionCount;
	}
	
	public String getCorrectionDetail() {
		return CorrectionDetail;
	}

	public void setCorrectionDetail(String correctionDetail) {
		CorrectionDetail = correctionDetail;
	}
	
	public int getItemNo() {
		return itemNo;
	}

	public void setItemNo(int itemNo) {
		this.itemNo = itemNo;
	}

	public byte[] getImage1() {
		return image1;
	}

	public void setImage1(byte[] image1) {
		this.image1 = image1;
	}

	public byte[] getImage2() {
		return image2;
	}

    public void setImage2(byte[] image2) {
        this.image2 = image2;
    }

    public byte[] getImage3() {
        return image3;
    }

    public void setImage3(byte[] image3) {
        this.image3 = image3;
    }

    public byte[] getImage4() {
        return image4;
    }

    public void setImage4(byte[] image4) {
        this.image4 = image4;
    }

    public byte[] getImage5() {
        return image5;
    }

	public void setImage5(byte[] image5) {
		this.image5 = image5;
	}

	public byte[] getImage6() {
		return image6;
	}

	public void setImage6(byte[] image6) {
		this.image6 = image6;
	}

	public byte[] getImage7() {
		return image7;
	}

	public void setImage7(byte[] image7) {
		this.image7 = image7;
	}

	public byte[] getImage8() {
		return image8;
	}

	public void setImage8(byte[] image8) {
		this.image8 = image8;
	}

	public byte[] getImage9() {
		return image9;
	}

	public void setImage9(byte[] image9) {
		this.image9 = image9;
	}

	public byte[] getImage10() {
		return image10;
	}

	public void setImage10(byte[] image10) {
		this.image10 = image10;
	}

	public String getBoxNo() {
		return BoxNo;
	}

	public void setBoxNo(String boxNo) {
		BoxNo = boxNo;
	}


	public String getBoxSubNo() {
		return BoxSubNo;
	}

	public void setBoxSubNo(String boxSubNo) {
		BoxSubNo = boxSubNo;
	}

	public String getRefPrice() {
		return RefPrice;
	}

	public void setRefPrice(String refPrice) {
		RefPrice = refPrice;
	}

	public String getItemName(){
		return ItemName;
	}
	public void setItemName(String itemName){
		this.ItemName = itemName;
	}

	public String getBrandName(){
		return this.BrandName;
	}
	public void setBrandName(String brandName){
		this.BrandName = brandName;
	}

	public String getKindName(){
		return this.KindName;
	}
	public void setKindName(String kindName){
		this.KindName = kindName;
	}

	public String getRank(){
		return this.Rank;
	}
	public void setRank(String state){
		this.Rank = state;
	}

	public String getListPrice(){
		return this.ListPrice;
	}
	public void setListPrice(String listPrice){
		this.ListPrice = listPrice;
	}

	public String getMaterial(){
		return this.Material;
	}
	public void setMaterial(String material){
		this.Material = material;
	}



	public String getResult() {
		return Result;
	}

	public void setResult(String result) {
		Result = result;
	}

	public String getResultName() {
		return ResultName;
	}

	public void setResultName(String resultName) {
		ResultName = resultName;
	}

	public String getStartPrice() {
		return StartPrice;
	}

	public void setStartPrice(String startPrice) {
		StartPrice = startPrice;
	}

	public String getLastPrice() {
		return LastPrice;
	}

	public void setLastPrice(String lastPrice) {
		LastPrice = lastPrice;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getAbsencePrice() {
		return AbsencePrice;
	}

	public void setAbsencePrice(String absencePrice) {
		AbsencePrice = absencePrice;
	}

	public String getBidCount() {
		return BidCount;
	}

	public void setBidCount(String bidCount) {
		BidCount = bidCount;
	}

	public int getAbsenceFlag() {
		return AbsenceFlag;
	}

	public void setAbsenceFlag(int absenceFlag) { AbsenceFlag = absenceFlag; }

	public int getRegistFlag() {
		return RegistFlag;
	}

	public void setRegistFlag(int registFlag) {
		RegistFlag= registFlag;
	}


	public double getRateHKD() {
		return RateHKD;
	}

	public void setRateHKD(double rateHKD) {
		RateHKD = rateHKD;
	}

	public String getSellMemberNo(){
		return SellMemberNo;
	}
	public void setSellMemberNo(String sellMemberNo){
		this.SellMemberNo = sellMemberNo;
	}

	public double getRateDollar() {return this.RateDollar;}
	public void setRateDollar(double rateDollar){this.RateDollar = rateDollar;}

	public double getRateEuro() {return  this.RateEuro;}
	public void setRateEuro(double rateEuro){this.RateEuro = rateEuro;}

	public double getRateYuan() {return  this.RateYuan;}
	public void setRateYuan(double rateYuan) {this.RateYuan = rateYuan;}

	public String getBidDate() {
		return BidDate;
	}

	public void setBidDate(String bidDate) {
		BidDate = bidDate;
	}

	public String getBrand() {
		return Brand;
	}

	public void setBrand(String brand) {
		Brand = brand;
	}

	public String getModel1() {
		return Model1;
	}

	public void setModel1(String model1) {
		Model1 = model1;
	}

	public String getModel2() {
		return Model2;
	}

	public void setModel2(String model2) {
		Model2 = model2;
	}

	public String getSerialNo() {
		return SerialNo;
	}

	public void setSerialNo(String serialNo) {
		SerialNo = serialNo;
	}

	public String getAccessories() {
		return Accessories;
	}

	public void setAccessories(String accessories) {
		Accessories = accessories;
	}

	public String getBless() {
		return Bless;
	}

	public void setBless(String bless) {
		Bless = bless;
	}

	public String getIndividualNo() {
		return IndividualNo;
	}

	public void setIndividualNo(String individualNo) {
		IndividualNo = individualNo;
	}

	public String getColor() {
		return Color;
	}

	public void setColor(String color) {
		Color = color;
	}

	public String getSize() {
		return Size;
	}

	public void setSize(String size) {
		Size = size;
	}

	public String getInstrument() {
		return Instrument;
	}

	public void setInstrument(String instrument) {
		Instrument = instrument;
	}

	public String getDetails() {
		return Details;
	}

	public void setDetails(String details) {
		Details = details;
	}

	public String getComments() {
		return Comments;
	}

	public void setComments(String comments) {
		Comments = comments;
	}

	public String getMemo() {
		return Memo;
	}

	public void setMemo(String memo) {
		Memo = memo;
	}

	public String getSellingSort() {
		return SellingSort;
	}

	public void setSellingSort(String sellingSort) {
		SellingSort = sellingSort;
	}

	public String getAbsenceBidPrice() {
		return AbsenceBidPrice;
	}

	public void setAbsenceBidPrice(String absenceBidPrice) {
		AbsenceBidPrice = absenceBidPrice;
	}

	public String getOtherLaneSellNo() {
		return OtherLaneSellNo;
	}

	public void setOtherLaneSellNo(String otherLaneSellNo) {
		OtherLaneSellNo = otherLaneSellNo;
	}

	public static final String TAG_STATUS = "Status";

	static String Name_Item = "Item";
	static String Name_ItemNo = "ItemNo";
	static String Name_ItemName = "ItemName";
	static String Name_Image1 = "Image1";
	static String Name_Image2 = "Image2";
    static String Name_Image3 = "Image3";
    static String Name_Image4 = "Image4";
    static String Name_Image5 = "Image5";
	static String Name_Image6 = "Image6";
	static String Name_Image7 = "Image7";
	static String Name_Image8 = "Image8";
	static String Name_Image9 = "Image9";
	static String Name_Image10 = "Image10";
	static String Name_BoxNo = "BoxNo";
	static String Name_BoxSubNo = "BoxSubNo";

	static String Name_Result = "Result";
	static String Name_ResultName = "ResultName";
	static String Name_StartPrice = "StartPrice";
	static String Name_LastPrice = "LastPrice";
	static String Name_StartTime = "StartTime";
	static String Name_AbsencePrice = "AbsencePrice";
	static String Name_BidCount = "BidCount";
	static String Name_PageNo = "PageNo";
	static String Name_ResultCount = "ResultCount";
	static String Name_PlaceID = "PlaceID";
	static String Name_PlaceName = "PlaceName";
	static String Name_TimesNo = "TimesNo";
	static String Name_HoldDate = "HoldDate";

	static String Name_CorrectionCount = "CorrectionCount";
	static String Name_CorrectionDetail = "CorrectionDetail";
	static String Name_AbsenceFlag = "AbsenceFlag";
	static String Name_RegistFlag = "RegistFlag";

	static String Name_MemberType = "MemberType";


	static String Name_Rank = "Rank";
	static String Name_ListPrice = "ListPrice";
	static String Name_SellMemberNo = "SellMemberNo";

	static String Name_RateDollar = "RateDoller";
	static String Name_RateEuro = "RateEuro";
	static String Name_RateYuan = "RateYuan";
	static String Name_RateHKD = "RateHKD";
	static String Name_AbsenceBidPrice = "AbsenceBidPrice";



	//private String Rank;

	static String Name_TotalPrice = "TotalPrice";
	static  String Name_AskingPrice = "AskingPrice";
	static  String Name_Mess = "Message";
	static  String Name_OkiniriFlg = "OkiniriFlg";

	static String Name_Brand = "Brand";
	static  String Name_Material = "Material";
	static  String Name_Model1 = "Model1";
	static  String Name_Model2 = "Model2";
	static String Name_SerialNo = "SerialNo";
	static  String Name_Accessories = "Accessories";
	static  String Name_Bless = "Bless";
	static  String Name_Individual = "IndividualNo";
	static String Name_Color = "Color";
	static  String Name_Size = "Size";
	static  String Name_Instrument = "Instrument";
	static  String Name_Details = "Details";
	static String Name_Comments = "Comments";
	static String Name_Memo = "Memo";
	static  String Name_SellingSort = "SellingSort";
	static  String Name_CornerID = "CornerID";
	static  String Name_BidDate = "BidDate";
	static String Name_CornerNm = "CornerName";
	static String Name_Line = "Line";
	static String Name_RefPrice = "ReferencePrice";
	static String Name_SubCat = "MediumCategory";
	static String Name_PlaceList = "PlaceList";
	static String Name_ItemList = "ItemList";
	static String Name_SessionList = "SessionList";
	static String Name_Correction = "Correction";
	static String Name_HoldPrice = "HoldPrice";
	static String Name_SellingResult = "SellingResult";
	static String Name_SellingResultBK = "SellingResultBK";
	static String Name_JishaFlg = "JishaFlg";
	static String Name_HoldFlg = "HoldFlg";
	static String Name_ReholdPrice = "ReHoldPrice";
	static String Name_FinalBidPrice = "FinalBidPrice";
	static String Name_MemoFlg = "MemoFlg";
	static String Name_OtherLane = "OtherLaneSellNo";
	static String Name_LastBidmemberNo = "LastBidMemberNo";

	static String Name_QR = "QR";
	static String Name_QR2 = "QR2";
	static String Name_QR3 = "QR3";
	static String Name_QR4 = "QR4";

	static String Name_Size2 = "Size2";
	static String Name_Size3 = "Size3";
	static String Name_Size4 = "Size4";

	public ItemInfo() {
		super();
	}

	public void appendItemInfo(String data) throws XMLParserException,
			InvalidDataException, ServerErrorException {
		List<NameValuePair> parserResult;

		try {
			parserResult = parse(data);

			for (NameValuePair nvp : parserResult) {
				if (nvp.getName().equalsIgnoreCase(TAG_STATUS)) {
					if (Integer.parseInt(nvp.getValue()) == -1)
						throw new ServerErrorException("Server Error");
					else
						this.setStatus(Integer.parseInt(nvp.getValue()));
				} else if (nvp.getName().equalsIgnoreCase(Name_Image1)) {
					byte[] imageData = Base64.decode(nvp.getValue(),
							Base64.DEFAULT);
					if (imageData != null && imageData.length > 0)
						this.setImage1(imageData);
					else
						this.setImage1(null);
				} else if (nvp.getName().equalsIgnoreCase(Name_Image2)) {
					byte[] imageData = Base64.decode(nvp.getValue(),
							Base64.DEFAULT);
					if (imageData != null && imageData.length > 0)
						this.setImage2(imageData);
					else
						this.setImage2(null);
				}else if (nvp.getName().equalsIgnoreCase(Name_Image3)) {
                    byte[] imageData = Base64.decode(nvp.getValue(),
                            Base64.DEFAULT);
                    if (imageData != null && imageData.length > 0)
                        this.setImage3(imageData);
                    else
                        this.setImage3(null);
                }else if (nvp.getName().equalsIgnoreCase(Name_Image4)) {
                    byte[] imageData = Base64.decode(nvp.getValue(),
                            Base64.DEFAULT);
                    if (imageData != null && imageData.length > 0)
                        this.setImage4(imageData);
                    else
                        this.setImage4(null);
                }else if (nvp.getName().equalsIgnoreCase(Name_Image5)) {
                    byte[] imageData = Base64.decode(nvp.getValue(),
                            Base64.DEFAULT);
                    if (imageData != null && imageData.length > 0)
                        this.setImage5(imageData);
                    else
                        this.setImage5(null);
                }
                else if (nvp.getName().equalsIgnoreCase(Name_Image6)) {
					byte[] imageData = Base64.decode(nvp.getValue(),
							Base64.DEFAULT);
					if (imageData != null && imageData.length > 0)
						this.setImage6(imageData);
					else
						this.setImage6(null);
				}
				else if (nvp.getName().equalsIgnoreCase(Name_Image7)) {
					byte[] imageData = Base64.decode(nvp.getValue(),
							Base64.DEFAULT);
					if (imageData != null && imageData.length > 0)
						this.setImage7(imageData);
					else
						this.setImage7(null);
				}
				else if (nvp.getName().equalsIgnoreCase(Name_Image8)) {
					byte[] imageData = Base64.decode(nvp.getValue(),
							Base64.DEFAULT);
					if (imageData != null && imageData.length > 0)
						this.setImage8(imageData);
					else
						this.setImage8(null);
				}
				else if (nvp.getName().equalsIgnoreCase(Name_Image9)) {
					byte[] imageData = Base64.decode(nvp.getValue(),
							Base64.DEFAULT);
					if (imageData != null && imageData.length > 0)
						this.setImage9(imageData);
					else
						this.setImage9(null);
				}
				else if (nvp.getName().equalsIgnoreCase(Name_Image10)) {
					byte[] imageData = Base64.decode(nvp.getValue(),
							Base64.DEFAULT);
					if (imageData != null && imageData.length > 0)
						this.setImage10(imageData);
					else
						this.setImage10(null);
				}
                else if (nvp.getName().equalsIgnoreCase(Name_BoxNo)) {
					this.setBoxNo(nvp.getValue());
				} else if (nvp.getName().equalsIgnoreCase(Name_BoxSubNo)) {
					this.setBoxSubNo(nvp.getValue());
				}else if (nvp.getName().equalsIgnoreCase(Name_Result)) {
					this.setResult(nvp.getValue());
				} else if (nvp.getName().equalsIgnoreCase(Name_ResultName)) {
					this.setResultName(nvp.getValue());
				} else if (nvp.getName().equalsIgnoreCase(Name_StartPrice)) {
					this.setStartPrice(nvp.getValue());
				} else if (nvp.getName().equalsIgnoreCase(Name_LastPrice)) {
					this.setLastPrice(nvp.getValue());
				} else if (nvp.getName().equalsIgnoreCase(Name_StartTime)) {
					this.setStartTime(nvp.getValue());
				} else if (nvp.getName().equalsIgnoreCase(Name_AbsencePrice)) {
					this.setAbsencePrice(nvp.getValue());
				} else if (nvp.getName().equalsIgnoreCase(Name_BidCount)) {
					this.setBidCount(nvp.getValue());
				}else if (nvp.getName().equalsIgnoreCase(Name_ItemNo)) {
					this.setItemNo(Integer.parseInt(nvp.getValue()));
				} else if (nvp.getName().equalsIgnoreCase(Name_ItemName)) {
					this.setItemName(nvp.getValue());
				} else if (nvp.getName().equalsIgnoreCase(Name_PlaceID)) {
					this.setPlaceId(nvp.getValue());
				} else if (nvp.getName().equalsIgnoreCase(Name_PlaceName)) {
					this.setPlaceName(nvp.getValue());
				} else if (nvp.getName().equalsIgnoreCase(Name_TimesNo)) {
					this.setTimesNo(nvp.getValue());
				} else if (nvp.getName().equalsIgnoreCase(Name_HoldDate)) {
					this.setHoldDate(nvp.getValue());
				} else if (nvp.getName().equalsIgnoreCase(Name_CorrectionCount)) {
					this.setCorrectionCount(Integer.parseInt(nvp.getValue()));
				} else if (nvp.getName().equalsIgnoreCase(Name_CorrectionDetail)) {
					this.setCorrectionDetail(nvp.getValue());
				} else if (nvp.getName().equalsIgnoreCase(Name_AbsenceFlag)) {
					this.setAbsenceFlag(Integer.parseInt(nvp.getValue()));
				} else if (nvp.getName().equalsIgnoreCase(Name_RegistFlag)) {
					this.setRegistFlag(Integer.parseInt(nvp.getValue()));
				}
				else if (nvp.getName().equalsIgnoreCase(Name_MemberType)) {
					this.setMemberType(Integer.parseInt(nvp.getValue()));
				}

				else if (nvp.getName().equalsIgnoreCase(Name_ItemName)) {
					this.setItemName(nvp.getValue());
				}

				else if (nvp.getName().equalsIgnoreCase(Name_Rank)) {
					this.setRank(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_ListPrice)) {
					this.setListPrice(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_Material)) {
					this.setMaterial(nvp.getValue());
				}

				else if (nvp.getName().equalsIgnoreCase(Name_SellMemberNo)) {
					this.setSellMemberNo(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_RateDollar)) {
					this.setRateDollar(Double.parseDouble( nvp.getValue()));
				}
				else if (nvp.getName().equalsIgnoreCase(Name_RateEuro)) {
					this.setRateEuro(Double.parseDouble( nvp.getValue()));
				}
				else if (nvp.getName().equalsIgnoreCase(Name_RateYuan)) {
					this.setRateYuan(Double.parseDouble( nvp.getValue()));
				}
				else if (nvp.getName().equalsIgnoreCase(Name_RateHKD)) {
					this.setRateHKD(Double.parseDouble( nvp.getValue()));
				}

				else if (nvp.getName().equalsIgnoreCase(Name_AskingPrice)) {
					this.setAskingPrice(nvp.getValue());
				}

				else if (nvp.getName().equalsIgnoreCase(Name_OkiniriFlg)) {
					this.setOkiniriFlg(nvp.getValue());
				}

				else if (nvp.getName().equalsIgnoreCase(Name_CornerID)) {
					this.setCornerID(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_BidDate)) {
					this.setBidDate(nvp.getValue());
				}else if (nvp.getName().equalsIgnoreCase(Name_Brand)) {
					this.setBrand(nvp.getValue());
				}else if (nvp.getName().equalsIgnoreCase(Name_Model1)) {
					this.setModel1(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_Model2)) {
					this.setModel2(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_SerialNo)) {
					this.setSerialNo(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_Accessories)) {
					this.setAccessories(nvp.getValue());
				}else if (nvp.getName().equalsIgnoreCase(Name_Bless)) {
					this.setBless(nvp.getValue());
				}else if (nvp.getName().equalsIgnoreCase(Name_Individual)) {
					this.setIndividualNo(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_Color)) {
					this.setColor(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_Size)) {
					this.setSize(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_Instrument)) {
					this.setInstrument(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_Details)) {
					this.setDetails(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_Comments)) {
					this.setComments(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_Memo)) {
					this.setMemo(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_SellingSort)) {
					this.setSellingSort(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_AbsenceBidPrice)) {
					this.setAbsenceBidPrice(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_CornerNm)) {
					this.setCornerNm(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_Line)) {
					this.setLine(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_RefPrice)) {
					this.setRefPrice(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_SubCat)) {
					this.setSubCat(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_Correction)) {
					this.setCorrection(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_HoldPrice)) {
					this.setHoldPrice(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_SellingResult)) {
					this.setSellingResult(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_SellingResultBK)) {
					this.setSellingResultBK(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_JishaFlg)) {
					this.setJishaFlg(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_ReholdPrice)) {
					this.setReholdPrice(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_MemoFlg)) {
					this.setMemoFlg(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_FinalBidPrice)) {
					this.setFinalBidPrice(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_HoldFlg)) {
					this.setHoldFlg(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_OtherLane)) {
					this.setOtherLaneSellNo(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_LastBidmemberNo)) {
					this.setLastBidMemberNo(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_QR)) {
					this.setQR(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_QR2)) {
					this.setQR2(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_QR3)) {
					this.setQR3(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_QR4)) {
					this.setQR4(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_Size2)) {
					this.setSize2(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_Size3)) {
					this.setSize3(nvp.getValue());
				}
				else if (nvp.getName().equalsIgnoreCase(Name_Size4)) {
					this.setSize4(nvp.getValue());
				}
			}
		} catch (XMLParserException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (ServerErrorException ex) {
			throw ex;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InvalidDataException(ex.getMessage());
		}

	}
	
	public ItemInfo(List<NameValuePair> parserResult) {
		for (NameValuePair nvp : parserResult) {
			if (nvp.getName().equalsIgnoreCase(TAG_STATUS)) {
				this.setStatus(Integer.parseInt(nvp.getValue()));
			} else if (nvp.getName().equalsIgnoreCase(Name_Image1)) {
				this.setImage1(this.getImageValue(nvp.getValue()));
			} else if (nvp.getName().equalsIgnoreCase(Name_Image2)) {
				this.setImage2(this.getImageValue(nvp.getValue()));
			} else if (nvp.getName().equalsIgnoreCase(Name_Image3)) {
				this.setImage3(this.getImageValue(nvp.getValue()));
			} else if (nvp.getName().equalsIgnoreCase(Name_Image4)) {
				this.setImage4(this.getImageValue(nvp.getValue()));
			} else if (nvp.getName().equalsIgnoreCase(Name_Image5)) {
				this.setImage5(this.getImageValue(nvp.getValue()));
			} else if (nvp.getName().equalsIgnoreCase(Name_Image6)) {
				this.setImage6(this.getImageValue(nvp.getValue()));
			}else if (nvp.getName().equalsIgnoreCase(Name_Image7)) {
				this.setImage7(this.getImageValue(nvp.getValue()));
			}
			else if (nvp.getName().equalsIgnoreCase(Name_Image8)) {
				this.setImage8(this.getImageValue(nvp.getValue()));
			}
			else if (nvp.getName().equalsIgnoreCase(Name_Image9)) {
				this.setImage9(this.getImageValue(nvp.getValue()));
			}
			else if (nvp.getName().equalsIgnoreCase(Name_Image10)) {
				this.setImage10(this.getImageValue(nvp.getValue()));
			}
			else if (nvp.getName().equalsIgnoreCase(Name_BoxNo)) {
				this.setBoxNo(nvp.getValue());
			} else if (nvp.getName().equalsIgnoreCase(Name_BoxSubNo)) {
				this.setBoxSubNo(nvp.getValue());
			}else if (nvp.getName().equalsIgnoreCase(Name_Result)) {
				this.setResult(nvp.getValue());
			} else if (nvp.getName().equalsIgnoreCase(Name_ResultName)) {
				this.setResultName(nvp.getValue());
			} else if (nvp.getName().equalsIgnoreCase(Name_StartPrice)) {
				this.setStartPrice(nvp.getValue());
			} else if (nvp.getName().equalsIgnoreCase(Name_LastPrice)) {
				this.setLastPrice(nvp.getValue());
			} else if (nvp.getName().equalsIgnoreCase(Name_StartTime)) {
				this.setStartTime(nvp.getValue());
			} else if (nvp.getName().equalsIgnoreCase(Name_AbsencePrice)) {
				this.setAbsencePrice(nvp.getValue());
			} else if (nvp.getName().equalsIgnoreCase(Name_BidCount)) {
				this.setBidCount(nvp.getValue());
			} else if (nvp.getName().equalsIgnoreCase(Name_ItemNo)) {
				this.setItemNo(Integer.parseInt(nvp.getValue()));
			} else if (nvp.getName().equalsIgnoreCase(Name_ItemName)) {
				this.setItemName(nvp.getValue());
			} else if (nvp.getName().equalsIgnoreCase(Name_PlaceID)) {
				this.setPlaceId(nvp.getValue());
			} else if (nvp.getName().equalsIgnoreCase(Name_PlaceName)) {
				this.setPlaceName(nvp.getValue());
			} else if (nvp.getName().equalsIgnoreCase(Name_TimesNo)) {
				this.setTimesNo(nvp.getValue());
			} else if (nvp.getName().equalsIgnoreCase(Name_HoldDate)) {
				this.setHoldDate(nvp.getValue());
			} else if (nvp.getName().equalsIgnoreCase(Name_CorrectionCount)) {
				this.setCorrectionCount(Integer.parseInt(nvp.getValue()));
			} else if (nvp.getName().equalsIgnoreCase(Name_CorrectionDetail)) {
				this.setCorrectionCount(Integer.parseInt(nvp.getValue()));
			} else if (nvp.getName().equalsIgnoreCase(Name_AbsenceFlag)) {
				this.setAbsenceFlag(Integer.parseInt(nvp.getValue()));
			} else if (nvp.getName().equalsIgnoreCase(Name_RegistFlag)) {
				this.setRegistFlag(Integer.parseInt(nvp.getValue()));
			}
			else if (nvp.getName().equalsIgnoreCase(Name_MemberType)) {
				this.setMemberType(Integer.parseInt(nvp.getValue()));
			}
			else if (nvp.getName().equalsIgnoreCase(Name_ItemName)) {
				this.setItemName(nvp.getValue());
			}

			else if (nvp.getName().equalsIgnoreCase(Name_Rank)) {
				this.setRank(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_ListPrice)) {
				this.setListPrice(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_Material)) {
				this.setMaterial(nvp.getValue());
			}

			else if (nvp.getName().equalsIgnoreCase(Name_SellMemberNo)) {
				this.setSellMemberNo(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_RateDollar)) {
				this.setRateDollar(Double.parseDouble( nvp.getValue()));
			}
			else if (nvp.getName().equalsIgnoreCase(Name_RateEuro)) {
				this.setRateEuro(Double.parseDouble( nvp.getValue()));
			}
			else if (nvp.getName().equalsIgnoreCase(Name_RateYuan)) {
				this.setRateYuan(Double.parseDouble( nvp.getValue()));
			}
			else if (nvp.getName().equalsIgnoreCase(Name_RateHKD)) {
				this.setRateHKD(Double.parseDouble( nvp.getValue()));
			}

			else if (nvp.getName().equalsIgnoreCase(Name_AskingPrice)) {
				this.setAskingPrice(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_OkiniriFlg)) {
				this.setOkiniriFlg(nvp.getValue());
			}else if (nvp.getName().equalsIgnoreCase(Name_CornerID)) {
				this.setCornerID(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_BidDate)) {
				this.setBidDate(nvp.getValue());
			}else if (nvp.getName().equalsIgnoreCase(Name_Brand)) {
				this.setBrand(nvp.getValue());
			}else if (nvp.getName().equalsIgnoreCase(Name_Model1)) {
				this.setModel1(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_Model2)) {
				this.setModel2(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_SerialNo)) {
				this.setSerialNo(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_Accessories)) {
				this.setAccessories(nvp.getValue());
			}else if (nvp.getName().equalsIgnoreCase(Name_Bless)) {
				this.setBless(nvp.getValue());
			}else if (nvp.getName().equalsIgnoreCase(Name_Individual)) {
				this.setIndividualNo(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_Color)) {
				this.setColor(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_Size)) {
				this.setSize(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_Instrument)) {
				this.setInstrument(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_Details)) {
				this.setDetails(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_Comments)) {
				this.setComments(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_Memo)) {
				this.setMemo(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_SellingSort)) {
				this.setSellingSort(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_AbsenceBidPrice)) {
				this.setAbsenceBidPrice(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_SubCat)) {
				this.setSubCat(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_Correction)) {
				this.setCorrection(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_HoldPrice)) {
				this.setHoldPrice(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_SellingResult)) {
				this.setSellingResult(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_SellingResultBK)) {
				this.setSellingResultBK(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_JishaFlg)) {
				this.setJishaFlg(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_ReholdPrice)) {
				this.setReholdPrice(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_MemoFlg)) {
				this.setMemoFlg(nvp.getValue());
			}else if (nvp.getName().equalsIgnoreCase(Name_FinalBidPrice)) {
				this.setFinalBidPrice(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_HoldFlg)) {
				this.setHoldFlg(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_OtherLane)) {
				this.setOtherLaneSellNo(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_LastBidmemberNo)) {
				this.setLastBidMemberNo(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_QR)) {
				this.setQR(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_QR2)) {
				this.setQR2(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_QR3)) {
				this.setQR3(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_QR4)) {
				this.setQR4(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_Size2)) {
				this.setSize2(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_Size3)) {
				this.setSize3(nvp.getValue());
			}
			else if (nvp.getName().equalsIgnoreCase(Name_Size4)) {
				this.setSize4(nvp.getValue());
			}
		}
	}
	
	public static List<ItemInfo> getList(String xmlData) throws XMLParserException,
			InvalidDataException, ServerErrorException {

		List<ItemInfo> itemInfoList = new ArrayList<ItemInfo>();
		Message = "";
		String tagName = null;
		String tagValue = null;

		List<NameValuePair> parserResult = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();

			xpp.setInput(new StringReader(xmlData));

			int eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					tagName = xpp.getName();
					if (tagName.equalsIgnoreCase(Name_Item)) {
						parserResult = new ArrayList<NameValuePair>();
					}
				} else if (eventType == XmlPullParser.TEXT) {
					tagValue = xpp.getText();
				} else if (eventType == XmlPullParser.END_TAG) {
					if (xpp.getName().equalsIgnoreCase(TAG_STATUS)) {
						if (Integer.parseInt(tagValue) == -1)
							throw new ServerErrorException("Server Error");
					} else if (xpp.getName().equalsIgnoreCase(Name_Item)) {
						ItemInfo obj = new ItemInfo(parserResult);
						itemInfoList.add(obj);
					} else if (xpp.getName().equalsIgnoreCase(Name_PageNo)) {
						PageNo = Integer.parseInt(tagValue);
					} else if (xpp.getName().equalsIgnoreCase(Name_ResultCount)) {
						RecordCount = Integer.parseInt(tagValue);
					}else if (xpp.getName().equalsIgnoreCase(Name_TotalPrice)) {
						TotalPrice = Integer.parseInt(tagValue);
					}else if (xpp.getName().equalsIgnoreCase(Name_Mess)) {
						Message = tagValue;
					}else if (xpp.getName().equalsIgnoreCase(Name_PlaceList)) {
						PlaceLst = tagValue;
					}
					else if (xpp.getName().equalsIgnoreCase(Name_ItemList)) {
						ItemLst = tagValue;
					}
					else if (xpp.getName().equalsIgnoreCase(Name_SessionList)) {
						SessionLst = tagValue;
					}
					else {
						if (parserResult != null && (tagName != null) && (tagValue != null)) {
							parserResult.add(new BasicNameValuePair(tagName,
									tagValue));
						}
					}
					tagName = null;
					tagValue = null;
				}
				eventType = xpp.next();
			}

		} catch (XmlPullParserException ex) {
			ex.printStackTrace();
			throw new XMLParserException(ex.getMessage());
		} catch (ServerErrorException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new XMLParserException(ex.getMessage());
		}
		return itemInfoList;
	}

	private byte[] getImageValue(String data) {
		byte[] imageData = Base64.decode(data, Base64.NO_PADDING);

		return (imageData != null && imageData.length > 0) ? imageData : null;
	}
}