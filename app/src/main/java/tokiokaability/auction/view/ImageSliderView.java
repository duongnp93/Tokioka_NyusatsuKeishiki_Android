package tokiokaability.auction.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.view.ViewGroup;
import tokiokaability.auction.R;
import tokiokaability.auction.commons.Utilities;

/**
 * Created by chungnv on 2/10/14.
 */
public class ImageSliderView extends RelativeLayout {
    private ImageButton btnPrev = null;
    private ImageButton btnNext = null;
    private ImageSliderScroll scrollView = null;
    private LinearLayout scrollContent = null;
    private ImageView imgImage1 = null;
    private ImageView imgImage2 = null;
    private ImageView imgImage3 = null;
    private ImageView imgImage4 = null;
    private ImageView imgImage5 = null;
    private OnImageClickedListener onImageClickedListener;
    private int totalImages = 0;
    private int imgWidth = 0;
    private int currentIndex = 0;
    private int currentPosition = 0;

    public void setOnImageClickedListener(OnImageClickedListener onImageClickedListener){
        this.onImageClickedListener = onImageClickedListener;
    }

    public OnImageClickedListener getOnImageClickedListener(){
        return this.onImageClickedListener;
    }

    public interface OnImageClickedListener{
        public void onImageClicked(int index);
    }

    public ImageSliderView(Context context) {
        super(context);
        initView(context);
    }

    public ImageSliderView(Context context, AttributeSet attrs) {
        super(context,attrs);
        initView(context);
    }

    public ImageSliderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public void initView(Context context){
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_image_slider,this);
        //btnPrev = (ImageButton) findViewById(R.id.btnPrev);
        //btnNext = (ImageButton) findViewById(R.id.btnNext);
        scrollView = (ImageSliderScroll) findViewById(R.id.scrollView);
        scrollContent = (LinearLayout) findViewById(R.id.scrollContent);
        imgImage1 = (ImageView) findViewById(R.id.imgImage1);
        imgImage2 = (ImageView) findViewById(R.id.imgImage2);
        imgImage3 = (ImageView) findViewById(R.id.imgImage3);
        imgImage4 = (ImageView) findViewById(R.id.imgImage4);
        imgImage5 = (ImageView) findViewById(R.id.imgImage5);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnPrev = (ImageButton) findViewById(R.id.btnPrev);


        scrollView.setSmoothScrollingEnabled(true);
        scrollView.setHorizontalScrollBarEnabled(false);
        scrollView.setOnImageSliderScrollListener(imageSliderScrollListener);




        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics display = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(display);
        float scrollContentWidth =display.widthPixels - Utilities.convertDpToPixel(10,context)*2;


        ViewGroup.LayoutParams params =  imgImage1.getLayoutParams();
        params.width = (int)(scrollContentWidth - Utilities.convertDpToPixel(10,context))/2;
        params.height = LayoutParams.WRAP_CONTENT;
        imgImage1.setLayoutParams(params);
        imgImage1.setTag(1);
        imgImage1.setOnClickListener(imgImagesClick);


        params =  imgImage2.getLayoutParams();
        params.width = (int)(scrollContentWidth - (int)Utilities.convertDpToPixel(10,context))/2;
        params.height = LayoutParams.WRAP_CONTENT;
        imgImage2.setLayoutParams(params);
        imgImage2.setTag(2);
        imgImage2.setOnClickListener(imgImagesClick);


        params =  imgImage3.getLayoutParams();
        params.width = (int)(scrollContentWidth - (int)Utilities.convertDpToPixel(10,context))/2;
        params.height = LayoutParams.WRAP_CONTENT;
        imgImage3.setLayoutParams(params);
        imgImage3.setTag(3);
        imgImage3.setOnClickListener(imgImagesClick);


        params =  imgImage4.getLayoutParams();
        params.width = (int)(scrollContentWidth - (int)Utilities.convertDpToPixel(10,context))/2;
        params.height = LayoutParams.WRAP_CONTENT;
        imgImage4.setLayoutParams(params);
        imgImage4.setTag(4);
        imgImage4.setOnClickListener(imgImagesClick);


        params =  imgImage5.getLayoutParams();
        params.width = (int)(scrollContentWidth - (int)Utilities.convertDpToPixel(10,context))/2;
        params.height = LayoutParams.WRAP_CONTENT;
        imgImage5.setLayoutParams(params);
        imgImage5.setTag(5);
        imgImage5.setOnClickListener(imgImagesClick);


        btnPrev.setOnClickListener(btnPrevClick);
        btnNext.setOnClickListener(btnNextClick);


        imgImage3.setVisibility(ImageView.GONE);
        imgImage4.setVisibility(ImageView.GONE);
        imgImage5.setVisibility(ImageView.GONE);
        btnPrev.setVisibility(ImageButton.GONE);
        btnNext.setVisibility(ImageButton.GONE);


    }

    private View.OnClickListener btnPrevClick = new View.OnClickListener() {
        public void onClick(View v) {
        	/*
            scrollView.pageScroll(FOCUS_LEFT);
            btnNext.setVisibility(ImageButton.VISIBLE);
            btnPrev.setVisibility(ImageButton.INVISIBLE);
            */
        	currentIndex = getCurrentIndexByPostion();
        	if(currentIndex > 0) {
        		currentIndex--;
            	int nextPosition = currentIndex * (imgWidth + 20) - 10;
            	currentPosition = nextPosition;
            	scrollView.scrollTo(nextPosition, 0);
        	}
        }
    };

    private View.OnClickListener btnNextClick = new View.OnClickListener() {
        public void onClick(View v) {
            //scrollView.pageScroll(FOCUS_RIGHT);
        	/*
        	scrollView.pageScroll(FOCUS_RIGHT);
        	btnPrev.setVisibility(ImageButton.VISIBLE);
            btnNext.setVisibility(ImageButton.INVISIBLE);
            */
        	currentIndex = getCurrentIndexByPostion();
        	if(currentIndex < totalImages) {
        		currentIndex++;
            	int nextPosition = currentIndex * (imgWidth + 20) - 10;
            	currentPosition = nextPosition;
            	scrollView.scrollTo(nextPosition, 0);
        	}
        }
    };
    
    private int getCurrentIndexByPostion() {
    	int returnVal = currentIndex;
    	for(int i=0; i<=5; i++){
    	    if((currentPosition > (currentIndex * (imgWidth + 20) - 10)) && (currentPosition < ((currentIndex + 1) * (imgWidth + 20) - 10))) {
    	    	returnVal = i;
    	    	break;
    	    }
    	}
    	return returnVal;
    }

    private View.OnClickListener imgImagesClick = new View.OnClickListener() {
        public void onClick(View v) {
            if (onImageClickedListener != null){
                onImageClickedListener.onImageClicked((Integer) v.getTag());
            }
        }
    };


    private ImageSliderScroll.OnImageSliderScrollListener imageSliderScrollListener = new ImageSliderScroll.OnImageSliderScrollListener() {
        public void onScrollChanged (int l, int t, int oldl, int oldt){
        	currentPosition = l;
        	if(totalImages > 2) {
        		int maxPosition = (totalImages-2) * (imgWidth + 20) - 20;
            	if ( l  == 0){
                    //scrollView.fullScroll(FOCUS_RIGHT);
                    btnNext.setVisibility(ImageButton.VISIBLE);
                    btnPrev.setVisibility(ImageButton.INVISIBLE);
                }
            	//else if (l >= scrollView.getMaxScrollAmount() - 1){
                else if (l >= maxPosition){

                    btnPrev.setVisibility(ImageButton.VISIBLE);
                    btnNext.setVisibility(ImageButton.INVISIBLE);
                }
                else{
                    btnNext.setVisibility(ImageButton.VISIBLE);
                    btnPrev.setVisibility(ImageButton.VISIBLE);
                }
        	}
        }
    };
    
    public void setTotalImage(int total) {
    	this.totalImages = total;
    }
    
    public int getTotalImage() {
    	return this.totalImages;
    }
    
    public void initBtnState() {
    	ViewGroup.LayoutParams imgLayout = imgImage1.getLayoutParams();
    	this.imgWidth = imgLayout.width;
    	//Log.w("Image width: ", String.valueOf(imgWidth));
    	if(totalImages >= 3) {
    		btnPrev.setVisibility(ImageButton.INVISIBLE);
            btnNext.setVisibility(ImageButton.VISIBLE);
            scrollView.fullScroll(FOCUS_LEFT);
    	}
    	else {
    		btnPrev.setVisibility(ImageButton.GONE);
            btnNext.setVisibility(ImageButton.GONE);
    	}
    }

    public void setImage1(Bitmap image){
        imgImage1.setImageBitmap(image);
    }


    public void setImage2(Bitmap image){
        imgImage2.setImageBitmap(image);
    }

    public void setImage3(Bitmap image){
        if (image != null){
            imgImage3.setImageBitmap(image);
            imgImage3.setVisibility(ImageView.VISIBLE);
//            btnPrev.setVisibility(ImageButton.INVISIBLE);
//            btnNext.setVisibility(ImageButton.VISIBLE);
//            scrollView.fullScroll(FOCUS_LEFT);
        }
        else{
            imgImage3.setVisibility(ImageView.GONE);
//            btnPrev.setVisibility(ImageButton.GONE);
//            btnNext.setVisibility(ImageButton.GONE);
        }

    }

    public void setImage4(Bitmap image){
        if (image != null){
            imgImage4.setImageBitmap(image);
            imgImage4.setVisibility(ImageView.VISIBLE);
        }
        else{
            imgImage4.setVisibility(ImageView.GONE);
        }

    }

    public void setImage5(Bitmap image){
        if (image != null){
            imgImage5.setImageBitmap(image);
            imgImage5.setVisibility(ImageView.VISIBLE);
        }
        else{
            imgImage5.setVisibility(ImageView.GONE);
        }

    }
}

class ImageSliderScroll extends HorizontalScrollView{



    public interface OnImageSliderScrollListener{
        public void onScrollChanged (int l, int t, int oldl, int oldt);
    }

    private OnImageSliderScrollListener onImageSliderScrollListener;
    public  void setOnImageSliderScrollListener(OnImageSliderScrollListener listener){
        this.onImageSliderScrollListener = listener;
    }
    public OnImageSliderScrollListener getsetOnImageSliderScrollListener(){
        return this.onImageSliderScrollListener;
    }


    public ImageSliderScroll(Context context, AttributeSet attrs) {
        super(context,attrs);

    }

    public ImageSliderScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }


    @Override
    protected void onScrollChanged (int l, int t, int oldl, int oldt){
    	//Log.w("old position: ", String.valueOf(oldl));
    	//Log.w("new position: ", String.valueOf(l));
    	super.onScrollChanged(l, t, oldl, oldt);
    	
        if (this.onImageSliderScrollListener != null){
            this.onImageSliderScrollListener.onScrollChanged(l,t,oldl,oldt);
        }

    }

/*
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Do not allow touch events.
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Do not allow touch events.
        return false;
    }
    */
}