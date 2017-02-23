package com.ltf.mytoolslibrary.viewbase.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ltf.mytoolslibrary.R;
import com.ltf.mytoolslibrary.viewbase.utils.ScreenUtils;

/**
 * 仿IOS的弹出框
 */
public class UIAlertView extends Dialog {

    private Context context;
    private String title;
    private String message;
    private String buttonLeftText;
    private String buttonRightText;
    private ClickListenerInterface clickListenerInterface;
    private int s = Gravity.CENTER;

    public UIAlertView(Context context, String title, String message,
                       String buttonLeftText, String buttonRightText) {
        super(context, R.style.UIAlertViewStyle);

        this.context = context;
        this.title = title;
        this.message = message;
        this.buttonLeftText = buttonLeftText;
        this.buttonRightText = buttonRightText;
    }

    public UIAlertView(int s, Context context, String title, String message,
                       String buttonLeftText, String buttonRightText) {
        super(context, R.style.UIAlertViewStyle);
        this.s = s;
        this.context = context;
        this.title = title;
        this.message = message;
        this.buttonLeftText = buttonLeftText;
        this.buttonRightText = buttonRightText;
    }

	public interface ClickListenerInterface {
        void doLeft();

        void doRight();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inite();
    }

    public void inite() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ui_alert_view, null);
        setContentView(view);

        TextView tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        ScrollView sc = (ScrollView) view.findViewById(R.id.ScrollView);
        if(s == Gravity.CENTER){
            sc.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        }else{
            sc.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenW((Activity) context)*1/3));
        }
        tvMessage.setGravity(s);
        TextView tvLeft = (TextView) view.findViewById(R.id.tvBtnLeft1);
        TextView tvRight = (TextView) view.findViewById(R.id.tvBtnRight1);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);

        if ("".equals(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }

        tvMessage.setText(message);

        if(TextUtils.isEmpty(buttonLeftText)){
            tvLeft.setVisibility(View.GONE);
        }else{
            tvLeft.setVisibility(View.VISIBLE);
        }
        tvLeft.setText(buttonLeftText);
        tvRight.setText(buttonRightText);

        tvLeft.setOnClickListener(new clickListener());
        tvRight.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics();

        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            int id = v.getId();
            if (id == R.id.tvBtnLeft1) {
                clickListenerInterface.doLeft();
            } else if (id == R.id.tvBtnRight1) {
                clickListenerInterface.doRight();
            }
        }
    }
}
