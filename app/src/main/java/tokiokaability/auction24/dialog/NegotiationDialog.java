package tokiokaability.auction24.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import tokiokaability.auction24.R;
import tokiokaability.auction24.commons.Utilities;

public class NegotiationDialog extends Dialog {
    private TextView lblCorrectionTitle,lblCorrectionBoxTitle,lblCorrectionBox,lblLastBid,lblHoldPrice,lblReholdPrice,lblHoldFlg;
    private Button btnBack;
    private android.view.View.OnClickListener backClickListener;
    private Context context;
    public NegotiationDialog(@NonNull Activity activity, android.view.View.OnClickListener backClickListener) {
        super(activity);
        this.context = activity;
        this.backClickListener = backClickListener;
        this.setCanceledOnTouchOutside(false);
    }
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_kosho);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.FILL_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        lblLastBid = findViewById(R.id.lblLastBidPrice);
        lblCorrectionTitle = findViewById(R.id.lblCorrectionTitle);
        lblCorrectionBoxTitle = findViewById(R.id.lblCorrectionBoxTitle);
        lblCorrectionBox = findViewById(R.id.lblCorrectionSellNo);
        lblHoldPrice = findViewById(R.id.lblHoldPrice);
        lblHoldFlg = findViewById(R.id.lblHoldFlg);
        lblReholdPrice = findViewById(R.id.lblReholdPrice);
        btnBack = (Button) findViewById(R.id.btnBack);
        if (backClickListener != null){
            btnBack.setOnClickListener(backClickListener);
        }


        if(Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT)
            setFont();

    }

    public void show(String sellNo, String lastBidPrice,String holdPrice,String holdFlg,String reholdPrice){
        show();
        lblCorrectionBox.setText(sellNo);
        lblLastBid.setText(String.format(context.getResources().getString(R.string.neg_popup_lastbid),
                Utilities.formatPrice(context,lastBidPrice)));
        if(lastBidPrice.length() == 0 || lastBidPrice.equals("0"))
            lblLastBid.setVisibility(View.GONE);
        else lblLastBid.setVisibility(View.VISIBLE);
        lblHoldPrice.setText(String.format(context.getResources().getString(R.string.neg_popup_holdprice),
                Utilities.formatPrice(context,holdPrice)));
        if(holdPrice.length() == 0 || holdPrice.equals("0"))
            lblHoldPrice.setVisibility(View.GONE);
        else lblHoldPrice.setVisibility(View.VISIBLE);
        lblReholdPrice.setText(String.format(context.getResources().getString(R.string.neg_popup_rehold),
                Utilities.formatPrice(context,reholdPrice)));
        if(reholdPrice.length() == 0 || reholdPrice.equals("0"))
            lblReholdPrice.setVisibility(View.GONE);
        else lblReholdPrice.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) lblHoldFlg.getLayoutParams();
        if(holdFlg.length() == 0 || holdFlg.equals("0")){
            lp.height = 0;
            lp.setMargins((int)convertDpToPixel(20,context),(int)convertDpToPixel(100,context),0,0);
            lblHoldFlg.setLayoutParams(lp);
            lblHoldFlg.requestLayout();
        }
        else{
            lp.height = (int)convertDpToPixel(30,context);
            lp.setMargins((int)convertDpToPixel(20,context),(int)convertDpToPixel(110,context),0,0);
            lblHoldFlg.setLayoutParams(lp);
            lblHoldFlg.requestLayout();
        }
    }
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    private void setFont(){
        AssetManager am = getContext().getAssets();
        Typeface yGB = Typeface.createFromAsset(am,"fonts/YUGOTHB.TTC");
        Typeface yGR = Typeface.createFromAsset(am,"fonts/YUGOTHR.TTC");
        btnBack.setTypeface(yGB);
        lblCorrectionTitle.setTypeface(yGB);
        lblCorrectionBox.setTypeface(yGB);
        lblCorrectionBoxTitle.setTypeface(yGB);
        lblLastBid.setTypeface(yGB);
        lblHoldPrice.setTypeface(yGB);
        lblReholdPrice.setTypeface(yGB);
        lblHoldFlg.setTypeface(yGB);
    }



    @Override
    public void dismiss() {
        super.dismiss();
    }
}
