package com.example.chenlei.my_weather.tool;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlei on 2017/9/6.
 */

public class ListToJson {
    static class Temp {
        public List<String> list=new ArrayList<>();
    }

    public static String ListToJson(List<String> list) {
        Temp temp=new Temp();
        temp.list=list;
        Gson gson=new Gson();
        return gson.toJson(temp);
    }

    public static List<String> jsonToList(String json){
        Temp temp=new Temp();
        temp=(new Gson()).fromJson(json,Temp.class);
        return temp.list;
    }
}
