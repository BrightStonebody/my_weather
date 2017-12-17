package com.example.chenlei.my_weather.tool;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by chenlei on 2017/9/20.
 */

public class GetSystemHeight {

    public static int getScreenHeight(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowMgr.getDefaultDisplay().getRealMetrics(dm);
        return dm.heightPixels;
    }

    public static int getScreenWidth(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowMgr.getDefaultDisplay().getRealMetrics(dm);
        return dm.widthPixels;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getDaoHangHeight(Context context) {
        int result = 0;
        int resourceId=0;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid!=0){
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        }else
            return 0;
    }

    public static int dptoPx(Context context,int dp){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int) (dp*scale+0.5f);
    }

    public static int pxToDp(Context context,int px){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int) (px/scale+0.5f);
    }

    public static void adapterStatusBar(Context context, View title, int height){
        int statusBarHeight=GetSystemHeight.getStatusBarHeight(context);
        Log.i("系统高度", "onCreate: "+statusBarHeight);
        ViewGroup.LayoutParams params=  title.getLayoutParams();
        params.height=GetSystemHeight.dptoPx(context,height)+statusBarHeight;
        title.setLayoutParams(params);
    }
}
