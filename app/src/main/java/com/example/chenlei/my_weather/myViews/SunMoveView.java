package com.example.chenlei.my_weather.myViews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chenlei.my_weather.R;
import com.example.chenlei.my_weather.tool.GetSystemHeight;

import java.util.Calendar;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by chenlei on 2017/10/10.
 */

public class SunMoveView extends RelativeLayout{

    private ImageView sun=null;
    private TextView sunRise=null;
    private TextView sunSet=null;

    public SunMoveView(Context context) {
        super(context);
        initView(context);
    }

    public SunMoveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.part_sun_view,this,true);
        sun= (ImageView) findViewById(R.id.sun);
        sunRise= (TextView) findViewById(R.id.sunrise);
        sunSet= (TextView) findViewById(R.id.sunset);
    }

    public void sunMoveTo(String r,String s){
        sunRise.setText(r);
        sunSet.setText(s);

        int sunHeight = 0;
        int sunWidth = 0;

        int height = 0;
        int width = 0;


        //由于找不到能够马上得到控件长宽的方法，暂时这样子将dp转换成px
        //measurespec这个方法在测量road的时候测量的值出现问题
        sunHeight = GetSystemHeight.dptoPx(getContext(),40);
        sunWidth = GetSystemHeight.dptoPx(getContext(),40);
        width = GetSystemHeight.dptoPx(getContext(),170);

        final int R = (width / 2);

        int now = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//        int now=1;
        now = now * 60 + Calendar.getInstance().get(Calendar.MINUTE);
        int rise = timeToInt(r);
        int set = timeToInt(s);
        if(rise==-1||set==-1)
            return ;
        RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams)
                sun.getLayoutParams();
        Log.i("时间", "sunMove: " + now + "   " + rise + "   " + set);

        //setmargin左上右下
        if (now > set) {
            layout.setMargins(2 * R, 0, 2 * R + sunWidth, 0);
            sun.setImageResource(com.example.chenlei.my_weather.R.mipmap.sun_half);
            sun.setAlpha((float) 0.3);
        } else if (now < rise) {
            layout.setMargins(0, 0, sunWidth, 0);
            sun.setImageResource(com.example.chenlei.my_weather.R.mipmap.sun_half);
            sun.setAlpha((float) 0.3);
        } else {
            sun.setImageResource(com.example.chenlei.my_weather.R.mipmap.sun_2);
            float angle = (((float) (now - rise) / (set - rise)) * 180);
            angle = (float) (angle * 2 * PI / 360);

            int x = (int) (R - R * cos(angle));
            int y = (int) (R * sin(angle));

            Log.i("时间", "sunMove: " + "sin  " + sin(angle) + "cos  " + cos(angle));
            Log.i("时间", "sunMove: " + x + "  " + y);
            layout.setMargins(x, 0, 0, y);
        }
        sun.setLayoutParams(layout);
    }

    private int timeToInt(String r) {
        if (r==null||r.equals(""))
            return -1;
        int t = (r.charAt(0) - '0') * 10 + (r.charAt(1) - '0');
        t = t * 60;
        t = t + (r.charAt(3) - '0') * 10 + (r.charAt(4) - '0');
        return t;
    }

}
