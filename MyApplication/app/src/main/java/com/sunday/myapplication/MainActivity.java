package com.sunday.myapplication;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sunday.cardscanning.CameraHelper;
import com.sunday.cardscanning.CameraSurfaceView;

public class MainActivity extends AppCompatActivity{

    public static final String TAG = "MainActivity";

    private CameraSurfaceView mSurfaceView;
    private CameraHelper mCameraHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSurfaceView = findViewById(R.id.surface_view);
        mCameraHelper = new CameraHelper(this);
        mSurfaceView.setOnPictureListener(new CameraSurfaceView.OnPictureListener() {
            @Override
            public void onPicture(String base64Data, Bitmap bitmap, String imagePath) {
                Log.e(TAG, "base64: " + base64Data);
                Log.e(TAG, "imagePath: " + imagePath);
                Toast.makeText(MainActivity.this, "拍照成功-图片保存至:" + imagePath, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onStartClick (View v) {
        Camera camera = mCameraHelper.openCamera();
        mSurfaceView.startPriview(camera);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mSurfaceView.stopCameraSurfaceView();
    }

    public void onTakePictureClick (View v) {
        mSurfaceView.takePicture();
    }
}
