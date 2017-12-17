package com.example.chenlei.my_weather.dataBase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by chenlei on 2017/9/28.
 */

public class WeatherData extends DataSupport {
    private int id;
    private String cityId;
    private String jsonData;
    private long time;

    public  static boolean timeSubtract(long time_last,Context context) {
        long time_now = System.currentTimeMillis();
        Log.i("service", "run: " + time_last + "   " + time_now);
        double time_between = (time_now - time_last) / 3600.0 / 1000;
        SharedPreferences storeSetTime =context.getSharedPreferences("set", Context.MODE_PRIVATE);
        float setTime = storeSetTime.getFloat("update", (float) 3.0);

        if (time_last == -1 || time_between > setTime)
            return true;
        else
            return false;
    }

    public static WeatherData getWeatherDataFromDb(String cityId){
        try {
            List<WeatherData> weatherDatas= DataSupport.where("cityId = ?", cityId)
                    .find(WeatherData.class);
            if(weatherDatas==null)
                return null;
            else if(weatherDatas.size()==0)
                return null;
            else if(weatherDatas.get(0).getJsonData()==null
                    ||weatherDatas.get(0).getJsonData().equals(""))
                return null;
            else
                return weatherDatas.get(0);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
