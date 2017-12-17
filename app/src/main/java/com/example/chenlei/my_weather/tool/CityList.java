package com.example.chenlei.my_weather.tool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CityList implements Serializable {
    private List<String> cities=new ArrayList<>();

    public CityList(){}

    public CityList(List<String> cities) {
        this.cities = cities;
    }

    public void addCity(String city){
        cities.add(city);
    }

    public void removeCity(int position){
        cities.remove(position);
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }
}
