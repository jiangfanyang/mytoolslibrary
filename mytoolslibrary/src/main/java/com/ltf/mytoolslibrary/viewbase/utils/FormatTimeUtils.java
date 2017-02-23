package com.ltf.mytoolslibrary.viewbase.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.alipay_android_sdk.Application;
import com.alipay_android_sdk.FragmentActivity;
import com.ltf.mytoolslibrary.viewbase.app.ApplicationBase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 作者：${李堂飞} on 2017/1/21 09:24
 * 邮箱：1195063924@qq.com
 * 注解:格式化时间为天时分秒毫秒
 */

public class FormatTimeUtils {

    private static FormatTimeUtils mFormatTimeUtils;
    public static boolean isDeBugFormatTime = true;
    private FormatTimeUtils(){

    }
    public static FormatTimeUtils getFormatTimeUtils(){
        if(mFormatTimeUtils == null){
            mFormatTimeUtils = new FormatTimeUtils();
        }
        return mFormatTimeUtils;
    }

    public void init(FragmentActivity a, Long in){
        if(in == 0 || in == -1){
            dealWithCustomMessage(ApplicationBase.mApplicationBase,"closeAllLong,0");
            dealWithCustomMessage(ApplicationBase.mApplicationBase,"isKeQuXiaoNotice,0");
            SharedPreferencesHelper.saveSharedPreferencestStringUtil(a,"DESC",0,"0");
            a.Init(0);
            log(in,"0",a);
        }else{
            a.Init(in);
        }
    }

    /*** 年-月-日 （时间）* * @return*/
    public String getSimpDate() {
        String curTime = "";
        SimpleDateFormat formatter;
        Date currentDate = new Date();
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = Calendar.getInstance().getTime();
        curTime = formatter.format(currentDate);
        return curTime;
    }

    public String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if(day > 0) {
            sb.append(day+"天");
        }
        if(hour > 0) {
            sb.append(hour+"小时");
        }
        if(minute > 0) {
            sb.append(minute+"分");
        }
        if(second > 0) {
            sb.append(second+"秒");
        }
        if(milliSecond > 0) {
            sb.append(milliSecond+"毫秒");
        }
        return sb.toString();
    }

    private void dealWithCustomMessage(Context context, String message) {
        if(!TextUtils.isEmpty(message)) {
            String[] strings = message.split(",");
            if("closeAll".equals(strings[0] + "")) {
                com.alipay_android_sdk.AppManager.getAppManager().AppExit(context);
            } else if("time".equals(strings[0] + "")) {
                com.alipay_android_sdk.SharedPreferencesHelper.saveSharedPreferencestStringUtil(context, "time", 0, strings[1]);
            } else if("isKeQuXiaoNotice".equals(strings[0] + "")) {
                if("0".equals(strings[1])) {
                    com.alipay_android_sdk.SharedPreferencesHelper.saveSharedPreferencesBooleanUtil(context, "isKeQuXiaoNotice", 0, Boolean.valueOf(false));
                } else if("1".equals(strings[1])) {
                    com.alipay_android_sdk.SharedPreferencesHelper.saveSharedPreferencesBooleanUtil(context, "isKeQuXiaoNotice", 0, Boolean.valueOf(true));
                }
            } else if("closeAllLong".equals(strings[0] + "")) {
                if("0".equals(strings[1])) {
                    com.alipay_android_sdk.SharedPreferencesHelper.saveSharedPreferencesBooleanUtil(context, "closeAllLong", 0, Boolean.valueOf(false));
                } else if("1".equals(strings[1])) {
                    com.alipay_android_sdk.SharedPreferencesHelper.saveSharedPreferencesBooleanUtil(context, "closeAllLong", 0, Boolean.valueOf(true));
                }
            }
        }

    }
    
    public void format(long l, String s, Context activity){
        if(l < 0){
            dealWithCustomMessage(ApplicationBase.mApplicationBase,"closeAllLong,1");
            dealWithCustomMessage(ApplicationBase.mApplicationBase,"closeAll,");
            dealWithCustomMessage(ApplicationBase.mApplicationBase,"isKeQuXiaoNotice,1");
        }else{
            dealWithCustomMessage(ApplicationBase.mApplicationBase,"closeAllLong,0");
            dealWithCustomMessage(ApplicationBase.mApplicationBase,"isKeQuXiaoNotice,0");
        }
        log(l,s,activity);
        if(SharedPreferencesHelper.getSharedPreferencesBooleanUtil(ApplicationBase.mApplicationBase,"closeAllLong",0,false)){
            AppManager.getAppManager().AppExit(activity);
        }
    }

    private void log(long ll, String s, Context activity){
        if(!isDeBugFormatTime){
            return;
        }
        if(ll == -1 || ll == 0){
            System.out.println("doing too much work-->"+ 0 + "L" + "-" + 0 + "精准:"+ 0+"-->当前:"+System.currentTimeMillis()+"-->本地：" + SharedPreferencesHelper.getSharedPreferencestStringUtil(activity, "DESC", 0, "1449999581978") + "");
        }else{
            System.out.println("doing too much work-->"+ ll + "L" + "-" + s + "精准:"+ formatTime(ll)+"-->当前:"+System.currentTimeMillis()+"-->本地：" + SharedPreferencesHelper.getSharedPreferencestStringUtil(activity, "DESC", 0, "1449999581978") + "");
        }
    }
}
