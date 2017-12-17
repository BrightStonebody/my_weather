package com.example.chenlei.my_weather.dataBase;

import cn.bmob.v3.BmobObject;

/**
 * Created by chenlei on 2017/9/24.
 */

public class Suggestion extends BmobObject {
    private String suggestion;
    private String contact;

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
