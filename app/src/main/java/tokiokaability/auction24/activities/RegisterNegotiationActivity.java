package tokiokaability.auction24.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import tokiokaability.auction24.HTTPInfo.BaseInfo;
import tokiokaability.auction24.HTTPInfo.HTTPService;
import tokiokaability.auction24.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction24.HTTPInfo.ItemInfo;
import tokiokaability.auction24.HTTPInfo.NegotiationInfo;
import tokiokaability.auction24.R;
import tokiokaability.auction24.RealLuceAuctionApplication;
import tokiokaability.auction24.commons.MessageBox;
import tokiokaability.auction24.commons.Utilities;

public class RegisterNegotiationActivity extends BaseActivity {

	private RelativeLayout blockJisha, blockRaku,blockSaikosho,blockMemo;
	private TextView lblSellNo, lblLastBidPrice, lblAskingPrice,
			lblHoldPrice,lblHoldPrice2,lblReholdPrice,
			lblDescrip1,lblLastBidPrice2,lblDescrip2,lblFuzai,
			memoTitle,memoContent;
	private EditText txtHoldPrice,txtReHoldPrice;
	private Button btnHikiByJisha, btnSonomamaRaku, btnKosho, btnHikiByRaku, btnRaku,btnSaikosho,btnHikiSaikosho,btnRakuSaikosho,btnFastKosho;

	private int searchMode;
	private ItemInfo itemInfo;
	private NegotiationInfo negotiationInfo;
	private CheckBox chkHoldFlg;
	public AssetManager am ;
	public Typeface yGR,yGB;

	// ************************ SCREEN LIFECYCLE ******************

	/**
	 * Initialization
	 */
	@Override
	protected void initialization() {
		setContentView(R.layout.activity_register_negotiation);
		super.initialization();
		setStatusBar();
		// Get control.
		blockJisha = (RelativeLayout) findViewById(R.id.blockJisha);
		blockRaku = (RelativeLayout) findViewById(R.id.blockRaku);
		this.getTextView(R.id.lblScreenTitle);
		lblSellNo = (TextView) findViewById(R.id.lblSellNo);
		lblLastBidPrice = (TextView) findViewById(R.id.lblLastBidPrice);
		lblAskingPrice = (TextView) findViewById(R.id.lblAskingPrice);
		txtHoldPrice = (EditText) findViewById(R.id.txtHoldPrice);
		lblHoldPrice = (TextView) findViewById(R.id.lblHoldPrice);
		btnHikiByJisha = (Button) findViewById(R.id.btnHikiByJisha);
		btnSonomamaRaku = (Button) findViewById(R.id.btnSonomamaRaku);
		btnKosho = (Button) findViewById(R.id.btnKosho);
		btnHikiByRaku = (Button) findViewById(R.id.btnHikiByRaku);
		btnRaku = (Button) findViewById(R.id.btnRaku);
		chkHoldFlg = findViewById(R.id.chkBoxHoldFlg);
		// Register listener.
		btnHikiByJisha.setOnClickListener(btnHikiClick);
		btnSonomamaRaku.setOnClickListener(btnRakuClick);
		btnKosho.setOnClickListener(btnKoshoClick);
		btnHikiByRaku.setOnClickListener(btnHikiClick);
		btnRaku.setOnClickListener(btnRaku2Click);
		btnSaikosho = findViewById(R.id.btnSaiKosho);
		btnSaikosho.setOnClickListener(btnSaikoshoClick);
		btnHikiSaikosho = findViewById(R.id.btnHikiSaikosho);
		btnHikiSaikosho.setOnClickListener(btnHikiClick);
		btnRakuSaikosho = findViewById(R.id.btnRakuSaikosho);
		btnRakuSaikosho.setOnClickListener(btnRaku3Click);
		blockSaikosho = findViewById(R.id.blockSaikosho);
		lblHoldPrice2 = findViewById(R.id.lblHoldPrice2);
		lblReholdPrice = findViewById(R.id.lblReHoldPrice);
		txtReHoldPrice = findViewById(R.id.txtReHoldPrice);
		lblDescrip1 = findViewById(R.id.lblDescrip1);
		lblDescrip2 = findViewById(R.id.lblDescrip2);
		lblLastBidPrice2 = findViewById(R.id.lblLastBidPrice2);
		lblFuzai = findViewById(R.id.lblFuzai);
		memoTitle = findViewById(R.id.memoTitle);
		memoContent=findViewById(R.id.memoContent);
		blockMemo = findViewById(R.id.blockMemo);
		btnFastKosho = findViewById(R.id.btnFastKosho);
		btnFastKosho.setOnClickListener(btnFastKoshoClick);
		memoContent.setMovementMethod(new ScrollingMovementMethod());
		// Set OnEditorAction
		// txtPrice.setOnEditorActionListener(txtPriceAction);

		// ActivityParamから値を取得
		initializeParameter();
		// 表示・非表示を設定
		//initializeVisibleControls();
		// 保留交渉情報を取得
		getNegotiationInfo();
		// 初期値を設定
		initializeText();
		am = getApplicationContext().getAssets();
		yGB = Typeface.createFromAsset(am,"fonts/YUGOTHB.TTC");
		yGR = Typeface.createFromAsset(am,"fonts/YUGOTHR.TTC");
		setFonts();
	}

	void setFonts(){
		if(Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT){
			lblSellNo.setTypeface(yGB);
			lblAskingPrice.setTypeface(yGB);
			lblHoldPrice.setTypeface(yGB);
			lblLastBidPrice.setTypeface(yGB);
			btnHikiByRaku.setTypeface(yGB);
			btnRaku.setTypeface(yGB);
			btnSaikosho.setTypeface(yGB);
			btnSonomamaRaku.setTypeface(yGB);
			btnKosho.setTypeface(yGB);
			btnHikiSaikosho.setTypeface(yGB);
			btnRakuSaikosho.setTypeface(yGB);
			lblHoldPrice2.setTypeface(yGB);
			lblReholdPrice.setTypeface(yGB);
			lblDescrip1.setTypeface(yGB);
			lblDescrip2.setTypeface(yGB);
			lblLastBidPrice2.setTypeface(yGB);
			lblFuzai.setTypeface(yGB);
			memoTitle.setTypeface(yGB);
			memoContent.setTypeface(yGR);
			//btnRaku.setTypeface(yGB);
		}
	}

	/**
	 * ActivityParamから値を取得
	 */
	private void initializeParameter() {
		searchMode = (Integer) getActivityParam("SearchMode", 8);
		itemInfo = (ItemInfo) getActivityParam("ItemInfo", new ItemInfo());
	}

	/**
	 * 表示・非表示を設定
	 */
	private void initializeVisibleControls() {
		if (searchMode == 2) {
			blockJisha.setVisibility(View.VISIBLE);
			lblAskingPrice.setVisibility(View.VISIBLE);
			blockRaku.setVisibility(View.GONE);
		} else {
			blockJisha.setVisibility(View.GONE);
            lblAskingPrice.setVisibility(View.GONE);
			blockRaku.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 初期値を設定
	 */
	private void initializeText() {
		// 箱番枝番
		lblSellNo.setText(itemInfo.getBoxNo() + "-" + itemInfo.getBoxSubNo());
	}

	/**
	 * Get Textview from resouceId and set font for textview.
	 *
	 * @param resourceId
	 * @return
	 */
	private TextView getTextView(int resourceId) {
		TextView textView = (TextView) findViewById(resourceId);

		// If textView is null, return null;
		if (textView == null)
			return null;

		return textView;
	}

	// ************************ VALIDATION ***************************

	/**
	 * Validate to Register
	 *
	 * @return
	 */
	private boolean validateBeforeRegister(int type) {
		String holdPrice = txtHoldPrice.getText().toString().trim();
		String reholdPrice = txtReHoldPrice.getText().toString().trim();
		if(type == 1){
			if (holdPrice.equals("") || holdPrice.equals("0")) {
				MessageBox.show(getActivityContext(),
						getActivityContext().getString(R.string.register_negotiation_message_title),
						getActivityContext().getString(R.string.register_negotiation_message_no_holdPrice));
				return false;
			}
			else if(Integer.parseInt(holdPrice) <= Integer.parseInt(negotiationInfo.getLastPrice())){
				MessageBox.show(getActivityContext(),
						getActivityContext().getString(R.string.register_negotiation_message_title),
						getActivityContext().getString(R.string.register_negotiation_message_holdPrice_check));
				return false;
			}
			else if(Integer.parseInt(holdPrice) > Integer.parseInt(negotiationInfo.getAskingPrice())){
				MessageBox.show(getActivityContext(),
						getActivityContext().getString(R.string.register_negotiation_message_title),
						getActivityContext().getString(R.string.register_negotiation_message_holdPrice_check2));
				return false;
			}
		}
		else{
			if (reholdPrice.equals("") || reholdPrice.equals("0")) {
				MessageBox.show(getActivityContext(),
						getActivityContext().getString(R.string.register_negotiation_message_title),
						getActivityContext().getString(R.string.register_negotiation_message_no_holdPrice));
				return false;
			}
			else if(Integer.parseInt(reholdPrice) <= Integer.parseInt(negotiationInfo.getLastPrice())){
				MessageBox.show(getActivityContext(),
						getActivityContext().getString(R.string.register_negotiation_message_title),
						getActivityContext().getString(R.string.register_negotiation_message_holdPrice_check));
				return false;
			}
			else if(Integer.parseInt(reholdPrice) >= Integer.parseInt(negotiationInfo.getHoldPrice())){
				MessageBox.show(getActivityContext(),
						getActivityContext().getString(R.string.register_negotiation_message_title),
						getActivityContext().getString(R.string.register_negotiation_message_holdPrice_check3));
				return false;
			}
		}
		return true;
	}

	// ************************ BUSINESS LOGIC PROCESSING ****************

	/**
	 * 保留交渉情報を取得
	 */
	private void getNegotiationInfo() {
		HTTPService httpService = new HTTPService(new HTTPServiceCallback() {

			@Override
			public void onSucessfully(Object returnData) {

				negotiationInfo = (NegotiationInfo) returnData;

				if(negotiationInfo != null){
					// 最終応札価格
					String lastBidPrice = String.valueOf(negotiationInfo.getLastPrice());

					// 指値
					String askingPrice = String.valueOf(negotiationInfo.getAskingPrice());
					lblAskingPrice.setText(String.format("%s%s",
							getActivityContext().getString(R.string.register_negotiation_askingPrice_title),
							Utilities.formatPrice(getActivityContext(), askingPrice)));

					// 交渉価格
					if (searchMode == 2) {
						txtHoldPrice.requestFocus();
						chkHoldFlg.setChecked(negotiationInfo.getHoldFlg().equals("1"));
					} else {
						lblHoldPrice.setText(Utilities.formatPrice(getActivityContext(), negotiationInfo.getHoldPrice()));
					}
					String reholdPrice = negotiationInfo.getReholdPrice();
					if (searchMode == 2) {
						if(reholdPrice == null||reholdPrice.length() == 0||reholdPrice.equals("0")){
							blockJisha.setVisibility(View.VISIBLE);
							lblDescrip1.setVisibility(View.VISIBLE);
							lblLastBidPrice.setVisibility(View.GONE);
							lblAskingPrice.setVisibility(View.VISIBLE);
							blockRaku.setVisibility(View.GONE);
							blockSaikosho.setVisibility(View.GONE);
						}
						else{
							lblDescrip1.setVisibility(View.GONE);
							blockJisha.setVisibility(View.GONE);
							lblAskingPrice.setVisibility(View.VISIBLE);
							blockRaku.setVisibility(View.GONE);
							blockSaikosho.setVisibility(View.VISIBLE);
							lblLastBidPrice.setVisibility(View.VISIBLE);
							lblLastBidPrice.setText(String.format("%s%s",
									getActivityContext().getString(R.string.register_negotiation_lastBidPrice_title),
									Utilities.formatPrice(getActivityContext(), lastBidPrice)));
						}

					} else {
						lblDescrip1.setVisibility(View.GONE);
						blockJisha.setVisibility(View.GONE);
						lblAskingPrice.setVisibility(View.GONE);
						blockRaku.setVisibility(View.VISIBLE);
						blockSaikosho.setVisibility(View.GONE);
						lblLastBidPrice.setVisibility(View.VISIBLE);
						lblLastBidPrice.setText(String.format("%s%s",
								getActivityContext().getString(R.string.register_negotiation_lastBidPrice_title2),
								Utilities.formatPrice(getActivityContext(), lastBidPrice)));
					}
				}
				lblHoldPrice2.setText(String.format(getActivityContext().getString(R.string.saikosho_holdprice),
						Utilities.formatPrice(getActivityContext(),negotiationInfo.getHoldPrice())));
				lblReholdPrice.setText(String.format(getActivityContext().getString(R.string.saikosho_reholdprice),
						Utilities.formatPrice(getActivityContext(),negotiationInfo.getReholdPrice())));
				lblLastBidPrice2.setText(String.format(getActivityContext().getString(R.string.neg_lastbidprice2),Utilities.formatPrice(getActivityContext(),negotiationInfo.getLastPrice())));
				hideProgressDialog();
				lblFuzai.setText(String.format(getActivityContext().getString(R.string.neg_fuzai),Utilities.formatPrice(getActivityContext(),negotiationInfo.getAbsenceBidPrice())));
				memoContent.setText(negotiationInfo.getMemo());
				if(negotiationInfo.getMemo().length() > 0)
					blockMemo.setVisibility(View.VISIBLE);
			}

			@Override
			public void onFailed(int errorCode) {

				// Hidden progress dialog.
				hideProgressDialog();

				// Set enable controls register, unregister.
				setEnableControls(false);

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

		httpService.getNegotiationInfo(itemInfo.getPlaceId(), itemInfo.getTimesNo(), String.valueOf(itemInfo.getItemNo()));
	}

	/**
	 * セリ結果区分の更新
	 * @param resultFlg 1：保留、2：引き、6：保留後成約
	 */
	private void updateSellingResult(final String resultFlg) {
		HTTPService httpService = new HTTPService(new HTTPServiceCallback() {

			@Override
			public void onSucessfully(Object returnData) {

				BaseInfo resultInfo = (BaseInfo) returnData;

				// Hidden progress dialog.
				hideProgressDialog();

				if (resultInfo.getStatus() == 0) {
					setEnableControls(false);
					RealLuceAuctionApplication.isUpdatedKosho = true;
					// ダイアログを表示
					MessageBox.show(getActivityContext(),
							getString(R.string.register_negotiation_message_title),
							getString(R.string.register_negotiation_message_success),
							new DialogInterface.OnDismissListener() {

								@Override
								public void onDismiss(DialogInterface dialog) {
									closeActivity();
//									setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
//									showActivity(ItemDetailActivity.class);
								}
							});
				}
				else if(resultInfo.getMessage()!= null && resultInfo.getMessage().length() > 0){
					MessageBox.show(getActivityContext(),"",resultInfo.getMessage());
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
		String holdPrice = "";
		String reholdPrice = negotiationInfo.getReholdPrice();
		if(searchMode == 2 && resultFlg.equals("6") && (reholdPrice!= null && reholdPrice.length() > 0 && !reholdPrice.equals("0")))
			holdPrice = reholdPrice;
		if(searchMode == 2 && resultFlg.equals("1"))
		 	holdPrice = txtHoldPrice.getText().toString().trim();
		else if(searchMode == 3){
			holdPrice = negotiationInfo.getHoldPrice();
		}
		httpService.updateSellingResult(itemInfo.getPlaceId(),
				itemInfo.getTimesNo(), String.valueOf(itemInfo.getItemNo()),
				holdPrice,resultFlg.equals("1") && searchMode==2 && chkHoldFlg.isChecked()?"1":"0", resultFlg);
	}

	private void updateReholdPrice() {
		HTTPService httpService = new HTTPService(new HTTPServiceCallback() {

			@Override
			public void onSucessfully(Object returnData) {

				BaseInfo resultInfo = (BaseInfo) returnData;

				// Hidden progress dialog.
				hideProgressDialog();

				if (resultInfo.getStatus() == 0) {
					setEnableControls(false);
					RealLuceAuctionApplication.isUpdatedKosho = true;
					// ダイアログを表示
					MessageBox.show(getActivityContext(),
							getString(R.string.register_negotiation_message_title),
							getString(R.string.register_negotiation_message_success),
							new DialogInterface.OnDismissListener() {

								@Override
								public void onDismiss(DialogInterface dialog) {
									closeActivity();
//									setTransitAnimationType(TRANSIT_ANIMATION_FADEINOUT);
//									showActivity(ItemDetailActivity.class);
								}
							});
				}
				else if(resultInfo.getMessage()!= null && resultInfo.getMessage().length() > 0){
					MessageBox.show(getActivityContext(),"",resultInfo.getMessage());
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
		httpService.updateReholdPrice(itemInfo.getPlaceId(),
				itemInfo.getTimesNo(), String.valueOf(itemInfo.getItemNo()),
				txtReHoldPrice.getText().toString());
	}

	/**
	 * Set enable/disable
	 *
	 * @param enableMode
	 */
	private void setEnableControls(boolean enableMode) {
		btnHikiByJisha.setEnabled(enableMode);
		btnSonomamaRaku.setEnabled(enableMode);
		btnKosho.setEnabled(enableMode);
		btnHikiByRaku.setEnabled(enableMode);
		btnRaku.setEnabled(enableMode);
	}

	/**
	 * Create confirm message. メッセージ用メソッド
	 *
	 * @param titleId
	 * @param messageId
	 * @param okText
	 * @param cancelText
	 * @param okClick
	 * @param cancelClick
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

		createAlertDialog(builder);
	}

	/**
	 * Create confirm message. メッセージ用メソッド フォーマット用
	 * @param titleId
	 * @param messageTxt
	 * @param okText
	 * @param cancelText
	 * @param okClick
	 * @param cancelClick
	 */
	private void confirmMessageByFormat(int titleId, String messageTxt, String okText,
								String cancelText, DialogInterface.OnClickListener okClick,
								DialogInterface.OnClickListener cancelClick) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// Set value.
		builder.setMessage(messageTxt);
		builder.setTitle(titleId);
		builder.setCancelable(false);
		builder.setPositiveButton(okText, okClick);
		builder.setNegativeButton(cancelText, cancelClick);

		createAlertDialog(builder);
	}

	/**
	 * アラートダイアログ生成
	 * @param builder
	 */
	private void createAlertDialog(AlertDialog.Builder builder) {
		// Create dialog and show to screen.
		AlertDialog alert = builder.create();
		alert.show();
		// Must call show() prior to fetching views
		TextView messageView = (TextView) alert.findViewById(android.R.id.message);
		messageView.setGravity(Gravity.CENTER);

		TextView titleView = (TextView) alert.findViewById(this.getResources().getIdentifier("alertTitle", "id", "android"));
		if (titleView != null) {
			titleView.setGravity(Gravity.CENTER);
		}
	}

	/**
	 * 交渉ボタンクリックイベントを設定
	 */
	private void setKoshoClick() {
		// キーボートを閉じる
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(txtHoldPrice.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);

		String holdPrice = Utilities.formatPrice(getActivityContext(), txtHoldPrice.getText().toString().trim());
		if (validateBeforeRegister(1)) {
			// ダイアログを表示
			if(chkHoldFlg.isChecked()){
				confirmMessageByFormat(R.string.register_negotiation_message_title,
						getString(R.string.neg_confirm_chkbox),
						getString(R.string.register_negotiation_ok),
						getString(R.string.register_negotiation_cancel),
						okButtonOfConfirmChkbox, cancelClick);
			}
			else{
				confirmMessageByFormat(R.string.register_negotiation_message_title,
						String.format(getString(R.string.register_negotiation_message_confirm_kosho), holdPrice),
						getString(R.string.register_negotiation_ok),
						getString(R.string.register_negotiation_cancel),
						okButtonOfKoshoClick, cancelClick);
			}

		}
	}

	private void setFastKoshoClick() {
		// キーボートを閉じる
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(txtHoldPrice.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		txtHoldPrice.setText(negotiationInfo.getAskingPrice());
		String holdPrice = Utilities.formatPrice(getActivityContext(), txtHoldPrice.getText().toString().trim());
		if (validateBeforeRegister(1)) {
			// ダイアログを表示
			if(chkHoldFlg.isChecked()){
				confirmMessageByFormat(R.string.register_negotiation_message_title,
						getString(R.string.neg_confirm_chkbox),
						getString(R.string.register_negotiation_ok),
						getString(R.string.register_negotiation_cancel),
						okButtonOfConfirmChkbox, cancelClick);
			}
			else{
				confirmMessageByFormat(R.string.register_negotiation_message_title,
						String.format(getString(R.string.register_negotiation_message_confirm_kosho), holdPrice),
						getString(R.string.register_negotiation_ok),
						getString(R.string.register_negotiation_cancel),
						okButtonOfKoshoClick, cancelClick);
			}
		}
	}


	// ************************ LISTENER ****************

	/**
	 * 引くボタンクリックイベント
	 */
	private OnClickListener btnHikiClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// キーボートを閉じる
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(txtHoldPrice.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);

			// ダイアログを表示
			confirmMessage(R.string.register_negotiation_message_title,
					searchMode == 8?R.string.register_negotiation_message_confirm_hiki:R.string.register_negotiation_message_confirm_hiki2,
					getString(R.string.register_negotiation_ok),
					getString(R.string.register_negotiation_cancel),
					okButtonOfHikiClick, cancelClick);
		}
	};

	private  OnClickListener btnSaikoshoClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(txtReHoldPrice.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);

			String reholdPrice = Utilities.formatPrice(getActivityContext(), txtReHoldPrice.getText().toString().trim());
			if (validateBeforeRegister(2)) {
				// ダイアログを表示
				confirmMessageByFormat(R.string.register_negotiation_message_title,
						String.format(getString(R.string.register_negotiation_message_confirm_saikosho), reholdPrice),
						getString(R.string.register_negotiation_ok),
						getString(R.string.register_negotiation_cancel),
						okButtonOfSaikoshoClick, cancelClick);
			}
		}
	};

	private DialogInterface.OnClickListener okButtonOfSaikoshoClick = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialogInterface, int i) {
			updateReholdPrice();
		}
	};

	/**
	 * そのまま落札ボタン、落札するボタンクリックイベント
	 */
	private OnClickListener btnRakuClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// ダイアログを表示
			confirmMessageByFormat(R.string.register_negotiation_message_title,
					String.format(getActivityContext().getString(R.string.register_negotiation_message_confirm_raku),
							Utilities.formatPrice(getActivityContext(), negotiationInfo.getLastPrice())),
					getString(R.string.register_negotiation_ok),
					getString(R.string.register_negotiation_cancel),
					okButtonOfRakuClick, cancelClick);
		}
	};

	private OnClickListener btnRaku2Click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// ダイアログを表示
			confirmMessageByFormat(R.string.register_negotiation_message_title,
					String.format(getActivityContext().getString(R.string.register_negotiation_message_confirm_raku2),
							Utilities.formatPrice(getActivityContext(), negotiationInfo.getHoldPrice())),
					getString(R.string.register_negotiation_ok),
					getString(R.string.register_negotiation_cancel),
					okButtonOfRakuClick, cancelClick);
		}
	};

	private OnClickListener btnRaku3Click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// ダイアログを表示
			confirmMessageByFormat(R.string.register_negotiation_message_title,
					String.format(getActivityContext().getString(R.string.register_negotiation_message_confirm_raku),
							Utilities.formatPrice(getActivityContext(), negotiationInfo.getReholdPrice())),
					getString(R.string.register_negotiation_ok),
					getString(R.string.register_negotiation_cancel),
					okButtonOfRakuClick, cancelClick);
		}
	};


	/**
	 * 交渉するボタンクリックイベント
	 */
	private OnClickListener btnKoshoClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setKoshoClick();
		}
	};

	private OnClickListener btnFastKoshoClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setFastKoshoClick();
		}
	};

	/**
	 * 引く　OKボタンクリックイベント
	 */
	private DialogInterface.OnClickListener okButtonOfHikiClick = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			updateSellingResult("2");
		}
	};

	/**
	 * 交渉する　OKボタンクリックイベント
	 */
	private DialogInterface.OnClickListener okButtonOfKoshoClick = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			updateSellingResult("1");
		}
	};

	private DialogInterface.OnClickListener okButtonOfConfirmChkbox = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			String holdPrice = Utilities.formatPrice(getActivityContext(), txtHoldPrice.getText().toString().trim());
			confirmMessageByFormat(R.string.register_negotiation_message_title,
					String.format(getString(R.string.register_negotiation_message_confirm_kosho), holdPrice),
					getString(R.string.register_negotiation_ok),
					getString(R.string.register_negotiation_cancel),
					okButtonOfKoshoClick, cancelClick);
		}
	};

	/**
	 * そのまま落札する、落札する　OKボタンクリックイベント
	 */
	private DialogInterface.OnClickListener okButtonOfRakuClick = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			updateSellingResult("6");
		}
	};

	/**
	 * キャンセルボタンクリックイベント
	 */
	private DialogInterface.OnClickListener cancelClick = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}
	};

	/**
	 * Call event when click go button on Numeric keypad.
	 */
	private OnEditorActionListener txtPriceAction = new OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
			setKoshoClick();
			return true;
		}
	};

}