package com.example.chenlei.my_weather.gson;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by chenlei on 2017/9/24.
 */

public class WeatherHefeng {
    public Alarms alarms;
    public Basic basic;
    public List<Daily_forecast> daily_forecast;
    public List<Hourly_forecast> hourly_forecast;
    public Now now;
    public String status;
    public Suggestion suggestion;
    public Aqi aqi;


    public class Suggestion{
        public Index comf;
        public Index cw;
        public Index drsg;
        public Index flu;
        public Index sport;
        public Index air;
        public Index uv;

        public class Index{
            public String brf;
            public String txt;
        }
    }

    public class Now{
        public Cond cond;
        public String fi;
        public String humn;
        public String pcpn;
        public String pres;
        public String tmp;
        public String vis;
        public Wind wind;

        public class Cond{
            public String code;
            public String txt;
        }
    }

    public class Hourly_forecast{
        public Cond cond;
        public String date;
        public String hum;
        public String pop;
        public String pres;
        public String tmp;
        public Wind Wind;

        public class Cond{
            public String code;
            public String txt;
        }
    }

    public class Daily_forecast{
        public Astro astro;
        public Cond cond;
        public String date;
        public String hum;
        public String pcpn;
        public String pop;
        public String pres;
        public Tmp tmp;
        public String vis;
        public Wind wind;

        public class Tmp{
            public String max;
            public String min;
        }

        public class Cond{
            public String code_d;
            public String code_n;
            public String txt_d;
            public String txt_n;
        }
        public class Astro{
            public String mr;
            public String ms;
            public String sr;
            public String ss;
        }
    }

    public class Basic{
        public String city;
        public String cnty;
        public String id;
        public String lat;
        public String lon;
        public String prov;
        public Update update;
        public class Update{
            public String loc;
            public String utc;
        }
    }
    public class Alarms{
        public String level;
        public String stat;
        public String title;
        public String txt;
        public String type;
    }


    public class Aqi{
        public City city;

        public class City{
            public String aqi;
            public String co;
            public String no2;
            public String o3;
            public String pm10;
            public String pm25;
            public String qlty;
            public String so2;


        }
    }

    public class Wind{
        public String deg;
        public String dir;
        public String sc;
        public String spd;
    }

    public static Weather toWeather(WeatherHefeng whf){
        Weather weather=new Weather();
        //主要数据
        weather.city=whf.basic.city;
        weather.weather=whf.now.cond.txt;
        weather.updatetime=whf.basic.update.loc;
        weather.citycode=whf.basic.id;
        weather.pressure=whf.now.pres;
        weather.temphigh=whf.daily_forecast
                .get(0).tmp.max;
        weather.templow=whf.daily_forecast
                .get(0).tmp.min;
        weather.date=whf.basic.update.loc;
        weather.week=getWeek(whf.basic.update.loc);
        weather.winddirect=whf.now.wind.dir;
        weather.windpower=whf.now.wind.sc;
        weather.temp=whf.now.tmp;
        //空气质量数据
        //内部类的创建
        weather.aqi= weather.new Aqi();
        weather.aqi.aqi=whf.aqi.city.aqi;
        weather.aqi.ico=whf.aqi.city.co;
        weather.aqi.ino2=whf.aqi.city.no2;
        weather.aqi.io3=whf.aqi.city.o3;
        weather.aqi.ipm2_5=whf.aqi.city.pm25;
        weather.aqi.ipm10=whf.aqi.city.pm10;
        weather.aqi.iso2=whf.aqi.city.so2;
        weather.aqi.quality=whf.aqi.city.qlty;
        //小时天气预报
        weather.hourly=new ArrayList<>();
        for(Hourly_forecast hourly:whf.hourly_forecast){
            Weather.Hourly weaHourly=new Weather().new Hourly();
            weaHourly.time=hourly.date;
            weaHourly.weather=hourly.cond.txt;
            weaHourly.temp=hourly.tmp;
            weather.hourly.add(weaHourly);
        }
        //未来一周天气
        weather.daily=new ArrayList<>();
        for(Daily_forecast daily:whf.daily_forecast){
            Weather.Daily weaDaily=new Weather().new Daily();
            weaDaily.date=daily.date;
            weaDaily.day.temphigh=daily.tmp.max;
            weaDaily.day.weather=daily.cond.txt_d;
            weaDaily.night.templow=daily.tmp.min;
            weaDaily.night.weather=daily.cond.txt_n;
            weaDaily.sunset=daily.astro.ss;
            weaDaily.sunrise=daily.astro.sr;

            weaDaily.week=getWeek(daily.date);

            weather.daily.add(weaDaily);
        }
        //生活指数
        weather.index=new ArrayList<>();

        Weather.Index index=new Weather().new Index();

        index.ivalue=whf.suggestion.comf.brf;
        index.detail=whf.suggestion.comf.txt;
        index.iname="舒适指数";
        weather.index.add(index);
        index=new Weather().new Index();

        index.detail=whf.suggestion.sport.txt;
        index.iname="运动指数";
        index.ivalue=whf.suggestion.sport.brf;
        weather.index.add(index);

        index=new Weather().new Index();
        index.detail=whf.suggestion.uv.txt;
        index.iname="紫外线指数";
        index.ivalue=whf.suggestion.uv.brf;
        weather.index.add(index);
        index=new Weather().new Index();

        index.detail=whf.suggestion.flu.txt;
        index.iname="感冒指数";
        index.ivalue=whf.suggestion.flu.brf;
        weather.index.add(index);
        index=new Weather().new Index();

        index.detail=whf.suggestion.cw.txt;
        index.iname="洗车指数";
        index.ivalue=whf.suggestion.cw.brf;
        weather.index.add(index);
        index=new Weather().new Index();

        index.detail=whf.suggestion.drsg.txt;
        index.iname="穿衣指数";
        index.ivalue=whf.suggestion.drsg.brf;
        weather.index.add(index);
        index=new Weather().new Index();

        index.detail=whf.suggestion.air.txt;
        index.iname="空气污染扩散条件";
        index.ivalue=whf.suggestion.air.brf;
        weather.airIndex=index;

        return weather;
    }

    public static String getWeek(String date) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date theDate= null;
        try {
            theDate = sdf.parse(date);
        } catch (ParseException e) {
            Log.e("星期", "getWeek: "+"获取星期出错");
            e.printStackTrace();
        }
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(theDate);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
}
