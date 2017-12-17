package com.example.chenlei.my_weather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chenlei.my_weather.R;
import com.example.chenlei.my_weather.gson.Weather;
import com.example.chenlei.my_weather.tool.WeatherToIcon;

import java.util.List;

import static android.R.attr.data;
import static com.example.chenlei.my_weather.R.id.date;

/**
+ * Created by chenlei on 2017/7/22.
+ */

public class Forecast_dayAdapter extends RecyclerView.Adapter<Forecast_dayAdapter.ViewHolder> {
    private List<Weather.Daily> forecasts;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView weather_day;
        TextView date_day;
        TextView day_max;
        TextView day_min;
        ImageView weather_icon;
        TextView date_week;

        public ViewHolder(View itemView) {
            super(itemView);
            weather_day=(TextView)itemView.findViewById(R.id.weather_day);
            date_day= (TextView) itemView.findViewById(date);
            day_max= (TextView) itemView.findViewById(R.id.day_max);
            day_min=(TextView)itemView.findViewById(R.id.day_min);
            weather_icon= (ImageView) itemView.findViewById(R.id.weather_icon);
            date_week= (TextView) itemView.findViewById(R.id.week);
        }
    }

    public Forecast_dayAdapter(List<Weather.Daily> forecasts) {
        this.forecasts = forecasts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast_day,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Weather.Daily forecast=forecasts.get(position);
        holder.date_day.setText(forecast.date);
        forecast.week="周"+forecast.week.substring
                (forecast.week.length()-1);
        holder.date_week.setText(forecast.week);
        holder.weather_day.setText(forecast.day.weather);
        holder.day_max.setText(forecast.day.temphigh+"°");
        holder.day_min.setText(forecast.night.templow+"°");
        holder.weather_icon.setImageResource(
                WeatherToIcon.weatherToIcon(forecast.day.weather));
    }

    @Override
    public int getItemCount() {
        return forecasts.size();
    }
}