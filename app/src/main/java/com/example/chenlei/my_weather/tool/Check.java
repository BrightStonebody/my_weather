package com.example.chenlei.my_weather.tool;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.chenlei.my_weather.R;
import com.example.chenlei.my_weather.dataBase.Version;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by chenlei on 2017/10/16.
 */

public class Check {
    public static void checkVersion(final Activity context) {
        //每天监测一次是否有新的更新
        SharedPreferences sharedPreferences = context.getSharedPreferences("set", Context.MODE_PRIVATE);
        long askTimeLast = sharedPreferences.getLong("checkVersion", 0);
        boolean must=sharedPreferences.getBoolean("hasMust",false);
        final long now = System.currentTimeMillis();
        if ((now - askTimeLast) / 3600 / 1000 < 24 && !must)
            return;

        Log.i("访问", "checkVersion: " + now + "  " + askTimeLast);
        String bmobKey = context.getResources().getString(R.string.bmob_key);
        Bmob.initialize(context, bmobKey);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                            context.getPackageName(), 0);
                    final int versionCodeLocal = packageInfo.versionCode;
                    Log.i("版本", "run: " + versionCodeLocal);
                    BmobQuery<Version> bmobQuery = new BmobQuery<>();
                    bmobQuery.getObject("GxlVFFF3", new QueryListener<Version>() {
                        @Override
                        public void done(final Version version, final BmobException e) {
                            if (e == null) {
                                final SharedPreferences.Editor editor = context.getSharedPreferences("set", Context.MODE_PRIVATE)
                                        .edit();
                                editor.putLong("checkVersion", now);
                                if (version.getVersionCode() > versionCodeLocal) {
                                    Log.i("版本", "done: " + version.getVersionCode());
                                    context.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                            builder.setCancelable(false);
                                            builder.setTitle("警告");
                                            builder.setMessage("监测到有新版本，请下载最新版本");
                                            if (version.isMust()) {
                                                editor.putBoolean("hasMust",true);
                                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        ActivityCollector.removeAll();
                                                    }
                                                });
                                            } else {
                                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                    }
                                                });
                                            }
                                            builder.show();
                                        }
                                    });
                                    editor.apply();
                                }
                            } else
                                Log.i("版本", "done: " + e.getMessage());
                        }
                    });

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

}
