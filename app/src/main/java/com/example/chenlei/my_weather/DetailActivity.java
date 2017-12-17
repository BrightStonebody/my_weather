package com.example.chenlei.my_weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chenlei.my_weather.dataBase.WeatherData;
import com.example.chenlei.my_weather.gson.Parse;
import com.example.chenlei.my_weather.gson.Weather;
import com.example.chenlei.my_weather.myViews.BlurView;
import com.example.chenlei.my_weather.myViews.TitleView;
import com.example.chenlei.my_weather.tool.GetSystemHeight;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detial);
        TextView temp = (TextView) findViewById(R.id.temp);
        TextView windPower = (TextView) findViewById(R.id.wind_power);
        TextView windDirect = (TextView) findViewById(R.id.wind_direct);
        TextView sun = (TextView) findViewById(R.id.sun);
        TextView air = (TextView) findViewById(R.id.air);
        ImageView indexImage = (ImageView) findViewById(R.id.index_image);
        TextView indexValue = (TextView) findViewById(R.id.index_value);
        TextView suggestion = (TextView) findViewById(R.id.suggestion);
        TitleView title = (TitleView) findViewById(R.id.title);

        BlurView background= (BlurView) findViewById(R.id.backGround);
        //适配系统状态栏
        GetSystemHeight.adapterStatusBar(this, title.getRoot(), 50);

        title.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        String cityId=intent.getStringExtra("cityId");
        int imageId = intent.getIntExtra("imageId", -1);
        int whichIndex = intent.getIntExtra("index", -1);
        int backgroundId = intent.getIntExtra("background",R.mipmap.bg_sunny);
        if (whichIndex == -1 || cityId==null||cityId.equals(""))
            return;

        //设置背景
        background.setImageResource(backgroundId,this);
        background.doBlur(100);

        WeatherData weatherData=WeatherData.getWeatherDataFromDb(cityId);
        if(weatherData==null) {
            Log.e("错误", "onCreate: "+"错误，detailActivity中获取天气数据为null");
            return;
        }
        Weather weather = Parse.parseWeather(weatherData.getJsonData());

        //与mainactivity相匹配
//        weather.index.remove(5);

        Weather.Index index = weather.index.get(whichIndex);

        title.setTitle(index.iname);
        indexValue.setText(index.ivalue);
        indexImage.setImageResource(imageId);
        suggestion.setText(index.detail);

        temp.setText(weather.templow + "~" + weather.temphigh);
        windPower.setText(weather.windpower);
        windDirect.setText(weather.winddirect);
        sun.setText(weather.index.get(2).ivalue);
        air.setText(weather.aqi.quality);

        ViewGroup viewGroup= (ViewGroup) getWindow().getDecorView();
        viewGroup.setBackgroundResource(backgroundId);
    }
}
