package com.example.chenlei.my_weather.dataBase;

import org.litepal.crud.DataSupport;

/**
 * Created by chenlei on 2017/9/3.
 */


//这是急速天气的数据类
public class CityFirst extends DataSupport{
    String cityid;
    String parentid;
    String citycode;
    String city;

    public CityFirst(){}

    public CityFirst(City city){
        parentid=city.getParentid();
        citycode=city.getCityId();
        this.city=city.getName();
    }
}
