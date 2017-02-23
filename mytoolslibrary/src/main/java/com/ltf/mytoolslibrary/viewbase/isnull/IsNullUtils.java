package com.ltf.mytoolslibrary.viewbase.isnull;

import android.text.TextUtils;

/**
 * 作者：${郭林海} on 2016/11/28 11:17
 * 邮箱：zaq2584157471@163.com
 * 注解: 字符串非空判断
 */
public class IsNullUtils {

    /***
     * 字符串非空判断
     * value：待判定的值
     * return 判断结果
     * **/
    public static boolean isNulls(String value){
        return "[]".equals(value+"")||TextUtils.isEmpty(value)||"null".equals(value+"");
    }

    /***
     * 字符串非空判断  value集合内所有不为null才返回false  只要有一个为null就返回true
     * 相当于与运算 &&
     * value：待判定的值
     * return 判断结果
     * **/
    public static boolean isNulls(String... value){
        boolean isNull = false;
        for (int i=0;i<value.length;i++){
            if(isNulls(value[i])){
                isNull = true;
            }
        }
        return isNull;
    }

    /***
     * 字符串非空判断  value集合内所有不为null才返回false  只要有一个为null就返回true
     * 相当于与运算 &&
     * value：待判定的值
     * return 判断结果
     * **/
    public static boolean isNullss(Object... value){
        boolean isNull = false;
        for (int i=0;i<value.length;i++){
            if(value[i] instanceof String){
                if(isNulls(value[i].toString())){
                    isNull = true;
                }
            }else {
                if(value[i] == null){
                    isNull = true;
                }
            }
        }
        return isNull;
    }

    /***
     * 字符串非空判断
     * value：待判定的值
     * return 判断结果
     * **/
    public static String isNullBackZero(String value){
        if(isNulls(value)){
            value = "0";
        }
        return value;
    }

    /***
     * 字符串非空判断
     * value：待判定的值
     * return 判断结果
     * **/
    public static String isNullBackStr(String value,String str){
        if(isNulls(value)){
            return str;
        }
        return value;
    }
}
