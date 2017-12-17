package com.example.chenlei.my_weather;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chenlei.my_weather.adapter.Forecast_dayAdapter;
import com.example.chenlei.my_weather.adapter.IndexAdapter;
import com.example.chenlei.my_weather.dataBase.City;
import com.example.chenlei.my_weather.dataBase.WeatherData;
import com.example.chenlei.my_weather.gson.Parse;
import com.example.chenlei.my_weather.gson.Weather;
import com.example.chenlei.my_weather.myViews.AirCircle;
import com.example.chenlei.my_weather.myViews.BlurView;
import com.example.chenlei.my_weather.myViews.BottomDailyView;
import com.example.chenlei.my_weather.myViews.DayForecastView;
import com.example.chenlei.my_weather.myViews.HourForecastView;
import com.example.chenlei.my_weather.myViews.MyPullToRefreshScrollView;
import com.example.chenlei.my_weather.myViews.MyScrollView;
import com.example.chenlei.my_weather.myViews.SunMoveView;
import com.example.chenlei.my_weather.tool.Font;
import com.example.chenlei.my_weather.tool.GetSystemHeight;
import com.example.chenlei.my_weather.tool.ManagerCityList;
import com.handmark.pulltorefresh.library.LoadingLayoutProxy;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.util.List;

public class CityFragment extends android.support.v4.app.Fragment {

    private TextView temperature = null;
    private TextView cond = null;
    private TextView air_qlty = null;
    private TextView suggestion = null;
    private RecyclerView forecast_day = null;
    private TextView max;
    private TextView min;
    private TextView publishTime = null;
    private TextView cityDetail = null;
    private MyPullToRefreshScrollView scrollView = null;
    private BlurView background = null;
    private TextView dark = null;
    private RecyclerView showIndexes = null;
    private HourForecastView hourForecastView = null;
    private DayForecastView dayForecastView = null;
    private CheckBox chooseShow = null;
    private HorizontalScrollView includeForecast = null;
    private ImageView air_image = null;
    private AirCircle airCircle = null;
    private TextView windPower = null;

    private FrameLayout mainView = null;

    private TextView aqi_pm10 = null;
    private TextView aqi_pm2_5 = null;
    private TextView aqi_no2 = null;
    private TextView aqi_so2 = null;
    private TextView aqi_o3 = null;
    private TextView aqi_co = null;
    private TextView air_aqi2 = null;
    private TextView air_qlty2 = null;
    private TextView bgChangeColor = null;
    private ProgressBar pending = null;
    private SunMoveView sunMove = null;
    private LinearLayout content = null;
    private BottomDailyView bottomDailyView_1 = null;
    private BottomDailyView bottomDailyView_2 = null;
    private ViewGroup containHourly=null;

    private int backgroundId = R.mipmap.bg_sunny;
    private WeatherData weatherData = new WeatherData();

    private float scale = 0;

    public CityFragment() {
    }


    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    private Activity getMyActivity() {
        return mActivity;
    }


    private int screenWidth = -1;
    private int screenHeight = -1;

    private CityFragment.ReceiverToChangeBg receiverToChangeBg = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("碎片", "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_city, container, false);
        //改变字体
        Font.changeFont((ViewGroup) view, getContext());

        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.chenlei.my_weather.changeBackgroundNow");
        receiverToChangeBg = new ReceiverToChangeBg();
        getMyActivity().registerReceiver(receiverToChangeBg, intentFilter);

        temperature = (TextView) view.findViewById(R.id.temperature);
        cond = (TextView) view.findViewById(R.id.weather);
        air_qlty = (TextView) view.findViewById(R.id.air_qlty);
        suggestion = (TextView) view.findViewById(R.id.suggestion);
        forecast_day = (RecyclerView) view.findViewById(R.id.forecast_day);
        max = (TextView) view.findViewById(R.id.max);
        min = (TextView) view.findViewById(R.id.min);
        publishTime = (TextView) view.findViewById(R.id.publish_time);
        cityDetail = (TextView) view.findViewById(R.id.cityDetail);
        scrollView = (MyPullToRefreshScrollView) view.findViewById(R.id.my_scrollview);
        background = (BlurView) view.findViewById(R.id.backGround);
        dark = (TextView) view.findViewById(R.id.dark);
        showIndexes = (RecyclerView) view.findViewById(R.id.show_index_list);
        aqi_pm10 = (TextView) view.findViewById(R.id.api_pm10);
        aqi_pm2_5 = (TextView) view.findViewById(R.id.api_pm2_5);
        aqi_so2 = (TextView) view.findViewById(R.id.api_so2);
        aqi_no2 = (TextView) view.findViewById(R.id.api_no2);
        aqi_o3 = (TextView) view.findViewById(R.id.api_o3);
        aqi_co = (TextView) view.findViewById(R.id.api_co);
        air_aqi2 = (TextView) view.findViewById(R.id.air_aqi2);
        air_qlty2 = (TextView) view.findViewById(R.id.air_qlty2);
        mainView = (FrameLayout) view.findViewById(R.id.main_view);
        hourForecastView = (HourForecastView) view.findViewById(R.id.forecastHour);
        dayForecastView = (DayForecastView) view.findViewById(R.id.forecastDay);
        chooseShow = (CheckBox) view.findViewById(R.id.choose_show);
        includeForecast = (HorizontalScrollView) view.findViewById(R.id.includeForecast);
        air_image = (ImageView) view.findViewById(R.id.air_image);
        airCircle = (AirCircle) view.findViewById(R.id.huan);
        bgChangeColor = (TextView) view.findViewById(R.id.bg_change_color);
        windPower = (TextView) view.findViewById(R.id.wind_power);
        pending = (ProgressBar) view.findViewById(R.id.pending);
        sunMove = (SunMoveView) view.findViewById(R.id.sun_move);
        content = (LinearLayout) view.findViewById(R.id.content);
        bottomDailyView_1 = (BottomDailyView) view.findViewById(R.id.bottom_daily_1);
        bottomDailyView_2 = (BottomDailyView) view.findViewById(R.id.bottom_daily_2);
        containHourly= (ViewGroup) view.findViewById(R.id.contain_hourly);

        scale = getResources().getDisplayMetrics().density;

        screenHeight = GetSystemHeight.getScreenHeight(getContext());
        screenWidth = GetSystemHeight.getScreenWidth(getContext());

        //对标题栏，主视图进行适配
        int titleHeight = (int) (60 * scale);
        LinearLayout.LayoutParams layout = (LinearLayout.LayoutParams) mainView.getLayoutParams();
        int statusBarHeight = GetSystemHeight.getStatusBarHeight(getContext());
        layout.height = screenHeight - titleHeight - statusBarHeight;
        int padding = GetSystemHeight.dptoPx(getContext(), 1);
        layout.setMargins(padding, padding, padding, padding);

        RelativeLayout.LayoutParams scrollLayout = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
        scrollLayout.setMargins(0, statusBarHeight + titleHeight, 0, 0);

        mainView.setLayoutParams(layout);

        //对下拉更新进行设置
        toRefresh();

        scrollView.getRefreshableView().setScrollViewListener(new MyScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
                changeBackground(y);
            }
        });


        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.getRefreshableView().scrollTo(0, 0);
            }
        });

        List<String> cityList = (new ManagerCityList(getContext())).getCityList();
        if (position < 0 || position > cityList.size()) {
            Log.e("错误", "onCreateView: " + "错误，position下标超限");
            return view;
        }

        //预先加上一个背景，以免出现白屏
        background.setImageResource(R.mipmap.bg_sunny, getMyActivity());

        cityId = cityList.get(position);

        content.setVisibility(View.GONE);
        //检查是否需要网络加载数据
        boolean needUpdate = false;
        weatherData = WeatherData.getWeatherDataFromDb(cityId);
        if (weatherData == null) {
            needUpdate = true;
            Log.e("错误", "onCreateView: " + "错误，CityFragment中weatherData为空");
        } else {
            load_data(weatherData.getJsonData());
            if (WeatherData.timeSubtract
                    (weatherData.getTime(), getContext())) {
                needUpdate = true;
            }
        }
        if (needUpdate) {
            scrollView.getRefreshableView().post(new Runnable() {
                @Override
                public void run() {
                    Log.i("更新", "run: " + "开始更新");
                    scrollView.firstRefreshing(true);
                }
            });
        }
        return view;
    }

    private void getWeatherData(final String cityId, boolean ifSkipCheck) {
        //网络请求更新数据

        ((MainActivity) getMyActivity()).getWeatherData(cityId, ifSkipCheck, new MainActivity.AfterGetWeatherDataFromServer() {
            @Override
            public void toDo(final Boolean ifSuccess) {

                getMyActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (ifSuccess) {
                            WeatherData weatherData = WeatherData.getWeatherDataFromDb(cityId);
                            if (weatherData != null) {
                                load_data(weatherData.getJsonData());
                            } else {
                                Log.e("错误", "toDo: " + "cityfragment中得到weatherdata竟然为空");
                            }
                        } else {
                            Log.e("错误", "toDo: CityFragment" + "错误，天气数据api出现问题");
                        }
                        scrollView.onRefreshComplete();
                    }
                });
            }
        });
    }

    public void toRefresh() {
        /**
         * 设置刷新的模式：常用的有三种
         * PullToRefreshBase.Mode.BOTH  //上下拉刷新都可以
         * PullToRefreshBase.Mode.PULL_FROM_START  //只允许下拉刷新
         * PullToRefreshBase.Mode.PULL_FROM_END   //只允许上拉刷新
         */
        scrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        //设置是否允许刷新的时候可以滑动
        scrollView.setScrollingWhileRefreshingEnabled(true);
        //layoutProxy初始化时候的两个参数，分别表示应用于哪里，
        // 第一个参数表示是否应用于刷新头部，第二个参数表示是否应用于尾部。
        LoadingLayoutProxy proxy = (LoadingLayoutProxy)
                scrollView.getLoadingLayoutProxy(true, false);
        proxy.setPullLabel("下拉更新");
        proxy.setReleaseLabel("松手可更新");
        proxy.setRefreshingLabel("正在更新...");
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<MyScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<MyScrollView> refreshView) {
                getWeatherData(cityId, true);
            }
        });
    }

    private int position = -1;
    private String cityId = null;

    public void setPosition(int position) {
        this.position = position;
    }

    public int getBackground() {
        return backgroundId;
    }

    public void returnState() {
        if (scrollView != null)
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.getRefreshableView().scrollTo(0, 0);
                    scrollView.getRefreshableView().smoothScrollTo(0, 0);
                }
            });
    }

    private void changeBackground(int height) {
        float mainViewHeight = mainView.getHeight();
        double start_dark = mainViewHeight / 3;
        double end_dark = mainViewHeight * 2 / 3;
        double alphaMax = 0.7;
        double blurMax = 100;

        if (height > start_dark && height < end_dark) {
            float alpha = (float) (
                    (height - start_dark) / (end_dark - start_dark) * alphaMax);
            float blur = (float)
                    ((height - start_dark) / (end_dark - start_dark) * blurMax);
            dark.setAlpha(alpha);
            background.doBlur((int) blur);

        } else if (height <= start_dark) {
            dark.setAlpha(0);
            background.doBlur(0);
        } else if (height >= end_dark) {
            dark.setAlpha((float) alphaMax);
            background.doBlur(100);
        }
    }

    private void load_data(String data) {
        content.setVisibility(View.VISIBLE);

        Weather weather = Parse.parseWeather(data);
        if (weather == null) {
            content.setVisibility(View.GONE);
            Log.e("错误", "load_data: 数据加载时，weather为null，天气json数据解析出现问题,json数据为");
            return;
        }
        load_background(weather);

        //加载城市详情
        City city = City.getSingleCityFromId(cityId);
        if (city != null)//防止position的位置越界
        {
            String detail = city.getCityCh() + "，" + city.getProvinceCh();
            cityDetail.setText(detail);
        }

        //加载天气数据
        windPower.setText(weather.windpower);
        publishTime.setText(weather.updatetime.substring(11, 16) + "发布");
//        temperature.setText(weather.hourly.get(0).temp);
        temperature.setText(weather.temp);
        cond.setText(weather.weather);
        if (weather.aqi != null) {
            String quality = weather.aqi.quality;
            if (quality.length() > 2)
                air_qlty.setText(quality.substring(0, 2));
            else
                air_qlty.setText(quality);
        }
//        if (weather.index.size() > 6) {
//            suggestion.setText(weather.index.get(5).detail);
//        }

        max.setText(weather.temphigh + "°");
        min.setText(weather.templow + "°");

        sunMove.sunMoveTo(weather.daily.get(0).sunrise, weather.daily.get(0).sunset);

        //加载天气预报信息
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        for (Weather.Daily forecast : weather.daily) {
            String date = forecast.date.substring(forecast.date.indexOf('-') + 1);
            forecast.date = date;
        }

        Forecast_dayAdapter forecast_dayAdapter = new
                Forecast_dayAdapter(weather.daily);
        Log.i("forecast", "run: " + weather.daily.size());
        forecast_day.setLayoutManager(manager);
        forecast_day.setAdapter(forecast_dayAdapter);

        //加载天气指数
        suggestion.setText(weather.airIndex.detail);
        final int[] imageArray = {
                R.mipmap.index_comf,
                R.mipmap.index_sport,
                R.mipmap.index_sun,
                R.mipmap.index_cold,
                R.mipmap.index_washcar,
                R.mipmap.index_clothes
        };
        if (weather.index.size() >= 6) {
            showIndexes.setVisibility(View.VISIBLE);
//            weather.index.remove(5);
            GridLayoutManager manager_index = new GridLayoutManager(getContext(), 3);
            IndexAdapter indexAdapter = new IndexAdapter(weather.index, imageArray, screenWidth);
            //设置indexadapter的item点击事件
            indexAdapter.setDoOnClick(new IndexAdapter.DoOnClick() {
                @Override
                public void doOnClick(int i) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("cityId", cityId);
                    intent.putExtra("index", i);
                    intent.putExtra("imageId", imageArray[i]);
                    intent.putExtra("background", backgroundId);
                    startActivity(intent);
                }
            });
            showIndexes.setAdapter(indexAdapter);
            showIndexes.setLayoutManager(manager_index);
        } else
            showIndexes.setVisibility(View.GONE);


        //加载空气质量信息
        aqi_o3.setText(weather.aqi.io3);
        aqi_no2.setText(weather.aqi.ino2);
        aqi_co.setText(weather.aqi.ico);
        aqi_pm2_5.setText(weather.aqi.ipm2_5);
        aqi_pm10.setText(weather.aqi.ipm10);
        aqi_so2.setText(weather.aqi.iso2);
        air_qlty2.setText(weather.aqi.quality);
        air_aqi2.setText(weather.aqi.aqi);
        //改变空气质量图标颜色
        changeAirColor(weather.aqi.quality);
        airCircle.setData(weather.aqi.quality);

        //加载24小时天气预报图表
        addHourForecast(weather.hourly);
        //加载一周天气预报图表
        bottomDailyView_1.initDate(weather.daily.get(1));
        bottomDailyView_2.initDate(weather.daily.get(2));
        addDayForecast(weather.daily);
        //设置选择图标
        chooseShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    forecast_day.setVisibility(View.GONE);
                    includeForecast.setVisibility(View.VISIBLE);
                } else {
                    forecast_day.setVisibility(View.VISIBLE);
                    includeForecast.setVisibility(View.GONE);
                }
            }
        });

    }

    private void load_background(Weather weather) {
        backgroundId=R.mipmap.bg_sunny;
        if(weather!=null) {
            if (weather.weather.contains("晴")
                    || weather.weather.contains("多云"))
                backgroundId = R.mipmap.bg_sunny;
            else if (weather.weather.contains("阴"))
                backgroundId = R.mipmap.bg_cloudy;
            else if (weather.weather.contains("雪"))
                backgroundId = R.mipmap.bg_snowy;
            else if (weather.weather.contains("雨"))
                backgroundId = R.mipmap.bg_rainy;
            else
                backgroundId = R.mipmap.bg_sunny;
        }
        background.setImageResource(backgroundId, getMyActivity());
    }

    private void changeAirColor(String quality) {
        int imageId;
        if (quality.equals("优"))
            imageId = R.mipmap.air_you;
        else if (quality.equals("良"))
            imageId = R.mipmap.air_liang;
        else if (quality.equals("轻度污染"))
            imageId = R.mipmap.air_qingdu;
        else if (quality.equals("中度污染"))
            imageId = R.mipmap.air_zhongdu;
        else if (quality.equals("重度污染"))
            imageId = R.mipmap.air_chongdu;
        else if (quality.equals("严重污染"))
            imageId = R.mipmap.air_yanzhong;
        else imageId = R.mipmap.air_liang;
        air_image.setImageResource(imageId);
    }

    private void addHourForecast(List<Weather.Hourly> datas) {
        if(!(datas!=null&&datas.size()!=0)){
            containHourly.setVisibility(View.GONE);
            return ;
        }
        LinearLayout.LayoutParams layoutParams =
                (LinearLayout.LayoutParams) hourForecastView.getLayoutParams();
        layoutParams.width = (int) (scale * datas.size() * 50 + 20);
        hourForecastView.setLayoutParams(layoutParams);
        hourForecastView.setDatas(datas);
    }

    private void addDayForecast(List<Weather.Daily> datas) {
        LinearLayout.LayoutParams layoutParams =
                (LinearLayout.LayoutParams) dayForecastView.getLayoutParams();
        layoutParams.width = (int) (scale * datas.size() * 60 + 20);
        Log.i("预报", "addDayForecast: " + layoutParams.width);
        dayForecastView.setLayoutParams(layoutParams);
        dayForecastView.setDatas(datas);
    }

    @Override
    public void onDestroyView() {
        returnState();
        getMyActivity().unregisterReceiver(receiverToChangeBg);
        super.onDestroyView();
    }

    public class ReceiverToChangeBg extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("广播", "onReceive: " + "已接收到广播");
            if (weatherData == null) {
                load_background(null);
                return;
            }
            Weather weather = Parse.parseWeather(weatherData.getJsonData());
            load_background(weather);

        }
    }
}
