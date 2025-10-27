package tokiokaability.auction24.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tokiokaability.auction24.HTTPInfo.BaseInfo;
import tokiokaability.auction24.HTTPInfo.HTTPService;
import tokiokaability.auction24.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction24.HTTPInfo.InquiryInfo;
import tokiokaability.auction24.HTTPInfo.ItemInfo;
import tokiokaability.auction24.R;
import tokiokaability.auction24.RealLuceAuctionApplication;
import tokiokaability.auction24.commons.MessageBox;

public class ContactActivity extends BaseActivity {
    private InputMethodManager m_inputMethodManager = null;
    private RelativeLayout m_mainLayout = null;
    private EditText m_txtContact = null;
    private EditText m_txtAnswer = null;
    private Button m_btnSubmit = null;
    private Button m_btnCancel = null;
    private TextView m_lblScreenTitle = null;
    private TextView m_lblContact = null;
    private TextView m_lblNote = null;
    private TextView m_lblAnswer = null;
    private TextView m_lblUpdateDate = null;
    private TextView m_lblUpdateDateValue = null;
    private TextView m_lblBoxNo = null;

    private String m_boxNo = null;
    private String m_boxSubNo = null;
    private String m_placeId = null;
    private String m_sessionNo = null;
    private String m_preInputQuestion = null;



   /* // Timer Test
    private TextView m_lblTimer = null;
    private long m_startTime = 0;
    private final Handler m_handler = new Handler(Looper.getMainLooper());
    private volatile boolean m_stopRun = false;
    private final SimpleDateFormat m_dataFormat =
            new SimpleDateFormat("mm:ss.SS", Locale.JAPAN);
    private long m_targetTime = 15 * 1000;
    private long m_nextTargetTime = m_targetTime;*/

    // ************************ SCREEN LIFECYCLE ******************
    /*
     * Initialization
     */
    @Override
    protected void initialization() {
        //m_startTime = System.currentTimeMillis();

        setContentView(R.layout.activity_contact);
        super.initialization();
        setStatusBar();
        // Get control.
        m_mainLayout = (RelativeLayout)findViewById(R.id.contactLayout);
        m_inputMethodManager =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        m_txtContact = (EditText) findViewById(R.id.txtContact);
        m_txtAnswer = (EditText) findViewById(R.id.txtAnswer);
        m_btnSubmit = (Button) findViewById(R.id.btnSubmit);
        m_btnCancel = (Button) findViewById(R.id.btnCancel);
        m_lblScreenTitle = (TextView) findViewById(R.id.lblScreenTitle);
        m_lblContact = (TextView) findViewById(R.id.lblContact);
        m_lblNote = (TextView) findViewById(R.id.lblNote);
        m_lblAnswer = (TextView) findViewById(R.id.lblAnswer);
        m_lblUpdateDate = (TextView) findViewById(R.id.lblUpdateDate);
        m_lblUpdateDateValue = (TextView) findViewById(R.id.lblUpdateDateValue);
        m_lblBoxNo = (TextView) findViewById(R.id.lblBoxNo);

       /* // Timer Test
        m_lblTimer = (TextView) findViewById(R.id.lblTimer);
        m_lblTimer.setText(m_dataFormat.format(m_targetTime));
        m_stopRun = false;*/

        // Submit listener.
        m_btnSubmit.setOnClickListener(btnSubmitClick);
        m_btnCancel.setOnClickListener(btnCancelClick);

        // Set image for helps.
        this.setHelpResourceId(R.layout.absence_help);

        // Initalize textviews.
        this.intializeTextViews();

        // set parameters from activity
        m_boxNo = (String) getActivityParam("boxNo", new ItemInfo());
        m_boxSubNo = (String) getActivityParam("boxSubNo", new ItemInfo());
        m_placeId = (String) getActivityParam("placeId", null);
        m_sessionNo = (String) getActivityParam("sessionNo", null);

        // Get value from iteminfo object to screen.
        m_lblBoxNo.setText(m_boxNo + "-" + m_boxSubNo);
        // Get InquiryInfo from server
        this.getInquiryInfo();

        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) setFont();
//		lblYen.setVisibility(RealLuceAuctionApplication.allPlaces.get(0).getAuctionStartFlg().equals("0")?View.GONE:View.VISIBLE);
//        txtPrice.setVisibility(RealLuceAuctionApplication.allPlaces.get(0).getAuctionStartFlg().equals("0")?View.GONE:View.VISIBLE);
//        m_lblContact.setVisibility(RealLuceAuctionApplication.allPlaces.get(0).getAuctionStartFlg().equals("0")?View.GONE:View.VISIBLE);
//        lblScreenTitle.setText(RealLuceAuctionApplication.allPlaces.get(0).getAuctionStartFlg().equals("0")?
//                                getString(R.string.register_absence_bid_title_nobid):getString(R.string.register_absence_bid_title));
//        lblStartPrice.setText(RealLuceAuctionApplication.allPlaces.get(0).getAuctionStartFlg().equals("0")?
//                getString(R.string.register_absence_bid_start_price_nobid):getString(R.string.register_absence_bid_start_price));
//        lblMessageInfo.setText(RealLuceAuctionApplication.allPlaces.get(0).getAuctionStartFlg().equals("0")?
//                getString(R.string.register_absence_bid_contents_nobid):getString(R.string.register_absence_bid_contents));
    }

    /*// Timer Test
    @Override
    public void run() {
        // 10 msec order
        int period = 10;

        while (!m_stopRun) {
            // sleep: period msec
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                e.printStackTrace();
                m_stopRun = true;
            }

            m_handler.post(new Runnable() {
                @Override
                public void run() {
                    long endTime = System.currentTimeMillis();
                    // カウント時間 = 経過時間 - 開始時間
                    long diffTime = (endTime - m_startTime);
                    // 表示時間 = ターゲット時間 - カウント時間
                    long setTime = Math.max(m_targetTime - diffTime, 0);
                    m_lblTimer.setText(m_dataFormat.format(setTime));
                    if (setTime == 0) {
                        back();
                    }
                }
            });
        }
    }*/
    @Override
    public void onPause() {
       /* if(!m_stopRun) {
            m_stopRun = true;
            long diffTime = (System.currentTimeMillis() - m_startTime);
            m_nextTargetTime = Math.max(m_targetTime - diffTime, 0);
        }*/

        super.onPause();
    }

    @Override
    public void onResume() {
        /*if(m_stopRun) {
            m_stopRun = false;
            m_targetTime = m_nextTargetTime;
            startTimerThread();
        }*/
        super.onResume();
    }

    private void setFont() {
        AssetManager am = getApplicationContext().getAssets();
        Typeface yGR = Typeface.createFromAsset(am, "fonts/YUGOTHR.TTC");
        Typeface yGB = Typeface.createFromAsset(am, "fonts/YUGOTHB.TTC");

        m_txtContact.setTypeface(yGR);
        m_txtAnswer.setTypeface(yGR);
        m_btnSubmit.setTypeface(yGB);
        m_btnCancel.setTypeface(yGB);
        m_lblBoxNo.setTypeface(yGB);
        m_lblScreenTitle.setTypeface(yGB);
        m_lblContact.setTypeface(yGB);
        m_lblAnswer.setTypeface(yGB);
    }

    private void intializeTextViews() {
        this.getTextView(R.id.lblContact);
        this.getTextView(R.id.lblNote);
        this.getTextView(R.id.lblAnswer);
        this.getTextView(R.id.lblUpdateDate);
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

    /*
     * Set EditText from InquiryInfo
     */
    private void setInquiryValues(InquiryInfo inquiryInfo) {
        if (inquiryInfo == null) {
            this.m_txtContact.setText(null);
            this.m_txtAnswer.setText(null);
            this.m_lblUpdateDate.setText(null);
            this.m_lblUpdateDateValue.setText(null);
            return;
        }

        m_preInputQuestion = inquiryInfo.getQuestion();
        this.m_txtContact.setText(m_preInputQuestion);
        String answer = inquiryInfo.getAnswer();
        this.m_txtAnswer.setText(answer);
        String answerDate = inquiryInfo.getAnswerDate();
        if (!answer.isEmpty() && !answerDate.isEmpty()) {
            this.m_lblUpdateDate.setText(getText(R.string.contact_update_date));
            this.m_lblUpdateDateValue.setText(answerDate);
        } else {
            this.m_lblUpdateDate.setText(null);
            this.m_lblUpdateDateValue.setText(null);
        }

    }

    // ************************ LOAD FROM SERVER ***************************

    /**
     * Load inquiry info from server
     */
    private void getInquiryInfo() {

        HTTPService objService = new HTTPService(new HTTPServiceCallback() {

            @Override
            public void onSucessfully(Object returnData) {
                // Hide process dialog.
                hideProgressDialog();
                // Get Inquiry information
                InquiryInfo inquiryInfo = (InquiryInfo) returnData;
                setInquiryValues(inquiryInfo);
                //startTimerThread();
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

        String sellNo = null;
        try {
            // 2桁へ変換
            sellNo = m_boxNo + String.format("%02d", Integer.parseInt(m_boxSubNo));
        } catch (Exception ignored) {

        }
        objService.getInquiry(RealLuceAuctionApplication.loginID, m_placeId, m_sessionNo, sellNo);
    }

   /* // Timer Test
    private void startTimerThread(){
        m_startTime = System.currentTimeMillis();
        Thread thread = new Thread(this);
        thread.start();
    }*/

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

    // ************************ BUSINESS LOGIC PROCESSING ****************
    /*
     * Submit
     *
     */
    private void submitInquiry() {
        HTTPService httpService = new HTTPService(new HTTPServiceCallback() {

            @Override
            public void onSucessfully(Object returnData) {

                BaseInfo resultInfo = (BaseInfo) returnData;

                // Hidden progress dialog.
                hideProgressDialog();

                if (resultInfo.getStatus() != 0) {
                    // Set enable controls register, unregister.
                    setEnableControls(true);

                    MessageBox
                            .show(getActivityContext(),
                                    R.string.contact_message_title,
                                    R.string.contact_message_confirm_submit_failed,
                                    new OnDismissListener() {
                                        @Override
                                        public void onDismiss(
                                                DialogInterface dialog) {
                                        }
                                    });
                } else {
                    MessageBox
                            .show(getActivityContext(),
                                    R.string.contact_message_title,
                                    R.string.contact_message_confirm_submit_success,
                                    new OnDismissListener() {

                                        @Override
                                        public void onDismiss(
                                                DialogInterface dialog) {
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

        String sellNo = null;
        try {
            // 2桁へ変換
            sellNo = m_boxNo + String.format("%02d", Integer.parseInt(m_boxSubNo));
        } catch (Exception ignored) {

        }

        httpService.submitInquiry(RealLuceAuctionApplication.loginID, m_placeId, m_sessionNo, sellNo, m_txtContact.getText().toString().trim());
    }

    /*
     * Set enable/disable for m_btnSubmit, unregister.
     */
    private void setEnableControls(boolean enableMode) {
        m_btnSubmit.setEnabled(enableMode);
        m_btnCancel.setEnabled(enableMode);
    }

    private void backProcessing() {
        if (m_txtContact == null) {
            return;
        }

        if (m_txtContact.getText().toString().equals(m_preInputQuestion)) {
            back();
            return;
        }

        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(m_txtContact.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

        confirmMessage(R.string.contact_message_title,
                R.string.contact_message_confirm_back,
                getString(R.string.register_absence_bid_ok),
                getString(R.string.register_absence_bid_cancel),
                okButtonOfCancelClick, cancelClick);
    }

    private void back() {
        // m_stopRun = true;
        super.onBackPressed();
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
        TextView messageView = (TextView) alert.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);

        TextView titleView = (TextView) alert.findViewById(this.getResources().getIdentifier("alertTitle", "id", "android"));
        if (titleView != null) {
            titleView.setGravity(Gravity.CENTER);
        }
    }

    // ************************ LISTENER ****************
    /*
     * Call when register button clicked.
     */
    public OnClickListener btnSubmitClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (m_txtContact == null) {
                return;
            }
            if (m_txtContact.getText().toString().trim().isEmpty()) {
                MessageBox.show(getActivityContext(),
                        getString(R.string.contact_message_title),
                        getString(R.string.contact_message_confirm_submit_notext_error));
                return;
            }

            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(m_txtContact.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

            // Display confirm messagebox.
            // Show confirm dialog
            confirmMessage(R.string.contact_message_title,
                    R.string.contact_message_confirm_submit,
                    getString(R.string.contact_ok),
                    getString(R.string.contact_cancel),
                    okButtonOfContactSubmitClick, cancelClick);
        }
    };

    /*
     * Call when unregister button clicked.
     */
    public OnClickListener btnCancelClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            backProcessing();
        }
    };

    /*
     * Call when click ok button of confim message of reigster button.
     */
    public DialogInterface.OnClickListener okButtonOfContactSubmitClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // Call Submit with the EditText for Contact.
            submitInquiry();
        }
    };

    /*
     * Call when click ok button of confim message of Unreigster button.
     */
    public DialogInterface.OnClickListener okButtonOfCancelClick = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            back();
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

    @Override
    public void onBackPressed() {
        backProcessing();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //キーボードを隠す
        m_inputMethodManager.hideSoftInputFromWindow(m_mainLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        //背景にフォーカスを移す
        m_mainLayout.requestFocus();

        return false;
    }

}