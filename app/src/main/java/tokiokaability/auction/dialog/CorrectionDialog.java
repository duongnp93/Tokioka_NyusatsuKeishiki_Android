package tokiokaability.auction.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import tokiokaability.auction.HTTPInfo.HTTPService;
import tokiokaability.auction.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction.HTTPInfo.ImageInfo;
import tokiokaability.auction.R;
import tokiokaability.auction.RealLuceAuctionApplication;
import tokiokaability.auction.commons.MessageBox;
import tokiokaability.auction.commons.Utilities;
import tokiokaability.auction.view.PinchImageView;

public class CorrectionDialog extends Dialog {
    private TextView lblCorrectionTitle,lblCorrectionBoxTitle,lblCorrectionBox,lblCorrectionContentTitle,lblCorrectionContent;
    private Button btnBack;
    private android.view.View.OnClickListener backClickListener;
    private Context context;
    public CorrectionDialog(@NonNull Activity activity,android.view.View.OnClickListener backClickListener) {
        super(activity);
        this.context = activity.getApplicationContext();
        this.backClickListener = backClickListener;
        this.setCanceledOnTouchOutside(false);
    }
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_correction);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.FILL_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        lblCorrectionContent = findViewById(R.id.lblCorrectionContent);
        lblCorrectionTitle = findViewById(R.id.lblCorrectionTitle);
        lblCorrectionBoxTitle = findViewById(R.id.lblCorrectionBoxTitle);
        lblCorrectionBox = findViewById(R.id.lblCorrectionSellNo);
        lblCorrectionContentTitle = findViewById(R.id.lblCorrectionContentTitle);
        lblCorrectionContent.setMovementMethod(new ScrollingMovementMethod());
        btnBack = (Button) findViewById(R.id.btnBack);
        if (backClickListener != null){
            btnBack.setOnClickListener(backClickListener);
        }


        if(Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT)
            setFont();

    }

    public void show(String sellNo, String correctionContent){
                show();
                lblCorrectionBox.setText(sellNo);
                lblCorrectionContent.setText(correctionContent);
        lblCorrectionContent.post(new Runnable() {
            @Override
            public void run() {
                lblCorrectionContent.scrollTo(0,0);
            }
        });
    }

    private void setFont(){
        AssetManager am = getContext().getAssets();
        Typeface yGB = Typeface.createFromAsset(am,"fonts/YUGOTHB.TTC");
        Typeface yGR = Typeface.createFromAsset(am,"fonts/YUGOTHR.TTC");
        btnBack.setTypeface(yGB);
        lblCorrectionTitle.setTypeface(yGB);
        lblCorrectionBox.setTypeface(yGB);
        lblCorrectionBoxTitle.setTypeface(yGB);
        lblCorrectionContentTitle.setTypeface(yGB);
        lblCorrectionContent.setTypeface(yGR);
    }



    @Override
    public void dismiss() {
        super.dismiss();
    }

}
