package com.ltf.mytoolslibrary.viewbase.utils.autoImage;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ltf.mytoolslibrary.R;
import com.ltf.mytoolslibrary.viewbase.utils.AutoUtils;
import com.ltf.mytoolslibrary.viewbase.utils.PicassoUtil;

import java.util.List;

/**
 * 作者：${李堂飞} on 2016/12/9 0009 15:50
 * 邮箱：1195063924@qq.com
 * 注解: 横向动态添加图片
 */
public class TransverseAutoImageUtils {

    private static TransverseAutoImageUtils mTransverseAutoImageUtils;
    public static TransverseAutoImageUtils getTransverseAutoImageUtils(){
        if(mTransverseAutoImageUtils == null){

        }mTransverseAutoImageUtils = new TransverseAutoImageUtils();
        return mTransverseAutoImageUtils;
    }
    private TransverseAutoImageUtils(){

    }

    /**
     * 横向添加图片 最后一个图片用点点代替
     * @param activity
     * @param auto_LinearLayout
     * @param list
     */
    public void auto_LinearLayout(Activity activity,LinearLayout auto_LinearLayout, List<String> list){//每个item中间的商品图片
        auto_LinearLayout.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            ImageView iv = new ImageView(activity);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setClickable(false);
            iv.setFocusable(false);
            iv.setFocusableInTouchMode(false);
            iv.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));

            if(i != 0){
                View v2 = new View(activity);
                LinearLayout.MarginLayoutParams lp = new LinearLayout.MarginLayoutParams(
                        AutoUtils.getDisplayWidthValue(6), LinearLayout.LayoutParams.MATCH_PARENT);
                lp.leftMargin = 0;
                lp.rightMargin = 0;
                lp.topMargin = 0;
                lp.bottomMargin = 0;
                v2.setBackgroundColor(ContextCompat.getColor(activity,R.color.white));
                v2.setLayoutParams(lp);
                auto_LinearLayout.addView(v2);
            }

            if(i == 2 && list.size() > 2){
                iv.setImageResource(R.drawable.order_more);
                auto_LinearLayout.addView(iv);
                return;
            }
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                    AutoUtils.getDisplayWidthValue(112),AutoUtils.getDisplayHeightValue(118));
            lp.leftMargin = 0;
            lp.rightMargin = AutoUtils.getDisplayHeightValue(8);
            lp.topMargin = 0;
            lp.bottomMargin = 0;
//	        iv.setLayoutParams(lp);
            PicassoUtil.getInstantiation().setLoad_error(R.mipmap.default_image).setLoad_image(R.mipmap.default_image).
                    onWidgetImage(activity, list.get(i), iv);
            auto_LinearLayout.addView(iv,lp);
        }
    }
}
