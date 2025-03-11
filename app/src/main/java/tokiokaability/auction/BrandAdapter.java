package tokiokaability.auction;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.String.join;


public class BrandAdapter extends BaseAdapter {
    ArrayList<String> lstValue;
    ArrayList<String> lstSession;
    ArrayList<String> lstCheck;
    Context mContext;
    AssetManager am;
    Typeface yGR;

    public ArrayList<String> getLstValue() {
        return lstValue;
    }

    public void setLstValue(ArrayList<String> lstValue) {
        this.lstValue = lstValue;
    }

    public ArrayList<String> getLstSession() {
        return lstSession;
    }

    public void setLstSession(ArrayList<String> lstSession) {
        this.lstSession = lstSession;
    }

    public ArrayList<String> getLstCheck() {
        return lstCheck;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
        am = mContext.getAssets();
        yGR = Typeface.createFromAsset(am,"fonts/YUGOTHR.TTC");
    }

    public void resetChkList(){
        lstCheck = new ArrayList<>();
        for(int i = 0;i<lstValue.size();i++){
            lstCheck.add("0");
        }
    }

    public void updateChkList(ArrayList<String> lstToUpdate){
        resetChkList();
        for (String s:lstToUpdate
             ) {
            int index = lstValue.indexOf(s);
            lstCheck.set(index,"1");
        }
    }


    public void updateChkList(String str,String value){
        int index = lstValue.indexOf(str);
        lstCheck.set(index,value);
    }

    public String getCurrentValue(){
        String result = "";

        for(int i=0;i<lstValue.size();i++){
            if(lstCheck.get(i).equals("1")){
                if(result.length() == 0)
                    result += lstValue.get(i);
                else
                    result=result+","+lstValue.get(i);
            }
        }
        return result;
    }

    public String getUpdateableValue(){
        String result = "";

        for(int i=0;i<lstValue.size();i++){
            if(lstCheck.get(i).equals("1")){
                if(result.length() == 0)
                    result += "'"+lstValue.get(i)+"'";
                else
                    result=result+",'"+lstValue.get(i)+"'";
            }
        }
        return result;
    }
    public String getUpdateableSpaceValue(){
        String result = "";

        for(int i=0;i<lstValue.size();i++){
            if(lstCheck.get(i).equals("1")){
                if(result.length() == 0)
                    result += "'"+lstValue.get(i)+"'";
                else
                    result=result+", '"+lstValue.get(i)+"'";
            }
        }
        return result;
    }

    public String getUpdateableSessionValue(){
        String result = "";

        for(int i=0;i<lstSession.size();i++){
            if(lstCheck.get(i).equals("1")){
                if(result.length() == 0)
                    result += "'"+lstSession.get(i)+"'";
                else
                    result=result+",'"+lstSession.get(i)+"'";
            }
        }
        return result;
    }

    @Override
    public int getCount() {
        return lstValue.size();
    }

    @Override
    public Object getItem(int position) {
        return lstValue.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BrandViewHolder bHolder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.lst_item_checkbox,parent,false);
            bHolder = new BrandViewHolder();
            bHolder.valueTxt = convertView.findViewById(R.id.valueTxt);
            bHolder.itemCB = convertView.findViewById(R.id.itemCB);
            convertView.setTag(bHolder);
        }
        else{
            bHolder = (BrandViewHolder) convertView.getTag();
        }
        bHolder.valueTxt.setTypeface(yGR);
        bHolder.valueTxt.setText(lstValue.get(position));
        if(lstCheck.get(position).equals("1")){
            bHolder.itemCB.setImageResource(R.drawable.mn_search_check);
        }
        else{
            bHolder.itemCB.setImageResource(R.drawable.mn_search_un);
        }
        return convertView;
    }

    class BrandViewHolder{
        TextView valueTxt;
        ImageView itemCB;
    }
}
