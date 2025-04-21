package tokiokaability.auction24.activities;

import java.util.Locale;

import tokiokaability.auction24.R;
import tokiokaability.auction24.RealLuceAuctionApplication;
import tokiokaability.auction24.HTTPInfo.BaseInfo;
import tokiokaability.auction24.HTTPInfo.ItemInfo;
import tokiokaability.auction24.HTTPInfo.HTTPService;
import tokiokaability.auction24.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction24.commons.MessageBox;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class RegisterAbsenceBidActivity extends BaseActivity {

	private EditText txtPrice;
	private Button btnRegister;
	private Button btnUnRegister;
	private TextView lblStartPriceValue;
	private TextView lblScreenTitle;
	private TextView lblPrice;
	private TextView lblYen;
	private TextView lblStartPrice;
	private TextView lblMessageInfo,lblBoxNo;
	private ItemInfo itemInfo;


	// ************************ SCREEN LIFECYCLE ******************
	/*
	 * Initialization
	 */
	@Override
	protected void initialization() {
		setContentView(R.layout.activity_register_absence_bid);
		super.initialization();

		// Get control.
		txtPrice = (EditText) findViewById(R.id.txtPrice);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnUnRegister = (Button) findViewById(R.id.btnUnRegister);
		lblScreenTitle = (TextView) findViewById(R.id.lblScreenTitle);
		lblYen = (TextView) findViewById(R.id.lblYen);
		lblStartPrice = (TextView) findViewById(R.id.lblStartPrice);
		lblMessageInfo = (TextView) findViewById(R.id.lblMessageInfo);
		lblPrice = (TextView) findViewById(R.id.lblPrice);
		lblBoxNo = findViewById(R.id.lblBoxNo);
		// Register listener.
		btnRegister.setOnClickListener(btnRegisterClick);
		btnUnRegister.setOnClickListener(btnUnRegisterClick);

		// Set image for helps.
		this.setHelpResourceId(R.layout.absence_help);

		// Initalize textviews.
		this.intializeTextViews();

		// set iteminfo from activity parameter.
		itemInfo = (ItemInfo) getActivityParam("ItemInfo", new ItemInfo());

		// Set OnEditorAction
		txtPrice.setOnEditorActionListener(txtPriceAction);

		// Get value from iteminfo object to screen.
		String value = itemInfo.getAbsencePrice();
		lblBoxNo.setText(itemInfo.getBoxNo()+"-"+itemInfo.getBoxSubNo());
		txtPrice.setText((value == null) ? "" : value);
		txtPrice.setSelection(txtPrice.getText().length());
		txtPrice.requestFocus();

		String startPrice = itemInfo.getStartPrice();
		try {
			// カンマ3桁区切りに変換
			startPrice = String.format("%,d", Long.parseLong(startPrice));
		}catch(Exception ex){

		}
		lblStartPriceValue.setText(String.format("%s %s",
				this.getString(R.string.register_absence_bid_money_yen),
				startPrice));
		if(Build.VERSION.SDK_INT!=Build.VERSION_CODES.KITKAT)setFont();
//		lblYen.setVisibility(RealLuceAuctionApplication.allPlaces.get(0).getAuctionStartFlg().equals("0")?View.GONE:View.VISIBLE);
//        txtPrice.setVisibility(RealLuceAuctionApplication.allPlaces.get(0).getAuctionStartFlg().equals("0")?View.GONE:View.VISIBLE);
//        lblPrice.setVisibility(RealLuceAuctionApplication.allPlaces.get(0).getAuctionStartFlg().equals("0")?View.GONE:View.VISIBLE);
//        lblScreenTitle.setText(RealLuceAuctionApplication.allPlaces.get(0).getAuctionStartFlg().equals("0")?
//                                getString(R.string.register_absence_bid_title_nobid):getString(R.string.register_absence_bid_title));
//        lblStartPrice.setText(RealLuceAuctionApplication.allPlaces.get(0).getAuctionStartFlg().equals("0")?
//                getString(R.string.register_absence_bid_start_price_nobid):getString(R.string.register_absence_bid_start_price));
//        lblMessageInfo.setText(RealLuceAuctionApplication.allPlaces.get(0).getAuctionStartFlg().equals("0")?
//                getString(R.string.register_absence_bid_contents_nobid):getString(R.string.register_absence_bid_contents));
	}

	private void setFont(){
		AssetManager am = getApplicationContext().getAssets();
		Typeface yGR = Typeface.createFromAsset(am,"fonts/YUGOTHR.TTC");
		Typeface yGB = Typeface.createFromAsset(am,"fonts/YUGOTHB.TTC");
		lblScreenTitle.setTypeface(yGB);
		lblPrice.setTypeface(yGB);
		lblMessageInfo.setTypeface(yGR);
		lblStartPrice.setTypeface(yGB);
		lblYen.setTypeface(yGB);
		lblStartPriceValue.setTypeface(yGB);
		txtPrice.setTypeface(yGR);
		btnRegister.setTypeface(yGB);
		btnUnRegister.setTypeface(yGB);
		lblBoxNo.setTypeface(yGB);
	}

	private void intializeTextViews() {
		this.getTextView(R.id.lblScreenTitle);
		this.getTextView(R.id.lblPrice);
		this.getTextView(R.id.lblMoneyUnit);
		this.getTextView(R.id.lblStartPrice);
		lblStartPriceValue = this.getTextView(R.id.lblStartPriceValue);
		this.getTextView(R.id.lblMessageInfo);
	}

	/*
	 * Get Textview from resouceId and set font for textview.
	 */
	private TextView getTextView(int resourceId) {
		TextView textView = (TextView) findViewById(resourceId);

		// If textView is null, return null;
		if (textView == null)
			return null;

		return textView;
	}

	// ************************ VALIDATION ***************************
	/*
	 * Validate to Register
	 */
	private boolean validateBeforeRegister() {

		
		/*
		String input = txtPrice.getText().toString().trim();

		// If input is empty, display error message.
		if (input.length() == 0) {
			MessageBox.show(getActivityContext(),
					R.string.register_absence_bid_error_message_input_price_title,
					R.string.register_absence_bid_message_no_input_price);

			return false;
		} else {
			int value = Integer.parseInt(input);

			// if input value == 0, display error message.
			if (value < 1) {
				MessageBox
						.show(getActivityContext(),
								R.string.register_absence_bid_error_message_input_price_title,
								R.string.register_absence_bid_message_input_price_greater_than_zero);

				return false;
			}
		}
		*/

		return true;
	}

	// ************************ BUSINESS LOGIC PROCESSING ****************
	/*
	 * Register
	 * 
	 * @ registerMode: register mode. 1: Register mode. Other: Unregister mode.
	 */
	private void registAbsenceBid(final int registerMode) {
		HTTPService httpService = new HTTPService(new HTTPServiceCallback() {

			@Override
			public void onSucessfully(Object returnData) {

				BaseInfo resultInfo = (BaseInfo) returnData;

				// Hidden progress dialog.
				hideProgressDialog();

				if (resultInfo.getStatus() != 0) {

					// Set enable controls register, unregister.
					setEnableControls(true);

					/*
					MessageBox.show(getActivityContext(),
							getString(R.string.register_absence_bid_title),
							resultInfo.getMessage());
							*/

					SharedPreferences settings = getSharedPreferences(RealLuceAuctionApplication.SETTINGS, MODE_PRIVATE);
					String lang = settings.getString(RealLuceAuctionApplication.SETTINGS_VIEW_LANGUAGE, Locale.getDefault().getLanguage());
					String langKbn = lang.equals("ja") ? "0" : "1";
					if(langKbn.equals("0")) {
						MessageBox.show(getActivityContext(),
								getString(R.string.register_absence_bid_title_error),
								resultInfo.getMessage());
					}
					else {
						MessageBox.show(getActivityContext(),
								getString(R.string.register_absence_bid_title_error),
								resultInfo.getMessage());
					}
				} else {
					MessageBox
							.show(getActivityContext(),
									R.string.register_absence_bid_title,
									(registerMode != 2) ? R.string.register_absence_bid_message_register_success
											: R.string.register_absence_bid_message_un_register_success,
									new OnDismissListener() {

										@Override
										public void onDismiss(
												DialogInterface dialog) {
											RealLuceAuctionApplication.fuzaiOffsetLst.add(RealLuceAuctionApplication.detailOffset);
											RealLuceAuctionApplication.fuzaiLst.add(registerMode != 2? txtPrice.getText().toString().trim():"0");
											closeActivity();
										}
									});
				}
			}

			@Override
			public void onFailed(int errorCode) {

				// Hidden progress dialog.
				hideProgressDialog();

				// Set enable controls register, unregister.
				setEnableControls(true);

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
		});

		// Show progress
		showProgressDialog(R.string.please_waiting);

		String absensePrice = "0";

		// If regsiter mode is register, set absensePrice = value of txtPrice.
		if (registerMode == 1) {
			absensePrice = txtPrice.getText().toString().trim();
		}
		else if(registerMode == 3){
			absensePrice = itemInfo.getAskingPrice();
		}
		else absensePrice = "-1";

		httpService.registerBidOffline(itemInfo.getPlaceId(),
				itemInfo.getTimesNo(), String.valueOf(itemInfo.getItemNo()),
				RealLuceAuctionApplication.loginID, absensePrice);
	}

	/*
	 * Set enable/disable for btnRegister, unregister.
	 */
	private void setEnableControls(boolean enableMode) {
		btnRegister.setEnabled(enableMode);
		btnUnRegister.setEnabled(enableMode);
	}

	/*
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

	// ************************ LISTENER ****************
	/*
	 * Call when register button clicked.
	 */
	public OnClickListener btnRegisterClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(txtPrice.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
			// Validate OK, display confirm messagebox.
			if (validateBeforeRegister()) {
				// Show confirm dialog
				confirmMessage(R.string.register_absence_bid_message_register_success_title,
						R.string.register_absence_bid_message_confirm_register,
						getString(R.string.register_absence_bid_ok),
						getString(R.string.register_absence_bid_cancel),
						okButtonOfRegisterConfirmClick, cancelClick);
			}
		}
	};

	/*
	 * Call when unregister button clicked.
	 */
	public OnClickListener btnUnRegisterClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(txtPrice.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
			
			confirmMessage(R.string.register_absence_bid_message_un_register_success_title,
					R.string.register_absence_bid_message_confirm_un_register,
					getString(R.string.register_absence_bid_ok),
					getString(R.string.register_absence_bid_cancel),
					okButtonOfUnRegisterConfirmClick, cancelClick);
		}
	};

	/*
	 * Call when click ok button of confim message of reigster button.
	 */
	public DialogInterface.OnClickListener okButtonOfRegisterConfirmClick = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// Call register with price get from txtprice.
			registAbsenceBid(1);
		}
	};

	/*
	 * Call when click ok button of confim message of Unreigster button.
	 */
	public DialogInterface.OnClickListener okButtonOfUnRegisterConfirmClick = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// Call registAbsenceBid with value 0 to unregister.
			registAbsenceBid(2);
		}
	};

	/*
	 * Close confirm dialog.
	 */
	public DialogInterface.OnClickListener cancelClick = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}
	};

	/*
	 * Call event when click go button on Numeric keypad.
	 */
	private OnEditorActionListener txtPriceAction = new OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(txtPrice.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);

			// If Data is valid call bussiness process.
			if (validateBeforeRegister()) {
				// Show confirm dialog
				confirmMessage(R.string.register_absence_bid_message_un_register_success_title,
						R.string.register_absence_bid_message_confirm_register,
						getString(R.string.register_absence_bid_ok),
						getString(R.string.register_absence_bid_cancel),
						okButtonOfRegisterConfirmClick, cancelClick);
			}
			return true;
		}
	};
}