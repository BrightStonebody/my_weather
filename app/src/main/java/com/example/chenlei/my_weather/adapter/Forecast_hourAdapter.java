package com.example.chenlei.my_weather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chenlei.my_weather.R;
import com.example.chenlei.my_weather.gson.Weather;

import java.util.List;

/**
 * + * Created by chenlei on 2017/7/23.
 * +
 */

public class Forecast_hourAdapter extends RecyclerView.Adapter<Forecast_hourAdapter.HourViewHolder> {
    private List<Weather.Hourly> forecast_hourlies;

    public Forecast_hourAdapter(List<Weather.Hourly> forecast_hourlies) {
        this.forecast_hourlies = forecast_hourlies;
    }

    static class HourViewHolder extends RecyclerView.ViewHolder {
        TextView time = null;
        TextView weather = null;
        TextView tmp = null;

        public HourViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.date_hour);
            weather = (TextView) itemView.findViewById(R.id.weather_hour);
            tmp = (TextView) itemView.findViewById(R.id.hour_tmp);
        }
    }

    @Override
    public Forecast_hourAdapter.HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast_hour, parent, false);
        HourViewHolder hourViewHolder = new HourViewHolder(view);
        return hourViewHolder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {
        Weather.Hourly forecast_hourly = forecast_hourlies.get(position);
        holder.weather.setText(forecast_hourly.weather);
        holder.time.setText(forecast_hourly.time);
        holder.tmp.setText(forecast_hourly.temp + "°");
    }

    @Override
    public int getItemCount() {
        return forecast_hourlies.size();
    }
}