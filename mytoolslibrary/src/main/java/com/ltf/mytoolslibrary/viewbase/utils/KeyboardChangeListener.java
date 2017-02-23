package com.ltf.mytoolslibrary.viewbase.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;

/**
 * 作者：${李堂飞} on 2016/11/12 0012 11:05
 * 邮箱：1195063924@qq.com
 * 注解: 监听软键盘弹出收起
 *
 * 实例:如果使用EditText 那么1. 在EditText的父控件加上android:focusable="true"
 android:focusableInTouchMode="true"
 如果不使用如果使用EditText 则将KeyboardChangeListener(Activity contextObj,EditText editText)中EditText editText传入空值(null)
 */
public class KeyboardChangeListener implements ViewTreeObserver.OnGlobalLayoutListener{
    private static final String TAG = "ListenerHandler";
    private View mContentView;
    private int mOriginHeight;
    private int mPreHeight;
    private KeyBoardListener mKeyBoardListen;

    public interface KeyBoardListener {
        /**
         * call back
         * @param isShow true is show else hidden
         * @param keyboardHeight keyboard height
         */
        void onKeyboardChange(boolean isShow, int keyboardHeight);
    }

    public void setKeyBoardListener(KeyBoardListener keyBoardListen) {
        this.mKeyBoardListen = keyBoardListen;
    }

    private EditText mEditText;
    private Activity contextObj;
    public KeyboardChangeListener(Activity contextObj,EditText editText) {
        this.mEditText = editText;
        this.contextObj = contextObj;
        if (contextObj == null) {
            Log.i(TAG, "contextObj is null");
            return;
        }
        mContentView = findContentView(contextObj);

        if (mContentView != null) {
            addContentTreeObserver();
        }else{
            Log.e(TAG, "----------->"+mContentView+"=null");
        }
    }

    private View findContentView(Activity contextObj) {
        return contextObj.findViewById(android.R.id.content);
    }

    private boolean init = false;
    private void addContentTreeObserver() {
        mContentView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        if(mEditText != null){
            mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
            {
                public void onFocusChange(View v, boolean hasFocus)
                {
                    if (hasFocus)
                    {
                        init = true;
                    }else{
                        init = false;
                    }
                }
            });
        }
    }

    @Override
    public void onGlobalLayout() {
        if(mEditText != null){
            boolean isShow = false;
            if (init)
            {
                if (isKeyboardShown(mEditText.getRootView()))
                {
                    isShow = true;
                    // Do something when keyboard is shown
                }
                else
                {
                    isShow = false;
                    // Do something when keyboard is hidden
                }
            }

            if (mKeyBoardListen != null) {
                mKeyBoardListen.onKeyboardChange(isShow, 0);
            }
        }else{
            int currHeight = mContentView.getHeight();
            if (currHeight == 0) {
                Log.i(TAG, "currHeight is 0");
                return;
            }
            boolean hasChange = false;
            if (mPreHeight == 0) {
                mPreHeight = currHeight;
                mOriginHeight = currHeight;
            } else {
                if (mPreHeight != currHeight) {
                    hasChange = true;
                    mPreHeight = currHeight;
                } else {
                    hasChange = false;
                }

                if (hasChange) {
                    boolean isShow;
                    int keyboardHeight = 0;
                    if (mOriginHeight == currHeight) {
                        //hidden
                        isShow = false;
                    } else {
                        //show
                        keyboardHeight = mOriginHeight - currHeight;
                        isShow = true;
                    }

                    if (mKeyBoardListen != null) {
                        mKeyBoardListen.onKeyboardChange(isShow, keyboardHeight);
                    }
                }
            }
        }
    }

    private boolean isKeyboardShown(View rootView)
    {
        final int SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD = 128;

        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        int heightDiff = rootView.getBottom() - r.bottom;

        return heightDiff > dip2px(contextObj,SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void destroy() {
        if (mContentView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }
    }
}
