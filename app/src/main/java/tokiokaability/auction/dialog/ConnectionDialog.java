package tokiokaability.auction.dialog;


import tokiokaability.auction.R;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ConnectionDialog extends Dialog {
	private TextView lblScreenTitle;
	private TextView lblLabelConfirm;
	private TextView lblLabelConnecting;
	private ProgressBar pgbIndicator;
	private Button btnBack;
	private Button btnConnect;
	
	//private Dialog dialog;
	public android.view.View.OnClickListener backClickListener;
	public android.view.View.OnClickListener connectListener;
	
	
	public ConnectionDialog(Activity activity, android.view.View.OnClickListener backClickListener, android.view.View.OnClickListener connectListener){
		super(activity);
		this.backClickListener = backClickListener;
		this.connectListener = connectListener;
		this.setCanceledOnTouchOutside(false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		this.setContentView(R.layout.dialog_connect);
				
		btnBack = (Button) findViewById(R.id.btnBack);

        if (backClickListener != null){
        	btnBack.setOnClickListener(backClickListener);
        }
     
        btnConnect = (Button) findViewById(R.id.btnConnect);

        if (connectListener != null){
        	btnConnect.setOnClickListener(connectListener);
        }
        
        lblScreenTitle = (TextView) findViewById(R.id.lblScreenTitle);
        
        lblLabelConfirm = (TextView) findViewById(R.id.lblLabelConfirm);

        lblLabelConnecting = (TextView) findViewById(R.id.lblLabelConnecting);

        pgbIndicator = (ProgressBar) findViewById(R.id.pgbIndicator);
        
	}
	
	
    @Override
    public void onBackPressed(){
    	if (backClickListener != null){
    		backClickListener.onClick(btnBack);
    	}

    } 
	
	public void showConfirmConnecting(){

		if (!isShowing()){
			show();
		}
		
		lblLabelConfirm.setVisibility(TextView.VISIBLE);
		lblLabelConnecting.setVisibility(TextView.INVISIBLE);
		pgbIndicator.setVisibility(ProgressBar.INVISIBLE);
		
		btnBack.setEnabled(true);
		btnConnect.setEnabled(true);
		

	}
	
	public void showConnecting(){
		
		if (!isShowing()){
			show();
		}
		
		lblLabelConfirm.setVisibility(TextView.INVISIBLE);
		lblLabelConnecting.setVisibility(TextView.VISIBLE);
		pgbIndicator.setVisibility(ProgressBar.VISIBLE);
		
		btnBack.setEnabled(false);
		btnConnect.setEnabled(false);
		

	}
	
}
