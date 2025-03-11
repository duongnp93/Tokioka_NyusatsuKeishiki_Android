package tokiokaability.auction.activities;

import tokiokaability.auction.R;
import tokiokaability.auction.HTTPInfo.BaseInfo;
import tokiokaability.auction.HTTPInfo.HTTPService;
import tokiokaability.auction.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction.HTTPInfo.PreviewRequestInfo;
import tokiokaability.auction.HTTPInfo.RuleURLInfo;
import tokiokaability.auction.commons.MessageBox;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

public class PreviewRequestActivity extends BaseActivity {

	private TextView lblPlaceNameVal;
	private TextView lblHoldDateVal;
	private TextView lblTimeNoVal;
	private TextView lblItemNoVal;
	private TextView lblCarNameVal;
	private EditText txtPersonNameVal;
	private EditText txtTelVal;
	private EditText txtTel2Val;
	private EditText txtFaxVal;
	
	private CheckBox cbOutsideFlag;
	private CheckBox cbInsideFlag;
	private CheckBox cbSeatFlag;
	private CheckBox cbSizeFlag;
	private CheckBox cbAudioFlag;
	private CheckBox cbDriveFlag;
	private CheckBox cbEngineFlag;
	private CheckBox cbCapacityFlag;
	private CheckBox cbElectricFlag;
	private CheckBox cbPowerSteeringFlag;
	private CheckBox cbTireFlag;
	private CheckBox cbWheelFlag;
	private EditText txtRequestMemo;
	
	private ImageButton btnRegisterPreviewRequest;
	private ImageButton btnRuleURL;
	
	private PreviewRequestInfo previewRequestInfo;
	
	private ScrollView scvDetailsPreviewRequest;
	
	static int INVALID_NOT_INPUT_PERSON_NAME = 1;
	static int INVALID_NOT_INPUT_TEL = 2;
	static int INVALID_NOT_INPUT_FAX = 3;
	static int INVALID_NOT_CHECK_ONE = 4;
	static int VALID = 0;

	// ************************ SCREEN LIFECYCLE ******************
	/*
	 * Initialization
	 */
	@Override
	protected void initialization() {
		setContentView(R.layout.activity_preview_request);
		super.initialization();

		// Get control.
		lblPlaceNameVal = (TextView) findViewById(R.id.lblPlaceNameVal);
		lblHoldDateVal = (TextView) findViewById(R.id.lblHoldDateVal);
		lblTimeNoVal = (TextView) findViewById(R.id.lblTimesNoVal);
		lblItemNoVal = (TextView) findViewById(R.id.lblItemNoVal);
		lblCarNameVal = (TextView) findViewById(R.id.lblCarNameVal);
		txtPersonNameVal = (EditText) findViewById(R.id.txtPersonNameVal);
		txtTelVal = (EditText) findViewById(R.id.txtTelVal);
		txtTel2Val = (EditText) findViewById(R.id.txtTel2Val);
		txtFaxVal = (EditText) findViewById(R.id.txtFaxVal);
		
		cbOutsideFlag = (CheckBox) findViewById(R.id.cbOutsideFlag);
		cbInsideFlag = (CheckBox) findViewById(R.id.cbInsideFlag);
		cbSeatFlag = (CheckBox) findViewById(R.id.cbSeatFlag);
		cbSizeFlag = (CheckBox) findViewById(R.id.cbSizeFlag);
		cbAudioFlag = (CheckBox) findViewById(R.id.cbAudioFlag);
		cbDriveFlag = (CheckBox) findViewById(R.id.cbDriveFlag);
		cbCapacityFlag = (CheckBox) findViewById(R.id.cbCapacityFlag);
		cbElectricFlag = (CheckBox) findViewById(R.id.cbElectricFlag);
		cbEngineFlag = (CheckBox) findViewById(R.id.cbEngineFlag);
		cbPowerSteeringFlag = (CheckBox) findViewById(R.id.cbPowerSteeringFlag);
		cbTireFlag = (CheckBox) findViewById(R.id.cbTireFlag);
		cbWheelFlag = (CheckBox) findViewById(R.id.cbWheelFlag);
		
		txtRequestMemo = (EditText) findViewById(R.id.txtRequestMemo);
		
		btnRegisterPreviewRequest = (ImageButton) findViewById(R.id.btnRegisterPreviewRequest);
		
		btnRuleURL = (ImageButton) findViewById(R.id.btnRuleURL);
		
		// Register listener.
		btnRegisterPreviewRequest.setOnClickListener(btnRegisterPreviewRequestClick);
		
		btnRuleURL.setOnClickListener(btnRuleURLClick);

		// Set image for helps.
		this.setHelpResourceId(R.drawable.help_content_bit_offline);

		// set previewRequestInfo from activity parameter.
		previewRequestInfo = (PreviewRequestInfo) getActivityParam("PreviewRequestInfo", new PreviewRequestInfo());
		
		// Get value from previewRequestInfo object to screen.
		setPreviewRequestInfoToActivity(previewRequestInfo);
		
		// Disable/Anable button and EdiText
		setEnableOrDisbaleButonAndEditText(previewRequestInfo.getRequestStatus());
		
		scvDetailsPreviewRequest = (ScrollView) findViewById(R.id.scvDetailsPreviewRequest);
		scvDetailsPreviewRequest.scrollTo(0, 0);
	}
	
	private void setPreviewRequestInfoToActivity(PreviewRequestInfo previewRequestInfo){
		lblPlaceNameVal.setText(previewRequestInfo.getPlaceName());
		lblHoldDateVal.setText(previewRequestInfo.getHoldDate());
		lblTimeNoVal.setText(previewRequestInfo.getTimesNo());
		lblItemNoVal.setText(previewRequestInfo.getItemNo());
		lblCarNameVal.setText(previewRequestInfo.getCarName());
		txtPersonNameVal.setText(previewRequestInfo.getPersonName());
		txtTel2Val.setText(previewRequestInfo.getTEL2());
		txtTelVal.setText(previewRequestInfo.getTEL());
		txtFaxVal.setText(previewRequestInfo.getFAX());
		cbOutsideFlag.setChecked(previewRequestInfo.getOutsideFlag()==1);
		cbInsideFlag.setChecked(previewRequestInfo.getInsideFlag()==1);
		cbSeatFlag.setChecked(previewRequestInfo.getSeatFlag()==1);
		cbSizeFlag.setChecked(previewRequestInfo.getSizeFlag()==1);
		cbAudioFlag.setChecked(previewRequestInfo.getAudioFlag()==1);
		cbCapacityFlag.setChecked(previewRequestInfo.getCapacityFlag()==1);
		cbElectricFlag.setChecked(previewRequestInfo.getElectricFlag()==1);
		cbPowerSteeringFlag.setChecked(previewRequestInfo.getPowerSteeringFlag()==1);
		cbTireFlag.setChecked(previewRequestInfo.getTireFlag()==1);
		cbWheelFlag.setChecked(previewRequestInfo.getWheelFlag()==1);
		cbEngineFlag.setChecked(previewRequestInfo.getEngineFlag()==1);
		cbDriveFlag.setChecked(previewRequestInfo.getDriveFlag()==1);
		txtRequestMemo.setText(previewRequestInfo.getRequestMemo());
	}
	
	private void setActivityToPreviewRequestInfo(){
		previewRequestInfo.setPersonName(txtPersonNameVal.getText().toString());
		previewRequestInfo.setTEL(txtTelVal.getText().toString());
		previewRequestInfo.setTEL2(txtTel2Val.getText().toString());
		previewRequestInfo.setFAX(txtFaxVal.getText().toString());
		previewRequestInfo.setRequestMemo(txtRequestMemo.getText().toString());
		
		previewRequestInfo.setOutsideFlag(cbOutsideFlag.isChecked()?1:0);
		previewRequestInfo.setInsideFlag(cbInsideFlag.isChecked()?1:0);
		previewRequestInfo.setSeatFlag(cbSeatFlag.isChecked()?1:0);
		previewRequestInfo.setSizeFlag(cbSizeFlag.isChecked()?1:0);
		previewRequestInfo.setAudioFlag(cbAudioFlag.isChecked()?1:0);
		previewRequestInfo.setCapacityFlag(cbCapacityFlag.isChecked()?1:0);
		previewRequestInfo.setPowerSteeringFlag(cbPowerSteeringFlag.isChecked()?1:0);
		previewRequestInfo.setTireFlag(cbTireFlag.isChecked()?1:0);
		previewRequestInfo.setWheelFlag(cbWheelFlag.isChecked()?1:0);
		previewRequestInfo.setEngineFlag(cbEngineFlag.isChecked()?1:0);
		previewRequestInfo.setElectricFlag(cbElectricFlag.isChecked()?1:0);
		previewRequestInfo.setDriveFlag(cbDriveFlag.isChecked()?1:0);
	}
	
	private void setEnableOrDisbaleButonAndEditText(int requestStatus){
		if (requestStatus == 1 || requestStatus == 2 || requestStatus == 3) {
			txtPersonNameVal.setEnabled(false);
			
			txtTelVal.setEnabled(false);
			txtTel2Val.setEnabled(false);
			txtFaxVal.setEnabled(false);
			
			cbOutsideFlag.setEnabled(false);
			cbInsideFlag.setEnabled(false);
			cbSeatFlag.setEnabled(false);
			cbSizeFlag.setEnabled(false);
			cbAudioFlag.setEnabled(false);
			cbDriveFlag.setEnabled(false);
			cbEngineFlag.setEnabled(false);
			cbCapacityFlag.setEnabled(false);
			cbElectricFlag.setEnabled(false);
			cbPowerSteeringFlag.setEnabled(false);
			cbTireFlag.setEnabled(false);
			cbWheelFlag.setEnabled(false);
			txtRequestMemo.setEnabled(false);
			
			btnRegisterPreviewRequest.setEnabled(false);
		}
		
		// 下見依頼申込情報の状態に関わらず、戻り値２のチェックボックスは非表示に変更してください。
		if(previewRequestInfo.getOutsideFlag() == 2) cbOutsideFlag.setVisibility(View.GONE);
		if(previewRequestInfo.getInsideFlag() == 2) cbInsideFlag.setVisibility(View.GONE);
		if(previewRequestInfo.getSeatFlag() == 2) cbSeatFlag.setVisibility(View.GONE);
		if(previewRequestInfo.getSizeFlag() == 2) cbSizeFlag.setVisibility(View.GONE);
		if(previewRequestInfo.getAudioFlag() == 2) cbAudioFlag.setVisibility(View.GONE);
		if(previewRequestInfo.getDriveFlag() == 2) cbDriveFlag.setVisibility(View.GONE);
		if(previewRequestInfo.getEngineFlag() == 2) cbEngineFlag.setVisibility(View.GONE);
		if(previewRequestInfo.getCapacityFlag() == 2) cbCapacityFlag.setVisibility(View.GONE);
		if(previewRequestInfo.getElectricFlag() == 2) cbElectricFlag.setVisibility(View.GONE);
		if(previewRequestInfo.getPowerSteeringFlag() == 2) cbPowerSteeringFlag.setVisibility(View.GONE);
		if(previewRequestInfo.getTireFlag() == 2) cbTireFlag.setVisibility(View.GONE);
		if(previewRequestInfo.getWheelFlag() == 2) cbWheelFlag.setVisibility(View.GONE);
	}
	
	private boolean checkValidattionInput() {
		if (txtPersonNameVal == null || txtPersonNameVal.length() == 0) {
			txtPersonNameVal.requestFocus();
			MessageBox.show(getActivityContext(), R.string.register_preview_request_title, R.string.register_preview_request_no_person_name);
			return false;
		}
		
		if ((txtTelVal == null || txtTelVal.length() == 0) && (txtTel2Val == null || txtTel2Val.length() == 0)) {
			txtTelVal.requestFocus();
			MessageBox.show(getActivityContext(), R.string.register_preview_request_title, R.string.register_preview_request_no_tel);
			return false;
		}
		
		/*if (txtFaxVal == null || txtFaxVal.length() == 0) {
			txtFaxVal.requestFocus();
			MessageBox.show(getActivityContext(), R.string.register_preview_request_title, R.string.register_preview_request_no_fax);
			return false;
		}*/
		
		if (!cbOutsideFlag.isChecked() && !cbInsideFlag.isChecked()
			&& !cbSeatFlag.isChecked() && !cbSizeFlag.isChecked()
			&& !cbAudioFlag.isChecked() && !cbDriveFlag.isChecked()
			&& !cbEngineFlag.isChecked() && !cbCapacityFlag.isChecked()
			&& !cbElectricFlag.isChecked() && !cbPowerSteeringFlag.isChecked()
			&& !cbTireFlag.isChecked() && !cbWheelFlag.isChecked()) {
			MessageBox.show(getActivityContext(), R.string.register_preview_request_title, R.string.register_preview_request_no_checkbox);
			return false;
		}
		
		return true;
	}
	
	private View.OnClickListener btnRuleURLClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			getRequestRuleURL();
		}
	};
	
	private View.OnClickListener btnRegisterPreviewRequestClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(txtPersonNameVal.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); 
    		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(txtTelVal.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(txtFaxVal.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(txtRequestMemo.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    		
			// CHECK VALIDATE
			if (checkValidattionInput() == true) {
				
				// Show confirm message.
				confirmMessage(R.string.register_preview_request_title_confirm_dialog, R.string.register_preview_request_message_confirm_dialog,
						getString(R.string.register_preview_request_ok),
						getString(R.string.register_preview_request_cancel),
						okItemRegistClick, cancelClick);
			}
		}
	};
	
	
	/**
	 * Create confirm message.
	 */
	private void confirmMessage(int titleId, int messageId, String okText,
			String cancelText, DialogInterface.OnClickListener okClick,
			DialogInterface.OnClickListener cancelClick) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// Set value.
		builder.setMessage(messageId);
		builder.setTitle(titleId);
		builder.setCancelable(false);
		builder.setPositiveButton(okText, okClick);
		builder.setNegativeButton(cancelText, cancelClick);

		// Create dialog and show to screen.
		AlertDialog alert = builder.create();
		alert.show();
		// Must call show() prior to fetching views
		TextView messageView = (TextView)alert.findViewById(android.R.id.message);
		messageView.setGravity(Gravity.CENTER);

		TextView titleView = (TextView)alert.findViewById(this.getResources().getIdentifier("alertTitle", "id", "android"));
		if (titleView != null) {
		    titleView.setGravity(Gravity.CENTER);
		}
	}
	
	/**
	 * Call when click accept button of confirm message of register button.
	 */
	public DialogInterface.OnClickListener okItemRegistClick = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// CALL SERVICE
			btnRegisterPreviewRequest.setEnabled(false);
			callRegistPreviewRequest();
		}
	};
	
	/**
	 * Close confirm dialog.
	 */
	public DialogInterface.OnClickListener cancelClick = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}
	};
	
	private void getRequestRuleURL() {
		
		HTTPService objService = new HTTPService(new HTTPServiceCallback() {
			@Override
			public void onSucessfully(Object returnData) {
				
				RuleURLInfo resultInfo = (RuleURLInfo) returnData;
				
				hideProgressDialog(); // Hide process dialog.
				
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(resultInfo.getURL()));
	    		startActivity(browserIntent);
			}

			@Override
			public void onFailed(int errorCode) {
				// Hide process dialog.
				hideProgressDialog();
				
				// Show error content
				catchSystemErrors(errorCode);
			}
		});

		showProgressDialog(R.string.please_waiting);
		
		objService.getRequestRuleURLContent(previewRequestInfo.getPlaceID());
	}

	
	private void callRegistPreviewRequest() {
		
		HTTPService objService = new HTTPService(new HTTPServiceCallback() {
			@Override
			public void onSucessfully(Object returnData) {
				
				BaseInfo resultInfo = (BaseInfo) returnData;
				
				hideProgressDialog(); // Hide process dialog.
				
				if (resultInfo.getStatus() != 0) {
					try{
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivityContext());
						AlertDialog alertDialog = null; 
						
						builder.setTitle(getString(R.string.register_preview_request_title));
						builder.setMessage(resultInfo.getMessageEnglish());
						builder.setCancelable(false);
						builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								btnRegisterPreviewRequest.setEnabled(true);
							}
						});
						
						alertDialog = builder.create();
						alertDialog.show();
						// Must call show() prior to fetching views
						TextView messageView = (TextView)alertDialog.findViewById(android.R.id.message);
						messageView.setGravity(Gravity.CENTER);

						TextView titleView = (TextView)alertDialog.findViewById(getActivityContext().getResources().getIdentifier("alertTitle", "id", "android"));
						if (titleView != null) {
						    titleView.setGravity(Gravity.CENTER);
						}
					}
					catch(Exception ex){
						ex.printStackTrace();
					}
				}
				else {
					
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivityContext());
					AlertDialog alertDialog = null; 
					
					builder.setTitle(getString(R.string.register_preview_request_title));
					builder.setMessage(getString(R.string.register_preview_request_success));
					builder.setCancelable(false);
					builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Back to CarDetail
							closeActivity();
						}
					});
					
					alertDialog = builder.create();
					alertDialog.show();
					// Must call show() prior to fetching views
					TextView messageView = (TextView)alertDialog.findViewById(android.R.id.message);
					messageView.setGravity(Gravity.CENTER);

					TextView titleView = (TextView)alertDialog.findViewById(getActivityContext().getResources().getIdentifier("alertTitle", "id", "android"));
					if (titleView != null) {
					    titleView.setGravity(Gravity.CENTER);
					}
				}
				
			}

			@Override
			public void onFailed(int errorCode) {
				// Hide process dialog.
				hideProgressDialog();
				
				btnRegisterPreviewRequest.setEnabled(true);

				// Show error content
				catchSystemErrors(errorCode);
			}
		});

		showProgressDialog(R.string.please_waiting);
		
		// SET INFO
		setActivityToPreviewRequestInfo();

		objService.registPreviewRequestContent(previewRequestInfo);
	}
	
	
	/**
	 * catch System Errors
	 * 
	 * @param int errorCode
	 */
	private void catchSystemErrors(int errorCode) {

		switch (errorCode) {
		case HTTPService.ERROR_NETWORK:
			MessageBox.show(getActivityContext(),
					R.string.error_title_webservice,
					R.string.error_message_xml_connect_gateway);
			break;
		case HTTPService.ERROR_XMLPARSER:
			MessageBox.show(getActivityContext(),
					R.string.error_title_webservice,
					R.string.error_message_xml_parser);
			break;
		case HTTPService.ERROR_RETURN_DATA_INVALID:
			MessageBox.show(getActivityContext(),
					R.string.error_title_webservice,
					R.string.error_message_return_data_invalid);
			break;
		case HTTPService.ERROR_SERVER_ERROR:
			MessageBox.show(getActivityContext(),
					R.string.error_title_webservice,
					R.string.error_message_server_error);
			break;
		case HTTPService.ERROR_CANCEL:
			break;
		default:
			MessageBox.show(getActivityContext(),
					R.string.error_title_webservice,
					R.string.error_message_unknow);
			break;
		}
	}
}