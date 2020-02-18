package com.example.myapplication;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

public class PathView extends View {


    private Handler mHandler = new Handler(Looper.getMainLooper());
    private MyRunable runable = new MyRunable();
    private Paint mPaint;
    private Paint mRecPaint;
    private Paint mCriclePaint;
    private Path mPath;
    private Path mAnimPath;
    private PathMeasure mPathMeasure;

    private ValueAnimator mAnimator;//动画对象
    private ValueAnimator mTranAnimator;//动画对象
    private float tranY = 0;

    private float[] pos; // 当前点的实际位置
    private float[] tan;

    private List<FloorImg> floorImgList = new ArrayList<>();
    private List<Point> pointList = new ArrayList<>();
    private List<Routes> routesList = new ArrayList<>();
    private Bitmap curBitmap;

    private int mWidth = 320;
    private int mHeight = 320;

    private int mCurPos = 0;//从0开始

    private Canvas mCanvas;

    private boolean needDrawRoute = false;
    private boolean needChangeFloor = false;
    private boolean isEnd = false;
    private int pointFloor;
    private int startFloor;
    private String mPointName;
    private String mLoutiName;

    private int bitmapWidth;
    private int bitmapHeight;

    public PathView(Context context) {
        super(context);
        init(context);
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);           // 画笔颜色 - 黑色
        mPaint.setStyle(Paint.Style.STROKE);    // 填充模式 - 描边
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);

        mCriclePaint = new Paint();
        mCriclePaint.setColor(Color.RED);           // 画笔颜色 - 黑色
        mCriclePaint.setStyle(Paint.Style.FILL);    // 填充模式 - 描边
        mCriclePaint.setStrokeWidth(2);
        mCriclePaint.setAntiAlias(true);

        mRecPaint = new Paint();
        mRecPaint.setColor(Color.BLUE);
        mRecPaint.setStyle(Paint.Style.FILL);    // 填充模式 - 描边

        mPath = new Path();

        mAnimPath = new Path();

        mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mPath, false);

        pos = new float[2];
        tan = new float[2];

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;

        //提前将背景图片绘制在画布上
        for (int i = 0; i < floorImgList.size(); i++) {
            curBitmap = ((BitmapDrawable) getResources().getDrawable(
                    floorImgList.get(i).getImg())).getBitmap();
            int left = 0;
            int right = mWidth;
            int top = (int) (0 - i * mHeight + tranY);
            int bootoom = top + mHeight;
            canvas.drawBitmap(curBitmap,
                    new Rect(0, 0, curBitmap.getWidth(), curBitmap.getHeight()),
                    new Rect(left, top, right, bootoom), mPaint);
            Log.d("RectPos", "top:" + top + "     bootoom:" + bootoom);
        }

        //绘制路径和路径前方的圆点，mAnimPath是通过预先设置好的Path中截取的。
        if (needDrawRoute) {
            canvas.drawPath(mAnimPath, mPaint);
            canvas.drawCircle(pos[0], pos[1], 3, mCriclePaint);
        }

    }

    /**
     * 开始执行路径动画
     */
    private void startRouteAnim(String pointname) {

        needDrawRoute = true;

        mPath.reset();
        mAnimPath.reset();

        List<Routes> list = DButils.getRoutePoints(pointname);
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                mPath.moveTo(list.get(i).getPointX() * mWidth / 1756,
                        list.get(i).getPointY() * mHeight / 865);
            }
            mPath.lineTo(list.get(i).getPointX() * mWidth / 1756,
                    list.get(i).getPointY() * mHeight / 865);
        }
        mPathMeasure.setPath(mPath, false);

        mAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration(2000);
        mAnimator.setRepeatCount(0);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                mPathMeasure.getSegment(0,
                        mPathMeasure.getLength() * value, mAnimPath,
                        true);
                mPathMeasure.getPosTan(mPathMeasure.getLength() * value, pos, tan);
                invalidate();
            }
        });
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (needChangeFloor) {
                    tranFloor(pointFloor - 1);
                } else {
//                    mHandler.postDelayed(runable, 2000);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mAnimator.start();
    }

    /**
     * 背景图切换动画
     */
    public void tranFloor(final int floor) {

        needDrawRoute = false;

        mPath.reset();
        mAnimPath.reset();

        mTranAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        mTranAnimator.setInterpolator(new LinearInterpolator());
        mTranAnimator.setDuration(1000);
        mTranAnimator.setRepeatCount(0);
        mTranAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                tranY = value * mHeight * floor;
                if (startFloor > floor) {
                    tranY = -tranY;
                }
                invalidate();
            }
        });
        mTranAnimator.start();

        mTranAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (needChangeFloor) {
                    needChangeFloor = false;
                    startRouteAnim(mPointName);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }


    /**
     * @param imglist
     * @param pList
     * @param rList
     * @return
     */
    public PathView loadFloorImg(List<FloorImg> imglist, List<Routes> rList, List<Point> pList) {
        needDrawRoute = false;
        floorImgList.clear();
        pointList.clear();
        routesList.clear();
        floorImgList.addAll(imglist);
        pointList.addAll(pList);
        routesList.addAll(rList);
        startFloor = DButils.getStartFloor();
        invalidate();
        tranFloor(startFloor - 1);

        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(
                floorImgList.get(0).getImg())).getBitmap();
        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();
        return this;
    }

    /**
     * 前往导航点
     *
     * @param pointName
     */
    public void gotoPoint(String pointName) {

        tranFloor(startFloor - 1);

        if (mAnimator != null) {
            mAnimator.end();
        }
        if (mTranAnimator != null) {
            mTranAnimator.end();
        }

//        mHandler.removeCallbacks(runable);
//        mHandler.removeCallbacksAndMessages(null);

        mPointName = pointName;

        pointFloor = DButils.getPointFloor(pointName);
        if (pointFloor != startFloor) {
            mLoutiName = DButils.getRightLouTi(pointName);
            needChangeFloor = true;
            startRouteAnim(mLoutiName);
        } else {
            needChangeFloor = false;
            startRouteAnim(mPointName);
        }

        invalidate();
    }

    class MyRunable implements Runnable {
        @Override
        public void run() {
            tranFloor(startFloor - 1);
        }
    }
}
