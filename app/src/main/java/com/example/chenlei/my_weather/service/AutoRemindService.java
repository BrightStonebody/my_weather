package com.example.chenlei.my_weather.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.chenlei.my_weather.MainActivity;
import com.example.chenlei.my_weather.R;
import com.example.chenlei.my_weather.gson.Parse;
import com.example.chenlei.my_weather.gson.Weather;
import com.example.chenlei.my_weather.tool.Net_http;

/**
 * Created by chenlei on 2017/7/23.
 */

public class AutoRemindService extends Service {


    final long TIME_LONG =6;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    Weather weather=null;

    private void remind(){
        if(weather==null)
            return ;
        boolean rain=false,snow=false;
        for(Weather.Hourly forecast_hourly:weather.hourly){
            if(forecast_hourly.weather.contains("雨"))
                rain=true;
            else if(forecast_hourly.weather.contains("雪"))
                snow=true;
        }
        String str=null;
        if(rain&&!snow)
            str="最近几个小时有降雨,出门记得带伞哦!";
        else if((!rain)&&snow)
            str="最近几个小时有降雪,可以出去玩雪了呢!!";
        else if(rain&&snow)
            str="最近几个小时有雨雪天气，出门记得带伞哦!!";

        if(str==null)
            return ;
        Log.i("service", "remind: "+"");

        Intent intent=new Intent(this,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
        NotificationManager manager=(NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        Notification notification=new NotificationCompat.Builder(this)
                .setContentTitle("提醒")
                .setContentText(str)
                .setSmallIcon(R.mipmap.icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.icon))
                .setDefaults(android.support.v7.app.NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();
        manager.notify(1,notification);

        Log.i("service", "remind: "+"通知应该已经发送");
    }

    private void get_data_from_server(){
        if(city_name.equals(""))
            return ;
        Net_http net_http=new Net_http();
        String datas=net_http.get_withNoThread(city_name);
        weather=Parse.parseWeather(datas);
    }

    private String city_name=null;

    private void get_selected_city() {
            SharedPreferences sharedPreferences = getSharedPreferences("selected_city", MODE_PRIVATE);
            city_name = sharedPreferences.getString("city_lbs", "");
            Log.i("service", "get_selected_city: " + city_name);
    }




    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        Log.i("service", "onStartCommand: "+"后台服务已经打开");
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences=getSharedPreferences
                        ("service",MODE_PRIVATE);
                long time_last=sharedPreferences.getLong("service_start_time",-1);
                long time_now= System.currentTimeMillis();
                Log.i("service", "run: "+time_last+"   "+time_now);
                double  time_between=(time_now-time_last)/3600.0/1000;
                if(time_last==-1||time_between>TIME_LONG)
                {
                    get_selected_city();
                    get_data_from_server();
                    remind();
                }
                if(weather!=null){
                    SharedPreferences.Editor editor=getSharedPreferences("service",MODE_PRIVATE)
                            .edit();
                    editor.putLong("service_start_time",time_now);
                    editor.apply();
                }
            }
        }).start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
