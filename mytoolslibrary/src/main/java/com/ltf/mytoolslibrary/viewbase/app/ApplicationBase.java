package com.ltf.mytoolslibrary.viewbase.app;

import com.alipay_android_sdk.Application;
import android.content.Context;
import android.text.TextUtils;

import com.ltf.mytoolslibrary.viewbase.CacheFolder.CacheFolderUtils;
import com.ltf.mytoolslibrary.viewbase.mvp.GetNetWorkDataImpl;
import com.ltf.mytoolslibrary.viewbase.recorderror.CrashHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 作者：${李堂飞} on 2016/8/15 0015 15:02
 * 邮箱：1195063924@qq.com
 * 注解: 框架Application
 */
public abstract class ApplicationBase extends Application {

    public static ApplicationBase mApplicationBase;
    public static List<String> urlList = new ArrayList<>();//网络请求队列
    /**
     * 设置是否开启错误日志搜集
     *
     * @return
     */
    protected abstract boolean setIsOpenCrashErrorMessage();
    /**
     * 设置错误日志缓存路径文件夹名称
     *
     * @return
     */
    public abstract String setCrashErrorMessageName();
    @Override
    public void onCreate() {

        super.onCreate();
        mApplicationBase = this;
        if(setIsOpenCrashErrorMessage()){
            startCrashErrorMessage(this);
        }
        if(urlList != null){
            urlList.clear();
        }
        init();
    }

    public static void init() {
        //一下语句必须放在上面判断之后  否则路径不对
        GetNetWorkDataImpl.init(60*1000*5);

        CacheFolderUtils.getCacheFolderUtils().setPicassoCashPath();

        CacheFolderUtils.getCacheFolderUtils().setCropCacheFolderBackFile(CacheFolderUtils.getCacheFolderUtils().CacheTopDirectoryName);
    }

    private static onBackErrorCrashMessage monBackErrorCrashMessage;
    public static void setonBackErrorCrashMessage(onBackErrorCrashMessage onBackErrorCrashMessages){
        monBackErrorCrashMessage = onBackErrorCrashMessages;
    }
    public interface onBackErrorCrashMessage{
        void onBackErrorCrashMessage(String fileUrl, String name);
    }

    /**
     * 得到当前运行的Context对应的Activity的名字
     * @param context
     * @return
     */
    public static String getRunningActivityName(Context context){
        String contextString = context.toString();
        return contextString.substring(contextString.lastIndexOf(".")+1, contextString.indexOf("@"));
    }

    public static String CrashErrorMessageName = "ZeroLife";
    /**
     * 启动全局监听app报错信息收集 文件保存在
     * Environment.getExternalStorageDirectory().getPath()/
     * FenYou/Crash/crash-2015-08-20-timestamp.log
     */
    public void startCrashErrorMessage(Context context) {
        CrashHandler crashHandler = CrashHandler.getInstance();
        CrashErrorMessageName = TextUtils.isEmpty(setCrashErrorMessageName())?"APPErrorCrash":setCrashErrorMessageName();
        crashHandler.init(context,CrashErrorMessageName);
        crashHandler.setonCrashMessageBack(crashMessageBack);
    }
    /**
     * 启动全局监听app报错信息收集 文件保存在
     * Environment.getExternalStorageDirectory().getPath()/
     * FenYou/Crash/crash-2015-08-20-timestamp.log
     */
    public static void startCrashErrorMessages(Context context) {
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(context,CrashErrorMessageName);
        crashHandler.setonCrashMessageBack(crashMessageBack);
    }

    private static CrashHandler.onCrashMessageBack crashMessageBack = new CrashHandler.onCrashMessageBack() {

        @Override
        public void onErrorBack(String fileUrl,String fileName) {
            // TODO 处理系统报错 信息收集 将发送到服务器备份 注意需要启动线程且不能在这里处理上传文件事件 建议发送handle
            System.out.println("app运行时异常,错误信息已记录...");
            if(monBackErrorCrashMessage !=null){
                monBackErrorCrashMessage.onBackErrorCrashMessage(fileUrl,fileName);
            }
        }
    };
}
