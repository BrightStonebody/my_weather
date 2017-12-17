package com.example.chenlei.my_weather.tool;

import android.app.Activity;

import com.example.chenlei.my_weather.CityManagerActivity;
import com.example.chenlei.my_weather.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlei on 2017/9/6.
 */

public class ActivityCollector {
    private static CityManagerActivity cityManagerActivity;

    private static List<Activity> activityList=new ArrayList<>();

    private static MainActivity mainActivity;

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        ActivityCollector.mainActivity = mainActivity;
    }

    public static void removeMainActivity(){
        mainActivity=null;
    }

    public static CityManagerActivity getCityManagerActivity() {
        return cityManagerActivity;
    }

    public static void setCityManagerActivity(CityManagerActivity cityManagerActivity) {
        ActivityCollector.cityManagerActivity = cityManagerActivity;
    }

    public static void removeCityManagerActivity(){
        cityManagerActivity=null;
    }

    public static void addActivity(Activity activity){
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity){
        activityList.remove(activity);
    }

    public static void removeAll(){
        for(Activity activity:activityList){
            if(!activity.isFinishing())
                activity.finish();
        }
    }
}
