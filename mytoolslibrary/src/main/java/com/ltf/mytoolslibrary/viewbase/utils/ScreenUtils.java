package com.ltf.mytoolslibrary.viewbase.utils;

import android.app.Activity;
import android.util.DisplayMetrics;


/**
 * @author 李堂飞 20160614
 * 使用：次工具类主要是获取屏幕的宽高
 */
public class ScreenUtils {
    private static int screenW;
    private static int screenH;

    public static int getScreenW(Activity mActivity){
        if (screenW == 0){
            initScreen(mActivity);
        }
        return screenW;
    }

    public static int getScreenH(Activity mActivity){
        if (screenH == 0){
            initScreen(mActivity);
        }
        return screenH;
    }

    private static void initScreen(Activity mActivity){
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenW = metric.widthPixels;
        screenH = metric.heightPixels;
    }
}
