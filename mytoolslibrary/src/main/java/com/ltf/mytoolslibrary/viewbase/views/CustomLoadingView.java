package com.ltf.mytoolslibrary.viewbase.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.ltf.mytoolslibrary.R;

/**
 * Created by Administrator on 2016/3/30. 框架网络加载动画 含自定义返回View
 */
public class CustomLoadingView extends Dialog {

    public static boolean isUsedCatLoadingView = true;
    Animation operatingAnim, eye_left_Anim, eye_right_Anim;

    Dialog mDialog;

    View mouse, eye_left, eye_right;

    EyelidView eyelid_left, eyelid_right;

    GraduallyTextView mGraduallyTextView;

    private boolean isCustomLoding = false;
    private int customLodingView;
    private UIAlertCustomBackView.onUIAlertCustomBackViewLisnner clickListenerInterface;

    public CustomLoadingView(Activity context) {
        super(context);
        init(context);
    }

    public CustomLoadingView(Activity context, boolean isCustomLoding, int customLodingView) {
        super(context);
        this.isCustomLoding = isCustomLoding;
        this.customLodingView = customLodingView;
        init(context);
    }

    public CustomLoadingView(Activity context, int customLodingView, UIAlertCustomBackView.onUIAlertCustomBackViewLisnner mUIAlertCustomBackView) {
        super(context);
        if(customLodingView != 0){
            this.isCustomLoding = true;
        }else{
            this.isCustomLoding = false;
        }
        this.customLodingView = customLodingView;
        this.clickListenerInterface = mUIAlertCustomBackView;
        init(context);
    }

    public CustomLoadingView(Activity context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected CustomLoadingView(Activity context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Activity activity){
        if (mDialog == null) {
            mDialog = new Dialog(activity, R.style.cart_dialog);
            if(isCustomLoding && customLodingView != 0){
                mDialog.setContentView(customLodingView);
            }else{
                mDialog.setContentView(R.layout.catloading_main);
            }
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.getWindow().setGravity(Gravity.CENTER);

            if(isCustomLoding){
                if(clickListenerInterface != null){
                    clickListenerInterface.onUIAlertCustomBackView(this,mDialog.getWindow().getDecorView());
                }
            }else{
                operatingAnim = new RotateAnimation(360f, 0f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                operatingAnim.setRepeatCount(Animation.INFINITE);
                operatingAnim.setDuration(2000);

                eye_left_Anim = new RotateAnimation(360f, 0f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                eye_left_Anim.setRepeatCount(Animation.INFINITE);
                eye_left_Anim.setDuration(2000);

                eye_right_Anim = new RotateAnimation(360f, 0f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                eye_right_Anim.setRepeatCount(Animation.INFINITE);
                eye_right_Anim.setDuration(2000);

                LinearInterpolator lin = new LinearInterpolator();
                operatingAnim.setInterpolator(lin);
                eye_left_Anim.setInterpolator(lin);
                eye_right_Anim.setInterpolator(lin);

                View view = mDialog.getWindow().getDecorView();

                mouse = view.findViewById(R.id.mouse);

                eye_left = view.findViewById(R.id.eye_left);

                eye_right = view.findViewById(R.id.eye_right);

                eyelid_left = (EyelidView) view.findViewById(R.id.eyelid_left1);

                eyelid_left.setColor(Color.parseColor("#d0ced1"));

                eyelid_left.setFromFull(true);

                eyelid_right = (EyelidView) view.findViewById(R.id.eyelid_right);

                eyelid_right.setColor(Color.parseColor("#d0ced1"));

                eyelid_right.setFromFull(true);

                mGraduallyTextView = (GraduallyTextView) view.findViewById(
                        R.id.graduallyTextView);

                operatingAnim.setAnimationListener(
                        new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }


                            @Override
                            public void onAnimationEnd(Animation animation) {
                            }


                            @Override
                            public void onAnimationRepeat(Animation animation) {
                                eyelid_left.resetAnimator();
                                eyelid_right.resetAnimator();
                            }
                        });
            }

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
            mDialog.show();
            if(!isCustomLoding){
                onResume();
            }
        }
    }

    public void onResume() {
        mouse.setAnimation(operatingAnim);
        eye_left.setAnimation(eye_left_Anim);
        eye_right.setAnimation(eye_right_Anim);
        eyelid_left.startLoading();
        eyelid_right.startLoading();
//        mGraduallyTextView.startLoading();
    }

    public void dismisss(){
        if(isUsedCatLoadingView){
            onPause();
            if(!isCustomLoding){
                mDialog.dismiss();
            }
        }
    }

    public void onPause() {
        if(isCustomLoding){
            return;
        }
        operatingAnim.reset();
        eye_left_Anim.reset();
        eye_right_Anim.reset();

        mouse.clearAnimation();
        eye_left.clearAnimation();
        eye_right.clearAnimation();

        eyelid_left.stopLoading();
        eyelid_right.stopLoading();
//        mGraduallyTextView.stopLoading();
    }


    public void onDismiss() {
        mDialog = null;
        onPause();
        System.gc();
    }
}
