package com.example.chenlei.my_weather.tool;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlei on 2017/8/30.
 */
//这个类用来保存添加的city，将对象转换成json数据，用sharedpreferences保存
public class ManagerCityList{
    private Context context;

    public ManagerCityList(Context context) {
        this.context = context;
    }

    private void fromSharedPreferences(Want want){
        SharedPreferences sharedPreferences=context.getSharedPreferences("selected_city",Context.MODE_PRIVATE);
        String data=sharedPreferences.getString("cityList",null);
        Gson gson=new Gson();
        CityList cityList = gson.fromJson(data,CityList.class);
        if (cityList==null)
            cityList=new CityList();
        want.toDo(cityList);
        data=gson.toJson(cityList);
        SharedPreferences.Editor editor=context.getSharedPreferences("selected_city",Context.MODE_PRIVATE).edit();
        editor.putString("cityList",data);
        editor.apply();
    }

    private interface Want{
        public void toDo(CityList cityList);
    }

    public String getJson() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("selected_city", Context.MODE_PRIVATE);
        return sharedPreferences.getString("cityList", null);
    }

    public void addCity(final String cityName){
       fromSharedPreferences(new Want() {
           @Override
           public void toDo(CityList cityList) {
               for(String city:cityList.getCities()){
                   if (city.equals(cityName))
                       return ;
               }
               cityList.addCity(cityName);
           }
       });
    }

    public void removeCity(final int position){
        if(position<0||position>=getCityList().size())
            return ;
        fromSharedPreferences(new Want() {
            @Override
            public void toDo(CityList cityList) {
                cityList.removeCity(position);
            }
        });
    }
    private List<String> cities=new ArrayList<>();

    public List<String> getCityList(){
        fromSharedPreferences(new Want() {
            @Override
            public void toDo(CityList cityList) {
                cities=cityList.getCities();
            }
        });
        return cities;
    }

    public void setCityList(final List<String> newCityIdList){
        fromSharedPreferences(new Want() {
            @Override
            public void toDo(CityList cityList) {
                cityList.setCities(newCityIdList);
            }
        });
    }
}
