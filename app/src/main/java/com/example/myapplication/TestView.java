package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class TestView extends View {

    private Paint paint;
    private Canvas mCanvas;

    public TestView(Context context) {
        super(context);
        init();
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.FILL);    // 填充模式 - 描边
    }

    private boolean has = true;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
//        canvas.translate(10, 10);
        if (has) {
            canvas.drawRect(new Rect(0, 0, 80, 80), paint);
            has = false;
        }
//        canvas.save();
//        canvas.translate(50, 50);
//        canvas.restore();
//        canvas.drawRect(new Rect(90, 90, 150, 150), paint);


    }

    public void tarn() {
        mCanvas.translate(10, 10);
//        invalidate();
    }
}
