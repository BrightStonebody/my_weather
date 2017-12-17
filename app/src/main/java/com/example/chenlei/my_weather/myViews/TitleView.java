package com.example.chenlei.my_weather.myViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chenlei.my_weather.R;

/**
 * Created by chenlei on 2017/9/23.
 */

public class TitleView extends RelativeLayout {
    private Button back;
    private TextView title;
    private FrameLayout root;
    private Button imageBack;

    public TitleView(Context context) {
        super(context);
        initView(context);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initTypedArray(context,attrs);
    }

    private void initTypedArray(Context context,AttributeSet attrs){
        TypedArray mTypedArray=context.obtainStyledAttributes
                (attrs,R.styleable.TitleView);
        String titleName=mTypedArray.getString(R.styleable.TitleView_title_text);
        title.setText(titleName);
        //获取资源后要及时回收
        mTypedArray.recycle();

    }

    public void initView(Context context){
        LayoutInflater.from(context).inflate
                (R.layout.part_title_view,this,true);
        back= (Button) findViewById(R.id.back);
        title= (TextView) findViewById(R.id.title_text);
        imageBack= (Button) findViewById(R.id.image_back);
        root= (FrameLayout) findViewById(R.id.root);
    }

    public void setTitle(String text){
        title.setText(text);
    }

    public void setBackListener(OnClickListener onClickListener){
        back.setOnClickListener(onClickListener);
        imageBack.setOnClickListener(onClickListener);
    }

    public FrameLayout getRoot(){
        return root;
    }
}
