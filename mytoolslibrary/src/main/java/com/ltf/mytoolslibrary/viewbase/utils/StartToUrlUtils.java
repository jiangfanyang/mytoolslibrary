package com.ltf.mytoolslibrary.viewbase.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.ltf.mytoolslibrary.viewbase.constent.constent;
import com.ltf.mytoolslibrary.viewbase.isnull.IsNullUtils;
import com.ltf.mytoolslibrary.viewbase.utils.show.L;
import com.ltf.mytoolslibrary.viewbase.view.JavascriptInterfaceLisnner;
import com.ltf.mytoolslibrary.viewbase.view.UniversalWebActivity;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 李堂飛 on 2016/4/18 0018.
 * 封裝activity跳轉的方法可以調網頁也可以調Activity 也可以打电话
 */
public class StartToUrlUtils {

    private static StartToUrlUtils mStartToUrlUtils;
    public static StartToUrlUtils getStartToUrlUtils(){
        if(mStartToUrlUtils == null){
            mStartToUrlUtils = new StartToUrlUtils();
        }
        return mStartToUrlUtils;
    }
    private StartToUrlUtils(){

    }

    /**跳转Url指定网页 使用自定义UniversalWebActivity**/
    public void startToUrl(String url,Activity s,String title){
        url = PicassoUtil.getInstantiation().getUrlToUtf(url);
        Bundle bundle = new Bundle();
//        Pattern pattern = Pattern
//                .compile("((http://|https://){1}[\\w\\.\\-/:]+)|(#(.+?)#)|(@[\\u4e00-\\u9fa5\\w\\-]+)");
//        Matcher matcher = pattern.matcher(url);
        if(!AnyIdCardCheckUtils.getInstance(s).isUrl(url)){//如果不是网页地址则不跳转
//            T.showShort(s,"功能还未开放，敬请期待...");
            L.e("网址匹配","--->不是网页地址");
            return;
        }
        L.e("需要跳转URL",url);
        bundle.putString(constent.WEBSITE, url);
        bundle.putString("title", title);
        bundle.putBoolean("isTitle", true);
        Intent in = new Intent(s, UniversalWebActivity.class);
        in.putExtras(bundle);
        s.startActivity(in);
    }

    /**跳转Url指定网页 使用自定义UniversalWebNoBarActivity 没有TitleBar的**/
    public void startToUrl(String url,Activity s){
        url = PicassoUtil.getInstantiation().getUrlToUtf(url);
        Bundle bundle = new Bundle();
        if(!AnyIdCardCheckUtils.getInstance(s).isUrl(url)){//如果不是网页地址则不跳转
            L.e("网址匹配","--->不是网页地址");
            return;
        }
        L.e("需要跳转URL",url);
        bundle.putString(constent.WEBSITE, url);
        bundle.putBoolean("isTitle", false);
        Intent in = new Intent(s, UniversalWebActivity.class);
        in.putExtras(bundle);
        s.startActivity(in);
    }

    /**
     * 跳转Url指定网页 使用自定义UniversalWebNoBarActivity
     * @param s 上下文
     * @param url 指定链接URL
     * @param titleName webView 标题栏中间的文字
     * @param setLayoutId 自定义WebView的布局样式 不使用自定义布局请传值0 例如 com.ltf.mytoolslibrary.R.layout.activity_universalwebview
     * @param webViewId 给定WebView的id 不使用自定义布局请传值0 例如 R.id.pw_loadwebview
     * @param fromWebMethod WebView网页内调原生的方法 在回调中回传网页给原生数据为JSON
     * 普通使用方法:
     * 1.原生调用网页testJavascriptHandler方法更新网页 并传入参数jsonData  -->是一个JSONObject
     * JSONObject jsonData = new JSONObject();
    try {
    JSONObject messageData = new JSONObject();
    messageData.put("content", "From Android");

    jsonData.put("code", "2000");
    jsonData.put("type", "alert");
    jsonData.put("message", messageData);
    } catch (JSONException e) {
    e.printStackTrace();
    }
    //向框架发起通知让框架去处理网页方法的调用
    UniversalWebActivity.onJavascriptInterfaceToWebValueBackss(bridgeWebViewClient,"testJavascriptHandler",jsonData);
     2.网页调用原生方法 所有方法调用前进行框架注册
    ArrayList<String> s3 = new ArrayList<>();
    s3.add("goShopViewController");
    s3.add("goGoodsViewController");
    StartToUrlUtils.getStartToUrlUtils().startToUrl(getActivity(), ZeroLifeURl.IP + "/app/zlifeclient/ExampleApp.html", null, 0, 0, s3, new JavascriptInterfaceLisnner() {
    @Override
    public void onInitView(Activity view, WebView webView) {//自定义传入布局时 在这里初始化 通过Activity的findViewById更新空间信息
    L.e("onInitView","-----------");
    }

    @Override
    public void onJavascriptInterfaceFromWebValueBacks(String method, Object data) {//网页传入数据到原生的回调
    L.e("webView回调", "Response data from web "+ method+":" + data.toString());
    }

    //回调从URl回来的事件 次事件是原生调用网页方法后回调的方法
    //此方法配合第一种方式使用 是原生调用网页时需要的客户端 配合使用UniversalWebActivity.onJavascriptInterfaceToWebValueBackss(bridgeWebViewClient,"testJavascriptHandler",jsonData);从而进行原生调网页方法
    @Override
    public void onJavascriptInterfaceToWebValueBacks(WebViewJavascriptBridgeClient bridgeWebViewClients) {
        L.e("onJavascriptInterfaceToWebValueBacks","-----------");
        bridgeWebViewClient = bridgeWebViewClients;
    }
});
     */
    public void startToUrl(String url, Activity s,String titleName, int setLayoutId, int webViewId, ArrayList<String> fromWebMethod, JavascriptInterfaceLisnner mJavascriptInterfaceLisnner){
        url = PicassoUtil.getInstantiation().getUrlToUtf(url);
        UniversalWebActivity.setonJavascriptInterfaceLisnner(mJavascriptInterfaceLisnner);
        Bundle bundle = new Bundle();
        if(!AnyIdCardCheckUtils.getInstance(s).isUrl(url)){//如果不是网页地址则不跳转
            L.e("网址匹配","--->不是网页地址");
            return;
        }
        L.e("需要跳转URL",url);
        bundle.putString(constent.WEBSITE, url);
        if(IsNullUtils.isNullss(titleName)){
            bundle.putBoolean("isTitle", false);
        }else{
            bundle.putBoolean("isTitle", true);
            bundle.putString("title", titleName);
        }
        bundle.putInt("setLayoutId",setLayoutId);
        bundle.putInt("webViewId",webViewId);
        bundle.putStringArrayList("fromWebMethod",fromWebMethod);
        Intent in = new Intent(s, UniversalWebActivity.class);
        in.putExtras(bundle);
        s.startActivity(in);
    }
    
    /**跳转Url指定网页 使用手机自带的浏览器**/
    public void startToUrlFromMobile(String url,Activity s){
        if(!AnyIdCardCheckUtils.getInstance(s).isUrl(url)){//如果不是网页地址则不跳转
            L.e("网址匹配","--->不是网页地址");
            return;
        }
        L.e("需要跳转URL",url);
        s.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
    
    /**跳转Activity**/
    public void startToActivity(Activity s,Class<?> cls,Bundle bundle){
        Intent in = new Intent(s, cls);
        if(bundle == null || "".equals(bundle+"")){
        	
        }else{
        	in.putExtras(bundle);
        }
        s.startActivity(in);
    }
    
    /**带返回值跳转Activity**/
    public void startToActivity(Activity s,Class<?> cls,Bundle bundle,int requestCode){
        Intent in = new Intent(s, cls);
        if(bundle == null || "".equals(bundle+"")){
        	
        }else{
        	in.putExtras(bundle);
        }
        s.startActivityForResult(in, requestCode);
    }
    
    /**打电话**/
    public void startToCall(Activity s,String phone){
    	Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        s.startActivity(intent);
    }

    /**带参数启动高德地图
     * Activity s:上下文
     * route	服务类型	是
     * sourceApplication	第三方调用应用名称。如 amap	是
     * slat	起点纬度	是
     * slon	起点经度	是
     * sname	起点名称	是
     * dlat	终点纬度	是
     * dlon	终点经度	是
     * dname	终点名称	是
     * dev	起终点是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)	是
     * m	驾车方式 =0（速度快）=1（费用少） =2（路程短）=3 不走高速 =4（躲避拥堵）=5（不走高速且避免收费） =6（不走高速且躲避拥堵） =7（躲避收费和拥堵） =8（不走高速躲避收费和拥堵）。 公交 =0（速度快）=1（费用少） =2（换乘较少）=3（步行少）=4（舒适）=5（不乘地铁）	是
     * t	t = 1(公交) =2（驾车） =4(步行)
     * 高德地图包名:com.autonavi.minimap
     * 调用详情请访问:http://developer.amap.com/api/uri-api/android-uri-explain/
     * **/
    public void startToGaoDeMap(Activity s,String AppName,String slat,String slon,String sname,String dlat,String dlon,String dname){
        if(isAvilible(s,"com.autonavi.minimap")) {//传入指定应用包名
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("androidamap://route?sourceApplication="+AppName+
                    "&slat="+slat+ "&slon="+ slon+ "&sname="+sname+ "&dlat="+dlat+ "&dlon="+dlon+ "&dname="+dname+ "&dev=0&m=0&t=1"));
            intent.setPackage("com.autonavi.minimap");
            s.startActivity(intent);
        }else{
            //market为路径，id为包名
            //显示手机上所有的market商店
//            Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            s.startActivity(intent);
            launchAppDetail(s,"com.autonavi.minimap","");
        }
    }

    /**
     * 启动到app详情界面
     *
     * @param appPkg
     *            App的包名
     * @param marketPkg
     *            应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public void launchAppDetail(Activity ac,String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg))
                return;
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg))
                intent.setPackage(marketPkg);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ac.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**带参数启动百度地图
     * Activity s:上下文
     * 1(参数名称)origin(参数说明)起点名称或经纬度，或者可同时提供名称和经纬度，此时经纬度优先级高，将作为导航依据，名称只负责展示。(是否必选)必选	    (备注)1、名称：天安门 2、经纬度：39.98871 <纬度> ,116.43234 <经度> 。3、名称和经纬度：name:天安门|latlng:39.98871,116.43234
     * 2(参数名称)destination(参数说明)终点名称或经纬度，或者可同时提供名称和经纬度，此时经纬度优先级高，将作为导航依据，名称只负责展示。(是否必选)必选	   (备注) 同上
     * 3(参数名称)mode(参数说明)导航模式，固定为transit、driving、walking，分别表示公交、驾车和步行	(是否必选)必选
     * 4(参数名称)region(参数说明)城市名或县名	(是否必选)当给定region时，认为起点和终点都在同一城市，除非单独给定起点或终点的城市。
     *5(参数名称) origin_region(参数说明)起点所在城市或县(是否必选)同上
     * 6(参数名称)destination_region(参数说明)终点所在城市或县(是否必选)同上
     * 7(参数名称)coord_type(参数说明)坐标类型，可选参数，默认为bd09经纬度坐标。(是否必选)可选	(备注)允许的值为bd09ll、bd09mc、gcj02、wgs84。bd09ll表示百度经纬度坐标，bd09mc表示百度墨卡托坐标，gcj02表示经过国测局加密的坐标，wgs84表示gps获取的坐标。
     *8(参数名称) zoom(参数说明) 展现地图的级别，默认为视觉最优级别。(是否必选)可选
     * 9(参数名称)src(参数说明) 调用来源，规则：companyName|appName。(是否必选)必选
     * AppName	第三方调用应用名称。如 amap
     * slat	起点纬度 必选
     * slon	起点经度 必选
     * sname	起点名称 必选
     * dlat	终点纬度 必选
     * dlon	终点经度 必选
     * dname	终点名称 必选
     * region 城市名或县名 可选
     * yourCompanyName 你的应用公司名字 必选
     * 百度地图包名:com.baidu.BaiduMap
     * **/
    public void startToBaiDuMap(Activity s,String AppName,String slat,String slon,String sname,String dlat,String dlon,String dname,String region,String yourCompanyName){
        if(isAvilible(s,"com.baidu.BaiduMap")){//传入指定应用包名
            Intent intent = null;
            try {
                intent = Intent.getIntent("intent://map/direction?" +"origin=latlng:"+slat+","+slon+"|" +
                        "name:"+ sname +"&" + "destination=latlng:" +dlat+","+dlon+"|" +
                        "name:"+ dname +"&mode=driving&" + "region="+region +"&src="+yourCompanyName+"|"+AppName+"#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            } catch (URISyntaxException e) {
                e.printStackTrace();
                L.e("intent", e.getMessage());
            }
            s.startActivity(intent); //启动调用
        }else{//未安装
            //market为路径，id为包名
            //显示手机上所有的market商店
//            Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            s.startActivity(intent);
            launchAppDetail(s,"com.baidu.BaiduMap","");
        }

    }

    /**
     * 检查手机上是否安装了指定的软件
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public boolean isAvilible(Context context, String packageName){
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if(packageInfos != null){
            for(int i = 0; i < packageInfos.size(); i++){
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 判断当前时间是否在规定时间范围段内
     * @param StartHour 开始时间 小时
     * @param StartMin 开始时间 分钟
     * @param endHour 结束时间 小时
     * @param endtMin 结束时间 分钟
     */
    public boolean fomatTimeToIsYuNei(int StartHour,int StartMin,int endHour,int endtMin,onBackSearchResult monBackSearchResult){
        Calendar cal = Calendar.getInstance();// 当前日期
        int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
        int minute = cal.get(Calendar.MINUTE);// 获取分钟
        int minuteOfDay = hour * 60 + minute;// 从0:00分开是到目前为止的分钟数
        final int start = StartHour * 60 + StartMin;// 起始时间 17:20的分钟数
        final int end = endHour * 60 + endtMin;// 结束时间 19:00的分钟数

        if (minuteOfDay >= start && minuteOfDay <= end) {
            if(monBackSearchResult != null){
                monBackSearchResult.onBackSearchResult(true, "在输入时间段范外围内",minuteOfDay);
            }
            return true;
        } else {
            if(monBackSearchResult != null){
                monBackSearchResult.onBackSearchResult(false, "在输入时间段范围围外",minuteOfDay);
            }
            return false;
        }
    }

    /**
     * 判断当前时间是否在规定时间范围段内
     * @param StartHour 开始时间 小时
     * @param StartMin 开始时间 分钟
     * @param endHour 结束时间 小时
     * @param endtMin 结束时间 分钟
     */
    public boolean fomatTimeToIsYuNei(int StartHour,int StartMin,int endHour,int endtMin){
        Calendar cal = Calendar.getInstance();// 当前日期
        int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
        int minute = cal.get(Calendar.MINUTE);// 获取分钟
        int minuteOfDay = hour * 60 + minute;// 从0:00分开是到目前为止的分钟数
        final int start = StartHour * 60 + StartMin;// 起始时间 17:20的分钟数
        final int end = endHour * 60 + endtMin;// 结束时间 19:00的分钟数

        if (minuteOfDay > start) {//在输入时间内比如现在是15:00  而输入是13:00
//    		if(monBackSearchResult != null){
//    			monBackSearchResult.onBackSearchResult(true, "在输入时间段范外围内",minuteOfDay);
//    		}
            return false;
        } else {
//    		if(monBackSearchResult != null){
//    			monBackSearchResult.onBackSearchResult(false, "在输入时间段范围围外",minuteOfDay);
//    		}
            return true;
        }
    }

    public interface onBackSearchResult{
        /**
         * @param isIn 是否在范围内
         * @param backResult 返回处理结果字符串
         */
        void onBackSearchResult(Boolean isIn, String backResult, int nowTime);
    }

}
