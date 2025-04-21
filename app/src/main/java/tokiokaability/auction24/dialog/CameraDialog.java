package tokiokaability.auction24.dialog;
import tokiokaability.auction24.HTTPInfo.BaseInfo;
import tokiokaability.auction24.HTTPInfo.ItemInfo;
import tokiokaability.auction24.HTTPInfo.HTTPService;
import tokiokaability.auction24.HTTPInfo.HTTPServiceCallback;
import tokiokaability.auction24.R;
import tokiokaability.auction24.commons.MessageBox;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Chung on 2016/08/24.
 */
public class CameraDialog extends Dialog implements SurfaceHolder.Callback   {
    public enum ShootingType{
        Single,
        Continuous
    }


    public interface PictureCallback{
        public void onPictureReady(int imagePosition, byte[] data);
    }

    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ImageButton btnCapture;
    private ImageButton btnFlash;
    private ImageButton btnBack;
    private ProgressBar pgbIndicator;
    //Camera.PictureCallback rawCallback;
    //Camera.ShutterCallback shutterCallback;
    private Camera.PictureCallback jpegCallback;
    private CameraDialog.PictureCallback callback;
    private Camera.Size cameraSize;
    private Camera.Size cameraPreviewSize;
    private ShootingType shootingType;
    private int imagePosition;
    private byte[] imageData;
    private boolean flashIsOn = false;
    private RelativeLayout lblContinueMessage;
    ItemInfo itemInfo;

    public  CameraDialog(Activity activity, PictureCallback callback){
        super(activity);
        this.callback = callback;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.setContentView(R.layout.dialog_camera);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));


        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height =WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);


        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        jpegCallback = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                byte[] imageData = data.clone();
                refreshCamera();
                uploadImageData(imageData);
            }
        };

        btnCapture = (ImageButton) findViewById(R.id.btnCapture);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnFlash = (ImageButton) findViewById(R.id.btnFlash);
        lblContinueMessage = (RelativeLayout)findViewById(R.id.lblContinueMessage);
        lblContinueMessage.setAlpha(0);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCapture.setEnabled(false);
                camera.takePicture(null, null, jpegCallback);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 閉じる際に、メッセージを削除
                if( lblContinueMessage.getAlpha() > 0.0f ) {
                    lblContinueMessage.setAlpha(0.0f);
                }
                dismiss();
            }
        });

        btnFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flashIsOn){
                    flashIsOn = false;
                    btnFlash.setImageResource(R.drawable.camera_flash_off_button_style);
                }
                else{
                    flashIsOn = true;
                    btnFlash.setImageResource(R.drawable.camera_flash_on_button_style);
                }

                Camera.Parameters param;
                param = camera.getParameters();
                if (getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
                    if (flashIsOn)
                        param.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                    else
                        param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                }
                camera.setParameters(param);

            }
        });

        pgbIndicator = (ProgressBar) findViewById(R.id.pgbIndicator);
        pgbIndicator.setVisibility(View.INVISIBLE);
        flashIsOn = false;
        btnFlash.setImageResource(R.drawable.camera_flash_off_button_style);
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();



            // find 4/3 size
            Camera.Parameters params = camera.getParameters();
            // 端末の撮影サイズのリストを取得
            List<Camera.Size> sizes = params.getSupportedPictureSizes();
            cameraSize =sizes.get(0);
            for(int i=0; i< sizes.size(); i++){
                /*
                // 横幅が400より小さければ、採用しない
                if(sizes.get(i).width < 400) {
                    continue;
                }
                */
                // 撮影サイズが４：３の比率に近い値を取得
                if (Math.abs( ((double) cameraSize.width)/((double)cameraSize.height) - 4.00/3.0 ) >  Math.abs( ((double) sizes.get(i).width)/((double)sizes.get(i).height) - 4.00/3.0 ) && sizes.get(i).width <=1024){
                    cameraSize = sizes.get(i);
                }
                // 現在の撮影サイズと比率が同じであれば、横幅が大きいほうを採用
                else if (Math.abs( ((double) cameraSize.width)/((double)cameraSize.height) - 4.00/3.0 ) ==  Math.abs( ((double) sizes.get(i).width)/((double)sizes.get(i).height) - 4.00/3.0 ) && sizes.get(i).width <=1024){
                    if (cameraSize.width < sizes.get(i).width){
                        cameraSize = sizes.get(i);
                    }
                }
                // 現在の撮影サイズの横幅が1024より大きければ、それより小さいサイズを採用
                else if ((double) cameraSize.width > 1024 && sizes.get(i).width <=1024){
                    cameraSize = sizes.get(i);
                }

            }

            int previewWidth;
            int previewHeigh;

            previewWidth = surfaceView.getWidth();
            previewHeigh = (int)(4.0/3.0*previewWidth);

            ViewGroup.LayoutParams layoutParam= surfaceView.getLayoutParams();
            layoutParam.height = previewHeigh;
            surfaceView.setLayoutParams(layoutParam);

            sizes =  params.getSupportedPreviewSizes();
            cameraPreviewSize = sizes.get(0);
            for(int i=0; i< sizes.size(); i++){
                if (Math.abs( ((double) cameraPreviewSize.width)/((double)cameraPreviewSize.height) - 4.00/3.0 ) >  Math.abs( ((double) sizes.get(i).width)/((double)sizes.get(i).height) - 4.00/3.0 ) && sizes.get(i).width <=previewWidth){
                    cameraPreviewSize = sizes.get(i);
                }
                else if (Math.abs( ((double) cameraPreviewSize.width)/((double)cameraPreviewSize.height) - 4.00/3.0 ) ==  Math.abs( ((double) sizes.get(i).width)/((double)sizes.get(i).height) - 4.00/3.0 ) && sizes.get(i).width <=previewWidth){
                    if (cameraPreviewSize.width < sizes.get(i).width){
                        cameraPreviewSize = sizes.get(i);
                    }
                }
            }

            Camera.Parameters param;
            param = camera.getParameters();
            param.setPreviewSize(cameraPreviewSize.width, cameraPreviewSize.height);
            param.setPictureSize(cameraSize.width, cameraSize.height);
            if (getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
                if (flashIsOn)
                    param.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                else
                    param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            }
            else{
                btnFlash.setVisibility(View.GONE);
            }
            camera.setParameters(param);

            camera.setDisplayOrientation(90);




            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }


    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void show(ShootingType shootingType, int fromImagePosition, ItemInfo itemInfo){
        if (!isShowing()){
            this.shootingType = shootingType;
            this.imagePosition = fromImagePosition;
            this.itemInfo = itemInfo;
            show();
        }
    }

    private void uploadImageData(byte[] data){
        pgbIndicator.setVisibility(View.VISIBLE);
        btnCapture.setEnabled(false);
        imageData = data;


        HTTPService objService = new HTTPService(new HTTPServiceCallback(){
            @Override
            public void onSucessfully(Object returnData) {
                BaseInfo retObj = (BaseInfo) returnData;
                if (retObj.getStatus() != 0 ){
                    MessageBox.show(getContext(),R.string.error_title_webservice, R.string.error_message_server_error);
                }
                else{
                    if (callback != null){
                        callback.onPictureReady(imagePosition, imageData);
                    }

                    if (shootingType == ShootingType.Continuous){
                        imagePosition = imagePosition + 1;
                        if (imagePosition > 6){
                            CameraDialog.this.dismiss();
                        }
                        else{
                            /*
                            Toast myToast = new Toast(getContext());
                            TextView text = new TextView(getContext());
                            text.setText(R.string.camera_dialog_continue_capture);
                            text.setRotation(-90);
                            myToast.setView(text);

                            myToast.setDuration(Toast.LENGTH_SHORT);
                            myToast.setGravity(Gravity.CENTER|Gravity.LEFT, 100,100);
                            myToast.show();
*/

                            //Toast.makeText(getContext(), getContext().getResources().getString (R.string.camera_dialog_continue_capture), Toast.LENGTH_SHORT).show();

                            // 撮影後のメッセージ表示(表示しているときに表示しようとすると、落ちるため表示してない時のみ表示処理)
                            if( lblContinueMessage.getAlpha() == 0.0f ) {
                                final Timer time = new Timer();
                                time.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        lblContinueMessage.animate().setDuration(1000).alpha(0.0f);

                                    }
                                }, 2000);
                                lblContinueMessage.animate().setDuration(1000).alpha(1.0f);
                            }
                        }
                    }
                    else{
                        CameraDialog.this.dismiss();
                    }
                }
                pgbIndicator.setVisibility(View.INVISIBLE);
                btnCapture.setEnabled(true);
                btnFlash.setEnabled(true);
                btnBack.setEnabled(true);
            }

            @Override
            public void onFailed(int errorCode) {
                pgbIndicator.setVisibility(View.INVISIBLE);
                btnCapture.setEnabled(true);
                btnFlash.setEnabled(true);
                btnBack.setEnabled(true);

                switch(errorCode){
                    case HTTPService.ERROR_NETWORK:
                        MessageBox.show(getContext(),R.string.error_title_webservice, R.string.error_message_xml_connect_gateway);
                        break;
                    case HTTPService.ERROR_XMLPARSER:
                        MessageBox.show(getContext(),R.string.error_title_webservice, R.string.error_message_xml_parser);
                        break;
                    case HTTPService.ERROR_RETURN_DATA_INVALID:
                        MessageBox.show(getContext(),R.string.error_title_webservice, R.string.error_message_return_data_invalid);
                        break;
                    case HTTPService.ERROR_SERVER_ERROR:
                        MessageBox.show(getContext(),R.string.error_title_webservice, R.string.error_message_server_error);
                        break;
                    case HTTPService.ERROR_CANCEL:
                        break;
                    default:
                        MessageBox.show(getContext(),R.string.error_title_webservice, R.string.error_message_unknow);
                        break;

                }
            }

        });

        objService.setImage(itemInfo.getPlaceId(), itemInfo.getTimesNo(), String.valueOf(itemInfo.getItemNo()), imagePosition, data);
        btnCapture.setEnabled(false);
        btnFlash.setEnabled(false);
        btnBack.setEnabled(false);

    }
}
