package com.ltf.mytoolslibrary.viewbase.mvp;



/**
 * Created by Administrator on 2016/1/22 0022.
 */
public interface ICallBackListener<T> {
    void initData(T arg0);
    void showFailed(String error);
}
