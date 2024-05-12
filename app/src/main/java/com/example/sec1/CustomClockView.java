package com.example.sec1;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CustomClockView extends View {
    private Paint circlePaint;
    private Paint handPaint;
    private Paint textPaint;
    private int radius;
    private int centerX;
    private int centerY;

    public CustomClockView(Context context) {
        super(context);
        init();
    }

    public CustomClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setColor(Color.LTGRAY  );
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(8);

        handPaint = new Paint();
        handPaint.setColor(Color.BLACK);
        handPaint.setStrokeWidth(6);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = Math.min(w, h) / 2 - 20;
        centerX = w / 2;
        centerY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制表盘
        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        // 绘制表盘刻度
        for (int i = 0; i < 60; i++) {
            float angle = (float) (i * Math.PI / 30);
            float startX = (float) (centerX + (radius - 40) * Math.sin(angle));
            float startY = (float) (centerY - (radius - 40) * Math.cos(angle));
            float endX = (float) (centerX + radius * Math.sin(angle));
            float endY = (float) (centerY - radius * Math.cos(angle));
            canvas.drawLine(startX, startY, endX, endY, circlePaint);

            if (i % 5 == 0) {
                int number = i / 5 == 0 ? 12 : i / 5;
                float textX = (float) (centerX + (radius - 100) * Math.sin(angle));
                float textY = (float) (centerY - (radius - 100) * Math.cos(angle)) + 20;
                canvas.drawText(String.valueOf(number), textX, textY, textPaint);
            }
        }

        // 获取当前时间
        long currentTime = System.currentTimeMillis();
        int seconds = (int) (currentTime / 1000 % 60);
        int minutes = (int) ((currentTime / (1000 * 60)) % 60);
        int hours = (int) ((currentTime / (1000 * 60 * 60)) % 12);

        // 计算时针、分针、秒针的角度
        float secondAngle = seconds * 6;
        float minuteAngle = minutes * 6 + seconds * 0.1f;
        float hourAngle = hours * 30 + minutes * 0.5f;

        // 绘制时针
        drawHand(canvas, hourAngle, radius * 0.5f, handPaint);

        // 绘制分针
        drawHand(canvas, minuteAngle, radius * 0.7f, handPaint);

        // 绘制秒针
        drawHand(canvas, secondAngle, radius * 0.9f, handPaint);

        // 通过 postInvalidateDelayed 实现动态更新
        postInvalidateDelayed(1000);
    }

    private void drawHand(Canvas canvas, float angle, float length, Paint paint) {
        float startX = centerX;
        float startY = centerY;
        float endX = (float) (centerX + length * Math.sin(Math.toRadians(angle)));
        float endY = (float) (centerY - length * Math.cos(Math.toRadians(angle)));
        canvas.drawLine(startX, startY, endX, endY, paint);
    }
}
