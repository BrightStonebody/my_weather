package com.example.chenlei.my_weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.chenlei.my_weather.adapter.CityManagerAdapter;
import com.example.chenlei.my_weather.dataBase.City;
import com.example.chenlei.my_weather.myViews.BlurView;
import com.example.chenlei.my_weather.myViews.TitleView;
import com.example.chenlei.my_weather.tool.ActivityCollector;
import com.example.chenlei.my_weather.tool.GetSystemHeight;
import com.example.chenlei.my_weather.tool.ManagerCityList;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


public class CityManagerActivity extends AppCompatActivity implements View.OnClickListener{

    private CityManagerAdapter adapter=null;
    private List<City> cities=new ArrayList<>();
    private BlurView background=null;
    private int backgroundId=R.mipmap.bg_sunny;
    private TitleView title=null;
    private Button edit=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);
        Button toAddCity = (Button) findViewById(R.id.toAddCity);
        RecyclerView cityList= (RecyclerView) findViewById(R.id.cityList);
        background= (BlurView) findViewById(R.id.backGround);
        title= (TitleView) findViewById(R.id.title);
        edit= (Button) findViewById(R.id.edit);
        edit.setOnClickListener(this);

        ActivityCollector.addActivity(this);
        ActivityCollector.setCityManagerActivity(this);

        setBackground();

        title.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toAddCity.setOnClickListener(this);

        //适配系统状态栏
        GetSystemHeight.adapterStatusBar(this,title.getRoot(),50);

        //初始化城市列表
        for(String id:managerCityList.getCityList()){
            List<City> city_=DataSupport.where("cityId = ?",id).find(City.class);
            City city=city_.get(0);
            cities.add(city);
        }

        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter=new CityManagerAdapter(cities,this);
        cityList.setAdapter(adapter);
        cityList.setLayoutManager(manager);
    }

    private ManagerCityList managerCityList=new ManagerCityList(this);

    private void setBackground(){
        Intent intent=getIntent();
        backgroundId=intent.getIntExtra("backgroundId",R.mipmap.bg_sunny);
        background.setImageResource(backgroundId,this);
        background.doBlur(100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!=RESULT_OK)
            return ;
        switch(requestCode){
            case 2:
                String add=data.getStringExtra("selected_city");
                for(City city:cities){
                    if(city.getCityId().equals(add)){
                        Toast.makeText(this, "不能重复添加城市哦！！", Toast.LENGTH_SHORT).show();
                        return ;
                    }
                }
                cities.add(City.getSingleCityFromId(add));
                adapter.notifyDataSetChanged();

                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.toAddCity:{
                if(adapter.ifCanDelete()){
                    adapter.setCanDelete(false);
                    textChange();
                }
                Intent intent=new Intent(CityManagerActivity.this,SelectCityActivity.class);
                intent.putExtra("backgroundId",backgroundId);
                startActivityForResult(intent,2);
            }
            break;
            case R.id.edit:{
                if(!adapter.ifCanDelete()) {
                    adapter.setCanDelete(true);
                }else{
                    adapter.setCanDelete(false);
                }
                textChange();
            }
            break;
        }
    }

    public void textChange(){
        String text=edit.getText().toString();
        if(text.equals("完成")){
            edit.setText("编辑");
        }else {
            edit.setText("完成");
        }
    }

    @Override
    public void onBackPressed() {
        if(adapter.ifCanDelete()) {
            adapter.setCanDelete(false);
            textChange();
        }
        else
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(this);
        ActivityCollector.removeCityManagerActivity();
        super.onDestroy();
    }
}
