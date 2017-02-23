package com.ltf.mytoolslibrary.viewbase.alphaview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ltf.mytoolslibrary.viewbase.base.FragmentBase;
import com.ltf.mytoolslibrary.viewbase.utils.AutoUtils;

import butterknife.ButterKnife;

/**
 * @author fml
 * created at 2016/6/24 15:17
 * description：懒加载
 * 子类使用方式:LazyFragment使用懒加载方法，避免切换fragment的时候造成其它fragment onstart方法运行,lazyLoad方法代替onstart方法
 */
public abstract class LazyFragment extends FragmentBase {

    /**
     * 设置资源布局
     *
     * @return
     */
    protected abstract int setLayoutId();
    // 标志fragment是否初始化完成
    private boolean isPrepared = false;
    //Fragment的布局View
    public View mView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(setLayoutId(), container, false);
            AutoUtils.auto(mView);
            ButterKnife.bind(this,mView);
            isPrepared = true;
            initView();
        }
        return mView;
    }

    /**
     * 自定义抽象加载数据方法 所有更新UI或操作UI的方法放在这里面
     * */
    protected abstract void initTitle();

    @Override
    protected void initView() {
        if(!isPrepared || !isVisible) {
            return;
        }

        initTitle();
    }
}
