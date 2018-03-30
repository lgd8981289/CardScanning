package com.sunday.cardscanning;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Base64;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Sunday on 2018/3/29.
 */

public class ImageUtils {

    public static final String saveImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/saveImageForCard";

    public static void saveImage(Context context, byte[] data, CameraSurfaceView.OnPictureListener onPictureListener) {
        String savePath = saveImagePath;
        String fileName = System.currentTimeMillis() + ".jpg";
        File pathFile = new File(savePath);
        File imageFile = new File(savePath + "/" + fileName);
        FileOutputStream fileOutputStream;
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }

        try {
            if (!imageFile.exists()) {
                imageFile.createNewFile();
            }

            fileOutputStream = new FileOutputStream(imageFile);
            fileOutputStream.write(data);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
        }
        setPictureDegreeZero(imageFile.getAbsolutePath());
        shearImage(imageFile, onPictureListener);
    }


    private static void setPictureDegreeZero(String path) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            // 修正图片的旋转角度，设置其不旋转。这里也可以设置其旋转的角度，可以传值过去，
            // 例如旋转90度，传值ExifInterface.ORIENTATION_ROTATE_90，需要将这个值转换为String类型的
            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_ROTATE_90));
            exifInterface.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对拍下来的照片进行裁剪
     * @param file
     */
    private static void shearImage (File file, CameraSurfaceView.OnPictureListener onPictureListener) {
        try {
            String toShearBitmapPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
            Bitmap bitmap = getBitmapFromImagePath(file.getAbsolutePath(), toShearBitmapPath);
            Bitmap shearBitmap = Bitmap.createBitmap(bitmap,
                    CameraSurfaceBgView.shearX,
                    CameraSurfaceBgView.shearY,
                    CameraSurfaceBgView.shearWidth,
                    CameraSurfaceBgView.shearHeight);

            saveBitmapToFile(shearBitmap, toShearBitmapPath);

            if (onPictureListener != null) {
                onPictureListener.onPicture(bitmapToString(shearBitmap), shearBitmap, toShearBitmapPath);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    /**
     * 根据路径生成Bitmap
     */
    private static Bitmap getBitmapFromImagePath(String imagePath, String comPath) throws FileNotFoundException {
        FileOutputStream fos = new FileOutputStream(comPath);
        FileInputStream fis = new FileInputStream(imagePath);
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize = getComSize(imagePath);
        Bitmap bitmap = rotateBitmap(readPicDegree(imagePath)
                , BitmapFactory.decodeStream(fis, new Rect(), options));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        return bitmap;
    }


    /**
     * 图片缩放的规则
     * @param path
     * @return
     */
    private static Integer getComSize(String path){
        File file=new File(path);
        if(file.exists()){
            if (file.length() > 400 * 1024){
                return (int)(file.length() / (400 * 1024));
            }

        }
        return 1;
    }

    /**
     * 将图片纠正到正确方向
     *
     * @param degree ： 图片被系统旋转的角度
     * @param bitmap ： 需纠正方向的图片
     * @return 纠向后的图片
     */
    private static Bitmap rotateBitmap(int degree, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return bm;
    }

    /**
     * 通过ExifInterface类读取图片文件的被旋转角度
     * @param path ： 图片文件的路径
     * @return 图片文件的被旋转角度
     */
    private static int readPicDegree(String path) {
        int degree = 0;

        // 读取图片文件信息的类ExifInterface
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        }
        return degree;
    }


    /**
     * 将Bitmap转换成Base64字符串
     * @param bitmap
     * @return
     */
    private static String bitmapToString(Bitmap bitmap) {
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }

    private static void saveBitmapToFile(Bitmap bitmap, String path){
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
