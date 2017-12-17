package com.example.chenlei.my_weather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenlei.my_weather.LBS.Lbs;
import com.example.chenlei.my_weather.R;
import com.example.chenlei.my_weather.dataBase.City;
import com.example.chenlei.my_weather.dataBase.WeatherData;
import com.example.chenlei.my_weather.gson.Parse;
import com.example.chenlei.my_weather.gson.Weather;
import com.example.chenlei.my_weather.tool.ActivityCollector;
import com.example.chenlei.my_weather.tool.WeatherToIcon;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by chenlei on 2017/8/30.
 */

public class CityManagerAdapter extends RecyclerView.Adapter<CityManagerAdapter.ViewHolder> {

    private List<City> cityList = new ArrayList<>();
    private Context context = null;
    private boolean canDelete = false;

    public CityManagerAdapter(List<City> cityList, Context context) {
        this.cityList = cityList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_manager_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int pos) {
        final int position = holder.getLayoutPosition();
        holder.locate.setVisibility(View.INVISIBLE);
        Lbs lbs = new Lbs(context);
        String cityLbsId = lbs.getLbsCityId();
        Log.i("显示定位", "onBindViewHolder: " + cityList.get(position).getName() + "  "
                + cityList.get(position).getCityId() + "  " + cityLbsId);
        if (cityList.get(position).getCityId().equals(cityLbsId))
            holder.locate.setVisibility(View.VISIBLE);
        holder.city_name.setText(City.simpleCityName
                (cityList.get(position).getName()));
        String detail = cityList.get(position).getProvinceCh();
        holder.city_detail.setText(detail);

        WeatherData weatherData = WeatherData.getWeatherDataFromDb(cityList.get(position).getCityId());
        Weather weather = null;
        if (weatherData == null) {
            Log.e("错误", "onBindViewHolder:  cityManagerAdapter " + "recyclerview中天气数据获取失败");
        } else
            weather = Parse.parseWeather
                    (weatherData.getJsonData());

        if (weather != null) {
            holder.temp.setText(weather.hourly.get(0).temp + "°");
            holder.weather.setImageResource(WeatherToIcon.weatherToIcon
                    (weather.weather));
        } else {
            holder.temp.setText("--");
            holder.weather.setImageResource(R.mipmap.cloudy);
        }

        //单击回到指定viewpager页面
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCollector.getMainActivity() != null) {
                    if (!(ActivityCollector.getMainActivity()).isFinishing()) {
                        (ActivityCollector.getMainActivity()).trunToPage(position);
                    }
                }
                if(ActivityCollector.getCityManagerActivity()!=null) {
                    if (!ActivityCollector.getCityManagerActivity().isFinishing())
                        (ActivityCollector.getCityManagerActivity()).finish();
                }
            }
        });

        //长按弹出删除按钮
        if (canDelete) {
            holder.itemView.setLongClickable(false);
            holder.itemView.setOnLongClickListener(null);
        } else {
            holder.itemView.setLongClickable(true);
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    setCanDelete(true);
                    (ActivityCollector.getCityManagerActivity()).textChange();
                    return true;
                }
            });
        }

        if (canDelete)
            holder.delete.setVisibility(View.VISIBLE);
        else
            holder.delete.setVisibility(View.GONE);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("管理", "onBindViewHolder: " + position);
                if (cityList.size() == 1) {
                    Toast.makeText(context, "至少需要保留一个城市!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                notifyItemRemoved(position);
                cityList.remove(position);
                notifyItemRangeChanged(position, cityList.size() - position);
                (ActivityCollector.getMainActivity()).deleteCity(position);

            }
        });
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
        notifyDataSetChanged();
    }

    public boolean ifCanDelete() {
        return canDelete;
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView city_name = null;
        TextView city_detail = null;
        Button delete = null;
        View itemView = null;
        ImageView locate = null;
        TextView temp = null;
        ImageView weather = null;

        public ViewHolder(View itemView) {
            super(itemView);
            city_detail = (TextView) itemView.findViewById(R.id.cityDetail);
            city_name = (TextView) itemView.findViewById(R.id.cityName);
            delete = (Button) itemView.findViewById(R.id.deleteCity);
            locate = (ImageView) itemView.findViewById(R.id.locate);
            this.itemView = itemView;
            temp = (TextView) itemView.findViewById(R.id.temp);
            weather = (ImageView) itemView.findViewById(R.id.weather);
        }
    }
}
