package com.example.chenlei.my_weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.chenlei.my_weather.myViews.BlurView;
import com.example.chenlei.my_weather.myViews.TitleView;
import com.example.chenlei.my_weather.tool.GetSystemHeight;
import com.example.chenlei.my_weather.tool.StoreBlurPic;
import com.yanzhenjie.durban.Controller;
import com.yanzhenjie.durban.Durban;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;


public class ChooseBgActivity extends AppCompatActivity {

    private Button fromUser = null;
    private Button system = null;
    private BlurView background = null;

    private final int ALBUM = 1000;
    private final int CUT = 1001;

    private SharedPreferences.Editor editor = null;

    private int screenHeight;
    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user_pic);
        fromUser = (Button) findViewById(R.id.user);
        system = (Button) findViewById(R.id.system);
        TitleView title = (TitleView) findViewById(R.id.title);
        background = (BlurView) findViewById(R.id.background);

        title.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //适配系统状态栏
        GetSystemHeight.adapterStatusBar(this, title.getRoot(), 50);

        setBackground();

        editor = getSharedPreferences("set", MODE_PRIVATE).edit();

        screenHeight = GetSystemHeight.getScreenHeight(this);
        screenWidth = GetSystemHeight.getScreenWidth(this);

        system.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("userSetBg", false);
                editor.putString("storePicPath", null);
                editor.putString("blurPicPath", null);
                editor.apply();
                restartApp();
            }
        });

        fromUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiImageSelector.create(ChooseBgActivity.this)
                        .showCamera(false) // 是否显示相机. 默认为显示
                        .single() // 单选模式
                        .origin(null) // 默认已选择图片. 只有在选择模式为多选时有效
                        .start(ChooseBgActivity.this, ALBUM);

            }
        });
    }

    private String storePicPath;
    private String blurPicPath;

    private void onCut(String path) {
        String fileName = path.substring(path.lastIndexOf('/') + 1);
        blurPicPath = getFilesDir() + "/blur/" + fileName;
        Durban.with(this)
                // 裁剪界面的标题。
                .title("裁剪")
                .statusBarColor(ContextCompat.getColor(this, R.color.durban_ColorPrimaryBlack))
                .toolBarColor(ContextCompat.getColor(this, R.color.durban_ColorPrimaryBlack))
                .navigationBarColor(ContextCompat.getColor(this, R.color.durban_ColorPrimaryBlack))
                // 图片路径list或者数组。
                .inputImagePaths(path)
                // 图片输出文件夹路径。
                .outputDirectory(getFilesDir() + "")
                // 裁剪图片输出的最大宽高。
                .maxWidthHeight(1000, 1000)
                // 裁剪时的宽高比。
                .aspectRatio(screenWidth, screenHeight)
                // 图片压缩格式：JPEG、PNG。
                .compressFormat(Durban.COMPRESS_JPEG)
                // 图片压缩质量，请参考：Bitmap#compress(Bitmap.CompressFormat, int, OutputStream)
                .compressQuality(100)
                // 裁剪时的手势支持：ROTATE, SCALE, ALL, NONE.
                .gesture(Durban.GESTURE_ALL)
                .controller(
                        Controller.newBuilder()
                                .enable(false) // 是否开启控制面板。
                                .rotation(true) // 是否有旋转按钮。
                                .rotationTitle(true) // 旋转控制按钮上面的标题。
                                .scale(true) // 是否有缩放按钮。
                                .scaleTitle(true) // 缩放控制按钮上面的标题。
                                .build()) // 创建控制面板配置。
                .requestCode(CUT)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CUT:
                if (resultCode == RESULT_OK) {
                    storePicPath = Durban.parseResult(data).get(0);
                    blurPicPath = getFilesDir() + "/blur/" + storePicPath.substring(storePicPath.lastIndexOf('/') + 1);
                    Log.i("背景", "onActivityResult: " + storePicPath);
                    StoreBlurPic storeBlurPic = new StoreBlurPic(this);
                    if (storeBlurPic.storeSingleImage
                            (storePicPath, -1, blurPicPath)) {
                        editor.putBoolean("userSetBg", true);
                        editor.putString("storePicPath", storePicPath);
                        editor.putString("blurPicPath", blurPicPath);
                        editor.apply();
                        restartApp();
                    } else {
                        Toast.makeText(this, "裁剪失败...", Toast.LENGTH_SHORT).show();
                        Log.e("错误", "onActivityResult: 裁剪图片保存模糊图像失败");
                    }
                    finish();
                } else
                    Log.e("错误", "onActivityResult: 图片裁剪失败");
                break;
            case ALBUM:
                if (resultCode != RESULT_OK) {
                    Log.e("错误", "onActivityResult: 获取相册图片失败");
                    break;
                }
                String path= data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT)
                        .get(0);
                onCut(path);
                break;
        }
    }

    private void restartApp() {
        Intent intent = new Intent("com.example.chenlei.my_weather.changeBackgroundNow");
        sendBroadcast(intent);
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void setBackground() {
        Intent intent = getIntent();
        int backgroundId = intent.getIntExtra("backgroundId", R.mipmap.bg_sunny);
        background.setImageResource(backgroundId, this);
        background.doBlur(100);
    }

}