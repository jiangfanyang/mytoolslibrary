package com.ltf.mytoolslibrary.viewbase.takephoto.ui;

import android.os.Bundle;

import com.ltf.mytoolslibrary.viewbase.base.ActivityTitleBase;


/**
 * ================================================
 * 作    者：李堂飞
 * 版    本：1.0
 * 创建日期：2016/5/19
 * 描    述：
 * 修订历史：
 * ================================================
 */
public abstract class ImageBaseActivity extends ActivityTitleBase {

    /**
     * 初始化标题栏颜色
     *
     * @param flage
     * @param coror
     **/
    @Override
    public void initTitleBarBg(int flage, int coror) {

    }

//    protected SystemBarTintManager tintManager;

    /**
     * 继承TitleFragmentBase  初始化title将要显示什么出来  什么可以点击
     */
    @Override
    protected void initTitle() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//        }
//        tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setStatusBarTintResource(R.color.status_bar);  //设置上方状态栏的颜色
    }

//    @TargetApi(19)
//    private void setTranslucentStatus(boolean on) {
//        Window win = getWindow();
//        WindowManager.LayoutParams winParams = win.getAttributes();
//        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//        if (on) {
//            winParams.flags |= bits;
//        } else {
//            winParams.flags &= ~bits;
//        }
//        win.setAttributes(winParams);
//    }
}
