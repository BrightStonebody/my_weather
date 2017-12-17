package com.example.chenlei.my_weather;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.example.chenlei.my_weather.LBS.Lbs;
import com.example.chenlei.my_weather.adapter.MainCityAdapter;
import com.example.chenlei.my_weather.dataBase.City;
import com.example.chenlei.my_weather.dataBase.DBHelper;
import com.example.chenlei.my_weather.myViews.BlurView;
import com.example.chenlei.my_weather.myViews.TitleView;
import com.example.chenlei.my_weather.tool.ActivityCollector;
import com.example.chenlei.my_weather.tool.Check;
import com.example.chenlei.my_weather.tool.GetSystemHeight;
import com.example.chenlei.my_weather.tool.ManagerCityList;
import com.example.chenlei.my_weather.tool.Pending;
import com.example.chenlei.my_weather.tool.StoreBlurPic;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class SelectCityActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputCity = null;
    private ListView showAboutCity = null;
    private BaseAdapter arrayAdapter = null;
    private TextView noCity = null;
    private RecyclerView showMainCities = null;
    private Button get_lbs_city = null;
    private RelativeLayout show_city=null;
    private LinearLayout show_search=null;
    private BlurView background=null;
    private TitleView title=null;

    private List<String> aboutCityList = new ArrayList<>();
    private List<City> showCities = new ArrayList<>();

    private Lbs lbs = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("后台", "onCreate: " + "111");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        inputCity = (EditText) findViewById(R.id.inputCity);
        get_lbs_city = (Button) findViewById(R.id.get_lbs_city);
        showAboutCity = (ListView) findViewById(R.id.showAboutCity);
        noCity = (TextView) findViewById(R.id.noCity);
        showMainCities = (RecyclerView) findViewById(R.id.mainCities);
        show_city=(RelativeLayout) findViewById(R.id.show_cities);
        show_search= (LinearLayout) findViewById(R.id.show_search);
        background= (BlurView) findViewById(R.id.backGround);
        title= (TitleView) findViewById(R.id.title);

        ActivityCollector.addActivity(this);

        title.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        get_lbs_city.setOnClickListener(this);

        //适配系统状态栏
        GetSystemHeight.adapterStatusBar(this,title.getRoot(),50);

        askPermissions();

        arrayAdapter = new ArrayAdapter<>(this
                ,R.layout.item_about_cities,aboutCityList);
        showAboutCity.setAdapter(arrayAdapter);

        //对输入内容进行动态监听
        inputCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                aboutCityList.clear();
                showCities.clear();
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = inputCity.getText().toString();
                if (input.equals("")) {
                    show_search.setVisibility(View.GONE);
                    show_city.setVisibility(View.VISIBLE);
                    return;
                }
                else{
                    show_search.setVisibility(View.VISIBLE);
                    show_city.setVisibility(View.GONE);
                }
                List<City> cities = DataSupport.where("name like ?", "%" + input + "%")
                        .find(City.class);
                if (cities.isEmpty()) {
                    noCity.setVisibility(View.VISIBLE);
                    return;
                }
                else if(cities.size()==1&&(
                        cities.get(0).getCityId()==null
                                ||cities.get(0).getCityId().equals(""))){
                    noCity.setVisibility(View.VISIBLE);
                    return ;
                }
                else
                    noCity.setVisibility(View.GONE);

                for (City city : cities) {
                    if (city.getCityId().equals("") || city.getCityId() == null)
                        continue;
                    showCities.add(city);
                    String show = city.getName() + "，" + city.getCityCh() + "，"
                            + city.getProvinceCh();
                    aboutCityList.add(show);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });

        showAboutCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                City city = showCities.get(i);
                if (checkRepeat(city.getCityId())) {
                    sendResult(city.getCityId());
                    finish();
                }
            }
        });

        Check.checkVersion(this);
        setBackground();

        showLbs();

        lbs = new Lbs(this);
        lbs.setAMaplocationListener(new Lbs.WantAfterLocate() {
            @Override
            public void wantAfterLocate(final AMapLocation aMapLocation) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showLbs();
                    }
                });
            }
        });

        DBHelper dBHelper = new DBHelper(this);

        //加载主要城市列表
        if (dBHelper.checkIfStore())
            loadMainCities();

        //第一次打开应用加载数据库
        dBHelper.loadDb(new DBHelper.WantDoAfter() {
            @Override
            public void wantDoAfter() {
                lbs.startLocation();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadMainCities();
                    }
                });
            }
        });

        storeBlurPic();

        lbs.startLocation();
    }

    private void setBackground(){
        Intent intent=getIntent();
        int backgroundId=intent.getIntExtra("backgroundId",R.mipmap.bg_sunny);
        background.setImageResource(backgroundId,this);
        background.doBlur(100);
    }

    private void showLbs() {
        String lbsId = (new Lbs(this)).getLbsCityId();
        City cityLbs = null;
        if (lbsId == null)
            return;
        cityLbs = City.getSingleCityFromId(lbsId);
        cityLbs.setName(City.simpleCityName(cityLbs.getName()));
        if (cityLbs.getName() != null)
            get_lbs_city.setText("定位：" + cityLbs.getName());
    }

    private void loadMainCities() {
        //加载主要城市列表
        Log.i("主要城市", "loadMainCities: "+"加载主要城市");
        List<City> mainCityList = new ArrayList<>();
//        String[] mainCityIdList = {
//                "101010100", "101020100", "101280101", "101280601", "101200101"
//                , "101190101", "101210101", "101110101", "101180101", "101270101"
//                , "101070101", "101030100", "101040100", "101250101", "101050101"
//        };
        String[] mainCityIdList={
          "CN101010100", "CN101280601","CN101210101","CN101270101","CN101040100",
          "CN101020100","CN101200101","CN101060705","CN101070101","CN101250101",
          "CN101280101", "CN101190101","CN101180101","CN101030100","CN101050101"
        };
        for (String id : mainCityIdList) {
            List<City> citiesList = DataSupport.where("cityId=?", id).find(City.class);
            if (citiesList.isEmpty())
                return;
            City city = citiesList.get(0);
            mainCityList.add(city);
            Log.i("主要城市", "loadMainCities: "+city.toString());
        }
        MainCityAdapter mainCityAdapter = new MainCityAdapter(mainCityList, this);
        mainCityAdapter.setWantOnItemClick(new MainCityAdapter.WantOnItemClick() {
            @Override
            public void wantOnItemClick(String id) {
                if (checkRepeat(id)) {
                    sendResult(id);
                    finish();
                }
            }
        });

        showMainCities.setAdapter(mainCityAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(SelectCityActivity.this, 3);
        showMainCities.setLayoutManager(layoutManager);
        mainCityAdapter.notifyDataSetChanged();
    }


    private boolean checkRepeat(String id) {
        ManagerCityList managerCityList = new ManagerCityList(SelectCityActivity.this);
        final List<String> storeCityList = (managerCityList).getCityList();
        for (String temp : storeCityList) {
            if (temp.equals(id)) {
                Toast.makeText(SelectCityActivity.this, "不可重复添加城市", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_lbs_city: {
                Lbs lbs = new Lbs(SelectCityActivity.this);
                String cityId = lbs.getLbsCityId();
                if(cityId==null){
                    Toast.makeText(SelectCityActivity.this, "没有找到定位", Toast.LENGTH_SHORT).show();
                    break;
                }
                else if (!checkRepeat(cityId))
                    break;
                else {
                    sendResult(cityId);
                    finish();
                }
            }
            break;
        }
    }

    private void sendResult(String cityId) {
        Intent intent = new Intent();
        intent.putExtra("selected_city", cityId);
        setResult(RESULT_OK, intent);
        //以上 加上也没关系，没什么作用，以前残留的代码

        ManagerCityList managerCityList=new ManagerCityList(this);
        if(managerCityList.getCityList().size()>=9) {
            Toast.makeText(this, "不能添加再多城市了...", Toast.LENGTH_SHORT).show();
            return ;
        }
        managerCityList.addCity(cityId);

        if (ActivityCollector.getCityManagerActivity() != null
                && !ActivityCollector.getCityManagerActivity().isFinishing())
            ActivityCollector.getCityManagerActivity().finish();
    }

    @Override
    public void onBackPressed() {
        ManagerCityList managerCityList = new ManagerCityList(this);
        if (managerCityList.getCityList().isEmpty()) {
            ActivityCollector.removeAll();
        }
        super.onBackPressed();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        int result = grantResults[i];
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Log.i("权限获取", "onRequestPermissionsResult: " + permissions.length + "  " + grantResults.length);
                            Toast.makeText(this, "必须同意所有权限才能使用本应用", Toast.LENGTH_SHORT).show();
                            ActivityCollector.removeAll();
                            return;
                        } else {
                            if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)
                                    || permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                                lbs.startLocation();
                            }
                        }
                    }
                }
                break;
        }
    }


    public void askPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE};
        List<String> permissionList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i])
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[i]);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionList.toArray(new String[permissionList.size()]), 1);
        }
    }

    @Override
    protected void onDestroy() {
        lbs.stopLocation();
        ActivityCollector.removeActivity(this);
        super.onDestroy();
    }

    private void storeBlurPic() {
        final StoreBlurPic storeBlurPic = new StoreBlurPic(this);
        final Pending pending=new Pending(this);
        pending.showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                storeBlurPic.store();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pending.closeProgressDialog();
                        background.doBlur(100);
                    }
                });
            }
        }).start();
    }
}

