package com.ltf.mytoolslibrary.viewbase.utils.autoStaue;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ltf.mytoolslibrary.R;
import com.ltf.mytoolslibrary.viewbase.utils.AutoUtils;
import com.ltf.mytoolslibrary.viewbase.utils.ScreenUtils;

import java.util.List;


/**
 * 作者：${李堂飞} on 2016/11/12 0012 16:18
 * 邮箱：1195063924@qq.com
 * 注解: 自动订单生成顶部各种选择状态
 */
public class AutoStaueUtils {

    private static AutoStaueUtils mAutoStaueUtils;
    public static AutoStaueUtils getAutoStaueUtils(){
        if(mAutoStaueUtils == null){
            mAutoStaueUtils = new AutoStaueUtils();
        }
        return mAutoStaueUtils;
    }

    public void startAutoStaueUtils(final Activity activity, final RadioGroup rg_type, List<String> timeTitle, final onBackAutoStaueUtilsLiserner m){
        if(lp == null){
            initMarginLayoutParams();
        }
        rg_type.removeAllViews();
        for (int i = 0; i < timeTitle.size(); i++) {
//			View v = LayoutInflater.from(this).inflate(R.layout.item_tiem_select_title, null);
            RadioButton tempButton = new RadioButton(activity);
            tempButton.setBackgroundResource(R.drawable.rb_errand_mission_hall);   // 设置RadioButton的背景图片
            tempButton.setButtonDrawable(android.R.color.transparent);           // 设置按钮的样式
            tempButton.setText(timeTitle.get(i));
            tempButton.setGravity(Gravity.CENTER);
            tempButton.setPadding(4,
                    4,
                    4,
                    4);                 // 设置文字距离按钮四周的距离
            tempButton.setTextColor(ContextCompat.getColor(activity,R.color.main_tab_textcolor_selector));
            tempButton.setId(i);
            tempButton.setTextSize(14);
            if(i == 0){
                tempButton.setChecked(true);
                tempButton.setTextColor(ContextCompat.getColor(activity,R.color.title_color));
            }
            if(timeTitle.size() >= 4){
                rg_type.addView(tempButton, ScreenUtils.getScreenW(activity)/4, AutoUtils.getDisplayHeightValue(65));
            }else{
                rg_type.addView(tempButton, ScreenUtils.getScreenW(activity)/timeTitle.size(), AutoUtils.getDisplayHeightValue(65));
            }
        }

        rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                RadioButton tempButtons = (RadioButton) rg_type.getChildAt(checkedId); // 通过RadioGroup的findViewById方法，找到ID为checkedID的RadioButton
                // 以下就可以对这个RadioButton进行处理了
                tempButtons.setTextColor(ContextCompat.getColor(activity,R.color.title_color));
                for (int i = 0; i < group.getChildCount(); i++) {
                    if(checkedId != i){
                        ((TextView) group.getChildAt(i)).setTextColor(ContextCompat.getColor(activity,R.color.text_666));
                    }
                }
                if(m != null){
                    m.onBackAutoStaueUtils(checkedId);
                }
            }
        });
    }

    private ViewGroup.MarginLayoutParams lp;
    /**初始化各个类型值之间的间距**/
    private void initMarginLayoutParams(){
        lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 0;
        lp.rightMargin = 0;
        lp.topMargin = 0;
        lp.bottomMargin = 0;
    }

    public interface onBackAutoStaueUtilsLiserner{
        void onBackAutoStaueUtils(int selectPosition);
    }
}
