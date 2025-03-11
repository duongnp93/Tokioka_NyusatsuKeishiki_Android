package tokiokaability.auction.commons;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

import tokiokaability.auction.R;

public class CustomImageAdapter extends PagerAdapter {
    private ArrayList<ImageModel> imageModelArrayList;
    private LayoutInflater inflater;
    private Context context;


    public CustomImageAdapter(Context c, ArrayList<ImageModel> imageModelArrayList){
        this.context = c;
        this.imageModelArrayList = imageModelArrayList;
        inflater = LayoutInflater.from(c);
    }

    public void setImageModelArrayList(ArrayList<ImageModel> lst){
        this.imageModelArrayList = lst;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
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
        private byte[] byteArr;

        public ImageModel(){

        }
        public byte[] getByteArr() {
            return byteArr;
        }

        public void setByteArr(byte[] byteArr) {
            this.byteArr = byteArr;
        }
    }
}
