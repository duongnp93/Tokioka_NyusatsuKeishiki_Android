package tokiokaability.auction24.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

public class HiraginoW6Button extends Button {
    public HiraginoW6Button(Context context) {
        super(context);
        init(context);
    }

    public HiraginoW6Button(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HiraginoW6Button(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context c){
        AssetManager am = c.getAssets();
        Typeface hiragino_w6 = Typeface.createFromAsset(am,"fonts/hiragino_kakugo_w6.otf");
        if(Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT)this.setTypeface(hiragino_w6);
    }
}
