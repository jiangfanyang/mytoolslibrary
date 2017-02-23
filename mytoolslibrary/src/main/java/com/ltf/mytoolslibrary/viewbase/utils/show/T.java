package com.ltf.mytoolslibrary.viewbase.utils.show;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by 李堂飞 on 2015/4/1.
 * Toast统一管理类
 */
public class T {

    private T() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 是否显示Tost
     */
    public static boolean isShow = true;

    /**
     * 短时间显示Toast
     */
    public static void showShort(Context context, CharSequence message) {
        if (isShow)
            ToastUtils.showToast(context,message.toString());
//            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 屏幕中间短时间显示Toast
     */
    public static void showGravityShort(Context context, CharSequence message) {
        if (isShow) {
//            Toast makeText = Toast.makeText(context, message, Toast.LENGTH_SHORT);
//            makeText.setGravity(Gravity.CENTER, 0, 0);
//            makeText.show();
            ToastUtils.showToast_CENTER(context,message.toString());
        }
    }

    /**
     * 短时间显示Toast
     */
    public static void showShort(Context context, int message) {
        if (isShow)
            ToastUtils.showToast(context,message);
//            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 屏幕中间长时间显示Toast
     */
    public static void showGravityLong(Context context, CharSequence message) {
        if (isShow) {
            Toast makeText = Toast.makeText(context, message, Toast.LENGTH_LONG);
            makeText.setGravity(Gravity.CENTER, 0, 0);
            makeText.show();
        }
    }

    /**
     * 长时间显示Toast
     */
    public static void showLong(Context context, int message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}