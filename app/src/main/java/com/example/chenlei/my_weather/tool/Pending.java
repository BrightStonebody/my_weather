package com.example.chenlei.my_weather.tool;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by chenlei on 2017/6/22.
 */

public class Pending {
    private ProgressDialog progressDialog;
    private Context context;
    private boolean show=false;
    private String message="正在加载...";

    public Pending(Context context){
        this.context=context;
    }

    public Pending(Context context,String message){
        this.context=context;
        this.message=message;
    }

    public  void showProgressDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(context);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
        }
        show=true;
        progressDialog.show();
    }
    public void closeProgressDialog(){
        if(!show)
            return ;
        progressDialog.dismiss();
        show=false;
    }
}
