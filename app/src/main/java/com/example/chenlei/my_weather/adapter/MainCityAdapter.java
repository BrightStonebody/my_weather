package com.example.chenlei.my_weather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.chenlei.my_weather.R;
import com.example.chenlei.my_weather.dataBase.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlei on 2017/9/10.
 */

public class MainCityAdapter extends RecyclerView.Adapter<MainCityAdapter.MainCityHolder>{

    private List<City> mainCities=new ArrayList<>();

    public MainCityAdapter(List<City> mainCities, Context context) {
        this.mainCities = mainCities;
        this.context = context;
    }

    private Context context=null;

    class MainCityHolder extends RecyclerView.ViewHolder{
        Button mainCity=null;

        public MainCityHolder(View itemView) {
            super(itemView);
            mainCity= (Button) itemView.findViewById(R.id.main_City);
        }
    }


    @Override
    public MainCityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_main_city,parent,false);
        return new MainCityHolder(view);
    }

    @Override
    public void onBindViewHolder(MainCityHolder holder, int position) {
        final City city=mainCities.get(position);
        holder.mainCity.setText(city.getName());
        holder.mainCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wantOnItemClick.wantOnItemClick(city.getCityId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mainCities.size();
    }

    private WantOnItemClick wantOnItemClick;

    public void setWantOnItemClick(WantOnItemClick wantOnItemClick){
        this.wantOnItemClick=wantOnItemClick;
    }

    public interface WantOnItemClick{
        void wantOnItemClick(String id);
    }
}
