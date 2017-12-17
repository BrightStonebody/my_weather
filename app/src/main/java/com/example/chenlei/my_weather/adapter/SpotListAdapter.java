package com.example.chenlei.my_weather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.chenlei.my_weather.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlei on 2017/9/1.
 */

public class SpotListAdapter extends RecyclerView.Adapter<SpotListAdapter.ViewHolder> {

    private List<Boolean> spotsList=new ArrayList<>();

    public SpotListAdapter(List<Boolean> spotsList) {
        this.spotsList = spotsList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView spot=null;

        public ViewHolder(View itemView) {
            super(itemView);
            spot= (ImageView) itemView.findViewById(R.id.spot);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spot,parent,false);
        return new SpotListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageView spot=holder.spot;
        int res=R.mipmap.spot_white;
        if(spotsList.get(position))
            res=R.mipmap.spot_dark;
        spot.setImageResource(res);
    }

    @Override
    public int getItemCount() {
        return spotsList.size();
    }
}
