package com.ltf.mytoolslibrary.viewbase.notifications;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;

import com.ltf.mytoolslibrary.viewbase.app.ApplicationBase;
import com.ltf.mytoolslibrary.viewbase.views.UIAlertView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NotificationsUtils {

	private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    public static boolean isNotificationEnabled(Context context) {
        if(Build.VERSION.SDK_INT < 18){
//    		SettingNotification(context,"　　零生活应用通知权限是否已授权?不开启通知权限将无法接收订单推送!\n　　点击\'设置\'找到应用\'零生活\',点击\'通知管理\',点击允许通知即可!\n　　如若已授权请忽略,谢谢!");
            return true;
        }
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);

        ApplicationInfo appInfo = context.getApplicationInfo();

        String pkg = context.getApplicationContext().getPackageName();

        int uid = appInfo.uid;

        Class appOpsClass = null; /* Context.APP_OPS_MANAGER */

        try {

            appOpsClass = Class.forName(AppOpsManager.class.getName());

            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);

            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (int)opPostNotificationValue.get(Integer.class);

            return ((int)checkOpNoThrowMethod.invoke(mAppOps,value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static void guideUserOpenNotification(Context context){
    	if(isNotificationEnabled(context)){
    		
    	}else{
    		SettingNotification(context);
    	}
    }
    
    private static void SettingNotification(final Context context) {
        final UIAlertView delDialog = new UIAlertView(Gravity.LEFT,
        		context,
                "温馨提示",
                "　　通知权限缺失,不开启通知权限将无法接收订单推送!\n　　点击\'设置\'找到应用\'"+ ApplicationBase.CrashErrorMessageName+"\',点击\'通知管理\',点击允许通知即可!",
                "取消", "设置");
        delDialog.show();
        delDialog.setClicklistener(new UIAlertView.ClickListenerInterface() {
                                       @Override
                                       public void doLeft() {
                                           delDialog.dismiss();
                                       }

                                       @Override
                                       public void doRight() {
                                    	   delDialog.dismiss();
                                    	   Intent intent =  new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);  
                                    	   context.startActivity(intent);
                                       }
                                   }
        );
    }
}