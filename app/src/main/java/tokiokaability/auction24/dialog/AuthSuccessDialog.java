package tokiokaability.auction24.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import tokiokaability.auction24.R;


public class AuthSuccessDialog extends Dialog {

    ImageView authSuccessImg;
    RelativeLayout background;
    AnimationDrawable authAnimation;
    Context c;
    public AuthSuccessDialog(@NonNull Context context) {
        super(context);
        c = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_auth_success);
        authSuccessImg = findViewById(R.id.successImg);
        background = findViewById(R.id.background);
        Animation scaleUpAnim = AnimationUtils.loadAnimation(c,R.anim.animation_scale_up);
        background.startAnimation(scaleUpAnim);
        authSuccessImg.setBackgroundResource(R.drawable.auth1);
        authSuccessImg.setAlpha(1);
//        Animation fadeInAnim = AnimationUtils.loadAnimation(c,R.anim.fade_out);
//        fadeInAnim.setStartOffset(300);
//        authSuccessImg.startAnimation(fadeInAnim);
        new CountDownTimer(500,500) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                authSuccessImg.setBackgroundResource(R.drawable.auth_animation);
                authAnimation = (AnimationDrawable)authSuccessImg.getBackground();
                authAnimation.start();
            }
        }.start();

    }
}
