package com.example.chenlei.my_weather.LBS;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.chenlei.my_weather.dataBase.City;

import org.litepal.crud.DataSupport;

/**
 * Created by chenlei on 2017/8/28.
 */

public class Lbs {

    private AMapLocationClient mLocationClient = null;

    private String city_now=null;

    private Context context;


    public Lbs(Context context){
        this.context=context;
        mLocationClient = new AMapLocationClient(context.getApplicationContext());
        AMapLocationClientOption option=new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setOnceLocationLatest(true);
        //option.setInterval(2000);
        mLocationClient.setLocationOption(option);
    }


    private AMapLocationListener mLocationListener=null;

    public void setAMaplocationListener(final WantAfterLocate wantAfterLocate){

        mLocationListener=new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                Log.i("定位", "onLocationChanged: "+"定位开始");
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {

                        city_now=amapLocation.getCity();

                        String countyLbs=amapLocation.getDistrict();
                        String cityLbs=amapLocation.getCity();
                        String provinceLbs=amapLocation.getProvince();
                        Log.i("定位", "onLocationChanged: "+countyLbs+"  "+provinceLbs+"  "+cityLbs);
                        //除去定位中的省、市、县、区等词，防止对后面的查询产生干扰
                        countyLbs=City.simpleCityName(countyLbs);
                        provinceLbs=City.simpleCityName(provinceLbs);
                        cityLbs=City.simpleCityName(cityLbs);

                        //防止有些地方查找不到地名，直接查找地级市
                        City temp= DataSupport
                                .where("name = ? and cityCh = ? and provinceCh = ?",countyLbs,cityLbs,provinceLbs)
                                .findFirst(City.class);
                        if(temp==null)
                            Log.i("定位", "onLocationChanged: "+countyLbs+"没有找到");

                        City city=null;
                        if(temp!=null&&!temp.getCityId().equals(""))
                            city=temp;
                        else{
                            City temp2=DataSupport.where("name = ? and provinceCh = ?",
                                    cityLbs,provinceLbs)
                                    .findFirst(City.class);
                            city=temp2;
                        }
                        if(city==null)
                            Log.e("错误", "onLocationChanged: "+"错误，没有在数据库中找到定位城市");

                        SharedPreferences.Editor editor = context.getSharedPreferences("selected_city", Context.MODE_PRIVATE)
                                .edit();
                        editor.putString("city_lbs", city.getCityId());
                        editor.apply();
                        if(wantAfterLocate!=null)
                            wantAfterLocate.wantAfterLocate(amapLocation);

                    }else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError","location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }
            }
        };

        mLocationClient.setLocationListener(mLocationListener);
    }

    public interface WantAfterLocate{
        void wantAfterLocate(AMapLocation aMapLocation);
    }

    public void startLocation(){
        mLocationClient.startLocation();
    }

    public void stopLocation(){
        mLocationClient.stopLocation();
    }

    public String getLbsCityId(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("selected_city", Context.MODE_PRIVATE);
        String cityId = sharedPreferences.getString("city_lbs", null);
        return cityId;
    }
}
