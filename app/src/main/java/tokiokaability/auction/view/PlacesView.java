package tokiokaability.auction.view;

import java.util.ArrayList;

import tokiokaability.auction.R;
import tokiokaability.auction.HTTPInfo.PlaceInfo;
import tokiokaability.auction.commons.Utilities;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class PlacesView extends RelativeLayout{
	private ImageButton btnPrev = null;
	private ImageButton btnNext = null;
	private ContainerScroll scroller = null;
	private LinearLayout scrollContent = null;
	private ArrayList<PlaceInfo> places = null;
	private ArrayList<Button> buttonList = null;
	private int selectedIndex = -1;
	private OnPlaceSelectedListener onPlaceSelectedListener = null;


	public PlacesView(Context context) {
		super(context);
		initView(context);
	}

	public PlacesView(Context context, AttributeSet attrs) {
		super(context,attrs);
		initView(context);
	}
	public PlacesView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
		
	}

	
	
	
	
	private void initView(Context context){
		
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.view_places,this);
		 
		
		
		btnPrev = (ImageButton) findViewById(R.id.btnPrev);
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		scroller = (ContainerScroll) findViewById(R.id.scroller);
		scrollContent = (LinearLayout) findViewById(R.id.scrollContent);
		scroller.setHorizontalScrollBarEnabled(false);
		scroller.setBtnNext(btnNext);
		scroller.setBtnPrev(btnPrev);
	}
	
	
	public ArrayList<PlaceInfo> getPlaces() {
		return places;
	}

	public void setPlaces(ArrayList<PlaceInfo> places) {
		this.places = places;
		buttonList = new ArrayList<Button>();
		
		scrollContent.removeAllViews();
		
		for(int i=0;i<places.size();i++){
			Button btn = new Button(getContext());

	        btn.setText(places.get(i).getName());
	        btn.setTag(i);
	        btn.setTextColor(Color.BLACK);
            btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
            btn.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
            btn.setPadding((int)Utilities.convertDpToPixel(10, this.getContext()), 0, (int) Utilities.convertDpToPixel(10, this.getContext()), 0);
	        btn.setBackgroundResource (R.drawable.standard_button);
	        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
	        lp.leftMargin = 1;
	        lp.rightMargin = 0;
	        btn.setLayoutParams(lp);
	        btn.setOnClickListener(btnPlaceClick);
	        scrollContent.addView(btn);
            //btn.setWidth(400);
	        buttonList.add(btn);

			
			
		}
		setSelectedIndex(getSelectedIndex());

	}





    public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		if (this.selectedIndex >= 0 && this.selectedIndex < places.size()){
			//buttonList.get(this.selectedIndex).setBackgroundResource(R.drawable.no_round_corner_style);
            buttonList.get(this.selectedIndex).setBackgroundResource(R.drawable.standard_button);
            buttonList.get(this.selectedIndex).setTextColor(Color.BLACK);
		}
		
		this.selectedIndex = selectedIndex;
		
		if (this.selectedIndex >= 0 && this.selectedIndex < places.size()){
			//buttonList.get(this.selectedIndex).setBackgroundResource(R.drawable.no_round_corner_focus_style);
            buttonList.get(this.selectedIndex).setBackgroundResource(R.drawable.standard_button_pressed);
            buttonList.get(this.selectedIndex).setTextColor(Color.WHITE);
		}
		scrollToPlaceIndex(this.selectedIndex);
	}

	public OnPlaceSelectedListener getOnPlaceSelectedListener() {
		return onPlaceSelectedListener;
	}

	public void setOnPlaceSelectedListener(
			OnPlaceSelectedListener onPlaceSelectedListener) {
		this.onPlaceSelectedListener = onPlaceSelectedListener;
	}



	private View.OnClickListener btnPlaceClick = new View.OnClickListener() {
        public void onClick(View v) {
        	setSelectedIndex((Integer)v.getTag());
        	if (onPlaceSelectedListener != null)
        		onPlaceSelectedListener.onPlaceSelected((Integer)v.getTag());
        }
    };
	
    
    
    
    public void scrollToPlaceIndex(int placeIndex){
        

            ScrollTask task = new ScrollTask();
            task.execute(placeIndex);


        
    }

    
    
    
    

    
	
    public interface OnPlaceSelectedListener{
    	public void onPlaceSelected(int index);
    }


    private class ScrollTask extends AsyncTask<Integer, Void, Integer> {
        protected Integer doInBackground(Integer... indexs) {

        	while(scroller.getWidth() <= 0){
        		
        	}
            return indexs[0];
        }

   

        protected void onPostExecute(Integer placeIndex) {
        	int contentWidth = scroller.getContentWidth();
        	int scrollWidth = scroller.getWidth();
        	int offset = scroller.getScrollX();
        	
        	if (scrollWidth-contentWidth > 0){
            	LayoutParams param = (LayoutParams) getLayoutParams();
            	param.width = getWidth()  - scrollWidth + contentWidth;
            	setLayoutParams(param);    	
            	scroller.resetButton();
        	}
        	
        	if (placeIndex>=0 && placeIndex<buttonList.size() && buttonList.get(placeIndex).getLeft() >= offset + scrollWidth - 5){
    			scroller.scrollBy(buttonList.get(placeIndex).getLeft(),0);
    		}
        }
    }
}


class ContainerScroll extends HorizontalScrollView{
	private ImageButton btnNext = null;
	private ImageButton btnPrev = null;
	
	
	
	
	public ImageButton getBtnNext() {
		return btnNext;
	}

	public void setBtnNext(ImageButton btnNext) {
		this.btnNext = btnNext;
	}

	public ImageButton getBtnPrev() {
		return btnPrev;
	}

	public void setBtnPrev(ImageButton btnPrev) {
		this.btnPrev = btnPrev;
	}

	public ContainerScroll(Context context) {
		super(context);
		
	}

	public ContainerScroll(Context context, AttributeSet attrs) {
		super(context,attrs);
		
	}
	public ContainerScroll(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

    public void scrollToX(int x){
        this.scrollTo(x,0);
    }

    public int getContentWidth(){
    	return computeHorizontalScrollRange();
    }
    
    
    
	@Override
	protected void onScrollChanged (int l, int t, int oldl, int oldt){

		super.onScrollChanged(l, t, oldl, oldt);
		resetButton();

	}

    @Override
    protected void onLayout (boolean changed, int l, int t, int r, int b){
        super.onLayout(changed, l, t, r, b);
        resetButton();
    }

	public void resetButton(){
		int offset = computeHorizontalScrollOffset();
		int range = computeHorizontalScrollRange();
		int width = this.getWidth();

		// nullかどうか判断しないと、『Rendering Problems』が発生したので追加 2016/08/22 Amano
		if(btnPrev != null) {
			if (offset == 0) {
				btnPrev.setVisibility(View.INVISIBLE);
			} else {
				btnPrev.setVisibility(View.VISIBLE);
			}
		}

		if(btnNext != null) {
			if (offset + width >= range) {
				btnNext.setVisibility(View.INVISIBLE);
			} else {
				btnNext.setVisibility(View.VISIBLE);
			}
		}

	}
	
	
}
