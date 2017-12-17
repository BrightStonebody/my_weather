package com.example.chenlei.my_weather.myViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chenlei.my_weather.R;
import com.example.chenlei.my_weather.gson.Weather;
import com.example.chenlei.my_weather.tool.WeatherToIcon;

/**
 * Created by chenlei on 2017/11/6.
 */

public class BottomDailyView extends RelativeLayout {


    public BottomDailyView(Context context) {
        super(context);
        initView(context);
    }

    public BottomDailyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BottomDailyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private TextView date=null;
    private TextView temp=null;
    private ImageView weather_ic=null;

    private void initView(Context context){
        LayoutInflater.from(context).inflate
                (R.layout.part_bottom_daily_view,this,true);
        date= (TextView) findViewById(R.id.date);
        temp= (TextView) findViewById(R.id.temp);
        weather_ic= (ImageView) findViewById(R.id.bottom_weather_ic);
    }

    public void initDate(Weather.Daily daily){
        temp.setText(daily.night.templow+"°~"+daily.day.temphigh+"°");
        weather_ic.setImageResource
                (WeatherToIcon.weatherToIcon(daily.day.weather));
    }


}
