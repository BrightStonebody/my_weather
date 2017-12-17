package com.example.chenlei.my_weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.chenlei.my_weather.myViews.BlurView;
import com.example.chenlei.my_weather.myViews.TitleView;
import com.example.chenlei.my_weather.tool.GetSystemHeight;

public class SetActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;
    private BlurView background = null;
    private TitleView title = null;
    private Button ownBackground = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        Button openList = (Button) findViewById(R.id.open_list);
        Switch ifRemind = (Switch) findViewById(R.id.if_remind);
        final Button hint = (Button) findViewById(R.id.hint);
        background = (BlurView) findViewById(R.id.backGround);
        title = (TitleView) findViewById(R.id.title);
        Button suggest = (Button) findViewById(R.id.suggest);
        ownBackground = (Button) findViewById(R.id.own_background);
        //适配系统状态栏
        GetSystemHeight.adapterStatusBar(this, title.getRoot(), 50);

        title.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sharedPreferences = getSharedPreferences("set", MODE_PRIVATE);
        editor = getSharedPreferences("set", MODE_PRIVATE).edit();
        float time = sharedPreferences.getFloat("update", (float) 3.0);
        hint.setText(time + "小时");

        setBackground();

        suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetActivity.this, SuggestActivity.class);
                intent.putExtra("backgroundId", backgroundId);
                startActivity(intent);
            }
        });

        View.OnClickListener openListListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SetActivity.this);
                final View dialogView = getLayoutInflater().inflate(R.layout.fragment_dialog, null);
                final EditText inputTime = (EditText) dialogView.findViewById(R.id.input_time);

                float time = sharedPreferences.getFloat("update", (float) 3.0);
                inputTime.setText("" + time);

                Button sure = (Button) dialogView.findViewById(R.id.sure);
                Button cancel = (Button) dialogView.findViewById(R.id.cancel);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(true);
                final AlertDialog dialog = dialogBuilder.show();
                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String time = inputTime.getText().toString();
                        if (Float.valueOf(time) > 24) {
                            Toast.makeText(SetActivity.this, "设置时间过长,设置无效,请重新设置!!", Toast.LENGTH_SHORT)
                                    .show();
                            dialog.dismiss();
                            return;
                        } else if (Float.valueOf(time) < 1) {
                            Toast.makeText(SetActivity.this, "设置时间过短。设置无效，请重新设置!!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        float floatTime = (float) ((Math.round(Float.valueOf(time) * 10)) / 10.0);
                        editor.putFloat("update", floatTime);
                        editor.apply();
                        hint.setText("" + floatTime + "小时");
                        dialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        };

        openList.setOnClickListener(openListListener);
        hint.setOnClickListener(openListListener);

        //设置提醒
        ifRemind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    editor.putBoolean("remind", true);
                } else {
                    editor.putBoolean("remind", false);
                }
            }
        });

        //设置背景
        ownBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetActivity.this, ChooseBgActivity.class);
                intent.putExtra("backgroundId", backgroundId);
                startActivity(intent);
            }
        });

    }

    private int backgroundId = R.mipmap.bg_sunny;

    private void setBackground() {
        Intent intent = getIntent();
        backgroundId = intent.getIntExtra("backgroundId", R.mipmap.bg_sunny);
        background.setImageResource(backgroundId,this);
        background.doBlur(100);
    }
}
