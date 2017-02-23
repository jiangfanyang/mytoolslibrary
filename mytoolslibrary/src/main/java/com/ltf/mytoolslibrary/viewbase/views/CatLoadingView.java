package com.ltf.mytoolslibrary.viewbase.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ltf.mytoolslibrary.R;
import com.ltf.mytoolslibrary.viewbase.gif.GifDecoder;
import com.ltf.mytoolslibrary.viewbase.gif.GifDrawer;

/**
 * Created by Administrator on 2016/3/30. 框架网络加载动画
 *
 * GifDecoder.with(getActivity()).load(current_appAdStructItem.img_url, new GifDecoder.OnLoadGifListener() {
@Override
public void loadGifSuccess(File file) {
GifDecoder.with(getActivity()).load(file).into(mFloatingView);
}

@Override
public void loadGifFailed() {
onGainAdError();
}
}).into(mFloatingView);
 */
public class CatLoadingView extends Dialog {

    public static boolean isUsedCatLoadingView = true;
    Dialog mDialog;
    private Activity context;

    public CatLoadingView(Activity context) {
        super(context);
        this.context = context;
        init(context);
    }


    public CatLoadingView(Activity context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        init(context);
    }

    protected CatLoadingView(Activity context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
        init(context);
    }

    private GifDecoder myGifView;
    private GifDrawer mGifDrawer;
    private void init(Activity activity){
        if (mDialog == null) {
            mDialog = new Dialog(activity, R.style.cart_dialog);
            View views = LayoutInflater.from(activity).inflate(R.layout.common_loding_view,null);
//            AutoUtils.auto(views);
            mDialog.setContentView(views);
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.getWindow().setGravity(Gravity.CENTER);

            View v = mDialog.getWindow().getDecorView();
            // 从xml中得到GifView的句柄，当然，事先还要引入GifView的包import com.ant.liao.GifView;
            if(myGifView == null){
                myGifView = new GifDecoder();
            }
            ImageView iv = (ImageView) v.findViewById(R.id.img_gif);
            mGifDrawer = myGifView.with(activity).load(R.drawable.loding);
            mGifDrawer.into(iv);
            LinearLayout loding = (LinearLayout) v.findViewById(R.id.loding);
            TextView tv = (TextView) v.findViewById(R.id.tv);
//            loding.setLayoutParams(new FrameLayout.LayoutParams(AutoUtils.getDisplayWidthValue(160),AutoUtils.getDisplayHeightValue(160)));
//            tv.setTextSize(AutoUtils.getDisplayHeightValue(8));
            // 设置Gif图片源，首先要将loading.gif导入到drawable文件内
           // myGifView.setGifImage(R.drawable.loding);//
            // 添加监听器
            //myGifView.setOnClickListener(this);
            // 设置显示的大小，拉伸或者压缩
            //myGifView.setShowDimension(AutoUtils.getDisplayWidthValue(200),AutoUtils.getDisplayWidthValue(220));//
            // 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
            //myGifView.setGifImageType(GifView.GifImageType.COVER);//

            mDialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    dismisss();
                }
            });
        }
    }

    public void shows() {
        if(isUsedCatLoadingView){
            if(!this.context.isFinishing() && !mDialog.isShowing()){
                mDialog.show();
            }
        }
    }

    public void dismisss(){
        if(isUsedCatLoadingView){
            if(!this.context.isFinishing())
            mDialog.dismiss();
        }
    }

    public void onDismiss() {
        mDialog = null;
        myGifView.pauseGif();
        myGifView.closeGif();
        mGifDrawer.pauseMovie();
        mGifDrawer.closeMovie();
        this.context = null;
        System.gc();
    }
}
