package com.example.chenlei.my_weather.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.chenlei.my_weather.service.AutoRemindService;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equalsIgnoreCase(Intent.ACTION_USER_PRESENT))
        {
            Log.i("service", "onReceive: "+"1");
            Log.i("service", "onReceive: "+"监听到解锁action");
            Intent i=new Intent(context, AutoRemindService.class);
            //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            context.startService(i);
//            Intent i=new Intent(context,SelectCityActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
        }
    }
}
