package tokiokaability.auction24.dialog;

import tokiokaability.auction24.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

public class HelpDialog extends Dialog {
	//private ImageView imvHelpContent = null;
	private Button btnBack = null;
	private android.view.View.OnClickListener backClickListener;
	private int helpContentResourceId;
	private RelativeLayout helpContentLayout;
	public HelpDialog(Activity activity, android.view.View.OnClickListener backClickListener, int helpContentResourceId){
		super(activity);
		this.backClickListener = backClickListener;
		this.setCanceledOnTouchOutside(false);
		this.helpContentResourceId = helpContentResourceId;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		this.setContentView(R.layout.dialog_help);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
		
		WindowManager.LayoutParams lp = getWindow().getAttributes();  
		lp.dimAmount=0.9f;  
		getWindow().setAttributes(lp);  
		getWindow().addFlags(WindowManager.LayoutParams.DIM_AMOUNT_CHANGED); 
		
		
		btnBack = (Button) findViewById(R.id.btnBack);
        if (backClickListener != null){
        	btnBack.setOnClickListener(backClickListener);
        }

        helpContentLayout = (RelativeLayout) findViewById(R.id.helpContent);

		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(helpContentResourceId,(ViewGroup) findViewById(R.id.textContent));
		//helpContentLayout.addView(v);
//        imvHelpContent = (ImageView) findViewById(R.id.imvContentHelp);
//        imvHelpContent.setImageResource(helpContentResourceId);
	}
}
