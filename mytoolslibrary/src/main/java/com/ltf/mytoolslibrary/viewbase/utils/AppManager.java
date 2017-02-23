package com.ltf.mytoolslibrary.viewbase.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;

/**
 * 
* 李堂飞 20160614
* 应用程序Activity管理
 * 1. 添加权限
   <uses-permission android:name="android.permission.RESTART_PACKAGES" />
 //    private static AppManager instance;
 //    private AppManager(){}
 //    instance=new AppManager();
 使用AppManager工具类
  Activity启动时，在的onCreate方法里面，将该Activity实例添加到AppManager的堆中
  AppManager.getAppManager().addActivity(this);
     退出程序时，调用
  AppManager.getAppManager().AppExit(this);
* @version    
*
 */
public class AppManager {
     
    private static AppManager instance;
    private AppManager(){}
    /**
     * 单一实例
     */ 
    public static AppManager getAppManager(){
        if(instance==null){
            instance=new AppManager();
        }
        return instance; 
    } 
    
    /**
     * 添加Activity到堆
     */ 
    public void addActivity(Activity activity){
        com.alipay_android_sdk.AppManager.getAppManager().addActivity(activity);
    } 
    /**
     * 获取当前Activity
     */ 
    public Activity currentActivity(){
        return com.alipay_android_sdk.AppManager.getAppManager().currentActivity();
    } 
    /**
     * 结束当前Activity
     */ 
    public void finishActivity(){ 
        com.alipay_android_sdk.AppManager.getAppManager().finishActivity();
    } 
    /**
     * 结束指定的Activity
     */ 
    public void finishActivity(Activity activity){
        if(activity != null)
            com.alipay_android_sdk.AppManager.getAppManager().finishActivity(activity);
    } 
    /**
     * 结束指定类名的Activity
     */ 
    public void finishActivity(Class<?> cls){
        com.alipay_android_sdk.AppManager.getAppManager().finishActivity(cls);
    }
    /**
     * 结束所有的Activity
     */ 
    public void finishAllActivity(){
        com.alipay_android_sdk.AppManager.getAppManager().finishAllActivity();
    }
    /**
     * 退出应用程序
     */ 
    @SuppressWarnings("deprecation")
	public void AppExit(Context context) {
        com.alipay_android_sdk.AppManager.getAppManager().AppExit(context);
        AppAllExit(context);
    }

    private void AppAllExit(Context context){//关闭APP并回到Home
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

} 
