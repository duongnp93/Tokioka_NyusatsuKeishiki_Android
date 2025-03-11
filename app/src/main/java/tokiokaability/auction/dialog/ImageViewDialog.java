package tokiokaability.auction.dialog;

import tokiokaability.auction.HTTPInfo.HTTPService;
import tokiokaability.auction.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction.HTTPInfo.ImageInfo;
import tokiokaability.auction.R;
import tokiokaability.auction.RealLuceAuctionApplication;
import tokiokaability.auction.commons.MessageBox;
import tokiokaability.auction.commons.Utilities;
import tokiokaability.auction.view.PinchImageView;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;


public class ImageViewDialog extends Dialog {
	private PinchImageView imvImage;
	private Button btnBack;
	private ProgressDialog progressDialog;
	RelativeLayout rlayout;

	//private Dialog dialog;
	private android.view.View.OnClickListener backClickListener;
	private Context context;
	
	
	public ImageViewDialog(Activity activity, android.view.View.OnClickListener backClickListener){
		super(activity);
		this.context = activity.getApplicationContext();
		this.backClickListener = backClickListener;
		this.setCanceledOnTouchOutside(false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		this.setContentView(R.layout.dialog_image_view);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));

		makeProgressDialog();

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.FILL_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        rlayout = (RelativeLayout) findViewById(R.id.layoutParent);

		btnBack = (Button) findViewById(R.id.btnBack);
        if (backClickListener != null){
        	btnBack.setOnClickListener(backClickListener);
        }

        imvImage = (PinchImageView) findViewById(R.id.imvImage);
        if(Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT)
        	setFont();

	}

	private void setFont(){
		AssetManager am = getContext().getAssets();
		Typeface yGB = Typeface.createFromAsset(am,"fonts/YUGOTHB.TTC");
		btnBack.setTypeface(yGB);
	}

	public void show(String sellNo, final String position,String sessionNo){
		show();

		progressDialog.show();
		imvImage.setImageBitmap(null);
		HTTPServiceCallback objServiceCallback = new HTTPServiceCallback() {
			@Override
			public void onSucessfully(Object returnData) {
				ImageInfo imageInfo = (ImageInfo) returnData;
				switch (Integer.parseInt(position)){
					case 1:
						if(imageInfo.getImg1()!=null)
						imvImage.setImageBitmap(BitmapFactory.decodeByteArray(Utilities.rotaledByteArr(imageInfo.getImg1()),0,Utilities.rotaledByteArr(imageInfo.getImg1()).length));
						else
						    imvImage.setImageResource(R.drawable.auction_no_photo);
						progressDialog.hide();
						break;
					case 2:
						if(imageInfo.getImg2()!=null)
						imvImage.setImageBitmap(BitmapFactory.decodeByteArray(Utilities.rotaledByteArr(imageInfo.getImg2()),0,Utilities.rotaledByteArr(imageInfo.getImg2()).length));
                        else
                            imvImage.setImageResource(R.drawable.auction_no_photo);
						progressDialog.hide();
						break;
					case 3:
						if(imageInfo.getImg3()!=null)
						imvImage.setImageBitmap(BitmapFactory.decodeByteArray(Utilities.rotaledByteArr(imageInfo.getImg3()),0,Utilities.rotaledByteArr(imageInfo.getImg3()).length));
                        else
                            imvImage.setImageResource(R.drawable.auction_no_photo);
						progressDialog.hide();
						break;
					case 4:
						if(imageInfo.getImg4()!= null)
						imvImage.setImageBitmap(BitmapFactory.decodeByteArray(Utilities.rotaledByteArr(imageInfo.getImg4()),0,Utilities.rotaledByteArr(imageInfo.getImg4()).length));
                        else
                            imvImage.setImageResource(R.drawable.auction_no_photo);
						progressDialog.hide();
						break;
					case 5:
						if(imageInfo.getImg5()!=null)
						imvImage.setImageBitmap(BitmapFactory.decodeByteArray(Utilities.rotaledByteArr(imageInfo.getImg5()),0,Utilities.rotaledByteArr(imageInfo.getImg5()).length));
                        else
                            imvImage.setImageResource(R.drawable.auction_no_photo);
						progressDialog.hide();
						break;
					case 6:
						if(imageInfo.getImg6()!=null)
						imvImage.setImageBitmap(BitmapFactory.decodeByteArray(Utilities.rotaledByteArr(imageInfo.getImg6()),0,Utilities.rotaledByteArr(imageInfo.getImg6()).length));
                        else
                            imvImage.setImageResource(R.drawable.auction_no_photo);
						progressDialog.hide();
						break;
					case 7:
						if(imageInfo.getImg7()!=null)
							imvImage.setImageBitmap(BitmapFactory.decodeByteArray(Utilities.rotaledByteArr(imageInfo.getImg7()),0,Utilities.rotaledByteArr(imageInfo.getImg7()).length));
						else
							imvImage.setImageResource(R.drawable.auction_no_photo);
						progressDialog.hide();
						break;
					case 8:
						if(imageInfo.getImg8()!=null)
							imvImage.setImageBitmap(BitmapFactory.decodeByteArray(Utilities.rotaledByteArr(imageInfo.getImg8()),0,Utilities.rotaledByteArr(imageInfo.getImg8()).length));
						else
							imvImage.setImageResource(R.drawable.auction_no_photo);
						progressDialog.hide();
						break;
					case 9:
						if(imageInfo.getImg9()!=null)
							imvImage.setImageBitmap(BitmapFactory.decodeByteArray(Utilities.rotaledByteArr(imageInfo.getImg9()),0,Utilities.rotaledByteArr(imageInfo.getImg9()).length));
						else
							imvImage.setImageResource(R.drawable.auction_no_photo);
						progressDialog.hide();
						break;
					case 10:
						if(imageInfo.getImg10()!=null)
							imvImage.setImageBitmap(BitmapFactory.decodeByteArray(Utilities.rotaledByteArr(imageInfo.getImg10()),0,Utilities.rotaledByteArr(imageInfo.getImg10()).length));
						else
							imvImage.setImageResource(R.drawable.auction_no_photo);
						progressDialog.hide();
						break;
						default:
							break;
				}

			}

			@Override
			public void onFailed(int errorCode) {
				switch(errorCode){
					case HTTPService.ERROR_NETWORK:
						MessageBox.show(context, R.string.error_title_webservice, R.string.error_message_xml_connect_gateway);
						break;
					case HTTPService.ERROR_XMLPARSER:
						MessageBox.show(context,R.string.error_title_webservice, R.string.error_message_xml_parser);
						break;
					case HTTPService.ERROR_RETURN_DATA_INVALID:
						MessageBox.show(context,R.string.error_title_webservice, R.string.error_message_return_data_invalid);
						break;
					case HTTPService.ERROR_SERVER_ERROR:
						MessageBox.show(context,R.string.error_title_webservice, R.string.error_message_server_error);
						break;
					case HTTPService.ERROR_CANCEL:
						break;
					default:
						MessageBox.show(context,R.string.error_title_webservice, R.string.error_message_unknow);
						break;

				}
			}
		};
		HTTPService objService = new HTTPService(objServiceCallback);
		objService.getImage(RealLuceAuctionApplication.allPlaces.get(0).getPlaceNo(),sessionNo,sellNo,position);

	}

	private void makeProgressDialog(){
		progressDialog = new ProgressDialog(this.getContext());
		progressDialog.setMessage(this.getContext().getText(R.string.please_waiting));
		progressDialog.setCancelable(false);
	}

	@Override
	public void dismiss() {
		progressDialog.dismiss();
		makeProgressDialog();
		super.dismiss();
	}
}
