# CardScanning
提供作为识别身份证、银行卡等卡片或驾驶证等证件的功能模块（具体对接需对接OCR识别接口）


----------

**CameraSurfaceView**： 继承 SurfaceView ，作为摄像头预览画面承载控件
**CameraHelper**： 调用系统摄像头辅助类，提供摄像头初始化，图片校正，预览画面比例设置功能。
**CameraSurfaceBgView：** CameraSurfaceView 遮挡层，用于切割证件与背景
**ImageUtils：** 拍照完成之后的图片处理，包括 图片的切割，校正 ，压缩 ，转换等功能。


----------
**功能截图**
![这里写图片描述](https://img-blog.csdn.net/20180330150005620?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTEwNjg5OTY=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

保存下来的图片截取内容

![这里写图片描述](https://img-blog.csdn.net/20180330150031755?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTEwNjg5OTY=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)


----------
提供了拍照之后的回调事件

```
mSurfaceView.setOnPictureListener(new CameraSurfaceView.OnPictureListener() {
            @Override
            public void onPicture(String base64Data, Bitmap bitmap, String imagePath) {
                Log.e(TAG, "base64: " + base64Data);
                Log.e(TAG, "imagePath: " + imagePath);
                Toast.makeText(MainActivity.this, "拍照成功-图片保存至:" + imagePath, Toast.LENGTH_SHORT).show();
            }
        });
```


----------
使用方法

```
// 初始化 surfaceView
private CameraSurfaceView mSurfaceView;
private CameraHelper mCameraHelper;

mSurfaceView = findViewById(R.id.surface_view);
 mCameraHelper = new CameraHelper(this);

// 设置 拍照回调
mSurfaceView.setOnPictureListener(new CameraSurfaceView.OnPictureListener() {
            @Override
            public void onPicture(String base64Data, Bitmap bitmap, String imagePath) {
                Log.e(TAG, "base64: " + base64Data);
                Log.e(TAG, "imagePath: " + imagePath);
                Toast.makeText(MainActivity.this, "拍照成功-图片保存至:" + imagePath, Toast.LENGTH_SHORT).show();
            }
        });
```


```
// 开启预览

Camera camera = mCameraHelper.openCamera();
        mSurfaceView.startPriview(camera);
```

```
//拍照
 mSurfaceView.takePicture();
```

```
// 监听pause事件

@Override
    protected void onPause() {
        super.onPause();

        mSurfaceView.stopCameraSurfaceView();
    }
```

