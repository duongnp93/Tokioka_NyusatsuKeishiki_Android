package tokiokaability.auction.dialog;

import tokiokaability.auction.HTTPInfo.ItemInfo;
import tokiokaability.auction.R;
import tokiokaability.auction.HTTPInfo.LaneInfo;
import tokiokaability.auction.activities.AuctionActivity;
import tokiokaability.auction.commons.Utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class SuccessfullBitDialog extends Dialog {
    ImageButton closeBtn;
    ImageView imgShuppin;
    TextView brandTxt, modelTxt, serialTxt, accTxt, priceTxt;
    RelativeLayout formBgr;
    private android.view.View.OnClickListener closeClickListener;


    public SuccessfullBitDialog(Activity activity, android.view.View.OnClickListener closeClickListener) {
        super(activity);
        this.closeClickListener = closeClickListener;
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_successfull_bit);

        getWindow().setBackgroundDrawable(new ColorDrawable(0));

        closeBtn = findViewById(R.id.closeBtn);
        imgShuppin = findViewById(R.id.imgShuppin);
        brandTxt = findViewById(R.id.brandTxt);
        modelTxt = findViewById(R.id.ModelTxt);
        serialTxt = findViewById(R.id.SerialTxt);
        accTxt = findViewById(R.id.AccTxt);
        priceTxt = findViewById(R.id.PriceTxt);
        formBgr = findViewById(R.id.formBgr);
        formBgr.setOnClickListener(this.closeClickListener);
        closeBtn.setOnClickListener(this.closeClickListener);
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) setFont();
    }

    private void setFont() {
        AssetManager am = getContext().getAssets();
        Typeface yGB = Typeface.createFromAsset(am, "fonts/YUGOTHB.TTC");
        Typeface yGR = Typeface.createFromAsset(am, "fonts/YUGOTHR.TTC");
        brandTxt.setTypeface(yGR);
        modelTxt.setTypeface(yGR);
        serialTxt.setTypeface(yGR);
        accTxt.setTypeface(yGR);
        priceTxt.setTypeface(yGB);
    }

    public void show(LaneInfo lane, int currentRate) {
        this.show();
        ItemInfo itemInfo = lane.getCurrentItem();
        if (itemInfo.getImage1() != null)
            imgShuppin.setImageBitmap(BitmapFactory.decodeByteArray(Utilities.rotaledByteArr(itemInfo.getImage1()), 0, Utilities.rotaledByteArr(itemInfo.getImage1()).length));
        else{
            Drawable drawable = getContext().getResources().getDrawable(R.drawable.auction_no_photo);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            imgShuppin.setImageBitmap(bitmap);
        }
        brandTxt.setText(itemInfo.getBrand());
        modelTxt.setText(itemInfo.getModel1());
        serialTxt.setText(itemInfo.getSerialNo());
        accTxt.setText(itemInfo.getAccessories());

        priceTxt.setText(getDisplayPrice(currentRate, lane.getCurrentPrice(), itemInfo.getRateDollar(), itemInfo.getRateEuro(), itemInfo.getRateYuan(), itemInfo.getRateHKD()));
    }

    public static String getDisplayPrice(int currentRate, long price, double rateDollar, double rateEuro, double rateYuan, double rateHKD) {
        double displayPrice = 0;
        DecimalFormat formatter = new DecimalFormat("#,###,###");

        if (price == 99999999)
            return "999999999";
        String ret = "";
        switch (currentRate) {
            case 1:
                displayPrice = price;
                formatter = new DecimalFormat("#,###,###");
                ret = "￥" + formatter.format(displayPrice);
                break;
            case 2:
                displayPrice = price * rateDollar;
                formatter = new DecimalFormat("#,###,###");
                ret = "＄" + formatter.format(displayPrice);
                //displayPrice = price * rateDollar;
                //formatter = new DecimalFormat("#,###,###.00");
                break;
            case 3:
                displayPrice = price * rateEuro;
                formatter = new DecimalFormat("#,###,###.00");
                ret = "€" + formatter.format(displayPrice);
                break;
            case 4:
                displayPrice = price * rateYuan;
                formatter = new DecimalFormat("#,###,###");
                ret = "元" + formatter.format(displayPrice);
                break;
            case 5:
                displayPrice = price * rateHKD;
                formatter = new DecimalFormat("#,###,###");
                ret = "HK＄" + formatter.format(displayPrice);
                break;

        }
        if (currentRate == 3) {
            ret = ret.replace('.', ':');
            ret = ret.replace(',', '.');
            ret = ret.replace(':', ',');
            return ret;
        }
        return ret;
    }
}
