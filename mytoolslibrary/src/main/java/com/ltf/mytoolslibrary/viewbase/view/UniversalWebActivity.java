package com.ltf.mytoolslibrary.viewbase.view;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ltf.mytoolslibrary.R;
import com.ltf.mytoolslibrary.viewbase.base.ActivityTitleBase;
import com.ltf.mytoolslibrary.viewbase.constent.constent;
import com.ltf.mytoolslibrary.viewbase.isnull.IsNullUtils;
import com.ltf.mytoolslibrary.viewbase.utils.AnyIdCardCheckUtils;
import com.ltf.mytoolslibrary.viewbase.utils.AppManager;
import com.ltf.mytoolslibrary.viewbase.utils.StartToUrlUtils;
import com.ltf.mytoolslibrary.viewbase.utils.show.L;
import com.ltf.mytoolslibrary.viewbase.utils.show.T;
import com.ltf.mytoolslibrary.viewbase.views.CatLoadingView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 通用Web TitleBar 李堂飞 20160603
 *
 * 调用html中的js方法  详细请看以下链接
 * https://github.com/airymiao/WebViewJavascriptBridge/tree/master/ExampleApp-Android
 *
 */
public class UniversalWebActivity extends ActivityTitleBase {

    public WebView mLoadWebView;
    private String FristUrl = "";
    private String finalUrl = "";

    @Override
    public void initisBack() {
        super.initisBack();
        setIsBackUp(true);
    }

    @Override
    public void onTitleBackClick() {
        super.onTitleBackClick();
        exitByDoubleClick();
        if(!FristUrl.endsWith("/")){
            FristUrl = FristUrl + "/";
        }

        if(!finalUrl.endsWith("/")){
            finalUrl = finalUrl + "/";
        }

        if(FristUrl.replace("http://","https://").equals(finalUrl.replace("http://","https://"))){
            AppManager.getAppManager().finishActivity(this);
            return;
        }
        if (mLoadWebView.canGoBack()) {
            mLoadWebView.goBack(); // goBack()表示返回WebView的上一页面
        }else{
            AppManager.getAppManager().finishActivity(this);
        }
    }

    private String url1 = "";
    private boolean isTitle = true;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url1 = getIntent().getStringExtra(constent.WEBSITE);
        L.e("WebUrl",url1);

        FristUrl = url1;
        mLoadWebView.setDownloadListener(new DownloadListener() {//监听网页中的下载任务 这里默认跳转手机自带浏览器进行下载
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (IsNullUtils.isNulls(url) && AnyIdCardCheckUtils.getInstance(UniversalWebActivity.this).isUrl(url)){
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    StartToUrlUtils.getStartToUrlUtils().startToUrlFromMobile(url, UniversalWebActivity.this);
                }
            }
        });

        WebSettings setting = mLoadWebView.getSettings();
        //支持javascript
        setting.setJavaScriptEnabled(true);
        // 设置可以支持缩放
        setting.setSupportZoom(true);
        setting.setBuiltInZoomControls(true);
        //扩大比例的缩放
        setting.setUseWideViewPort(true);
        setting.setDisplayZoomControls(false);//隐藏Zoom缩放按钮

        setting.setJavaScriptEnabled(true);//设置webView可以实现上传图片
        // 设置可以访问文件
        setting.setAllowFileAccess(true);
        //自适应屏幕
        mLoadWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mLoadWebView.getSettings().setLoadWithOverviewMode(true);

//        mLoadWebView.loadUrl(url1);
        //加载assets目录下面的demo.html 界面
        //mLoadWebView.loadUrl("file:///android_asset/demo.html");

        this.renderWebView();
    }

    private static JavascriptInterfaceLisnner mJavascriptInterfaceLisnner;

    /**
     * 注意:如果当前WebView不存在则会返回null
     * 调用html中的js方法
     * mLoadWebView.loadUrl("javascript:updateHtml()");
     * @return WebViewJs
     */
    public static void setonJavascriptInterfaceLisnner(JavascriptInterfaceLisnner mJavascriptInterfaceLisnners){
        mJavascriptInterfaceLisnner = mJavascriptInterfaceLisnners;
    }

    @Override
    public boolean setIsViewStaueColor() {
        return true;
    }

    @Override
    public String setStatusBarTintResource() {
        if(getIntent().getBooleanExtra("isTitle",true)){
            return ""+0;
        }
        return ""+-1;
    }

    /***是否是自定义布局***/
    private boolean isZiDingYiLayout = false;
    private View status_bar;
    @Override
    protected int setLayoutId() {
        int layoutId = 0;
        isTitle = getIntent().getBooleanExtra("isTitle",true);
        int setLayoutId = getIntent().getIntExtra("setLayoutId",0);
        if(setLayoutId == 0){
            isZiDingYiLayout = false;
            if(isTitle){
                layoutId = R.layout.activity_universalwebview;
            }else{
                layoutId = R.layout.activity_universalwebview_no_title;
            }
        }else{
            isZiDingYiLayout = true;
            layoutId = setLayoutId;
        }
        return layoutId;
    }

    @Override
    // 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        exitByDoubleClick();
        if(!FristUrl.endsWith("/")){
            FristUrl = FristUrl + "/";
        }

        if(!finalUrl.endsWith("/")){
            finalUrl = finalUrl + "/";
        }
        if(FristUrl.replace("http://","https://").equals(finalUrl.replace("http://","https://"))){
            AppManager.getAppManager().finishActivity(this);
            return super.onKeyDown(keyCode,event);
        }
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mLoadWebView.canGoBack()) {
            mLoadWebView.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }else{
            AppManager.getAppManager().finishActivity(this);
        }

        return super.onKeyDown(keyCode,event);
    }

    private CatLoadingView mShow;
    private String title;

    /***WebView网页内调原生的方法 在回调中回传网页给原生数据为JSON**/
    private ArrayList<String> fromWebMethod;
    @Override
    protected void initTitle() {
        if(mShow == null){
            mShow = new CatLoadingView(this);
        }

        if(getIntent().getIntExtra("webViewId",0) != 0){
            mLoadWebView = (WebView) findViewById(getIntent().getIntExtra("webViewId", R.id.pw_loadwebview));
        }else{
            mLoadWebView = (WebView) findViewById(R.id.pw_loadwebview);
        }

        fromWebMethod = getIntent().getStringArrayListExtra("fromWebMethod");
        if(!IsNullUtils.isNulls(fromWebMethod+""))
            L.e("web中的方法集合",fromWebMethod.size()+"");

        if(isZiDingYiLayout){
            //自定义布局交给调用方处理布局空间初始化任务
        }else{
            isTitle = getIntent().getBooleanExtra("isTitle",true);
            title = getIntent().getStringExtra("title");
            if(isTitle){
                setUpTitleBack();
                setUpTitleCentreText(title);
            }else{
                status_bar = findViewById(R.id.status_bar);//设置状态栏是否有颜色 默认没有颜色
            }
        }
        if(mJavascriptInterfaceLisnner != null){
            L.e("UniversalWebActivity","界面初始化了...");
            mJavascriptInterfaceLisnner.onInitView(this,mLoadWebView);
        }
//	    pb.setMax(100);
    }

    private WebViewJavascriptBridgeClient bridgeWebViewClient;
    /**
     * Web View Bridge Center
     */
    private void renderWebView() {

        this.registerJavascriptBridge(mLoadWebView);

        Map<String, String> noCacheHeaders = new HashMap<>(2);
        noCacheHeaders.put("Pragma", "no-cache");
        noCacheHeaders.put("Cache-Control", "no-cache");

        L.e("UniversalWebActivity","开始web和原生方法互调...");
        mLoadWebView.loadUrl(this.url1, noCacheHeaders);
    }

    private void registerJavascriptBridge(WebView bridgeWebView) {
        if(bridgeWebViewClient == null){
            bridgeWebViewClient = new WebViewJavascriptBridgeClient(bridgeWebView){

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    finalUrl = request.getUrl().toString();
//                    view.loadUrl(finalUrl);
                    return super.shouldOverrideUrlLoading(view, request);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    finalUrl = url;
//                    view.loadUrl(url);
                    return super.shouldOverrideUrlLoading(view, url);//return super.shouldOverrideUrlLoading(view, url); 这个返回的方法会调用父类方法，也就是跳转至手机浏览器
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
//                    T.showShort(UniversalWebActivity.this, "网络错误");
                    if(mShow != null)
                        mShow.dismisss();
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    // TODO Auto-generated method stub
                    super.onPageStarted(view, url, favicon);
                    if(mShow != null && !mShow.isShowing())
                        mShow.shows();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    // TODO Auto-generated method stub
                    if(mShow != null)
                        mShow.dismisss();
                    super.onPageFinished(view, url);
                }
            };
        }

        bridgeWebViewClient.enableLogging();

        this.bindJavascriptBridge();
        if(mJavascriptInterfaceLisnner != null){
            mJavascriptInterfaceLisnner.onJavascriptInterfaceToWebValueBacks(bridgeWebViewClient);
        }
    }

    private final String AppLogTag = "WebViewJavascriptBridge";

    /**
     * toWebData集合里面的数据格式为JSON
     * 调用网页方法
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
     * @param bridgeWebViewClients
     * @param toWebMethod 原生调用网页时  网页中的方法名
     * @param toWebData 原生调用网页时  传入网页中的值
     */
    public void onJavascriptInterfaceToWebValueBackss(WebViewJavascriptBridgeClient bridgeWebViewClients, final String toWebMethod, Object toWebData, final WebViewJavascriptBridgeClient.WVJBResponseCallback responseCallback) {//调用网页方法
        L.e("UniversalWebActivity","开始web和原生方法互调生产_原生调网页方法...");
        if(IsNullUtils.isNullss(bridgeWebViewClients,toWebMethod,toWebData)){
            L.e(AppLogTag,"bridgeWebViewClients == null?==>"+bridgeWebViewClients +"toWebMethod==null?==>"+toWebMethod+"toWebData==null?==>"+toWebData);
            return;
        }
        bridgeWebViewClients.callHandler(toWebMethod, toWebData, new WebViewJavascriptBridgeClient.WVJBResponseCallback() {
            @Override
            public void callback(Object data) {
                L.e(AppLogTag, "Response data from web "+ toWebMethod + ":" + data.toString());
                responseCallback.callback(data);
            }
        });
    }

    private void bindJavascriptBridge() {//网页调用原生方法
        L.e("UniversalWebActivity","开始web和原生方法互调生产...");
        if(IsNullUtils.isNullss(fromWebMethod) || fromWebMethod == null || (fromWebMethod!=null && fromWebMethod.size() ==0)){
            L.e(AppLogTag,"fromWebMethod == null?==>"+fromWebMethod +"fromWebMethod.size() == 0?==>"+(fromWebMethod == null?"":fromWebMethod.size()));
            return;
        }
        for (int i = 0;i<fromWebMethod.size();i++){
            L.e(AppLogTag,"fromWebMethod ==>"+fromWebMethod.get(i));
            bridgeWebViewClient.registerHandler(fromWebMethod.get(i), new WebViewJavascriptBridgeClient.WVJBHandler() {
                @Override
                public void request(Object data,String method, WebViewJavascriptBridgeClient.WVJBResponseCallback callback) {
                    L.e(AppLogTag, "Received data from web goGoodsViewController:" + data.toString());

                    if(mJavascriptInterfaceLisnner != null){
                        mJavascriptInterfaceLisnner.onJavascriptInterfaceFromWebValueBacks(method,data,callback);
                    }else{
                        callback.callback(data);
                    }
                }
            });
        }
    }
    private boolean isExit = false;
    private void exitByDoubleClick() {
        Timer tExit=null;
        if(!isExit){
            isExit=true;
            //用户按返回键达到两次
            tExit=new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit=false;//取消退出
                }
            },2000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        }else{
            AppManager.getAppManager().finishActivity(this);
        }
    }

}
