package com.sunday.cardscanning;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.io.IOException;

/**
 * Created by Sunday on 2018/3/28.
 */

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    public static String TAG = "CameraSurfaceView";

    private OnPictureListener onPictureListener;

    public void setOnPictureListener(OnPictureListener onPictureListener) {
        this.onPictureListener = onPictureListener;
    }

    private Context mContext;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private int mHeight,mWidth;

    public CameraSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init (Context context) {
        mContext = context;

        setFocusableInTouchMode(true);

    }

    private void initHolder () {

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        layoutParams.width = mCamera.getParameters().getPreviewSize().height;
        layoutParams.height = mCamera.getParameters().getPreviewSize().width;
        setLayoutParams(layoutParams);
        mHolder = getHolder();

        mHolder.addCallback(this);
        mHolder.setFormat(PixelFormat.TRANSLUCENT);
    }


    public void startPriview (Camera camera) {
        mCamera = camera;
        initHolder();
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拍照事件
     */
    public void takePicture () {
        if (mCamera == null) return;
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                ImageUtils.saveImage(mContext, data, onPictureListener);
                mCamera = null;
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(TAG, "surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG, "surfaceDestroyed");
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mHeight = h;
        mWidth = w;
    }


    public void stopCameraSurfaceView () {
        if (mCamera == null) return;
        mCamera.stopPreview();
        mCamera.release();
    }



    public interface OnPictureListener {
        void onPicture (String base64Data, Bitmap bitmap, String imagePath) ;
    }
}
