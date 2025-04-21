package tokiokaability.auction24.commons;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

public class HiraginoW3TextView extends TextView {
    public HiraginoW3TextView(Context context) {
        super(context);
        init(context);
    }

    public HiraginoW3TextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HiraginoW3TextView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context c){
        if(Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT){
            AssetManager am = c.getAssets();
            Typeface hiragino_kakugo_w3 = Typeface.createFromAsset(am,"fonts/hiragino_kakugo_w3.otf");
            this.setTypeface(hiragino_kakugo_w3);
        }
    }
}
