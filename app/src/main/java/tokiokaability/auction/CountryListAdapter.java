package tokiokaability.auction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class CountryListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> lstCountryHeader;
    private HashMap<String,List<String>> lstCountryDetail;
    public CountryListAdapter(Context context, List<String> lstCountryHeader, LinkedHashMap<String,List<String>> lstCountryDetail){
        this.context = context;
        this.lstCountryHeader = lstCountryHeader;
        this.lstCountryDetail = lstCountryDetail;
    }
    @Override
    public int getGroupCount() {
        return this.lstCountryHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.lstCountryDetail.get(this.lstCountryHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.lstCountryHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.lstCountryDetail.get(this.lstCountryHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String lstHeader = (String) getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lst_country_header,null);
        }
        TextView txtHeader = convertView.findViewById(R.id.listTitle);
        txtHeader.setText(lstHeader);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String text = (String)getChild(groupPosition,childPosition);

        if(convertView == null){
            LayoutInflater inflater =   (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lst_country_item,null);
        }
        TextView itemTextview = (TextView)convertView.findViewById(R.id.expandedListItem);
        itemTextview.setText(text);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
