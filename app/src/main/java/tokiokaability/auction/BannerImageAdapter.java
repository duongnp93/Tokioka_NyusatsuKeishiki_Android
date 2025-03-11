package tokiokaability.auction;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.apache.commons.codec.binary.Hex;

import java.util.ArrayList;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class BannerImageAdapter  extends PagerAdapter {
    private ArrayList<ImageModel> imageModelArrayList;
    private LayoutInflater inflater;
    private Context context;


    public BannerImageAdapter(Context context, ArrayList<ImageModel> imageModelArrayList) {
        this.context = context;
        this.imageModelArrayList = imageModelArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }


    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);


        imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageModelArrayList.get(position).getByteArr(),0,imageModelArrayList.get(position).getByteArr().length));

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


    public static class ImageModel {

        private String bannerUrl;
        private byte[] byteArr;

        public ImageModel(){

        }

        public byte[] getByteArr() {
            return byteArr;
        }

        public String getBannerUrl() {
            return bannerUrl;
        }
        public String encode(String key, String data) {
            String result = "";
            try{
                Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
                SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
                sha256_HMAC.init(secret_key);
                byte[] finalByte = sha256_HMAC.doFinal(data.getBytes("UTF-8"));

                result = new String(Hex.encodeHex(finalByte));
            }
            catch (Exception ex){
                result = "";
            }
            return  result;
        }
        public void setBannerUrl(String bannerUrl) {
            if(bannerUrl.contains("?loginId=")){
                bannerUrl = bannerUrl+encode("TokiokaAbility",RealLuceAuctionApplication.loginID);
            }
            this.bannerUrl = bannerUrl;
        }

        public void setByteArr(byte[] byteArr) {
            this.byteArr = byteArr;
        }
    }
}


