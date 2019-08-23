package com.lk.myproject.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

public class HeartView extends View {
    private Paint paint;

    public HeartView(Context context) {
        super(context);
    }

    public HeartView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();//实例画笔
        paint.setAntiAlias(true);//抗锯齿
        paint.setStrokeWidth(2);//画笔宽度
        paint.setColor(Color.RED);//画笔颜色
        paint.setStyle(Paint.Style.FILL);//画笔样式
    }

    int offsetX, offsetY;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();//获取屏幕宽
        int height = getHeight();//获取屏幕高

        /*offsetX = width / 2;
        offsetY = height / 2;
        float angle = 10;
        while (angle < 180) {
            Point p = getHeartPoint(angle);
            canvas.drawPoint(p.x, p.y, paint);
            angle = angle + 0.02f;
        }
        if (2 > 1) {
            return;
        }*/
        /**
         *  绘制心形
         */
        //左半面
        Path path = new Path();
        path.moveTo(width / 2, height / 4);
        path.cubicTo((width * 6) / 7, height / 9, (width * 12) / 13, (height * 2) / 5, width / 2, (height * 7) / 12);
        canvas.drawPath(path, paint);
        //右半面
        Path path2 = new Path();
        path2.moveTo(width / 2, height / 4);
        path2.cubicTo(width / 7, height / 9, width / 13, (height * 2) / 5, width / 2, (height * 7) / 12);
        canvas.drawPath(path2, paint);

    }

    public Point getHeartPoint(float angle) {
        float t = (float) (angle / Math.PI);
        float x = (float) (19.5 * (16 * Math.pow(Math.sin(t), 3)));
        float y = (float) (-20 * (13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t)));
        return new Point(offsetX + (int) x, offsetY + (int) y);
    }
}
