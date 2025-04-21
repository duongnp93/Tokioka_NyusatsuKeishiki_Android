package tokiokaability.auction24.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

public class HiraginoW6TextView extends TextView {
    public HiraginoW6TextView(Context context) {
        super(context);
        init(context);
    }

    public HiraginoW6TextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HiraginoW6TextView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context c){
        AssetManager am = c.getAssets();
        Typeface hiragino_kakugo_w3 = Typeface.createFromAsset(am,"fonts/hiragino_kakugo_w6.otf");
        if(Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT)this.setTypeface(hiragino_kakugo_w3);
    }
}
