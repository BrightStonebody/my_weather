package com.example.chenlei.my_weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.chenlei.my_weather.LBS.Lbs;
import com.example.chenlei.my_weather.adapter.PageAdapter;
import com.example.chenlei.my_weather.adapter.SpotListAdapter;
import com.example.chenlei.my_weather.dataBase.City;
import com.example.chenlei.my_weather.dataBase.WeatherData;
import com.example.chenlei.my_weather.gson.Parse;
import com.example.chenlei.my_weather.gson.Weather;
import com.example.chenlei.my_weather.service.AutoRemindService;
import com.example.chenlei.my_weather.tool.ActivityCollector;
import com.example.chenlei.my_weather.tool.Font;
import com.example.chenlei.my_weather.tool.GetSystemHeight;
import com.example.chenlei.my_weather.tool.ManagerCityList;
import com.example.chenlei.my_weather.tool.Net_http;
import com.example.chenlei.my_weather.tool.Pending;
import com.example.chenlei.my_weather.tool.StoreBlurPic;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton choose_city = null;
    private ImageButton set = null;
    private Button city = null;
    private ViewPager pager = null;
    private PageAdapter pageAdapter = null;
    private SpotListAdapter spotListAdapter = null;
    private FrameLayout title = null;

    private RecyclerView showSpots = null;

    private Pending pending = new Pending(MainActivity.this);
    private Lbs lbs = null;
    private ManagerCityList managerCityList = new ManagerCityList(this);
    private List<City> cities = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<Boolean> spotsList = new ArrayList<>();
    private List<String> citiesId = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
//                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        LitePal.getDatabase();

        ViewGroup root = (ViewGroup) getWindow().getDecorView();
        //Font.changeFont(findViewById(R.id.root),"fonts/Roboto-Light.ttf");
        //改变字体
        Font.changeFont(root, this);
        //启动活动
        Intent intent = new Intent(this, AutoRemindService.class);
        startService(intent);

        choose_city = (ImageButton) findViewById(R.id.choose_city);
        set = (ImageButton) findViewById(R.id.set);
        city = (Button) findViewById(R.id.selected_city);
        pager = (ViewPager) findViewById(R.id.citiesPager);
        showSpots = (RecyclerView) findViewById(R.id.show_spots);
        choose_city.setOnClickListener(this);
        set.setOnClickListener(this);
        city.setOnClickListener(this);
        title = (FrameLayout) findViewById(R.id.title);
        ImageButton addCity = (ImageButton) findViewById(R.id.addCity);
        addCity.setOnClickListener(this);

        ActivityCollector.addActivity(this);
        ActivityCollector.setMainActivity(this);

        //适配系统状态栏
        GetSystemHeight.adapterStatusBar(this, title, 70);

        //友盟统计??不知道有没有用
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        UMConfigure.setLogEnabled(true);

        spotListAdapter = new SpotListAdapter(spotsList);
        final LinearLayoutManager recyclerViewManager = new LinearLayoutManager(this);
        recyclerViewManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        showSpots.setAdapter(spotListAdapter);
        showSpots.setLayoutManager(recyclerViewManager);
        pageAdapter = new PageAdapter(getSupportFragmentManager(), fragmentList);

        pager.setOffscreenPageLimit(1);
        pager.setAdapter(pageAdapter);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                city.setText(City.simpleCityName
                        (cities.get(position).getName()));

                spotsList.set(position, true);
                for (int i = 0; i < spotsList.size(); i++) {
                    if (i != position)
                        spotsList.set(i, false);
                }
                spotListAdapter.notifyDataSetChanged();
                //外部保存当前页面位置
                pagePosition = position;

                lastPage = position;

            }

            @Override

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        lbs = new Lbs(this);
        lbs.setAMaplocationListener(null);
        lbs.startLocation();

        storeBlurPic();
        if (managerCityList.getCityList().size() == 0) {
            intent = new Intent(MainActivity.this, SelectCityActivity.class);
            startActivityForResult(intent, 2);
        } else
            init();
    }

    private void init() {

        citiesId = managerCityList.getCityList();

        //初始化城市列表
        cities = City.getCityFromId(managerCityList.getCityList());

        //初始化 点
        for (int i = 0; i < cities.size(); i++)
            spotsList.add(false);

        if (cities.size() != 0) {
            city.setText(City.simpleCityName
                    (cities.get(0).getName()));
            spotsList.set(0, true);
        }

        for (int i = 0; i < citiesId.size(); i++) {
            CityFragment fragment = new CityFragment();
            fragment.setPosition(i);
            fragmentList.add(fragment);
        }

        pageAdapter.notifyDataSetChanged();
        spotListAdapter.notifyDataSetChanged();

    }

    private int pagePosition = 0;
    private int lastPage = 0;


    public void trunToPage(int position) {
        if (position > cities.size() - 1)
            return;
        pager.setCurrentItem(position);
        pagePosition = position;
    }

    public void deleteCity(final int j) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (j >= cities.size()) {
                    Log.e("错误", "run: " + "错误，下标j越界，在deleteCity函数中");
                    return;
                }
                fragmentList.remove(j);
                managerCityList.removeCity(j);

                cities.remove(j);
                citiesId.remove(j);

                //更新防止错乱
                //防止pagePosition超出页表的个数
                if (j < pagePosition)
                    pagePosition--;
                if (j < lastPage)
                    lastPage--;

                Log.i("页面", "run: " + pagePosition);

                //nonetfragment也有position属性，这个属性同样很重要
                for (int i = 0; i < fragmentList.size(); i++) {
                    ((CityFragment) fragmentList.get(i)).setPosition(i);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pageAdapter.notifyDataSetChanged();
                        flushTheSpots();
                    }
                });
            }
        }).start();
    }

    private void flushTheSpots() {
        spotsList.clear();
        for (int i = 0; i < cities.size(); i++) {
            spotsList.add(false);
        }
        //更新标题栏上的城市名

        Log.i("页面", "flushTheSpots: " + pagePosition);
        if (pagePosition < 0 || pagePosition > cities.size() - 1) {
            Log.e("错误", "flushTheSpots: 错误，pageposition下标超限");
            pagePosition = cities.size() - 1;
        }
        city.setText(City.simpleCityName
                (cities.get(pagePosition).getName()));

        //更新 点
        spotsList.set(pagePosition, true);
        spotListAdapter.notifyDataSetChanged();
    }

    private void addTheNewCity(String newCityId) {
        if (newCityId != null) {
            citiesId.add(newCityId);

            //对新的城市进行数据请求加载，防止城市管理页面天气数据为空

            getWeatherData(newCityId, false, null);

            City newCity = City.getSingleCityFromId(newCityId);
            cities.add(newCity);
            CityFragment cityFragment = new CityFragment();
            cityFragment.setPosition(fragmentList.size());
            fragmentList.add(cityFragment);
            pageAdapter.notifyDataSetChanged();
        }
    }

    public void getWeatherData(final String cityId, boolean ifSkipCheck
            , final AfterGetWeatherDataFromServer afterGetWeatherDataFromServer) {
        WeatherData weatherData = WeatherData.getWeatherDataFromDb(cityId);
        boolean ifOutOfDate = false;
        if (weatherData == null) {
            weatherData = new WeatherData();
            ifOutOfDate = true;
        } else {
            if (WeatherData.timeSubtract
                    (weatherData.getTime(), this))
                ifOutOfDate = true;
        }
        //如果没有设置跳过过期检查 或者 没有过期，则不必网络查询
        if (!(ifOutOfDate || ifSkipCheck))
            return;

        final WeatherData finalWeatherData = weatherData;

        Net_http net_http = new Net_http();
        net_http.get(cityId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请检查网络连接！！", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
                if (afterGetWeatherDataFromServer != null)
                    afterGetWeatherDataFromServer.toDo(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String data = response.body().string();
                Weather weather = Parse.parseWeather(data);
                boolean ifSuccess = false;
                if (weather != null) {
                    finalWeatherData.setCityId(cityId);
                    finalWeatherData.setJsonData(data);
                    finalWeatherData.setTime(System.currentTimeMillis());
                    finalWeatherData.save();
                    ifSuccess = true;
                } else {
                    ifSuccess = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "网络数据解析失败，请尝试刷新。。", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                    Log.e("错误", "onResponse: 错误，天气数据请求时json数据解析出现问题");
                }
                if (afterGetWeatherDataFromServer != null)
                    afterGetWeatherDataFromServer.toDo(ifSuccess);
            }
        });
    }

    public interface AfterGetWeatherDataFromServer {
        void toDo(Boolean ifSuccess);
    }

    @Override
    public void onClick(View view) {

        int backgroundId = R.mipmap.bg_sunny;
        //在阿里的测试中出现的问题
        if (fragmentList.size() != 0) {
            backgroundId = ((CityFragment) fragmentList.get(pagePosition)).getBackground();
        }

        switch (view.getId()) {
            case R.id.choose_city: {
                Intent intent = new Intent(MainActivity.this, CityManagerActivity.class);
                intent.putExtra("backgroundId", backgroundId);
                startActivityForResult(intent, 1);
            }
            break;
            case R.id.set: {
                Intent intent = new Intent(MainActivity.this, SetActivity.class);
                intent.putExtra("backgroundId", backgroundId);
                startActivity(intent);
            }
            break;
            case R.id.selected_city:
            case R.id.addCity: {
                Intent intent = new Intent(MainActivity.this, SelectCityActivity.class);
                intent.putExtra("backgroundId", backgroundId);
                startActivityForResult(intent, 2);
            }
            break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //当从其他页面返回到mainactivity时，检测是否新的城市添加
        for (String id : managerCityList.getCityList()) {
            if (citiesId.indexOf(id) == -1) {
                addTheNewCity(id);
                flushTheSpots();
            }
        }
    }

    @Override
    protected void onDestroy() {
        lbs.stopLocation();
//        if (Util.isOnMainThread()) {
//            Glide.with(this).pauseRequests();
//        }
        ActivityCollector.removeActivity(this);
        ActivityCollector.removeMainActivity();
        super.onDestroy();
    }

    private void storeBlurPic() {
        final StoreBlurPic storeBlurPic = new StoreBlurPic(this);
        pending.showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                storeBlurPic.store();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pending.closeProgressDialog();
                    }
                });
            }
        }).start();
    }

}

