package com.example.chenlei.my_weather.dataBase;

import android.util.Log;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlei on 2017/7/20.
 */

public class City extends DataSupport implements Serializable{

    private long id;
    private String parentid;
    private String cityId;
    private String englishName;
    private String name;
    private String countryId;
    private String countryEn;
    private String countryCh;
    private String provinceEn;
    private String provinceCh;
    private String cityEn;
    private String cityCh;
    private String latitude;
    private String longitude;


    public static String simpleCityName(String cityName){
        String simpleCityName=cityName;
        if(cityName.contains("县") ||cityName.contains("区")
                ||cityName.contains("市")||cityName.contains("省")) {
            if (cityName.length() > 2)
                return simpleCityName.substring(0, simpleCityName.length()-1);
            else
                return simpleCityName;
        }
        else
            return simpleCityName;
    }

    public static  List<City> getCityFromId(List<String> idList){
        List<City> cities=new ArrayList<>();
        for(String id:idList){
            List<City> temp= DataSupport.where("cityId = ?",id).find(City.class);
            City city=temp.get(0);
            cities.add(city);
        }
        if(cities.isEmpty()){
            Log.e("错误", "getCityFromId: "+"数据库错误，无法找到城市");
        }
        return cities;
    }

    public static City getSingleCityFromId(String id){
        List<City> temp= DataSupport.where("cityId = ?",id).find(City.class);
        if(temp==null||temp.isEmpty()) {
            Log.e("错误", "getSingleCityFromId: "+"数据库错误，无法找到城市");
            return new City();
        } else
             return temp.get(0);
    }

    @Override
    public String toString(){
        return cityId+englishName+name+countryId+countryEn+countryCh+provinceEn
                +provinceCh+cityEn+cityCh+latitude+longitude;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null)
            return false;
        if(getClass()!=obj.getClass())
            return false;
        City other= (City) obj;
        return cityId.equals(other.cityId);
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public long  getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryEn() {
        return countryEn;
    }

    public void setCountryEn(String countryEn) {
        this.countryEn = countryEn;
    }

    public String getCountryCh() {
        return countryCh;
    }

    public void setCountryCh(String countryCh) {
        this.countryCh = countryCh;
    }

    public String getProvinceEn() {
        return provinceEn;
    }

    public void setProvinceEn(String provinceEn) {
        this.provinceEn = provinceEn;
    }

    public String getProvinceCh() {
        return provinceCh;
    }

    public void setProvinceCh(String provinceCh) {
        this.provinceCh = provinceCh;
    }

    public String getCityEn() {
        return cityEn;
    }

    public void setCityEn(String cityEn) {
        this.cityEn = cityEn;
    }

    public String getCityCh() {
        return cityCh;
    }

    public void setCityCh(String cityCh) {
        this.cityCh = cityCh;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
