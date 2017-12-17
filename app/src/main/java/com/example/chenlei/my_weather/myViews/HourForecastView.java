package com.example.chenlei.my_weather.myViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.chenlei.my_weather.gson.Weather;
import com.example.chenlei.my_weather.tool.WeatherToIcon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlei on 2017/9/17.
 */

public class HourForecastView extends View {

    private Paint mPaint;
    private float scale;
    private List<Weather.Hourly> datas = new ArrayList<>();
    private int maxY = 0;
    private int minY = 100;

    public HourForecastView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取分辨率，方便以后使用
        scale = context.getResources().getDisplayMetrics().density;

        mPaint = resetPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int[] last = new int[2];
        String lastWeather = null;
        float oneY = toPx(30) / (maxY - minY);
        int timeY = toPx(160);
        int oneX = toPx(50);
        int marginStart = toPx(30);
        int marginTop = toPx(70);

        for (int i = 0; i < datas.size(); i++) {
            mPaint = resetPaint();

            String temp = datas.get(i).temp;
            String time = datas.get(i).time;
            time = time.substring(time.lastIndexOf(" ") + 1);
            String weather = datas.get(i).weather;
            int x = (int) (marginStart + i * oneX);
            int y = (int) (((maxY - Integer.valueOf(temp))) * oneY + marginTop);
            canvas.drawCircle(x, y, 10, mPaint);

            if (i != 0) {
                canvas.drawLine(x, y, last[0], last[1], mPaint);
            }

            //画出从点到底端的连线，不然显得太空了
            mPaint.setStrokeWidth(1);
            mPaint.setAlpha(70);
            if (i != 0) {
                canvas.drawLine(last[0], timeY - toPx(30), x, timeY - toPx(30), mPaint);
            }
            canvas.drawLine(x, timeY - toPx(30), x, y, mPaint);


            last[0] = x;
            last[1] = y;

            //画出文字和图片
            mPaint = resetPaint();
            //设置文字水平居中
            mPaint.setTextAlign(Paint.Align.CENTER);

            canvas.drawText(temp + "°", x, y - toPx(8), mPaint);
            canvas.drawText(time, x, timeY, mPaint);

            if (!weather.equals(lastWeather)) {
                Bitmap weatherIcon = BitmapFactory.decodeResource(
                        getResources(), WeatherToIcon.weatherToIcon(weather));
                canvas.drawBitmap(weatherIcon, x - toPx(17), y - toPx(60), mPaint);
                lastWeather = weather;
            }

        }

    }

    private Paint resetPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(3);
        paint.setTextSize(toPx(15));
        return paint;
    }

    public void setDatas(List<Weather.Hourly> datas) {
        this.datas = datas;
        for (Weather.Hourly data : datas) {
            int temp = Integer.valueOf(data.temp);
            if (temp > maxY)
                maxY = temp;
            else if (temp < minY) {
                minY = temp;
            }
        }
        //给其余的东西预留一些空间
        maxY += 2;
        minY -= 2;
        invalidate();
    }

    private int toPx(int dp) {
        return (int) (dp * scale + 0.5f);
    }


}
