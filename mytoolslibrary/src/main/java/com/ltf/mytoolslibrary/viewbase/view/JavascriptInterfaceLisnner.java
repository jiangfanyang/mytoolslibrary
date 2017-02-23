package com.ltf.mytoolslibrary.viewbase.view;

import android.app.Activity;
import android.webkit.WebView;

/**
 * 作者：${李堂飞} on 2016/11/28 0028 11:30
 * 邮箱：1195063924@qq.com
 * 注解:
 */
public interface JavascriptInterfaceLisnner {
    /***回传当前WebView布局的View 可以在此回调中初始化自定义布局参数值**/
    void onInitView(Activity activity, WebView webView);
    /***回调网页传入原生的参数 次方法是网页调用原生方法时传入参数的回调方法**/
    void onJavascriptInterfaceFromWebValueBacks(String method, Object data,WebViewJavascriptBridgeClient.WVJBResponseCallback callback);
    /***回调从URl回来的事件 次事件是原生调用网页方法后回调的方法**/
    void onJavascriptInterfaceToWebValueBacks(WebViewJavascriptBridgeClient bridgeWebViewClient);
}
