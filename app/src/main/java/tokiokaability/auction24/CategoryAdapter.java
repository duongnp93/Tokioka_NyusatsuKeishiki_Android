package tokiokaability.auction24;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryAdapter extends BaseAdapter {
    ArrayList<String> lstCat;
    Context mContext;
    AssetManager am;
    Typeface yGR;
    public ArrayList<String> getLstCat() {
        return lstCat;
    }

    public void setLstCat(ArrayList<String> lstCat) {
        this.lstCat = lstCat;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
        am = mContext.getAssets();
        yGR = Typeface.createFromAsset(am,"fonts/YUGOTHR.TTC");
    }

    @Override
    public int getCount() {
        return lstCat.size();
    }

    @Override
    public Object getItem(int position) {
        return lstCat.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CatHolder cHolder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.lst_item_blacktext,parent,false);
            cHolder = new CatHolder();
            cHolder.contentTxt = convertView.findViewById(R.id.contentTxt);
            convertView.setTag(cHolder);
        }
        else{
            cHolder = (CatHolder) convertView.getTag();
        }

        cHolder.contentTxt.setTypeface(yGR);
        cHolder.contentTxt.setText(lstCat.get(position));
        return convertView;
    }

    class CatHolder{
        TextView contentTxt;
    }
}
