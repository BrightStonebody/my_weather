package com.example.chenlei.my_weather.dataBase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.chenlei.my_weather.tool.Pending;

import org.litepal.LitePal;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class DBHelper {

    private Context context;

    public DBHelper(Context mContext) {
        LitePal.getDatabase();
        this.context = mContext;
    }


    public boolean checkIfStore() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("dataBase", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("hasStore", false);
    }

    public void loadDb(final WantDoAfter wantDoAfter) {
        if (checkIfStore())
            return;
        final String databasePath = "/data/data/com.example.chenlei.my_weather/databases/" + "area.db";

        final Pending pending = new Pending(context, "正在加载数据库...\n(此操作不消耗流量)");
        pending.showProgressDialog();

        new Thread(new Runnable() {
            @Override
            public void run() {
//                dataFromJisu();
//                dataFromDb(databasePath);

//                dataFromHefeng();
                dataFromDb(databasePath);

                SharedPreferences.Editor editor = context.getSharedPreferences(
                        "dataBase", Context.MODE_PRIVATE)
                        .edit();
                editor.putBoolean("hasStore", true);
                editor.apply();
                wantDoAfter.wantDoAfter();

                pending.closeProgressDialog();
            }
        }).start();

    }

    public interface WantDoAfter {
        void wantDoAfter();
    }


    private void dataFromDb(String databasePath) {

        try {
            InputStream is = context.getAssets().open("area_hefeng.db");
            FileOutputStream fos = new FileOutputStream(databasePath);
            byte[] temp = new byte[1024];
            while (is.read(temp) > 0) {//read返回的是读取的字节数
                fos.write(temp, 0, 1024);
            }
            is.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("错误", "dataFromDb: "+"错误，加载数据库数据失败");
        }
    }

//    private void dataFromHefeng(){
//        try {
//            InputStream is=context.getAssets().open("city_info.txt");
//            BufferedReader br=new BufferedReader(new InputStreamReader(is));
//            StringBuffer result=new StringBuffer();
//            String buffer=new String();
//            int id=1;
//            while((buffer=br.readLine())!=null){
//                result.append(buffer);
//                String [] values = buffer.split("\\s+");
//                City city=new City();
//                city.setId(id);
//                city.setCityId(values[0]);
//                city.setEnglishName(values[1]);
//                city.setName(values[2]);
//                city.setCountryId(values[3]);
//                city.setCountryEn(values[4]);
//                city.setCountryCh(values[5]);
//                city.setProvinceEn(values[6]);
//                city.setProvinceCh(values[7]);
//                city.setCityEn(values[8]);
//                city.setCityCh(values[9]);
//                city.setLatitude(values[10]);
//                city.setLongitude(values[11]);
//                id++;
//                city.save();
//                Log.i("和风数据", "dataFromHefeng: "+city);
//                Log.i("和风数据", "dataFromHefeng: "+values);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }



    //cityFirst是急速数据的天气类
//    private void dataFromJisu() {
//        try {
//            InputStream is = context.getAssets().open("city_from_jisu.txt");
//            // InputStreamReader isr=new InputStreamReader(is);
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//            StringBuffer result = new StringBuffer("");
//            String buffer;
//            while ((buffer = br.readLine()) != null) {
//                result.append(buffer);
//            }
//            String data = new String(result);
//
//            br.close();
//            is.close();
//
//            Log.i("数据库", "dataFromJisu: " + data);
//            Gson gson = new Gson();
//            List<CityFirst> citiesFirst = gson.fromJson(data
//                    , new TypeToken<List<CityFirst>>() {
//                    }.getType());
//            CityFirst[] citiesFirstArray = citiesFirst.toArray(
//                    new CityFirst[citiesFirst.size()]);
//            citiesFirst.clear();
//
//            Log.i("数据库", "dataFromJisu: " + citiesFirstArray.length);
//
//            for (int i = 0; i < citiesFirstArray.length; i++) {
//                CityFirst city = citiesFirstArray[i];
//                city.save();
//                if (citiesFirstArray[i].isSaved())
//                    Log.i("数据库", "dataFromJisu: " + citiesFirstArray[i].cityid);
//            }
//
//            for (int i = 0; i < citiesFirstArray.length; i++) {
//                City city = new City();
//                CityFirst ci = citiesFirstArray[i];
//
//                city.setName(ci.city);
//
//                city.setCityId(ci.citycode);
//                city.setId(Integer.parseInt(ci.cityid));
//                city.setParentid(ci.parentid);
//
//                CityFirst temp = ci;
//                int grade = 1;
//                List<CityFirst> parentList = new ArrayList<>();
//                while (!temp.cityid.equals("0")) {
////                    temp=DataSupport.find(CityFirst.class,temp.parentid);
//                    List<CityFirst> find = DataSupport.where("cityid=?", temp.parentid).find(CityFirst.class);
//                    if (find.size() != 0)
//                        temp = find.get(0);
//                    else
//                        break;
//                    grade++;
//                    parentList.add(temp);
//                }
//                switch (grade) {
//                    case 1:
//                        city.setCityCh(ci.city);
//                        city.setProvinceCh(ci.city);
//                        break;
//                    case 2: {
//                        city.setCityCh(ci.city);
//                        city.setProvinceCh(parentList.get(0).city);
//                    }
//                    break;
//                    case 3: {
//                        city.setCityCh(parentList.get(0).city);
//                        city.setProvinceCh(parentList.get(1).city);
//                    }
//                    break;
//                }
//                Log.i("数据库", "dataFromJisu: " + city.getName() + "  " + city.getCityCh());
//                if (city.getCityId() == "" || city.getCityCh() == null)
//                    continue;
//                if (!city.save())
//                    Log.i("数据库", "dataFromJisu: " + "数据保存失败");
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//




//    public void dataFromHeFeng() throws IOException {
//        InputStream is = context.getAssets().open("city_info.txt");
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//        String buffer;
//        int lineNum = 0;
//        while ((buffer = reader.readLine()) != null) {
//            //  Log.i("数据库", "loadDb: "+buffer);
//            City city = new City();
//            String[] datas = buffer.split("\\s+");
//
////            city.setId(lineNum++);
//            city.setCityId(datas[0]);
//            city.setEnglishName(datas[1]);
//            city.setName(datas[2]);
//            city.setCountryId(datas[3]);
//            city.setCountryEn(datas[4]);
//            city.setCountryCh(datas[5]);
//            city.setProvinceEn(datas[6]);
//            city.setProvinceCh(datas[7]);
//            city.setCityEn(datas[8]);
//            city.setCityCh(datas[9]);
//            city.setLatitude(datas[10]);
//            city.setLongitude(datas[11]);
//
//            if (city.save())
//                Log.i("数据库", "loadDb: " + "数据库保存成功");
//            else
//                Log.i("数据库", "loadDb: " + "数据库保存失败");
//        }
//        reader.close();
//        is.close();
//    }
}
