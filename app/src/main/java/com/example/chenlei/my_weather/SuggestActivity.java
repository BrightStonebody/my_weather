package com.example.chenlei.my_weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chenlei.my_weather.dataBase.Suggestion;
import com.example.chenlei.my_weather.myViews.BlurView;
import com.example.chenlei.my_weather.myViews.TitleView;
import com.example.chenlei.my_weather.tool.GetSystemHeight;
import com.example.chenlei.my_weather.tool.Pending;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SuggestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        final Button commit= (Button) findViewById(R.id.commit);
        TitleView title= (TitleView) findViewById(R.id.title);
        BlurView bg= (BlurView) findViewById(R.id.backGround);
        final EditText text= (EditText) findViewById(R.id.text);
        final EditText contact= (EditText) findViewById(R.id.contact);

        //适配系统状态栏
        GetSystemHeight.adapterStatusBar(this,title.getRoot(),50);

        Intent intent=getIntent();
        int backgroundId=intent.getIntExtra("backgroundId",R.mipmap.bg_sunny);
        bg.setImageResource(backgroundId,this);
        bg.doBlur(100);

        title.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String getText=text.getText().toString();
                final String getContact=contact.getText().toString();
                if(getText.equals("")){
                    Toast.makeText(SuggestActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                    return ;
                }else if(getContact.equals("")){
                    Toast.makeText(SuggestActivity.this, "联系方式不能为空", Toast.LENGTH_SHORT).show();
                    return ;
                }
                final Pending pending=new Pending(SuggestActivity.this);
                pending.showProgressDialog();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String bmobKey=getResources().getString(R.string.bmob_key);
                        Bmob.initialize(SuggestActivity.this,bmobKey);
                        Suggestion suggestion=new Suggestion();
                        suggestion.setSuggestion(getText);
                        suggestion.setContact(getContact);
                        suggestion.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                boolean success=false;
                                if(e==null){
                                    success=true;
                                }else{
                                    success=false;
                                }
                                final boolean finalSuccess = success;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pending.closeProgressDialog();
                                        if(finalSuccess){
                                            Toast.makeText(SuggestActivity.this, "上传成功", Toast.LENGTH_SHORT)
                                                    .show();
                                            onBackPressed();
                                        }else{
                                            Toast.makeText(SuggestActivity.this, "上传失败", Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }).start();
            }
        });

    }
}
