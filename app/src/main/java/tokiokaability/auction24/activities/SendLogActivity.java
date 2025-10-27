package tokiokaability.auction24.activities;

import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import tokiokaability.auction24.HTTPInfo.HTTPService;
import tokiokaability.auction24.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction24.HTTPInfo.SendLogInfo;
import tokiokaability.auction24.R;
import tokiokaability.auction24.RealLuceAuctionApplication;
import tokiokaability.auction24.commons.MessageBox;
import tokiokaability.auction24.commons.Utilities;

public class SendLogActivity extends BaseActivity {
    String infoLog;
    String errorLog;
    Button btnSendLog;
    TextView lblScreenTitle;
    @Override
    protected void initialization() {
        setContentView(R.layout.activity_sendlog);
        super.initialization();
        setStatusBar();
        lblScreenTitle = findViewById(R.id.lblScreenTitle);
        btnSendLog = (Button) findViewById(R.id.btnSendLog);
        btnSendLog.setOnClickListener(btnSendLogClick);
        btnBid.setVisibility(View.GONE);
        btnBid.setEnabled(true);
        btnBid2.setVisibility(View.GONE);
        btnBid2.setEnabled(true);
        if(RealLuceAuctionApplication.allPlaces.get(0).getRealFlag().equals("1")){
            btnBid.setVisibility(View.VISIBLE);
            btnBid2.setVisibility(View.VISIBLE);
        }
        else{
            btnBid.setVisibility(View.GONE);
            btnBid2.setVisibility(View.GONE);
        }
        if(Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT)
            setFont();
    }

    private View.OnClickListener btnSendLogClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MessageBox.showWithTwoButton(getActivityContext(),getString(R.string.sendlog_msg_title),getString( R.string.sendlog_msg_content), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendLog();
                }
            },"OK","Cancel");
        }
    };

    private void sendLog(){
        showProgressDialog(R.string.please_waiting);
        infoLog = Utilities.readLog(getActivityContext(),RealLuceAuctionApplication.getConfiguration("info_log_file"));
        errorLog = Utilities.readLog(getActivityContext(),RealLuceAuctionApplication.getConfiguration("error_log_file"));
        HTTPServiceCallback objServiceCallback = new HTTPServiceCallback() {
            @Override
            public void onSucessfully(Object returnData) {
                hideProgressDialog();
                SendLogInfo sendLogInfo = (SendLogInfo) returnData;
                String result = sendLogInfo.getResult();
                if(result.equals("1")){
                    MessageBox.show(getActivityContext(),R.string.sendlog_success_title,R.string.sendlog_success_content);
                }
                else{
                    MessageBox.show(getActivityContext(),R.string.sendlog_error_title,R.string.sendlog_error_content);
                }
            }

            @Override
            public void onFailed(int errorCode) {
                switch(errorCode){
                    case HTTPService.ERROR_NETWORK:
                        MessageBox.show(getActivityContext(), R.string.error_title_webservice, R.string.error_message_xml_connect_gateway);
                        break;
                    case HTTPService.ERROR_XMLPARSER:
                        MessageBox.show(getActivityContext(),R.string.error_title_webservice, R.string.error_message_xml_parser);
                        break;
                    case HTTPService.ERROR_RETURN_DATA_INVALID:
                        MessageBox.show(getActivityContext(),R.string.error_title_webservice, R.string.error_message_return_data_invalid);
                        break;
                    case HTTPService.ERROR_SERVER_ERROR:
                        MessageBox.show(getActivityContext(),R.string.error_title_webservice, R.string.error_message_server_error);
                        break;
                    case HTTPService.ERROR_CANCEL:
                        break;
                    default:
                        MessageBox.show(getActivityContext(),R.string.error_title_webservice, R.string.error_message_unknow);
                        break;
                }
            }
        };
        HTTPService objService = new HTTPService(objServiceCallback);
        objService.sendLog(RealLuceAuctionApplication.loginID,infoLog,errorLog);
    }

    private void setFont(){
        AssetManager am = getApplicationContext().getAssets();
        Typeface yGB = Typeface.createFromAsset(am,"fonts/YUGOTHB.TTC");
        lblScreenTitle.setTypeface(yGB);
        btnSendLog.setTypeface(yGB);
    }
}
