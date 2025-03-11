package tokiokaability.auction.commons;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import androidx.exifinterface.media.ExifInterface;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import tokiokaability.auction.R;
import tokiokaability.auction.RealLuceAuctionApplication;

public class Utilities {
	public static float convertPixelsToDp(float px,Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    return dp;

	}
	public static float convertDpToPixel(float dp,Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi / 160f);
	    return px;

	}
	public static float convertDpToDPI(float dp,Context context){
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return (dp * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;

	}

	public static String formatPrice(Context context, int price){
		return String.valueOf(String.format(context.getString(R.string.successfull_bit_price_value),price));
	}

	public static Bitmap rotateImage(Bitmap source, float angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
				matrix, true);
	}

	public static byte[] rotaledByteArr(byte[] input){
		byte[] res = null;
		try{
			ByteArrayInputStream bis = new ByteArrayInputStream(input);
			ExifInterface exif = new ExifInterface(bis);
			int orientaion = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
			Bitmap b = BitmapFactory.decodeByteArray(input,0,input.length);
			switch (orientaion){
				case ExifInterface.ORIENTATION_ROTATE_90:
					b = rotateImage(b,90);
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					b = rotateImage(b,180);
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					b = rotateImage(b,270);
					break;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			b.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			res = baos.toByteArray();
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		return res;
	}

	public static String formatPrice(Context context, String price){
		if (price == null || price.length() <= 0)
			return "";

		Long tmp = null;
		try {
			tmp = Long.parseLong(price);
		}
		catch(Exception ex){

		}
		if (tmp == null)
			return (price+" 円");
		else
			return String.valueOf(String.format(context.getString(R.string.successfull_bit_price_value),tmp));
	}

	public static  void writeLog(Context context,String filename, String content){
		File file = new File(context.getFilesDir(),filename);
		if(!file.isFile()){
			try{
				file.createNewFile();
			}
			catch (Exception ex){
				ex.printStackTrace();
			}
		}

		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = df.format(Calendar.getInstance().getTime());
			bw.write(dateString+" WatchAuction : "+content);
			bw.newLine();
			bw.close();
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		float file_size = file.length()/1024;
		if(file_size > 1000){
			String newContent = readLog(context,filename);
			file.delete();
			try{
				file.createNewFile();
				BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
				bw.write(newContent);
				bw.newLine();
				bw.close();
			}
			catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}

	public static String readLog(Context context, String filename){
		StringBuilder sb = new StringBuilder();
		List<String> tmp = new ArrayList<String>();
		int cap = 0;
		if(filename.equals(RealLuceAuctionApplication.getConfiguration("info_log_file"))){
			cap = 300;
		}
		else cap = 100;
		sb.append("");
		File file = new File(context.getFilesDir(),filename);
		if(!file.exists()) return sb.toString();
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while((line = br.readLine())!= null){
				tmp.add(line);
			}
			br.close();
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		if(tmp.size() > cap){
			for(int i=tmp.size() -1;i >= tmp.size() - cap;i--){
				sb.append(tmp.get(i));
				sb.append("\r\n");
			}
		}
		else{
			for(int i=tmp.size() -1;i>=0;i--){
				sb.append(tmp.get(i));
				sb.append("\r\n");
			}
		}
		return sb.toString();
	}

	public static String getPublicIPAddress(Context context) {
		//final NetworkInfo info = NetworkUtils.getNetworkInfo(context);

		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo info = cm.getActiveNetworkInfo();

		RunnableFuture<String> futureRun = new FutureTask(new Callable<String>() {
			@Override
			public String call() throws Exception {
				if ((info != null && info.isAvailable()) && (info.isConnected())) {
					StringBuilder response = new StringBuilder();

					try {
						HttpURLConnection urlConnection = (HttpURLConnection) (
								new URL("http://checkip.amazonaws.com/").openConnection());
						urlConnection.setRequestProperty("User-Agent", "Android-device");
						//urlConnection.setRequestProperty("Connection", "close");
						urlConnection.setReadTimeout(15000);
						urlConnection.setConnectTimeout(15000);
						urlConnection.setRequestMethod("GET");
						urlConnection.setRequestProperty("Content-type", "application/json");
						urlConnection.connect();

						int responseCode = urlConnection.getResponseCode();

						if (responseCode == HttpURLConnection.HTTP_OK) {

							InputStream in = new BufferedInputStream(urlConnection.getInputStream());
							BufferedReader reader = new BufferedReader(new InputStreamReader(in));

							String line;
							while ((line = reader.readLine()) != null) {
								response.append(line);
							}

						}
						urlConnection.disconnect();
						return response.toString();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					//Log.w(TAG, "No network available INTERNET OFF!");
					return null;
				}
				return null;
			}
		});

		new Thread(futureRun).start();

		try {
			return futureRun.get();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}
