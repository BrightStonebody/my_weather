package com.example.chenlei.my_weather.gson;

import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by chenlei on 2017/7/19.
 */

public class Parse {
    public static Weather parseWeather(String datas) {
        Weather weather=null;
        if(datas==null)
            return null;
        try {
            Gson gson=new Gson();
            JsonAdapte jsonAdapte=gson.fromJson(datas,JsonAdapte.class);
            if(jsonAdapte==null)
                return null;
            if(!jsonAdapte.code.equals("10000")){
                Log.e("错误", "错误，json数据解析失败 parseWeather: 错误代码 "+jsonAdapte.code);
                return null;
            }
            WeatherHefeng weatherHefeng=jsonAdapte.result.HeWeather5.get(0);
            weather=WeatherHefeng.toWeather(weatherHefeng);
        }catch (Exception e){
            Log.e("错误", "parseWeather: "+"错误，天气信息解析失败" );
            e.printStackTrace();
        }
        return weather;
    }

}
