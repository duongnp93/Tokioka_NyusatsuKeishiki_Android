package tokiokaability.auction.HTTPInfo;

import java.util.List;

import org.apache.http.NameValuePair;


public class LoginInfo extends BaseInfo {

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMemberType() {
		return memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	private String username = "";
	private String password = "";
	private String result = "";
	private String memberType = "";
	private String memberName = "";
	private String lanFlg = "";

	public String getLanFlg() {
		return lanFlg;
	}

	public void setLanFlg(String lanFlg) {
		this.lanFlg = lanFlg;
	}

	public String getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}

	public String getNewbie() {
		return newbie;
	}

	public void setNewbie(String newbie) {
		this.newbie = newbie;
	}

	private String memberNo = "";
	private String newbie = "";
	String douiFlg = "";

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDouiFlg() {
		return douiFlg;
	}

	public void setDouiFlg(String douiFlg) {
		this.douiFlg = douiFlg;
	}

	private String code = "";
	
	public static final String TAG_STATUS = "Status";
	public static final String TAG_RESULT = "Result";
	public static final String TAG_MEMBER_TYPE = "MemberType";
	public static final String TAG_MEMBER_NO = "MemberNo";
	public static final String TAG_NEWBIE = "Newbie";
	public static  final String TAG_CODE = "Code";
	public static  final String TAG_MEMBER_NM = "MemberName";
	public static  final String TAG_DOUIFLG = "DouiFlg";
	public static  final String TAG_LANFLG = "LanguageFlg";
	
	public LoginInfo(String data) throws XMLParserException,InvalidDataException, ServerErrorException{
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
				else if(nvp.getName().equalsIgnoreCase(TAG_MEMBER_TYPE)){
					this.setMemberType(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(TAG_MEMBER_NO)){
					this.setMemberNo(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(TAG_NEWBIE)){
					this.setNewbie(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(TAG_CODE)){
					this.setCode(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(TAG_MEMBER_NM)){
					this.setMemberName(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(TAG_DOUIFLG)){
					this.setDouiFlg(nvp.getValue());
				}
				else if(nvp.getName().equalsIgnoreCase(TAG_LANFLG)){
					this.setLanFlg(nvp.getValue());
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
