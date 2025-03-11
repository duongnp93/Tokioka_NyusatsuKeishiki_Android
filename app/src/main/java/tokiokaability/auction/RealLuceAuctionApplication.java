package tokiokaability.auction;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import tokiokaability.auction.HTTPInfo.PlaceInfo;
import tokiokaability.auction.HTTPInfo.SystemInfo;
import tokiokaability.auction.commons.Utilities;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.messaging.FirebaseMessaging;

public class RealLuceAuctionApplication extends Application {
	private static Context context;
	static Properties properties;
	public static SystemInfo systemInfo;

	/* Keep Login Info */
	public static String loginID = null; 	/* LoginID */
	public static String password = null;
	public static String memberType = null;
	public static byte[] posterImage = null;
	public static String posterURL = null;
	public static String announceInfo = null;
	public static ArrayList<PlaceInfo> allPlaces = null;
	public static String baseURL = "";
	public static String defaultBaseURL;
	public static ArrayList<String> backupURLs = null;
	public static boolean isConneted;
	public static boolean isDefaultURL;
	public static String publicIPAddress;
	public static String fireBaseToken;
	public static  boolean isTopAgree;

	//country code
	public static  String countryName = null;
	public static  String countryCode = null;
	public static  String memberName = "";
	/*
	 * Configuration key
	 */
	public static final String SETTINGS = "RealLuceAuctionSettings";
	public static final String SETTINGS_SHOW_PRICE = "show_price";
	public static final String SETTINGS_MAX_ROW_COUNT = "max_row_count";
	public static final String SETTINGS_VIEW_LANGUAGE = "view_language";
	public static  String currentItemOkiniri;
	public static ArrayList<String> okiniriLst;
	public static ArrayList<Integer> okiniriOffsetLst;
	public static ArrayList<String> fuzaiLst;
	public static ArrayList<Integer> fuzaiOffsetLst;
	public static int detailOffset;
	public static String member_no;
	public static String lane_id;
	public static boolean isUpdatedKosho;
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
    public void onCreate(){
    	
        super.onCreate();
        try {
        	SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS , MODE_PRIVATE);
			FirebaseMessaging.getInstance();
        	RealLuceAuctionApplication.context = getApplicationContext();
			RealLuceAuctionApplication.loadConfigurations();
			
			//baseURL = settings.getString("workingGateway", "");
			if(baseURL == ""){
				baseURL = RealLuceAuctionApplication.getConfiguration("service_url");
	        	//SharedPreferences.Editor editor = settings.edit();
				//editor.putString("workingGateway", baseURL);
				//editor.commit();
			}
			
			defaultBaseURL = baseURL;
			isDefaultURL = true;
			backupURLs = new ArrayList<String>();
			backupURLs.add(RealLuceAuctionApplication.getConfiguration("service_url_backup"));
			publicIPAddress = Utilities.getPublicIPAddress(getApplicationContext());
			isConneted = true;
			okiniriLst = new ArrayList<>();
			okiniriOffsetLst = new ArrayList<>();
			fuzaiLst = new ArrayList<>();
			fuzaiOffsetLst = new ArrayList<>();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }

    public static Context getAppContext() {
        return RealLuceAuctionApplication.context;
    }

	/*
	 * Load configurations from configuration from file (app.properties)
	 */
	private static void loadConfigurations() throws Exception{
		try{
			if (properties == null ){
				InputStream is =  RealLuceAuctionApplication.context.getAssets().open("app.properties");//Configurations.class.getResourceAsStream("");
				if (is != null){
					properties = new Properties();
					properties.load(is);
					is.close();
				}
				else
					throw new IOException();
				//dbConnStr = props.getProperty("dbUrl");
			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
	}
    
	
	/*
	 * get String value from app.properties
	 */
    public static String getConfiguration(String key){
		try{
			loadConfigurations();
			return properties.getProperty(key);
		}
		catch(Exception ex){
			ex.printStackTrace();
			return "";
		}
    }
    
    
	/*
	 * get Int value from app.properties
	 */
    public static int getIntConfiguration(String key){
		try{
			loadConfigurations();
			return Integer.parseInt(properties.getProperty(key));
		}
		catch(Exception ex){
			ex.printStackTrace();
			return 0;
		}

    }
    
    /*
     * clear session data.
     */
	public static void clearData(){
		RealLuceAuctionApplication.loginID = null;
	}
    
	


	
}
