package com.cmpt276.calciumparentapp.ui.timer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

// CODE ADAPTED FROM: https://github.com/MRezaNasirloo/CircularProgressBar


public class TimerProgressBar extends View {

    private float strokeWidth;
    private RectF rectF;
    private Paint backgroundPaint;
    private Paint foregroundPaint;
    private long maxTime = 1;
    private long currentTime = 1;

    private static final int STROKE_WIDTH_DP = 4;

    public TimerProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        rectF = new RectF();

        // Convert the stoke with dp into a pixel value
        strokeWidth = TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, STROKE_WIDTH_DP,
                getResources().getDisplayMetrics());

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(Color.GRAY);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);

        foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foregroundPaint.setColor(Color.BLUE);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(strokeWidth);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        rectF.set(0 + strokeWidth / 2, 0 + strokeWidth / 2, min - strokeWidth / 2, min - strokeWidth / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawOval(rectF, backgroundPaint);
        float angle = 360 * (float)currentTime / (float)maxTime;
        canvas.drawArc(rectF, -90, angle, false, foregroundPaint);
    }

    public void startTimerProgress(long maxTime) {
        this.maxTime = maxTime;
        this.currentTime = maxTime;
        invalidate();
    }

    public void updateTimerProgress(long currentTime) {
        this.currentTime = currentTime;
        invalidate();
    }


}
