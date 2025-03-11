package tokiokaability.auction.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Chung on 2016/09/05.
 */
public class PictureLayout extends RelativeLayout {
    public PictureLayout(Context context) {
        super(context);
    }

    public PictureLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        super.onLayout(changed, l, t, r, b);

/*
        int w = (r - l - 10) / 2;
        int h = w * 3 / 4;
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);

            if (RelativeLayout.class.isInstance(v)) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
                params.height = h;
                params.width = w;
                v.setLayoutParams(params);
            }
        }

*/

    }

    @Override
    protected void onSizeChanged(int lw, int lh, int oldw, int oldh) {
        super.onSizeChanged(lw, lh, oldw, oldh);
        int w = (lw - 10) / 2;
        int h = w * 3 / 4;
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);

            if (RelativeLayout.class.isInstance(v)) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
                params.height = h;
                params.width = w;
                v.setLayoutParams(params);
            }
        }
    }
}
