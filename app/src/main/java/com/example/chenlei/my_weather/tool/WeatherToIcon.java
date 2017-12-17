package com.example.chenlei.my_weather.tool;

import com.example.chenlei.my_weather.R;

/**
 * Created by chenlei on 2017/9/19.
 */

public class WeatherToIcon {
    public static int weatherToIcon(String weather){

        switch (weather){
            case "晴":
                return R.mipmap.sunny;
            case "小雨":
                return R.mipmap.rain_small;
            case "小雨-中雨":
            case "中雨":
                return R.mipmap.rain_middle;
            case "中雨-大雨":
            case "大雨":
                return R.mipmap.rain_big;
            case "大雨-暴雨":
            case "暴雨":
                return R.mipmap.rain_large;
            case "暴雨-大暴雨":
            case "大暴雨":
                return R.mipmap.rain_l_large;
            case "大暴雨-特大暴雨":
            case "特大暴雨":
                return R.mipmap.rain_l_l_large;
            case "阵雨":
                return R.mipmap.rain_sun;
            case "雷阵雨":
                return R.mipmap.rain_thunder;
            case "小雪":
                return R.mipmap.snow_small;
            case "小雪-中雪":
            case "中雪":
                return R.mipmap.snow_middle;
            case "中雪-大雪":
            case "大雪":
                return R.mipmap.snow_big;
            case "大雪-暴雪":
            case "暴雪":
                return R.mipmap.snow_large;
            case "大暴雪":
            case "特大暴雪":
                return R.mipmap.snow_large;
            case "阴":
                return R.mipmap.cloudy;
            case "多云":
                return R.mipmap.sun_cloudy;
            case "雾":
                return R.mipmap.wu;
            default:
                return R.mipmap.sun_cloudy;
        }
    }
}
