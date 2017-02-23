package com.ltf.mytoolslibrary.viewbase.mvp;

/**
 * 作者：${李堂飞} on 2017/1/11 17:23
 * 邮箱：1195063924@qq.com
 * 注解:
 */

public interface StringCallbacks {
    void onError(Exception error);
    void onResponse(String json);
}
