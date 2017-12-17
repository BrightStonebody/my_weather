package com.example.chenlei.my_weather.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import com.example.chenlei.my_weather.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by chenlei on 2017/10/12.
 */

public class StoreBlurPic {
    private Context context;

    public StoreBlurPic(Context context) {
        this.context = context;
    }

    public void store() {
        int[] pics = {R.mipmap.bg_cloudy,
                R.mipmap.bg_rainy,
                R.mipmap.bg_snowy,
                R.mipmap.bg_sunny};

        for (int i = 0; i < pics.length; i++) {
            Log.i("模糊", "store: " + pics[i]);
            String path = (context.getFilesDir() + "/blur/" + pics[i] + ".jpg");
            storeSingleImage(null, pics[i], path);
        }
    }

    public boolean storeSingleImage(String inputPath, int id, String outputPath) {

        File file = new File(outputPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (inputPath == null && file.exists())
            return true;

        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            Bitmap bitmap;
            if (inputPath == null)
                bitmap = BitmapFactory.decodeResource(context.getResources(), id);
            else {
                bitmap = BitmapFactory.decodeFile(inputPath);
            }
            Bitmap blurBitmap = blurImage(context, bitmap, 25f);
            blurBitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            Log.e("错误", "storeSingleImage: " + "保存模糊图片失败" + e.toString());
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            Log.e("错误", "storeSingleImage: " + "保存模糊图片失败" + e.toString());
            e.printStackTrace();
            return false;
        }
    }

    private Bitmap blurImage(Context context, Bitmap image, float blurRadius) {

        //图片缩放比例
        final float BITMAP_SCALE = 0.4f;
//        //设置模糊程度，最大为25f
//        float blurRadius=25f;

        // 计算图片缩小后的长宽
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        // 将缩小后的图片做为预渲染的图片
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        // 创建一张渲染后的输出图片
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(context);
        // 创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(blurRadius);
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn);
        // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut);

        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

}
