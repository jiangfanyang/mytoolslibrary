package com.ltf.mytoolslibrary.viewbase.mvp;

/**
 * Created by Administrator on 2016/1/20 0020.
 */
public abstract interface Presenter {

    void showLoading();

    void hideLoading();

    void showError(String e);
}
