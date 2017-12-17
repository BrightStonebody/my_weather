package com.example.chenlei.my_weather.tool;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by chenlei on 2017/9/16.
 */

public class Font {
    public static void changeFont(ViewGroup root, Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Light.ttf");
        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof Button) {
                ((Button) v).setTypeface(tf);
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(tf);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(tf);
            } else if (v instanceof ViewGroup) {
                changeFont((ViewGroup) v, context);
            }
        }
    }
}
