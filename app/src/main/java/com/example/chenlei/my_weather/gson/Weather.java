package com.example.chenlei.my_weather.gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlei on 2017/9/4.
 */

public class Weather implements Serializable {
    public String date=new String();
    public String templow=new String();
    public String temp;
    public String img;
    public String week;
    public String city;
    public String windpower;
    public List<Index> index=new ArrayList<>();
    public Index airIndex=new Index();
    public String cityid;
    public String pressure;
    public String temphigh;
    public String citycode;
    public String winddirect;
    public List<Daily> daily=new ArrayList<>();
    public String weather;
    public Aqi aqi=new Aqi();
    public List<Hourly> hourly=new ArrayList<>();
    public String updatetime;

    public class Hourly {
        public String temp;
        public String time;
        public String weather;
    }

    public class Aqi {
        public String io3;
        public String iso2;
        public String ipm2_5;
        public String ipm10;
        public String ino2;
        public String ico;

        public String quality;
        public String aqi;
    }

    public class Daily {
        public String date;
        public String sunrise;
        public String week;
        public String sunset;
        public Night night=new Night();
        public Day day=new Day();

        public class Night {
            public String templow;
            public String weather;
        }

        public class Day {
            public String temphigh;
            public String weather;
        }
    }

    public class Index {
        public String ivalue;
        public String detail;
        public String iname;
    }
}

