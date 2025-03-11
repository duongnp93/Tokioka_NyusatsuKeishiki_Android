package tokiokaability.auction.view;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import android.widget.Scroller;

import java.lang.reflect.Field;

public class ViewPagerCustomDuration extends ViewPager {
    private FixedSpeedScroller mScroller = null;

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    private boolean scrollable = true;
    public ViewPagerCustomDuration(Context context) {
        super(context);
        init();
    }

    public ViewPagerCustomDuration(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);

        //return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return false;
    }

    /*
     * Override the Scroller instance with our own class so we can change the
     * duration
     */
    private void init() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            mScroller = new FixedSpeedScroller(getContext(),
                    new DecelerateInterpolator());
            scroller.set(this, mScroller);
        } catch (Exception ignored) {
        }
    }

    /*
     * Set the factor by which the duration will change
     */
    public void setScrollDuration(int duration) {
        mScroller.setScrollDuration(duration);
    }

    private class FixedSpeedScroller extends Scroller {

        private int mDuration = 500;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);

        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);

        }

        public void setScrollDuration(int duration) {
            mDuration = duration;
        }
    }
}
