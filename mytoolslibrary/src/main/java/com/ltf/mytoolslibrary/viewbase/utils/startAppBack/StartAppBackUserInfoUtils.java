package com.ltf.mytoolslibrary.viewbase.utils.startAppBack;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import com.ltf.mytoolslibrary.viewbase.constent.constent;
import com.ltf.mytoolslibrary.viewbase.utils.StartToUrlUtils;
import com.ltf.mytoolslibrary.viewbase.utils.show.L;
import com.ltf.mytoolslibrary.viewbase.views.UIAlertView;

/**
 * 作者：${李堂飞} on 2016/12/12 0012 17:19
 * 邮箱：1195063924@qq.com
 * 注解: 选择跑跑腿配送 并跳至零生活客户端进行发布
 */
public class StartAppBackUserInfoUtils {

    private static final int back_flage = constent.back_flage;
    private static StartAppBackUserInfoUtils mStartAppBackUserInfoUtils;
    public static StartAppBackUserInfoUtils getSelectUserExpressUtils(){
        if(mStartAppBackUserInfoUtils == null){
            mStartAppBackUserInfoUtils = new StartAppBackUserInfoUtils();
        }
        return mStartAppBackUserInfoUtils;
    }

    /**
     * 开始启动指定包名的APP应用 并设置监听 得到指定应用返回的数据
     * @param activity
     * @param appName 指定包名的应用名字
     * @param pakeName 指定包名的应用的包名
     * @param activityName 指定包名的需启动到的入口  必须为包名加应用入口 列如:com.ltf.app.ui.MainActivity
     * @param bundle 需要发送到第三方应用界面的数据
     * @param monBackStartAppPublishLisnners 指定回调  指定应用处理完后返回的数据回调
     */
    public void startAppPublish(final Activity activity, String appName, final String pakeName, String activityName, Bundle bundle,onBackStartAppPublishLisnner monBackStartAppPublishLisnners){
        this.monBackStartAppPublishLisnner = monBackStartAppPublishLisnners;
        if(StartToUrlUtils.getStartToUrlUtils().isAvilible(activity,pakeName)){
            Intent i = new Intent();
            if(bundle == null || "".equals(bundle+"")){

            }else{
                i.putExtras(bundle);
            }
            ComponentName cn = new ComponentName(pakeName,	// 应用包名
                    activityName);		// 要启动的activity  的全类名
            i.setComponent(cn);							// 给Initent设置组件
            activity.startActivityForResult(i, back_flage);		// 我的是mvp 所以上下文是这样的  需根据你自己的进行修改
        }else{
            final UIAlertView delDialog = new UIAlertView(
                    activity,
                    "温馨提示","是否前往应用市场安装"+appName+"客户端!",
                    "取消","安装" );
            delDialog.show();
            delDialog.setClicklistener(new UIAlertView.ClickListenerInterface(){

                @Override
                public void doLeft() {
                    delDialog.dismiss();
                }

                @Override
                public void doRight() {
                    delDialog.dismiss();
                    StartToUrlUtils.getStartToUrlUtils().launchAppDetail(activity,pakeName,"");
                }
            });
        }
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(monBackStartAppPublishLisnner != null && requestCode == back_flage && resultCode == back_flage && data != null){
            monBackStartAppPublishLisnner.onBackStartAppPublish(requestCode, resultCode, data);
        }else{
            if(requestCode == back_flage)
            L.e("选择跑跑腿配送 并跳至零生活客户端进行发布","回调监听:"+monBackStartAppPublishLisnner+"->requestCode="+requestCode+"->resultCode="+resultCode+"->用户设置的返回标志:"+back_flage);
        }
    }

    private static onBackStartAppPublishLisnner monBackStartAppPublishLisnner;
    public interface onBackStartAppPublishLisnner{
        void onBackStartAppPublish(int requestCode, int resultCode, Intent data);
    }
}
