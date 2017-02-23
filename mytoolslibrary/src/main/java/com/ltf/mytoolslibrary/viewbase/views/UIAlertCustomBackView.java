package com.ltf.mytoolslibrary.viewbase.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ltf.mytoolslibrary.R;
import com.ltf.mytoolslibrary.viewbase.utils.AutoUtils;

/**
 * 仿IOS的弹出框 返回传进来的VIew
 */
public class UIAlertCustomBackView extends Dialog {

    private Context context;
    private int layout;
    private onUIAlertCustomBackViewLisnner clickListenerInterface;
    public UIAlertCustomBackView(Context context, int layout, onUIAlertCustomBackViewLisnner mUIAlertCustomBackView) {
        super(context, R.style.UIAlertViewStyle);
        this.context = context;
        this.layout = layout;
        this.clickListenerInterface = mUIAlertCustomBackView;
    }

	public interface onUIAlertCustomBackViewLisnner {
        void onUIAlertCustomBackView(Dialog dialog, View v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inite();
    }

    public void inite() {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(this.layout, null);
        AutoUtils.auto(view);
        setContentView(view);

        if(clickListenerInterface != null){
            clickListenerInterface.onUIAlertCustomBackView(this,view);
        }

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics();

        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);
    }
}
