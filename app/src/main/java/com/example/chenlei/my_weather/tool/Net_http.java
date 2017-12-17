package com.example.chenlei.my_weather.tool;

import android.util.Log;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by chenlei on 2017/7/18.
 */

public class Net_http {

//    和风天气
//    final private static String ip="https://free-api.heweather.com/v5/weather";
//    final private static String key="08b14a946e9b439c9bc19034b49ee1fa";

    //急速数据
//   final private static String ip = "http://api.jisuapi.com/weather/query";
//    final private static String key="e5842ca7614fc0a1";

    //京东和风天气
    final private static String ip="https://way.jd.com/he/freeweather";
    final private static String key="e4811d819d4efd2373e0d73fbaca45bd";

    public void get(String cityName, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        String address=ip+"?city="+cityName+"&appkey="+key;
        Log.i("地址", "get: " + address);
        Request request = new Request.Builder()
//                .url(ip+address)
                //.url(address)
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public String get_withNoThread(String cityName) {
        String datas = null;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            String address = ip + "?city=" + cityName + "&appkey=" + key;
            Log.i("地址", "get: " + address);
            Request request = new Request.Builder()
//                    .url(address)
                    .url(address)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            datas = response.body().string();
            return datas;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
