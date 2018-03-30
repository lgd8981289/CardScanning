package com.sunday.cardscanning;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Sunday on 2018/3/29.
 */

public class CameraSurfaceBgView extends View {

    public static int shearX = 0;
    public static int shearY = 0;
    public static int shearWidth = 0;
    public static int shearHeight = 0;

    /**
     * 裁剪比例
     */
    public static final int shearSize = 5;

    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 四个绿色边角对应的宽度
     */
    private static final int CORNER_WIDTH = 10;

    private int mWidth, mHeight;

    public CameraSurfaceBgView(Context context) {
        super(context);
        init(context);
    }

    public CameraSurfaceBgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CameraSurfaceBgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CameraSurfaceBgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    private void init (Context context) {
        paint = new Paint();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 识别扫描区域
        Rect frame = null;
        try {
            frame = new Rect(mWidth / shearSize , mHeight / shearSize , mWidth / shearSize * (shearSize - 1), mHeight / shearSize * (shearSize - 1));
            shearX = frame.left;
            shearY = frame.top;
            shearWidth = frame.right - frame.left;
            shearHeight = frame.bottom - frame.top;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (frame == null) {
            return;
        }

        paint.setColor(getResources().getColor(R.color.bg_gray));
        //画出扫描框外面的阴影部分，共四个部分，扫描框的上面到屏幕上面，扫描框的下面到屏幕下面
        //扫描框的左边面到屏幕左边，扫描框的右边到屏幕右边
        canvas.drawRect(0, 0, mWidth, frame.top, paint); //上
        canvas.drawRect(0, frame.top, frame.left, frame.bottom - 1, paint);  //左
        canvas.drawRect(frame.right, frame.top, mWidth, frame.bottom - 1, paint); //右
        canvas.drawRect(0, frame.bottom - 1, mWidth, mHeight, paint);


        //画扫描框边上的角，总共8个部分
//        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.colorAccent));
        canvas.drawRect(frame.left, frame.top, frame.left + getResources().getDimensionPixelOffset(R.dimen.line_width),
                frame.top + CORNER_WIDTH, paint);
        canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH, frame.top
                + getResources().getDimensionPixelOffset(R.dimen.line_width), paint);
        canvas.drawRect(frame.right - getResources().getDimensionPixelOffset(R.dimen.line_width), frame.top, frame.right,
                frame.top + CORNER_WIDTH, paint);
        canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right, frame.top
                + getResources().getDimensionPixelOffset(R.dimen.line_width), paint);
        canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left
                + getResources().getDimensionPixelOffset(R.dimen.line_width), frame.bottom, paint);
        canvas.drawRect(frame.left, frame.bottom - getResources().getDimensionPixelOffset(R.dimen.line_width),
                frame.left + CORNER_WIDTH, frame.bottom, paint);
        canvas.drawRect(frame.right - getResources().getDimensionPixelOffset(R.dimen.line_width), frame.bottom - CORNER_WIDTH,
                frame.right, frame.bottom, paint);
        canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - getResources().getDimensionPixelOffset(R.dimen.line_width),
                frame.right, frame.bottom, paint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
    }
}
