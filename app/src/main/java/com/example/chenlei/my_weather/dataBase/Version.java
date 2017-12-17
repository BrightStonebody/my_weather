package com.example.chenlei.my_weather.dataBase;

/**
 * Created by chenlei on 2017/9/16.
 */

public class Version {
    private int versionCode;
    private boolean must;

    public boolean isMust() {
        return must;
    }

    public void setMust(boolean must) {
        this.must = must;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
}
