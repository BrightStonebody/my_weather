package com.example.chenlei.my_weather.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chenlei.my_weather.R;
import com.example.chenlei.my_weather.gson.Weather;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlei on 2017/9/11.
 */

public class IndexAdapter  extends RecyclerView.Adapter<IndexAdapter.IndexViewHolder>{

    private List<Weather.Index> indexList=new ArrayList<>();
    private int screenWidth;
    private int[] imageList;

    public IndexAdapter(List<Weather.Index> indexList, int[] imageList,int screenWidth) {
        this.indexList = indexList;
        this.imageList = imageList;
        this.screenWidth=screenWidth;
    }

    @Override
    public IndexViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_index,parent,false);

        return new IndexViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IndexViewHolder holder, final int i) {
        holder.image.setImageResource(imageList[i]);
        holder.value.setText(indexList.get(i).ivalue);
        holder.name.setText(indexList.get(i).iname);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doOnClick.doOnClick(i);
            }
        });

        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) holder.cardView.getLayoutParams();
        params.width=screenWidth/3;
        holder.cardView.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return indexList.size();
    }

    private DoOnClick doOnClick=null;

    public void setDoOnClick(DoOnClick doOnClick) {
        this.doOnClick = doOnClick;
    }

    public interface DoOnClick{
        void doOnClick(int i);
    }

    public class IndexViewHolder extends RecyclerView.ViewHolder{

        ImageView image=null;
        TextView value=null;
        TextView name=null;
        CardView cardView=null;
        View view=null;
        public IndexViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            image= (ImageView) itemView.findViewById(R.id.index_image);
            value= (TextView) itemView.findViewById(R.id.index_value);
            name= (TextView) itemView.findViewById(R.id.index_name);
            cardView= (CardView) itemView.findViewById(R.id.root);
        }
    }
}
