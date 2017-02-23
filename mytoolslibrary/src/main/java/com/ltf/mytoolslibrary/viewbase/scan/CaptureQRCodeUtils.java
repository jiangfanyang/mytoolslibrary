package com.ltf.mytoolslibrary.viewbase.scan;

import android.Manifest;
import android.app.Activity;

import com.ltf.mytoolslibrary.viewbase.permission.CheckPermissionUtils;
import com.ltf.mytoolslibrary.viewbase.utils.StartToUrlUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：${李堂飞} on 2016/10/24 0024 14:05
 * 邮箱：1195063924@qq.com
 * 注解: 二维码扫描入口
 */
public class CaptureQRCodeUtils {

    private static CaptureQRCodeUtils mCaptureQRCodeUtils;
    public static CaptureQRCodeUtils getCaptureQRCodeUtils(){
        if(mCaptureQRCodeUtils == null){
            mCaptureQRCodeUtils = new CaptureQRCodeUtils();
        }
        return mCaptureQRCodeUtils;
    }
    private CaptureQRCodeUtils(){

    }

    /**
     * 二维码扫描入口
     * @param a 上下文
     * @param isSystom 是否使用系统 isSystom=true;表明使用系统内置扫描识别成功后自动跳转浏览器isSystom=false;表明自定义扫描识别后的规则
     * @param m 扫描结果回调  如果isSystom=true;设置为Null
     */
    public void startQRCodeUtils(final Activity a, boolean isSystom,CheckPermissionUtils.onBackPermissionResult m){
        if(!isSystom){
            CheckPermissionUtils.getSelectPicUpdateUtils().checkPermission(-1,a, true, m, new String[]{Manifest.permission.CAMERA});
        }else{
            CheckPermissionUtils.getSelectPicUpdateUtils().checkPermission(-1,a, true, new CheckPermissionUtils.onBackPermissionResult() {

                @Override
                public void onBackPermissionResult() {
                    StartToUrlUtils.getStartToUrlUtils().startToActivity(a, CaptureActivity.class, null);
                    CaptureActivity.setCaptureBarCodeResultLisner(new CaptureActivity.CaptureBarCodeResultLisner() {

                        @Override
                        public void onBackBarCodeResult(String backResult) {
                            Pattern pattern = Pattern
                                    .compile("((http://|https://){1}[\\w\\.\\-/:]+)|(#(.+?)#)|(@[\\u4e00-\\u9fa5\\w\\-]+)");
                            Matcher matcher = pattern.matcher(backResult);
                            if(!matcher.matches()){//如果不是网页地址则不跳转
//				            ToastHelper.showShortToast(getActivity(),"扫描识别结果:"+backResult);
                                StartToUrlUtils.getStartToUrlUtils().startToUrlFromMobile(backResult, a);
                                return;
                            }

                            StartToUrlUtils.getStartToUrlUtils().startToUrl(backResult, a, "");
                        }
                    }, true);
                }
            }, new String[]{Manifest.permission.CAMERA});
        }

    }
}
